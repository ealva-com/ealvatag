package ealvatag.audio.exceptions;

/**
 * This exception should be thrown idf it appears the file is a video file, ealvatag only supports audio
 * files.
 */
public class CannotReadVideoException extends CannotReadException
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 5031784427255883755L;

	/**
     * Creates an instance.
     */
    public CannotReadVideoException()
    {
        super();
    }

    public CannotReadVideoException(Throwable ex)
    {
        super(ex);
    }

    /**
     * Creates an instance.
     *
     * @param message The message.
     */
    public CannotReadVideoException(String message)
    {
        super(message);
    }

    /**
     * Creates an instance.
     *
     * @param message The error message.
     * @param cause   The throwable causing this exception.
     */
    public CannotReadVideoException(String message, Throwable cause)
    {
        super(cause, message);
    }
}
