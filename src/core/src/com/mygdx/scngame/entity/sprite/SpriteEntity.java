package com.mygdx.scngame.entity.sprite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.scngame.entity.Entity;

public class SpriteEntity extends Entity {

    private TextureRegion texture;
    
    public SpriteEntity(TextureRegion texture, float x, float y) {
        this.texture = texture;
        position.set(x, y);
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        batch.draw(texture, position.x, position.y);
    }

    @Override
    public void dispose() {

    }
}
