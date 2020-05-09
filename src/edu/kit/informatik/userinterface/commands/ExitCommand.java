package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.userinterface.Session;

/**
 * {@link Command} to terminate the session.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class ExitCommand extends Command {
    private Session session;

    /**
     * Package private to avoid direct initialisation without using the {@link CommandParser}.
     */
    ExitCommand() {
    }

    @Override
    public void execute() {
        assert (session != null);
        session.terminate();
    }

    // At this point a command really needs the session
    @Override
    public void setSession(final Session session) {
        this.session = session;
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public int getNumberOfArguments() {
        return 0;
    }

    @Override
    public String getCommandDescription() {
        return "exit";
    }
}
