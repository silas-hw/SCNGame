package com.mygdx.scngame.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dongbat.jbump.World;
import com.mygdx.scngame.physics.Box;

public class StateManager<T extends Entity> {

    EntityState<T> startingState;
    EntityState<? super T> currentState;

    T container;
    World<Box> world;

    public StateManager(EntityState<T> startingState, T container) {
        this.startingState = startingState;

        this.startingState.setContainer(container);

        this.currentState = this.startingState;

        this.container = container;
    }

    public void update(float delta) {
        EntityState<? super T> newState = currentState.update(delta);

        if(newState != null) {
            this.setState(newState);
        }
    }

    public void draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        this.currentState.draw(batch, shape, alpha);
    }

    public void drawWaterReflection(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        this.currentState.drawWaterReflection(batch, shape, alpha);
    }

    public void setWorld(World<Box> world) {
        this.currentState.setWorld(world);

        if(this.world == null) {
            this.currentState.enter();
        }

        this.world = world;
    }

    public void removeWorldItems() {
        this.currentState.removeWorldItems();
    }

    public void exit() {
        this.currentState.exit();
    }

    public void setState(EntityState<? super T> newState) {
        currentState.exit();

        currentState = newState;
        currentState.setContainer(container);
        currentState.setWorld(world);

        currentState.enter();
    }

    public void reset() {
        this.currentState.exit();

        this.currentState = this.startingState;
        this.currentState.setContainer(container);
        this.currentState.setWorld(world);
        this.currentState.enter();
    }
}
