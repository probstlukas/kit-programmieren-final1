package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.rollingstock.TrainSet;
import edu.kit.informatik.userinterface.InOutput;
import edu.kit.informatik.exception.InvalidInputException;

import java.util.List;

/**
 * {@link Command} to create a new train-set.
 * Call {@link this#setArguments(List)} before calling {@link this#execute()}.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class CreateTrainSetCommand extends Command {
    private String series;
    private String name;
    private int length;
    private boolean couplingFront;
    private boolean couplingBack;

    /**
     * Package private to avoid direct initialisation without using the {@link CommandParser}.
     */
    CreateTrainSetCommand() {
    }

    @Override
    public void execute() {
        try {
            register.verifyId(series, name);
            TrainSet trainSet = new TrainSet(length, couplingFront, couplingBack, series, name);
            register.createTrainSet(trainSet);
            Terminal.printLine(series + "-" + name);
        } catch (InvalidInputException e) {
            Terminal.printError(e.getMessage());
        }
    }

    @Override
    public void setArguments(List<String> arguments) throws InvalidInputException {
        series = InOutput.parseSeries(arguments.get(0));
        name = InOutput.parseName(arguments.get(1));
        length = InOutput.parseNumber(arguments.get(2), "length");
        couplingFront = InOutput.toBoolean(arguments.get(3));
        couplingBack = InOutput.toBoolean(arguments.get(4));
        InOutput.verifyCoupling(couplingFront, couplingBack);
    }

    @Override
    public String getName() {
        return "create train-set";
    }

    @Override
    public int getNumberOfArguments() {
        return 5;
    }

    @Override
    public String getCommandDescription() {
        return "create train-set <class> <name> <length> <couplingFront> <couplingBack>";
    }
}
