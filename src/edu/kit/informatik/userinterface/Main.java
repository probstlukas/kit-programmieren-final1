package edu.kit.informatik.userinterface;

/**
 * The program's main entry point.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class Main {
    /**
     * Initiates user input by creating and running a new {@link Session}.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Session session = new Session();
        session.run();
    }
}