/**
 * 
 */
package umlparser.umlparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

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
		createClassOrInterfaceMap(javaModel.getCompilationUnitArrayList());
		for (CompilationUnit cu : javaModel.getCompilationUnitArrayList()) {
			grammar += createGrammar(cu);
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
		javaModel.setCompilationUnitArrayList(compilationUnitArrayList);
	}

	private void createClassOrInterfaceMap(ArrayList<CompilationUnit> compilationUnitArrayList) {
		HashMap<String, Boolean> classOrInterfaceMap = new HashMap<String, Boolean>();
		for (CompilationUnit compilationUnit : compilationUnitArrayList) {
			List<TypeDeclaration> typeDeclarationList = compilationUnit.getTypes();
			for (Node node : typeDeclarationList) {
				ClassOrInterfaceDeclaration coi = (ClassOrInterfaceDeclaration) node;
				// coi.isInterface(): false --> class, true --> interface
				classOrInterfaceMap.put(coi.getName(), coi.isInterface()); 
			}
		}
		javaModel.setClassOrInterfaceMap(classOrInterfaceMap);
	}
	
	private String createGrammar(CompilationUnit cu) {
		String grammar = "";
		
		
		
		return grammar;
	}

}
