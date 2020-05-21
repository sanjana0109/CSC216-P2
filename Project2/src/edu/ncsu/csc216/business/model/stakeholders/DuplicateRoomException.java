package edu.ncsu.csc216.business.model.stakeholders;

/**
 * DuplicateRoomException class when there are duplicate rooms
 * 
 * @author Sanjana Cheerla
 * @author Kaitlyn Gosline
 */
public class DuplicateRoomException extends Exception {

	/** ID used for serialization. */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a DuplicateRoomException with the provided message
	 * 
	 * @param message for the DuplicateRoomException
	 */
	public DuplicateRoomException(String message) {
		super(message);
	}
}
