package com.ids.idsuserapp.percorso;


import android.graphics.PointF;

public interface Checkpoint {
    String getFloor();

    int getFloorInt();

    boolean isGeneral();

    boolean isRoom();

    boolean isExit();

    boolean isEmergencyExit();

    PointF toPointF();

    int getX();

    int getY();
}
