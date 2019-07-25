package org.concordia.soen691.assignment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.Log;

public class JavaParserOverCatch extends VoidVisitorAdapter<ArrayList<String>>{
	
	List <String> thrownExceptions = new ArrayList <String>();
	CombinedTypeSolver typeSolver = new CombinedTypeSolver();
	HashMap <String, List <Method>> callgraph = new HashMap <String,  List <Method>> ();
	
	public JavaParserOverCatch() {
		typeSolver.add(new ReflectionTypeSolver());
	    JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
	    JavaParser.getStaticConfiguration().setSymbolResolver(symbolSolver);
	}
	
	public JavaParserOverCatch(String jar) {
		typeSolver.add(new ReflectionTypeSolver());
		try {
			typeSolver.add(new JarTypeSolver(jar));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
	    JavaParser.getStaticConfiguration().setSymbolResolver(symbolSolver);
	}
	
	public JavaParserOverCatch(String jar, HashMap <String, List <Method>> callgraph) {
		this.callgraph = callgraph;
		typeSolver.add(new ReflectionTypeSolver());
		try {
			typeSolver.add(new JarTypeSolver(jar));
		} catch (IOException e) {
			e.printStackTrace();
		}
		JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
	    JavaParser.getStaticConfiguration().setSymbolResolver(symbolSolver);
	}
	
	
	@Override
	public void visit(TryStmt trystmt, ArrayList<String> collector) {
		// resolve class name
		String classname = "";
		CompilationUnit rootcu = trystmt.findCompilationUnit().get();
		if(rootcu.findFirst(ClassOrInterfaceDeclaration.class).isPresent()) {
			classname = rootcu.findFirst(ClassOrInterfaceDeclaration.class).get().getNameAsString();
		}
		else if(rootcu.findFirst(EnumDeclaration.class).isPresent()){
			classname = rootcu.findFirst(EnumDeclaration.class).get().getNameAsString();
		}
		
		// find all method calls inside the try-block
		List <MethodCallExpr> mc = trystmt.getTryBlock().findAll(MethodCallExpr.class);
		
		NodeList<CatchClause> catches = trystmt.getCatchClauses();
		
		int line = 0;
		try {
			line = trystmt.getBegin().get().line;
		} catch(NoSuchElementException nse) {
			Log.error(nse.getCause());
		}
		
		for(CatchClause cc: catches) {
			
			ResolvedType rt = null;
			ResolvedReferenceTypeDeclaration resolvedReferenceTypeDeclaration = null;
			// resolve exceptions inside catch clause, get its superclass
			try {
				rt = JavaParserFacade.get(typeSolver).convertToUsage(cc.getParameter().getType());
				if (rt == null)
					continue;
				resolvedReferenceTypeDeclaration = typeSolver.solveType(rt.describe());
			} catch (UnsupportedOperationException uoe) {
				Log.error(uoe.getCause());
			} catch (UnsolvedSymbolException use) {
				Log.error(use.getCause());
			} catch (RuntimeException re) {
				Log.error(re.getCause());
			}
			
			if (resolvedReferenceTypeDeclaration == null)
				continue;
			List <ResolvedReferenceType> ancestors = resolvedReferenceTypeDeclaration.getAncestors();

			List<String> ancestorList = new ArrayList<String>();
			for (ResolvedReferenceType ancestor : ancestors) {
				ancestorList.add(ancestor.describe());
			}
			
			//System.out.println(ancestorList);

			/*
			 * if the exception inherits java.lang.Throwable, it is a checked exception
			 * else if it inherits from java.lang.RuntimeException or java.lang.Error, it is a unchecked exception
			 */
			String natureOfException = "";
			if (ancestorList.contains("java.lang.Throwable")) {
				ancestors = resolvedReferenceTypeDeclaration.getAllAncestors();
				ancestorList = new ArrayList<String>();
				for (ResolvedReferenceType ancestor : ancestors) {
					ancestorList.add(ancestor.describe());
				}
				natureOfException = "checked exception: "+resolvedReferenceTypeDeclaration.getQualifiedName();
			} else if (resolvedReferenceTypeDeclaration.getQualifiedName().equals("java.lang.RuntimeException")
					||ancestorList.contains("java.lang.RuntimeException") 
					|| ancestorList.contains("java.lang.Error")) {
				natureOfException = "unchecked exception: "+cc.getParameter().toString();
			}
			
			// for each method call inside the try block
			for (MethodCallExpr expr : mc) {
				try {
					String qualifiedName = JavaParserFacade.get(typeSolver).solveMethodAsUsage(expr).getDeclaration().getClassName()+"."+expr.getNameAsString();
					
					List <Method> methodCalls = callgraph.get(qualifiedName);
					Method declaration = methodCalls.get(0);
					// if the method call throws an exception that is a sub-class of the caught exception, then mark it as over-catch anti-pattern
					boolean overcatch = declaration.getThrownExcptAncestors().contains(resolvedReferenceTypeDeclaration.getQualifiedName());
					if (overcatch) {
						collector.add("Over-catch: catch("+cc.getParameter().toString()+"), throwing "+declaration.getThrownExcpt()+" in "+classname+" at line "+line+" "+qualifiedName+", "+natureOfException);
						//System.out.println("Over-catch: catch("+cc.getParameter().toString()+"), throwing "+declaration.getThrownExcpt());
					}
				} catch (UnsolvedSymbolException use) {
					String qualifiedName = classname+"."+expr.getNameAsString();
					
					List <Method> methodCalls = callgraph.get(qualifiedName);
					if (methodCalls==null) {
						continue;
					}
					Method declaration = methodCalls.get(0);
					// if the method call throws an exception that is a sub-class of the caught exception, then mark it as over-catch anti-pattern
					boolean overcatch = declaration.getThrownExcptAncestors().contains(resolvedReferenceTypeDeclaration.getQualifiedName());
					if (overcatch) {
						collector.add("Over-catch: catch("+cc.getParameter().toString()+"), throwing "+declaration.getThrownExcpt()+" in "+classname+" at line "+line+" "+qualifiedName+", "+natureOfException);
						//System.out.println("Over-catch: catch("+cc.getParameter().toString()+"), throwing "+declaration.getThrownExcpt());
					}
					
					Log.error(use.getCause());
				} catch (RuntimeException re) {
					String qualifiedName = classname+"."+expr.getNameAsString();
					
					List <Method> methodCalls = callgraph.get(qualifiedName);
					if (methodCalls==null) {
						continue;
					}
					Method declaration = methodCalls.get(0);
					
					// if the method call throws an exception that is a sub-class of the caught exception, then mark it as over-catch anti-pattern
					boolean overcatch = declaration.getThrownExcptAncestors().contains(resolvedReferenceTypeDeclaration.getQualifiedName());
					if (overcatch) {
						collector.add("Over-catch: catch("+cc.getParameter().toString()+"), throwing "+declaration.getThrownExcpt()+" in "+classname+" at line "+line+" "+qualifiedName+", "+natureOfException);
						//System.out.println("Over-catch: catch("+cc.getParameter().toString()+"), throwing "+declaration.getThrownExcpt());
					}
					
					Log.error(re.getCause());
				}
			}
		}
		
		super.visit(trystmt, collector);
	}
	
	
	public void setCallGraph(HashMap <String, List <Method>> callgraph) {
		this.callgraph = callgraph;
	}
}
