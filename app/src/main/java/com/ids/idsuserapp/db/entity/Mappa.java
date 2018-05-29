package com.ids.idsuserapp.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;


@Entity
public class Mappa {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String immagine;


    public Mappa(){}

    public Mappa(String name) {
        this.name = name;
    }

    public Mappa(ArrayList<String> fields) {
        this.name = fields.get(0);
        this.id = Integer.parseInt(fields.get(1));
        this.immagine = fields.get(2);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }
}
