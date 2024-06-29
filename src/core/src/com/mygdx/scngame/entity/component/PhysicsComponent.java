package com.mygdx.scngame.entity.component;

import com.badlogic.gdx.utils.Disposable;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.Entity;

public interface PhysicsComponent<T extends Entity> extends Disposable {
    void update(T container, World<Object> world, float delta);
}
