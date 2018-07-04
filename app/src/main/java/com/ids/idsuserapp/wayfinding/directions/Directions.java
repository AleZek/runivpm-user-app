package com.ids.idsuserapp.wayfinding.directions;

import com.ids.idsuserapp.wayfinding.IndiciNavigazione;

import java.util.ArrayList;

public class Directions extends ArrayList<Actions> {
    public Actions getCurrent(IndiciNavigazione indices) {
        return get(indices.current);
    }

    public Actions getNext(IndiciNavigazione indices) {
        return get(indices.next);
    }
}
