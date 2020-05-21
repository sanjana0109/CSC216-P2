/**
 * 
 */
package edu.ncsu.csc216.business.model.io;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ncsu.csc216.business.model.stakeholders.PropertyManager;

/**
 * Tests RentalReader class
 * 
 * @author Sanjana Cheerla
 * @author Kaitlyn Gosline
 *
 */
public class RentalReaderTest {

	/**
	 * Tests reading rental data from a file
	 */
	@Test
	public void testReadRentalData() {

		PropertyManager p = PropertyManager.getInstance();
		p.flushAllData();

		// Test reading invalid file
		try {
			RentalReader.readRentalData("test-files/invalid_file.md");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Unable to load file.", e.getMessage());
		}

		p.flushAllData();

		// Test reading valid file
		RentalReader.readRentalData("test-files/sample.md");
		assertEquals(19, p.listRentalUnits().length);
	}

}
