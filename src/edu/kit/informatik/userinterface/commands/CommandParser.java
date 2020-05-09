package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.exception.InvalidInputException;
import edu.kit.informatik.userinterface.InOutput;

import java.util.Arrays;
import java.util.List;

/**
 * Responsible for initialising all {@link Command commands} and getting the arguments of them.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public final class CommandParser {

    /**
     * Private to avoid direct initialisation.
     */
    private CommandParser() {
    }

    /**
     * Initialises all {@link Command commands}.
     *
     * @return a list with all command classes
     */
    public static List<Command> initialiseCommands() {
        return Arrays.asList(new AddTrackCommand(), new AddSwitchCommand(), new DeleteTrackCommand(),
                new ListTracksCommand(), new SetSwitchCommand(), new CreateEngineCommand(),
                new ListEnginesCommand(), new CreateCoachCommand(), new ListCoachesCommand(),
                new CreateTrainSetCommand(), new ListTrainSetsCommand(), new DeleteRollingStockCommand(),
                new AddTrainCommand(), new DeleteTrainCommand(), new ListTrainsCommand(),
                new ShowTrainCommand(), new PutTrainCommand(), new StepCommand(), new ExitCommand());
    }

    /**
     * Extracts the arguments from the user input.
     *
     * @param input is the user input
     * @param command {@link Command} the user input should contain arguments for
     * @return the arguments for the provided {@code command} as extracted from the user input
     * @throws InvalidInputException if the user input contains a syntax error.
     *  This may include providing the wrong number of arguments for {@code command}
     */
    public static List<String> getArguments(final String input, final Command command)
            throws InvalidInputException {
        int commandCount = command.getName().split(InOutput.COMMAND_SEPARATOR).length;
        final List<String> inputSplit = Arrays.asList(input.split(InOutput.COMMAND_SEPARATOR));
        final List<String> arguments = inputSplit.subList(commandCount, inputSplit.size());
        if (arguments.size() != command.getNumberOfArguments()
                || input.substring(input.length() - 1).equals(InOutput.COMMAND_SEPARATOR)) {
            throw new InvalidInputException("invalid number of arguments. Expected " + command.getNumberOfArguments()
                    + " arguments of the form '" + command.getCommandDescription() + "'");
        }
        return arguments;
    }
}
