package com.ids.idsuserapp.percorso.directions;

import android.content.Context;


import com.ids.idsuserapp.R;
import com.ids.idsuserapp.wayfinding.directions.Actions;

import java.io.Serializable;

public class HumanDirection implements Serializable {
    private String direction;
    private int iconResource;

    public HumanDirection(String direction, int iconResource) {
        this.direction = direction;
        this.iconResource = iconResource;
    }

    public HumanDirection() {
    }

    public static HumanDirection createHumanDirection(Context context, Actions action) {
        HumanDirection humanDirection = new HumanDirection();
        switch (action) {
            case GO_AHEAD: {
                humanDirection.setDirection(context.getString(R.string.action_go_ahead));
                humanDirection.setIconResource(R.drawable.ic_go_ahead);
                break;
            }
            case TURN_RIGHT: {
                humanDirection.setDirection(context.getString(R.string.action_turn_right));
                humanDirection.setIconResource(R.drawable.ic_turn_right);
                break;
            }
            case TURN_LEFT: {
                humanDirection.setDirection(context.getString(R.string.action_turn_left));
                humanDirection.setIconResource(R.drawable.ic_turn_left);
                break;
            }
            case TURN_BACK_RIGHT: {
                humanDirection.setDirection(context.getString(R.string.action_turn_back_right));
                humanDirection.setIconResource(R.drawable.ic_turn_back_right);
                break;
            }
            case TURN_BACK_LEFT: {
                humanDirection.setDirection(context.getString(R.string.action_turn_back_left));
                humanDirection.setIconResource(R.drawable.ic_turn_back_left);
                break;
            }
            case GO_UPSTAIRS: {
                humanDirection.setDirection(context.getString(R.string.action_go_upstairs));
                humanDirection.setIconResource(R.drawable.ic_go_upstairs);
                break;
            }
            case GO_DOWNSTAIRS: {
                humanDirection.setDirection(context.getString(R.string.action_go_downstairs));
                humanDirection.setIconResource(R.drawable.ic_go_downstairs);
                break;
            }
            case EXIT: {
                humanDirection.setDirection(context.getString(R.string.action_exit));
                humanDirection.setIconResource(R.drawable.ic_exit);
                break;
            }
            case DESTINATION_REACHED: {
                humanDirection.setDirection(context.getString(R.string.action_destination_reached));
                humanDirection.setIconResource(R.drawable.ic_flag);
                break;
            }
        }
        return humanDirection;
    }

    public String getDirection() {

        return direction;
    }

    public HumanDirection setDirection(String direction) {
        this.direction = direction;
        return this;
    }

    public int getIconResource() {
        return iconResource;
    }

    public HumanDirection setIconResource(int iconResource) {
        this.iconResource = iconResource;
        return this;
    }

    @Override
    public String toString() {
        return "HumanDirection{" +
                "direction='" + direction + '\'' +
                ", iconResource=" + iconResource +
                '}';
    }
}
