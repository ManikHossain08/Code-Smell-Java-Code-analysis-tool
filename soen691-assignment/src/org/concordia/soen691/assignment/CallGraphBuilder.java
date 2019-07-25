package org.concordia.soen691.assignment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ReferenceType;
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

public class CallGraphBuilder  extends VoidVisitorAdapter<HashMap <String, List <Method>>>{

	String currentMD = "";
	List <Method> mcPath = new ArrayList <Method>();
	CombinedTypeSolver typeSolver = new CombinedTypeSolver();
	
	public CallGraphBuilder() {
		typeSolver.add(new ReflectionTypeSolver());
	    JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
	    JavaParser.getStaticConfiguration().setSymbolResolver(symbolSolver);
	}
	
	public CallGraphBuilder(String jar) {
		typeSolver.add(new ReflectionTypeSolver());
		try {
			typeSolver.add(new JarTypeSolver(jar));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
	    JavaParser.getStaticConfiguration().setSymbolResolver(symbolSolver);
	}
	
	@Override
	public void visit(MethodCallExpr expr, HashMap <String, List <Method>> collector) {
		try {
			String qualifiedName = JavaParserFacade.get(typeSolver).solveMethodAsUsage(expr).getDeclaration().getClassName()+"."+expr.getNameAsString();
			System.out.println(qualifiedName);
			Method methodCall = new Method(qualifiedName);
			mcPath.add(methodCall);
			collector.put(currentMD, mcPath);
		}catch (UnsolvedSymbolException use) {
			Log.error(use.getCause());
		}catch (UnsupportedOperationException uoe) {
			Log.error(uoe.getCause());
		}catch(RuntimeException re) {
			Log.error(re.getCause());
		}
		
		super.visit(expr, collector);
	}
	
	@Override
	public void visit(MethodDeclaration md, HashMap <String, List <Method>> collector) {
		List <String> ancestorList = new ArrayList <String> ();
		List <String> thrownExceptions = new ArrayList <String>();
		
		for (ReferenceType ref : md.getThrownExceptions()) {
			thrownExceptions.add(ref.toString());
			try {
				ResolvedType rt = JavaParserFacade.get(typeSolver).convertToUsage(ref);
				ResolvedReferenceTypeDeclaration resolvedReferenceTypeDeclaration = typeSolver.solveType(rt.describe());
				List <ResolvedReferenceType> ancestors = resolvedReferenceTypeDeclaration.getAllAncestors();
				
				for (ResolvedReferenceType ancestor : ancestors) {
					ancestorList.add(ancestor.describe());
				}
			}catch(UnsolvedSymbolException se) {
				Log.error(se.getCause());
			}catch(UnsupportedOperationException uoe) {
				Log.error(uoe.getCause());
			}catch(RuntimeException re) {
				Log.error(re.getCause());
			}
		}
		
		String classname = "";
		CompilationUnit rootcu = md.findCompilationUnit().get();
		if(rootcu.findFirst(ClassOrInterfaceDeclaration.class).isPresent()) {
			classname = rootcu.findFirst(ClassOrInterfaceDeclaration.class).get().getNameAsString();
		}
		else if(rootcu.findFirst(EnumDeclaration.class).isPresent()){
			classname = rootcu.findFirst(EnumDeclaration.class).get().getNameAsString();
		}
		
		String qualifiedName = classname+"."+md.getNameAsString();
		Method methodDeclaration = new Method(qualifiedName);
		methodDeclaration.setThrownExcpt(thrownExceptions);
		methodDeclaration.addThrownExcptAncestors(ancestorList);
		currentMD = qualifiedName;
		
		mcPath = new ArrayList <Method>();
		mcPath.add(methodDeclaration);
		collector.put(qualifiedName, mcPath);
		
		super.visit(md, collector);
	}
	
	
}
