/**
 * 
 */
package edu.ncsu.csc216.business.model.properties;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests RentalCapacityException class
 * 
 * @author Sanjana Cheerla
 * @author Kaitlyn Gosline
 *
 */
public class RentalCapacityExceptionTest {

	/**
	 * Tests that RentalCapacityException is created correctly Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.RentalCapacityException#RentalCapacityException(java.lang.String)}.
	 */
	@Test
	public void testRentalCapacityException() {
		RentalCapacityException exp = new RentalCapacityException("Custom exception message");
		assertEquals("Custom exception message", exp.getMessage());
	}

}
