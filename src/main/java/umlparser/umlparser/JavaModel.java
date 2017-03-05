/**
 * 
 */
package umlparser.umlparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

/**
 * @author Swati Gupta San Jose State University Spring 2017 CMPE 202
 *
 */
public class JavaModel {

	private ArrayList<CompilationUnit> compilationUnitArrayList;
	private ArrayList<String> variableList = new ArrayList<String>();
	private ArrayList<String> methodList = new ArrayList<String>();
	private ArrayList<String> constructList = new ArrayList<String>();
	private ArrayList<String> interfaceList = new ArrayList<String>();
	private ArrayList<String> methodParamList;
	private ArrayList<String> constructParamList = new ArrayList<String>();
	private HashMap<String, Boolean> classOrInterfaceMap;
	private HashMap<String, List<ClassOrInterfaceType>> classInterfaceMap = new HashMap<String, List<ClassOrInterfaceType>>();
	private HashMap<String, List<ClassOrInterfaceType>> classSuperClassMap = new HashMap<String, List<ClassOrInterfaceType>>();
	private static ConcurrentHashMap<String, String> usesMap = new ConcurrentHashMap<String, String>();
	private static ConcurrentHashMap<String, String> usesMap1 = new ConcurrentHashMap<String, String>();
	private ConcurrentHashMap<String, String> multiplicityMap = new ConcurrentHashMap<String, String>();
	private StringBuilder grammar = new StringBuilder();
	private String className;
	private String usesRelation = "";
	private String[] primitives = { "byte", "short", "int", "long", "float", "double", "boolean", "char", "Byte",
			"Short", "Integer", "Long", "Float", "Double", "Boolean", "Char" };
	private Boolean flag = true;

	/**
	 * @return the compilationUnitArrayList
	 */
	public ArrayList<CompilationUnit> getCompilationUnitArrayList() {
		return compilationUnitArrayList;
	}

	/**
	 * @param compilationUnitArrayList
	 *            the compilationUnitArrayList to set
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
	 * @param classOrInterfaceMap
	 *            the classOrInterfaceMap to set
	 */
	public void setClassOrInterfaceMap(HashMap<String, Boolean> classOrInterfaceMap) {
		this.classOrInterfaceMap = classOrInterfaceMap;
	}

	/**
	 * @return the grammar
	 */
	public StringBuilder getGrammar() {
		return grammar;
	}

	/**
	 * @param grammar
	 *            the grammar to set
	 */
	public void setGrammar(StringBuilder grammar) {
		this.grammar = grammar;
	}

	/**
	 * @return the variableList
	 */
	public ArrayList<String> getVariableList() {
		return variableList;
	}

	/**
	 * @param variableList
	 *            the variableList to set
	 */
	public void setVariableList(ArrayList<String> variableList) {
		this.variableList = variableList;
	}

	/**
	 * @return the methodList
	 */
	public ArrayList<String> getMethodList() {
		return methodList;
	}

	/**
	 * @param methodList
	 *            the methodList to set
	 */
	public void setMethodList(ArrayList<String> methodList) {
		this.methodList = methodList;
	}

	/**
	 * @return the constructList
	 */
	public ArrayList<String> getConstructList() {
		return constructList;
	}

	/**
	 * @param constructList
	 *            the constructList to set
	 */
	public void setConstructList(ArrayList<String> constructList) {
		this.constructList = constructList;
	}

	/**
	 * @return the interfaceList
	 */
	public ArrayList<String> getInterfaceList() {
		return interfaceList;
	}

	/**
	 * @param interfaceList
	 *            the interfaceList to set
	 */
	public void setInterfaceList(ArrayList<String> interfaceList) {
		this.interfaceList = interfaceList;
	}

	/**
	 * @return the flag
	 */
	public Boolean getFlag() {
		return flag;
	}

	/**
	 * @param flag
	 *            the flag to set
	 */
	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the classInterfaceMap
	 */
	public HashMap<String, List<ClassOrInterfaceType>> getClassInterfaceMap() {
		return classInterfaceMap;
	}

	/**
	 * @param classInterfaceMap
	 *            the classInterfaceMap to set
	 */
	public void setClassInterfaceMap(HashMap<String, List<ClassOrInterfaceType>> classInterfaceMap) {
		this.classInterfaceMap = classInterfaceMap;
	}

	/**
	 * @return the classSuperClassMap
	 */
	public HashMap<String, List<ClassOrInterfaceType>> getClassSuperClassMap() {
		return classSuperClassMap;
	}

	/**
	 * @param classSuperClassMap
	 *            the classSuperClassMap to set
	 */
	public void setClassSuperClassMap(HashMap<String, List<ClassOrInterfaceType>> classSuperClassMap) {
		this.classSuperClassMap = classSuperClassMap;
	}

	/**
	 * @return the primitives
	 */
	public String[] getPrimitives() {
		return primitives;
	}

	/**
	 * @return the methodParamList
	 */
	public ArrayList<String> getMethodParamList() {
		return methodParamList;
	}

	/**
	 * @param methodParamList
	 *            the methodParamList to set
	 */
	public void setMethodParamList(ArrayList<String> methodParamList) {
		this.methodParamList = methodParamList;
	}

	/**
	 * @return the usesMap
	 */
	public static ConcurrentHashMap<String, String> getUsesMap() {
		return usesMap;
	}

	/**
	 * @param usesMap
	 *            the usesMap to set
	 */
	public static void setUsesMap(ConcurrentHashMap<String, String> usesMap) {
		JavaModel.usesMap = usesMap;
	}

	/**
	 * @return the usesMap1
	 */
	public static ConcurrentHashMap<String, String> getUsesMap1() {
		return usesMap1;
	}

	/**
	 * @param usesMap1
	 *            the usesMap1 to set
	 */
	public static void setUsesMap1(ConcurrentHashMap<String, String> usesMap1) {
		JavaModel.usesMap1 = usesMap1;
	}

	/**
	 * @return the constructParamList
	 */
	public ArrayList<String> getConstructParamList() {
		return constructParamList;
	}

	/**
	 * @param constructParamList
	 *            the constructParamList to set
	 */
	public void setConstructParamList(ArrayList<String> constructParamList) {
		this.constructParamList = constructParamList;
	}

	/**
	 * @return the multiplicityMap
	 */
	public ConcurrentHashMap<String, String> getMultiplicityMap() {
		return multiplicityMap;
	}

	/**
	 * @param multiplicityMap
	 *            the multiplicityMap to set
	 */
	public void setMultiplicityMap(ConcurrentHashMap<String, String> multiplicityMap) {
		this.multiplicityMap = multiplicityMap;
	}

	/**
	 * @return the usesRelation
	 */
	public String getUsesRelation() {
		return usesRelation;
	}

	/**
	 * @param usesRelation
	 *            the usesRelation to set
	 */
	public void setUsesRelation(String usesRelation) {
		this.usesRelation = usesRelation;
	}

}
