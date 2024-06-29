package com.mygdx.scngame.entity.component;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.scngame.entity.Entity;

public interface GraphicsComponent<T extends Entity> extends Disposable {
    void draw(T container, SpriteBatch batch, ShapeRenderer shape, float alpha);
}
