package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.exception.LogicException;
import edu.kit.informatik.userinterface.InOutput;
import edu.kit.informatik.exception.InvalidInputException;

import java.util.List;

/**
 * {@link Command} to delete a track.
 * Call {@link this#setArguments(List)} before calling {@link this#execute()}.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class DeleteTrackCommand extends Command {
    private int trackId;

    /**
     * Package private to avoid direct initialisation without using the {@link CommandParser}.
     */
    DeleteTrackCommand() {
    }

    @Override
    public void execute() {
        try {
            register.removeTrack(trackId);
            Terminal.printLine(InOutput.OK_MESSAGE);
        } catch (LogicException e) {
            Terminal.printError(e.getMessage());
        }
    }

    @Override
    public void setArguments(List<String> arguments) throws InvalidInputException {
        trackId = InOutput.parseNumber(arguments.get(0), "track ID");
    }

    @Override
    public String getName() {
        return "delete track";
    }

    @Override
    public int getNumberOfArguments() {
        return 1;
    }

    @Override
    public String getCommandDescription() {
        return "delete track <trackID>";
    }
}
