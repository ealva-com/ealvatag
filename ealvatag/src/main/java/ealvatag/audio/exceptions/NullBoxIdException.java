package ealvatag.audio.exceptions;

/**
 * Thrown if when trying to read box id just finds nulls
 * Normally an error, but if occurs at end of file we allow it
 */
public class NullBoxIdException extends RuntimeException {
  /**
	 * 
	 */
	private static final long serialVersionUID = -7114685230539495823L;

public NullBoxIdException(String message) {
    super(message);
  }
}
