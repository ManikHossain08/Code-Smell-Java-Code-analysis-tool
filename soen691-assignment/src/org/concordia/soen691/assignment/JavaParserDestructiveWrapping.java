package org.concordia.soen691.assignment;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


public class JavaParserDestructiveWrapping extends VoidVisitorAdapter<ArrayList<String>>{
	public JavaParserDestructiveWrapping() {}
	
	@Override
	public void visit(CatchClause catchBlock, ArrayList<String> collector) {
		//Find all throw statements in specific catch clause block
		List<ThrowStmt> getAllThrowStmt = catchBlock.findAll(ThrowStmt.class);
		CatchClause innerCatchClause = new CatchClause();
		boolean isContainNew = false; 
		String exactThrowExceptionType = "";
		
		if(getAllThrowStmt.size() > 0) {
		    for (ThrowStmt throwStatmnt: getAllThrowStmt) {
		    		isContainNew = false;
					Optional<CatchClause> ParentCatchClause= throwStatmnt.findAncestor(CatchClause.class);
					Expression throwStatmntExpr = throwStatmnt.getExpression();
					isContainNew = checkStatementString(throwStatmntExpr.toString(),"new");
					if(ParentCatchClause.isPresent()) {
						innerCatchClause = ParentCatchClause.get();
					}
					
					String getExceptionTypeParameter = innerCatchClause.getParameter().getTypeAsString().toString();
					int LineNumber = throwStatmnt.getBegin().get().line;

					//Check if throw has ReferenceType statement
					if(isContainNew == true) {
						List<ReferenceType> getAllRT = throwStatmnt.findAll(ReferenceType.class);
						for (ReferenceType getSingleRT: getAllRT) {
							exactThrowExceptionType = getSingleRT.getElementType().toString();
						}
						if(getExceptionTypeParameter.matches(exactThrowExceptionType) == false ) {
							collector.add("Destructive Wrapping at line " + LineNumber+ " --> "
									+ "Throwing new " + exactThrowExceptionType + " after catching this Exception: " + getExceptionTypeParameter);
							
							}
						}
					//check if it is calling method in throw statement
					else if(throwStatmntExpr.isMethodCallExpr()) {
						MethodCallExpr getMethodName= throwStatmntExpr.asMethodCallExpr();
						collector.add("Destructive Wrapping at line " + LineNumber+ " --> "
								+ "Throwing new " + getMethodName.toString() + " after catching this Exception: " + getExceptionTypeParameter);
						}
					//Check other type of DW like VariableDeclarator
					else {
						List<Node> getNodes = throwStatmntExpr.getChildNodes();
						List<VariableDeclarator> getAllVariableDeclator = innerCatchClause.findAll(VariableDeclarator.class);
						if(getAllVariableDeclator.size() > 0) {
						for (VariableDeclarator singleVD: getAllVariableDeclator) {
							isContainNew = checkStatementString(singleVD.toString(), throwStatmntExpr.toString());
							 exactThrowExceptionType= singleVD.getType().toString();
							if(isContainNew== true && exactThrowExceptionType.matches(getExceptionTypeParameter) == false) {
								 collector.add("Destructive Wrapping at line " + LineNumber+ " --> "
											+ "Throwing new "+ exactThrowExceptionType + " after catching this Exception: " + getExceptionTypeParameter);
							}
						  }
						}
						else if (getNodes.size() == 2 ){
							collector.add("Destructive Wrapping at line " + LineNumber+ " --> "
									+ "Throwing new "+ getNodes.get(0).toString() + " after catching this Exception: " + getExceptionTypeParameter);
						}
						
					}
						
			}
        }
	
	}
	
	//check whether a throw statement contain a new keyword
	private boolean checkStatementString(String statements, String keyword) {
		boolean flag = false; 
		String[] splitStatements = statements.toString().split(" ");
		if(splitStatements.length > 0) {
			for (int i=0; i < splitStatements.length; i++)
	        {
	            if(splitStatements[i].trim().matches(keyword)) {
	            	flag = true;
	            }
	         }
		}
		return flag;
	}

	
}