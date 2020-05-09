package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.userinterface.InOutput;
import edu.kit.informatik.exception.InvalidInputException;

import java.util.List;

/**
 * {@link Command} to delete a train.
 * Call {@link this#setArguments(List)} before calling {@link this#execute()}.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class DeleteTrainCommand extends Command {
    private int id;

    /**
     * Package private to avoid direct initialisation without using the {@link CommandParser}.
     */
    DeleteTrainCommand() {
    }

    @Override
    public void execute() {
        try {
            register.removeTrain(id);
            Terminal.printLine(InOutput.OK_MESSAGE);
        } catch (InvalidInputException e) {
            Terminal.printError(e.getMessage());
        }
    }

    @Override
    public void setArguments(List<String> arguments) throws InvalidInputException {
        try {
            id = Integer.parseInt(arguments.get(0));
        } catch (NumberFormatException e) {
            throw new InvalidInputException("ID must be a 32-bit integer");
        }
    }

    @Override
    public String getName() {
        return "delete train";
    }

    @Override
    public int getNumberOfArguments() {
        return 1;
    }

    @Override
    public String getCommandDescription() {
        return "delete train <id>";
    }
}
