package edu.kit.informatik.railway;

import edu.kit.informatik.util.Point;

/**
 * Saves the position and direction of a {@link Train}.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class Placement {
    private Point position;
    private Point direction;

    /**
     * Creates a new placement from the given arguments.
     *
     * @param position of the train
     * @param direction of the train
     */
    public Placement(Point position, Point direction) {
        this.position = position;
        this.direction = direction;
    }

    /**
     * Moves the {@link Train} by invoking {@link #move(Point)}.
     *
     * @return updated placement after movement
     */
    public Placement move() {
        return move(direction);
    }

    /**
     * Moves the {@link Train} by adding the {@code direction} to its position.
     *
     * @param direction to be added
     * @return new placement of the train
     */
    public Placement move(Point direction) {
        return new Placement(position.add(direction), direction);
    }

    /**
     * Moves the {@link Train} backwards by invoking {@link #moveBackwards(Point)}.
     *
     * @return updated placement after movement
     */
    public Placement moveBackwards() {
        return moveBackwards(direction);
    }

    /**
     * Moves the {@link Train} backwards by adding the negated {@code direction} to its position.
     *
     * @param direction to be negated and added
     * @return new placement of the train
     */
    public Placement moveBackwards(Point direction) {
        return new Placement(position.add(direction.negate()), direction);
    }

    /**
     * Sets the {@code direction} of the {@link Train}.
     *
     * @param direction to be set
     */
    public void setDirection(Point direction) {
        this.direction = direction;
    }

    /**
     * Sets the {@code position} of the {@link Train}.
     *
     * @param position to be set
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * Gets the position of the {@link Train}.
     *
     * @return the position of the train
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Gets the direction of the {@link Train}.
     *
     * @return the direction of the train
     */
    public Point getDirection() {
        return direction;
    }
}