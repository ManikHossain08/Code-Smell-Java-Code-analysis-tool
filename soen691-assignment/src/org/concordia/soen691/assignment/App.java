package org.concordia.soen691.assignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class App {
	public static void main (String[]args) {
		HashMap <String, List <Method>> callgraph = new HashMap<String, List<Method>>();
//		callgraph.put("hello", new ArrayList<Method>());
//		store("methods", callgraph);
		callgraph = read("guava");
		System.out.println(callgraph.size());
	    int i = 0;
		for (HashMap.Entry<String, List <Method>> item : callgraph.entrySet()) {
		    String key = item.getKey();
		    List <Method> value = item.getValue();
		    
		    if (value.get(0).getThrownExcpt().size() > 0) {
		    	System.out.println(key+" "+value.get(0).getThrownExcpt().size());
		    	System.out.println(value.get(0));
		    	i++;
		    }
		}
	    System.out.println(i);
	}
	
	public static HashMap <String, List <Method>> read (String fileName){
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		HashMap <String, List <Method>> graph = new HashMap <String, List <Method>>();
		try {
			fis = new FileInputStream("/Users/anrchen/GitHub/soen691-assignment/source-code/soen691-assignment/graph/"+fileName+".graph");
			ois = new ObjectInputStream(fis);
			graph = (HashMap <String, List <Method>>) ois.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	public static void store(String fileName, HashMap <String, List <Method>> callgraph) {
		ObjectOutputStream oos;
		try {
			FileOutputStream fos = new FileOutputStream("graph/"+fileName+".graph");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(callgraph);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Success");
	}
}
