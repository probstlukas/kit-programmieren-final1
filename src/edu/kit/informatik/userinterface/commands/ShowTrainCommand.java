package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.railway.Train;
import edu.kit.informatik.exception.InvalidInputException;
import edu.kit.informatik.userinterface.InOutput;

import java.util.List;
import java.util.Map;

/**
 * {@link Command} to show the graphic of a train.
 * Call {@link this#setArguments(List)} before calling {@link this#execute()}.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class ShowTrainCommand extends Command {
    private int trainId;

    /**
     * Package private to avoid direct initialisation without using the {@link CommandParser}.
     */
    ShowTrainCommand() {
    }

    @Override
    public void execute() {
        Map<Integer, Train> trains = register.getTrains();
        if (trains.containsKey(trainId)) {
            Train train = trains.get(trainId);
            Terminal.printLine(train.show());
        } else {
            Terminal.printError("train with ID " + trainId + " does not exist");
        }
    }

    @Override
    public void setArguments(List<String> arguments) throws InvalidInputException {
        try {
            trainId = InOutput.parseNumber(arguments.get(0), "train ID");
        } catch (NumberFormatException e) {
            throw new InvalidInputException("train ID must be a 32-bit integer");
        }
    }

    @Override
    public String getName() {
        return "show train";
    }

    @Override
    public int getNumberOfArguments() {
        return 1;
    }

    @Override
    public String getCommandDescription() {
        return "show train <trainID>";
    }
}
