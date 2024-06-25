package com.mygdx.scngame.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.scngame.entity.Entity;
import com.mygdx.scngame.entity.GraphicsComponent;

public class PlayerGraphicsComponent implements GraphicsComponent<Entity> {
    Texture texture;

    public PlayerGraphicsComponent(Texture texture) {
        this.texture = texture;
    }
    @Override
    public void draw(Entity container, SpriteBatch batch, ShapeRenderer shape, float alpha) {
        batch.draw(texture, container.position.x, container.position.y);
    }

    @Override
    public void dispose() {
    }
}
