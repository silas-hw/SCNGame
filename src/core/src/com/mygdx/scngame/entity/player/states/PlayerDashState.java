package com.mygdx.scngame.entity.player.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dongbat.jbump.Rect;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.EntityState;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.physics.Box;

public class PlayerDashState extends PlayerState {

    private float dashTimer = 0f;
    private final float dashTime = 0.2f;
    private final float dashDist = 400f;

    @Override
    public EntityState<? super Player> update(float delta) {
        super.update(delta);

        dashTimer += delta;
        container.position.mulAdd(container.direction, (dashDist/dashTime)*delta);

        if(dashTimer >= dashTime) {
            return new PlayerMoveState();
        }

        Response.Result res = world.move(collisionItem, container.position.x, container.position.y, Box.GLOBAL_FILTER);

        Rect rect1 = world.getRect(collisionItem);
        container.position.x = rect1.x;
        container.position.y = rect1.y;

        world.move(hitbox, container.position.x, container.position.y, Box.GLOBAL_FILTER);
        return null;
    }

    @Override
    public void enter(World<Box> world, Player container) {
        super.enter(world, container);
        collisionItem.userData.response = Response.touch;

        dashTimer = 0f;
    }

    @Override
    public void exit() {
        super.exit();
        collisionItem.userData.response = Response.slide;
    }
}
