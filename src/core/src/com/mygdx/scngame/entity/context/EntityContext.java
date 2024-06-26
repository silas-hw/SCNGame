package com.mygdx.scngame.entity.context;

import com.mygdx.scngame.entity.Entity;

/**
 * The context for which a {@link Entity entity} may reside within. It is the point of contact
 * between an Entity and external state.
 */
public interface EntityContext {
    // TODO: add event handling/message bus so entities can communicate
    void addEntity(Entity entity);
    void removeEntity(Entity entity);
    void clearEntities();
    boolean hasEntity(Entity entity);
}
