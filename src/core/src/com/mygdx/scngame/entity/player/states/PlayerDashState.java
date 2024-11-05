package com.mygdx.scngame.entity.player.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.EntityState;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.physics.Box;

public class PlayerDashState implements EntityState<Player> {

    private float dashTimer = 0f;

    protected Player container;
    protected World<Box> world;

    @Override
    public EntityState<? super Player> update(float delta) {

        dashTimer += delta;

        float dashTime = 0.2f;
        float dashDist = 65f;
        container.position.mulAdd(container.direction, (dashDist / dashTime)*delta);

        if(dashTimer >= dashTime) {
            return new PlayerDefaultState();
        }

        return null;
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        batch.setColor(0.9f, 0.4f, 0.4f, 0.8f);
        batch.draw(container.texture, container.position.x, container.position.y);
        batch.setColor(1.0f, 1.0f, 1.0f, 1f);
    }

    @Override
    public void drawWaterReflection(SpriteBatch batch, ShapeRenderer shape, float alpha) {

    }

    @Override
    public void setContainer(Player container) {
        this.container = container;
    }

    @Override
    public void setWorld(World<Box> world) {
        this.world = world;
    }

    @Override
    public void removeWorldItems() {

    }

    @Override
    public void enter() {
        container.collisionItem.userData.internalFilter = Box.TOUCH_FILTER;
        dashTimer = 0f;

        container.hurtbox.setTakesDamage(false);
    }

    @Override
    public void exit() {
        container.collisionItem.userData.internalFilter = Box.SLIDE_FILTER;
    }
}
