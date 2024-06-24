package com.mygdx.scngame.entity;

import com.mygdx.scngame.entity.player.Player;

public interface InputComponent<T extends Entity> {
    void update(T container);
}
