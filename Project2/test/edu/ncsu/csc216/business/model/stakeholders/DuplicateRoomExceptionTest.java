/**
 * 
 */
package edu.ncsu.csc216.business.model.stakeholders;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests the DuplicateRoomException class
 * 
 * @author Sanjana Cheerla
 * @author Kaitlyn Gosline
 *
 */
public class DuplicateRoomExceptionTest {

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.stakeholders.DuplicateRoomException#DuplicateRoomException(java.lang.String)}.
	 */
	@Test
	public void testDuplicateRoomException() {
		DuplicateRoomException e = new DuplicateRoomException("test message");
		assertEquals("test message", e.getMessage());
	}

}
