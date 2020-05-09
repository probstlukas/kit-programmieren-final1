package edu.kit.informatik.railway;

import edu.kit.informatik.exception.LogicException;
import edu.kit.informatik.rollingstock.RollingStock;
import edu.kit.informatik.util.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Representing a train which consists of {@link RollingStock}.
 *
 * @author Lukas Probst
 * @version 1.0
 */
public class Train implements Comparable<Train> {
    private final List<RollingStock> train;
    private final int id;
    private long length;
    private final Placement placement;

    /**
     * Creates a new train with the given {@code id}.
     *
     * @param id of the train
     */
    public Train(int id) {
        train = new ArrayList<>();
        this.id = id;
        this.placement = new Placement(null, null);
    }

    /**
     * Gets the ID of the train.
     *
     * @return the ID of the train
     */
    public int getId() {
        return id;
    }

    /**
     * Adds {@code rollingStock} to the train under consideration of the coupling.
     *
     * @param rollingStock to be added
     * @throws LogicException if there are compatibility issues
     */
    public void addRollingStock(RollingStock rollingStock) throws LogicException {
        if (!train.isEmpty()) {
            RollingStock lastRollingStock = train.get(train.size() - 1);
            // Check coupling compatibility
            if (rollingStock.canCoupleTo(lastRollingStock) && lastRollingStock.canCoupleTo(rollingStock)
                    && lastRollingStock.isCouplingBack() && rollingStock.isCouplingFront()) {
                train.add(rollingStock);
                length += rollingStock.getLength();
            } else {
                throw new LogicException("invalid coupling");
            }
        } else {
            // No restrictions
            train.add(rollingStock);
            length += rollingStock.getLength();
        }
    }

    /**
     * Gets the length of the train.
     *
     * @return the length of the train
     */
    public long getLength() {
        return length;
    }

    /**
     * Gets the {@link RollingStock} of the train.
     *
     * @return the rolling stock of the train
     */
    public List<RollingStock> getTrain() {
        return train;
    }

    /**
     * Validates the train with regard to its restriction that
     * there must be at least one {@link edu.kit.informatik.rollingstock.Engine}
     * or {@link edu.kit.informatik.rollingstock.TrainSet} at the beginning or end of a train.
     *
     * @return <code>true</code> if the train is valid
     */
    public boolean isValidTrain() {
        RollingStock firstRollingStock = train.get(0);
        RollingStock lastRollingStock = train.get(train.size() - 1);
        return firstRollingStock.isUseful() || lastRollingStock.isUseful();
    }

    /**
     * Dynamically adjusts the height of the graphical representations.
     * This is useful if the height of another graphical representation of a train is different.
     *
     * @param requiredHeight for the graphical representation
     * @param wagon to adjust the height of
     * @return the adjusted height of {@code wagon}
     */
    private String[] adjustHeight(int requiredHeight, String[] wagon) {
        String[] result = new String[requiredHeight];
        for (int i = 0; i < requiredHeight - wagon.length; i++) {
            result[i] = "";
        }
        System.arraycopy(wagon, 0, result, requiredHeight - wagon.length, wagon.length);
        return result;
    }

    /**
     * Dynamically adjusts the width of the graphical representations.
     *
     * @param wagon to adjust the height of
     * @return the adjusted width of {@code wagon}
     */
    private String[] adjustWidth(String[] wagon) {
        String[] result = new String[wagon.length];
        int maxWidth = 0;
        for (String s : wagon) {
            if (s.length() > maxWidth) {
                maxWidth = s.length();
            }
        }
        for (int i = 0; i < wagon.length; i++) {
            result[i] = wagon[i];
            for (int j = 0; j < maxWidth - wagon[i].length(); j++) {
                result[i] += " ";
            }
        }
        return result;
    }

    /**
     * Shows the graphical representation of the {@link Train}.
     *
     * @return the graphical representation of the train
     */
    public String show() {
        int numberOfWagons = getTrain().size();
        String[][] trainGraphicParsing = new String[numberOfWagons][numberOfWagons];
        int count = 0;
        for (RollingStock rollingStock : getTrain()) {
            trainGraphicParsing[count] = rollingStock.graphic();
            count++;
        }
        int maxHeight = 0;
        for (String[] wagon : trainGraphicParsing) {
            if (wagon.length > maxHeight) {
                maxHeight = wagon.length;
            }
        }
        for (int i = 0; i < trainGraphicParsing.length; i++) {
            trainGraphicParsing[i] = adjustHeight(maxHeight, trainGraphicParsing[i]);
            trainGraphicParsing[i] = adjustWidth(trainGraphicParsing[i]);
        }
        StringBuilder trainGraphic = new StringBuilder();
        // Height index
        for (int i = 0; i < maxHeight; i++) {
            // Wagon index
            for (int j = 0; j < trainGraphicParsing.length; j++) {
                // Add a space between every wagon of the train
                if (j != trainGraphicParsing.length - 1) {
                    trainGraphic.append(trainGraphicParsing[j][i]).append(" ");
                } else {
                    trainGraphic.append(trainGraphicParsing[j][i]);
                }
            }
            if (i < maxHeight - 1) {
                trainGraphic.append(System.lineSeparator());
            }
        }
        return trainGraphic.toString();
    }

    /**
     * Returns the string representation of a train.
     *
     * @return String of the format <b>[trainID] [members]</b>
     */
    @Override
    public String toString() {
        StringBuilder rollingStockIds = new StringBuilder();
        int count = 0;
        for (RollingStock rollingStock : train) {
            count++;
            rollingStockIds.append(rollingStock.getId());
            if (count < train.size()) {
                rollingStockIds.append(" ");
            }
        }
        return getId() + " " + rollingStockIds;
    }

    /**
     * Gets the {@link Placement} of the train.
     *
     * @return the placement of the train
     */
    public Placement getPlacement() {
        return placement;
    }

    /**
     * Gets the position of the {@link Train} via its {@link Placement}.
     *
     * @return the position of the train
     */
    public Point getPosition() {
        return placement.getPosition();
    }

    /**
     * Gets the direction of the {@link Train} via its {@link Placement}.
     *
     * @return the direction of the train
     */
    public Point getDirection() {
        return placement.getDirection();
    }

    /**
     * Sets the position of the {@link Train} via its {@link Placement}.
     *
     * @param position to be set
     */
    public void setPosition(Point position) {
        placement.setPosition(position);
    }

    /**
     * Sets the direction of the {@link Train} via its {@link Placement}.
     *
     * @param direction to be set
     */
    public void setDirection(Point direction) {
        placement.setDirection(direction);
    }

    /**
     * Shortens the train by one length-unit.
     */
    void shorten() {
        length--;
    }

    /**
     * Resets the length of the train to its initial length.
     */
    public void resetLength() {
        length = 0;
        for (RollingStock stock : train) {
            length += stock.getLength();
        }
    }

    /**
     * Removes the train from the {@link Track tracks} by setting its position and direction to null.
     */
    public void removeFromTracks() {
        setPosition(null);
        setDirection(null);
    }

    /**
     * Returns <code>true</code> if the train touches the given {@code track}.
     *
     * @param track to be checked
     * @return <code>true</code> if the train touches the {@code track}
     */
    public boolean touches(Track track) {
        if (placement.getPosition() == null || placement.getDirection() == null) {
            return false;
        }
        int trainFirstComponent = (int) placement.getPosition().getFirstComponent();
        int trainSecondComponent = (int) placement.getPosition().getFirstComponent();
        int startPointFirstComponent = (int) track.getStartPoint().getFirstComponent();
        int startPointSecondComponent = (int) track.getStartPoint().getSecondComponent();
        int switchedToFirstComponent = (int) track.getSwitchedTo().getFirstComponent();
        int switchedToSecondComponent = (int) track.getSwitchedTo().getSecondComponent();
        return ((trainFirstComponent >= startPointFirstComponent
                && trainFirstComponent <= switchedToFirstComponent
                && trainSecondComponent >= startPointSecondComponent
                && trainSecondComponent <= switchedToSecondComponent)
                || (track.isPassable(this.getPosition())));
    }

    /**
     * Sorts the train by its {@link #id}.
     *
     * @param train to be compared to
     * @return the order of both trains
     */
    @Override
    public int compareTo(Train train) {
        return Integer.compare(id, train.getId());
    }
}