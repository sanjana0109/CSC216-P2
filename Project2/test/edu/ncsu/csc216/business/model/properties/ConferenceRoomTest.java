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
 * Test class for ConferenceRoom class
 * 
 * @author Sanjana Cheerla
 * @author Kaitlyn Gosline
 *
 */
public class ConferenceRoomTest {

	/** Test Capacity for Conference Room */
	public static final int CAPACITY_1 = 10;
	/** Test Location for Conference Room*/
	public static final String LOCATION_1 = "1-22";
	/** Floor as an integer */
	public static final int FLOOR = 1;
	/** Room as an integer */
	public static final int ROOM = 22;
	/** Conference Room Description */
	public static final String DESCRIPTION = "Conference Room:  1-22 |  10";

	/** client obj used for testing */
	public static Client c = new Client("Maddy Mae", "catsl");

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.ConferenceRoom#ConferenceRoom(java.lang.String, int)}.
	 */
	@Test
	public void testConferenceRoom() {
		ConferenceRoom room = new ConferenceRoom(LOCATION_1, CAPACITY_1);
		assertEquals(CAPACITY_1, room.getCapacity());
		assertEquals(FLOOR, room.getFloor());
		assertEquals(ROOM, room.getRoom());
		ConferenceRoom r = null;
		try {
			r = new ConferenceRoom(LOCATION_1, 26);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(r);
		}
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.ConferenceRoom#reserve(edu.ncsu.csc216.business.model.stakeholders.Client, java.time.LocalDate, int, int)}.
	 */
	@Test
	public void testReserve() {
		ConferenceRoom r = new ConferenceRoom(LOCATION_1, CAPACITY_1);
		Lease l1 = new Lease(0, c, r, LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-05"), 2);
		String[] l1Arr = l1.leaseData();
		try {
			Lease l = r.reserve(c, LocalDate.parse("2020-01-01"), 5, 2);
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
			l = r.reserve(c, LocalDate.parse("2020-02-01"), 26, 2);
			fail();
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException
				| IllegalArgumentException e) {
			assertNull(l);
		}

		try {
			l = r.reserve(c, LocalDate.parse("2020-02-01"), 5, -1);
			fail();
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException
				| IllegalArgumentException e) {
			assertNull(l);
		}

		try {
			l = r.reserve(c, LocalDate.parse("2020-02-01"), -1, 2);
			fail();
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException
				| IllegalArgumentException e) {
			assertNull(l);
		}

		try {
			l = r.reserve(c, LocalDate.parse("2020-02-01"), 2, 26);
			fail();
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException
				| IllegalArgumentException e) {
			assertNull(l);
		}
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.ConferenceRoom#recordExistingLease(int, edu.ncsu.csc216.business.model.stakeholders.Client, java.time.LocalDate, java.time.LocalDate, int)}.
	 */
	@Test
	public void testRecordExistingLease() {
		ConferenceRoom r = new ConferenceRoom(LOCATION_1, CAPACITY_1);
		Lease l1 = new Lease(0, c, r, LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-06"), 2);
		try {
			Lease l = r.recordExistingLease(0, c, LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-06"), 2);
			assertEquals(0, l1.compareTo(l));
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException e) {
			fail("should not reach this point");
		}

		r.takeOutOfService();
		Lease l = null;
		try {
			l = r.recordExistingLease(0, c, LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-06"), 2);
			assertEquals(0, l1.compareTo(l));
			fail();
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException e) {
			assertNull(l);
		}
		

		try {
			l = r.recordExistingLease(0, c, LocalDate.parse("2020-02-01"), LocalDate.parse("2020-02-06"), 26);
			fail();
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException
				| IllegalArgumentException e) {
			assertNull(l);
		}

		try {
			l = r.recordExistingLease(0, c, LocalDate.parse("2020-02-01"), LocalDate.parse("2020-02-06"), 26);
			fail();
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException
				| IllegalArgumentException e) {
			assertNull(l);
		}

	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.ConferenceRoom#removeFromServiceStarting(java.time.LocalDate)}.
	 */
	@Test
	public void testRemoveFromServiceStarting() {
		ConferenceRoom r = new ConferenceRoom(LOCATION_1, CAPACITY_1);
		/** Lease obj 1 testing */
		Lease l1 = new Lease(0, c, r, LocalDate.parse("2020-01-01"), LocalDate.parse("2020-02-02"), 2);
		/** Lease obj 2 testing */
		Lease l2 = new Lease(1, c, r, LocalDate.parse("2020-02-02"), LocalDate.parse("2020-03-01"), 2);
		/** Lease obj 3 testing */
		Lease l3 = new Lease(2, c, r, LocalDate.parse("2020-03-02"), LocalDate.parse("2020-04-01"), 2);
		r.returnToService();

		r.addLease(l1);
		r.addLease(l2);
		r.addLease(l3);
		assertEquals(3, r.myLeases.size());
		assertEquals(3, r.listLeases().length);

		r.removeFromServiceStarting(LocalDate.parse("2020-02-01"));
		assertEquals(1, r.myLeases.size());
		assertEquals(1, r.listLeases().length);

		r = null;
		l1 = null;
		l2 = null;
		l3 = null;

		r = new ConferenceRoom(LOCATION_1, CAPACITY_1);
		l1 = new Lease(0, c, r, LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-06"), 2);
		r.addLease(l1);
		l2 = new Lease(0, c, r, LocalDate.parse("2020-01-07"), LocalDate.parse("2020-01-13"), 2);
		r.addLease(l2);
		l3 = new Lease(0, c, r, LocalDate.parse("2020-01-13"), LocalDate.parse("2020-01-19"), 2);
		r.addLease(l3);

		r.removeFromServiceStarting(LocalDate.parse("2020-06-01"));

	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.properties.ConferenceRoom#getDescription()}.
	 */
	@Test
	public void testGetDescription() {
		ConferenceRoom room = new ConferenceRoom(LOCATION_1, CAPACITY_1);
		assertEquals(DESCRIPTION, room.getDescription());
	}

	/**
	 * Test for check same day and reserve
	 */
	@Test
	public void testSameDayReserves() {
		ConferenceRoom r = new ConferenceRoom("1-22", 10);
		/** Lease obj 1 testing */
		Lease l1 = new Lease(0, c, r, LocalDate.parse("2020-01-01"), LocalDate.parse("2020-02-01"), 2);
		/** Lease obj 2 testing */
		Lease l2 = new Lease(1, c, r, LocalDate.parse("2020-02-02"), LocalDate.parse("2020-03-01"), 2);
		/** Lease obj 3 testing */
		Lease l3 = new Lease(2, c, r, LocalDate.parse("2020-03-02"), LocalDate.parse("2020-04-01"), 2);
		r.addLease(l1);
		r.addLease(l2);
		r.addLease(l3);
		
		assertEquals(3, r.myLeases.size());
		
		try {
			r.reserve(c, LocalDate.parse("2020-02-01"), 3, 3);
			fail("This message should not occur");
		} catch (RentalDateException | RentalOutOfServiceException | RentalCapacityException
				| IllegalArgumentException e) {
			assertEquals(3, r.myLeases.size());
		}
	}
}
