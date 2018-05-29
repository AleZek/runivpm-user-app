package com.ids.idsuserapp.wayfinding;


import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.db.entity.Tronco;

import java.util.ArrayList;
import java.util.List;

public class Grafo extends ArrayList<Tronco> {
    public static final String TAG = Grafo.class.getName();

    public Grafo(List<? extends Tronco> trunks) {
        addAll(trunks);
    }

    public Grafo() {
    }

    public boolean Connessione() {
        Grafo regioneConnessa = new Grafo();
        Grafo regioneNonConnessa = new Grafo();

        // Spezzo il grafo in una parte connessa ed in una parte non connessa
        regioneConnessa.add(get(0));
        for (int i = 1; i < size(); i++) {
            regioneNonConnessa.add(get(i));
        }

        int maxIterations = regioneNonConnessa.size();
        ArrayList<Tronco> toDeleteTroncos;

        for (int i = 0; i < maxIterations && !regioneNonConnessa.isEmpty(); i++) {
            toDeleteTroncos = new ArrayList<>();

            for (Tronco troncoNonConnesso : regioneNonConnessa) {
                if(regioneConnessa.isConnectedTo(troncoNonConnesso)) {
                    regioneConnessa.add(troncoNonConnesso);
                    toDeleteTroncos.add(troncoNonConnesso);
                }
            }

            for (Tronco tronco : toDeleteTroncos) {
                regioneNonConnessa.remove(tronco);
            }

        }

        return regioneNonConnessa.isEmpty();
    }

    public List<Tronco> getStar(Beacon checkpoint) {
        List<Tronco> troncos = new ArrayList<>();

        for (Tronco tronco : this) {
            if(tronco.isConnectedTo(checkpoint)) {
                troncos.add(tronco);
            }
        }
        return troncos;
    }

    private boolean isConnectedTo(Tronco tronco) {
        for (Tronco graphTronco : this) {
            if(graphTronco.isConnectedTo(tronco)) {
                return true;
            }
        }

        return false;
    }

    public Grafo createNewGraphWithoutATrunk(Tronco troncoToKill) {
        Grafo subGrafo = new Grafo();
        for (Tronco tronco : this) {
            if (!tronco.equals(troncoToKill)) {
                subGrafo.add(tronco);
            }
        }
        return subGrafo;
    }

    public Tronco getTrunkToBreakPath(Percorso percorso) {
        return searchTrunk(percorso.get(0), percorso.get(1));
    }

    public Tronco searchTrunk(Beacon checkpoint1, Beacon checkpoint2) {
        Tronco found = null;

        for (Tronco tronco : this) {
            if (tronco.isConnecting(checkpoint1, checkpoint2)) {
                found = tronco;
                break;
            }
        }

        return found;
    }
}
