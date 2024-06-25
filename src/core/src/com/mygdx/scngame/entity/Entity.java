package com.mygdx.scngame.entity;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.dongbat.jbump.World;

public abstract class Entity implements Disposable, InputProcessor {
    public Vector2 position = new Vector2();
    public Vector2 direction = new Vector2();

    private final Array<InputProcessor> inputListeners = new Array<>();

    public Entity() {}

    public void update(World<Object> world, float delta) {}

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
