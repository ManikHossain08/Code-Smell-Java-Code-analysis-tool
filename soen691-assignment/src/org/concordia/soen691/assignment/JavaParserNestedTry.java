package org.concordia.soen691.assignment;

import java.util.ArrayList;
import java.util.List;


import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


public class JavaParserNestedTry extends VoidVisitorAdapter<ArrayList<String>>{
	public JavaParserNestedTry() {
		
	}
	
	@Override
	public void visit(TryStmt trystmt,ArrayList<String> collector) {
		int beginline = 0;
		int endline = 0;
		String classname = "";
		CompilationUnit rootcu = trystmt.findCompilationUnit().get();		 
        if(rootcu.findFirst(ClassOrInterfaceDeclaration.class).isPresent()) {
			classname = rootcu.findFirst(ClassOrInterfaceDeclaration.class).get().getNameAsString();
		}
		else if(rootcu.findFirst(EnumDeclaration.class).isPresent()){
			classname = rootcu.findFirst(EnumDeclaration.class).get().getNameAsString();
		}  	
 	
	   
		List<TryStmt> trys = trystmt.findAll(TryStmt.class);
		if(trys.size() > 1) {					      	        		
			 
			if(trystmt.getTryBlock().findAll(TryStmt.class).size() > 0) {
				if(trystmt.getTryBlock().getBegin().isPresent()) {
					beginline = trystmt.getTryBlock().getBegin().get().line;
				}			 
				if(trystmt.getTryBlock().getEnd().isPresent()) {
					endline = trystmt.getTryBlock().getEnd().get().line;
				}		
				collector.add("Nested Try in Class " + classname + ", Line "+beginline + " - "+ endline 
						+ " - Detecting another set of trys in Try Block");	
			}
			
			if(trystmt.getCatchClauses().isNonEmpty()) {
				NodeList<CatchClause> catches = trystmt.getCatchClauses();
				for(CatchClause cc: catches) {
					if(cc.findAll(TryStmt.class).size() > 0) {
						if(cc.getBegin().isPresent()) {
							beginline = cc.getBegin().get().line;
						}			 
						if(cc.getEnd().isPresent()) {
							endline = cc.getEnd().get().line;
						}
						collector.add("Nested Try in Class " + classname + ", Line "+ beginline + " - "+ endline 
								+ " - Detecting another set of trys in Catch Clauses when catching Exception " + cc.getParameter().getTypeAsString());	
					}
				}				
			}
			
			if(trystmt.getFinallyBlock().isPresent()) {
				if(trystmt.getFinallyBlock().get().findAll(TryStmt.class).size() > 0) {
					if(trystmt.getFinallyBlock().get().getBegin().isPresent()) {
						beginline = trystmt.getFinallyBlock().get().getBegin().get().line;
					}			 
					if(trystmt.getFinallyBlock().get().getEnd().isPresent()) {
						endline = trystmt.getFinallyBlock().get().getEnd().get().line;
					}
					collector.add("Nested Try in Class " + classname + ", Line "+beginline + " - "+ endline 
							+ " - Detecting another set of trys in Finally Block");	
				}
			}
			
		}
		
		
		/*
		 * Pattern Case Two: A try statement contains methods which contains try statement
		 */		 
		/*
		 * if(!trystmt.findAll(MethodCallExpr.class).isEmpty()) { for(MethodCallExpr
		 * methodcall: trystmt.findAll(MethodCallExpr.class)) { MethodDeclaration
		 * relateddeclaration = new MethodDeclaration(); Optional<MethodDeclaration> md
		 * = null; try { md = methodcall.resolve().toAst(); } catch
		 * (UnsolvedSymbolException e) { System.out.println(e.getMessage()); }
		 * 
		 * if(md.isPresent()) { relateddeclaration = md.get(); }
		 * if(relateddeclaration.findAll(TryStmt.class).isEmpty() == false) {
		 * collector.add("Nested Try in Class " + classname + ": Line "+beginline +
		 * " - "+ endline + ". Reason: Try contains method which contains try"); } } }
		 */
		
	}
	
	 
}
