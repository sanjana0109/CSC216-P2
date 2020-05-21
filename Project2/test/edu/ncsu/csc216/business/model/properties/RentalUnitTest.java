/**
 * 
 */
package edu.ncsu.csc216.business.model.properties;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.Month;

import org.junit.Test;

import edu.ncsu.csc216.business.model.contracts.Lease;
import edu.ncsu.csc216.business.model.stakeholders.Client;

/**
 * Test class for RentalUnit
 * 
 * @author sanjana cheerla
 * @author Kaitlyn Gosline
 *
 */
public class RentalUnitTest {

	/** Test Capacity for Rental Unit */
	public static final int CAPACITY_1 = 10;
	/** Test Capacity for Rental Unit */
	public static final int CAPACITY_2 = 8;
	/** Test Location for Rental Unit */
	public static final String LOCATION_1 = "1-22";
	/** Test Location for Rental Unit */
	public static final String LOCATION_2 = "4-22";
	/** Floor as an integer */
	public static final int FLOOR = 1;
	/** Room as an integer */
	public static final int ROOM = 22;
	/** Test Date */
	public static final LocalDate DATE_1 = LocalDate.of(2021, Month.JANUARY, 20);
	/** Test Date */
	public static final LocalDate DATE_2 = LocalDate.of(2021, Month.JULY, 15);
	/** Test Date */
	public static final LocalDate DATE_3 = LocalDate.of(2019, Month.JULY, 15);
	/** Test Date */
	public static final LocalDate DATE_4 = LocalDate.of(2030, Month.JULY, 15);
	/** Unit1 Description */
	public static final String DESCRIPTION = "Conference Room:  1-22 |  10";
	/** Client Name */
	public static final String CLIENT_NAME = "Kaitlyn Gosline";
	/** Client Id */
	public static final String CLIENT_ID = "@kfgoslin";
	/** Valid Client */
	public static final Client VALID_CLIENT = new Client(CLIENT_NAME, CLIENT_ID);
	/** Valid Duration of Lease */
	public static final int VALID_DURATION = 3;
	/** Invalid Duration of Lease */
	public static final int INVALID_DURATION = 0;
	/** Valid Number of Occupants for Lease */
	public static final int VALID_OCCUPANT = 3;
	/** Invalid Number of Occupants for Lease */
	public static final int INVALID_OCCUPANT = 0;

	/** client obj used for testing */
	public static Client c = new Client("Maddy Mae", "catsl");

	/** rental unit obj used for testing */
	public static RentalUnit r = new Office("2-56", 5);
	/** Lease obj 1 testing */
	public static final Lease L1 = new Lease(0, c, r, LocalDate.parse("2020-01-01"), LocalDate.parse("2020-02-01"), 2);
	/** Lease obj 2 testing */
	public static final Lease L2 = new Lease(1, c, r, LocalDate.parse("2020-02-02"), LocalDate.parse("2020-03-01"), 2);
	/** Lease obj 3 testing */
	public static final Lease L3 = new Lease(2, c, r, LocalDate.parse("2020-03-02"), LocalDate.parse("2020-04-01"), 2);

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.RentalUnit#RentalUnit(java.lang.String, int)}.
	 */
	@Test
	public void testRentalUnit() {
		RentalUnit unit = new ConferenceRoom(LOCATION_1, CAPACITY_1);
		assertEquals(CAPACITY_1, unit.getCapacity());
		assertEquals(FLOOR, unit.getFloor());
		assertEquals(ROOM, unit.getRoom());

	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.RentalUnit#compareTo(edu.ncsu.csc216.business.model.properties.RentalUnit)}.
	 */
	@Test
	public void testCompareTo() {
		RentalUnit unit1 = new ConferenceRoom(LOCATION_1, CAPACITY_1);
		RentalUnit unit2 = new ConferenceRoom(LOCATION_2, CAPACITY_1);
		assertTrue(unit1.compareTo(unit2) < 0);
		assertEquals(0, unit1.compareTo(unit1));
		assertTrue(unit2.compareTo(unit1) > 0);

	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.RentalUnit#returnToService()}.
	 */
	@Test
	public void testReturnToService() {
		RentalUnit unit1 = new ConferenceRoom(LOCATION_1, CAPACITY_1);
		unit1.returnToService();
		assertTrue(unit1.isInService());

	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.RentalUnit#takeOutOfService()}.
	 */
	@Test
	public void testTakeOutOfService() {
		RentalUnit unit1 = new ConferenceRoom(LOCATION_1, CAPACITY_1);
		unit1.takeOutOfService();
		assertFalse(unit1.isInService());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.RentalUnit#checkDates(java.time.LocalDate, java.time.LocalDate)}.
	 */
	@Test
	public void testCheckDates() {
		RentalUnit unit1 = new ConferenceRoom(LOCATION_1, CAPACITY_1);
		try {
			unit1.checkDates(DATE_2, DATE_1);
			fail();
		} catch (RentalDateException e) {
			assertEquals("Start Date is after End Date", e.getMessage());
		}
		try {
			unit1.checkDates(DATE_3, DATE_2);
			fail();
		} catch (RentalDateException e) {
			assertEquals("Invalid Start Date", e.getMessage());
		}
		try {
			unit1.checkDates(DATE_1, DATE_4);
			fail();
		} catch (RentalDateException e) {
			assertEquals("Invalid End Date", e.getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.RentalUnit#checkLeaseConditions(edu.ncsu.csc216.business.model.stakeholders.Client, java.time.LocalDate, int, int)}.
	 */
	@Test
	public void testCheckLeaseConditions() {
		RentalUnit unit1 = new ConferenceRoom(LOCATION_1, CAPACITY_1);
		try {
			unit1.checkLeaseConditions(null, DATE_1, VALID_DURATION, VALID_OCCUPANT);
			fail();
		} catch (IllegalArgumentException | RentalOutOfServiceException e) {
			assertEquals(null, e.getMessage());
		}
		try {
			unit1.checkLeaseConditions(VALID_CLIENT, null, VALID_DURATION, VALID_OCCUPANT);
			fail();
		} catch (IllegalArgumentException | RentalOutOfServiceException e) {
			assertEquals(null, e.getMessage());
		}
		try {
			unit1.checkLeaseConditions(VALID_CLIENT, DATE_1, INVALID_DURATION, VALID_OCCUPANT);
			fail();
		} catch (IllegalArgumentException | RentalOutOfServiceException e) {
			assertEquals(null, e.getMessage());
		}
		try {
			unit1.checkLeaseConditions(VALID_CLIENT, DATE_1, VALID_DURATION, INVALID_OCCUPANT);
			fail();
		} catch (IllegalArgumentException | RentalOutOfServiceException e) {
			assertEquals(null, e.getMessage());
		}
		unit1.takeOutOfService();
		try {
			unit1.checkLeaseConditions(VALID_CLIENT, DATE_1, VALID_DURATION, VALID_OCCUPANT);
			fail();
		} catch (IllegalArgumentException | RentalOutOfServiceException e) {
			assertEquals("Unit is not available for leasing", e.getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.RentalUnit#removeFromServiceStarting(java.time.LocalDate)}.
	 */
	@Test
	public void testRemoveFromServiceStarting() {
		r.removeFromServiceStarting(LocalDate.parse("2020-01-01"));
		assertEquals(0, r.myLeases.size());
		assertEquals(0, r.listLeases().length);
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.RentalUnit#cutoffIndex(java.time.LocalDate)}.
	 */
	@Test
	public void testCutoffIndex() {
		r.removeFromServiceStarting(LocalDate.parse("2020-01-01"));
		r.returnToService();
		r.addLease(L1);
		r.addLease(L2);
		r.addLease(L3);

		assertEquals(3, r.myLeases.size());
		assertEquals(3, r.listLeases().length);
		
		assertEquals(0, r.cutoffIndex(LocalDate.parse("2020-01-01")));
		
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.RentalUnit#cancelLeaseByNumber(int)}.
	 */
	@Test
	public void testCancelLeaseByNumber() {
		r.removeFromServiceStarting(LocalDate.parse("2020-01-01"));
		r.returnToService();
		r.addLease(L1);
		r.addLease(L2);
		r.addLease(L3);

		assertEquals(3, r.myLeases.size());
		assertEquals(3, r.listLeases().length);
		r.cancelLeaseByNumber(0);
		assertEquals(2, r.myLeases.size());
		assertEquals(2, r.listLeases().length);
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.RentalUnit#addLease(edu.ncsu.csc216.business.model.contracts.Lease)}.
	 * and
	 * {@link edu.ncsu.csc216.business.model.properties.RentalUnit#listLeases()}.
	 */
	@Test
	public void testAddLease() {
		r.removeFromServiceStarting(LocalDate.parse("2020-01-01"));
		r.returnToService();
		r.addLease(L1);
		r.addLease(L2);
		r.addLease(L3);

		assertEquals(3, r.myLeases.size());
		assertEquals(3, r.listLeases().length);
		assertEquals("000000 | 2020-01-01 to 2020-02-01 |   2 | Maddy Mae (catsl)", r.listLeases()[0]);
		assertEquals("000001 | 2020-02-02 to 2020-03-01 |   2 | Maddy Mae (catsl)", r.listLeases()[1]);
		assertEquals("000002 | 2020-03-02 to 2020-04-01 |   2 | Maddy Mae (catsl)", r.listLeases()[2]);
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.RentalUnit#getDescription()}.
	 */
	@Test
	public void testGetDescription() {
		RentalUnit unit1 = new ConferenceRoom(LOCATION_1, CAPACITY_1);
		assertEquals(DESCRIPTION, unit1.getDescription());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.RentalUnit#hashCode()}.
	 */
	@Test
	public void testHashCode() {
		RentalUnit unit1 = new ConferenceRoom(LOCATION_1, CAPACITY_1);
		RentalUnit unit2 = new ConferenceRoom(LOCATION_1, CAPACITY_1);
		RentalUnit unit3 = new ConferenceRoom(LOCATION_2, CAPACITY_1);

		assertEquals(unit1.hashCode(), unit2.hashCode());

		assertNotEquals(unit1.hashCode(), unit3.hashCode());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.RentalUnit#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject() {
		RentalUnit unit1 = new ConferenceRoom(LOCATION_1, CAPACITY_1);
		RentalUnit unit2 = new ConferenceRoom(LOCATION_2, CAPACITY_1);
		assertTrue(unit1.equals(unit1));
		assertFalse(unit1.equals(unit2));
	}

}
