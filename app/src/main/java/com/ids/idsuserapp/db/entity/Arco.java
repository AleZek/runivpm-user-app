package com.ids.idsuserapp.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.ids.idsuserapp.db.entity.Beacon;

import java.util.HashMap;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {
        @ForeignKey(entity = Beacon.class,
                parentColumns = "id",
                childColumns = "begin",
                onDelete = CASCADE),
        @ForeignKey(entity = Beacon.class,
                parentColumns = "id",
                childColumns = "end",
                onDelete = CASCADE)})
public class Arco {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "arco_id")
    private int id;


    private String  begin;
    private String end;
    private double length;
    private double width;
    private boolean stairs;
    private double v;
    private double i;
    private double los;
    private double c;


    public Arco(){}

    public Arco(String begin, String end, double length, double width, boolean stairs, double v, double i, double los, double c) {
        this.begin = begin;
        this.end = end;
        this.length = length;
        this.width = width;
        this.stairs = stairs;
        this.v = v;
        this.i = i;
        this.los = los;
        this.c = c;
    }

    public Arco (HashMap<String,String> nuovoArco){
        this.id = Integer.parseInt(nuovoArco.get("id"));
        this.begin = nuovoArco.get("begin").replace("/api/beacons/", "");
        this.end = nuovoArco.get("end").replace("/api/beacons/", "");
        this.length = Double.parseDouble(nuovoArco.get("length"));
        this.width = Double.parseDouble(nuovoArco.get("width"));
        this.stairs = Boolean.parseBoolean(nuovoArco.get("stairs"));
        this.v = Double.parseDouble(nuovoArco.get("v"));
        this.i = Double.parseDouble(nuovoArco.get("i"));
        this.c = Double.parseDouble(nuovoArco.get("c"));
        this.los = Double.parseDouble(nuovoArco.get("los"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public boolean isStairs() {
        return stairs;
    }

    public void setStairs(boolean stairs) {
        this.stairs = stairs;
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }

    public double getI() {
        return i;
    }

    public void setI(double i) {
        this.i = i;
    }

    public double getLos() {
        return los;
    }

    public void setLos(double los) {
        this.los = los;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "Arco{" +
                "id=" + id +
                ", length=" + length +
                '}';
    }
}
