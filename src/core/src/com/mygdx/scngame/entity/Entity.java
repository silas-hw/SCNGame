package com.mygdx.scngame.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.World;

public abstract class Entity {
    public Vector2 position;

    public Entity() {

    }

    public void update(World<?> world, float delta) {
        return;
    }

    public void draw(SpriteBatch batch, float alpha) {
        return;
    }
}
