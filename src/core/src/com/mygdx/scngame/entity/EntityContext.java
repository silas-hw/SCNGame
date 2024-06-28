package com.mygdx.scngame.entity;

/**
 * The context for which a {@link Entity entity} may reside within. It is the point of contact
 * between an Entity and external state.
 *
 * TODO: add event handling/message bus so entities can communicate
 */
public interface EntityContext {
    void addEntity(Entity entity);
    void removeEntity(Entity entity);
    void clearEntities();
    boolean hasEntity(Entity entity);
}
