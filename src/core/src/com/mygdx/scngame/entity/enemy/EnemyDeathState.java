package com.mygdx.scngame.entity.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.EntityState;
import com.mygdx.scngame.physics.Box;

public final class EnemyDeathState implements EntityState<Enemy> {

    Enemy enemy;
    float stateTime;
    float animDuration = 0f;

    @Override
    public EntityState<? super Enemy> update(float delta) {
        stateTime += delta;

        if(stateTime >= animDuration) {
            enemy.context.removeEntity(enemy);
        }

        return null;
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        TextureAtlas.AtlasRegion frame = enemy.type.deathAnimation().getKeyFrame(stateTime, true);

        float offsetx = (frame.getRegionWidth() - enemy.type.width())/2f;
        float offsety = (frame.getRegionHeight() - enemy.type.height())/2f;

        batch.draw(frame, enemy.position.x - offsetx, enemy.position.y - offsety);
    }

    @Override
    public void drawWaterReflection(SpriteBatch batch, ShapeRenderer shape, float alpha) {

    }

    @Override
    public void setContainer(Enemy container) {
        enemy = container;

        animDuration = enemy.type.deathAnimation().getAnimationDuration();
    }

    @Override
    public void setWorld(World<Box> world) {

    }

    @Override
    public void removeWorldItems() {

    }

    @Override
    public void enter() {
        enemy.hurtBox.setTakesDamage(false);
    }

    @Override
    public void exit() {

    }
}
