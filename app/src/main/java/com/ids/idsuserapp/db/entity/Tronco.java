package com.ids.idsuserapp.db.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class Tronco {

    private static final double P_V = 0.07;
    private static final double P_I = 0.45;
    private static final double P_LOS = 0.21;
    private static final double P_C = 0.21;
    private static final double P_L = 0.06;

    @Embedded
    Arco arco;
    @Relation(parentColumn = "begin", entityColumn = "id")
    List<Beacon> begin;
    @Relation(parentColumn = "end", entityColumn = "id")
    List<Beacon> end;

    public Arco getArco() {
        return arco;
    }

    public List<Beacon> getBegin() {
        return begin;
    }

    public List<Beacon> getEnd() {
        return end;
    }

    public Beacon getBeginBeacon() {
        return begin.get(0);
    }

    public Beacon getEndBeacon() {
        return end.get(0);
    }

    public void setArco(Arco arco) {
        this.arco = arco;
    }

    public void setBegin(List<Beacon> begin) {
        this.begin = begin;
    }

    public void setEnd(List<Beacon> end) {
        this.end = end;
    }

    public void setBegin(Beacon begin) {
        this.begin.set(0, begin);
    }

    public void setEnd(Beacon end) {
        this.end.set(0, end);
    }

    public double getCosto(double normalizationBasis, boolean emergency) {
        double length = P_L * arco.getLength() / normalizationBasis;
        double other = (P_I * arco.getI()) +
                (P_C * arco.getC()) +
                (P_LOS * arco.getLos()) +
                (P_V * arco.getV());

        if (emergency) {
            return length + other;
        } else {
            return length;
        }
    }

    public boolean isConnecting(Beacon beacon1, Beacon beacon2) {
        boolean condition1, condition2;

        condition1 = begin.get(0).equals(beacon1) && end.get(0).equals(beacon2);
        condition2 = begin.get(0).equals(beacon2) && end.get(0).equals(beacon1);

        return (condition1 && !condition2) || (!condition1 && condition2);
    }

    public boolean isConnectedTo(Beacon beacon) {
        return begin.get(0).equals(beacon) || end.get(0).equals(beacon);
    }

    public boolean isConnectedTo(Tronco tronco) {
        return begin.get(0).equals(tronco.getBeginBeacon()) ||
                begin.get(0).equals(tronco.getEndBeacon()) ||
                end.get(0).equals(tronco.getBeginBeacon()) ||
                end.get(0).equals(tronco.getEndBeacon());
    }

    @Override
    public String toString() {
        return "Tronco{" +
                arco +
                '}';
    }
}
