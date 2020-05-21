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
 * Tests the PropertyManager class
 * 
 * @author Sanjana Cheerla
 * @author Kaitlyn Gosline
 *
 */
public class PropertyManagerTest {

	/** Client Name for testing */
	public static final String CLIENT_NAME = "Maddy Mae";
	/** Client ID for testing */
	public static final String CLIENT_ID = "catsl";
	/** client obj used for testing */
	public static Client c = new Client("Maddy Mae", "catsl");
	/** Office type for testing */
	public static final String OFFICE = "Office";
	/** Hotel Suite type for testing */
	public static final String HOTEL = "Hotel Suite";
	/** Conference type for testing */
	public static final String CONFERENCE = "Conference Room";
	/** Rental Unit Location for testing */
	public static final String LOCATION_1 = "2-22";
	/** Rental Unit Location for testing */
	public static final String LOCATION_2 = "2-10";
	/** Rental Unit Location for testing */
	public static final String LOCATION_3 = "3-22";
	/** Rental Unit Location for testing */
	public static final String LOCATION_4 = "1-10";
	/** Rental Unit object for comparison */
	public static RentalUnit r = new Office(LOCATION_1, 20);
	/** Invalid Rental Unit Location for testing */
	public static final String INVALID_LOCATION = "46-22";
	/** Test Capacity for Office */
	public static final int OFFICE_CAPACITY = 100;
	/** Test Capacity for Hotel */
	public static final int HOTEL_CAPACITY = 2;
	/** Test Capacity for Conference Room */
	public static final int CONFERENCE_CAPACITY = 6;
	/** Lease for tests comparison */
	public static Lease compareLease = new Lease(c, r, LocalDate.parse("2020-03-01"), LocalDate.parse("2020-04-30"),
			20);

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.stakeholders.PropertyManager#getInstance()}.
	 */
	@Test
	public void testGetInstance() {
		assertNotNull(PropertyManager.getInstance());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.stakeholders.PropertyManager#addNewClient(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testAddNewClient() {
		PropertyManager.getInstance().flushAllData();

		// Tests adding valid client
		try {
			PropertyManager.getInstance().addNewClient(CLIENT_NAME, CLIENT_ID);
		} catch (DuplicateClientException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}
		assertEquals(1, PropertyManager.getInstance().listClients().length);

		// Tests adding duplicate client
		try {
			PropertyManager.getInstance().addNewClient(CLIENT_NAME, CLIENT_ID);
			fail();
		} catch (DuplicateClientException e) {
			assertEquals(1, PropertyManager.getInstance().listClients().length);
		}

	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.stakeholders.PropertyManager#addNewUnit(java.lang.String, java.lang.String, int)}.
	 */
	@Test
	public void testAddNewUnit() {
		PropertyManager.getInstance().flushAllData();

		// Tests adding valid unit
		try {
			PropertyManager.getInstance().addNewUnit(OFFICE, LOCATION_1, OFFICE_CAPACITY);
		} catch (DuplicateRoomException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		assertEquals(1, PropertyManager.getInstance().listRentalUnits().length);

		// Tests adding unit with invalid location
		try {
			PropertyManager.getInstance().addNewUnit(OFFICE, INVALID_LOCATION, OFFICE_CAPACITY);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(1, PropertyManager.getInstance().listRentalUnits().length);
		} catch (DuplicateRoomException dre) {
			fail("should not reach this point");
			dre.printStackTrace();
		}

		// Tests adding duplicate unit
		try {
			PropertyManager.getInstance().addNewUnit(OFFICE, LOCATION_1, OFFICE_CAPACITY);
			fail();
		} catch (DuplicateRoomException e) {
			assertEquals(1, PropertyManager.getInstance().listRentalUnits().length);
		}

	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.stakeholders.PropertyManager#createLease(int, int, java.time.LocalDate, int, int)}.
	 */
	@Test
	public void testCreateLease() {
		PropertyManager.getInstance().flushAllData();

		// Adding valid unit
		try {
			PropertyManager.getInstance().addNewUnit(OFFICE, LOCATION_1, OFFICE_CAPACITY);
		} catch (DuplicateRoomException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		// Adding valid client
		try {
			PropertyManager.getInstance().addNewClient(CLIENT_NAME, CLIENT_ID);
		} catch (DuplicateClientException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		Lease valid;
		// Tests adding valid lease
		try {
			valid = PropertyManager.getInstance().createLease(0, 0, LocalDate.parse("2020-03-01"), 2, 20);
			assertEquals(compareLease.getClient(), valid.getClient());
			assertEquals(compareLease.getEnd(), valid.getEnd());
			assertEquals(compareLease.getNumOccupants(), valid.getNumOccupants());
			assertEquals(compareLease.getProperty(), valid.getProperty());
			assertEquals(compareLease.getStart(), valid.getStart());
		} catch (IllegalArgumentException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		Lease invalid = null;
		// Tests adding invalid lease
		try {
			invalid = PropertyManager.getInstance().createLease(0, 0, LocalDate.parse("2020-03-11"), 2, 20);
			fail();
		} catch (IllegalArgumentException e) {
			assertNull(invalid);
		}
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.stakeholders.PropertyManager#cancelClientsLease(int, int)}.
	 */
	@Test
	public void testCancelClientsLease() {
		PropertyManager.getInstance().flushAllData();
		// Adding valid client
		try {
			PropertyManager.getInstance().addNewClient(CLIENT_NAME, CLIENT_ID);
		} catch (DuplicateClientException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		// Adding valid unit
		try {
			PropertyManager.getInstance().addNewUnit(OFFICE, LOCATION_1, OFFICE_CAPACITY);
		} catch (DuplicateRoomException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		// Adding Valid Lease for Client
		PropertyManager.getInstance().addLeaseFromFile(c, 0, r, LocalDate.parse("2020-03-01"),
				LocalDate.parse("2020-04-30"), 20);
		assertEquals(1, PropertyManager.getInstance().listClientLeases(0).length);

		PropertyManager.getInstance().cancelClientsLease(0, 0);
		assertEquals(0, PropertyManager.getInstance().listClientLeases(0).length);
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.stakeholders.PropertyManager#removeFromService(int, java.time.LocalDate)}.
	 */
	@Test
	public void testRemoveFromService() {
		PropertyManager.getInstance().flushAllData();

		// Adding valid client
		try {
			PropertyManager.getInstance().addNewClient(CLIENT_NAME, CLIENT_ID);
		} catch (DuplicateClientException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		// Adding valid unit
		try {
			PropertyManager.getInstance().addNewUnit(OFFICE, LOCATION_1, OFFICE_CAPACITY);
		} catch (DuplicateRoomException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		// Adding Valid Lease for Client
		PropertyManager.getInstance().addLeaseFromFile(c, 0, r, LocalDate.parse("2020-03-01"),
				LocalDate.parse("2020-04-30"), 20);

		assertEquals(r, PropertyManager.getInstance().removeFromService(0, LocalDate.parse("2020-04-01")));
		assertEquals(1, PropertyManager.getInstance().listClientLeases(0).length);
		PropertyManager.getInstance().filterRentalUnits("A", true);

	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.stakeholders.PropertyManager#closeRentalUnit(int)}.
	 */
	@Test
	public void testCloseRentalUnit() {
		PropertyManager.getInstance().flushAllData();

		// Adding valid client
		try {
			PropertyManager.getInstance().addNewClient(CLIENT_NAME, CLIENT_ID);
		} catch (DuplicateClientException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		// Adding valid unit
		try {
			PropertyManager.getInstance().addNewUnit(OFFICE, LOCATION_1, OFFICE_CAPACITY);
		} catch (DuplicateRoomException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		PropertyManager.getInstance().closeRentalUnit(0);
		assertEquals(0, PropertyManager.getInstance().listRentalUnits().length);

	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.stakeholders.PropertyManager#returnToService(int)}.
	 */
	@Test
	public void testReturnToService() {
		PropertyManager.getInstance().flushAllData();

		// Adding valid client
		try {
			PropertyManager.getInstance().addNewClient(CLIENT_NAME, CLIENT_ID);
		} catch (DuplicateClientException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		// Adding valid unit
		try {
			PropertyManager.getInstance().addNewUnit(OFFICE, LOCATION_1, OFFICE_CAPACITY);
		} catch (DuplicateRoomException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		PropertyManager.getInstance().removeFromService(0, LocalDate.parse("2020-04-01"));
		PropertyManager.getInstance().returnToService(0);
		PropertyManager.getInstance().filterRentalUnits("A", true);
		assertEquals(1, PropertyManager.getInstance().listRentalUnits().length);
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.stakeholders.PropertyManager#listRentalUnits()}.
	 */
	@Test
	public void testListRentalUnits() {
		PropertyManager.getInstance().flushAllData();

		// Adding valid unit
		try {
			PropertyManager.getInstance().addNewUnit(OFFICE, LOCATION_1, OFFICE_CAPACITY);
		} catch (DuplicateRoomException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		// Adding valid unit
		try {
			PropertyManager.getInstance().addNewUnit(HOTEL, LOCATION_2, HOTEL_CAPACITY);
		} catch (DuplicateRoomException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}
		// Adding valid unit
		try {
			PropertyManager.getInstance().addNewUnit(CONFERENCE, LOCATION_3, CONFERENCE_CAPACITY);
		} catch (DuplicateRoomException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		// Adding valid unit
		try {
			PropertyManager.getInstance().addNewUnit(CONFERENCE, LOCATION_4, CONFERENCE_CAPACITY);
		} catch (DuplicateRoomException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		PropertyManager.getInstance().filterRentalUnits(OFFICE, false);
		assertEquals(1, PropertyManager.getInstance().listRentalUnits().length);

		PropertyManager.getInstance().filterRentalUnits(HOTEL, false);
		assertEquals(1, PropertyManager.getInstance().listRentalUnits().length);

		PropertyManager.getInstance().filterRentalUnits(CONFERENCE, false);
		assertEquals(2, PropertyManager.getInstance().listRentalUnits().length);

		PropertyManager.getInstance().filterRentalUnits("A", true);
		assertEquals(4, PropertyManager.getInstance().listRentalUnits().length);

		PropertyManager.getInstance().removeFromService(0, LocalDate.parse("2020-04-01"));
		PropertyManager.getInstance().removeFromService(2, LocalDate.parse("2020-04-01"));

		assertEquals(2, PropertyManager.getInstance().listRentalUnits().length);

		PropertyManager.getInstance().filterRentalUnits(CONFERENCE, true);
		assertEquals(0, PropertyManager.getInstance().listRentalUnits().length);

	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.stakeholders.PropertyManager#listLeasesForRentalUnit(int)}.
	 */
	@Test
	public void testListLeasesForRentalUnit() {
		PropertyManager.getInstance().flushAllData();

		// Adding valid client
		try {
			PropertyManager.getInstance().addNewClient(CLIENT_NAME, CLIENT_ID);
		} catch (DuplicateClientException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		// Adding valid unit
		try {
			PropertyManager.getInstance().addNewUnit(OFFICE, LOCATION_1, OFFICE_CAPACITY);
		} catch (DuplicateRoomException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		// Adding Valid Lease for Client
		PropertyManager.getInstance().addLeaseFromFile(c, 0, r, LocalDate.parse("2020-03-01"),
				LocalDate.parse("2020-04-30"), 20);

		assertEquals(1, PropertyManager.getInstance().listLeasesForRentalUnit(0).length);
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.stakeholders.PropertyManager#addLeaseFromFile(edu.ncsu.csc216.business.model.stakeholders.Client, int, java.time.LocalDate, java.time.LocalDate, int)}.
	 */
	@Test
	public void testAddLeaseFromFile() {
		PropertyManager.getInstance().flushAllData();

		// Adding valid client
		try {
			PropertyManager.getInstance().addNewClient(CLIENT_NAME, CLIENT_ID);
		} catch (DuplicateClientException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		// Adding valid unit
		try {
			PropertyManager.getInstance().addNewUnit(OFFICE, LOCATION_1, OFFICE_CAPACITY);
		} catch (DuplicateRoomException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		PropertyManager.getInstance().addLeaseFromFile(c, 4, r, LocalDate.parse("2020-03-01"),
				LocalDate.parse("2020-04-30"), 20);

		assertEquals(1, PropertyManager.getInstance().listLeasesForRentalUnit(0).length);
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.business.model.stakeholders.PropertyManager#getUnitAtLocation(java.lang.String)}.
	 */
	@Test
	public void testGetUnitAtLocation() {
		PropertyManager.getInstance().flushAllData();

		// Adding valid unit
		try {
			PropertyManager.getInstance().addNewUnit(OFFICE, LOCATION_1, OFFICE_CAPACITY);
		} catch (DuplicateRoomException e) {
			fail("should not reach this point");
			e.printStackTrace();
		}

		assertEquals(r, PropertyManager.getInstance().getUnitAtLocation(LOCATION_1));

		try {
			PropertyManager.getInstance().listLeasesForRentalUnit(-1);
			fail();
		} catch (Exception e) {
			assertEquals(r, PropertyManager.getInstance().getUnitAtLocation(LOCATION_1));
		}

		try {
			PropertyManager.getInstance().createLease(-1, -1, LocalDate.parse("2020-03-01"), 3, 2);
			fail();
		} catch (Exception e) {
			assertEquals(r, PropertyManager.getInstance().getUnitAtLocation(LOCATION_1));
		}
	}

}
