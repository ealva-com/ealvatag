package ealvatag.audio.exceptions;

import static ealvatag.logging.ErrorMessage.exceptionMsg;

import java.io.IOException;

/**
 * Should be thrown when unable to rename a file when it is expected it should rename. For example could occur on Vista
 * because you do not have Special Permission 'Delete' set to Denied.
 */
public class UnableToRenameFileException extends IOException {
  public UnableToRenameFileException(String message) {
    super(message);
  }

  public UnableToRenameFileException(String message, Object... formatArgs) {
    super(exceptionMsg(message, formatArgs));
  }
}
