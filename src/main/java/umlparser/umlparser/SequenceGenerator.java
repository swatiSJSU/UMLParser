/**
 * 
 */
package umlparser.umlparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;

/**
 * @author Swati Gupta San Jose State University Spring 2017 CMPE 202
 */
public class SequenceGenerator {

	SeqModel seqModel;

	/**
	 * 
	 */
	public SequenceGenerator() {
		// TODO Auto-generated constructor stub
		seqModel = new SeqModel();
	}

	public void generate(String inputFilesPath, String outputFileName, String className, String functionName)
			throws IOException, ParseException {
		createCompilationUnit(inputFilesPath);
		StringBuilder seqGrammar = seqModel.getSeqGrammar();
		seqGrammar.append("@startuml\n");
		for (CompilationUnit cu : seqModel.getCompilationUnitArrayList()) {
			createSeqGrammar(cu);
		}
	}

	// Method to create a compilation unit
	// from all the java files in a folder
	private void createCompilationUnit(String inputFilesPath) throws IOException, ParseException {

		// ArrayList to store the compilation unit
		ArrayList<CompilationUnit> compilationUnitArrayList = new ArrayList<CompilationUnit>();

		File folder = new File(inputFilesPath);
		File listOfFiles[] = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".java")) {
				FileInputStream fileInputStream = new FileInputStream(listOfFiles[i]);
				try {
					CompilationUnit compilationUnit = JavaParser.parse(fileInputStream);
					compilationUnitArrayList.add(compilationUnit);
				} finally {
					fileInputStream.close();
				}
			}
		}
		seqModel.setCompilationUnitArrayList(compilationUnitArrayList);
	}

	private void createSeqGrammar(CompilationUnit compilationUnit) {
		String classID = "";
		List<TypeDeclaration> typeDeclareList = compilationUnit.getTypes();
		for (Node node : typeDeclareList) {
			ClassOrInterfaceDeclaration classOrInterfaceDeclaration = (ClassOrInterfaceDeclaration) node;
			classID = classOrInterfaceDeclaration.getName();
			for (BodyDeclaration bodyDeclaration : ((TypeDeclaration) classOrInterfaceDeclaration).getMembers()) {
				if (bodyDeclaration instanceof MethodDeclaration) {
					MethodDeclaration methodDeclaration = (MethodDeclaration) bodyDeclaration;
					ArrayList<MethodCallExpr> methodCallExprList = new ArrayList<MethodCallExpr>();
					for (Object blockStatementObj : methodDeclaration.getChildrenNodes()) {
						if (blockStatementObj instanceof BlockStmt) {
							for (Object exprStmtObj : ((Node) blockStatementObj).getChildrenNodes()) {
								if (exprStmtObj instanceof ExpressionStmt) {
									if (((ExpressionStmt) (exprStmtObj)).getExpression() instanceof MethodCallExpr) {
										methodCallExprList.add((MethodCallExpr) (((ExpressionStmt) (exprStmtObj)).getExpression()));
									}
								}
							}
						}
					}
					seqModel.getFunctionLinkMap().put(methodDeclaration.getName(), methodCallExprList);
					seqModel.getFunctionClassMap().put(methodDeclaration.getName(), classID);
				}
			}
		}
	}

}
