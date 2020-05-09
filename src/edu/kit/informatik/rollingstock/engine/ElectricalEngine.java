package edu.kit.informatik.rollingstock.engine;

import edu.kit.informatik.rollingstock.Engine;

/**
 * Represents an electrical engine.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class ElectricalEngine extends Engine {

    /**
     * Creates a new electrical engine from the given arguments.
     *
     * @param length of the electrical engine
     * @param couplingFront whether the electrical engine has a coupling at the front
     * @param couplingBack whether the electrical engine has a coupling at the back
     * @param series of the electrical engine
     * @param name of the electrical engine
     */
    public ElectricalEngine(int length, boolean couplingFront, boolean couplingBack, String series, String name) {
        super(length, couplingFront, couplingBack, series, name);
    }

    @Override
    public String getType() {
        return "electrical engine";
    }

    @Override
    public final String[] graphic() {
        return new String[]{
            "               ___    ",
            "                 \\    ",
            "  _______________/__  ",
            " /_| ____________ |_\\ ",
            "/   |____________|   \\",
            "\\                    /",
            " \\__________________/ ",
            "  (O)(O)      (O)(O)  "
        };
    }

    /**
     * Returns the string representation of a electrical engine.
     *
     * @return String of the format <b>e [series] [name] [length] [couplingFront] [couplingBack]</b>
     */
    @Override
    public String toString() {
        return "e " + getSeries() + " " + getName() + " " + getLength() + " " + isCouplingFront()
                + " " + isCouplingBack();
    }
}
