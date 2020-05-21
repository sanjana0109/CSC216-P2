/**
 * 
 */
package edu.ncsu.csc216.business.model.properties;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import edu.ncsu.csc216.business.model.contracts.Lease;
import edu.ncsu.csc216.business.model.stakeholders.Client;

/**
 * Test class for office class
 * 
 * @author Sanjana Cheerla
 * @author Kaitlyn Gosline
 *
 */
public class OfficeTest {

	/** Test Capacity for Office */
	public static final int CAPACITY_1 = 100;
	/** Test Location for Office */
	public static final String LOCATION_1 = "2-22";
	/** Floor as an integer */
	public static final int FLOOR = 2;
	/** Room as an integer */
	public static final int ROOM = 22;
	/** client obj used for testing */
	public static Client c = new Client("Maddy Mae", "catsl");

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.Office#Office(java.lang.String, int)}.
	 */
	@Test
	public void testOffice() {
		Office office = new Office(LOCATION_1, CAPACITY_1);
		assertEquals(CAPACITY_1, office.getCapacity());
		assertEquals(FLOOR, office.getFloor());
		assertEquals(ROOM, office.getRoom());
		Office office1 = null;
		try {
			office1 = new Office(LOCATION_1, 155);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(office1);
		}
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.Office#reserve(edu.ncsu.csc216.business.model.stakeholders.Client, java.time.LocalDate, int, int)}.
	 */
	@Test
	public void testReserve() {
		// Valid Lease
		Office office = new Office(LOCATION_1, CAPACITY_1);
		Lease l1 = new Lease(0, c, office, LocalDate.parse("2020-01-01"), LocalDate.parse("2020-02-29"), 90);
		String[] l1Arr = l1.leaseData();

		// Valid Test
		try {
			Lease l = office.reserve(c, LocalDate.parse("2020-01-01"), 2, 90);
			String[] lArr = l.leaseData();
			for (int i = 1; i < 6; i++) {
				assertEquals(l1Arr[i], lArr[i]);
			}
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		Lease l = null;

		// Invalid Start Date (not beginning of month)
		try {
			l = office.reserve(c, LocalDate.parse("2020-03-11"), 2, 100);
			fail();
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException
				| IllegalArgumentException e) {
			assertNull(l);
		}

		// Invalid Number of Occupants (Negative)
		try {
			l = office.reserve(c, LocalDate.parse("2020-03-01"), 2, -1);
			fail();
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException
				| IllegalArgumentException e) {
			assertNull(l);
		}

		// Invalid Duration
		try {
			l = office.reserve(c, LocalDate.parse("2020-03-01"), -1, 2);
			fail();
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException
				| IllegalArgumentException e) {
			assertNull(l);
		}

		// Invalid Number of Occupants (Greater than 2)
		try {
			l = office.reserve(c, LocalDate.parse("2020-03-01"), 2, 155);
			fail();
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException
				| IllegalArgumentException e) {
			assertNull(l);
		}
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.Office#recordExistingLease(int, edu.ncsu.csc216.business.model.stakeholders.Client, java.time.LocalDate, java.time.LocalDate, int)}.
	 */
	@Test
	public void testRecordExistingLease() {
		Office office = new Office(LOCATION_1, CAPACITY_1);
		Lease l1 = new Lease(0, c, office, LocalDate.parse("2020-01-01"), LocalDate.parse("2020-02-29"), 90);

		// Valid Test
		try {
			Lease l = office.recordExistingLease(0, c, LocalDate.parse("2020-01-01"), LocalDate.parse("2020-02-29"),
					90);
			assertEquals(0, l1.compareTo(l));
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException e) {
			fail("should not reach this point");
		}

		Lease l = null;
		// Invalid End Date
		try {
			l = office.recordExistingLease(0, c, LocalDate.parse("2020-02-01"), LocalDate.parse("2020-02-20"), 2);
			fail();
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException e) {
			assertNull(l);
		}

		// Invalid Number of Occupants (Greater than 150)
		try {
			l = office.recordExistingLease(0, c, LocalDate.parse("2020-02-01"), LocalDate.parse("2020-02-29"), 155);
			fail();
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException
				| IllegalArgumentException e) {
			assertNull(l);
		}

		// Invalid Number of Occupants (Negative)
		try {
			l = office.recordExistingLease(0, c, LocalDate.parse("2020-02-01"), LocalDate.parse("2020-02-29"), -1);
			fail();
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException
				| IllegalArgumentException e) {
			assertNull(l);
		}
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.Office#removeFromServiceStarting(java.time.LocalDate)}.
	 */
	@Test
	public void testRemoveFromServiceStarting() {
		Office office = new Office(LOCATION_1, CAPACITY_1);
		/** Lease obj 1 testing */
		Lease l1 = new Lease(0, c, office, LocalDate.parse("2020-01-01"), LocalDate.parse("2020-03-31"), 20);
		/** Lease obj 2 testing */
		Lease l2 = new Lease(1, c, office, LocalDate.parse("2020-02-01"), LocalDate.parse("2020-02-29"), 20);
		/** Lease obj 3 testing */
		Lease l3 = new Lease(2, c, office, LocalDate.parse("2020-03-01"), LocalDate.parse("2020-03-31"), 20);
		office.returnToService();

		office.addLease(l1);
		office.addLease(l2);
		office.addLease(l3);
		assertEquals(3, office.myLeases.size());
		assertEquals(3, office.listLeases().length);

		office.removeFromServiceStarting(LocalDate.parse("2020-02-01"));
		assertEquals(1, office.myLeases.size());
		assertEquals(1, office.listLeases().length);
		assertEquals(LocalDate.parse("2020-01-31"), l1.getEnd());

	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.Office#remainingCapacityFor(java.time.LocalDate)}.
	 */
	@Test
	public void testRemainingCapacityFor() {
		Office office = new Office(LOCATION_1, CAPACITY_1);
		try {
			office.reserve(c, LocalDate.parse("2020-01-01"), 2, 90);
			assertEquals(10, office.remainingCapacityFor(LocalDate.parse("2020-01-01")));
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.Office#getMonthsDuration(java.time.LocalDate, java.time.LocalDate)}.
	 */
	@Test
	public void testGetMonthsDuration() {
		assertEquals(2, Office.getMonthsDuration(LocalDate.parse("2020-01-01"), LocalDate.parse("2020-02-04")));
		assertEquals(13, Office.getMonthsDuration(LocalDate.parse("2020-01-05"), LocalDate.parse("2021-01-20")));
	}

}
