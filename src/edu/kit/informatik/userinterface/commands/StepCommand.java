package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.exception.LogicException;

import java.util.List;

/**
 * {@link Command} to let all trains drive by n-units.
 * Call {@link this#setArguments(List)} before calling {@link this#execute()}.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class StepCommand extends Command {
    private short speed;

    /**
     * Package private to avoid direct initialisation without using the {@link CommandParser}.
     */
    StepCommand() {
    }

    @Override
    public void execute() {
        try {
            register.step(speed);
        } catch (LogicException e) {
            Terminal.printError(e.getMessage());
        }
    }

    @Override
    public void setArguments(List<String> arguments) {
        try {
            speed = Short.parseShort(arguments.get(0));
        } catch (NumberFormatException e) {
            Terminal.printError("speed has to be a 16-bit integer");
        }
    }

    @Override
    public String getName() {
        return "step";
    }

    @Override
    public int getNumberOfArguments() {
        return 1;
    }

    @Override
    public String getCommandDescription() {
        return "step <speed>";
    }
}
