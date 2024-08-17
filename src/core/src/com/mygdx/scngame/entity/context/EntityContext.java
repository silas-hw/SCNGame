package com.mygdx.scngame.entity.context;

import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.Entity;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.settings.Controls;

/**
 * The context for which a {@link Entity entity} may reside within. The context also captures input events
 * and can provide polling capabilities for its {@link Entity entities}. The idea is that you can wire this up to
 * some {@link com.badlogic.gdx.InputMultiplexer} to receive input events, allowing for multiplexed inputs (e.g.
 * between the player and UI elements) whilst still allowing input to be handled via polling.
 */
public interface EntityContext {

    void addEntity(Entity entity);
    void removeEntity(Entity entity);
    void clearEntities();
    boolean hasEntity(Entity entity);

    /**
     * sets the world for all held entities
     */
    void setWorld(World<Box> world);

    World<Box> getWorld();

    boolean isKeyPressed(int keycode);
    boolean isKeyJustPressed(int keycode);
    boolean isActionPressed(Controls control);
    boolean isActionJustPressed(Controls control);
}
