package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.rollingstock.Engine;
import edu.kit.informatik.rollingstock.engine.DieselEngine;
import edu.kit.informatik.rollingstock.engine.ElectricalEngine;
import edu.kit.informatik.rollingstock.engine.SteamEngine;
import edu.kit.informatik.userinterface.InOutput;
import edu.kit.informatik.exception.InvalidInputException;

import java.util.List;

/**
 * {@link Command} to create a new engine.
 * Call {@link this#setArguments(List)} before calling {@link this#execute()}.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class CreateEngineCommand extends Command {
    private String engineType;
    private String series;
    private String name;
    private int length;
    private boolean couplingFront;
    private boolean couplingBack;

    /**
     * Package private to avoid direct initialisation without using the {@link CommandParser}.
     */
    CreateEngineCommand() {
    }

    @Override
    public void execute() {
        try {
            register.verifyId(series, name);
            Engine engine;
            switch (engineType) {
                case "electrical": {
                    engine = new ElectricalEngine(length, couplingFront, couplingBack, series, name);
                    break;
                }
                case "steam": {
                    engine = new SteamEngine(length, couplingFront, couplingBack, series, name);
                    break;
                }
                case "diesel": {
                    engine = new DieselEngine(length, couplingFront, couplingBack, series, name);
                    break;
                }
                default:
                    throw new InvalidInputException("invalid engine type. Either use 'electrical', "
                            + "'steam' or 'diesel'");
            }
            register.createEngine(engine);
            Terminal.printLine(series + "-" + name);
        } catch (InvalidInputException e) {
            Terminal.printError(e.getMessage());
        }
    }

    @Override
    public void setArguments(List<String> arguments) throws InvalidInputException {
        engineType = arguments.get(0);
        series = InOutput.parseSeries(arguments.get(1));
        name = InOutput.parseName(arguments.get(2));
        length = InOutput.parseNumber(arguments.get(3), "length");
        couplingFront = InOutput.toBoolean(arguments.get(4));
        couplingBack = InOutput.toBoolean(arguments.get(5));
        InOutput.verifyCoupling(couplingFront, couplingBack);
    }

    @Override
    public String getName() {
        return "create engine";
    }

    @Override
    public int getNumberOfArguments() {
        return 6;
    }

    @Override
    public String getCommandDescription() {
        return "create engine <engineType> <class> <name> <length> <couplingFront> <couplingBack>";
    }
}
