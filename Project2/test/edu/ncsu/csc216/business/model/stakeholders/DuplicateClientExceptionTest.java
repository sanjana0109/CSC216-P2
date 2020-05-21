/**
 * 
 */
package edu.ncsu.csc216.business.model.stakeholders;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests the DuplicateClientException class
 * 
 * @author Sanjana Cheerla
 * @author Kaitlyn Gosline
 *
 */
public class DuplicateClientExceptionTest {

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.stakeholders.DuplicateClientException#DuplicateClientException(java.lang.String)}.
	 */
	@Test
	public void testDuplicateClientException() {
		DuplicateClientException e = new DuplicateClientException("test message");
		assertEquals("test message", e.getMessage());
	}

}
