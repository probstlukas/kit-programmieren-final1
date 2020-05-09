package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.exception.LogicException;
import edu.kit.informatik.userinterface.InOutput;
import edu.kit.informatik.exception.InvalidInputException;
import edu.kit.informatik.util.Point;

import java.util.List;

/**
 * {@link Command} to set a switch.
 * Call {@link this#setArguments(List)} before calling {@link this#execute()}.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class SetSwitchCommand extends Command {
    private int trackId;
    private Point point;

    /**
     * Package private to avoid direct initialisation without using the {@link CommandParser}.
     */
    SetSwitchCommand() {
    }

    @Override
    public void execute() {
        try {
            register.setSwitch(trackId, point);
            Terminal.printLine(InOutput.OK_MESSAGE);
        } catch (LogicException e) {
            Terminal.printError(e.getMessage());
        }
    }

    @Override
    public void setArguments(List<String> arguments) throws InvalidInputException {
        try {
            trackId = Integer.parseInt(arguments.get(0));
        } catch (NumberFormatException e) {
            throw new InvalidInputException("track ID must be a 32-bit integer");
        }
        if (!"position".equals(arguments.get(1))) {
            throw new InvalidInputException("second argument must be 'position'. Instead you typed: "
                    + arguments.get(1));
        }
        point = InOutput.parsePoint(arguments.get(2));
    }

    @Override
    public String getName() {
        return "set switch";
    }

    @Override
    public int getNumberOfArguments() {
        return 3;
    }

    @Override
    public String getCommandDescription() {
        return "set switch <trackID> position <point>";
    }
}
