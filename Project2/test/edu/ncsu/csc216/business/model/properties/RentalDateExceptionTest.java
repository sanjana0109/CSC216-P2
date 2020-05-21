/**
 * 
 */
package edu.ncsu.csc216.business.model.properties;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests RentalDateException class
 * 
 * @author Sanjana Cheerla
 * @author Kaitlyn Gosline
 *
 */
public class RentalDateExceptionTest {

	/**
	 * Tests that RentalDateException is created correctly Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.RentalDateException#RentalDateException(java.lang.String)}.
	 */
	@Test
	public void testRentalDateException() {
		RentalDateException exp = new RentalDateException("Custom exception message");
		assertEquals("Custom exception message", exp.getMessage());
	}

}
