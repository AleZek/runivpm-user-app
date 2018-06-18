package com.ids.idsuserapp.db.entity;

import java.io.Serializable;

public class Position implements Serializable {
    public final double x;
    public final double y;
    public final int floor;

    public Position(double x, double y, int floor) {
        this.x = x;
        this.y = y;
        this.floor = floor;
    }

    public Position(Beacon node) {
        this.x = node.getX();
        this.y = node.getY();
        this.floor = node.getFloor();
    }

    public double distance(Position p) {
        return distance((int) p.x, (int) p.y);
    }

    public double distance(int x, int y) {
        return Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));
    }

    @Override
    public String toString() {
        return "{x: " + Double.toString(x) + ", y: " +
                Double.toString(y) + ", floor: " +
                floor + "}";
    }
}
