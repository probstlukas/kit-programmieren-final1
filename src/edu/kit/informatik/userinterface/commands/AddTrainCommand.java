package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.exception.LogicException;
import edu.kit.informatik.rollingstock.RollingStock;
import edu.kit.informatik.exception.InvalidInputException;

import java.util.List;

/**
 * {@link Command} to add a new train.
 * Call {@link this#setArguments(List)} before calling {@link this#execute()}.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class AddTrainCommand extends Command {
    private int trainId;
    private String rollingStockId;

    /**
     * Package private to avoid direct initialisation without using the {@link CommandParser}.
     */
    AddTrainCommand() {
    }

    @Override
    public void execute() {
        try {
            RollingStock rollingStock = register.getRollingStock(rollingStockId);
            register.addTrain(trainId, rollingStock);
            Terminal.printLine(rollingStock.getType() + " " + rollingStockId
                    + " added to train " + trainId);
        } catch (LogicException | InvalidInputException e) {
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
        rollingStockId = arguments.get(1);
    }

    @Override
    public String getName() {
        return "add train";
    }

    @Override
    public int getNumberOfArguments() {
        return 2;
    }

    @Override
    public String getCommandDescription() {
        return "add train <trainID> <rollingStockID>";
    }
}
