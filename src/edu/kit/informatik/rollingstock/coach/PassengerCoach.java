package edu.kit.informatik.rollingstock.coach;

import edu.kit.informatik.rollingstock.Coach;

/**
 * Represent a passenger coach.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class PassengerCoach extends Coach {

    /**
     * Creates a new passenger coach from the given arguments.
     *
     * @param length of the passenger coach
     * @param couplingFront whether the passenger coach has a coupling at the front
     * @param couplingBack whether the passenger coach has a coupling at the back
     * @param coachId of the passenger coach
     */
    public PassengerCoach(int length, boolean couplingFront, boolean couplingBack, int coachId) {
        super(length, couplingFront, couplingBack, coachId);
    }

    @Override
    public String getType() {
        return "passenger coach";
    }

    @Override
    public final String[] graphic() {
        return new String[]{
            "____________________",
            "|  ___ ___ ___ ___ |",
            "|  |_| |_| |_| |_| |",
            "|__________________|",
            "|__________________|",
            "   (O)        (O)   "
        };
    }

    /**
     * Returns the string representation of a passenger coach.
     *
     * @return String of the format <b>p [length] [couplingFront] [couplingBack]</b>
     */
    @Override
    public String toString() {
        return "p " + getLength() + " " + isCouplingFront() + " " + isCouplingBack();
    }
}
