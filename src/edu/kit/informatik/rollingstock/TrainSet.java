package edu.kit.informatik.rollingstock;

import edu.kit.informatik.exception.LogicException;

/**
 * Represents a train-set.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class TrainSet extends RollingStock {
    private final String series;
    private final String name;

    /**
     * Creates a new train-set from the given arguments.
     *
     * @param length of the train-set
     * @param couplingFront whether the train-set has a coupling at the front
     * @param couplingBack whether the train-set has a coupling at the back
     * @param series of the train-set
     * @param name of the train-set
     */
    public TrainSet(int length, boolean couplingFront, boolean couplingBack, String series, String name) {
        super(length, couplingFront, couplingBack);
        this.series = series;
        this.name = name;
    }

    /**
     * Gets the id of the train-set which consists of the series and the name.
     *
     * @return the id of the train-set
     */
    @Override
    public String getId() {
        return series + "-" + name;
    }

    @Override
    public String getType() {
        return "train-set";
    }

    @Override
    public final String[] graphic() {
        return new String[]{
            "         ++         ",
            "         ||         ",
            "_________||_________",
            "|  ___ ___ ___ ___ |",
            "|  |_| |_| |_| |_| |",
            "|__________________|",
            "|__________________|",
            "   (O)        (O)   "
        };
    }

    /**
     * Gets the series of the train-set.
     *
     * @return the series of the train-set
     */
    public String getSeries() {
        return series;
    }

    /**
     * Gets the name of the train-set.
     *
     * @return the name of the train-set
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the string representation of a train-set.
     *
     * @return String of the format <b>[series] [name] [length] [couplingFront] [couplingBack]</b>
     */
    @Override
    public String toString() {
        return getSeries() + " " + getName() + " " + getLength() + " " + isCouplingFront() + " " + isCouplingBack();
    }

    /**
     * Returns <code>true</code> if the rolling stock can couple to it.
     * A train-set has a special type of coupling and can therefore
     * only be composed with a train-set that has the same series.
     *
     * @param rollingStock to be coupled to
     * @return <code>true</code> if the rolling stock can couple to it
     * @throws LogicException if a train-set is being coupled to another rolling stock type
     *  or the train-set series do not match
     */
    @Override
    public boolean canCoupleTo(RollingStock rollingStock) throws LogicException {
        if (this.getClass() == rollingStock.getClass()) {
            String rollingStockSeries = rollingStock.getId().split("-")[0];
            if (!this.getSeries().equals(rollingStockSeries)) {
                throw new LogicException("train-set series do not match");
            }
            return true;
        } else {
            throw new LogicException("a train-set can only be composed with a train consisting of train-sets");
        }
    }

    @Override
    public boolean isUseful() {
        return true;
    }
}
