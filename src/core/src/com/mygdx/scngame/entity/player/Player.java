package com.mygdx.scngame.entity.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.*;
import com.mygdx.scngame.entity.context.EntityContext;
import com.mygdx.scngame.entity.player.states.PlayerMoveState;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.physics.HitBox;

// TODO: add comments
public class Player extends Entity {
    private EntityState<? super Player> state;

    public Item<Box> collisionItem;
    public Item<Box> hitbox;

    public float maxHealth = 500f;
    public float health = maxHealth;

    public Player() {
        Box foot;
        foot = new Box();
        foot.solid = true;
        foot.mask =  0b11000000;
        foot.response = Response.slide;

        collisionItem = new Item<>(foot);
        Box hit = new HitBox();
        hit.mask = 0b10000000;
        hit.response = Response.cross;
        hitbox = new Item<>(hit);

        this.state = new PlayerMoveState();
        this.state.setContainer(this);
    }

    @Override
    public void update(float delta) throws IllegalStateException {
        if(this.world == null) {
            throw new IllegalStateException("World must be set before calling update on Player");
        }

        EntityState<? super Player> newState =  state.update(delta);

        if(newState != null) {
            this.state.exit();

            this.state = newState;

            this.state.setContainer(this);
            this.state.setWorld(this.world);
            this.state.enter();
        }
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
        this.world.add(hitbox, position.x, position.y, 16, 32);
    }

    @Override
    public void dispose() {
        this.state.exit();
    }
}
