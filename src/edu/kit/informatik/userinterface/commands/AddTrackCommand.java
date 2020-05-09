package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.exception.LogicException;
import edu.kit.informatik.railway.Track;
import edu.kit.informatik.util.Point;
import edu.kit.informatik.Terminal;
import edu.kit.informatik.userinterface.InOutput;
import edu.kit.informatik.exception.InvalidInputException;

import java.util.Arrays;
import java.util.List;

/**
 * {@link Command} to add a new track.
 * Call {@link this#setArguments(List)} before calling {@link this#execute()}.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class AddTrackCommand extends Command {
    private List<Point> points;

    /**
     * Package private to avoid direct initialisation without using the {@link CommandParser}.
     */
    AddTrackCommand() {
    }

    @Override
    public void execute() {
        int trackId = register.getNextId(register.getNetwork().getTracks().keySet());
        try {
            register.addTrack(new Track(trackId, points));
            Terminal.printLine(trackId);
        } catch (LogicException e) {
            Terminal.printError(e.getMessage());
        }
    }

    @Override
    public void setArguments(List<String> arguments) throws InvalidInputException {
        Point startPoint = InOutput.parsePoint(arguments.get(0));
        if (!InOutput.ARROW_SEPARATOR.equals(arguments.get(1))) {
            throw new InvalidInputException("second argument must be '->'. Instead you typed: " + arguments.get(1));
        }
        Point endPoint = InOutput.parsePoint(arguments.get(2));
        points = Arrays.asList(startPoint, endPoint);
    }

    @Override
    public String getName() {
        return "add track";
    }

    @Override
    public int getNumberOfArguments() {
        return 3;
    }

    @Override
    public String getCommandDescription() {
        return "add track <startpoint> -> <endpoint>";
    }
}
