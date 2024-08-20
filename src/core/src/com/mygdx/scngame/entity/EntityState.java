package com.mygdx.scngame.entity;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.dongbat.jbump.World;
import com.mygdx.scngame.physics.Box;

public interface EntityState<T extends Entity> {

    /**
     * Tells the state to perform any physics or input logic.
     *
     * <p>
     *
     * The {@link World} and {@link Entity containing entity} must be set prior to calling update,
     * as well as calling <code>enter()</code>.
     *
     * @return A new state for the containing {@link Entity} to switch to, or <code>null</code> if
     * the state is to remain the same
     */
    EntityState<? super T> update(float delta);

    /**
     * Tells the state to render itself.
     *
     * <p>
     *
     * The {@link World} and {@link Entity containing entity} must be set prior to calling draw, as
     * well as calling enter.
     *
     * @return A new state for the containing {@link Entity} to switch to, or <code>null</code> if
     * the state is to remain the same
     */
    EntityState<? super T> draw(SpriteBatch batch, ShapeRenderer shape, float alpha);

    void drawWaterReflection(SpriteBatch batch, ShapeRenderer shape, float alpha);

    void setContainer(T container);

    /**
     * Sets the physics world for this state. Objects will only be added or removed from
     * the world if this state is currently active, otherwise the state will wait for
     * <code>enter()</code>) to be called to add physics objects.
     *
     * @param world
     */
    void setWorld(World<Box> world);

    /**
     * Marks the state as active, resetting its internal state, potenitally
     * adding physics objects to the {@link World}, and changing the external state
     * of the container
     */
    void enter();

    /**
     * Marks the state as inactive, resetting its internal state, removing any
     * added physics object from the {@link World}, and undoing changes to the external
     * state of the container
     */
    void exit();
}
