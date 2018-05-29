package com.ids.idsuserapp.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.ids.idsuserapp.wayfinding.Checkpoint;

import java.util.ArrayList;
import java.util.Objects;

import static android.arch.persistence.room.ForeignKey.CASCADE;


@Entity(foreignKeys = @ForeignKey(entity = Mappa.class,
        parentColumns = "id",
        childColumns = "mappa",
        onDelete = CASCADE))

public class Beacon implements Checkpoint{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nome;
    private String quota;
    private double larghezza;
    private int x;
    private int y;
    //private int x_meter;
    //private int y_meter;
    private int mappa;


    public Beacon(){}

    public Beacon(String nome, String quota, double larghezza, int x, int y, /*int x_meter, int y_meter,*/ int mappa) {
        this.nome = nome;
        this.quota = quota;
        this.larghezza = larghezza;
        this.x = x;
        this.y = y;
      //  this.x_meter = x_meter;
       // this.y_meter = y_meter;
        this.mappa = mappa;
    }

    public Beacon(ArrayList<String> fields) {
        this.id = Integer.parseInt(fields.get(0));
        this.nome = fields.get(1);
        this.quota = fields.get(4);
        this.larghezza = Double.parseDouble(fields.get(5));
        this.x = Integer.parseInt(fields.get(2));
        this.y = Integer.parseInt(fields.get(3));
      //  this.x_meter = Integer.parseInt(fields.get(6));
     //   this.y_meter = Integer.parseInt(fields.get(7));
        String apimappa = fields.get(6).substring(12);
        this.mappa = Integer.parseInt(apimappa);
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getQuota() { return quota; }

    public int getQuotaInt() { return Integer.parseInt(quota); }

    public Double getLarghezza() {return larghezza; }

    public int getX() {return x;}

    public int getY() {return y;}
/*
    public Integer getX_meter() {return x_meter;}

    public Integer getY_meter() {return y_meter;}
*/
    public int getMappa() {
        return mappa;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String name) {
        this.nome = name;
    }

    public void setQuota (String quota) {this.quota=quota;}

    public void setLarghezza(Double larghezza){this.larghezza = larghezza;}

    public void setX(int x) {
        this.x = x;
    }
/*
    public void setX_meter(int x_meter) {
        this.x_meter = x_meter;
    }*/
    public void setY(int y) {
        this.y = y;
    }
/*
    public void setY_meter(int y_meter) {
        this.y_meter = y_meter;
    }
*/
    public void setMappa(int mappa) {
        this.mappa = mappa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Beacon)) return false;
        Beacon beacon = (Beacon) o;
        return id == beacon.id &&
                Double.compare(beacon.larghezza, larghezza) == 0 &&
                x == beacon.x &&
                y == beacon.y &&
                mappa == beacon.mappa &&
                Objects.equals(nome, beacon.nome) &&
                Objects.equals(quota, beacon.quota);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, nome, quota, larghezza, x, y, mappa);
    }

    @Override
    public String toString() {
        return "Beacon{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", quota='" + quota + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}