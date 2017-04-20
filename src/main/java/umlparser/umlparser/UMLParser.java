/**
 * 
 */
package umlparser.umlparser;

import java.io.IOException;

import com.github.javaparser.ParseException;

/**
 * @author Swati Gupta San Jose State University Spring 2017 CMPE 202
 */
public class UMLParser {

	/**
	 * @param args
	 * @throws ParseException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException, ParseException {

		if ((args.length < 2) || (args.length > 2 && args.length < 4) || (args.length > 4)) {
			System.out.println("Incorrect arguments.");
			return;
		}

		// User inputs
		String inputFilesPath = args[0];
		String outputFileName = args[1];
		String className = "";
		String functionName = "";

		if (args.length > 2) {
			if (null != args[2]) {
				className = args[2];
			}

			if (null != args[3]) {
				functionName = args[3];
			}
		}

		if (inputFilesPath.length() > 0 && outputFileName.length() > 0 && className.isEmpty()
				&& functionName.isEmpty()) {
			// Invoke method to generate the UML diagram
			UMLGenerator umlGenerator = new UMLGenerator();
			umlGenerator.generate(inputFilesPath, outputFileName);
		} else if (inputFilesPath.length() > 0 && outputFileName.length() > 0 && className.length() > 0
				&& functionName.length() > 0) {
			// Invoke method to generate the Sequence diagram
			SequenceGenerator sequenceGenerator = new SequenceGenerator();
			sequenceGenerator.generate(inputFilesPath, outputFileName, className, functionName);
		} else {
			System.out.println("Incorrect arguments. Please enter the arguments correctly.");
		}
	}

}
