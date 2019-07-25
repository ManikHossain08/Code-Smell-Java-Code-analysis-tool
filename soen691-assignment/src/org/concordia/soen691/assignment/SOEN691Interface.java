package org.concordia.soen691.assignment;

import java.util.ArrayList;
import org.eclipse.jdt.core.dom.CompilationUnit;

/*
 * @version 1.0
 * @since 1.0
 * <p>
 * This is the interface to wrap the Eclipse AST Visitor. It is required to be able to have access to the Compilation Unit object and the response array list
 * </p>
 */
public interface SOEN691Interface {
	public void setCompilationUnit(CompilationUnit cu);
	public CompilationUnit getCompilationUnit();
	public void setCollector(ArrayList<String> collector);
	public ArrayList<String> getCollector();
}
