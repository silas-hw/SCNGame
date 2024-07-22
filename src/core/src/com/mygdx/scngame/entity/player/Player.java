package com.mygdx.scngame.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

import java.util.Arrays;

// TODO: add comments
public class Player extends Entity {
    private EntityState<? super Player> _state;

    @Override
    public void init(World<Box> world, EntityContext context) {
        super.init(world, context);

        this._state = new PlayerMoveState();
        this._state.enter(world, this);
    }

    @Override
    public void setWorld(World<Box> world) {
        super.setWorld(world);
        this._state.setWorld(world);
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
    public void dispose() {

    }
}
