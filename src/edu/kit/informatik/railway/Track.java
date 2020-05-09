package edu.kit.informatik.railway;

import edu.kit.informatik.exception.LogicException;
import edu.kit.informatik.util.Point;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a track, which can either be a normal track with one endpoint or a switch track with two endpoints.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class Track {
    /**
     * Maximum connections that a {@link Track} can have.
     */
    public static final int MAX_CONNECTIONS = 2;
    private final int id;
    private final List<Point> points;
    private Point switchedTo;
    private Train currentTrain;

    /**
     * Creates a track from the given arguments.
     *
     * @param id the track's id, uniquely identifying it within one {@linkplain Register}
     * @param points the start and endpoint(s) of the track
     */
    public Track(int id, List<Point> points) {
        this.id = id;
        // Normal track
        if (points.size() == 2) {
            this.switchedTo = points.get(1);
        }
        this.points = points;
    }

    /**
     * Returns <code>true</code> if this track is a switch.
     *
     * @param track to be checked
     * @return <code>true</code> if this track is a switch
     */
    public boolean isSwitch(Track track) {
        return track.getEndPoints().size() == 2;
    }

    /**
     * Changes the position of the switch.
     *
     * @param point endpoint of a switch
     * @throws LogicException if the given point is not an endpoint
     */
    public void setSwitchedTo(Point point) throws LogicException {
        int pointIndex = points.indexOf(point);
        if (pointIndex < 1) {
            throw new LogicException("must be an endpoint");
        }
        switchedTo = this.points.get(pointIndex);
    }

    /**
     * Gets the ID of the track.
     *
     * @return the ID of the track
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the {@link Point points} of the track.
     *
     * @return the points of the track
     */
    public List<Point> getPoints() {
        return points;
    }

    /**
     * Gets the startpoint of the track.
     *
     * @return the startpoint of the track
     */
    public Point getStartPoint() {
        return points.get(0);
    }

    /**
     * Gets the endpoints of the track.
     *
     * @return the endpoints of the track
     */
    public List<Point> getEndPoints() {
        return Collections.unmodifiableList(points.subList(1, getPoints().size()));
    }

    /**
     * Gets the current switch switch position.
     *
     * @return the switch position
     */
    public Point getSwitchedTo() {
        return switchedTo;
    }

    /**
     * Returns <code>true</code> if the track on which the point is located on is passable.
     *
     * @param point to be checked
     * @return <code>true</code> if the track is passable
     */
    public boolean isPassable(Point point) {
        if (switchedTo == null || getStartPoint() == null) {
            return false;
        }
        Point startPoint = getStartPoint();
        if (startPoint.equals(point) || switchedTo.equals(point)) {
            return true;
        }
        int startPointFirstComp = (int) startPoint.getFirstComponent();
        int startPointSecComp = (int) startPoint.getSecondComponent();
        int pointFirstComp = (int) point.getFirstComponent();
        int pointSecComp = (int) point.getSecondComponent();
        int switchedToFirstComp = (int) switchedTo.getFirstComponent();
        int switchedToSecComp = (int) switchedTo.getSecondComponent();
        return startPointFirstComp == pointFirstComp
                && pointFirstComp == switchedToFirstComp
                && Math.min(startPointSecComp, switchedToSecComp) <= pointSecComp
                && pointSecComp <= Math.max(startPointSecComp, switchedToSecComp)
                || startPointSecComp == pointSecComp
                && pointSecComp == switchedToSecComp
                && Math.min(startPointFirstComp, switchedToFirstComp) <= pointFirstComp
                && pointFirstComp <= Math.max(startPointFirstComp, switchedToFirstComp);
    }

    /**
     * Gets the current train that is located on the track.
     *
     * @return the current train
     */
    public Train getCurrentTrain() {
        return currentTrain;
    }

    /**
     * Sets a train on the track.
     *
     * @param currentTrain to set on the track
     */
    public void setCurrentTrain(Train currentTrain) {
        this.currentTrain = currentTrain;
    }

    /**
     * Gets the (normalised) driving direction to <code>switchedTo</code>.
     *
     * @param point to be checked
     * @return the driving direction
     */
    public Point getDrivingDirection(Point point) {
        Point direction = getStartPoint().vectorTo(switchedTo).normalise();
        return point.equals(switchedTo) ? direction : direction.negate();
    }

    /**
     * Gets the passed point of the track considering the direction in which the train is driving.
     *
     * @param direction of the train
     * @return the passed point
     * @throws LogicException if the directions do not match
     */
    public Point getPassedPoint(Point direction) throws LogicException {
        int firstCompDir = (int) switchedTo.getFirstComponent() - (int) getStartPoint().getFirstComponent();
        int secondCompDir = (int) switchedTo.getSecondComponent() - (int) getStartPoint().getSecondComponent();
        Point vector = new Point(firstCompDir, secondCompDir).normalise();
        if (vector.getFirstComponent() != direction.getFirstComponent()
                && vector.getSecondComponent() != direction.getSecondComponent()) {
            throw new LogicException("directions do not match");
        }
        return vector.equals(direction) ? getStartPoint() : switchedTo;
    }

    /**
     * Returns a String representation of the track. Preceded by an "s" if it is a switch and "t" if it is
     * a normal track. The length will only be shown if switchedTo is set.
     *
     * @return String of the format <b>[t] [trackID] [startpoint] -> [endpoint] [length]</b> or
     *  <b>[s] [trackID] [startpoint] -> [endpoint1],[endpoint2] [length]</b>
     */
    @Override
    public String toString() {
        String endPoints = getEndPoints().stream().map(String::valueOf).collect(Collectors.joining(","));
        String length = switchedTo == null ? "" : " " + getStartPoint().distanceTo(switchedTo);
        return (isSwitch(this) ? "s " : "t ") + getId() + " " + getStartPoint() + " -> " + endPoints + length;
    }
}