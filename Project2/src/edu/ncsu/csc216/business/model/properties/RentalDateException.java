/**
 * 
 */
package edu.ncsu.csc216.business.model.properties;

/**
 * RentalDateException to be thrown when the rental data is invalid
 * 
 * @author Kaitlyn Gosline
 * @author Sanjana Cheerla
 */
public class RentalDateException extends Exception {

	/** ID used for serialization. */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates RenalDataException with given message
	 * 
	 * @param message the message for the RentalDataException
	 */
	public RentalDateException(String message) {
		super(message);
	}

}
