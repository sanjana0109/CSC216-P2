/**
 * 
 */
package edu.ncsu.csc216.business.model.properties;

import java.time.LocalDate;
import java.time.Month;

import edu.ncsu.csc216.business.list_utils.SortedLinkedListWithIterator;
import edu.ncsu.csc216.business.list_utils.SortedList;
import edu.ncsu.csc216.business.model.contracts.Lease;
import edu.ncsu.csc216.business.model.stakeholders.Client;

/**
 * Abstract class of rental unit, is extended by all of the other possible
 * rental units. There are 4 constants for min and max floor numbers, and min
 * and max room numbers. This class contains common methods in all of the other
 * rental units, and methods that the rental units can use. There are methods
 * that get the private fields, manage the rental unit(such as setting in
 * service, returning a rental unit to service, checking the lease dates,
 * reserving a room, recording reserved rooms, as well as hash code and equals
 * methods.
 * 
 * @author Sanjana Cheerla
 * @author Kaitlyn Gosline
 *
 */
public abstract class RentalUnit implements Comparable<RentalUnit> {

	/** The maximum number of floors in the building */
	public static final int MAX_FLOOR = 45;

	/** The minimum numbers of floors in the building */
	public static final int MIN_FLOOR = 1;

	/** The maximum number of room numbers in the floor */
	public static final int MAX_ROOM = 99;

	/** The minimum number of numbers in the floor */
	public static final int MIN_ROOM = 10;

	/** Private member of whether the room is in service or not in service */
	private boolean inService;

	/** Private member of the the floor number */
	private int floor;

	/** Private member of the room number */
	private int room;

	/** Private number of capacity in the rental unit */
	private int capacity;

	/** Protected sorted list of leases for each rental unit */
	protected SortedLinkedListWithIterator<Lease> myLeases = new SortedLinkedListWithIterator<Lease>();

	/**
	 * Constructor of Rental unit, creates a Rental unit with the provided
	 * parameters
	 * 
	 * @param location of the room in the format ff(dash)rr, floor number dash room
	 *                 number
	 * @param capacity of the rental unit
	 * @throws IllegalArgumentException if the location is invalid or the capacity
	 *                                  is 0 or negative
	 */
	public RentalUnit(String location, int capacity) {
		if (capacity <= 0) {
			throw new IllegalArgumentException("Invalid capacity.");
		}
		String locationTrimmed = location.trim();
		char[] loc = locationTrimmed.toCharArray();
		String floorNum = "";
		String roomNum = "";
		int dash = -1;
		for (int i = 0; i < loc.length; i++) {
			if (loc[i] != '-') {
				floorNum += loc[i];
			}
			if (loc[i] == '-') {
				dash = i;
				break;
			}
		}
		for (int i = dash + 1; i < loc.length; i++) {
			roomNum += loc[i];
		}
		int unitFloor = Integer.parseInt(floorNum);
		int unitRoom = Integer.parseInt(roomNum);
		if (unitFloor < MIN_FLOOR || unitFloor > MAX_FLOOR) {
			throw new IllegalArgumentException("Invalid floor.");
		}
		if (unitRoom < MIN_ROOM || unitRoom > MAX_ROOM) {
			throw new IllegalArgumentException("Invalid room.");
		}
		this.floor = unitFloor;
		this.room = unitRoom;
		this.capacity = capacity;
		this.inService = true;
	}

	/**
	 * Returns the capacity of the rental unit
	 * 
	 * @return capacity of the rental unit
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Returns the floor number of the rental unit
	 * 
	 * @return floor number of the rental unit
	 */
	public int getFloor() {
		return this.floor;
	}

	/**
	 * Returns the room number of the rental unit
	 * 
	 * @return room number of the rental unit
	 */
	public int getRoom() {
		return this.room;
	}

	/**
	 * Compares the location of the provided Rental unit with the current one.
	 * 
	 * @param r RentalUnit that is being compared
	 * @return the comparison value, -1 if the parameter unit is greater, 1 if the
	 *         parameter unit is less, and 0 if they are the same.
	 */
	public int compareTo(RentalUnit r) {
		if (this.getFloor() < r.getFloor()) {
			return -1;
		}
		if (this.getFloor() > r.getFloor()) {
			return 1;
		}
		if (this.getFloor() == r.getFloor()) {
			if (this.getRoom() < r.getRoom()) {
				return -1;
			}
			if (this.getRoom() > r.getRoom()) {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * Returns a rental unit back to service
	 */
	public void returnToService() {
		this.inService = true;
	}

	/**
	 * Checks to see if a room in service
	 * 
	 * @return if the room is in service or not
	 */
	public boolean isInService() {
		return this.inService;
	}

	/**
	 * Takes a rental unit off the list, into service
	 */
	public void takeOutOfService() {
		this.inService = false;
	}

	/**
	 * Reserves a room with the provided information
	 * 
	 * @param c            Client who is reserving the room
	 * @param startDate    of the lease
	 * @param duration     of the lease
	 * @param numOccupants leasing the room
	 * @return the constructed lease for the client
	 * @throws IllegalArgumentException    if any of the parameters are null or the
	 *                                     duration or numOccupants is less than 1
	 *                                     of service and not available for lease
	 * @throws RentalDateException         if the start and end date are not valid
	 * @throws RentalCapacityException     if the rental unit cannot hold the number
	 *                                     of occupants over the dates of the
	 *                                     proposed lease
	 * @throws RentalOutOfServiceException if the RentalUnit is out of service
	 */
	public abstract Lease reserve(Client c, LocalDate startDate, int duration, int numOccupants)
			throws RentalDateException, RentalCapacityException, RentalOutOfServiceException;

	/**
	 * Reserves the rental unit from an existing lease, read from a file.
	 * 
	 * @param confNum      of the existing lease
	 * @param c            client who is reserving the lease
	 * @param startDate    of the lease
	 * @param endDate      of the lease
	 * @param numOccupants leasing the room
	 * @return the constructed lease for the client
	 * @throws RentalDateException         if the start and end date are not valid
	 * @throws RentalCapacityException     if the rental unit cannot hold the number
	 *                                     of occupants over the dates of the
	 *                                     proposed lease
	 * @throws RentalOutOfServiceException if the RentalUnit is out of service
	 */
	public abstract Lease recordExistingLease(int confNum, Client c, LocalDate startDate, LocalDate endDate,
			int numOccupants) throws RentalDateException, RentalCapacityException, RentalOutOfServiceException;

	/**
	 * Checks to see if the 2 provided dates are in between January 1 2020, and
	 * December 31 2029; and if the start date is AFTER the end date
	 * 
	 * @param startDate being checked
	 * @param endDate   being checked
	 * @throws RentalDateException if the start and end date are not valid
	 */
	public void checkDates(LocalDate startDate, LocalDate endDate) throws RentalDateException {
		LocalDate minDate = LocalDate.of(2020, Month.JANUARY, 1);
		LocalDate maxDate = LocalDate.of(2029, Month.DECEMBER, 31);
		if (startDate.getYear() < minDate.getYear()) {
			throw new RentalDateException("Invalid Start Date");
		}
		if (startDate.compareTo(minDate) < 0 || endDate.isBefore(minDate)) {
			throw new RentalDateException("Invalid Start Date");
		}
		if (endDate.isAfter(maxDate) || startDate.isAfter(maxDate)) {
			throw new RentalDateException("Invalid End Date");
		}
		if (startDate.isAfter(endDate)) {
			throw new RentalDateException("Start Date is after End Date");
		}

	}

	/**
	 * Checks the lease conditions of the provided parameters
	 * 
	 * @param c            client leasing the unit
	 * @param startDate    of the lease
	 * @param duration     of the lease
	 * @param numOccupants on the lease
	 * @throws IllegalArgumentException    if any of the parameters are null or the
	 *                                     duration or numOccupants is less than 1
	 * @throws RentalOutOfServiceException if the rental unit is out of service and
	 *                                     not available for leasing
	 */
	protected void checkLeaseConditions(Client c, LocalDate startDate, int duration, int numOccupants)
			throws IllegalArgumentException, RentalOutOfServiceException {
		if (c == null || startDate == null || duration < 1 || numOccupants < 1) {
			throw new IllegalArgumentException();
		}
		if (!isInService()) {
			throw new RentalOutOfServiceException("Unit is not available for leasing");
		}
	}

	/**
	 * Removes the rental unit from the serviceStartDate, and all the leases that
	 * being on or after the service start date.
	 * 
	 * @param serviceStartDate the date the service starts
	 * @return a sorted list of all the lists that were removed and that should
	 *         subsequently be cancelled
	 */
	public SortedList<Lease> removeFromServiceStarting(LocalDate serviceStartDate) {
		SortedList<Lease> removedUnits = new SortedLinkedListWithIterator<Lease>();
		for (int i = 0; i < this.myLeases.size(); i++) {
			if (this.myLeases.get(i).getStart().isEqual(serviceStartDate)
					|| this.myLeases.get(i).getStart().isAfter(serviceStartDate)) {
				removedUnits.add(this.myLeases.get(i));
				this.myLeases.remove(i);
				i--;
			}
		}
		this.takeOutOfService();
		return removedUnits;
	}

	/**
	 * Returns the index of the first lease with on the start date of after the
	 * start date.
	 * 
	 * @param startDate of the lease being found
	 * @return index of first lease on or after startDate, negative 1 if no such
	 *         lease exists
	 * 
	 */
	protected int cutoffIndex(LocalDate startDate) {
		for (int i = 0; i < this.myLeases.size(); i++) {
			if (this.myLeases.get(i).getStart().isEqual(startDate)
					|| this.myLeases.get(i).getStart().isAfter(startDate)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Cancels the lease provided with the confirmation number
	 * 
	 * @param confNum of the lease being cancelled
	 * @return the cancelled lease
	 * @throws IllegalArgumentException if no such lease exists with the provided
	 *                                  confirmation number.
	 */
	public Lease cancelLeaseByNumber(int confNum) {
		for (int i = 0; i < this.myLeases.size(); i++) {
			if (this.myLeases.get(i).getConfirmationNumber() == confNum) {
				Lease removed = this.myLeases.get(i);
				this.myLeases.remove(i);
				return removed;
			}
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Adds the provided lease to the rental units list of leases
	 * 
	 * @param lease being added
	 * @throws IllegalArgumentException if the lease if for a different rental unit
	 */
	public void addLease(Lease lease) {
		if (!this.inService) {
			return;
		}
		if (lease == null || !(this.equals(lease.getProperty()))) {
			throw new IllegalArgumentException();
		}

		this.myLeases.add(lease);
	}

	/**
	 * Returns the list of leases for the current rental unit as an array of
	 * strings, where each string represents a lease
	 * 
	 * @return a list of leases for the rental unit
	 */
	public String[] listLeases() {
		String[] leases = new String[this.myLeases.size()];

		for (int i = 0; i < this.myLeases.size(); i++) {
			leases[i] = leaseToString(this.myLeases.get(i));
		}

		return leases;
	}

	/**
	 * Formats a lease object with the confirmation #, date, occupants, name of
	 * owner and id of owner in parentheses
	 * 
	 * @param l lease that gets formatted
	 * @return a formatted lease object
	 */
	private String leaseToString(Lease l) {
		String s = "";
		String[] lArray = l.leaseData();
		s += lArray[0] + " | ";
		s += lArray[1] + " | ";
		int numOccupants = Integer.parseInt(lArray[2]);
		s += String.format("%3d", numOccupants) + " | ";
		s += lArray[4] + " (";
		s += lArray[5] + ")";
		return s;
	}

	/**
	 * Returns a string description of the rental unit, and unavailable if the
	 * rental unit is not available.
	 * 
	 * @return a string description of the rental unit, and unavailable if the
	 *         rental unit is not available.
	 */
	public String getDescription() {
		String s = "";
		String loc = this.getFloor() + "-" + this.getRoom();
		s += String.format("%5s", loc);
		s += " | ";
		s += String.format("%3d", this.capacity);
		if (!this.isInService()) {
			s += " Unavailable";
		}
		return s;
	}

	/**
	 * Hash code for the rental unit
	 * 
	 * @return the hash code
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + floor;
		result = prime * result + room;
		return result;
	}

	/**
	 * compares the current rental unit with the provided parameter.
	 * 
	 * @param obj object being compared with the current rental unit
	 * @return true if they are the 2 objects being compared are the same and false
	 *         if they are not the same.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RentalUnit)) {
			return false;
		}
		RentalUnit other = (RentalUnit) obj;
		if (compareTo(other) == 0) {
			return true;
		}
		return false;
	}
}
