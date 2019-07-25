package org.concordia.soen691.assignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.github.javaparser.ast.visitor.VoidVisitor;

/*
 * @version 1.0
 * @since 1.0
 * <p>
 * This is the configuration loader and resource initializer class.
 * </p>
 */

public class ConfigurationLoader {

	static Properties props = null;
	static final String propFileName = "config.properties";
	static List<VoidVisitor<ArrayList<String>>> enabledVisitorList = null;
	static List<SOEN691JDTCollector> enabledJDTASTVisitorList = null;

	private ConfigurationLoader() {
	}

	public static void init() throws FileNotFoundException, IOException, URISyntaxException {

		if (props == null) {
			props = new Properties();
		}

		String rootPath = new File(
				ConfigurationLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
		String fullConfigName = rootPath + File.separatorChar + propFileName;

		FileInputStream is = new FileInputStream(fullConfigName);
		if (is != null) {
			props.load(is);
			is.close();
		}
		buildEnabledVisitorList();
	}

	public static Properties getConfigurationProperties() {
		return props;
	}

	public static void init(String optionValue) throws IOException {
		if (props == null) {
			props = new Properties();
		}
		FileInputStream is = new FileInputStream(optionValue);
		if (is != null) {
			props.load(is);
			is.close();
		}
		buildEnabledVisitorList();
	}

	public static List<VoidVisitor<ArrayList<String>>> getEnabledVisitors(){
		return enabledVisitorList;

	}

	public static List<SOEN691JDTCollector> getJDTASTEnabledVisitors() {
		return enabledJDTASTVisitorList;
	}

	private static void buildEnabledVisitorList() {

		if (enabledVisitorList == null) {
			enabledVisitorList = new ArrayList<>();
			System.out.println("Creating enabledVisitor ArrayList from configuration file...");
		}

		if (enabledJDTASTVisitorList == null) {
			enabledJDTASTVisitorList = new ArrayList<>();
			System.out.println("Creating enabledJDTASTVisitor ArrayList from configuration file...");
		}

		String JDT_DESTRUCTIVE_WRAPPING = props.getProperty("pattern.jdt_destructive_wrapping", "false");
		if (JDT_DESTRUCTIVE_WRAPPING.equalsIgnoreCase("true")) {
			enabledJDTASTVisitorList.add(VisitorBuilder.createVisitorDestructiveWrapping());
			System.out.println("\tEnabling pattern " + "JDT_DESTRUCTIVE_WRAPPING");
		}

		String JavaParser_Nested_Try = props.getProperty("pattern.javaparser_nested_try","false");
		if(JavaParser_Nested_Try.equalsIgnoreCase("true")) {
			enabledVisitorList.add(VisitorBuilder.createVisitorNestedTry());
			System.out.println("\tEnabling pattern " + "JavaParser_Nested_Try");
		}
        String JavaParser_Destructive_Wrapping = props.getProperty("pattern.javaparser_destructive_wrapping","false");
		if(JavaParser_Destructive_Wrapping.equalsIgnoreCase("true")) {
			enabledVisitorList.add(VisitorBuilder.createVisitorJavaParserDestructiveWrapping());
			System.out.println("\tEnabling pattern " + "JavaParser_Destructive_Wrapping");
		}
		String JavaParser_Over_Catch = props.getProperty("pattern.javaparser_overcatch", "false");
		if (JavaParser_Over_Catch.equalsIgnoreCase("true")) {
			CallGraphLoader loader = new CallGraphLoader (props.getProperty("source_jar"));
			HashMap <String, List <Method>> callgraph = loader.read(props.getProperty("call_graph"));
			if (callgraph.size() == 0) {
				callgraph = loader.loadCallGraph(props.getProperty("source_folder"));
				loader.store(props.getProperty("call_graph"), callgraph);
				System.out.println("Generated call graph of size: "+callgraph.size());
			} else {
				System.out.println("Successfully loaded call graph");
			}
			enabledVisitorList.add(VisitorBuilder.createVisitorJavaParserOverCatch(props.getProperty("source_jar"), callgraph));
			System.out.println("\tEnabling pattern " + "JavaParser_Over_Catch");
		}
		int visitorsFound = enabledVisitorList.size() + enabledJDTASTVisitorList.size();
		System.out.println("Found " + visitorsFound + " patterns enabled");
	}
}
