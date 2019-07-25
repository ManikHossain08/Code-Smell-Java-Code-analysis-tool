package org.concordia.soen691.assignment;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodReference;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

/*
 * @version 1.0
 * @since 1.0
 * <p>
 * This is the class for detecting Destructive Wrapping exception handling anti-pattern.
 * </p>
 */

public class JDTDestructiveWrapping extends SOEN691JDTCollector {
	/*
	 * This visitor implements the destructive wrapping detector using the JDT
	 * library
	 */

	List<ThrowStatement> throwStatements = new ArrayList<>();

	public JDTDestructiveWrapping() {
		super();
	}

	public boolean visit(CatchClause catchClause) {
		boolean result = true;

		SingleVariableDeclaration excp = catchClause.getException();
		String exceptionTypeName = excp.getType().toString();
		Block catchBlock = catchClause.getBody();

		// get the statement list to determine whether there is a throw
		@SuppressWarnings("unchecked")
		List<Statement> innerStatements = catchBlock.statements();
		// ThrowStatement throwStmt = null;

		for (Statement stmt : innerStatements) {
			findSuspiciousThrow(stmt);
		}
		for (ThrowStatement throwStmt : throwStatements) {
			if (throwStmt != null) {
				// ThrowStatement throwStmt = (ThrowStatement)stmt;
				ITypeBinding typeBingingObject = null;
				Expression expressionObject = throwStmt.getExpression();

				if (expressionObject instanceof MethodInvocation) {
					IMethodBinding methodBinding = ((MethodInvocation) expressionObject).resolveMethodBinding();
					if (null != methodBinding) {
						typeBingingObject = methodBinding.getReturnType();
					}
				} else if (expressionObject instanceof MethodReference) {
					IMethodBinding methodBinding = ((MethodReference) expressionObject).resolveMethodBinding();
					if (null != methodBinding) {
						typeBingingObject = methodBinding.getReturnType();
					}
				} else {
					typeBingingObject = throwStmt.getExpression().resolveTypeBinding();
				}

				if (null != typeBingingObject) {
					String thrownExcp = typeBingingObject.getName();
					if (thrownExcp != null) {
						if (!exceptionTypeName.equalsIgnoreCase(thrownExcp)) {
							// we have a match of destructive wrapping
							collector.add("Destructive Wrapping at line "
									+ cu.getLineNumber(throwStmt.getStartPosition()) + " - " + "Throwing new "
									+ thrownExcp + " after catching exception: " + exceptionTypeName);
						}
					} else {
						collector.add("Warning! Possible Destructive Wrapping at line "
								+ cu.getLineNumber(throwStmt.getStartPosition()) + " - "
								+ "Empty name for thrown exception after catching exception: " + exceptionTypeName);
						collector.add("\tAssociated throw statement: " + throwStmt.toString());

					}
				} else {
					collector.add("Warning! Possible Destructive Wrapping at line "
							+ cu.getLineNumber(throwStmt.getStartPosition()) + " - "
							+ "Unresolved type binding for thrown exception after catching exception: "
							+ exceptionTypeName);
					collector.add("\tAssociated throw statement: " + throwStmt.toString());
				}
			}
		}
		throwStatements.clear();
		return result;
	}

	@SuppressWarnings("unchecked")
	private void findSuspiciousThrow(Statement stmt) {

		if (stmt instanceof ThrowStatement) {
			throwStatements.add((ThrowStatement) stmt);
		} else if (stmt instanceof TryStatement) {
			// skip this, nested try will be detected by next visitor pass
		} else if (stmt instanceof IfStatement) {
			findSuspiciousThrow(((IfStatement) stmt).getThenStatement());
			findSuspiciousThrow(((IfStatement) stmt).getElseStatement());
		} else if (stmt instanceof Block) {
			List<Statement> blkList = ((Block) stmt).statements();
			for (Statement blkStmt : blkList) {
				findSuspiciousThrow(blkStmt);
			}
		} else if (stmt instanceof ForStatement) {
			findSuspiciousThrow(((ForStatement) stmt).getBody());
		} else if (stmt instanceof EnhancedForStatement) {
			findSuspiciousThrow(((EnhancedForStatement) stmt).getBody());
		} else if (stmt instanceof WhileStatement) {
			findSuspiciousThrow(((WhileStatement) stmt).getBody());
		} else if (stmt instanceof DoStatement) {
			findSuspiciousThrow(((DoStatement) stmt).getBody());
		} else if (stmt instanceof SwitchStatement) {
			List<Statement> switchList = ((SwitchStatement) stmt).statements();
			for (Statement switchStmt : switchList) {
				findSuspiciousThrow(switchStmt);
			}
		} else if (stmt instanceof SynchronizedStatement) {
			findSuspiciousThrow(((SynchronizedStatement) stmt).getBody());
		} else {
			// default does nothing; used if-else-if for compiler optimization
		}

	}
}
