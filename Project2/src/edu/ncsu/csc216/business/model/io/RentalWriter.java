/**
 * 
 */
package edu.ncsu.csc216.business.model.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import edu.ncsu.csc216.business.model.stakeholders.PropertyManager;

/**
 * Writes rental data to a file
 * 
 * @author Kaitlyn Gosline
 * @author Sanjana Cheerla
 *
 */
public class RentalWriter {

	/**
	 * Writes rental data to a given file
	 * 
	 * @param filename the name of the file to write to
	 * @throws IllegalArgumentException with the message "Unable to save file" if
	 *                                  file fails to be saved
	 */
	public static void writeRentalFile(String filename) {
		PrintStream fileWriter;
		if (filename == null) { throw new IllegalArgumentException("Unable to save file"); }
		try { fileWriter = new PrintStream(new File(filename)); } 
		catch (NullPointerException | FileNotFoundException e) {
			throw new IllegalArgumentException("Unable to save file");
		}
		String rentalUnits = "";
		for (int i = 0; i < PropertyManager.getInstance().listRentalUnits().length; i++) {
			rentalUnits += PropertyManager.getInstance().listRentalUnits()[i] + "\n";
		}
		String clients = "";
		for (int i = 0; i < PropertyManager.getInstance().listClients().length; i++) {
			clients += "#" + PropertyManager.getInstance().listClients()[i] + "\n";
			for (int j = 0; j < PropertyManager.getInstance().listClientLeases(i).length; j++) {
				clients += "   " + PropertyManager.getInstance().listClientLeases(i)[j] + "\n";

			}
		}
		fileWriter.println(rentalUnits);
		fileWriter.print(clients);
		fileWriter.close();
	}

}
