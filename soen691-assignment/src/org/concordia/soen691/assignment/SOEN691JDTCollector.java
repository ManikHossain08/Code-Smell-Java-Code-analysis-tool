package org.concordia.soen691.assignment;

import java.util.ArrayList;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

/*
 * @version 1.0
 * @since 1.0
 * <p>
 * This is the class for Eclipse JDT Parser collector class which stores responses for a visited pattern in the Collector object.
 * </p>
 */
public class SOEN691JDTCollector extends ASTVisitor implements SOEN691Interface {

	ArrayList<String> collector = null;
	CompilationUnit cu = null;

	public SOEN691JDTCollector() {
		collector = new ArrayList<String>();
	}

	/**
	 * @return the collector
	 */
	public ArrayList<String> getCollector() {
		return collector;
	}

	/**
	 * @param collector the collector to set
	 */
	public void setCollector(ArrayList<String> collector) {
		this.collector = collector;
	}

	/**
	 * @return the cu
	 */
	public CompilationUnit getCompilationUnit() {
		return cu;
	}

	/**
	 * @param cu the cu to set
	 */
	public void setCompilationUnit(CompilationUnit cu) {
		this.cu = cu;
	}
}
