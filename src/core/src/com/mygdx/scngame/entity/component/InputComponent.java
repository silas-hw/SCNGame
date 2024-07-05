package com.mygdx.scngame.entity.component;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.scngame.entity.Entity;
import com.mygdx.scngame.event.GameEventListener;

public interface InputComponent<T extends Entity> extends Disposable, InputProcessor, GameEventListener {
    void update(T container);
}
