package org.concordia.soen691.assignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

/*
 * @version 1.0
 * @since 1.0
 * <p>
 * This is the top level bug detection class in the framework. It parses the file(s) to create AST and applies the enabled bug pattern visitors.
 * It also has a method for printing the raw results.
 * </p>
 */

public class BugFinder {
	/*
	 * The resultArray is an array of non-empty BugFinderResponse objects. A
	 * BugFinderResponse object is not empty if it contains at least one Bug Pattern
	 * incident.
	 */
	List<BugFinderResponse> resultArray = null;
	ASTParser parser = null;
	String[] encodings = {"UTF-8"};
	String rtJAR = "";

	int numberOfFilesParsed = 0;

	public BugFinder() {
		resultArray = new ArrayList<>();

		// define JavaParser parser and resolver
		// we need to define a reflection type resolver to detect java.lang object
		// types.
		TypeSolver typeSolver = new CombinedTypeSolver();
		((CombinedTypeSolver) typeSolver).add(new ReflectionTypeSolver());

		String sourceDir = ConfigurationLoader.getConfigurationProperties().getProperty("source_folder", "/tmp");
		((CombinedTypeSolver) typeSolver).add(new JavaParserTypeSolver(sourceDir));

		JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
		JavaParser.getStaticConfiguration().setSymbolResolver(symbolSolver);

		rtJAR = ConfigurationLoader.getConfigurationProperties().getProperty("rt_jar","");
		if (rtJAR.equals("")) {
			System.out.println("JRE Runtime JAR missing or not properly set in configuration file. Please set rt_jar property and try again...");
			System.exit(-1);
		}
	}

	@SuppressWarnings("deprecation")
	public void parseOneFile(File in) throws FileNotFoundException {

		// JavaParser
		com.github.javaparser.ast.CompilationUnit cu = JavaParser.parse(in);

		// JDT PARSER
		/*
		 * JDT AST parser resolver must be explicitly reset on each file otherwise at some point you may run out of memory
		 * as it appears the ASTParser is not properly cleaned up for new CompilationUnits
		 */
		parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		Hashtable<String, String> options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
		parser.setCompilerOptions(options);

		String encoding = null;
		try {
			parser.setSource(FileUtils.readFileToString(in, encoding).toCharArray());
		} catch (IOException e) {
			System.out.println("\nFile read error: " + in.getPath());
		}

		String[] sources = {in.getParent()+"/"};
		String[] classpath = {".", rtJAR};
		parser.setEnvironment(classpath, sources, encodings, true);
		parser.setUnitName(in.getName());

		//the 3 below need to be set each time for a new file otherwise if you had an error it will never recover from it!
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);

		boolean jdtParserOK = true;
		org.eclipse.jdt.core.dom.CompilationUnit cuJDT = null;
		try {
			cuJDT = (org.eclipse.jdt.core.dom.CompilationUnit) parser
				.createAST(null);
		} catch (Exception e) {
			System.out.println("JDT parser error on file:" + in.getName());
			jdtParserOK = false;
		}

		/*
		 * We iterate through the enabled visitor adapters and apply the pattern
		 * matching overridden by each type element. The response element is an array of
		 * strings storing occurrences of the visited pattern as applicable to the
		 * pattern.
		 */
		System.out.println("\nParsing file: " + in.getPath());
		BugFinderResponse bfResponse = new BugFinderResponse();
		bfResponse.setFileName(in.getPath());

		// iterate through JavaParser objects
		for (VoidVisitor<ArrayList<String>> pattern : ConfigurationLoader.getEnabledVisitors()) {
			ArrayList<String> responseElement = new ArrayList<String>();

			String patternName = pattern.getClass().getSimpleName();
			System.out.println("Searching for pattern " + patternName);

			pattern.visit(cu, responseElement);

			if (responseElement.isEmpty() == false) {
				System.out.println("\t Pattern found");
				bfResponse.getResultMap().put(patternName, responseElement);
			} else {
				System.out.println("\t Pattern not found");
			}
		}

		// iterate through Eclipse JDT AST parser objects
		if (jdtParserOK) {
			for (SOEN691JDTCollector pattern : ConfigurationLoader.getJDTASTEnabledVisitors()) {

				String patternName = pattern.getClass().getSimpleName();
				System.out.println("Searching for pattern " + patternName);
				pattern.setCompilationUnit(cuJDT);
				cuJDT.accept(pattern);

				if (pattern.getCollector().isEmpty() == false) {
					System.out.println("\t Pattern found");
					bfResponse.getResultMap().put(patternName, new ArrayList<String>(pattern.getCollector()));
					pattern.getCollector().clear();
				} else {
					System.out.println("\t Pattern not found");
				}
			}
		}

		if (bfResponse.isEmpty() == false) {
			resultArray.add(bfResponse);
		}
		cu = null; // force garbage collection
		cuJDT = null;
		parser = null;
		numberOfFilesParsed++;
	}

	public void parseDirectoryStructure() {

		String sourceDir = ConfigurationLoader.getConfigurationProperties().getProperty("source_folder", "/tmp");
		File projectDir = new File(sourceDir);
		explore(projectDir);
	}

	/*
	 * Recursively parse the folder structure and for each Java file apply parsing.
	 */
	private void explore(File file) {
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				explore(child);
			}
		} else {
			if (file.getName().endsWith(".java")) {
				try {
					parseOneFile(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void printResults() {
		System.out.println("\n\nPrinting Results:");
		for (BugFinderResponse resultElement : resultArray) {
			System.out.println("\tPatterns found in file: " + resultElement.getFileName());

			Map<String, ArrayList<String>> innerMap = resultElement.getResultMap();
			for (Map.Entry<String, ArrayList<String>> entry : innerMap.entrySet()) {
				System.out.println("\t\tPatterns: " + entry.getKey());
				ArrayList<String> patterns = entry.getValue();
				patterns.forEach(item -> System.out.println("\t\t\t" + item));
			}
		}
		System.out.println("\nTotal number of files parsed: " + numberOfFilesParsed);
	}
}
