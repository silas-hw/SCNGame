package com.mygdx.scngame.entity.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Rect;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.*;
import com.mygdx.scngame.entity.component.HealthComponent;
import com.mygdx.scngame.entity.component.HurtBox;
import com.mygdx.scngame.entity.context.EntityContext;
import com.mygdx.scngame.entity.player.states.PlayerMoveState;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.physics.HitBox;

// TODO: add comments
public class Player extends Entity {
    private EntityState<? super Player> state;

    public Item<Box> collisionItem;
    public Item<Box> hitbox;

    public HealthComponent health;
    public HurtBox hurtbox;

    public Player() {
        Box foot;
        foot = new Box();
        foot.solid = true;
        foot.mask =  0b11000000;
        foot.response = Response.slide;

        collisionItem = new Item<>(foot);

        health = new HealthComponent(500f);
        hurtbox = new HurtBox(health, 0b11000000, 16, 32, 5f);

        this.state = new PlayerMoveState();
        this.state.setContainer(this);
    }

    @Override
    public void update(float delta) throws IllegalStateException {
        if(this.world == null) {
            throw new IllegalStateException("World must be set before calling update on Player");
        }

        EntityState<? super Player> newState =  state.update(delta);

        Response.Result res = world.move(collisionItem, position.x, position.y, Box.GLOBAL_FILTER);

        Rect rect = world.getRect(collisionItem);
        position.x = rect.x;
        position.y = rect.y;

        if(newState != null) {
            this.state.exit();

            this.state = newState;

            this.state.setContainer(this);
            this.state.setWorld(this.world);
            this.state.enter();
        }

        hurtbox.update(delta, this.position);

        System.out.println(health.getHealth());
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        state.draw( batch, shape, alpha);
    }

    @Override
    public void setWorld(World<Box> world) {
        this.state.setWorld(world);

        // if it's the first time settings the world, we can now enter the initial state
        if(this.world == null) {
            this.state.enter();
        }

        if(this.world != null) {
            if(this.world.hasItem(collisionItem)) this.world.remove(collisionItem);
            if(this.world.hasItem(hitbox)) this.world.remove(hitbox);
        }

        this.world = world;

        this.world.add(collisionItem, position.x, position.y, 16, 16);

        this.hurtbox.setWorld(world);
    }

    @Override
    public void dispose() {
        this.state.exit();
    }
}
