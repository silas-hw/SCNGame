package com.mygdx.scngame.entity;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.scngame.entity.player.Player;

public interface InputComponent<T extends Entity> extends Disposable, InputProcessor {
    void update(T container);
}
