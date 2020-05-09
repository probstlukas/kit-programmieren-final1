package edu.kit.informatik.rollingstock;

/**
 * An abstract engine that inherits all the attributes of the {@link RollingStock} and also has a series and a name.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public abstract class Engine extends RollingStock {
    private final String series;
    private final String name;

    /**
     * Creates a new engine from the given arguments.
     *
     * @param length of the engine
     * @param couplingFront whether the engine has a coupling at the front
     * @param couplingBack whether the engine has a coupling at the back
     * @param series of the engine
     * @param name of the engine
     */
    public Engine(int length, boolean couplingFront, boolean couplingBack, String series, String name) {
        super(length, couplingFront, couplingBack);
        this.series = series;
        this.name = name;
    }

    /**
     * Gets the ID of the engine which consists of the series and the name.
     *
     * @return the ID of the engine
     */
    @Override
    public String getId() {
        return series + "-" + name;
    }

    @Override
    public String getType() {
        return "engine";
    }

    /**
     * Gets the series of the engine.
     *
     * @return the series of the engine
     */
    public String getSeries() {
        return series;
    }

    /**
     * Gets the name of the engine.
     *
     * @return name of the engine
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean isUseful() {
        return true;
    }
}
