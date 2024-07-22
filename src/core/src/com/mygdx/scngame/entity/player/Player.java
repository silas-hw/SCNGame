package com.mygdx.scngame.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.mygdx.scngame.dialog.DialogEnd;
import com.mygdx.scngame.dialog.DialogStart;
import com.mygdx.scngame.entity.*;
import com.mygdx.scngame.entity.component.GraphicsComponent;
import com.mygdx.scngame.entity.component.InputComponent;
import com.mygdx.scngame.entity.component.PhysicsComponent;
import com.mygdx.scngame.entity.context.EntityContext;
import com.mygdx.scngame.entity.player.states.PlayerDashState;
import com.mygdx.scngame.entity.player.states.PlayerMoveState;
import com.mygdx.scngame.event.GameEvent;
import com.mygdx.scngame.event.GameEventListener;
import com.mygdx.scngame.event.Global;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.physics.HitBox;

import java.util.Arrays;

// TODO: add comments
public class Player extends Entity {
    private EntityState<? super Player> _state;

    public Item<Box> collisionItem;
    public Item<Box> hitbox;

    public Player() {
        Box foot;
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
    }

    @Override
    public void init(World<Box> world, EntityContext context) {
        super.init(world, context);

        if(this._state != null) {
            this._state.exit();
            this._state.dispose();
        }

        this._state = new PlayerMoveState();
        this._state.enter(world, this);

        setWorld(world);
    }

    @Override
    public void update(float delta) {
        EntityState<? super Player> newState =  _state.update(delta);

        if(newState != null) {
            this._state.exit();
            this._state = newState;
            this._state.enter(world, this);
        }
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        _state.draw( batch, shape, alpha);
    }

    @Override
    public void setWorld(World<Box> world) {
        this._state.setWorld(world);

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

    }
}
