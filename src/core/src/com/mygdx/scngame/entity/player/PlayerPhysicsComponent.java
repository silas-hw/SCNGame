package com.mygdx.scngame.entity.player;

import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.PhysicsComponent;
import com.mygdx.scngame.physics.HitBox;

public class PlayerPhysicsComponent implements PhysicsComponent<Player> {

    private Item<Object> collisionItem;
    private Item<Object> hitbox;

    private final CollisionFilter FILTER = new Filter();

    public PlayerPhysicsComponent(Player player) {
        collisionItem = new Item<>(player);
        hitbox = new Item<>(new HitBox());
    }

    @Override
    public void update(Player container, World<Object> world, float delta) {
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

        container.position.mulAdd(container.direction, 500f*delta);

        world.move(collisionItem, container.position.x, container.position.y, FILTER);
        world.move(hitbox, container.position.x, container.position.y, FILTER);
    }

    @Override
    public void dispose() {
    }

    public static class Filter implements CollisionFilter {

        @Override
        public Response filter(Item item, Item item1) {
            return null;
        }
    }
}
