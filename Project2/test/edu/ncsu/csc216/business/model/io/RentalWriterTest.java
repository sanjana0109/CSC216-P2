/**
 * 
 */
package edu.ncsu.csc216.business.model.io;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.junit.Test;

import edu.ncsu.csc216.business.model.stakeholders.DuplicateClientException;
import edu.ncsu.csc216.business.model.stakeholders.DuplicateRoomException;
import edu.ncsu.csc216.business.model.stakeholders.PropertyManager;

/**
 * Tests RentalWriter class
 * 
 * @author Kaitlyn Gosline
 * @author Sanjana Cheerla
 *
 */
public class RentalWriterTest {

	/**
	 * Tests writing rental data to a file. Test method for
	 * {@link edu.ncsu.csc216.business.model.io.RentalWriter#writeRentalFile(java.lang.String)}.
	 */
	@Test
	public void testWriteRentalFile() {
		PropertyManager.getInstance().flushAllData();

		// test to invalid file
		try {
			RentalWriter.writeRentalFile(null);
		} catch (IllegalArgumentException e) {
			assertEquals("Unable to save file", e.getMessage());
		}

		// Adding valid unit
		try {
			PropertyManager.getInstance().addNewUnit("Conference Room", "38-67", 20);
		} catch (DuplicateRoomException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		// Adding valid unit
		try {
			PropertyManager.getInstance().addNewUnit("Hotel Suite", "1-99", 1);
		} catch (DuplicateRoomException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		// Adding valid client
		try {
			PropertyManager.getInstance().addNewClient("Amanda Smith", "a12#smL");
		} catch (DuplicateClientException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		RentalWriter.writeRentalFile("test-files/simple.md");
		checkFiles("test-files/simple_expected.md", "test-files/simple.md");

	}

	/**
	 * Helper method to compare two files for the same contents
	 * 
	 * @param expFile expected output
	 * @param actFile actual output
	 */
	private void checkFiles(String expFile, String actFile) {
		try {
			Scanner expScanner = new Scanner(new File(expFile));
			Scanner actScanner = new Scanner(new File(actFile));

			while (expScanner.hasNextLine()) {
				assertEquals(expScanner.nextLine(), actScanner.nextLine());
			}

			expScanner.close();
			actScanner.close();
		} catch (IOException e) {
			fail("Error reading files.");
		}
	}

}
