package test.org.concordia.soen691.assignment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.concordia.soen691.assignment.CallGraphBuilder;
import org.concordia.soen691.assignment.JavaParserOverCatch;
import org.concordia.soen691.assignment.Method;
import org.junit.Test;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;

public class JavaParserOverCatchTest {
	
	@Test
	public void testOvercatchChecked() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;

		try {
			file = new File(classLoader.getResource("Test17.java").getFile());
		} catch (NullPointerException e) {
			fail("Input file not found");
		}

		//System.out.println("Input file:\n " + file.toString() + "\n");

		CompilationUnit cu = null;
		try {
			cu = JavaParser.parse(file);
		} catch (ParseProblemException | FileNotFoundException e) {
			e.printStackTrace();
			fail("Parse error or input file not found");
		}
		
		CallGraphBuilder cg = new CallGraphBuilder();
		HashMap <String, List <Method>> callgraph = new HashMap <String,  List <Method>> ();
		cg.visit(cu, callgraph);
		
		if (callgraph.size()<0) {
			fail("Unable to build call graph");
		}
		
		JavaParserOverCatch collector = new JavaParserOverCatch();
		ArrayList<String> responseElement = new ArrayList<String>();

		collector.setCallGraph(callgraph);
		collector.visit(cu, responseElement);
		System.out.println(responseElement);
		
		assertEquals(1, responseElement.size());
	}
	
	@Test
	public void testOvercatchUnchecked() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;

		try {
			file = new File(classLoader.getResource("Test18.java").getFile());
		} catch (NullPointerException e) {
			fail("Input file not found");
		}

		//System.out.println("Input file:\n " + file.toString() + "\n");
		
		CompilationUnit cu = null;
		try {
			cu = JavaParser.parse(file);
		} catch (ParseProblemException | FileNotFoundException e) {
			e.printStackTrace();
			fail("Parse error or input file not found");
		}
		
		CallGraphBuilder cg = new CallGraphBuilder();
		HashMap <String, List <Method>> callgraph = new HashMap <String,  List <Method>> ();
		cg.visit(cu, callgraph);
		
		if (callgraph.size()<0) {
			fail("Unable to build call graph");
		}
		
		JavaParserOverCatch collector = new JavaParserOverCatch();
		ArrayList<String> responseElement = new ArrayList<String>();

		collector.setCallGraph(callgraph);
		collector.visit(cu, responseElement);
		System.out.println(responseElement);
		
		assertEquals(1, responseElement.size());
	}
	
}
