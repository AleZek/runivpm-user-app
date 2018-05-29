package com.ids.idsuserapp.wayfinding;

import java.util.Comparator;


public class PathComparator implements Comparator<Percorso> {
    @Override
    public int compare(Percorso lhs, Percorso rhs) {
        return lhs.getGoalState().compareTo(rhs.getGoalState());
    }
}