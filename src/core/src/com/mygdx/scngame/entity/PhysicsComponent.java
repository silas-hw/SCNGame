package com.mygdx.scngame.entity;

import com.dongbat.jbump.World;

public interface PhysicsComponent<T extends Entity> {
    void update(T container, World<?> world, float delta);
}
