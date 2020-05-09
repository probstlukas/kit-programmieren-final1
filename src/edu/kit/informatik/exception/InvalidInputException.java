package edu.kit.informatik.exception;

/**
 * This exception is used to indicate invalid input during command line parsing.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class InvalidInputException extends Exception {

    /**
     * Auto generated UID for serializing. Actually not needed, but
     * absence produces annoying warnings.
     */
    private static final long serialVersionUID = -8179857765746080778L;

    /**
     * Creates a new InvalidInputException with the given detailed message.
     *
     * @param pMessage some detailed error message (null is not allowed)
     */
    public InvalidInputException(final String pMessage) {
        super(pMessage);
    }
}