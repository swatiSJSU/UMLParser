/**
 * 
 */
package umlparser.umlparser;

import java.util.ArrayList;
import java.util.HashMap;

import com.github.javaparser.ast.CompilationUnit;

/**
 * @author Swati Gupta San Jose State University Spring 2017 CMPE 202
 *
 */
public class JavaModel {
	
	private ArrayList<CompilationUnit> compilationUnitArrayList;
	private HashMap<String, Boolean> classOrInterfaceMap;
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
	 * @return the classOrInterfaceMap
	 */
	public HashMap<String, Boolean> getClassOrInterfaceMap() {
		return classOrInterfaceMap;
	}
	/**
	 * @param classOrInterfaceMap the classOrInterfaceMap to set
	 */
	public void setClassOrInterfaceMap(HashMap<String, Boolean> classOrInterfaceMap) {
		this.classOrInterfaceMap = classOrInterfaceMap;
	}
	
	

}
