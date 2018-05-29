package com.ids.idsuserapp.wayfinding;

import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.db.entity.Tronco;

import java.util.ArrayList;
import java.util.List;

import es.usc.citius.hipster.model.impl.WeightedNode;

public class Percorso extends ArrayList<Beacon> {
    private WeightedNode goalState;
    private Tronco troncoEscluso;

    public Percorso() {
    }

    public Percorso(List<Beacon> nodes) {
        addAll(nodes);
    }

    public Percorso(Percorso percorso) {
        addAll(percorso);
        this.setGoalState(percorso.getGoalState());
        this.setTroncoEscluso(percorso.getTroncoEscluso());
    }

    /**
     * Iterate over a path and divide it by floor
     *
     * @return A List with solution divided by floor
     */
    public PercorsoMultipiano toMultiFloorPath() {
        String floor = null;
        PercorsoMultipiano multiFloorPath = new PercorsoMultipiano();

        multiFloorPath.setOrigine(getOrigine());
        multiFloorPath.setDestinazione(getDestinazione());

        for (Beacon checkpoint : this) {
            if (!checkpoint.getQuota().equals(floor)) {
                floor = checkpoint.getQuota();
                multiFloorPath.put(floor, new Percorso());
            }
            multiFloorPath.get(checkpoint.getQuota()).add(checkpoint);
        }

        return multiFloorPath;
    }

    public WeightedNode getGoalState() {
        return goalState;
    }

    public Tronco getTroncoEscluso() {
        return troncoEscluso;
    }

    public Percorso setTroncoEscluso(Tronco troncoEscluso) {
        this.troncoEscluso = troncoEscluso;
        return this;
    }

    public IndiciNavigazione incrementIndices(int current) {
        int next;
        current = isLastIndex(current) ? current : current + 1;
        next = isLastIndex(current) ? current : current + 1;

        return new IndiciNavigazione(current, next);
    }

    public IndiciNavigazione incrementIndices(IndiciNavigazione indices) {
        return incrementIndices(indices.current);
    }

    public IndiciNavigazione decrementIndices(IndiciNavigazione indices) {
        return decrementIndices(indices.current);
    }

    public IndiciNavigazione decrementIndices(int current) {
        current = (current > 0 && current <= size() - 1) ? current - 1 : current;
        return new IndiciNavigazione(current, current + 1);
    }

    public Beacon getCurrent(IndiciNavigazione indices) {
        return get(indices.current);
    }

    public Beacon getNext(IndiciNavigazione indices) {
        return get(indices.next);
    }

    public boolean isLastIndex(int index) {
        return index == size() - 1;
    }

    public Percorso setGoalState(WeightedNode goalState) {
        this.goalState = goalState;
        return this;
    }

    public Beacon getOrigine() {
        return get(0);
    }

    public Beacon getDestinazione() {
        return get(size() - 1);
    }

    public boolean destinazioneRaggiunta() {
        return size() == 1;
    }

    @Override
    public String toString() {
        return "Percorso{" +
                "goalState=" + goalState +
                ", troncoEscluso=" + troncoEscluso +
                '}';
    }
}
