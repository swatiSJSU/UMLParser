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
			// grammar += createGrammar(cu);
			grammar = createGrammar(cu);
		}

		String fullyQualifiedOutputFileName = inputFilesPath + "\\" + outputFileName + ".png";
		createUMLDiagram(grammar, fullyQualifiedOutputFileName);
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

	// Method to create the grammar
	private String createGrammar(CompilationUnit cu) {
		String grammar = "";

		grammar = "[<<interface>>;A1]," + "[<<interface>>;A2]," + "[B1]," + "[B1] -^ [P],"
				+ "[B1] -.-^ [<<interface>>;A1]," + "[B2],[B2] -^ [P]," + "[B2] -.-^ [<<interface>>;A1],"
				+ "[B2] -.-^ [<<interface>>;A2]," + "[C1|+ test(a1 : A1) : void]," + "[C1] uses -.->[<<interface>>;A1],"
				+ "[C2|+ test(a2 : A2) : void]," + "[C2] uses -.->[<<interface>>;A2]," + "[P]";

		return grammar;
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
