package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.railway.Train;

import java.util.Map;

/**
 * {@link Command} to list all trains.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class ListTrainsCommand extends Command {

    /**
     * Package private to avoid direct initialisation without using the {@link CommandParser}.
     */
    ListTrainsCommand() {
    }

    @Override
    public void execute() {
        Map<Integer, Train> trains = register.getTrains();
        if (trains.isEmpty()) {
            Terminal.printLine("No train exists");
            return;
        }
        for (Train train : trains.values()) {
            Terminal.printLine(train.toString());
        }
    }

    @Override
    public String getName() {
        return "list trains";
    }

    @Override
    public int getNumberOfArguments() {
        return 0;
    }

    @Override
    public String getCommandDescription() {
        return "list trains";
    }
}
