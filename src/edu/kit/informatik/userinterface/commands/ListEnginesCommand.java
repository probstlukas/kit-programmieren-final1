package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.rollingstock.Engine;

import java.util.Comparator;
import java.util.List;

/**
 * {@link Command} to list all engines.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class ListEnginesCommand extends Command {

    /**
     * Package private to avoid direct initialisation without using the {@link CommandParser}.
     */
    ListEnginesCommand() {
    }

    @Override
    public void execute() {
        List<Engine> engines = register.getEngines();
        // Sort list lexicographically
        engines.sort(Comparator.comparing(Engine::getId));
        if (engines.isEmpty()) {
            Terminal.printLine("No engine exists");
            return;
        }
        for (Engine engine : engines) {
            if (register.getTrainWithRollingStock(engine).isPresent()) {
                Terminal.printLine(register.getTrainWithRollingStock(engine).get().getId() + " " + engine.toString());
            } else {
                Terminal.printLine("none " + engine.toString());
            }
        }
    }

    @Override
    public String getName() {
        return "list engines";
    }

    @Override
    public int getNumberOfArguments() {
        return 0;
    }

    @Override
    public String getCommandDescription() {
        return "list engines";
    }
}
