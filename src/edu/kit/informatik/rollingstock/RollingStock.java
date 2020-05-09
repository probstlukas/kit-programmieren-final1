package edu.kit.informatik.rollingstock;

import edu.kit.informatik.exception.LogicException;

/**
 * An abstract rolling stock that has a length, with one coupling at the front and one coupling at the back.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public abstract class RollingStock {
    private final int length;
    private final boolean couplingFront;
    private final boolean couplingBack;

    /**
     * Creates a new rolling stock with the given arguments.
     *
     * @param length of the rolling stock
     * @param couplingFront whether the rolling stock has a coupling at the front
     * @param couplingBack whether the rolling stock has a coupling at the back
     */
    public RollingStock(int length, boolean couplingFront, boolean couplingBack) {
        this.length = length;
        this.couplingFront = couplingFront;
        this.couplingBack = couplingBack;
    }

    /**
     * Gets the length of the rolling stock.
     *
     * @return the length of the rolling stock
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns <code>true</code> if the rolling stock has a coupling at the front.
     *
     * @return <code>true</code> if the coupling at the front is existent
     */
    public boolean isCouplingFront() {
        return couplingFront;
    }

    /**
     * Returns <code>true</code> if the rolling stock has a coupling at the back.
     *
     * @return <code>true</code> if the coupling at the back is existent
     */
    public boolean isCouplingBack() {
        return couplingBack;
    }

    /**
     * Gets the ID of the rolling stock.
     *
     * @return the ID of the rolling stock
     */
    public abstract String getId();

    /**
     * Gets the type of the rolling stock.
     *
     * @return the type of the rolling stock
     */
    public abstract String getType();

    /**
     * Returns the graphical representation of the rolling stock.
     *
     * @return the graphical representation
     */
    public abstract String[] graphic();

    /**
     * Returns <code>true</code> if the rolling stock can couple to it.
     * This is always the case except for {@link TrainSet train-sets}.
     *
     * @param rollingStock to be coupled to
     * @return <code>true</code> if the rolling stock can couple to it
     * @throws LogicException if a train-set is being coupled to another rolling stock type
     */
    public boolean canCoupleTo(RollingStock rollingStock) throws LogicException {
        return true;
    }

    /**
     * Checks if rolling stock can be used at the start or end of a {@link edu.kit.informatik.railway.Train}.
     *
     * @return <code>true</code> if the train is useful at the start or end of a train
     */
    public boolean isUseful() {
        return false;
    }
}
