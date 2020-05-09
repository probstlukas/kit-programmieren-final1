package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.rollingstock.TrainSet;

import java.util.Comparator;
import java.util.List;

/**
 * {@link Command} to list all train-sets.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class ListTrainSetsCommand extends Command {

    /**
     * Package private to avoid direct initialisation without using the {@link CommandParser}.
     */
    ListTrainSetsCommand() {
    }

    @Override
    public void execute() {
        List<TrainSet> trainSets = register.getTrainSets();
        // Sort list lexicographically
        trainSets.sort(Comparator.comparing(TrainSet::getId));
        if (trainSets.isEmpty()) {
            Terminal.printLine("No train-set exists");
            return;
        }
        for (TrainSet trainSet : trainSets) {
            if (register.getTrainWithRollingStock(trainSet).isPresent()) {
                Terminal.printLine(register.getTrainWithRollingStock(trainSet).get().getId()
                        + " " + trainSet.toString());
            } else {
                Terminal.printLine("none " + trainSet.toString());
            }
        }
    }

    @Override
    public String getName() {
        return "list train-sets";
    }

    @Override
    public int getNumberOfArguments() {
        return 0;
    }

    @Override
    public String getCommandDescription() {
        return "list train-sets";
    }
}
