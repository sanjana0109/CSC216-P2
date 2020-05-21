/**
 * 
 */
package edu.ncsu.csc216.business.model.contracts;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc216.business.model.properties.Office;
import edu.ncsu.csc216.business.model.properties.RentalUnit;
import edu.ncsu.csc216.business.model.stakeholders.Client;

/**
 * Tests the Lease class
 * 
 * 
 * @author Sanjana Cheerla
 * @author Kaitlyn Gosline
 *
 */
public class LeaseTest {

	/** Client obj used for testing */
	public Client c = new Client("Maddy Mae", "catsl");

	/** Rental Unit obj used for testing */
	public RentalUnit r = new Office("2-56", 5);

	/** Start Date used for testing */
	public LocalDate start = LocalDate.parse("2020-01-01");

	/** Start Date used for testing */
	public LocalDate start1 = LocalDate.parse("2020-03-11");

	/** End Date used for testing */
	public LocalDate end = LocalDate.parse("2020-02-10");

	/** End Date used for testing */
	public LocalDate end1 = LocalDate.parse("2020-04-10");

	/**
	 * Before method, resets confirmation numbering for Lease.
	 */
	@Before
	public void beforeTests() {
		Lease.resetConfirmationNumbering(0);
	}

	/**
	 * Tests creating a lease without a confirmation number. Test method for
	 * {@link edu.ncsu.csc216.business.model.contracts.Lease#Lease(edu.ncsu.csc216.business.model.stakeholders.Client, edu.ncsu.csc216.business.model.properties.RentalUnit, java.time.LocalDate, java.time.LocalDate, int)}.
	 */
	@Test
	public void testLeaseClientRentalUnitLocalDateLocalDateInt() {
		Lease l = new Lease(c, r, start, end, 2);
		assertNotNull(l);
	}

	/**
	 * Tests creating a lease with a confirmation number. Test method for
	 * {@link edu.ncsu.csc216.business.model.contracts.Lease#Lease(int, edu.ncsu.csc216.business.model.stakeholders.Client, edu.ncsu.csc216.business.model.properties.RentalUnit, java.time.LocalDate, java.time.LocalDate, int)}.
	 */
	@Test
	public void testLeaseIntClientRentalUnitLocalDateLocalDateInt() {
		Lease l = new Lease(11, c, r, start, end, 2);
		assertNotNull(l);
	}

	/**
	 * Tests getting the confirmation number of a lease. Test method for
	 * {@link edu.ncsu.csc216.business.model.contracts.Lease#getConfirmationNumber()}
	 */
	@Test
	public void testGetConfirmationNumber() {
		Lease l = new Lease(11, c, r, start, end, 2);
		assertEquals(11, l.getConfirmationNumber());
		Lease l1 = new Lease(c, r, start1, end1, 2);
		assertEquals(12, l1.getConfirmationNumber());
		Lease l3 = new Lease(c, r, start, end, 2);
		assertEquals(13, l3.getConfirmationNumber());
		Lease l4 = new Lease(c, r, start1, end1, 2);
		assertEquals(14, l4.getConfirmationNumber());
	}

	/**
	 * Tests getting the client of a lease. Test method for
	 * {@link edu.ncsu.csc216.business.model.contracts.Lease#getClient()}.
	 */
	@Test
	public void testGetClient() {
		Lease l = new Lease(11, c, r, start, end, 2);
		assertEquals(c, l.getClient());
	}

	/**
	 * Tests getting the rental unit of a lease. Test method for
	 * {@link edu.ncsu.csc216.business.model.contracts.Lease#getProperty()}.
	 */
	@Test
	public void testGetProperty() {
		Lease l = new Lease(11, c, r, start, end, 2);
		assertEquals(r, l.getProperty());
	}

	/**
	 * Tests getting the start date of a lease. Test method for
	 * {@link edu.ncsu.csc216.business.model.contracts.Lease#getStart()}.
	 */
	@Test
	public void testGetStart() {
		Lease l = new Lease(11, c, r, start, end, 2);
		assertEquals(start, l.getStart());
	}

	/**
	 * Tests getting the end date of lease. Test method for
	 * {@link edu.ncsu.csc216.business.model.contracts.Lease#getEnd()}.
	 */
	@Test
	public void testGetEnd() {
		Lease l = new Lease(11, c, r, start, end, 2);
		assertEquals(end, l.getEnd());
	}

	/**
	 * Tests getting the number of occupants on a lease. Test method for
	 * {@link edu.ncsu.csc216.business.model.contracts.Lease#getNumOccupants()}.
	 */
	@Test
	public void testGetNumOccupants() {
		Lease l = new Lease(11, c, r, start, end, 2);
		assertEquals(2, l.getNumOccupants());
	}

	/**
	 * Tests setting a date for a lease earlier. Test method for
	 * {@link edu.ncsu.csc216.business.model.contracts.Lease#setEndDateEarlier(java.time.LocalDate)}.
	 */
	@Test
	public void testSetEndDateEarlier() {
		Lease l = new Lease(11, c, r, start, end1, 2);
		try {
			l.setEndDateEarlier(null);
		} catch (IllegalArgumentException e) {
			assertEquals(null, e.getMessage());
		}
		l.setEndDateEarlier(end);
		assertEquals(end, l.getEnd());
	}

	/**
	 * Tests getting lease data for lease. Test method for
	 * {@link edu.ncsu.csc216.business.model.contracts.Lease#leaseData()}.
	 */
	@Test
	public void testLeaseData() {
		Lease l = new Lease(11, c, r, start, end, 2);
		String[] lArray = l.leaseData();
		assertEquals("000011", lArray[0]);
		assertEquals("2020-01-01 to 2020-02-10", lArray[1]);
		assertEquals("2", lArray[2]);
		assertEquals("Office:           2-56 ", lArray[3]);
		assertEquals("Maddy Mae", lArray[4]);
		assertEquals("catsl", lArray[5]);
	}

	/**
	 * Tests comparing two leases. Test method for
	 * {@link edu.ncsu.csc216.business.model.contracts.Lease#compareTo(edu.ncsu.csc216.business.model.contracts.Lease)}.
	 */
	@Test
	public void testCompareTo() {
		Lease l = new Lease(11, c, r, start, end, 2);
		Lease l1 = new Lease(c, r, start1, end1, 2);

		assertEquals(-1, l.compareTo(l1));
		assertEquals(0, l.compareTo(l));
	}

}
