package test.org.concordia.soen691.assignment;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import org.apache.commons.io.FileUtils;
import org.concordia.soen691.assignment.JDTDestructiveWrapping;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.junit.BeforeClass;
import org.junit.Test;

/*
 * @version 1.0
 * @since 1.0
 * <p>
 * This is the class for testing Destructive Wrapping exception handling anti pattern.
 * </p>
 */

public class JDTDestructiveWrappingTest {

	static ASTParser parser=null;
	@SuppressWarnings("deprecation")
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		Hashtable<String, String> options = JavaCore.getOptions();
		parser.setCompilerOptions(options);

	}

	@Test
	public void testNoCaluseInFile() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;

		try {
			file = new File(classLoader.getResource("Test4.java").getFile());
		} catch (NullPointerException e) {
			fail("Input file not found");
		}
		String encoding = null;
		String[] sources = {file.getParent()+"/"};
		String[] encodings = {"UTF-8"};
		String rtJAR = "/usr/lib/jvm/java-8-oracle/jre/lib/rt.jar";
		String[] classpath = {".", rtJAR};
		parser.setEnvironment(classpath, sources, encodings, true);
		parser.setUnitName("Test4.java");

		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);

		try {
			parser.setSource(FileUtils.readFileToString(file, encoding).toCharArray());
		} catch (IOException e) {
			System.out.println("\nFile read error: " + file.getPath());
		}

		org.eclipse.jdt.core.dom.CompilationUnit cuJDT = (org.eclipse.jdt.core.dom.CompilationUnit) parser
				.createAST(null);

		JDTDestructiveWrapping wrapper = new JDTDestructiveWrapping();

		wrapper.setCompilationUnit(cuJDT);
		cuJDT.accept(wrapper);
		assertEquals(0, wrapper.getCollector().size());
	}


	@Test
	public void testOneDestructiveWrappingInstance() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;

		try {
			file = new File(classLoader.getResource("Test5.java").getFile());
		} catch (NullPointerException e) {
			fail("Input file not found");
		}
		String encoding = null;
		String[] sources = {file.getParent()+"/"};
		String[] encodings = {"UTF-8"};
		String rtJAR = "/usr/lib/jvm/java-8-oracle/jre/lib/rt.jar";
		String[] classpath = {".", rtJAR};
		parser.setEnvironment(classpath, sources, encodings, true);
		parser.setUnitName("Test4.java");

		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);

		try {
			parser.setSource(FileUtils.readFileToString(file, encoding).toCharArray());
		} catch (IOException e) {
			System.out.println("\nFile read error: " + file.getPath());
		}

		org.eclipse.jdt.core.dom.CompilationUnit cuJDT = (org.eclipse.jdt.core.dom.CompilationUnit) parser
				.createAST(null);

		JDTDestructiveWrapping wrapper = new JDTDestructiveWrapping();

		wrapper.setCompilationUnit(cuJDT);
		cuJDT.accept(wrapper);
		assertEquals(1, wrapper.getCollector().size());
	}

	@Test
	public void testNestedTryNoDestructiveWrappingInstance() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;

		try {
			file = new File(classLoader.getResource("Test6.java").getFile());
		} catch (NullPointerException e) {
			fail("Input file not found");
		}
		String encoding = null;
		String[] sources = {file.getParent()+"/"};
		String[] encodings = {"UTF-8"};
		String rtJAR = "/usr/lib/jvm/java-8-oracle/jre/lib/rt.jar";
		String[] classpath = {".", rtJAR};
		parser.setEnvironment(classpath, sources, encodings, true);
		parser.setUnitName("Test4.java");

		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);

		try {
			parser.setSource(FileUtils.readFileToString(file, encoding).toCharArray());
		} catch (IOException e) {
			System.out.println("\nFile read error: " + file.getPath());
		}

		org.eclipse.jdt.core.dom.CompilationUnit cuJDT = (org.eclipse.jdt.core.dom.CompilationUnit) parser
				.createAST(null);

		JDTDestructiveWrapping wrapper = new JDTDestructiveWrapping();

		wrapper.setCompilationUnit(cuJDT);
		cuJDT.accept(wrapper);
		assertEquals(0, wrapper.getCollector().size());
	}

	@Test
	public void testNestedTryInnerDestructiveWrappingInstance() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;

		try {
			file = new File(classLoader.getResource("Test7.java").getFile());
		} catch (NullPointerException e) {
			fail("Input file not found");
		}
		String encoding = null;
		String[] sources = {file.getParent()+"/"};
		String[] encodings = {"UTF-8"};
		String rtJAR = "/usr/lib/jvm/java-8-oracle/jre/lib/rt.jar";
		String[] classpath = {".", rtJAR};
		parser.setEnvironment(classpath, sources, encodings, true);
		parser.setUnitName("Test4.java");

		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);

		try {
			parser.setSource(FileUtils.readFileToString(file, encoding).toCharArray());
		} catch (IOException e) {
			System.out.println("\nFile read error: " + file.getPath());
		}

		org.eclipse.jdt.core.dom.CompilationUnit cuJDT = (org.eclipse.jdt.core.dom.CompilationUnit) parser
				.createAST(null);

		JDTDestructiveWrapping wrapper = new JDTDestructiveWrapping();

		wrapper.setCompilationUnit(cuJDT);
		cuJDT.accept(wrapper);
		assertEquals(1, wrapper.getCollector().size());
	}

	@Test
	public void testNestedTryOuterDestructiveWrappingInstance() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;

		try {
			file = new File(classLoader.getResource("Test8.java").getFile());
		} catch (NullPointerException e) {
			fail("Input file not found");
		}
		String encoding = null;
		String[] sources = {file.getParent()+"/"};
		String[] encodings = {"UTF-8"};
		String rtJAR = "/usr/lib/jvm/java-8-oracle/jre/lib/rt.jar";
		String[] classpath = {".", rtJAR};
		parser.setEnvironment(classpath, sources, encodings, true);
		parser.setUnitName("Test4.java");

		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);

		try {
			parser.setSource(FileUtils.readFileToString(file, encoding).toCharArray());
		} catch (IOException e) {
			System.out.println("\nFile read error: " + file.getPath());
		}

		org.eclipse.jdt.core.dom.CompilationUnit cuJDT = (org.eclipse.jdt.core.dom.CompilationUnit) parser
				.createAST(null);

		JDTDestructiveWrapping wrapper = new JDTDestructiveWrapping();

		wrapper.setCompilationUnit(cuJDT);
		cuJDT.accept(wrapper);
		assertEquals(1, wrapper.getCollector().size());
	}

	@Test
	public void testNestedTryInnerAndOuterDestructiveWrappingInstance() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;

		try {
			file = new File(classLoader.getResource("Test9.java").getFile());
		} catch (NullPointerException e) {
			fail("Input file not found");
		}
		String encoding = null;
		String[] sources = {file.getParent()+"/"};
		String[] encodings = {"UTF-8"};
		String rtJAR = "/usr/lib/jvm/java-8-oracle/jre/lib/rt.jar";
		String[] classpath = {".", rtJAR};
		parser.setEnvironment(classpath, sources, encodings, true);
		parser.setUnitName("Test4.java");

		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);

		try {
			parser.setSource(FileUtils.readFileToString(file, encoding).toCharArray());
		} catch (IOException e) {
			System.out.println("\nFile read error: " + file.getPath());
		}

		org.eclipse.jdt.core.dom.CompilationUnit cuJDT = (org.eclipse.jdt.core.dom.CompilationUnit) parser
				.createAST(null);

		JDTDestructiveWrapping wrapper = new JDTDestructiveWrapping();

		wrapper.setCompilationUnit(cuJDT);
		cuJDT.accept(wrapper);
		assertEquals(2, wrapper.getCollector().size());
	}

	@Test
	public void testNestedTryInnerAndOuterOneDestructiveWrappingInstance() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;

		try {
			file = new File(classLoader.getResource("Test10.java").getFile());
		} catch (NullPointerException e) {
			fail("Input file not found");
		}
		String encoding = null;
		String[] sources = {file.getParent()+"/"};
		String[] encodings = {"UTF-8"};
		String rtJAR = "/usr/lib/jvm/java-8-oracle/jre/lib/rt.jar";
		String[] classpath = {".", rtJAR};
		parser.setEnvironment(classpath, sources, encodings, true);
		parser.setUnitName("Test4.java");

		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);

		try {
			parser.setSource(FileUtils.readFileToString(file, encoding).toCharArray());
		} catch (IOException e) {
			System.out.println("\nFile read error: " + file.getPath());
		}

		org.eclipse.jdt.core.dom.CompilationUnit cuJDT = (org.eclipse.jdt.core.dom.CompilationUnit) parser
				.createAST(null);

		JDTDestructiveWrapping wrapper = new JDTDestructiveWrapping();

		wrapper.setCompilationUnit(cuJDT);
		cuJDT.accept(wrapper);
		assertEquals(1, wrapper.getCollector().size());
	}

	@Test
	public void testComplexCase1DestructiveWrappingInstance() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;

		try {
			file = new File(classLoader.getResource("Test.java").getFile());
		} catch (NullPointerException e) {
			fail("Input file not found");
		}
		String encoding = null;
		String[] sources = {file.getParent()+"/"};
		String[] encodings = {"UTF-8"};
		String rtJAR = "/usr/lib/jvm/java-8-oracle/jre/lib/rt.jar";
		String[] classpath = {".", rtJAR};
		parser.setEnvironment(classpath, sources, encodings, true);
		parser.setUnitName("Test4.java");

		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);

		try {
			parser.setSource(FileUtils.readFileToString(file, encoding).toCharArray());
		} catch (IOException e) {
			System.out.println("\nFile read error: " + file.getPath());
		}

		org.eclipse.jdt.core.dom.CompilationUnit cuJDT = (org.eclipse.jdt.core.dom.CompilationUnit) parser
				.createAST(null);

		JDTDestructiveWrapping wrapper = new JDTDestructiveWrapping();

		wrapper.setCompilationUnit(cuJDT);
		cuJDT.accept(wrapper);
		assertEquals(3, wrapper.getCollector().size());
	}

	@Test
	public void testComplexCase2DestructiveWrappingInstance() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;

		try {
			file = new File(classLoader.getResource("Test2.java").getFile());
		} catch (NullPointerException e) {
			fail("Input file not found");
		}
		String encoding = null;
		String[] sources = {file.getParent()+"/"};
		String[] encodings = {"UTF-8"};
		String rtJAR = "/usr/lib/jvm/java-8-oracle/jre/lib/rt.jar";
		String[] classpath = {".", rtJAR};
		parser.setEnvironment(classpath, sources, encodings, true);
		parser.setUnitName("Test4.java");

		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);

		try {
			parser.setSource(FileUtils.readFileToString(file, encoding).toCharArray());
		} catch (IOException e) {
			System.out.println("\nFile read error: " + file.getPath());
		}

		org.eclipse.jdt.core.dom.CompilationUnit cuJDT = (org.eclipse.jdt.core.dom.CompilationUnit) parser
				.createAST(null);

		JDTDestructiveWrapping wrapper = new JDTDestructiveWrapping();

		wrapper.setCompilationUnit(cuJDT);
		cuJDT.accept(wrapper);
		assertEquals(2, wrapper.getCollector().size());
	}

}
