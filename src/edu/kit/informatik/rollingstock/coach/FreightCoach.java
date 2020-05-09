package edu.kit.informatik.rollingstock.coach;

import edu.kit.informatik.rollingstock.Coach;

/**
 * Represent a freight coach.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class FreightCoach extends Coach {

    /**
     * Creates a new freight coach from the given arguments.
     *
     * @param length of the freight coach
     * @param couplingFront whether the freight coach has a coupling at the front
     * @param couplingBack whether the freight coach has a coupling at the back
     * @param coachId of the freight coach
     */
    public FreightCoach(int length, boolean couplingFront, boolean couplingBack, int coachId) {
        super(length, couplingFront, couplingBack, coachId);
    }

    @Override
    public String getType() {
        return "freight coach";
    }

    @Override
    public final String[] graphic() {
        return new String[]{
            "|                  |",
            "|                  |",
            "|                  |",
            "|__________________|",
            "   (O)        (O)   "
        };
    }

    /**
     * Returns the string representation of a freight coach.
     *
     * @return String of the format <b>f [length] [couplingFront] [couplingBack]</b>
     */
    @Override
    public String toString() {
        return "f " + getLength() + " " + isCouplingFront() + " " + isCouplingBack();
    }
}
