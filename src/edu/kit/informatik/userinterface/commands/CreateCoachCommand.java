package edu.kit.informatik.userinterface.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.rollingstock.Coach;
import edu.kit.informatik.rollingstock.coach.FreightCoach;
import edu.kit.informatik.rollingstock.coach.PassengerCoach;
import edu.kit.informatik.rollingstock.coach.SpecialCoach;
import edu.kit.informatik.userinterface.InOutput;
import edu.kit.informatik.exception.InvalidInputException;

import java.util.List;

/**
 * {@link Command} to create a new coach.
 * Call {@link this#setArguments(List)} before calling {@link this#execute()}.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class CreateCoachCommand extends Command {
    private String coachType;
    private int length;
    private boolean couplingFront;
    private boolean couplingBack;

    /**
     * Package private to avoid direct initialisation without using the {@link CommandParser}.
     */
    CreateCoachCommand() {
    }

    @Override
    public void execute() {
        int id = register.getNextId(register.getCoaches().keySet());
        try {
            Coach coach;
            switch (coachType) {
                case "passenger": {
                    coach = new PassengerCoach(length, couplingFront, couplingBack, id);
                    break;
                }
                case "freight": {
                    coach = new FreightCoach(length, couplingFront, couplingBack, id);
                    break;
                }
                case "special": {
                    coach = new SpecialCoach(length, couplingFront, couplingBack, id);
                    break;
                }
                default:
                    throw new InvalidInputException("invalid coach type. Either use 'passenger', "
                            + "'freight' or 'special'");
            }
            register.createCoach(coach);
            Terminal.printLine(id);
        } catch (InvalidInputException e) {
            Terminal.printError(e.getMessage());
        }
    }

    @Override
    public void setArguments(List<String> arguments) throws InvalidInputException {
        coachType = arguments.get(0);
        length = InOutput.parseNumber(arguments.get(1), "length");
        couplingFront = InOutput.toBoolean(arguments.get(2));
        couplingBack = InOutput.toBoolean(arguments.get(3));
        InOutput.verifyCoupling(couplingFront, couplingBack);
    }

    @Override
    public String getName() {
        return "create coach";
    }

    @Override
    public int getNumberOfArguments() {
        return 4;
    }

    @Override
    public String getCommandDescription() {
        return "create coach <coachType> <length> <couplingFront> <couplingBack>";
    }
}
