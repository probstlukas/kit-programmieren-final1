package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.railway.Register;
import edu.kit.informatik.exception.InvalidInputException;
import edu.kit.informatik.userinterface.Session;

import java.util.List;

/**
 * Base class for all commands. Stores the {@link Register} to operate on.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public abstract class Command {
    /**
     * The register to operate on.
     */
    protected Register register;

    /**
     * This allows a command to manipulate the {@link Session}. For instance, {@link Session#terminate()}
     * can be called on it or most commonly it will be used to get the current {@link Register}.
     * For commands that don't have to interact with the session this will be ignored.
     *
     * @param session the register was initiated with
     */
    public void setSession(Session session) {
        this.register = session.getRegister();
    }

    /**
     * Runs the command.
     */
    public abstract void execute();

    /**
     * Passes valid arguments to the command. If the command does not expect any arguments this
     * will be ignored.
     *
     * @param arguments specified by the user
     * @throws InvalidInputException if the arguments turn out to be
     *  syntactically incorrect
     */
    public void setArguments(List<String> arguments) throws InvalidInputException {
    }

    /**
     * Gets the name of the command.
     *
     * @return the name of the command
     */
    public abstract String getName();

    /**
     * Gets the number of arguments of the command.
     *
     * @return the number of arguments the command expects
     */
    public abstract int getNumberOfArguments();

    /**
     * Gets the description of the command.
     *
     * @return the description of the command.
     */
    public abstract String getCommandDescription();
}
