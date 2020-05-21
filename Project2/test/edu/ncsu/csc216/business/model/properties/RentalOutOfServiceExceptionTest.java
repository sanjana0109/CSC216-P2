/**
 * 
 */
package edu.ncsu.csc216.business.model.properties;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests RentalOutOfServiceException class
 * 
 * @author Sanjana Cheerla
 * @author Kaitlyn Gosline
 *
 */
public class RentalOutOfServiceExceptionTest {

	/**
	 * Tests that RentalOutOfServiceException is created correctly Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.RentalOutOfServiceException#RentalOutOfServiceException(java.lang.String)}.
	 */
	@Test
	public void testRentalOutOfServiceException() {
		RentalOutOfServiceException exp = new RentalOutOfServiceException("Custom exception message");
		assertEquals("Custom exception message", exp.getMessage());
	}
}
