package com.mygdx.scngame.entity.sprite;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.scngame.entity.Entity;

public class AnimatedSpriteEntity extends Entity {

    private Animation<TextureRegion> animation;
    private Vector2 position = new Vector2();

    public AnimatedSpriteEntity(Animation<TextureRegion> anim, float x, float y) {
        animation = anim;
        this.animation.setPlayMode(Animation.PlayMode.LOOP);

        position.set(x, y);
    }

    public AnimatedSpriteEntity(StaticTiledMapTile[] tiles, float interval, float x, float y) {
        Array<TextureRegion> frames = new Array<>();

        for(StaticTiledMapTile tile : tiles) {
            frames.add(tile.getTextureRegion());
        }

        this.animation = new Animation<>(interval, frames);
        this.animation.setPlayMode(Animation.PlayMode.LOOP);

        position.set(x, y);
    }

    // randomise the start time
    private float stateTime = (float) Math.random();

    @Override
    public void update(float delta) {
        stateTime += delta;
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        batch.draw(animation.getKeyFrame(stateTime), position.x, position.y);
    }

    @Override
    public void dispose() {

    }
}
