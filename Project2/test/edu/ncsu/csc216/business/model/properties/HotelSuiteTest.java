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
 * Test class for HotelSuite class
 * 
 * @author Sanjana Cheerla
 * @author Kaitlyn Gosline
 *
 */
public class HotelSuiteTest {

	/** Test Capacity for Hotel Suite */
	public static final int CAPACITY_1 = 1;
	/** Test Capacity for Hotel Suite */
	public static final int CAPACITY_2 = 2;
	/** Test Location for Hotel Suite */
	public static final String LOCATION_1 = "2-22";
	/** Floor as an integer */
	public static final int FLOOR = 2;
	/** Room as an integer */
	public static final int ROOM = 22;
	/** Hotel Suite Description */
	public static final String DESCRIPTION = "Hotel Suite:      2-22 |   2";
	/** client obj used for testing */
	public static Client c = new Client("Maddy Mae", "catsl");

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.HotelSuite#HotelSuite(java.lang.String)}.
	 */
	@Test
	public void testHotelSuiteString() {
		HotelSuite suite = new HotelSuite(LOCATION_1);
		assertEquals(CAPACITY_1, suite.getCapacity());
		assertEquals(FLOOR, suite.getFloor());
		assertEquals(ROOM, suite.getRoom());
		HotelSuite suite1 = null;
		try {
			suite1 = new HotelSuite(LOCATION_1, 26);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(suite1);
		}
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.HotelSuite#HotelSuite(java.lang.String, int)}.
	 */
	@Test
	public void testHotelSuiteStringInt() {
		HotelSuite suite = new HotelSuite(LOCATION_1, CAPACITY_2);
		assertEquals(CAPACITY_2, suite.getCapacity());
		assertEquals(FLOOR, suite.getFloor());
		assertEquals(ROOM, suite.getRoom());
		HotelSuite suite1 = null;
		try {
			suite1 = new HotelSuite(LOCATION_1, 26);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(suite1);
		}
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.HotelSuite#reserve(edu.ncsu.csc216.business.model.stakeholders.Client, java.time.LocalDate, int, int)}.
	 */
	@Test
	public void testReserve() {
		// Valid Lease
		HotelSuite suite = new HotelSuite(LOCATION_1, CAPACITY_2);
		Lease l1 = new Lease(0, c, suite, LocalDate.parse("2020-01-05"), LocalDate.parse("2020-01-19"), 2);
		String[] l1Arr = l1.leaseData();

		// Valid Test
		try {
			Lease l = suite.reserve(c, LocalDate.parse("2020-01-05"), 2, 2);
			String[] lArr = l.leaseData();
			for (int i = 1; i < 6; i++) {
				assertEquals(l1Arr[i], lArr[i]);
			}
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		Lease l = null;
		try {
			l = suite.reserve(c, LocalDate.parse("2020-01-12"), 1, 2);
			fail();
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException e) {
			assertNull(l);
		}

		// Invalid Start Date (not Sunday)
		try {
			l = suite.reserve(c, LocalDate.parse("2020-02-01"), 1, 2);
			fail();
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException
				| IllegalArgumentException e) {
			assertNull(l);
		}

		// Invalid Number of Occupants (Negative)
		try {
			l = suite.reserve(c, LocalDate.parse("2020-02-02"), 2, -1);
			fail();
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException
				| IllegalArgumentException e) {
			assertNull(l);
		}

		// Invalid Duration
		try {
			l = suite.reserve(c, LocalDate.parse("2020-02-02"), -1, 2);
			fail();
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException
				| IllegalArgumentException e) {
			assertNull(l);
		}

		// Invalid Number of Occupants (Greater than 2)
		try {
			l = suite.reserve(c, LocalDate.parse("2020-02-02"), 2, 8);
			fail();
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException
				| IllegalArgumentException e) {
			assertNull(l);
		}
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.HotelSuite#recordExistingLease(int, edu.ncsu.csc216.business.model.stakeholders.Client, java.time.LocalDate, java.time.LocalDate, int)}.
	 */
	@Test
	public void testRecordExistingLease() {
		HotelSuite suite = new HotelSuite(LOCATION_1, CAPACITY_2);
		Lease l1 = new Lease(0, c, suite, LocalDate.parse("2020-01-05"), LocalDate.parse("2020-01-12"), 2);

		// Valid Test
		try {
			Lease l = suite.recordExistingLease(0, c, LocalDate.parse("2020-01-05"), LocalDate.parse("2020-01-12"), 2);
			assertEquals(0, l1.compareTo(l));
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException e) {
			fail("should not reach this point");
		}

		Lease l = null;
		// Invalid End Date
		try {
			l = suite.recordExistingLease(0, c, LocalDate.parse("2020-02-02"), LocalDate.parse("2020-02-07"), 2);
			fail();
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException e) {
			assertNull(l);
		}

		// Invalid Number of Occupants (Greater than 2)
		try {
			l = suite.recordExistingLease(0, c, LocalDate.parse("2020-02-02"), LocalDate.parse("2020-02-09"), 8);
			fail();
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException
				| IllegalArgumentException e) {
			assertNull(l);
		}

		// Invalid Number of Occupants (Negative)
		try {
			l = suite.recordExistingLease(0, c, LocalDate.parse("2020-02-01"), LocalDate.parse("2020-02-06"), -1);
			fail();
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException
				| IllegalArgumentException e) {
			assertNull(l);
		}

	}

	/**
	 * Test for check same day and reserve
	 */
	@Test
	public void testSameDayReserves() {
		HotelSuite s = new HotelSuite("1-22", 2);
		/** Lease obj 1 testing */
		Lease l1 = new Lease(0, c, s, LocalDate.parse("2020-01-01"), LocalDate.parse("2020-02-01"), 2);
		/** Lease obj 2 testing */
		Lease l2 = new Lease(1, c, s, LocalDate.parse("2020-02-02"), LocalDate.parse("2020-03-01"), 2);
		/** Lease obj 3 testing */
		Lease l3 = new Lease(2, c, s, LocalDate.parse("2020-03-02"), LocalDate.parse("2020-04-01"), 2);
		s.addLease(l1);
		s.addLease(l2);
		s.addLease(l3);

		assertEquals(3, s.myLeases.size());

		try {
			s.reserve(c, LocalDate.parse("2020-02-01"), 3, 3);
			fail("This message should not occur");
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException
				| IllegalArgumentException e) {
			assertEquals(3, s.myLeases.size());
		}
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.HotelSuite#removeFromServiceStarting(java.time.LocalDate)}.
	 */
	@Test
	public void testRemoveFromServiceStarting() {
		HotelSuite suite = new HotelSuite(LOCATION_1, CAPACITY_2);
		/** Lease obj 1 testing */
		Lease l1 = new Lease(0, c, suite, LocalDate.parse("2020-01-05"), LocalDate.parse("2020-02-09"), 2);
		/** Lease obj 2 testing */
		Lease l2 = new Lease(1, c, suite, LocalDate.parse("2020-02-02"), LocalDate.parse("2020-02-09"), 2);
		/** Lease obj 3 testing */
		Lease l3 = new Lease(2, c, suite, LocalDate.parse("2020-03-01"), LocalDate.parse("2020-03-08"), 2);
		suite.returnToService();

		suite.addLease(l1);
		suite.addLease(l2);
		suite.addLease(l3);
		assertEquals(3, suite.myLeases.size());
		assertEquals(3, suite.listLeases().length);

		suite.removeFromServiceStarting(LocalDate.parse("2020-02-01"));
		assertEquals(1, suite.myLeases.size());
		assertEquals(1, suite.listLeases().length);
		assertEquals(LocalDate.parse("2020-01-26"), l1.getEnd());

	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.HotelSuite#getDescription()}.
	 */
	@Test
	public void testGetDescription() {
		HotelSuite suite = new HotelSuite(LOCATION_1, CAPACITY_2);
		assertEquals(DESCRIPTION, suite.getDescription());
	}

}
