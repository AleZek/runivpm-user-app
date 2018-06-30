package com.ids.idsuserapp.percorso;

import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.wayfinding.Percorso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class MultiFloorPath extends HashMap<String, Percorso> {
    private Beacon origin;
    private Beacon destination;

    public MultiFloorPath() {
    }

    public Percorso toPath() {
        return toPath(origin, destination);
    }

    public Percorso toPath(Beacon origin, Beacon destination) {
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

        Percorso path = new Percorso();

        for (Integer floor : floors) {
            String f = String.valueOf(floor);
            for (Beacon beacon : get(f)) {
                path.add(beacon);
            }
        }

        return path;
    }

    public Beacon getOrigin() {
        return origin;
    }

    public MultiFloorPath setOrigin(Beacon origin) {
        this.origin = origin;
        return this;
    }

    public Beacon getDestination() {
        return destination;
    }

    public MultiFloorPath setDestination(Beacon destination) {
        this.destination = destination;
        return this;
    }
}
