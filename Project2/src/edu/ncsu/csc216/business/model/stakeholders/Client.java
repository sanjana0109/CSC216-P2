package edu.ncsu.csc216.business.model.stakeholders;

import edu.ncsu.csc216.business.list_utils.SimpleArrayList;
import edu.ncsu.csc216.business.model.contracts.Lease;

/**
 * Creates a client object of name and id, There are other methods that are
 * related to the client and lease classes such as add new lease, list lease,
 * cancel lease and cancel lease with a specific confirmation number.
 * 
 * @author Kaitlyn Gosline
 * @author Sanjana Cheerla
 *
 */
public class Client {

	/** name of the client */
	private String name;

	/** id of the client */
	private String id;

	/** SimpleArrayList of current leases */
	private SimpleArrayList<Lease> myLeases;

	/**
	 * Creates a client with the provided information
	 * 
	 * @param name of the client
	 * @param id   of the client
	 * @throws IllegalArgumentException if any of the parameters are not valid
	 */
	public Client(String name, String id) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		if (!isValidName(name)) {
			throw new IllegalArgumentException();
		}
		if (id == null) {
			throw new IllegalArgumentException();
		}
		if (!isValidId(id)) {
			throw new IllegalArgumentException();
		}
		this.name = trimNameAndIds(name);
		this.id = trimNameAndIds(id);
		this.myLeases = new SimpleArrayList<Lease>();
	}

	/**
	 * Checks to see if the name is alpha numeric characters with spaces
	 * 
	 * @param name being checked to see if it alphanumeric characters with spaces
	 * @return true if the name is valid, false if not
	 */
	private boolean isValidName(String name) {

		if (name.isBlank()) {
			return false;
		}

		boolean alphanumeric = false;
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (Character.isWhitespace(c) || Character.isLetterOrDigit(c)) {
				alphanumeric = true;
			} else {
				alphanumeric = false;
				break;
			}
		}
		System.out.println();
		return alphanumeric;
	}

	/**
	 * checks to see if the ID is valid (have at least three characters, where each
	 * character is alphanumeric or one of the characters @, #, or $. Client ids are
	 * unique).
	 * 
	 * @param id being checked to see if its valid
	 * @return true if the name is valid, false if not
	 */
	private boolean isValidId(String id) {
		boolean validID = false;

		if (id.isBlank()) {
			return validID;
		}
		if (id.length() < 3) {
			return validID;
		}
		if (containsInternalBlanks(id)) {
			return validID;
		}
		for (int i = 0; i < id.length(); i++) {
			char c = id.charAt(i);
			if (Character.isWhitespace(c) || Character.isLetterOrDigit(c) || c == '@' || c == '#' || c == '$') {
				validID = true;

			} else {
				validID = false;
				break;
			}
		}
		System.out.println();
		return validID;
	}

	/**
	 * Checks to see if a string has internal blanks
	 * 
	 * @param s the string being checked to see if there are internal blanks
	 * @return true if there are internal blanks, false otherwise
	 */
	private boolean containsInternalBlanks(String s) {
		// case one leading white spaces get ignored
		int k = 0;
		boolean leadingWhiteSpace = false;
		for (k = 0; k < s.length(); k++) {
			if (Character.isWhitespace(s.charAt(k))) {
				leadingWhiteSpace = true;
			}
			if (!Character.isWhitespace(s.charAt(k))) {
				leadingWhiteSpace = false;
				break;
			}
		}

		// case 2 internal white spaces
		if (!leadingWhiteSpace) {
			for (int i = k; i < s.length(); i++) {
				if (Character.isWhitespace(s.charAt(i))) {
					for (int j = i; j < s.length(); j++) {
						if (Character.isLetterOrDigit(s.charAt(j))) {
							return true;
						}
					}
				}
			}
		}

		// case 3 no internal white spaces/only terminating white spaces
		return false;
	}

	/**
	 * trims the leading and terminating spaces of a string object
	 * 
	 * @param s string to be trimmed of white spaces before and after
	 * @return the newly trimmed string
	 */
	private String trimNameAndIds(String s) {
		int k = 0;
		@SuppressWarnings("unused")
		boolean leadingWhiteSpace = false;
		for (k = 0; k < s.length(); k++) {
			if (Character.isWhitespace(s.charAt(k))) {
				leadingWhiteSpace = true;
			}
			if (!Character.isWhitespace(s.charAt(k))) {
				leadingWhiteSpace = false;
				break;
			}
		}

		String leadingSpacesGone = s.substring(k);

		int i = 0;
		for (i = s.length() - 1; i >= k; i--) {
			if (!Character.isWhitespace(s.charAt(i))) {
				break;
			}
		}

		String terminatingSpacesGone = leadingSpacesGone.substring(0, i - k + 1);

		return terminatingSpacesGone;
	}

	/**
	 * Returns the name of the client
	 * 
	 * @return the name of the client as a String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the id of the client
	 * 
	 * @return the id of the client as a String
	 */
	public String getId() {
		return id;

	}

	/**
	 * Generates a hash code of the object
	 * 
	 * @return the hash code as an int
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * Checks to see of 2 objects are the same, have same name and if
	 * 
	 * @param obj the object being compared to the current Client object
	 * @return true if equal and false if not
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		if (id == null && other.getId() != null) {
			return false;
		}
		if (id != null && other.getId() == null) {
			return false;
		}
		if ((id != null && other.getId() != null && id.equals(other.getId())) || this.hashCode() == other.hashCode()) {
			return true;
		}
		if (!id.equals(other.getId())) {
			return false;
		}
		if (this.hashCode() != other.hashCode()) {
			return false;
		}
		return false;
	}

	/**
	 * Adds a new lease to the client's list of leases
	 * 
	 * @param lease to be added to the client's list
	 * @throws IllegalArgumentException if the lease does not belong to this client
	 */
	public void addNewLease(Lease lease) {
		if (lease == null || !this.equals(lease.getClient())) {
			throw new IllegalArgumentException();
		}
		this.myLeases.add(lease);
	}

	/**
	 * Returns an array of strings, where each string represents a lease of the
	 * current client
	 * 
	 * @return an array of strings containing the leases for that client
	 */
	public String[] listLeases() {
		int size = this.myLeases.size();
		String[] leaseString = new String[size];
		for (int i = 0; i < size; i++) {
			leaseString[i] = toStringLease(this.myLeases.get(i));
		}
		return leaseString;
	}

	/**
	 * Formats a lease object with the confirmation #, date, occupants, and the type
	 * of rental unit followed by the location
	 * 
	 * @param l lease that gets formatted
	 * @return a formatted client object
	 */
	private String toStringLease(Lease l) {
		String lease = "";
		String[] lArray = l.leaseData();

		lease += lArray[0] + " | ";

		lease += lArray[1] + " | ";

		String occupants = "";
		int numOccupants = Integer.parseInt(lArray[2]);
		if (numOccupants >= 100) {
			occupants += numOccupants;
		}
		if (numOccupants <= 99 && numOccupants >= 10) {
			occupants += " " + numOccupants;
		}
		if (numOccupants <= 9) {
			occupants += "  " + numOccupants;
		}
		lease += occupants + " | ";

		lease += lArray[3];

		return lease;
	}

	/**
	 * Cancels the lease at the provided index from the client's list of leases
	 * 
	 * @param idx of the lease being cancelled
	 * @return the cancelled lease
	 * @throws IllegalArgumentException if the idx is not valid
	 */
	public Lease cancelLeaseAt(int idx) {
		if (idx < 0 || idx >= this.myLeases.size()) {
			throw new IllegalArgumentException();
		}
		Lease l = this.myLeases.get(idx);
		this.myLeases.remove(idx);
		return l;
	}

	/**
	 * Cancels the lease with the provided confirmation number from the client's
	 * list of leases
	 * 
	 * @param confNum of the lease being cancelled
	 * @return the cancelled lease
	 * @throws IllegalArgumentException if the confirmation number does not
	 *                                  correspond to any lease for this client
	 */
	public Lease cancelLeaseWithNumber(int confNum) {
		Lease l = null;
		int i = 0;
		for (i = 0; i < this.myLeases.size(); i++) {
			if (this.myLeases.get(i).getConfirmationNumber() == confNum) {
				l = this.myLeases.get(i);
				break;
			}
		}
		if (l != null) {
			this.myLeases.remove(i);
			return l;
		}
		throw new IllegalArgumentException();
	}
}
