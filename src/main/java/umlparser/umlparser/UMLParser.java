/**
 * 
 */
package umlparser.umlparser;

import java.io.IOException;

import com.github.javaparser.ParseException;

/**
 * @author Swati Gupta
 * San Jose State University
 * Spring 2017
 * CMPE 202
 */
public class UMLParser {

	/**
	 * @param args
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, ParseException {
		// User inputs
		String inputFilesPath = args[0];
		String outputFileName = args[1];
		
		// Invoke method to generate the UML diagram
		UMLGenerator umlGenerator = new UMLGenerator();
		umlGenerator.generate(inputFilesPath, outputFileName);
		
		// Invoke method to generate the Sequence diagram
		//SequenceGenerator sequenceGenerator = new SequenceGenerator();
		//sequenceGenerator.generate(inputFilesPath, outputFileName);
		
	}

}
