package test.org.concordia.soen691.assignment;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.concordia.soen691.assignment.JavaParserNestedTry;
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

public class JavaParserNestedTryTest {
	private JavaParserNestedTry nestTryProbe = new JavaParserNestedTry();
	private ArrayList<String> responseElement = new ArrayList<String>();
	
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
		responseElement.clear();
	}

	@Test
	public void noTry() {
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
		
		nestTryProbe.visit(cu, responseElement);
		assertEquals(0,responseElement.size());
	}
	
	@Test
	public void noNestedTry_noCalledMethod() {
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
		
		nestTryProbe.visit(cu, responseElement);
		assertEquals(0,responseElement.size());
	}
	
	@Test
	public void noNestedTry_InCalledMethod() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;	
		try {
			file = new File(classLoader.getResource("Test11.java").getFile());
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
		
		nestTryProbe.visit(cu, responseElement);
		assertEquals(0,responseElement.size());
	}
	
	@Test
	public void NestedTryInCatch() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;	
		try {
			file = new File(classLoader.getResource("Test10.java").getFile());
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
		
		nestTryProbe.visit(cu, responseElement);
		assertEquals(1,responseElement.size());
	}
	
	@Test
	public void NestTryInTry() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;	
		try {
			file = new File(classLoader.getResource("Test14.java").getFile());
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
		
		nestTryProbe.visit(cu, responseElement);
		assertEquals(1,responseElement.size());
	}
	
	@Test
	public void NestTryinTryBlock() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;	
		try {
			file = new File(classLoader.getResource("Test12.java").getFile());
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
		
		nestTryProbe.visit(cu, responseElement);
		assertEquals(1,responseElement.size());
	}
	
	@Test
	public void MultipleNestTry() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;	
		try {
			file = new File(classLoader.getResource("Test13.java").getFile());
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
		
		nestTryProbe.visit(cu, responseElement);
		assertEquals(1,responseElement.size());
	}
	

	@Test
	public void NestedTry_twocatch() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;	
		try {
			file = new File(classLoader.getResource("Test15.java").getFile());
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
		
		nestTryProbe.visit(cu, responseElement);
		assertEquals(1,responseElement.size());
	}
	
	@Test
	public void MultipleNestedTry_tryfinal() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;	
		try {
			file = new File(classLoader.getResource("Test16.java").getFile());
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
		
		nestTryProbe.visit(cu, responseElement);
		assertEquals(2,responseElement.size());
	}

}
