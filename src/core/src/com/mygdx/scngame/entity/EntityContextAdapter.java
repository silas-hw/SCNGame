package com.mygdx.scngame.entity;

/**
 * A convenience implementation of {@link EntityContext entity context} so you can implement
 * whatever methods needed.
 */
public class EntityContextAdapter implements EntityContext {
    @Override
    public void addEntity(Entity entity) {

    }

    @Override
    public void removeEntity(Entity entity) {

    }

    @Override
    public void clearEntities() {

    }

    @Override
    public boolean hasEntity(Entity entity) {
        return false;
    }
}
