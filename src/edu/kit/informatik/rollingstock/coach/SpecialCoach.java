package edu.kit.informatik.rollingstock.coach;

import edu.kit.informatik.rollingstock.Coach;

/**
 * Represents a special coach.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class SpecialCoach extends Coach {

    /**
     * Creates a new special coach from the given arguments.
     *
     * @param length of the special coach
     * @param couplingFront whether the special coach has a coupling at the front
     * @param couplingBack whether the special coach has a coupling at the back
     * @param coachId of the special coach
     */
    public SpecialCoach(int length, boolean couplingFront, boolean couplingBack, int coachId) {
        super(length, couplingFront, couplingBack, coachId);
    }

    @Override
    public String getType() {
        return "special coach";
    }

    @Override
    public final String[] graphic() {
        return new String[]{
            "               ____",
            "/--------------|  |",
            "\\--------------|  |",
            "  | |          |  |",
            " _|_|__________|  |",
            "|_________________|",
            "   (O)       (O)   "
        };
    }

    /**
     * Returns the string representation of a special coach.
     *
     * @return String of the format <b>s [length] [couplingFront] [couplingBack]</b>
     */
    @Override
    public String toString() {
        return "s " + getLength() + " " + isCouplingFront() + " " + isCouplingBack();
    }
}
