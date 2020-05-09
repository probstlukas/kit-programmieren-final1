package edu.kit.informatik.railway;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.exception.LogicException;
import edu.kit.informatik.userinterface.InOutput;
import edu.kit.informatik.util.Point;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Manages all {@link Train trains} that are on {@link Track tracks}.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class TrainManager {
    /**
     * Maximum amount of {@link Train trains} that can be on a {@link Track}.
     */
    private static final int MAX_TRAINS_ON_TRACK = 1;
    private final List<Train> trainsOnTracks;
    private final RailNetwork railNetwork;

    /**
     * Creates a new train manager with the given {@code railNetwork}.
     *
     * @param railNetwork that the trains will operate on
     */
    public TrainManager(RailNetwork railNetwork) {
        this.trainsOnTracks = new ArrayList<>();
        this.railNetwork = railNetwork;
    }

    /**
     * Gets all {@link Train trains} that are on tracks.
     *
     * @return all trains that are on tracks
     */
    public List<Train> getTrainsOnTracks() {
        return trainsOnTracks;
    }

    /**
     * Places a valid train on a track at the position {@code point}.
     *
     * @param train to be placed
     * @param point intended for the head of the {@code train}
     * @param direction is the initial direction of movement of the train
     * @throws LogicException if there is a problem with placing the train on the specific location
     */
    public void putTrain(Train train, Point point, Point direction) throws LogicException {
        checkPositionOfSwitches();
        if (trainsOnTracks.contains(train)) {
            throw new LogicException("train with ID " + train.getId() + " is already on tracks");
        }
        if (!train.isValidTrain()) {
            throw new LogicException("there must always be at least one engine/train-set "
                    + "at the beginning or end of a valid train");
        }
        if (direction.equals(new Point(0, 0))) {
            throw new LogicException("the direction vector cannot be 0,0");
        }
        if (!railNetwork.findTrack(point, direction).isPresent()) {
            throw new LogicException("point " + point + " is not passable");
        }
        long trainLength = train.getLength();
        if (!(direction.getFirstComponent() == 0 || direction.getSecondComponent() == 0)) {
            throw new LogicException("invalid direction vector. One value must be 0, because tracks "
                    + "can only be vertical or horizontal");
        }
        Track headOfTracks = railNetwork.findTrack(point, direction).get();
        if (headOfTracks.getSwitchedTo() == null) {
            throw new LogicException("point " + point + " is not passable, because the position "
                    + "of the switch is not set");
        }
        List<Track> requiredTracks = railNetwork.getRequiredTracks(headOfTracks, point, direction, trainLength,
                true);
        train.setPosition(point);
        train.setDirection(direction);
        assertThatNoTrainIsAssigned(requiredTracks);
        assignTrainToTracks(train, requiredTracks);
    }

    /**
     * Checks if all positions of all switches have been set.
     *
     * @throws LogicException if one or more switch positions have not been set
     */
    private void checkPositionOfSwitches() throws LogicException {
        if (railNetwork.getTracks().values().stream().anyMatch(track -> track.getSwitchedTo() == null)) {
            throw new LogicException("position of switches not set");
        }
    }

    /**
     * Asserts that no train is on the {@code requiredTracks}.
     *
     * @param requiredTracks to be checked
     * @throws LogicException if the required tracks are not free of trains
     */
    private void assertThatNoTrainIsAssigned(List<Track> requiredTracks) throws LogicException {
        for (Track track : requiredTracks) {
            if (track.getCurrentTrain() != null) {
                throw new LogicException("required tracks are not free of trains");
            }
        }
    }

    /**
     * Assigns the {@code train} to its {@code requiredTracks}.
     *
     * @param train to be assigned to its {@code requiredTracks}
     * @param requiredTracks of the {@code train}
     */
    private void assignTrainToTracks(Train train, List<Track> requiredTracks) {
        requiredTracks.forEach(required -> required.setCurrentTrain(train));
        trainsOnTracks.add(train);
    }

    /**
     * Adds the {@code newSet} to the existing {@code listSet}.
     *
     * @param listSet is the already existing set
     * @param newSet to be added to {@code listSet}
     */
    private void addToSetOrAddNew(List<SortedSet<Train>> listSet, SortedSet<Train> newSet) {
        Set<Train> existingTrains = null;
        int i = 0;
        while (i < listSet.size()) {
            final Set<Train> otherSet = listSet.get(i);
            if (otherSet.stream().anyMatch(newSet::contains)) {
                if (existingTrains == null) {
                    existingTrains = otherSet;
                    existingTrains.addAll(newSet);
                } else {
                    existingTrains.addAll(otherSet);
                    listSet.remove(i);
                    continue;
                }
            }
            i++;
        }
        if (existingTrains == null) {
            listSet.add(newSet);
        }
    }

    /**
     * Lets all {@link Train trains} move {@code speed}-units.
     *
     * @param speed to move all trains by
     * @throws LogicException if there is a problem with moving the trains
     */
    public void step(short speed) throws LogicException {
        checkPositionOfSwitches();
        if (trainsOnTracks.isEmpty()) {
            Terminal.printLine(InOutput.OK_MESSAGE);
            return;
        }
        List<SortedSet<Train>> crashes = new ArrayList<>();
        for (int i = 0; i < Math.abs(speed); i++) {
            step(speed < 0, crashes);
        }
        for (SortedSet<Train> set : crashes.stream()
                .sorted(Comparator.comparing(SortedSet::first))
                .collect(Collectors.toList())) {
            Terminal.printLine("Crash of train "  + String.join(",", set.stream()
                    .map(Train::getId)
                    .map(Object::toString)
                    .toArray(CharSequence[]::new)));
        }
        showTrainPositions();
    }

    /**
     * Shows the positions of all {@link Train trains} that are on {@link Track tracks}.
     */
    private void showTrainPositions() {
        trainsOnTracks.stream()
                .map(train -> "Train " + train.getId() + " at " + train.getPosition()).forEach(Terminal::printLine);
    }

    /**
     * One partial step which lets all {@link Train trains} move one unit.
     *
     * @param isDrivingBackwards whether the {@link Train} is driving backwards or not.
     * @param crashes to find
     * @throws LogicException if there is a problem with moving the trains
     */
    private void step(boolean isDrivingBackwards, List<SortedSet<Train>> crashes) throws LogicException {
        Map<Train, Placement> placements = nextPlacements(isDrivingBackwards);
        List<Train> removed = trainsOnTracks.stream()
                .filter(train -> !placements.containsKey(train))
                .collect(Collectors.toList());
        removed.forEach(Train::shorten);
        removed.forEach(train -> placements.put(train, train.getPlacement()));
        List<SortedSet<Train>> collided = findCollidedTrains(placements);
        trainsOnTracks.removeAll(removed);
        removed.forEach(Train::resetLength);
        placements.keySet().removeAll(removed);
        handleCollisions(placements, collided);
        if (!collided.isEmpty()) {
            crashes.addAll(collided);
        }
        removed.forEach(train -> addToSetOrAddNew(crashes, Stream.of(train)
                .collect(Collectors.toCollection(TreeSet::new))));
        railNetwork.getTracks().values().forEach(train -> train.setCurrentTrain(null));
        moveTrains(placements);
    }

    /**
     * Handles all collisions by removing the collided {@link Train trains} from the {@link Track tracks}.
     *
     * @param placements of all trains
     * @param collided trains to be removed
     */
    private void handleCollisions(Map<Train, Placement> placements, List<SortedSet<Train>> collided) {
        for (SortedSet<Train> trainSet : collided) {
            placements.keySet().removeAll(trainSet);
            trainsOnTracks.removeAll(trainSet);
        }
    }

    /**
     * Gets all next {@link Placement placements} of the {@link Train trains}.
     *
     * @param isDrivingBackwards whether the train is driving backwards or not
     * @return all next placements of the trains
     */
    private Map<Train, Placement> nextPlacements(boolean isDrivingBackwards) {
        Map<Train, Placement> result = new HashMap<>();
        trainsOnTracks.forEach((train) -> nextPlacement(train, isDrivingBackwards)
                .ifPresent(p -> result.put(train, p)));
        return result;
    }

    /**
     * Gets the next {@link Placement} of {@code train}.
     *
     * @param train to get the next placement of
     * @param isDrivingBackwards whether the {@link Train} is driving backwards or not
     * @return the next placement of the {@code train}
     */
    private Optional<Placement> nextPlacement(Train train, boolean isDrivingBackwards) {
        Track track = railNetwork.findTrack(train.getPosition(), train.getDirection()).get();
        Placement next = train.getPlacement();
        next = isDrivingBackwards ? next.moveBackwards() : next.move();
        if (!track.isPassable(next.getPosition())) {
            if (railNetwork.getConnection(train.getPosition(), track).isPresent()) {
                track = railNetwork.getConnection(train.getPosition(), track).get();
                Point current = train.getPosition();
                Point drivingDirection = track.getDrivingDirection(current).negate();
                if (isDrivingBackwards) {
                    next = train.getPlacement().moveBackwards(drivingDirection);
                } else {
                    next = train.getPlacement().move(drivingDirection);
                }
            } else {
                next = null;
            }
        }
        if (next != null) {
            try {
                railNetwork.getRequiredTracks(track, next.getPosition(), next.getDirection(), train.getLength(),
                        isDrivingBackwards);
            } catch (LogicException e) {
                next = null;
            }
        }
        return Optional.ofNullable(next);
    }

    /**
     * Finds all collided {@link Train trains}.
     *
     * @param placements of the trains
     * @return all collided trains
     * @throws LogicException if there is a problem with the required {@link Track tracks}
     */
    private List<SortedSet<Train>> findCollidedTrains(Map<Train, Placement> placements) throws LogicException {
        Map<Track, TreeSet<Train>> trainsOnTracks = new HashMap<>();
        for (Map.Entry<Train, Placement> entry : placements.entrySet()) {
            Train train = entry.getKey();
            Placement next = entry.getValue();
            Track requiredTrack = railNetwork.findTrack(next.getPosition(), next.getDirection()).get();
            List<Track> required = railNetwork.getRequiredTracks(requiredTrack, next.getPosition(), next.getDirection(),
                    train.getLength(), false);
            for (Track track : required) {
                trainsOnTracks.computeIfAbsent(track, x -> new TreeSet<>()).add(train);
            }
        }
        // Get collided trains
        trainsOnTracks.values().removeIf(trains -> trains.size() <= MAX_TRAINS_ON_TRACK);
        List<SortedSet<Train>> trainSet = new ArrayList<>();
        for (TreeSet<Train> trains : trainsOnTracks.values()) {
            if (trains.size() > 1) {
                addToSetOrAddNew(trainSet, trains);
            }
        }
        return trainSet;
    }

    /**
     * Actually move the {@link Train trains} to its {@link Placement placements} if they have not collided before.
     *
     * @param placements of the trains
     * @throws LogicException if there is a problem with the required {@link Track tracks}
     */
    private void moveTrains(Map<Train, Placement> placements) throws LogicException {
        for (Map.Entry<Train, Placement> entry : placements.entrySet()) {
            Train train = entry.getKey();
            train.setPosition(entry.getValue().getPosition());
            train.setDirection(entry.getValue().getDirection());
            Track startTrack = railNetwork.findTrack(train.getPosition(), train.getDirection()).get();
            List<Track> current = railNetwork.getRequiredTracks(startTrack, train.getPosition(), train.getDirection(),
                    train.getLength(), true);
            current.forEach(track -> track.setCurrentTrain(train));
        }
    }
}