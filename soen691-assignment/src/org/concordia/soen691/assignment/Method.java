package org.concordia.soen691.assignment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Method implements Serializable{
	String name = "";
	String className = "";
	String qualifiedName = "";
	List <String> thrownExcpt = new ArrayList <String>();
	List <String> thrownExcptAncestors = new ArrayList <String>();

	public Method(String name, String className) {
		this.name = name;
		this.className = className;
	}
	
	public Method(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}
	
	public String getQualifiedName() {
		return qualifiedName;
	}
	public void setQualifiedName(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public void addThrownExcptAncestors(List <String> newExcptAncestors) {
		thrownExcptAncestors.addAll(newExcptAncestors);
	}
	public List <String> getThrownExcptAncestors() {
		return thrownExcptAncestors;
	}

	public List <String> getThrownExcpt() {
		return thrownExcpt;
	}

	public void setThrownExcpt(List <String> thrownExcpt) {
		this.thrownExcpt = thrownExcpt;
	}

	@Override
	public String toString() {
		return "Method [qualifiedName=" + qualifiedName + ", thrownExcptAncestors=" + thrownExcptAncestors + "]";
	}
	
}
