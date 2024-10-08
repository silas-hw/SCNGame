package com.mygdx.scngame.entity.context;

import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.Entity;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.controls.Controls;

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

    @Override
    public void setWorld(World<Box> world) {

    }

    @Override
    public World<Box> getWorld() {
        return null;
    }

    @Override
    public boolean isActionPressed(Controls.Actions control) {
        return false;
    }

    @Override
    public boolean isActionJustPressed(Controls.Actions control) {
        return false;
    }


}
