package com.mygdx.scngame.entity.player;

import com.dongbat.jbump.*;
import com.mygdx.scngame.entity.component.PhysicsComponent;
import com.mygdx.scngame.event.GameEvent;
import com.mygdx.scngame.event.Global;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.physics.DamageBox;
import com.mygdx.scngame.physics.HitBox;

public class PlayerPhysicsComponent implements PhysicsComponent<Player> {

    private Item<Box> collisionItem;
    private Item<Box> hitbox;

    Box foot;

    private float health = 500f;

    public PlayerPhysicsComponent(Player player) {
        foot = new Box();
        foot.solid = true;
        foot.mask = (byte) 0b11000000;
        //foot.layer = (byte) 0b01000000;
        foot.response = Response.slide;

        collisionItem = new Item<>(foot);
        Box hit = new HitBox();
        hit.mask = (byte) 0b10000000;
        hit.response = Response.cross;
        hitbox = new Item<>(hit);

        Global.bus.addEventListener(this);
    }

    private float dashTimer = 0f;
    private final float dashTime = 0.2f;
    private final float dashDist = 400f;

    private float invisTimer = 0f;
    private final float invisTime = 0.3f;
    private boolean invis = false;

    @Override
    public void update(Player container, World<Box> world, float delta) {
        System.out.println("Health: " + health);
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

                world.move(collisionItem, container.position.x, container.position.y, Box.GLOBAL_FILTER);
                Rect rect = world.getRect(collisionItem);
                container.position.x = rect.x;
                container.position.y = rect.y;

                Response.Result res = world.move(hitbox, container.position.x, container.position.y, Box.GLOBAL_FILTER);

                if(invis && invisTimer < invisTime) {
                    invisTimer += delta;
                    break;
                } else if(invis) {
                    invis = false;
                    invisTimer = 0f;
                }

                for(int i = 0; i<res.projectedCollisions.size(); i++) {
                    Collision col = res.projectedCollisions.get(i);

                    if(col.other.userData instanceof DamageBox) {
                        DamageBox dBox = (DamageBox) col.other.userData;

                        health -= dBox.damage;
                        invis = true;
                    }

                }
                break;

            case DASHING:
                dashTimer += delta;
                container.position.mulAdd(container.direction, (dashDist/dashTime)*delta);

                if(dashTimer >= dashTime) {
                    dashTimer = 0f;
                    container.setState(Player.PlayerState.MOVING);
                }

                world.move(collisionItem, container.position.x, container.position.y, Box.GLOBAL_FILTER);
                Rect rect1 = world.getRect(collisionItem);
                container.position.x = rect1.x;
                container.position.y = rect1.y;

                world.move(hitbox, container.position.x, container.position.y, Box.GLOBAL_FILTER);

                break;
        }



    }

    @Override
    public void dispose() {
        Global.bus.removeEventListener(this);
    }

    @Override
    public void notify(GameEvent event) {
        if(event instanceof PlayerStateChangeEvent) {
            switch(((PlayerStateChangeEvent) event).newState) {
                // stick player to wall if they collide whilst dashing
                case DASHING:
                    foot.response = Response.touch;
                    break;

                default:
                    foot.response = Response.slide;
            }
        }
    }
}
