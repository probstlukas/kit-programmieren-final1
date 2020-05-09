package edu.kit.informatik.railway;

import edu.kit.informatik.exception.LogicException;
import edu.kit.informatik.util.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Represents a rail network which has edges that represent tracks.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class RailNetwork {
    /**
     * Stores all tracks that start or end at a given {@link Point}.
     */
    private final Map<Point, List<Track>> edges;
    private final Map<Integer, Track> tracks;

    /**
     * Creates a new rail network which initialises the required collections.
     */
    public RailNetwork() {
        this.edges = new HashMap<>();
        this.tracks = new TreeMap<>();
    }

    /**
     * Gets the tracks.
     *
     * @return the tracks
     */
    public Map<Integer, Track> getTracks() {
        return tracks;
    }

    /**
     * Returns <code>true</code> if the given track is horizontal or vertical.
     *
     * @param points of the track
     * @return <code>true</code> if the track is horizontal or vertical
     */
    private boolean isHorizontalOrVertical(List<Point> points) {
        if (points == null || points.size() < 2) {
            throw new IllegalArgumentException("invalid number of points");
        }
        Point startPoint = points.get(0);
        Point firstEndPoint = points.get(1);
        if (points.size() == 2) {
            return startPoint.getFirstComponent() == firstEndPoint.getFirstComponent()
                    || startPoint.getSecondComponent() == firstEndPoint.getSecondComponent();
        }
        return points.stream().allMatch(point -> isHorizontalOrVertical(Arrays.asList(startPoint, point)));
    }

    /**
     * Returns <code>true</code> if the point has any connections.
     *
     * @param point to be checked
     * @return <code>true</code> if the point has any connections
     */
    private boolean hasConnections(Point point) {
        return !edges.computeIfAbsent(point, x -> new ArrayList<>()).isEmpty();
    }

    /**
     * Adds a new track to the {@link RailNetwork}.
     *
     * @param track to be added
     * @throws LogicException if the {@code track} is not valid or
     *  it doesn't fit in the current rail network.
     */
    public void addTrack(Track track) throws LogicException {
        if (track.getEndPoints().stream().anyMatch(point -> point.equals(track.getStartPoint()))) {
            throw new LogicException("startpoint cannot be equal to an endpoint");
        }
        // Check if start- or endpoint of the track to be added is connected with a start- or endpoint of another track
        if (!edges.isEmpty() && !hasConnections(track.getStartPoint())
                && track.getEndPoints().stream().noneMatch(this::hasConnections)) {
            throw new LogicException("track not connected to other track");
        }
        if (!isHorizontalOrVertical(track.getPoints())) {
            throw new LogicException("creation not possible wrong position");
        }
        // Only one other track (normal track or track switch) can be connected at any one point on a track
        if (track.getPoints().stream()
                .map(point -> edges.computeIfAbsent(point, x -> new ArrayList<>()))
                .anyMatch(list -> list.size() >= Track.MAX_CONNECTIONS)) {
            throw new LogicException("each point must not be connected to more than two tracks");
        }
        edges.computeIfAbsent(track.getStartPoint(), x -> new ArrayList<>()).add(track);
        track.getEndPoints().forEach(point -> edges.computeIfAbsent(point, x -> new ArrayList<>()).add(track));
        this.tracks.put(track.getId(), track);
    }

    /**
     * Returns <code>true</code> if the {@code train} touches the {@code track}.
     *
     * @param train to be checked
     * @param track to be checked
     * @return <code>true</code> if the train touches the track
     * @throws LogicException if the directions of the track and train do not match
     */
    public boolean touchesTrack(Train train, Track track) throws LogicException {
        List<Point> points = track.getPoints();
        Point position = train.getPosition();
        if (points.contains(position)) {
            return true;
        }
        Track currentTrack = findTrack(position, train.getDirection()).get();
        long length = train.getLength();
        Point direction = train.getDirection();
        while (length > 0) {
            if (points.contains(position)) {
                return true;
            }
            Point passed = currentTrack.getPassedPoint(direction);
            length -= position.distanceTo(passed);
            if (length == 0 && points.contains(passed)) {
                return true;
            }
            position = passed;
            if (!getConnection(passed, currentTrack).isPresent()) {
                return false;
            }
            currentTrack = getConnection(passed, currentTrack).get();
            direction = currentTrack.getDrivingDirection(passed);
        }
        return false;
    }

    /**
     * Removes the track from the {@link RailNetwork}.
     *
     * @param trackId of the track to be removed
     * @throws LogicException if there is a complication with the removal
     */
    public void removeTrack(int trackId) throws LogicException {
        checkTrackId(trackId);
        Track track = tracks.get(trackId);
        if (track.getCurrentTrain() != null) {
            throw new LogicException("there is currently train with ID " + track.getCurrentTrain().getId()
                    + " on this track");
        }
        if (isBridge(track)) {
            throw new LogicException("removal of track with ID " + trackId + " not possible. "
                    + "This would lead to a disconnected rail network");
        }
        tracks.remove(trackId);
        track.getPoints().stream().filter(edges::containsKey).forEach(point -> edges.get(point).remove(track));
        // Clear all keys of the map if all values have been removed
        edges.entrySet().removeIf(edge -> edge.getValue().isEmpty());
    }

    /**
     * Sets the position of the switch.
     *
     * @param trackId of the track
     * @param point to switch to
     * @throws LogicException if the track is not a switch
     */
    public void setSwitch(int trackId, Point point) throws LogicException {
        checkTrackId(trackId);
        Track track = tracks.get(trackId);
        if (!track.isSwitch(track)) {
            throw new LogicException("track with ID " + trackId + " is not a switch");
        }
        track.setSwitchedTo(point);
    }

    /**
     * Checks if the given track ID is existent.
     *
     * @param trackId to be checked
     * @throws LogicException if it doesn't exist
     */
    private void checkTrackId(int trackId) throws LogicException {
        if (!tracks.containsKey(trackId)) {
            throw new LogicException("track with ID " + trackId + " not existent");
        }
    }

    /**
     * Gets all tracks that start or end at the given {@code point}.
     *
     * @param point to get all track connections of
     * @return all tracks that or end at the given {@code point}
     */
    public List<Track> getTrackConnections(Point point) {
        return edges.get(point);
    }

    /**
     * Return <code>true</code> if the given track {@code toRemove} is a bridge in the {@link RailNetwork}.
     *
     * @param toRemove the track that is checked whether it is a bridge or not
     * @return <code>true</code> if the track is a bridge
     */
    private boolean isBridge(Track toRemove) {
        Set<Point> notVisited = edges.entrySet().stream()
                .filter(edge -> edge.getValue().stream().anyMatch(track -> !track.equals(toRemove)))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        if (notVisited.isEmpty()) {
            return false;
        }
        visit(notVisited.iterator().next(), notVisited, toRemove);
        return !notVisited.isEmpty();
    }

    /**
     * Visits one point and all points that can be reached from this point via existing tracks.
     *
     * @param next point
     * @param notVisited points not yet visited
     * @param toRemove track to be checked
     */
    private void visit(Point next, Set<Point> notVisited, Track toRemove) {
        if (!notVisited.remove(next)) {
            return;
        }
        // Visits all points of each track
        edges.get(next).stream()
                // Only the tracks that should not be removed are considered
                .filter(track -> !toRemove.equals(track))
                // Mapping to points
                .flatMap(track -> track.getPoints().stream())
                // The starting point is not visited again
                .filter(point -> !point.equals(next))
                // Each point must be visited only once
                .distinct()
                .forEach(point -> visit(point, notVisited, toRemove));
    }

    /**
     * Finds the track on which the given point is located.
     *
     * @param point to find the track of
     * @param direction to find the track
     * @return the track wrapped in an {@link Optional}
     */
    Optional<Track> findTrack(Point point, Point direction) {
        if (edges.containsKey(point)) {
            List<Track> connections = getTrackConnections(point);
            for (Track track : connections) {
                if (track.isPassable(point.add(direction.negate()))) {
                    return Optional.of(track);
                }
            }
            for (Track track : connections) {
                if (track.isPassable(point.add(direction))) {
                    return Optional.of(track);
                }
            }
            return Optional.of(connections.get(0));
        } else {
            // Find point over values of edges (all tracks)
            return tracks.values().stream().filter(track -> track.isPassable(point)).findAny();
        }
    }

    /**
     * Provides the connected track of {@code track} at {@code point}.
     *
     * @param point to be checked
     * @param track to get the connection of
     * @return the connected track of {@code track} wrapped in an {@link Optional}
     */
    public Optional<Track> getConnection(Point point, Track track) {
        if (edges.containsKey(point)) {
            return edges.get(point).stream().filter(tr -> !tr.equals(track)
                    && tr.isPassable(point)).findAny();
        }
        return Optional.empty();
    }

    /**
     * Determines the required tracks of the given train using its {@code trainLength}.
     *
     * @param startTrack that the train starts on
     * @param startPoint where the head of the train is located
     * @param direction of the train
     * @param trainLength the length of the train
     * @param includeBoundaries determines whether the adjacent tracks are included or not
     * @return the required tracks of the train
     * @throws LogicException if there is a problem with the required tracks
     */
    public List<Track> getRequiredTracks(Track startTrack, Point startPoint, Point direction, long trainLength,
                                         boolean includeBoundaries) throws LogicException {
        Track currentTrack = startTrack;
        Point position = startPoint;
        List<Track> requiredTracks = new ArrayList<>();
        // Add adjacent track
        if (includeBoundaries) {
            getConnection(startPoint, startTrack).ifPresent(requiredTracks::add);
        }
        long length = trainLength;
        Point dir = direction;
        while (length > 0) {
            if (currentTrack == null) {
                throw new LogicException("train cannot be positioned");
            }
            requiredTracks.add(currentTrack);
            Point passed = currentTrack.getPassedPoint(dir);
            length -= position.distanceTo(passed);
            if (length > 0) {
                position = passed;
                // If there exists no track that ends a the point
                if (!getConnection(passed, currentTrack).isPresent()) {
                    throw new LogicException("not enough connected tracks found");
                }
                currentTrack = getConnection(passed, currentTrack).get();
                dir = currentTrack.getDrivingDirection(passed);
            }
            if (length == 0) {
                if (includeBoundaries) {
                    getConnection(passed, currentTrack).ifPresent(requiredTracks::add);
                }
            }
        }
        return requiredTracks;
    }
}