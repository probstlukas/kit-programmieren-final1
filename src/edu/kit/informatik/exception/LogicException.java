package edu.kit.informatik.exception;

// I am kind of unsure if one single exception is sufficient. But at the same time there is not
// that much extra information that those exceptions could provide while a great number of
// exceptions would make the program more complex.

/**
 * This exception is thrown when a method cannot be executed with the provided arguments at this time.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class LogicException extends Exception {
    /**
     * Auto generated UID for serializing. Actually not needed, but
     * absence produces annoying warnings.
     */
    private static final long serialVersionUID = -4737318736086628400L;

    /**
     * Creates a new LogicException with the given detailed message.
     *
     * @param message some detailed error message (null is not allowed)
     */
    public LogicException(final String message) {
        super(message);
    }
}