package edu.kit.informatik.railway;

import edu.kit.informatik.exception.LogicException;
import edu.kit.informatik.rollingstock.Coach;
import edu.kit.informatik.rollingstock.Engine;
import edu.kit.informatik.rollingstock.RollingStock;
import edu.kit.informatik.rollingstock.TrainSet;
import edu.kit.informatik.exception.InvalidInputException;
import edu.kit.informatik.util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

/**
 * Register to manage railway-related objects. The register keeps track of {@link Train trains},
 * the {@link RailNetwork}, the {@link TrainManager} and all kinds of rolling stock, among other things.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class Register {
    private final RailNetwork network;
    private final TrainManager trainManager;
    private final Map<Integer, Train> trains;
    private final Map<Integer, Coach> coaches;
    private final List<Engine> engines;
    private final List<TrainSet> trainSets;

    /**
     * Creates a new register and initialises the collections and states.
     */
    public Register() {
        this.trains = new TreeMap<>();
        this.coaches = new TreeMap<>();
        this.engines = new ArrayList<>();
        this.trainSets = new ArrayList<>();
        this.network = new RailNetwork();
        this.trainManager = new TrainManager(network);
    }

    /**
     * Gets the {@link RailNetwork}.
     *
     * @return the rail network
     */
    public RailNetwork getNetwork() {
        return network;
    }

    /*
    Track commands
     */

    /**
     * Adds a new track to the {@link RailNetwork}.
     *
     * @param track to be added
     * @throws LogicException if the {@code track} cannot be added
     */
    public void addTrack(Track track) throws LogicException {
        network.addTrack(track);
        for (Train train : trainManager.getTrainsOnTracks()) {
            if (network.touchesTrack(train, track)) {
                track.setCurrentTrain(train);
            }
        }
    }

    /**
     * Removes a track from the {@link RailNetwork}.
     *
     * @param trackId of the track to be removed
     * @throws LogicException if the track cannot be removed. For instance, it could be a bridge in the network
     */
    public void removeTrack(int trackId) throws LogicException {
        network.removeTrack(trackId);
    }

    /**
     * Sets the position of the switch
     *
     * @param trackId of the switch to be positioned
     * @param point to be switched to
     * @throws LogicException if the switch cannot be positioned.
     *  For instance, if the given point is not an endpoint of the switch
     */
    public void setSwitch(int trackId, Point point) throws LogicException {
        network.setSwitch(trackId, point);
        Track track = network.getTracks().get(trackId);
        trains.values().stream().filter(train -> train.touches(track)).forEach(toRemove -> {
            toRemove.removeFromTracks();
            network.getTracks().values().stream()
                    .filter(tr -> tr.getCurrentTrain() == toRemove)
                    .forEach(tr -> tr.setCurrentTrain(null));
            trainManager.getTrainsOnTracks().remove(toRemove);
        });
    }

    /*
    Helper methods
     */

    /**
     * Gets the next smallest available ID greater 0 in the given set {@code ids}.
     *
     * @param ids to get the next smallest available ID of
     * @return the next smallest available ID
     */
    public int getNextId(Set<Integer> ids) {
        int id = 1;
        for (int key : ids) {
            if (key > id) {
                break;
            }
            id = key + 1;
        }
        return id;
    }

    /**
     * Verifies the given ID which consists of a series and a name.
     *
     * @param series of the ID
     * @param name of the ID
     * @throws InvalidInputException if the ID already exists
     */
    public void verifyId(String series, String name) throws InvalidInputException {
        for (Engine engine : engines) {
            if (engine.getSeries().equals(series) && engine.getName().equals(name)) {
                throw new InvalidInputException("engine with ID " + engine.getId() + " already exists");
            }
        }
        for (TrainSet trainSet : trainSets) {
            if (trainSet.getSeries().equals(series) && trainSet.getName().equals(name)) {
                throw new InvalidInputException("train-set with ID " + trainSet.getId() + " already exists");
            }
        }
    }

    /*
    Engine Commands
     */

    /**
     * Creates a new {@link Engine}.
     *
     * @param engine to be created
     */
    public void createEngine(Engine engine) {
        this.engines.add(engine);
    }

    /**
     * Gets all {@link Engine engines}.
     *
     * @return all engines
     */
    public List<Engine> getEngines() {
        return engines;
    }

    /*
    Coach Commands
    */

    /**
     * Creates a new {@link Coach}.
     *
     * @param coach to be created
     */
    public void createCoach(Coach coach) {
        this.coaches.put(coach.getCoachId(), coach);
    }

    /**
     * Gets all {@link Coach coaches}.
     *
     * @return all coaches
     */
    public Map<Integer, Coach> getCoaches() {
        return coaches;
    }

    /*
    TrainSet Commands
     */

    /**
     * Creates a new {@link TrainSet}.
     *
     * @param trainSet to be created
     */
    public void createTrainSet(TrainSet trainSet) {
        this.trainSets.add(trainSet);
    }

    /**
     * Gets all {@link TrainSet train-sets}.
     *
     * @return all train-sets
     */
    public List<TrainSet> getTrainSets() {
        return trainSets;
    }

    /*
    Train Commands
     */

    /**
     * Gets the rolling stock by its {@code rollingStockId}.
     *
     * @param rollingStockId of the rolling stock to get
     * @return the rolling stock
     * @throws LogicException if the rolling stock is not existent
     * @throws InvalidInputException if the rolling stock is a {@link Coach} and the ID is not a 32-bit integer
     */
    public RollingStock getRollingStock(String rollingStockId) throws LogicException, InvalidInputException {
        if (rollingStockId.contains("-")) {
            for (Engine engine : engines) {
                if (engine.getId().equals(rollingStockId)) {
                    return engine;
                }
            }
            for (TrainSet trainSet : trainSets) {
                if (trainSet.getId().equals(rollingStockId)) {
                    return trainSet;
                }
            }
            throw new LogicException("there is no train-set or engine with ID " + rollingStockId);
        } else {
            int coachId;
            try {
                coachId = Integer.parseInt(rollingStockId.substring(1));
            } catch (NumberFormatException e) {
                throw new InvalidInputException("coach ID must be a 32-bit integer");
            }
            if (coaches.containsKey(coachId)) {
                return coaches.get(coachId);
            } else {
                throw new LogicException("coach with ID " + coachId + " not existent");
            }
        }
    }

    /**
     * Adds the given {@code rollingStock} to the {@link Train} with the given {@code trainId}.
     * If the {@code trainId} does not exist, a new train will be created with the given {@code rollingStock}.
     * However, only if it is the next smallest available ID.
     *
     * @param trainId of the train
     * @param rollingStock to be added
     * @throws LogicException if the {@code rollingStock} is already being used by another train;
     *  if the train has already been put on a track; if the {@code trainId} does not match next smallest available ID
     */
    public void addTrain(int trainId, RollingStock rollingStock) throws LogicException {
        // Check if rolling stock is not being used in another train so far
        if (getTrainWithRollingStock(rollingStock).isPresent()) {
            throw new LogicException("rolling stock with ID " + rollingStock.getId() + " is already "
                    + "being used in train " + getTrainWithRollingStock(rollingStock).get().getId());
        }
        int nextId = getNextId(trains.keySet());
        if (trains.containsKey(trainId)) {
            Train train = trains.get(trainId);
            List<Train> trainsOnTrack = trainManager.getTrainsOnTracks();
            if (trainsOnTrack != null && trainsOnTrack.contains(train)) {
                throw new LogicException("the train has already been put on a track");
            }
            train.addRollingStock(rollingStock);
        } else {
            if (trainId != nextId) {
                throw new LogicException("train ID must match the next available ID which is " + nextId);
            }
            Train newTrain = new Train(trainId);
            newTrain.addRollingStock(rollingStock);
            this.trains.put(trainId, newTrain);
        }
    }

    /**
     * Removes the {@link Train} with the given {@code id}.
     *
     * @param id of the train to be removed
     * @throws InvalidInputException if the train is not existent
     */
    public void removeTrain(int id) throws InvalidInputException {
        if (trains.containsKey(id)) {
            Train train = trains.get(id);
            this.trains.remove(id);
            trainManager.getTrainsOnTracks().remove(train);
        } else {
            throw new InvalidInputException("train with ID " + id + " does not exist");
        }
    }

    /**
     * Gets all {@link Train trains}.
     *
     * @return all trains.
     */
    public Map<Integer, Train> getTrains() {
        return trains;
    }

    /**
     * Gets the train with the given {@code rollingStock}.
     *
     * @param rollingStock to get the train of
     * @return the train that contains {@code rollingStock} wrapped in an {@link Optional}
     */
    public Optional<Train> getTrainWithRollingStock(RollingStock rollingStock) {
        return trains.values().stream().filter(train -> train.getTrain().contains(rollingStock)).findFirst();
    }

    /**
     * Puts a valid {@link Train} on a {@link Track} at the position {@code point} with its initial {@code direction}.
     *
     * @param trainId of the train
     * @param point to place the train head on
     * @param directionVector of the train
     * @throws LogicException if the train is not existent
     */
    public void putTrain(int trainId, Point point, Point directionVector) throws LogicException {
        if (trains.containsKey(trainId)) {
            Train train = trains.get(trainId);
            trainManager.putTrain(train, point, directionVector);
        } else {
            throw new LogicException("train with ID " + trainId + " not existent");
        }
    }

    /**
     * Lets all trains move {@code speed}-units.
     *
     * @param speed to move all trains by
     * @throws LogicException if there is a problem with moving the trains
     */
    public void step(short speed) throws LogicException {
        trainManager.step(speed);
    }
}
