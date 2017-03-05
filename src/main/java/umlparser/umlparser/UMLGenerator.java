/**
 * 
 */
package umlparser.umlparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.ModifierSet;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.body.VariableDeclaratorId;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.VoidType;

/**
 * @author Swati Gupta San Jose State University Spring 2017 CMPE 202
 */
public class UMLGenerator {

	JavaModel javaModel;

	/**
	 * Constructor
	 */
	public UMLGenerator() {
		javaModel = new JavaModel();
	}

	// Method to generate the UML diagram
	// from all .java files in a folder
	public void generate(String inputFilesPath, String outputFileName) throws IOException, ParseException {
		String grammar = "";
		createCompilationUnit(inputFilesPath);
		for (CompilationUnit cu : javaModel.getCompilationUnitArrayList()) {
			grammar = createGrammar(cu);
		}

		grammar = createRelationship();
		String fullyQualifiedOutputFileName = inputFilesPath + "\\" + outputFileName + ".png";
		createUMLDiagram(grammar, fullyQualifiedOutputFileName);
		System.out.println("UML Diagram created at the location: " + fullyQualifiedOutputFileName);
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
		javaModel.setCompilationUnitArrayList(compilationUnitArrayList);
	}

	// Method to create the grammar
	private String createGrammar(CompilationUnit cu) {
		StringBuilder grammar = javaModel.getGrammar();
		if (grammar.length() > 0 && (grammar.charAt(grammar.length() - 1) != ',')) {
			grammar.append(",");
		}
		javaModel.setVariableList(new ArrayList<String>());
		javaModel.setMethodList(new ArrayList<String>());
		javaModel.setConstructList(new ArrayList<String>());
		grammar.append("[");

		List<Node> childrenNodes = cu.getChildrenNodes();
		createClassInterfaceMap(childrenNodes);

		// next step is to check the declaration within the class body
		List<TypeDeclaration> bodyTypes = cu.getTypes();
		for (TypeDeclaration bodyType : bodyTypes) {
			List<BodyDeclaration> bodyDec = bodyType.getMembers();

			if (!bodyDec.isEmpty() && bodyDec.size() > 0) {
				String accessModifier = "";
				for (BodyDeclaration body : bodyDec) {
					if (body instanceof FieldDeclaration) {
						String primitiveType = "";
						FieldDeclaration fieldDec = (FieldDeclaration) body;
						// checking access modifiers
						int fieldDecModifiers = fieldDec.getModifiers();
						boolean success = false;

						switch (fieldDecModifiers) {
						case ModifierSet.PRIVATE:
							accessModifier = "-";
							success = true;
							break;
						case ModifierSet.PUBLIC:
							accessModifier = "+";
							success = true;
							break;

						}

						// end checking access modifiers
						if (success) {
							boolean enterVariable = true;

							List<Node> fieldChildNodes = fieldDec.getChildrenNodes();

							for (Node fieldNode : fieldChildNodes) {
								if (fieldNode instanceof ReferenceType) {
									String refType = ((ReferenceType) fieldNode).getType().toString();
									if (refType.equals("String")) {
										primitiveType += refType;
									} else {
										boolean foundPrimitive = false;
										boolean foundCollection = false;

										for (String primitiveRef : javaModel.getPrimitives()) {
											if (refType.contains(primitiveRef)) {
												primitiveType += refType + "(*)";
												foundPrimitive = true;
												break;
											}
										}
										if (!foundPrimitive) {
											// logic for checking multiplicity
											Util util = new Util(javaModel);
											if (util.calculateMultiplicity(refType, javaModel.getClassName())) {
												enterVariable = false;
												foundCollection = true;
												break;
											}

										}
									}
								} else if (fieldNode instanceof PrimitiveType) {
									PrimitiveType pType = (PrimitiveType) fieldNode;
									primitiveType = pType.toString();
								}
								if (fieldNode instanceof VariableDeclarator && enterVariable) {
									VariableDeclarator variableDec = (VariableDeclarator) fieldNode;
									javaModel.getVariableList()
											.add(accessModifier + variableDec.toString() + ":" + primitiveType);
								}
							}
						}

					}

					else if (body instanceof MethodDeclaration) {
						if (javaModel.getFlag() == true) {
							String tempMethodParam = "";
							MethodDeclaration method = (MethodDeclaration) body;
							String methodAccessModifier = "";
							String methodReferenceType = "";
							String methodName = "";
							int methodModifier = method.getModifiers();
							boolean success = false;

							switch (methodModifier) {
							case ModifierSet.PUBLIC:
								methodAccessModifier = "+";
								success = true;
								break;
							case ModifierSet.PUBLIC + ModifierSet.STATIC:
								methodAccessModifier = "+";
								success = true;
								break;
							}

							if (success) {
								List<Node> methodChildNodes = method.getChildrenNodes();

								for (Node methodChildNode : methodChildNodes) {
									if (methodChildNode instanceof ReferenceType) {
										ReferenceType referenceMethod = (ReferenceType) methodChildNode;

										methodReferenceType = referenceMethod.getType().toString();
									} else if (methodChildNode instanceof VoidType) {
										methodReferenceType = "void";
									}
								}

								methodName = method.getName();

								List<Parameter> methodParams = method.getParameters();
								javaModel.setMethodParamList(new ArrayList<String>());
								if (methodParams.size() > 0) {
									tempMethodParam += "(";
									for (Parameter param : methodParams) {
										List<Node> paramChildNodes = param.getChildrenNodes();

										String methodParamReferenceType = "", variable = "";
										for (Node paramChild : paramChildNodes) {
											if (paramChild instanceof ReferenceType) {
												ReferenceType r = (ReferenceType) paramChild;
												methodParamReferenceType = r.getType().toString();
												Util util = new Util(javaModel.getClassName(), method, javaModel);
												util.calculateDependency("method", javaModel.getInterfaceList());
											} else if (paramChild instanceof VariableDeclaratorId) {
												VariableDeclaratorId v = (VariableDeclaratorId) paramChild;
												variable = v.getName().toString();
											}

										}
										javaModel.getMethodParamList().add(variable + ":" + methodParamReferenceType);
									}

									for (int i = 0; i < javaModel.getMethodParamList().size(); i++) {
										if (i != javaModel.getMethodParamList().size() - 1) {
											tempMethodParam += javaModel.getMethodParamList().get(i) + ",";
										} else {
											tempMethodParam += javaModel.getMethodParamList().get(i);
										}
									}
									tempMethodParam += ")";
								} else {
									tempMethodParam = "()";
								}
								if (methodName.startsWith("get") || methodName.startsWith("set")) {

								} else {
									javaModel.getMethodList().add(methodAccessModifier + methodName + tempMethodParam
											+ ":" + methodReferenceType);
								}
							}
						}
					} else if (body instanceof ConstructorDeclaration) {
						String tempConstructParam = "";
						ConstructorDeclaration construct = (ConstructorDeclaration) body;
						List<Node> constructNodes = construct.getChildrenNodes();

						String constructAccessModifier = "";
						String constrctName = "";

						int constructModifier = construct.getModifiers();
						boolean success = false;

						switch (constructModifier) {
						case ModifierSet.PUBLIC:
							constructAccessModifier = "+";
							success = true;
							break;
						}
						constrctName = construct.getName();
						List<Parameter> constructParams = construct.getParameters();
						javaModel.setConstructParamList(new ArrayList<String>());

						if (constructParams.size() > 0) {
							tempConstructParam += "(";
							for (Parameter param : constructParams) {
								List<Node> paramChildNodes = param.getChildrenNodes();
								String conParamReferenceType = "", variable = "";
								for (Node paramChild : paramChildNodes) {
									if (paramChild instanceof ReferenceType) {
										ReferenceType r = (ReferenceType) paramChild;
										conParamReferenceType = r.getType().toString();
										Util util = new Util(javaModel.getClassName(), construct, javaModel);
										util.calculateDependency("constructor", javaModel.getInterfaceList());
									} else if (paramChild instanceof VariableDeclaratorId) {
										VariableDeclaratorId v = (VariableDeclaratorId) paramChild;
										variable = v.getName().toString();
									}

								}

								javaModel.getConstructParamList().add(variable + ":" + conParamReferenceType);
							}

							for (int i = 0; i < javaModel.getConstructParamList().size(); i++) {
								if (i != javaModel.getConstructParamList().size() - 1) {
									tempConstructParam += javaModel.getConstructParamList().get(i) + ",";
								} else {
									tempConstructParam += javaModel.getConstructParamList().get(i);
								}
							}
							tempConstructParam += ")";
						} else {
							tempConstructParam = "()";
						}
						javaModel.getConstructList().add(constructAccessModifier + constrctName + tempConstructParam);
					}

				}
			}
		}

		if (javaModel.getVariableList().size() > 0) {
			grammar.append("|");
			for (int i = 0; i < javaModel.getVariableList().size(); i++) {
				if (i != javaModel.getVariableList().size() - 1)
					grammar.append(javaModel.getVariableList().get(i) + ";");
				else
					grammar.append(javaModel.getVariableList().get(i));
			}

		}

		if (javaModel.getMethodList().size() > 0) {
			grammar.append("|");
			for (int i = 0; i < javaModel.getMethodList().size(); i++) {
				if (i != javaModel.getMethodList().size() - 1)
					grammar.append(javaModel.getMethodList().get(i) + ";");
				else
					grammar.append(javaModel.getMethodList().get(i) + ";");
			}
		}

		if (javaModel.getConstructList().size() > 0) {
			if (javaModel.getMethodList().isEmpty() && javaModel.getMethodList().size() == 0) {
				grammar.append("|");
				for (int i = 0; i < javaModel.getConstructList().size(); i++) {
					if (i != javaModel.getConstructList().size() - 1)
						grammar.append(javaModel.getConstructList().get(i) + ";");
					else
						grammar.append(javaModel.getConstructList().get(i));
				}
			} else {
				for (int i = 0; i < javaModel.getConstructList().size(); i++) {
					if (i != javaModel.getConstructList().size() - 1)
						grammar.append(javaModel.getConstructList().get(i) + ";");
					else
						grammar.append(javaModel.getConstructList().get(i));
				}
			}
		}
		grammar.append("]");
		grammar.append(",");

		return grammar.toString();
	}

	// Method to create the class/interface map
	public void createClassInterfaceMap(List<Node> childrenNodes) {
		StringBuilder grammar = javaModel.getGrammar();
		for (Node child : childrenNodes) {
			if (child instanceof ClassOrInterfaceDeclaration) {
				ClassOrInterfaceDeclaration classOrInterfaceDeclaration = (ClassOrInterfaceDeclaration) child;

				// create a list with interfaces as entries
				if (classOrInterfaceDeclaration.isInterface()) {
					javaModel.getInterfaceList().add(classOrInterfaceDeclaration.getName());
					grammar.append("<<Interface>>;");
					grammar.append(classOrInterfaceDeclaration.getName());
					grammar.append("");
					javaModel.setFlag(false);
					continue;
				}
				javaModel.setFlag(true);
				grammar.append(classOrInterfaceDeclaration.getName());
				javaModel.setClassName(classOrInterfaceDeclaration.getName());

				// create HashMaps to map classes with interfaces and parent
				// classes for implements and extends relation
				List<ClassOrInterfaceType> implementsList = classOrInterfaceDeclaration.getImplements();
				if (implementsList != null) {
					javaModel.getClassInterfaceMap().put(classOrInterfaceDeclaration.getName(), implementsList);
				}

				List<ClassOrInterfaceType> extendsList = classOrInterfaceDeclaration.getExtends();
				if (extendsList != null) {
					javaModel.getClassSuperClassMap().put(classOrInterfaceDeclaration.getName(), extendsList);
				}

			}
		} // end of for loop for childrenNodes
	}

	// Method to create the relationship from the maps
	// populated via createGrammar() method
	// This method also calculates the multiplicity in a relation
	public String createRelationship() {
		StringBuilder grammar = javaModel.getGrammar();

		if (!javaModel.getUsesMap().isEmpty() && javaModel.getUsesMap().size() > 0) {
			for (String keys : javaModel.getUsesMap().keySet()) {
				String tempKey = keys;
				grammar.append("[");
				grammar.append(javaModel.getUsesMap().get(tempKey));
				grammar.append("]uses -.->[<<interface>>;");
				grammar.append(tempKey);
				grammar.append("],");
			}
		}
		if (!javaModel.getUsesMap1().isEmpty() && javaModel.getUsesMap1().size() > 0) {
			for (String keys : javaModel.getUsesMap1().keySet()) {
				String tempKey = keys;
				grammar.append("[");
				grammar.append(tempKey);
				grammar.append("]uses -.->[<<interface>>;");
				grammar.append(javaModel.getUsesMap1().get(tempKey));
				grammar.append("],");
			}
		}
		if (!javaModel.getMultiplicityMap().isEmpty() && javaModel.getMultiplicityMap().size() > 0) {
			String usesRelation = "";
			for (String keys : javaModel.getMultiplicityMap().keySet()) {
				String tempKey = keys;
				if (javaModel.getInterfaceList().contains(tempKey.split("\\~")[1])) {
					usesRelation += "[" + tempKey.split("\\~")[0] + "]" + javaModel.getMultiplicityMap().get(tempKey)
							+ "[<<interface>>;" + tempKey.split("\\~")[1] + "],";
				} else {
					usesRelation += "[" + tempKey.split("\\~")[0] + "]" + javaModel.getMultiplicityMap().get(tempKey)
							+ "[" + tempKey.split("\\~")[1] + "],";
				}
				javaModel.setUsesRelation(usesRelation);
			}
		}
		for (String key : javaModel.getClassInterfaceMap().keySet()) {
			String tempKey = key;
			List<ClassOrInterfaceType> tempList = javaModel.getClassInterfaceMap().get(tempKey);
			if (tempList != null) {
				for (int i = 0; i < tempList.size(); i++) {
					if (!tempList.isEmpty()) {
						if (tempList.get(i) != null) {
							grammar = grammar.append("[<<interface>>;" + tempList.get(i) + "]^-.-[" + tempKey + "],");
						}

					}
				}
			}
		}

		// combine class inheritance
		for (String key : javaModel.getClassSuperClassMap().keySet()) {
			String tempKey = key;
			List<ClassOrInterfaceType> tempList = javaModel.getClassSuperClassMap().get(tempKey);

			if (tempList != null) {
				for (int i = 0; i < tempList.size(); i++) {
					if (!tempList.isEmpty()) {
						if (tempList.get(i) != null) {
							grammar = grammar.append("[" + tempList.get(i) + "]^-[" + tempKey + "],");
						}

					}
				}
			}
		}

		grammar.append(javaModel.getUsesRelation());
		grammar.append("");
		grammar.deleteCharAt(grammar.length() - 1);
		return grammar.toString();
	}

	// Method to create the UML diagram and
	// create a .png file of the diagram at the test cases location
	public void createUMLDiagram(String grammar, String fullyQualifiedOutputFileName) {
		String yumlURLString = "https://yuml.me/diagram/scruffy/class/" + grammar + ".png";
		int count = 0;
		byte[] bufferArray = new byte[1024];
		try {

			URL url = new URL(yumlURLString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			OutputStream outputStream = new FileOutputStream(new File(fullyQualifiedOutputFileName));

			while ((count = conn.getInputStream().read(bufferArray)) != -1) {
				outputStream.write(bufferArray, 0, count);
			}
			outputStream.close();
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
