package com.ids.idsuserapp.wayfinding.directions;


public enum Actions {
    /**
     * Andare avanti
     */
    GO_AHEAD,

    /**
     * Girare a destra
     */
    TURN_RIGHT,

    /**
     * Girare a sinistra
     */
    TURN_LEFT,

    /**
     * Girare (curva a gomito) a destra
     */
    TURN_BACK_RIGHT,

    /**
     * Girare (curva a gomito) a sinistra
     */
    TURN_BACK_LEFT,

    /**
     * Salire le scale
     */
    GO_UPSTAIRS,

    /**
     * Scendere le scale
     */
    GO_DOWNSTAIRS,

    /**
     * Uscire (dall'aula dove ci si trova)
     */
    EXIT,

    /**
     * Destinazione raggiunta
     */
    DESTINATION_REACHED
}
