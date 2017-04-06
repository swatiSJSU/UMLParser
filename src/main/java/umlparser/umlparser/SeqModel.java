/**
 * 
 */
package umlparser.umlparser;

import java.util.ArrayList;
import java.util.HashMap;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;

/**
 * @author Swati Gupta
 * San Jose State University
 * Spring 2017
 * CMPE 202
 */
public class SeqModel {

	private ArrayList<CompilationUnit> compilationUnitArrayList;
	private StringBuilder seqGrammar = new StringBuilder();
    private HashMap<String, String> functionClassMap = new HashMap<String, String>();;
    private HashMap<String, ArrayList<MethodCallExpr>> functionLinkMap = new HashMap<String, ArrayList<MethodCallExpr>>();;	

	/**
	 * @return the compilationUnitArrayList
	 */
	public ArrayList<CompilationUnit> getCompilationUnitArrayList() {
		return compilationUnitArrayList;
	}

	/**
	 * @param compilationUnitArrayList the compilationUnitArrayList to set
	 */
	public void setCompilationUnitArrayList(ArrayList<CompilationUnit> compilationUnitArrayList) {
		this.compilationUnitArrayList = compilationUnitArrayList;
	}

	/**
	 * @return the seqGrammar
	 */
	public StringBuilder getSeqGrammar() {
		return seqGrammar;
	}

	/**
	 * @param seqGrammar the seqGrammar to set
	 */
	public void setSeqGrammar(StringBuilder seqGrammar) {
		this.seqGrammar = seqGrammar;
	}

	/**
	 * @return the functionClassMap
	 */
	public HashMap<String, String> getFunctionClassMap() {
		return functionClassMap;
	}

	/**
	 * @param functionClassMap the functionClassMap to set
	 */
	public void setFunctionClassMap(HashMap<String, String> functionClassMap) {
		this.functionClassMap = functionClassMap;
	}

	/**
	 * @return the functionLinkMap
	 */
	public HashMap<String, ArrayList<MethodCallExpr>> getFunctionLinkMap() {
		return functionLinkMap;
	}

	/**
	 * @param functionLinkMap the functionLinkMap to set
	 */
	public void setFunctionLinkMap(HashMap<String, ArrayList<MethodCallExpr>> functionLinkMap) {
		this.functionLinkMap = functionLinkMap;
	}
	
	
}
