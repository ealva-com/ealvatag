package org.jaudiotagger.tag;

/**
 */
public class InvalidDataTypeException  extends InvalidTagException
{
    /**
     * Creates a new InvalidTagException datatype.
     */
    public InvalidDataTypeException()
    {
    }

    public InvalidDataTypeException(Throwable ex)
    {
        super(ex);
    }

    /**
     * Creates a new InvalidTagException datatype.
     *
     * @param msg the detail message.
     */
    public InvalidDataTypeException(String msg)
    {
        super(msg);
    }

    public InvalidDataTypeException(String msg, Throwable ex)
    {
        super(msg, ex);
    }
}
