package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.exception.LogicException;
import edu.kit.informatik.userinterface.InOutput;
import edu.kit.informatik.exception.InvalidInputException;
import edu.kit.informatik.util.Point;

import java.util.List;

/**
 * {@link Command} to put a train on a track.
 * Call {@link this#setArguments(List)} before calling {@link this#execute()}.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class PutTrainCommand extends Command {
    private int trainId;
    private Point point;
    /**
     * There is a small difference between points and vectors:
     * - Points are locations in space.
     * - Vectors are displacements in space.
     * However, the datatype makes sense in this context.
     */
    private Point directionVector;

    /**
     * Package private to avoid direct initialisation without using the {@link CommandParser}.
     */
    PutTrainCommand() {
    }

    @Override
    public void execute() {
        try {
            register.putTrain(trainId, point, directionVector);
            Terminal.printLine(InOutput.OK_MESSAGE);
        } catch (LogicException e) {
            Terminal.printError(e.getMessage());
        }
    }

    @Override
    public void setArguments(List<String> arguments) throws InvalidInputException {
        try {
            trainId = Integer.parseInt(arguments.get(0));
        } catch (NumberFormatException e) {
            throw new InvalidInputException("train ID must be a 32-bit integer");
        }
        point = InOutput.parsePoint(arguments.get(2));
        directionVector = InOutput.parseVector(arguments.get(5));
    }

    @Override
    public String getName() {
        return "put train";
    }

    @Override
    public int getNumberOfArguments() {
        return 6;
    }

    @Override
    public String getCommandDescription() {
        return "put train <trainID> at <point> in direction <x>,<y>";
    }
}
