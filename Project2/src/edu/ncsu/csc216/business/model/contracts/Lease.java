package edu.ncsu.csc216.business.model.contracts;

import java.time.LocalDate;

import edu.ncsu.csc216.business.model.properties.RentalUnit;
import edu.ncsu.csc216.business.model.stakeholders.Client;

/**
 * Lease class which manages the creation of a Lease object, and creates a valid
 * lease using the client and rental unit package information
 * 
 * @author Sanjana Cheerla
 * @author Kaitlyn Gosline
 */
public class Lease implements Comparable<Lease> {

	/** keeps track of the number of confirmation numbers */
	private static int confirmationCounter = 0;

	/** the maximum confirmation number there can be */
	private static final int MAX_CONF_NUM = 999999;

	/** confirmation number of the Lease */
	private int confirmationNumber;

	/** start date of the Lease */
	private LocalDate startDate;

	/** end date of the Lease */
	private LocalDate endDate;

	/** number of occupants on the lease */
	private int numOccupants;

	/** owner of the lease */
	private Client owner;

	/** Property being leased */
	private RentalUnit property;

	/**
	 * Creates a lease object that already has a confirmation number with the
	 * provided information
	 * 
	 * @param confNum      of the lease
	 * @param c            client on the lease
	 * @param r            rental unit on the lease
	 * @param startDate    of the lease
	 * @param endDate      of the lease
	 * @param numOccupants on the lease
	 */
	public Lease(int confNum, Client c, RentalUnit r, LocalDate startDate, LocalDate endDate, int numOccupants) {
		this.owner = c;
		this.property = r;
		this.startDate = startDate;
		this.endDate = endDate;
		this.numOccupants = numOccupants;
		if (confNum > confirmationCounter) {
            confirmationCounter = confNum + 1;
        }
		if (confNum == confirmationCounter) {
            confirmationCounter++;
        }
		if (confirmationCounter > MAX_CONF_NUM) {
            resetConfirmationNumbering(0);
        }
		this.confirmationNumber = confNum;
	}

	/**
	 * Creates a lease object that doesn't have a confirmation number with the
	 * provided information
	 * 
	 * @param c            client on the lease
	 * @param r            rental unit on the lease
	 * @param startDate    of the lease
	 * @param endDate      of the lease
	 * @param numOccupants on the lease
	 */
	public Lease(Client c, RentalUnit r, LocalDate startDate, LocalDate endDate, int numOccupants) {
		this(confirmationCounter, c, r, startDate, endDate, numOccupants);
	}

	/**
	 * Returns the confirmation number on the lease
	 * 
	 * @return the confirmation number on the lease
	 */
	public int getConfirmationNumber() {
		return this.confirmationNumber;
	}

	/**
	 * Returns the client on the lease
	 * 
	 * @return the client on the lease
	 */
	public Client getClient() {
		return this.owner;
	}

	/**
	 * Returns the rental unit on the lease
	 * 
	 * @return the rental unit on the lease
	 */
	public RentalUnit getProperty() {
		return this.property;
	}

	/**
	 * Returns the start date of the lease
	 * 
	 * @return the start date of the lease
	 */
	public LocalDate getStart() {
		return this.startDate;
	}

	/**
	 * Returns the end date of the lease
	 * 
	 * @return the end date of the lease
	 */
	public LocalDate getEnd() {
		return this.endDate;
	}

	/**
	 * Returns the number of occupants of the lease
	 * 
	 * @return the number of occupants of the lease
	 */
	public int getNumOccupants() {
		return this.numOccupants;
	}

	/**
	 * Sets the current lease to an earlier end date
	 * 
	 * @param earlierDate the earlier date to set the end date of the lease to
	 * @throws IllegalArgumentException if the end date is before the start date
	 */
	public void setEndDateEarlier(LocalDate earlierDate) {
		if (earlierDate == null || earlierDate.isBefore(this.startDate)) {
			throw new IllegalArgumentException();
		}
		this.endDate = earlierDate;
	}

	/**
	 * Returns the data for a lease in an array of 6 strings
	 * 
	 * @return the data for a lease in the following array format: [0] confirmation
	 *         number, [1] start date to end date, [2] number of occupants, [3]
	 *         rental unit (Kind: floor-room), [4] client name, [5] client id.
	 */
	public String[] leaseData() {
		String[] lArray = new String[6];

		String confNum = "";
		int numDigits = Integer.toString(confirmationNumber).length();
		int addZeros = 6 - numDigits;
		for (int i = 0; i < addZeros; i++) {
			confNum += '0';
		}
		lArray[0] = confNum + this.confirmationNumber;

		lArray[1] = this.startDate.toString() + " to " + this.endDate.toString();

		lArray[2] = numOccupants + "";

		lArray[3] = this.getProperty().getDescription().substring(0, 23);

		lArray[4] = this.owner.getName();

		lArray[5] = this.owner.getId();

		return lArray;
	}

	/**
	 * Resets the confirmation counter value to the provided parameter
	 * 
	 * @param resetNum the value the confirmationCounter variable is being reset to
	 * @throws IllegalArgumentException if the parameter is not a permissible
	 *                                  confirmation number.
	 */
	public static void resetConfirmationNumbering(int resetNum) {
		if (resetNum < 0 || resetNum > MAX_CONF_NUM) {
			throw new IllegalArgumentException();
		}
		confirmationCounter = resetNum;
	}

	/**
	 * Compares 2 leases by ordering them from start date, then end dates to see if
	 * the dates math, then sees the confirmation number if the dates match.
	 * 
	 * @param lease the lease being compared to the current lease
	 * @return if the 2 leases are equal or not.
	 */
	@Override
	public int compareTo(Lease lease) {
		if (this.startDate.isEqual(lease.startDate)) {
			if (this.confirmationNumber < lease.confirmationNumber) {
				return -1;
			}
			if (this.confirmationNumber > lease.confirmationNumber) {
				return 1;
			}
			if (this.confirmationNumber == lease.confirmationNumber) {
				return 0;
			}
		}
		if (this.startDate.isBefore(lease.startDate)) {
			return -1;
		}
		if (this.startDate.isAfter(lease.startDate)) {
			return 1;
		}
		if (this.confirmationNumber < lease.confirmationNumber) {
			return -1;
		}
		if (this.confirmationNumber > lease.confirmationNumber) {
			return 1;
		}
		return 0;

	}
}
