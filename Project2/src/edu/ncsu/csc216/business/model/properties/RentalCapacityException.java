/**
 * 
 */
package edu.ncsu.csc216.business.model.properties;

/**
 * RentalCapacityException to be thrown when rental capacity is invalid
 * 
 * @author Kaitlyn Gosline
 * @author Sanjana Cheerla
 */
public class RentalCapacityException extends Exception {

	/** ID used for serialization. */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a RentalCapacityException with a given message
	 * 
	 * @param message the message for the RentalCapacityException
	 */
	public RentalCapacityException(String message) {
		super(message);
	}

}
