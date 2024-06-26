package com.mygdx.scngame.entity;

public interface EntityContext {
    void addEntity(Entity entity);
    void removeEntity(Entity entity);
    void clearEntities();
    boolean hasEntity(Entity entity);
}
