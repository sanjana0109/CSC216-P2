/**
 * 
 */
package edu.ncsu.csc216.business.model.properties;

/**
 * RentalOutOfServiceException to be thrown when a rental unit is out a service
 *
 * @author Kaitlyn Gosline
 * @author Sanjana Cheerla
 */
public class RentalOutOfServiceException extends Exception {

	/** ID used for serialization. */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates RentalOutOfServiceException with a given message
	 * 
	 * @param message the message for the RentalOutOfServiceException
	 */
	public RentalOutOfServiceException(String message) {
		super(message);
	}

}
