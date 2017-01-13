package ealvatag.tag;

/**
 * Thrown if the key cannot be found
 * <p>
 * <p>Should not happen with well written code, hence RuntimeException.
 */
public class KeyNotFoundException extends RuntimeException {
    /**
     * Creates a new KeyNotFoundException datatype.
     */
    public KeyNotFoundException() {
    }

    /**
     * Creates a new KeyNotFoundException datatype.
     *
     * @param msg the detail message.
     */
    public KeyNotFoundException(String msg) {
        super(msg);
    }

}
