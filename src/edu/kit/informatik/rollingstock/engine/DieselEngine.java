package edu.kit.informatik.rollingstock.engine;

import edu.kit.informatik.rollingstock.Engine;

/**
 * Represents a diesel engine.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class DieselEngine extends Engine {

    /**
     * Creates a new diesel engine from the given arguments.
     *
     * @param length of the diesel engine
     * @param couplingFront whether the diesel engine has a coupling at the front
     * @param couplingBack whether the diesel engine has a coupling at the back
     * @param series of the diesel engine
     * @param name of the diesel engine
     */
    public DieselEngine(int length, boolean couplingFront, boolean couplingBack, String series, String name) {
        super(length, couplingFront, couplingBack, series, name);
    }

    @Override
    public String getType() {
        return "diesel engine";
    }

    @Override
    public final String[] graphic() {
        return new String[] {
            "  _____________|____  ",
            " /_| ____________ |_\\ ",
            "/   |____________|   \\",
            "\\                    /",
            " \\__________________/ ",
            "  (O)(O)      (O)(O)  "
        };
    }

    /**
     * Returns the string representation of a diesel engine.
     *
     * @return String of the format <b>d [series] [name] [length] [couplingFront] [couplingBack]</b>
     */
    @Override
    public String toString() {
        return "d " + getSeries() + " " + getName() + " " + getLength() + " " + isCouplingFront()
                + " " + isCouplingBack();
    }
}
