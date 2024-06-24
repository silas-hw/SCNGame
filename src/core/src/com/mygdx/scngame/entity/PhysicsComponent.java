package com.mygdx.scngame.entity;

import com.badlogic.gdx.utils.Disposable;
import com.dongbat.jbump.World;

public interface PhysicsComponent<T extends Entity> extends Disposable {
    void update(T container, World<?> world, float delta);
}
