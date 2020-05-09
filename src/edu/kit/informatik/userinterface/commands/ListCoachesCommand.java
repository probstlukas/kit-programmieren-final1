package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.rollingstock.Coach;

import java.util.Map;

/**
 * {@link Command} to list all coaches.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class ListCoachesCommand extends Command {

    /**
     * Package private to avoid direct initialisation without using the {@link CommandParser}.
     */
    ListCoachesCommand() {
    }

    @Override
    public void execute() {
        Map<Integer, Coach> coaches = register.getCoaches();
        if (coaches.isEmpty()) {
            Terminal.printLine("No coach exists");
            return;
        }
        for (Coach coach : coaches.values()) {
            if (register.getTrainWithRollingStock(coach).isPresent()) {
                Terminal.printLine(coach.getCoachId() + " "
                        + register.getTrainWithRollingStock(coach).get().getId() + " " + coach.toString());
            } else {
                Terminal.printLine(coach.getCoachId() + " none " + coach.toString());
            }
        }
    }

    @Override
    public String getName() {
        return "list coaches";
    }

    @Override
    public int getNumberOfArguments() {
        return 0;
    }

    @Override
    public String getCommandDescription() {
        return "list coaches";
    }
}
