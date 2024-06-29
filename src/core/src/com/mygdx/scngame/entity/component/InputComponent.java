package com.mygdx.scngame.entity.component;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.scngame.entity.Entity;

public interface InputComponent<T extends Entity> extends Disposable, InputProcessor {
    void update(T container);
}
