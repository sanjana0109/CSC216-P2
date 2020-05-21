/**
 * 
 */
package edu.ncsu.csc216.business.model.stakeholders;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import edu.ncsu.csc216.business.model.contracts.Lease;
import edu.ncsu.csc216.business.model.properties.Office;
import edu.ncsu.csc216.business.model.properties.RentalUnit;

/**
 * Test class for the Client class
 * 
 * @author Sanjana Cheerla
 * @author Kaitlyn Gosline
 *
 */
public class ClientTest {

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.stakeholders.Client#hashCode()}.
	 */
	@Test
	public void testClient() {
		Client c = new Client("Maddy Mae", "catsl#");
		assertNotNull(c);
		c = null;
		try {
			c = new Client("Maddy Mae", "@@");
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(c);
		}
		c = null;
		try {
			c = new Client("M@ddy Mae", "xx@");
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(c);
		}
		c = null;
		try {
			c = new Client("Maddy Mae", "xx");
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(c);
		}
		c = null;
		try {
			c = new Client("Maddy Mae", "xxx x");
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(c);
		}
		c = null;
		try {
			c = new Client("          ", "xxxx");
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(c);
		}
		c = null;
		c = new Client("Maddy Mae", "   xxxx");
		c = null;
		c = new Client("Maddy Mae", "xxxx   ");
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.stakeholders.Client#getName()}.
	 */
	@Test
	public void testGetName() {
		Client c = new Client("Maddy Mae", "catsl");
		assertEquals("Maddy Mae", c.getName());

		Client c1 = new Client("       Maddy Mae     ", "catsl");
		assertEquals("Maddy Mae", c1.getName());

		Client c2 = new Client("       Maddy Mae", "catsl");
		assertEquals("Maddy Mae", c2.getName());

		Client c3 = new Client("Maddy Mae     ", "catsl");
		assertEquals("Maddy Mae", c3.getName());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.stakeholders.Client#getId()}.
	 */
	@Test
	public void testGetId() {
		Client c = new Client("Maddy Mae", "catsl");
		assertEquals("catsl", c.getId());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.stakeholders.Client#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject() {
		Client c = new Client("Maddy Mae", "catsl");
		Client c1 = new Client("Maddy Mae", "catsl");
		assertTrue(c.equals(c1));
		Client c2 = new Client("Maddy Ma", "catsll");
		assertFalse(c.equals(c2));
		Client c3 = new Client("Maddy Mae", "cats");
		assertFalse(c.equals(c3));
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.stakeholders.Client#addNewLease(edu.ncsu.csc216.business.model.contracts.Lease)}.
	 */
	@Test
	public void testAddNewLease() {
		Client c = new Client("Maddy Mae", "catsl#");
		RentalUnit r = new Office("2-56", 5);
		LocalDate start = LocalDate.parse("2020-10-01");
		LocalDate end = LocalDate.parse("2020-10-31");
		Lease l = new Lease(11, c, r, start, end, 2);

		c.addNewLease(l);

		assertEquals(1, c.listLeases().length);
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.stakeholders.Client#listLeases()}.
	 */
	@Test
	public void testListLeases() {
		Client c = new Client("Maddy Mae", "catsl#");
		RentalUnit r = new Office("2-56", 5);
		LocalDate start = LocalDate.parse("2020-10-01");
		LocalDate end = LocalDate.parse("2020-10-31");
		Lease l = new Lease(11, c, r, start, end, 2);

		c.addNewLease(l);

		String[] leases = c.listLeases();

		assertEquals("000011 | 2020-10-01 to 2020-10-31 |   2 | Office:           2-56 ", leases[0]);
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.stakeholders.Client#cancelLeaseAt(int)}.
	 */
	@Test
	public void testCancelLeaseAt() {
		Client c = new Client("Maddy Mae", "catsl#");
		RentalUnit r = new Office("2-56", 5);
		LocalDate start = LocalDate.parse("2020-10-01");
		LocalDate end = LocalDate.parse("2020-10-31");
		Lease l = new Lease(11, c, r, start, end, 2);

		c.addNewLease(l);
		assertEquals(1, c.listLeases().length);
		c.cancelLeaseAt(0);
		assertEquals(0, c.listLeases().length);
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.stakeholders.Client#cancelLeaseWithNumber(int)}.
	 */
	@Test
	public void testCancelLeaseWithNumber() {
		Client c = new Client("Maddy Mae", "catsl#");
		RentalUnit r = new Office("2-56", 5);
		LocalDate start = LocalDate.parse("2020-10-01");
		LocalDate end = LocalDate.parse("2020-10-31");
		Lease l = new Lease(11, c, r, start, end, 2);

		c.addNewLease(l);
		assertEquals(1, c.listLeases().length);
		c.cancelLeaseWithNumber(11);
		assertEquals(0, c.listLeases().length);

	}

}
