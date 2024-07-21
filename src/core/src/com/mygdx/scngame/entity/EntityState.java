package com.mygdx.scngame.entity;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.dongbat.jbump.World;
import com.mygdx.scngame.physics.Box;

public interface EntityState<T extends Entity> extends InputProcessor, Disposable {
    EntityState<? super T> update(float delta);
    EntityState<? super T> draw(SpriteBatch batch, ShapeRenderer shape, float alpha);

    void setContainer(T container);
    void setWorld(World<Box> world);

    void enter();
    void exit();
}
