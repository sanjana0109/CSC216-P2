/**
 * 
 */
package edu.ncsu.csc216.business.model.properties;

import java.time.DayOfWeek;
import java.time.LocalDate;

import edu.ncsu.csc216.business.list_utils.SortedList;
import edu.ncsu.csc216.business.model.contracts.Lease;
import edu.ncsu.csc216.business.model.stakeholders.Client;

/**
 * Hotel Suite to be rented. A child class of RentalUnit and is used by Lease.
 * Overrides the following methods from RentalUnit: reserve,
 * recordExistingLease, removeFromServiceStarting, getDescription, and check
 * dates.
 * 
 * @author Kaitlyn Gosline
 * @author Sanjana Cheerla
 *
 */
public class HotelSuite extends RentalUnit {

	/** Constant for the maximum capacity of a Hotel Suite */
	public static final int MAX_CAPACITY = 2;

	/**
	 * Creates a Hotel Suite given the location
	 * 
	 * @param location the location of the hotel suite written ff(dash)rr
	 */
	public HotelSuite(String location) {
		this(location, 1);
	}

	/**
	 * Creates a Hotel Suite given the location and capacity
	 * 
	 * @param location the location of the hotel suite written ff(dash)rr
	 * @param capacity the capacity of the hotel suite
	 * @throws IllegalArgumentException if the capacity is too large
	 */
	public HotelSuite(String location, int capacity) {
		super(location, capacity);
		if (capacity > MAX_CAPACITY) {
			throw new IllegalArgumentException("Invalid capacity.");
		}
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
	 * @throws RentalOutOfServiceException if the rental unit is currently out of
	 *                                     service and not available for lease
	 * @throws RentalDateException         if the start and end date are not valid,
	 *                                     and if the start and end dates are not
	 *                                     Sundays or if the non-Sunday dates in the
	 *                                     lease overlap with another lease
	 * @throws RentalCapacityException     if the rental unit cannot hold the number
	 *                                     of occupants over the dates of the
	 *                                     proposed lease
	 */
	@Override
	public Lease reserve(Client c, LocalDate startDate, int duration, int numOccupants)
			throws RentalDateException, RentalCapacityException, RentalOutOfServiceException {
		super.checkLeaseConditions(c, startDate, duration, numOccupants);
		LocalDate endDate = startDate.plusWeeks(duration);
		this.checkDates(startDate, endDate);
		if (numOccupants < 1 || numOccupants > this.getCapacity()) {
			throw new RentalCapacityException("Number of Occupants is over Capacity");
		}
		Lease lease = new Lease(c, this, startDate, endDate, numOccupants);
		if (checkSameDayLease(lease)) {
			throw new RentalDateException("");
		}
		this.addLease(lease);
		return lease;
	}

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
	 *                                     and if the start and end dates are not
	 *                                     Sundays or if the non-Sunday dates in the
	 *                                     lease overlap with another lease
	 * @throws RentalCapacityException     if the rental unit cannot hold the number
	 *                                     of occupants over the dates of the
	 *                                     proposed lease
	 * @throws RentalOutOfServiceException if the RentalUnit is out of service
	 */
	@Override
	public Lease recordExistingLease(int confNum, Client c, LocalDate startDate, LocalDate endDate, int numOccupants)
			throws RentalDateException, RentalCapacityException, RentalOutOfServiceException {
		this.checkDates(startDate, endDate);
		if (numOccupants > this.getCapacity()) {
			throw new RentalCapacityException("Number of Occupants is over Capacity");
		}
		if (!this.isInService()) {
			throw new RentalOutOfServiceException("");
		}
		Lease lease = new Lease(confNum, c, this, startDate, endDate, numOccupants);
		if (checkSameDayLease(lease)) {
			throw new RentalDateException("");
		}
		this.addLease(lease);
		return lease;
	}

	/**
	 * Removes the rental unit from the serviceStartDte, and all the leases that
	 * begin on or after the service start date.
	 * 
	 * @param serviceStartDate start date where unit should be removed from service
	 * @return a sorted list of all the lists that were removed and that should
	 *          subsequently be cancelled
	 */
	@Override
	public SortedList<Lease> removeFromServiceStarting(LocalDate serviceStartDate) {
		SortedList<Lease> removedLeases = super.removeFromServiceStarting(serviceStartDate);
		for (int i = 0; i < this.myLeases.size(); i++) {
			if (this.myLeases.get(i).getEnd().isAfter(serviceStartDate)) {
				if (serviceStartDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
					this.myLeases.get(i).setEndDateEarlier(serviceStartDate);
				} else {
					LocalDate endSunday = serviceStartDate;
					while (!endSunday.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
						endSunday = endSunday.minusDays(1);
					}
					this.myLeases.get(i).setEndDateEarlier(endSunday);
				}

			}
		}
		return removedLeases;
	}

	/**
	 * Returns a string description of the rental unit, and unavailable if the
	 * rental unit is not available.
	 */
	@Override
	public String getDescription() {
		String s = "Hotel Suite:     ";
		s += super.getDescription();
		return s;
	}

	/**
	 * Checks to see if the 2 provided dates are in between January 1 2020, and
	 * December 31 2029; and if the start date is AFTER the end date
	 * 
	 * @param startDate being checked
	 * @param endDate   being checked
	 * @throws RentalDateException if the start and end date are not valid
	 */
	@Override
	public void checkDates(LocalDate startDate, LocalDate endDate) throws RentalDateException {
		super.checkDates(startDate, endDate);
		if (startDate.getDayOfWeek() != DayOfWeek.SUNDAY || endDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
			throw new RentalDateException("");
		} else if (startDate.equals(endDate)) {
			throw new RentalDateException("");
		}
	}

	/**
	 * Checks to see if there is conflicting lease of the same unit with
	 * 
	 * @param l lease trying to be created to check against existing leases
	 */
	private boolean checkSameDayLease(Lease l) {
		LocalDate start2 = l.getStart();
		LocalDate end2 = l.getEnd();
		for (int i = 0; i < this.myLeases.size(); i++) {
			LocalDate start1 = this.myLeases.get(i).getStart();
			LocalDate end1 = this.myLeases.get(i).getEnd();
			if (this.equals(l.getProperty())) {

				if ((start2.isAfter(start1) || start2.equals(start1)) && (end2.isBefore(end1) || end2.equals(end1))) {
					return true;
				}

				if ((end2.isAfter(start1)) && (start2.isBefore(start1) || start2.equals(start1))) {
					return true;
				}

				if ((start2.isBefore(end1)) && (end2.isAfter(end1) || end2.equals(end1))) {
					return true;
				}
			}
		}
		return false;
	}

}
