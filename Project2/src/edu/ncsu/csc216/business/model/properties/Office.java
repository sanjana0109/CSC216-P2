/**
 * 
 */
package edu.ncsu.csc216.business.model.properties;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;

import edu.ncsu.csc216.business.list_utils.SortedList;
import edu.ncsu.csc216.business.model.contracts.Lease;
import edu.ncsu.csc216.business.model.stakeholders.Client;

/**
 * Office space to be rented, maintains a calendar. A child class of RentalUnit
 * and is used by Lease. Overrides the following methods from RentalUnit:
 * reserve, recordExistingLease, removeFromServiceStarting, and getDescription.
 * Has extra methods remainingCapacityFor, getMonthsDuration.
 * 
 * @author Kaitlyn Gosline
 * @author Sanjana Cheerla
 */
public class Office extends RentalUnit {

	/** Constant for the number of months in a year */
	private static final int MONTHS_IN_YEAR = 12;
	/** Constant for the maximum capacity of an office */
	public static final int MAX_CAPACITY = 150;

	/**
	 * Creates an Office given the location and capacity
	 * 
	 * @param location the location of the office written ff(dash)rr
	 * @param capacity the capacity of the office
	 */
	public Office(String location, int capacity) {
		super(location, capacity);
		if (capacity > MAX_CAPACITY) {
			throw new IllegalArgumentException("Invalid capacity.");
		}
	}

	/**
	 * Returns the remaining capacity for a office given the date
	 * 
	 * @param date the date for which remaining capacity is being returned
	 * @return the remaining capacity for the given date
	 * @throws IllegalArgumentException if the date is before jan 1 2020 or after
	 *                                  dec 31 20209
	 */
	protected int remainingCapacityFor(LocalDate date) throws IllegalArgumentException {
		LocalDate minDate = LocalDate.of(2020, Month.JANUARY, 1);
		LocalDate maxDate = LocalDate.of(2029, Month.DECEMBER, 31);

		if (date.isAfter(maxDate) || date.isBefore(minDate)) {
			throw new IllegalArgumentException();
		}

		int cap = this.getCapacity();
		int numOccupants = 0;
		for (int i = 0; i < this.myLeases.size(); i++) {
			if (date.isAfter(this.myLeases.get(i).getStart()) && date.isBefore(this.myLeases.get(i).getEnd())) {
				numOccupants += this.myLeases.get(i).getNumOccupants();
			}
			if (date.isEqual(this.myLeases.get(i).getStart()) || date.isEqual(this.myLeases.get(i).getEnd())) {
				numOccupants += this.myLeases.get(i).getNumOccupants();
			}
		}
		return cap - numOccupants;
	}

	/**
	 * Returns the duration of office rental in months given two dates
	 * 
	 * @param startDate the start date of the rental
	 * @param endDate   the end date of the rental
	 * @return the length in months that the office would be rented for given the
	 *         start date and end date
	 */
	protected static int getMonthsDuration(LocalDate startDate, LocalDate endDate) {
		LocalDate newStart = startDate.withDayOfMonth(1);
		LocalDate newEnd = endDate.withDayOfMonth(endDate.lengthOfMonth());
		Period p = Period.between(newStart, newEnd.plusDays(1));
		return p.getMonths() + p.getYears() * MONTHS_IN_YEAR;
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
	 * @throws RentalDateException         if the start and end date are not valid
	 *                                     and if the start date is not the first
	 *                                     day of the month
	 * @throws RentalCapacityException     if the rental unit cannot hold the number
	 *                                     of occupants over the dates of the
	 *                                     proposed lease and if if the capacity is
	 *                                     breached for any of the duration of the
	 *                                     lease
	 */
	@Override
	public Lease reserve(Client c, LocalDate startDate, int duration, int numOccupants)
			throws RentalDateException, RentalCapacityException, RentalOutOfServiceException {
		super.checkLeaseConditions(c, startDate, duration, numOccupants);
		if (startDate.getDayOfMonth() != 1) {
			throw new RentalDateException("");
		}
		LocalDate endDate = startDate.plusMonths(duration).minusDays(1);
		this.checkDates(startDate, endDate);
		LocalDate currDate = startDate;
		while (currDate.isBefore(endDate) || currDate.equals(endDate)) {
			if (this.remainingCapacityFor(currDate) - numOccupants < 0) {
				throw new RentalCapacityException("Insufficient capacity for proposed lease.");
			}
			currDate = currDate.plusDays(1);
		}
		Lease l = new Lease(c, this, startDate, endDate, numOccupants);
		this.addLease(l);
		return l;
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
	 *                                     and if the start date is not the first
	 *                                     day of the month and the end date is not
	 *                                     the last day of a month
	 * @throws RentalCapacityException     if the rental unit cannot hold the number
	 *                                     of occupants over the dates of the
	 *                                     proposed lease and if the capacity is
	 *                                     breached for any of the duration of the
	 *                                     lease
	 * @throws RentalOutOfServiceException if the RentalUnit is out of service
	 */
	@Override
	public Lease recordExistingLease(int confNum, Client c, LocalDate startDate, LocalDate endDate, int numOccupants)
			throws RentalDateException, RentalCapacityException, RentalOutOfServiceException {
		if (!this.isInService()) {
			throw new RentalOutOfServiceException("");
		}
		if (c == null || startDate == null || endDate == null || numOccupants < 1) {
			throw new IllegalArgumentException();
		}
		this.checkDates(startDate, endDate);
		LocalDate currDate = startDate;
		while (currDate.isBefore(endDate) || currDate.equals(endDate)) {
			if (this.remainingCapacityFor(currDate) - numOccupants < 0) {
				throw new RentalCapacityException("");
			}
			currDate = currDate.plusDays(1);
		}
		Lease lease = new Lease(confNum, c, this, startDate, endDate, numOccupants);
		this.addLease(lease);
		return lease;
	}

	/**
	 * Removes the rental unit from the serviceStartDte, and all the leases that
	 * being on or after the service start date.
	 * 
	 * @param serviceStartDate start date where unit should be removed from service
	 * @return a sorted list of all the lists that were removed and that should
	 *          subsequently be cancelled
	 */
	@Override
	public SortedList<Lease> removeFromServiceStarting(LocalDate serviceStartDate) {
		LocalDate beginMonth = serviceStartDate;
		SortedList<Lease> removedLeases;
		if (serviceStartDate.getDayOfMonth() == 1) {
			removedLeases = super.removeFromServiceStarting(serviceStartDate);
		} else {
			while (beginMonth.getDayOfMonth() != 1) {
				beginMonth = beginMonth.minusDays(1);
			}
			removedLeases = super.removeFromServiceStarting(beginMonth);
		}
		for (int i = 0; i < this.myLeases.size(); i++) {
			if (this.myLeases.get(i).getEnd().isAfter(beginMonth)) {
				try {
					this.myLeases.get(i).setEndDateEarlier(beginMonth.minusDays(1));
				} catch (IllegalArgumentException e) {
					super.removeFromServiceStarting(this.myLeases.get(i).getStart());
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
		String s = "Office:          ";
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
		if (startDate.getDayOfMonth() != 1 || endDate.getDayOfMonth() != endDate.lengthOfMonth()) {
			throw new RentalDateException("");
		}
	}

}
