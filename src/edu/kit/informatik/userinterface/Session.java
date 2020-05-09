package edu.kit.informatik.userinterface;

import edu.kit.informatik.exception.InvalidInputException;
import edu.kit.informatik.railway.Register;
import edu.kit.informatik.Terminal;
import edu.kit.informatik.userinterface.commands.Command;
import edu.kit.informatik.userinterface.commands.CommandParser;

import java.util.List;

/**
 * User input and output are handled here. Exception handling for invalid input also takes place here.
 * A session can be started and stopped.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class Session {
    private boolean running = true;
    private Register register;

    /**
     * After the session is started, this method remains in a loop until the {@link this#terminate()} method is called.
     */
    public void run() {
        register = new Register();
        while (running) {
            String input = Terminal.readLine();
            try {
                final List<Command> commands = CommandParser.initialiseCommands();
                final Command command = commands.stream()
                        .filter(cmd -> (input.startsWith(cmd.getName()) && (input.length() == cmd.getName().length()
                                || input.charAt(cmd.getName().length()) == ' ')))
                        .findAny()
                        .orElseThrow(() -> new InvalidInputException("unknown command"));
                final List<String> arguments = CommandParser.getArguments(input, command);
                if (command.getNumberOfArguments() > 0) {
                    command.setArguments(arguments);
                }
                command.setSession(this);
                command.execute();
            } catch (InvalidInputException e) {
                Terminal.printError(e.getMessage());
            }
        }
    }

    /**
     * Terminates the active session.
     */
    public void terminate() {
        running = false;
    }

    /**
     * Gets the register of the session.
     *
     * @return register of the session
     */
    public Register getRegister() {
        return register;
    }
}