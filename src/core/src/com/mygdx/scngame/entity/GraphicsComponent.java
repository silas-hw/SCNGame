package com.mygdx.scngame.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface GraphicsComponent<T extends Entity> {
    void draw(T container, SpriteBatch batch, float alpha);
}
