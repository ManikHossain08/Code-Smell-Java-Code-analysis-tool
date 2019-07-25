package org.concordia.soen691.assignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.javaparser.ast.visitor.VoidVisitor;

/*
 * @version 1.0
 * @since 1.0
 * <p>
 * This is the Visitor Factory Builder class. It creates a specialized type of Visitor based on the invoked method.
 * </p>
 */

public class VisitorBuilder {

	private VisitorBuilder() {

	}

	public static SOEN691JDTCollector createVisitorDestructiveWrapping() {
		return new JDTDestructiveWrapping();
	}

	public static VoidVisitor<ArrayList<String>> createVisitorNestedTry(){
		return new JavaParserNestedTry();
	}

	public static VoidVisitor<ArrayList<String>> createVisitorJavaParserDestructiveWrapping() {
		return new JavaParserDestructiveWrapping();
	}

	public static VoidVisitor<ArrayList<String>> createVisitorJavaParserOverCatch(String lib, HashMap <String, List <Method>> callgraph) {
		return new JavaParserOverCatch(lib, callgraph);
	}
}
