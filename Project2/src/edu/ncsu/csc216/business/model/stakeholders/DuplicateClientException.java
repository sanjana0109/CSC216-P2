package edu.ncsu.csc216.business.model.stakeholders;

/**
 * DuplicateClientException class when there are duplicate clients
 * 
 * @author Sanjana Cheerla
 * @author Kaitlyn Gosline
 */
public class DuplicateClientException extends Exception {

	/** ID used for serialization. */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a DuplicateClientException with the provided message
	 * 
	 * @param message for the DuplicateClientException
	 */
	public DuplicateClientException(String message) {
		super(message);
	}

}
