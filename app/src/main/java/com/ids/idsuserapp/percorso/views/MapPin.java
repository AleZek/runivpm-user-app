package com.ids.idsuserapp.percorso.views;

import android.graphics.PointF;

public class MapPin {
    float X, Y;
    int id;
    Colors color = Colors.RED;

    public MapPin(float X, float Y, int id) {
        this.X = X;
        this.Y = Y;
        this.id = id;
    }

    public MapPin(float X, float Y) {
        this.X = X;
        this.Y = Y;
    }

    public MapPin(PointF pointF, int id) {
        this.X = pointF.x;
        this.Y = pointF.y;
        this.id = id;
    }

    public MapPin(PointF pointF) {
        this.X = pointF.x;
        this.Y = pointF.y;
    }

    public MapPin(PointF pointF, Colors color) {
        this.X = pointF.x;
        this.Y = pointF.y;
        this.color = color;
    }

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

    public float getX() {
        return X;
    }

    public void setX(float X) {
        this.X = X;
    }

    public float getY() {
        return Y;
    }

    public void setY(float Y) {
        this.Y = Y;
    }

    public PointF getPoint() {
        return new PointF(this.X, this.Y);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public enum Colors {BLUE, RED, LOCATION}
}