package com.ids.idsuserapp.wayfinding;

import com.ids.idsuserapp.db.entity.Beacon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class PercorsoMultipiano extends HashMap<String, Percorso> {
    private Beacon origine;
    private Beacon destinazione;

    public PercorsoMultipiano() {
    }

    public Percorso toPath() {
        return toPath(origine, destinazione);
    }

    public Percorso toPath(Beacon origine, Beacon destinazione) {
        int pianoOrigine = origine.getFloorInt();
        int pianoDestinazione = destinazione.getFloorInt();
        boolean ascesa = pianoOrigine <= pianoDestinazione;

        Set<String> floorSet = keySet();
        ArrayList<Integer> floors = new ArrayList<>(floorSet.size());
        for (String floor : floorSet) {
            floors.add(Integer.parseInt(floor));
        }

        if (ascesa) {
            Collections.sort(floors);
        } else {
            Collections.sort(floors, Collections.<Integer>reverseOrder());
        }

        Percorso percorso = new Percorso();
        for (Integer floor : floors) {
            String f = String.valueOf(floor);
            for (Beacon checkpoint : get(f)) {
                percorso.add(checkpoint);
            }
        }

        return percorso;
    }

    public Beacon getOrigine() {
        return origine;
    }

    public PercorsoMultipiano setOrigine(Beacon origine) {
        this.origine = origine;
        return this;
    }

    public Beacon getDestinazione() {
        return destinazione;
    }

    public PercorsoMultipiano setDestinazione(Beacon destinazione) {
        this.destinazione = destinazione;
        return this;
    }
}
