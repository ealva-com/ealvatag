package ealvatag.audio.exceptions;

import static ealvatag.logging.ErrorMessage.exceptionMsg;

import java.io.IOException;

/**
 * Should be thrown when unable to create a file when it is expected it should be creatable. For example because
 * you dont have permission to write to the folder that it is in.
 */
public class UnableToCreateFileException extends IOException {
  public UnableToCreateFileException(final String message) {
    super(message);
  }

  public UnableToCreateFileException(final String message, final Object... formatArgs) {
    super(exceptionMsg(message, formatArgs));
  }

  public UnableToCreateFileException(final Throwable throwable, final String message, final Object... formatArgs) {
    super(exceptionMsg(message, formatArgs), throwable);
  }

}
