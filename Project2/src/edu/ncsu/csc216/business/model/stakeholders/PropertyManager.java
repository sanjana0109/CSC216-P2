package edu.ncsu.csc216.business.model.stakeholders;

import java.time.LocalDate;
import java.time.Month;

import edu.ncsu.csc216.business.list_utils.SimpleArrayList;
import edu.ncsu.csc216.business.list_utils.SortedLinkedListWithIterator;
import edu.ncsu.csc216.business.list_utils.SortedList;
import edu.ncsu.csc216.business.model.contracts.Lease;
import edu.ncsu.csc216.business.model.properties.ConferenceRoom;
import edu.ncsu.csc216.business.model.properties.HotelSuite;
import edu.ncsu.csc216.business.model.properties.Office;
import edu.ncsu.csc216.business.model.properties.RentalUnit;

/**
 * This class follows the Singleton pattern which is "implemented through a
 * private constructor and the method ProjectManager.getInstance(), which
 * returns the only instance of ProjectManager." and the factory pattern which
 * is "implemented through the method ProjectManager.addNewUnit() and the
 * RentalUnit class hierarchy. The method creates a concrete RentalUnit
 * according to the method’s parameters. Both RentalGUI and RentalReader call
 * this method for RentalUnit creation." This class implements landlord and all
 * of its methods, as well as methods not described in landlord including
 * getInstance. addLeaseFromFile, and getUnitAtLocation.
 * 
 * @author Kaitlyn Gosline
 * @author Sanjana Cheerla
 *
 */
public class PropertyManager implements Landlord {

	/** Earliest possible lease date */
	public static final LocalDate EARLIEST_DATE = null;

	/** Latest possible lease date */
	public static final LocalDate LATEST_DATE = null;

	/** filtering by kind of rental unit */
	private String kindFilter;

	/** filtering by in service rental unit */
	private boolean inServiceFilter;

	/** SimpleArrayList of customer bases */
	private SimpleArrayList<Client> customerBase;

	/** linked list of rental units available for rent */
	private SortedLinkedListWithIterator<RentalUnit> rooms;

	/** instance of the property manager */
	private static PropertyManager instance;

	/* earliest date of removal */
	private static final LocalDate MIN_DATE = LocalDate.of(2020, Month.JANUARY, 1);

	/** Constant for the maximum capacity of a Hotel Suite */
	private static final int HOTEL_MAX_CAPACITY = 2;

	/** Constant for the maximum capacity of a Conference Room */
	public static final int CONFERENCE_MAX_CAPACITY = 25;

	/**
	 * gets the instance of the property manager
	 * 
	 * @return the property manager
	 */
	public static PropertyManager getInstance() {
		if (instance == null) {
			instance = new PropertyManager();
		}
		return instance;
	}

	/**
	 * Constructs a property manager by initializing the room and customerBases to
	 * empty list
	 */
	private PropertyManager() {
		rooms = new SortedLinkedListWithIterator<RentalUnit>();
		customerBase = new SimpleArrayList<Client>();
		kindFilter = "";
		inServiceFilter = false;
	}

	/**
	 * Adds a new client with the given name and id to the client database.
	 * 
	 * @param name Name of the new client
	 * @param id   Unique id of the new client
	 * @return The new Client who was created and registered
	 * @throws DuplicateClientException if id of the new client matches one for an
	 *                                  existing client.
	 * @throws IllegalArgumentException if the name is null, empty (when trimmed),
	 *                                  or contains any characters that are not
	 *                                  blanks or not alphanumeric. Also throws
	 *                                  IllegalArgumentException if the id is null,
	 *                                  empty (when trimmed), or contains any
	 *                                  characters that are non-alphanumeric or that
	 *                                  don't belong to the set ['@', '#', '$'].
	 */
	@Override
	public Client addNewClient(String name, String id) throws DuplicateClientException, IllegalArgumentException {
		Client addClient = null;
		try {
			addClient = new Client(name, id);
		} catch (Exception e) {
			throw new IllegalArgumentException("Improper name or id.");
		}
		try {
			customerBase.add(addClient);
		} catch (IllegalArgumentException e) {
			throw new DuplicateClientException("There is already a client with this id.");
		}
		return addClient;
	}

	/**
	 * Adds a new RentalUnit with the given parameters to the system.
	 * 
	 * @param kind     Type of RentalUnit (starts with 'O' for office, 'C' for
	 *                 conference room, 'H' for hotel suite)
	 * @param location String of the form FF-RR, where FF is the floor, and RR is
	 *                 the room.
	 * @param capacity Number of people the unit can accommodate on any single day
	 * @return The new RentalUnit that was created
	 * @throws IllegalArgumentException if the parameters do not describe a valid
	 *                                  location and type
	 * @throws DuplicateRoomException   if the floor and room match another rental
	 *                                  unit already in the Landlord's property
	 *                                  database
	 */
	@Override
	public RentalUnit addNewUnit(String kind, String location, int capacity) throws DuplicateRoomException {
		RentalUnit addUnit = null;
		if (kind.startsWith("O")) {
			addUnit = new Office(location, capacity);
		} else if (kind.startsWith("C")) {
			addUnit = new ConferenceRoom(location, capacity);
		} else if (kind.startsWith("H")) {
			addUnit = new HotelSuite(location, capacity);
		} else {
			throw new IllegalArgumentException("Invalid kind of rental unit");
		}
		try {
			rooms.add(addUnit);
		} catch (IllegalArgumentException e) {
			throw new DuplicateRoomException("There is already a rental unit with this location.");
		}
		return addUnit;
	}

	/**
	 * Adds a lease into the system with the provided information
	 * 
	 * @param c            client of the lease
	 * @param confNum      of the lease
	 * @param r            rental unit in the lease
	 * @param startDate    of the lease
	 * @param endDate      of the lease
	 * @param numOccupants of the lease
	 * @throws IllegalArgumentException there are any inconsistencies with the
	 *                                  property manager’s current data
	 */
	public void addLeaseFromFile(Client c, int confNum, RentalUnit r, LocalDate startDate, LocalDate endDate,
			int numOccupants) {
		try {
			Lease newLease = new Lease(confNum, c, r, startDate, endDate, numOccupants);

			r.checkDates(startDate, endDate);
			if (numOccupants <= 0) {
				throw new IllegalArgumentException();
			}
			if (r instanceof Office && numOccupants > 150) {
				throw new IllegalArgumentException();
			} else if (r instanceof ConferenceRoom && numOccupants > CONFERENCE_MAX_CAPACITY) {
				throw new IllegalArgumentException();
			} else if (r instanceof HotelSuite && numOccupants > HOTEL_MAX_CAPACITY) {
				throw new IllegalArgumentException();
			}
			if (customerBase.contains(c) && rooms.contains(r)) {
				customerBase.get(customerBase.indexOf(c)).addNewLease(newLease);
				rooms.get(rooms.indexOf(r)).recordExistingLease(confNum, c, startDate, endDate, numOccupants);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Sets filters for rental units so that only those that match the filters are
	 * considered.
	 * 
	 * @param kindFilter      String type of rental units
	 * @param inServiceFilter boolean for whether or not the rental units are in
	 *                        service
	 */
	@Override
	public void filterRentalUnits(String kindFilter, boolean inServiceFilter) {
		if (kindFilter.startsWith("O") || kindFilter.startsWith("o")) {
			this.kindFilter = "O";
		} else if (kindFilter.startsWith("C") || kindFilter.startsWith("c")) {
			this.kindFilter = "C";
		} else if (kindFilter.startsWith("H") || kindFilter.startsWith("h")) {
			this.kindFilter = "H";
		} else if (kindFilter.startsWith("A") || kindFilter.startsWith("a") || kindFilter.startsWith("X")
				|| kindFilter.startsWith("x")) {
			this.kindFilter = "";
		} else {
			this.kindFilter = null;
		}
		this.inServiceFilter = inServiceFilter;
	}

	/**
	 * Creates a new lease with information based on the given parameters.
	 * 
	 * @param clientIndex   Index of the client in the Landlord's customer base
	 * @param propertyIndex Index of the rental unit in the Landlord's filtered list
	 *                      of rental units
	 * @param start         Start date for the lease
	 * @param duration      Duration of the lease (units depending on rental unit
	 *                      type)
	 * @param occupants     Number of occupants the lease is for
	 * @return the created lease
	 * @throws IllegalArgumentException if the parameters do not constitute valid
	 *                                  lease data
	 */
	@Override
	public Lease createLease(int clientIndex, int propertyIndex, LocalDate start, int duration, int occupants) {
		if (clientIndex < 0 || clientIndex >= getInstance().customerBase.size()) {
			throw new IllegalArgumentException();
		}
		propertyIndex = this.getRoomsIndexFromFiltered(propertyIndex);
		Client owner = getInstance().customerBase.get(clientIndex);
		RentalUnit property = getInstance().rooms.get(propertyIndex);
		LocalDate end = null;
		
		if (rooms.get(propertyIndex) instanceof ConferenceRoom) {
			end = start.plusDays(duration).minusDays(1);
		} else if (rooms.get(propertyIndex) instanceof HotelSuite) {
			end = start.plusWeeks(duration);
		} else {
			end = start.plusMonths(duration).minusDays(1);
		}
		if (!property.isInService()) {
			throw new IllegalArgumentException("Rental unit is not in service.");
		} else if (occupants > property.getCapacity()) {
			throw new IllegalArgumentException("Insufficient capacity for proposed lease.");
		}

		try {
			property.checkDates(start, end);
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid start date or duration.");
		}

		Lease lease = new Lease(owner, property, start, end, occupants);
		property.addLease(lease);
		owner.addNewLease(lease);
		return lease;
	}

	/**
	 * Cancels the lease in the given position on the client's list of leases.
	 * 
	 * @param clientIndex Index of the client whose lease is to be cancelled
	 * @param leaseIndex  Position of the lease in the client's list
	 * @throws IllegalArgumentException if clientIndex or leaseIndex are not valid
	 */
	@Override
	public void cancelClientsLease(int clientIndex, int leaseIndex) {
		if (clientIndex < 0 || clientIndex >= this.customerBase.size()) {
			throw new IllegalArgumentException();
		}
		if (leaseIndex < 0 || leaseIndex > this.customerBase.get(clientIndex).listLeases().length) {
			throw new IllegalArgumentException();
		}
		Lease cancelLease = customerBase.get(clientIndex).cancelLeaseAt(leaseIndex);
		try {
			int cancelConfirmNum = cancelLease.getConfirmationNumber();
			RentalUnit property = cancelLease.getProperty();
			int i = 0;
			int propIndex = -1;
			for (i = 0; i < rooms.size(); i++) {
				if (rooms.get(i).equals(property)) {
					propIndex = i;
					break;
				}
			}
			for (int j = 0; j < this.rooms.get(propIndex).listLeases().length; j++) {
				if (Integer.parseInt(this.rooms.get(propIndex).listLeases()[j].substring(0, 6)) == cancelConfirmNum) {
					this.rooms.get(propIndex).cancelLeaseByNumber(cancelConfirmNum);
					return;
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Cancels all leases for a rental unit on or after a particular date. The
	 * remaining leases should still be valid.
	 * 
	 * @param propertyIndex Index for the rental unit (subject to filtering)
	 * @param start         Date for starting cancellations
	 * @return the RentalUnit that was removed
	 * @throws IllegalArgumentException if propertyIndex is not a valid index for
	 *                                  the rental units currently under
	 *                                  consideration
	 */
	@Override
	public RentalUnit removeFromService(int propertyIndex, LocalDate start) {
		try {
			int roomsIndex = getRoomsIndexFromFiltered(propertyIndex);
			RentalUnit removed = rooms.get(roomsIndex);
			SortedList<Lease> removedLeases = removed.removeFromServiceStarting(start);
			removed.takeOutOfService();
			for (int i = 0; i < removedLeases.size(); i++) {
				removedLeases.get(i).getClient().cancelLeaseWithNumber(removedLeases.get(i).getConfirmationNumber());
			}
			return removed;
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}

	}

	/**
	 * Returns the rental unit at the given position to service. Does nothing if the
	 * rental unit is already in service or if the position does not correspond to
	 * any rental unit (subject to filtering).
	 * 
	 * @param propertyIndex Position/index of the rental unit (subject to filtering)
	 * @throws IllegalArgumentException if propertyIndex is not a valid index for
	 *                                  the rental units currently under
	 *                                  consideration
	 */
	@Override
	public void closeRentalUnit(int propertyIndex) {
		try {
			int roomsIndex = getRoomsIndexFromFiltered(propertyIndex);
			if (roomsIndex < 0 || propertyIndex >= getInstance().rooms.size()) {
				throw new IllegalArgumentException();
			}

			SortedList<Lease> removedLeases = rooms.get(roomsIndex).removeFromServiceStarting(MIN_DATE);
			this.rooms.remove(roomsIndex);
			for (int i = 0; i < removedLeases.size(); i++) {
				try {
					removedLeases.get(i).getClient()
							.cancelLeaseWithNumber(removedLeases.get(i).getConfirmationNumber());
				} catch (Exception e) {
					continue;
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Returns the rental unit at the given position to service. Does nothing if the
	 * rental unit is already in service or if the position does not correspond to
	 * any rental unit (subject to filtering).
	 * 
	 * @param propertyIndex Position/index of the rental unit (subject to filtering)
	 * @throws IllegalArgumentException if propertyIndex is not a valid index for
	 *                                  the rental units currently under
	 *                                  consideration
	 */
	@Override
	public void returnToService(int propertyIndex) {
		int roomsIndex = getRoomsIndexFromFiltered(propertyIndex);
		try { rooms.get(roomsIndex).returnToService(); } 
		catch (IndexOutOfBoundsException e) { throw new IllegalArgumentException(); }
	}

	/**
	 * Who are the clients for this Landlord's properties?
	 * 
	 * @return an array of strings, where each string describes a client
	 */
	@Override
	public String[] listClients() {
		String[] listClients = new String[customerBase.size()];
		for (int i = 0; i < customerBase.size(); i++) {
			listClients[i] = customerBase.get(i).getName() + " (" + customerBase.get(i).getId() + ")";
		}
		return listClients;
	}

	/**
	 * What are the leases for a particular client?
	 * 
	 * @param clientIndex Index of the targeted client in the landlord's list of
	 *                    clients
	 * @return an array of strings in which each string describes a lease for the
	 *         targeted client
	 * @throws IllegalArgumentException if the clientIndex does not correspond to
	 *                                  any client.
	 */
	@Override
	public String[] listClientLeases(int clientIndex) {
		return customerBase.get(clientIndex).listLeases();
	}

	/**
	 * What are the rental units for this landlord? (Consider only the units that
	 * meet filters currently in place.)
	 * 
	 * @return an array of strings in which each string describes a rental unit that
	 *         meets all filters in place. There are exactly as many strings in the
	 *         array as there are such rental units.
	 */
	@Override
	public String[] listRentalUnits() {
		SimpleArrayList<String> filteredArr = new SimpleArrayList<String>();
		for (int i = 0; i < rooms.size(); i++) {
			if (this.kindFilter.isEmpty()) {
				if (this.inServiceFilter && this.rooms.get(i).isInService()) {
					filteredArr.add(rooms.get(i).getDescription());
				} else if (!this.inServiceFilter) {
					filteredArr.add(rooms.get(i).getDescription());
				}
			}

			if (this.kindFilter.equalsIgnoreCase("C") && this.rooms.get(i).getDescription().charAt(0) == 'C') {
				if (this.inServiceFilter && this.rooms.get(i).isInService()) {
					filteredArr.add(rooms.get(i).getDescription());
				} else if (!this.inServiceFilter) {
					filteredArr.add(rooms.get(i).getDescription());
				}
			}

			if (this.kindFilter.equalsIgnoreCase("H") && this.rooms.get(i).getDescription().charAt(0) == 'H') {
				if (this.inServiceFilter && this.rooms.get(i).isInService()) {
					filteredArr.add(rooms.get(i).getDescription());
				} else if (!this.inServiceFilter) {
					filteredArr.add(rooms.get(i).getDescription());
				}
			}

			if (this.kindFilter.equalsIgnoreCase("O") && this.rooms.get(i).getDescription().charAt(0) == 'O') {
				if (this.inServiceFilter && this.rooms.get(i).isInService()) {
					filteredArr.add(rooms.get(i).getDescription());
				} else if (!this.inServiceFilter) {
					filteredArr.add(rooms.get(i).getDescription());
				}
			}
		}

		int size = filteredArr.size();
		String[] filteredReturnArr = new String[size];
		for (int i = 0; i < size; i++) {
			filteredReturnArr[i] = filteredArr.get(i);
		}
		return filteredReturnArr;
	}

	/**
	 * What are the leases for the rental unit at this particular index in the
	 * filtered list of rental units?
	 * 
	 * @param propertyIndex Index of the targeted rental unit (subject to filtering)
	 * @return an array of strings in which each string describes a lease for the
	 *         targeted rental unit.
	 * @throws IllegalArgumentException if propertyIndex is not a valid index for
	 *                                  the rental units currently under
	 *                                  consideration
	 */
	@Override
	public String[] listLeasesForRentalUnit(int propertyIndex) {
		if (propertyIndex < 0 || propertyIndex >= rooms.size()) {
			throw new IllegalArgumentException();
		}
		int roomsIndex = getRoomsIndexFromFiltered(propertyIndex);
		String[] listRentalLeases = rooms.get(roomsIndex).listLeases();
		return listRentalLeases;
	}

	/**
	 * Helper method to return the index of a unit in rooms based on its index in
	 * the filtered list of rental units
	 * 
	 * @param filteredIndex the index of the unit in the filtered list
	 * @return the index of the unit in rooms
	 */
	private int getRoomsIndexFromFiltered(int filteredIndex) {
		String[] filteredUnits = this.listRentalUnits();
		for (int i = 0; i < rooms.size(); i++) {
			if (filteredUnits[filteredIndex].equals(rooms.get(i).getDescription())) { return i; }
		}
		return -1;
	}

	/**
	 * Removes all Landlord data and resets the reservation confirmation numbering
	 * to 0.
	 */
	@Override
	public void flushAllData() {
		rooms = new SortedLinkedListWithIterator<RentalUnit>();
		customerBase = new SimpleArrayList<Client>();
		kindFilter = "";
		inServiceFilter = false;
		Lease.resetConfirmationNumbering(0);
	}

	/**
	 * Gets the Rental Unit at the specified location
	 * 
	 * @param location of the rental unit
	 * @return the RentalUnit at the location
	 * @throws IllegalArgumentException if there is no such rental unit.
	 */
	public RentalUnit getUnitAtLocation(String location) {
		RentalUnit unitAtLocation = null;
		for (int i = 0; i < rooms.size(); i++) {
			String roomLocation = rooms.get(i).getFloor() + "-" + rooms.get(i).getRoom();
			if (roomLocation.equals(location)) {
				unitAtLocation = rooms.get(i);
			}
		}
		if (unitAtLocation == null) {
			throw new IllegalArgumentException();
		}
		return unitAtLocation;
	}

}
