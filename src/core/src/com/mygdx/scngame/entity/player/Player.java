package com.mygdx.scngame.entity.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.*;
import com.mygdx.scngame.entity.component.GraphicsComponent;
import com.mygdx.scngame.entity.component.InputComponent;
import com.mygdx.scngame.entity.component.PhysicsComponent;
import com.mygdx.scngame.entity.context.EntityContext;
import com.mygdx.scngame.entity.player.states.PlayerDashState;
import com.mygdx.scngame.entity.player.states.PlayerMoveState;
import com.mygdx.scngame.event.GameEvent;
import com.mygdx.scngame.event.Global;
import com.mygdx.scngame.physics.Box;

// TODO: add comments
public class Player extends Entity {
    public boolean isDying = false;

    private EntityState<? super Player> _state;

    public final PlayerMoveState moveState = new PlayerMoveState();
    public final PlayerDashState dashState = new PlayerDashState();

    public Player(World<Box> world) {
        super(world);

        this._state = moveState;
        this._state.setContainer(this);
        this._state.setWorld(world);
        this._state.enter();
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

            this._state.setContainer(this);
            this._state.setWorld(this.world);

            this._state.enter();
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
