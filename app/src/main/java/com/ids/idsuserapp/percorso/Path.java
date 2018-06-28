package com.ids.idsuserapp.percorso;

import com.ids.idsuserapp.db.entity.Tronco;

import java.util.ArrayList;
import java.util.List;

import es.usc.citius.hipster.model.impl.WeightedNode;

public class Path extends ArrayList<Checkpoint> {
    private WeightedNode goalState;
    private Tronco excludedTrunk;

    public Path() {
    }

    public Path(List<Checkpoint> nodes) {
        addAll(nodes);
    }

    public Path(Path path) {
        addAll(path);
        this.setGoalState(path.getGoalState());
        this.setExcludedTrunk(path.getExcludedTrunk());
    }

    /**
     * Iterate over a path and divide it by floor
     *
     * @return A List with solution divided by floor
     */
    public MultiFloorPath toMultiFloorPath() {
        String floor = null;
        MultiFloorPath multiFloorPath = new MultiFloorPath();

        multiFloorPath.setOrigin(getOrigin());
        multiFloorPath.setDestination(getDestination());

        for (Checkpoint checkpoint : this) {
            if (!checkpoint.getFloor().equals(floor)) {
                floor = checkpoint.getFloor();
                multiFloorPath.put(floor, new Path());
            }
            multiFloorPath.get(checkpoint.getFloor()).add(checkpoint);
        }

        return multiFloorPath;
    }

    public WeightedNode getGoalState() {
        return goalState;
    }

    public Tronco getExcludedTrunk() {
        return excludedTrunk;
    }

    public Path setExcludedTrunk(Tronco excludedTrunk) {
        this.excludedTrunk = excludedTrunk;
        return this;
    }

    public NavigationIndices incrementIndices(int current) {
        int next;
        current = isLastIndex(current) ? current : current + 1;
        next = isLastIndex(current) ? current : current + 1;

        return new NavigationIndices(current, next);
    }

    public NavigationIndices incrementIndices(NavigationIndices indices) {
        return incrementIndices(indices.current);
    }

    public NavigationIndices decrementIndices(NavigationIndices indices) {
        return decrementIndices(indices.current);
    }

    public NavigationIndices decrementIndices(int current) {
        current = (current > 0 && current <= size() - 1) ? current - 1 : current;
        return new NavigationIndices(current, current + 1);
    }

    public Checkpoint getCurrent(NavigationIndices indices) {
        return get(indices.current);
    }

    public Checkpoint getNext(NavigationIndices indices) {
        return get(indices.next);
    }

    public boolean isLastIndex(int index) {
        return index == size() - 1;
    }

    public Path setGoalState(WeightedNode goalState) {
        this.goalState = goalState;
        return this;
    }

    public Checkpoint getOrigin() {
        return get(0);
    }

    public Checkpoint getDestination() {
        return get(size() - 1);
    }

    public boolean isDestinationReached() {
        return size() == 1;
    }

    @Override
    public String toString() {
        return "Path{" +
                "goalState=" + goalState +
                ", excludedTrunk=" + excludedTrunk +
                '}';
    }
}
