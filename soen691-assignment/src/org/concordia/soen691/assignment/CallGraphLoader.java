package org.concordia.soen691.assignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import com.github.javaparser.JavaParser;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;

public class CallGraphLoader {
	int fileN = 0;
	CallGraphBuilder cg = null;
	HashMap <String, List <Method>> callgraph = new HashMap <String,  List <Method>> ();

	public CallGraphLoader(String libFolder) {
		cg = new CallGraphBuilder(libFolder);
	}
	
	public HashMap <String, List <Method>> loadCallGraph(String sourceDir) {
		JavaParser.getStaticConfiguration().setAttributeComments(false);
        JavaParser.getStaticConfiguration().setStoreTokens(false);
        
		if (sourceDir == null) {
			System.out.println("No source folder found, failed to build the call graph");
		}
		File projectDir = new File(sourceDir);
		explore(projectDir);
		
		if (callgraph.size()<0) {
			System.out.println("Failed to build the call graph");
		}
		return callgraph;
	}
	
	private void explore(File file) {
		fileN++;
		if (fileN % 1000 ==0) {
			JavaParserFacade.clearInstances();
		}
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				explore(child);
			}
		} else {
			if (file.getName().endsWith(".java") && !file.getName().contains("Test")) {
				try {
					com.github.javaparser.ast.CompilationUnit cu = JavaParser.parse(file);
					cg.visit(cu, callgraph);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (StackOverflowError e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public HashMap <String, List <Method>> read (String graphPath){
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		HashMap <String, List <Method>> graph = new HashMap <String, List <Method>>();
		try {
			fis = new FileInputStream(graphPath);
			ois = new ObjectInputStream(fis);
			graph = (HashMap <String, List <Method>>) ois.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					ois.close();
					fis.close();
				}
			} catch (IOException io) {
				io.printStackTrace();
			}
		}
		return graph;
	}
	
	public void store(String graphPath, HashMap <String, List <Method>> callgraph) {
		ObjectOutputStream oos;
		try {
			FileOutputStream fos = new FileOutputStream(graphPath);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(callgraph);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
