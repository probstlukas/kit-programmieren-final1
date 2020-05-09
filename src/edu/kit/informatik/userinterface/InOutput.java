package edu.kit.informatik.userinterface;

import edu.kit.informatik.exception.InvalidInputException;
import edu.kit.informatik.util.Point;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class containing methods to parse input and also contains constants for the output.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public final class InOutput {
    /**
     * The output to use for communicating successful execution.
     */
    public static final String OK_MESSAGE = "OK";
    /**
     * Separates start- and endpoint(s) for the input.
     */
    public static final String ARROW_SEPARATOR = "->";
    /**
     * Separates the command from its potential arguments.
     */
    public static final String COMMAND_SEPARATOR = " ";
    // Precompile the patterns
    /**
     * Pattern for a point: (<x-coordinate>,<y-coordinate>).
     */
    private static final Pattern POINT_PATTERN = Pattern.compile("\\(([+-]?[0-9]+),([+-]?[0-9]+)\\)");
    /**
     * Pattern for two points: (<x-coordinate>,<y-coordinate>),(<x-coordinate>,<y-coordinate>).
     */
    private static final Pattern TWO_POINTS_PATTERN = Pattern.compile(POINT_PATTERN + "," + POINT_PATTERN);
    /**
     * Pattern for a direction vector: <x-coordinate>,<y-coordinate>.
     */
    private static final Pattern DIRECTION_PATTERN = Pattern.compile("([+-]?[0-9]+),([+-]??[0-9]+)");
    /**
     * Pattern for a boolean which can be either true or false.
     */
    private static final Pattern BOOLEAN_PATTERN = Pattern.compile("true|false");
    /**
     * Pattern for name and series which only allows characters from all unicode letter categories.
     */
    private static final Pattern NAME_SERIES_PATTERN = Pattern.compile("[\\p{L}0-9]+");
    /**
     * Pattern for a natural number excluding zero.
     */
    private static final Pattern NUMBER_PATTERN = Pattern.compile("[+]?[0-9]*[1-9][0-9]*");

    /**
     * This helper class is not meant to be instantiated.
     */
    private InOutput() {
    }

    /**
     * Parses a {@link Point}.
     *
     * @param input to be parsed
     * @return new {@link Point} with the given components
     * @throws InvalidInputException if the input format is incorrect or the coordinates are not 32-bit integers
     */
    public static Point parsePoint(final String input) throws InvalidInputException {
        final Matcher matcher = POINT_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new InvalidInputException("a point must be entered as follows '(<x-coordinate>,<y-coordinate>)'");
        }
        try {
            return new Point(Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)));
        } catch (NumberFormatException e) {
            throw new InvalidInputException("coordinates of a point must be 32-bit integers");
        }
    }

    /**
     * Parses two {@link Point points}.
     *
     * @param input to be parsed
     * @return a list with two points in it
     * @throws InvalidInputException if the input format is incorrect or the coordinates are not 32-bit integers
     */
    public static List<Point> parseTwoPoints(final String input) throws InvalidInputException {
        final Matcher matcher = TWO_POINTS_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new InvalidInputException("two endpoints must be entered as follows "
                    + "'(<x-coordinate>,<y-coordinate>),(<x-coordinate>,<y-coordinate>)'");
        }
        try {
            Point firstPoint = new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            Point secondPoint = new Point(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
            return Arrays.asList(firstPoint, secondPoint);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("coordinates of a point must be 32-bit integers");
        }
    }

    /**
     * Parses a <code>boolean</code>.
     *
     * @param input to be parsed
     * @return a boolean
     * @throws InvalidInputException if the input format is incorrect
     */
    public static boolean toBoolean(final String input) throws InvalidInputException {
        final Matcher matcher = BOOLEAN_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new InvalidInputException(input + " is not a valid boolean. Either use 'true' or 'false'");
        }
        return Boolean.parseBoolean(input);
    }

    /**
     * Parses a direction vector.
     *
     * @param input to be parsed
     * @return array of type <code>int[]</code> with the coordinates of the given vector
     * @throws InvalidInputException if the input format is incorrect or the coordinates are not 32-bit integers
     */
    public static Point parseVector(final String input) throws InvalidInputException {
        final Matcher matcher = DIRECTION_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new InvalidInputException(input + " is not a valid direction vector. Expect input of the form "
                    + "'<x-coordinate>,<y-coordinate>'");
        }
        String[] inputSplit = input.split(",");
        try {
            return new Point(Integer.parseInt(inputSplit[0]), Integer.parseInt(inputSplit[1]));
        } catch (NumberFormatException e) {
            throw new InvalidInputException("coordinates of the direction vector must be 32-bit integers");
        }
    }

    /**
     * Parses a positive natural number excluding zero.
     *
     * @param input to be parsed
     * @param description of the input argument
     * @return a natural number excluding zero
     * @throws InvalidInputException if the input format is incorrect or the number is not a 32-bit integer
     */
    public static int parseNumber(final String input, final String description) throws InvalidInputException {
        final Matcher matcher = NUMBER_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new InvalidInputException(description + " must be a natural number excluding zero");
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new InvalidInputException(description + " must be a 32-bit integer");
        }
    }

    /**
     * Parses a series.
     *
     * @param input to be parsed
     * @return a valid series
     * @throws InvalidInputException if the input format is incorrect or the input only consists of the character "W"
     */
    public static String parseSeries(final String input) throws InvalidInputException {
        final Matcher matcher = NAME_SERIES_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new InvalidInputException("the series must consist of letters and numbers");
        }
        if (input.equals("W")) {
            throw new InvalidInputException("the series must not consist of the character string 'W'");
        }
        return input;
    }

    /**
     * Parses a name.
     *
     * @param input to be parsed
     * @return a valid name
     * @throws InvalidInputException if the input format is incorrect
     */
    public static String parseName(final String input) throws InvalidInputException {
        final Matcher matcher = NAME_SERIES_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new InvalidInputException("the name must consist of letters and numbers");
        }
        return input;
    }

    /**
     * Parses and verifies a coupling.
     *
     * @param couplingFront the coupling at the front of the rolling stock
     * @param couplingBack the coupling at the back of the rolling stock
     * @throws InvalidInputException if the coupling is not valid
     */
    public static void verifyCoupling(final boolean couplingFront, final boolean couplingBack)
            throws InvalidInputException {
        if (!(couplingFront || couplingBack)) {
            throw new InvalidInputException("there must be at least one but not more than two couplings");
        }
    }
}