package edu.kit.informatik.rollingstock.engine;

import edu.kit.informatik.rollingstock.Engine;

/**
 * Represents a steam engine.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class SteamEngine extends Engine {

    /**
     * Creates a new steam engine from the given arguments.
     *
     * @param length of the steam engine
     * @param couplingFront whether the steam engine has a coupling at the front
     * @param couplingBack whether the steam engine has a coupling at the back
     * @param series of the steam engine
     * @param name of the steam engine
     */
    public SteamEngine(int length, boolean couplingFront, boolean couplingBack, String series, String name) {
        super(length, couplingFront, couplingBack, series, name);
    }

    @Override
    public String getType() {
        return "steam engine";
    }

    @Override
    public final String[] graphic() {
        return new String[]{
            "     ++      +------",
            "     ||      |+-+ | ",
            "   /---------|| | | ",
            "  + ========  +-+ | ",
            " _|--/~\\------/~\\-+ ",
            "//// \\_/      \\_/   "
        };
    }

    /**
     * Returns the string representation of a steam engine.
     *
     * @return String of the format <b>s [series] [name] [length] [couplingFront] [couplingBack]</b>
     */
    @Override
    public String toString() {
        return "s " + getSeries() + " " + getName() + " " + getLength() + " " + isCouplingFront()
                + " " + isCouplingBack();
    }
}
