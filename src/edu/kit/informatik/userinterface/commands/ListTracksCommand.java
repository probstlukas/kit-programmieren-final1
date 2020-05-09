package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.railway.Track;

import java.util.Map;

/**
 * {@link Command} to list all tracks.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class ListTracksCommand extends Command {

    /**
     * Package private to avoid direct initialisation without using the {@link CommandParser}.
     */
    ListTracksCommand() {
    }

    @Override
    public void execute() {
        Map<Integer, Track> tracks = register.getNetwork().getTracks();
        if (tracks.isEmpty()) {
            Terminal.printLine("No track exists");
            return;
        }
        for (Track track : tracks.values()) {
            Terminal.printLine(track.toString());
        }
    }

    @Override
    public String getName() {
        return "list tracks";
    }

    @Override
    public int getNumberOfArguments() {
        return 0;
    }

    @Override
    public String getCommandDescription() {
        return "list tracks";
    }
}
