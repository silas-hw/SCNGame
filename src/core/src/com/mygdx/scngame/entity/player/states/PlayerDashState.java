package com.mygdx.scngame.entity.player.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dongbat.jbump.Rect;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.EntityState;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.physics.Box;

public class PlayerDashState implements EntityState<Player> {

    private float dashTimer = 0f;
    private final float dashTime = 0.2f;
    private final float dashDist = 400f;

    protected Player container;
    protected World<Box> world;

    @Override
    public EntityState<? super Player> update(float delta) {

        dashTimer += delta;
        container.position.mulAdd(container.direction, (dashDist/dashTime)*delta);

        if(dashTimer >= dashTime) {
            return new PlayerMoveState();
        }

        Response.Result res = world.move(container.collisionItem, container.position.x, container.position.y, Box.GLOBAL_FILTER);

        Rect rect1 = world.getRect(container.collisionItem);
        container.position.x = rect1.x;
        container.position.y = rect1.y;

        world.move(container.hitbox, container.position.x, container.position.y, Box.GLOBAL_FILTER);
        return null;
    }

    @Override
    public EntityState<? super Player> draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        return null;
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
    public void enter() {
        container.collisionItem.userData.response = Response.touch;
        dashTimer = 0f;
    }

    @Override
    public void exit() {
        container.collisionItem.userData.response = Response.slide;
    }
}
