package test.org.concordia.soen691.assignment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.concordia.soen691.assignment.JavaParserDestructiveWrapping;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;


public class JavaParserDestructiveWrappingTest {
	private JavaParserDestructiveWrapping JavaParserDW = new JavaParserDestructiveWrapping();
	private ArrayList<String> foundActualNumberOfDW = new ArrayList<String>();
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//when running on different system, need to update according to config.properties
		String sourceDir = "resources";
		String rtJAR = "/usr/lib/jvm/java-8-oracle/jre/lib/rt.jar";
		
		TypeSolver typeSolver = new CombinedTypeSolver();
		((CombinedTypeSolver) typeSolver).add(new ReflectionTypeSolver());
		
		((CombinedTypeSolver) typeSolver).add(new JavaParserTypeSolver(sourceDir));

		JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
		JavaParser.getStaticConfiguration().setSymbolResolver(symbolSolver);
		
		if (rtJAR.equals("")) {
			System.out.println("JRE Runtime JAR missing or not properly set in configuration file. Please set rt_jar property and try again...");
			System.exit(-1);
		}
	}
	
	@After
	public void tearDown() throws Exception {
		foundActualNumberOfDW.clear();
	}
	
	@Test
	public void noCatchClause() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;	
		try {
			file = new File(classLoader.getResource("Test3.java").getFile());
		} catch (NullPointerException e) {
			fail("Input file not found");
		}
		CompilationUnit cu = null;
		try {
			cu = JavaParser.parse(file);
		} catch (ParseProblemException | FileNotFoundException e) {
			e.printStackTrace();
			fail("Parse error or input file not found");
		}
		
		JavaParserDW.visit(cu, foundActualNumberOfDW);
		assertEquals(0,foundActualNumberOfDW.size());
	}
	
	@Test
	public void oneDestructiveWrapping() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;	
		try {
			file = new File(classLoader.getResource("Test5.java").getFile());
		} catch (NullPointerException e) {
			fail("Input file not found");
		}
		CompilationUnit cu = null;
		try {
			cu = JavaParser.parse(file);
		} catch (ParseProblemException | FileNotFoundException e) {
			e.printStackTrace();
			fail("Parse error or input file not found");
		}
		
		JavaParserDW.visit(cu, foundActualNumberOfDW);
		assertEquals(1,foundActualNumberOfDW.size());
	}
	
	@Test
	public void NestedCatchInCatchClauseButNoDW() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;	
		try {
			file = new File(classLoader.getResource("Test6.java").getFile());
		} catch (NullPointerException e) {
			fail("Input file not found");
		}
		CompilationUnit cu = null;
		try {
			cu = JavaParser.parse(file);
		} catch (ParseProblemException | FileNotFoundException e) {
			e.printStackTrace();
			fail("Parse error or input file not found");
		}
		
		JavaParserDW.visit(cu, foundActualNumberOfDW);
		assertEquals(0,foundActualNumberOfDW.size());
	}
	
	@Test
	public void NestedCatchInCatchClauseWithDW() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;	
		try {
			file = new File(classLoader.getResource("Test7.java").getFile());
		} catch (NullPointerException e) {
			fail("Input file not found");
		}
		CompilationUnit cu = null;
		try {
			cu = JavaParser.parse(file);
		} catch (ParseProblemException | FileNotFoundException e) {
			e.printStackTrace();
			fail("Parse error or input file not found");
		}
		
		JavaParserDW.visit(cu, foundActualNumberOfDW);
		assertEquals(1,foundActualNumberOfDW.size());
	}
	
	@Test
	public void NestedTryOuterInCatchClauseWithDW() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;	
		try {
			file = new File(classLoader.getResource("Test8.java").getFile());
		} catch (NullPointerException e) {
			fail("Input file not found");
		}
		CompilationUnit cu = null;
		try {
			cu = JavaParser.parse(file);
		} catch (ParseProblemException | FileNotFoundException e) {
			e.printStackTrace();
			fail("Parse error or input file not found");
		}
		
		JavaParserDW.visit(cu, foundActualNumberOfDW);
		assertEquals(1,foundActualNumberOfDW.size());
	}
	@Test
	public void NestedTryInnerAndOuterInCatchClauseWithDW() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;	
		try {
			file = new File(classLoader.getResource("Test9.java").getFile());
		} catch (NullPointerException e) {
			fail("Input file not found");
		}
		CompilationUnit cu = null;
		try {
			cu = JavaParser.parse(file);
		} catch (ParseProblemException | FileNotFoundException e) {
			e.printStackTrace();
			fail("Parse error or input file not found");
		}
		
		JavaParserDW.visit(cu, foundActualNumberOfDW);
		assertEquals(2,foundActualNumberOfDW.size());
	}
	
	
	@Test
	public void TestConsideingBigProjecCatchClauseWithThreeDW() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;	
		try {
			file = new File(classLoader.getResource("Test.java").getFile());
		} catch (NullPointerException e) {
			fail("Input file not found");
		}
		CompilationUnit cu = null;
		try {
			cu = JavaParser.parse(file);
		} catch (ParseProblemException | FileNotFoundException e) {
			e.printStackTrace();
			fail("Parse error or input file not found");
		}
		
		JavaParserDW.visit(cu, foundActualNumberOfDW);
		assertEquals(3,foundActualNumberOfDW.size());
	}
	@Test
	public void TestConsideingBigProjecCatchClauseWithTwoDW() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;	
		try {
			file = new File(classLoader.getResource("Test2.java").getFile());
		} catch (NullPointerException e) {
			fail("Input file not found");
		}
		CompilationUnit cu = null;
		try {
			cu = JavaParser.parse(file);
		} catch (ParseProblemException | FileNotFoundException e) {
			e.printStackTrace();
			fail("Parse error or input file not found");
		}
		
		JavaParserDW.visit(cu, foundActualNumberOfDW);
		assertEquals(2,foundActualNumberOfDW.size());
	}

}
