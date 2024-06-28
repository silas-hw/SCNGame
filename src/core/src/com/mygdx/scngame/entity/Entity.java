package com.mygdx.scngame.entity;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.dongbat.jbump.World;

/**
 * Represents a single 'Entity' within the Game. An Entity is also a {@link InputProcessor input processor} and is
 * expected to be able to handle input events. It is recommended
 *
 */
public abstract class Entity implements Disposable, InputProcessor {
    public Vector2 position = new Vector2();
    public Vector2 direction = new Vector2();

    // used by Scene to remove dead entities without coupling Entity to Scene
    public boolean isDead = false;

    private final Array<InputProcessor> inputListeners = new Array<>();

    /**
     * An empty {@link EntityContextAdapter} such that if the context is not set there is still
     * a context that can be interacted with
     */
    public final static EntityContext NULL_CONTEXT = new EntityContextAdapter();
    public EntityContext context = NULL_CONTEXT;


    public Entity() {}

    /**
     * Invoked when the entity is to update its state and handle input
     *
     * @param world the {@link World physics world} for the Entity to interact with
     * @param delta the delta time since the last call to update
     */
    public void update(World<Object> world, float delta) {}

    /**
     *
     * @param batch the {@link SpriteBatch sprite batch} to draw sprites to
     * @param shape the {@link ShapeRenderer shape renderer} to draw shapes to
     * @param alpha
     */
    public void draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {}

    public void addListener(InputProcessor listener) {
        inputListeners.add(listener);
    }

    public void removeListener(InputProcessor listener) {
        inputListeners.removeValue(listener, false);
    }

    public void clearListeners() {
        inputListeners.clear();
    }

    // Propagate input events to all registered listeners

    @Override
    public boolean keyDown(int i) {
        boolean handled = false;

        for(InputProcessor listener : inputListeners) {
            handled = listener.keyDown(i) || handled;
        }

        return handled;
    }

    @Override
    public boolean keyUp(int i) {
        boolean handled = false;

        for(InputProcessor listener : inputListeners) {
            handled = listener.keyUp(i) || handled;
        }

        return handled;
    }

    @Override
    public boolean keyTyped(char c) {
        boolean handled = false;

        for(InputProcessor listener : inputListeners) {
            handled = listener.keyTyped(c) || handled;
        }

        return handled;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        boolean handled = false;

        for(InputProcessor listener : inputListeners) {
            handled = listener.touchDown(i, i1, i2, i3) || handled;
        }

        return handled;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        boolean handled = false;

        for(InputProcessor listener : inputListeners) {
            handled = listener.touchUp(i, i1, i2, i3) || handled;
        }

        return handled;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        boolean handled = false;

        for(InputProcessor listener : inputListeners) {
            handled = listener.touchCancelled(i, i1, i2, i3) || handled;
        }

        return handled;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        boolean handled = false;

        for(InputProcessor listener : inputListeners) {
            handled = listener.touchDragged(i, i1, i2) || handled;
        }

        return handled;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        boolean handled = false;

        for(InputProcessor listener : inputListeners) {
            handled = listener.mouseMoved(i, i1) || handled;
        }

        return handled;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        boolean handled = false;

        for(InputProcessor listener : inputListeners) {
            handled = listener.scrolled(v, v1) || handled;
        }

        return handled;
    }
}
