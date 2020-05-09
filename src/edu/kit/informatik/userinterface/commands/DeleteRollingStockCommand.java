package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.railway.Train;
import edu.kit.informatik.rollingstock.Coach;
import edu.kit.informatik.rollingstock.Engine;
import edu.kit.informatik.rollingstock.RollingStock;
import edu.kit.informatik.rollingstock.TrainSet;
import edu.kit.informatik.userinterface.InOutput;
import edu.kit.informatik.exception.InvalidInputException;

import java.util.List;
import java.util.Map;

/**
 * {@link Command} to delete rolling stock.
 * Call {@link this#setArguments(List)} before calling {@link this#execute()}.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class DeleteRollingStockCommand extends Command {
    private String id;
    private int coachId;

    /**
     * Package private to avoid direct initialisation without using the {@link CommandParser}.
     */
    DeleteRollingStockCommand() {
    }

    @Override
    public void execute() {
        // Check if rolling stock is being used in a train
        for (Train train : register.getTrains().values()) {
            for (RollingStock rollingStock : train.getTrain()) {
                if (id.equals(rollingStock.getId())) {
                    Terminal.printError("rolling stock is being used in a train and therefore cannot be deleted");
                    return;
                }
            }
        }
        Map<Integer, Coach> coaches = register.getCoaches();
        List<Engine> engines = register.getEngines();
        List<TrainSet> trainSets = register.getTrainSets();
        if (id.contains("-")) {
            for (Engine engine : engines) {
                if (engine.getId().equals(id)) {
                    engines.remove(engine);
                    Terminal.printLine(InOutput.OK_MESSAGE);
                    return;
                }
            }
            for (TrainSet trainSet : trainSets) {
                if (trainSet.getId().equals(id)) {
                    trainSets.remove(trainSet);
                    Terminal.printLine(InOutput.OK_MESSAGE);
                    return;
                }
            }
        } else {
            if (coaches.containsKey(coachId)) {
                coaches.remove(coachId);
                Terminal.printLine(InOutput.OK_MESSAGE);
                return;
            }
        }
        Terminal.printError("rolling stock with ID " + id + " not found");
    }

    @Override
    public void setArguments(List<String> arguments) throws InvalidInputException {
        id = arguments.get(0);
        if (!id.contains("-")) {
            try {
                coachId = Integer.parseInt(id.substring(1));
            } catch (NumberFormatException e) {
                throw new InvalidInputException("coach ID must be a 32-bit integer");
            }
        }
    }

    @Override
    public String getName() {
        return "delete rolling stock";
    }

    @Override
    public int getNumberOfArguments() {
        return 1;
    }

    @Override
    public String getCommandDescription() {
        return "delete rolling stock <id>";
    }
}
