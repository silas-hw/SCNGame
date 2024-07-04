package com.mygdx.scngame.entity.component;

import com.badlogic.gdx.utils.Disposable;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.Entity;
import com.mygdx.scngame.physics.Box;

public interface PhysicsComponent<T extends Entity> extends Disposable {
    void update(T container, World<Box> world, float delta);
}
