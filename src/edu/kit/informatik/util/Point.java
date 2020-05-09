package edu.kit.informatik.util;

import java.util.Objects;

/**
 * A Point in which both values are of the type long.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class Point {
    private final long firstComponent;
    private final long secondComponent;

    /**
     * Constructs and initialises a point at the specified
     * {@code (firstComponent,secondComponent)} location in the coordinate space.
     *
     * @param firstComponent the X coordinate of the newly constructed point
     * @param secondComponent the Y coordinate of the newly constructed point
     */
    public Point(long firstComponent, long secondComponent) {
        this.firstComponent = firstComponent;
        this.secondComponent = secondComponent;
    }

    /**
     * Euclidean distance between this point and that point.
     *
     * @param that point to be compared
     * @return distance from one point to another
     */
    public long distanceTo(Point that) {
        long distanceFirstComponent = Math.abs(this.firstComponent - that.firstComponent);
        long distanceSecondComponent = Math.abs(this.secondComponent - that.secondComponent);
        return (long) Math.hypot(distanceFirstComponent, distanceSecondComponent);
    }

    /**
     * Calculates the vector to {@code point}.
     *
     * @param point to get the vector to
     * @return vector to {@code point}
     */
    public Point vectorTo(Point point) {
        return new Point(point.firstComponent - firstComponent,
                point.secondComponent - secondComponent);
    }

    /**
     * Negates a point.
     *
     * @return the negated point
     */
    public Point negate() {
        return new Point(-firstComponent, -secondComponent);
    }

    /**
     * Normalizes a point (vector).
     *
     * @return normalized vector
     */
    public Point normalise() {
        return new Point((int) Math.signum(firstComponent), (int) Math.signum(secondComponent));
    }

    /**
     * Adds this first component to {@code that} first component and
     * this second component to {@code that} second component.
     *
     * @param that point to be added
     * @return sum of both points
     */
    public Point add(Point that) {
        return new Point(firstComponent + that.firstComponent,
                secondComponent + that.secondComponent);
    }

    /**
     * Determines whether or not two Points are equal. Two instances of
     * Point are equal if the values of their x and y component are the same.
     *
     * @param toCheck an object of this class
     * @return true if the object to be compared is an instance of Point and
     *  has the same values; false otherwise
     */
    @Override
    public boolean equals(final Object toCheck) {
        if (this == toCheck) {
            return true;
        }
        if (toCheck == null || getClass() != toCheck.getClass()) {
            return false;
        }
        Point other = (Point) toCheck;
        return firstComponent == other.firstComponent && secondComponent == other.secondComponent;
    }

    /**
     * Gets the first component of the Point.
     *
     * @return the X coordinate of the Point
     */
    public long getFirstComponent() {
        return firstComponent;
    }

    /**
     * Gets the second component of the Point.
     *
     * @return the Y coordinate of the Point
     */
    public long getSecondComponent() {
        return secondComponent;
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(firstComponent, secondComponent);
    }

    /**
     * Returns the string representation of a Point.
     *
     * @return String of the format (firstComponent,secondComponent)
     */
    @Override
    public String toString() {
        return "(" + firstComponent + "," + secondComponent + ")";
    }
}
