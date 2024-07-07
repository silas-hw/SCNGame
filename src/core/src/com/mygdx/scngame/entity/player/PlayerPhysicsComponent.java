package com.mygdx.scngame.entity.player;

import com.dongbat.jbump.*;
import com.mygdx.scngame.entity.component.PhysicsComponent;
import com.mygdx.scngame.event.GameEvent;
import com.mygdx.scngame.event.Global;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.physics.HitBox;

public class PlayerPhysicsComponent implements PhysicsComponent<Player> {

    private Item<Box> collisionItem;
    private Item<Box> hitbox;

    public PlayerPhysicsComponent(Player player) {
        Box foot = new Box();
        foot.solid = true;
        foot.mask = (byte) 0b10000000;
        foot.response = Response.slide;

        collisionItem = new Item<>(foot);
        hitbox = new Item<>(new HitBox());

        Global.bus.addEventListener(this);
    }

    private float dashTimer = 0f;
    private final float dashTime = 0.2f;
    private final float dashDist = 400f;

    @Override
    public void update(Player container, World<Box> world, float delta) {
        if(container.isDying) {
            if(world.hasItem(collisionItem)) world.remove(collisionItem);
            if(world.hasItem(hitbox)) world.remove(hitbox);
            container.isDead = true;
            return;
        }

        if(!world.hasItem(collisionItem)) {
            world.add(collisionItem, container.position.x, container.position.y, 16, 32);
        }

        if(!world.hasItem(hitbox)) {
            world.add(hitbox, container.position.x, container.position.y, 32, 64);
        }

        switch(container.getState()) {
            case MOVING:
                container.position.mulAdd(container.direction, 500f*delta);
                break;

            case DASHING:
                dashTimer += delta;
                container.position.mulAdd(container.direction, (dashDist/dashTime)*delta);

                if(dashTimer >= dashTime) {
                    dashTimer = 0f;
                    container.setState(Player.PlayerState.MOVING);
                }

                break;
        }


        world.move(collisionItem, container.position.x, container.position.y, Box.GLOBAL_FILTER);
        Rect rect = world.getRect(collisionItem);
        container.position.x = rect.x;
        container.position.y = rect.y;

        world.move(hitbox, container.position.x, container.position.y, Box.GLOBAL_FILTER);
    }

    @Override
    public void dispose() {
        Global.bus.removeEventListener(this);
    }

    @Override
    public void notify(GameEvent event) {
        if(event instanceof PlayerStateChangeEvent) {
            System.out.println("Player changing state! Detected by physics component");
        }
    }
}
