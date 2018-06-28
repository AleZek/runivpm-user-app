package com.ids.idsuserapp.percorso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class MultiFloorPath extends HashMap<String, Path> {
    private Checkpoint origin;
    private Checkpoint destination;

    public MultiFloorPath() {
    }

    public Path toPath() {
        return toPath(origin, destination);
    }

    public Path toPath(Checkpoint origin, Checkpoint destination) {
        int originFloor = origin.getFloorInt();
        int destinationFloor = destination.getFloorInt();
        boolean ascendant = originFloor <= destinationFloor;

        Set<String> floorSet = keySet();
        ArrayList<Integer> floors = new ArrayList<>(floorSet.size());
        for (String floor : floorSet) {
            floors.add(Integer.parseInt(floor));
        }

        if (ascendant) {
            Collections.sort(floors);
        } else {
            Collections.sort(floors, Collections.<Integer>reverseOrder());
        }

        Path path = new Path();
        for (Integer floor : floors) {
            String f = String.valueOf(floor);
            for (Checkpoint checkpoint : get(f)) {
                path.add(checkpoint);
            }
        }

        return path;
    }

    public Checkpoint getOrigin() {
        return origin;
    }

    public MultiFloorPath setOrigin(Checkpoint origin) {
        this.origin = origin;
        return this;
    }

    public Checkpoint getDestination() {
        return destination;
    }

    public MultiFloorPath setDestination(Checkpoint destination) {
        this.destination = destination;
        return this;
    }
}
