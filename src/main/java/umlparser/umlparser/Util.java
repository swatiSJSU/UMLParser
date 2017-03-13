/**
 * 
 */
package umlparser.umlparser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;

/**
 * @author Swati Gupta 
 * San Jose State University 
 * Spring 2017 
 * CMPE 202
 *
 */
public class Util {

	private String className = "";
	private MethodDeclaration methodDeclaration;
	private ConstructorDeclaration constructorDeclaration;
	JavaModel javaModel;

	/**
	 * 
	 */
	public Util(JavaModel javaModel) {
		this.javaModel = javaModel;
	}

	/**
	 * @param className
	 * @param methodDeclaration
	 */
	public Util(String className, MethodDeclaration methodDeclaration, JavaModel javaModel) {
		super();
		this.className = className;
		this.methodDeclaration = methodDeclaration;
		this.javaModel = javaModel;
	}

	/**
	 * @param className
	 * @param constructorDeclaration
	 */
	public Util(String className, ConstructorDeclaration constructorDeclaration, JavaModel javaModel) {
		super();
		this.className = className;
		this.constructorDeclaration = constructorDeclaration;
		this.javaModel = javaModel;
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

	public ConcurrentHashMap<String, String> calculateDependency(String methodOrConstructor,
			ArrayList<String> interfaceList) {

		if (methodOrConstructor.equalsIgnoreCase("method")) {
			List<Parameter> list = methodDeclaration.getParameters();
			if (!list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					Type temp = list.get(i).getType();
					if (temp instanceof ReferenceType && interfaceList.contains(temp.toString())) {
						if (!javaModel.getUsesMap().containsKey(temp)) {
							javaModel.getUsesMap().put(temp.toString(), className);
						}
					}
				}
			}
			return javaModel.getUsesMap();
		} else if (methodOrConstructor.equalsIgnoreCase("constructor")) {
			List<Parameter> list = constructorDeclaration.getParameters();
			if (!list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					Type temp = list.get(i).getType();
					if (temp instanceof ReferenceType && interfaceList.contains(temp.toString())) {
						javaModel.getUsesMap1().put(className, temp.toString());
					}
				}
			}
			return javaModel.getUsesMap1();
		}
		return null;
	}

	public boolean calculateMultiplicity(String referenceClass, String className) {
		String relationValue = "";
		String relationKey = "";
		String reverseRelationKey = "";

		if (referenceClass.contains("Collection")) {
			referenceClass = referenceClass.toString().replace("Collection<", "");
			referenceClass = referenceClass.replace(">", "");

			relationValue = "1-*";
			relationKey = className + "~" + referenceClass;
			reverseRelationKey = referenceClass + "~" + className;

			javaModel.getMultiplicityMap();

			if (javaModel.getMultiplicityMap().isEmpty()) {
				javaModel.getMultiplicityMap().put(relationKey, relationValue);
			}
			// check key already exists
			else if (!javaModel.getMultiplicityMap().isEmpty() && javaModel.getMultiplicityMap().size() > 0) {
				if (!javaModel.getMultiplicityMap().containsKey(relationKey)
						&& !javaModel.getMultiplicityMap().containsKey(reverseRelationKey)) {
					for (String keys : javaModel.getMultiplicityMap().keySet()) {
						String tempKey = keys;
						if (tempKey.equals(reverseRelationKey) == false) {
							javaModel.getMultiplicityMap().put(relationKey, relationValue);

						}
					}
				}
			}

			return true;

		} else {
			relationValue = "1-1";
			relationKey = className + "~" + referenceClass;
			reverseRelationKey = referenceClass + "~" + className;
			if (javaModel.getMultiplicityMap().isEmpty()) {
				javaModel.getMultiplicityMap().put(relationKey, relationValue);

			}
			// check key already exists
			else if (!javaModel.getMultiplicityMap().isEmpty() && javaModel.getMultiplicityMap().size() > 0) {
				if (!javaModel.getMultiplicityMap().containsKey(relationKey)
						&& !javaModel.getMultiplicityMap().containsKey(reverseRelationKey)) {
					for (String keys : javaModel.getMultiplicityMap().keySet()) {
						String tempKey = keys;
						if (tempKey.equals(reverseRelationKey) == false) {
							javaModel.getMultiplicityMap().put(relationKey, relationValue);

						}
					}
				}
			}

			return true;

		}

	}

}
