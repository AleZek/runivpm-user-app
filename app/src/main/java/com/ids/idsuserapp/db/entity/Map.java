package com.ids.idsuserapp.db.entity;

import android.graphics.Bitmap;

public class Map {
    private String floor;
    private Bitmap image;

    public Map(String floor, Bitmap image) {
        this.floor = floor;
        this.image = image;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public int getFloorInt() {
        return Integer.parseInt(floor);
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Map{" +
                "floor='" + floor + '\'' +
                ", image=" + image +
                '}';
    }
}
