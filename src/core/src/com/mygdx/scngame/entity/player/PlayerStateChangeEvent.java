package com.mygdx.scngame.entity.player;

import com.mygdx.scngame.event.GameEvent;

public class PlayerStateChangeEvent extends GameEvent {
    Player.PlayerState prevState;
    Player.PlayerState newState;

    public PlayerStateChangeEvent(Object source) {
        super(source);
    }
}
