package edu.kit.informatik.rollingstock;

/**
 * An abstract coach that inherits all the attributes of the {@link RollingStock} and also has an ID.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public abstract class Coach extends RollingStock {
    private final int coachId;

    /**
     * Creates a new coach from the given arguments.
     *
     * @param length of the coach
     * @param couplingFront whether the coach has a coupling at the front
     * @param couplingBack whether the coach has a coupling at the back
     * @param coachId of the coach
     */
    public Coach(int length, boolean couplingFront, boolean couplingBack, int coachId) {
        super(length, couplingFront, couplingBack);
        this.coachId = coachId;
    }

    /**
     * Gets the textual representation of the coach ID which prepends a "W" in front of {@link #coachId}.
     *
     * @return the textual representation of the coach ID
     */
    @Override
    public String getId() {
        return "W" + coachId;
    }

    @Override
    public String getType() {
        return "coach";
    }

    /**
     * Gets the ID of the coach.
     *
     * @return the ID of the coach
     */
    public int getCoachId() {
        return coachId;
    }
}
