package com.mygdx.scngame.entity.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dongbat.jbump.World;
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

// TODO: add comments
public class Player extends Entity implements GameEventListener {
    public boolean INPUT_INTERACT;
    public boolean INPUT_UP;
    public boolean INPUT_DOWN;
    public boolean INPUT_LEFT;
    public boolean INPUT_RIGHT;
    public boolean INPUT_SHIFT;

    private int interactCount = 0;

    public boolean isDying = false;

    private EntityState<? super Player> _state;

    public Player(World<Box> world) {
        super(world);

        this._state = new PlayerMoveState();
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
        if(interactCount > 1) {
            INPUT_INTERACT = false;
        } else if (INPUT_INTERACT) {
            interactCount++;
        }

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

    private void clearInputs() {
        INPUT_INTERACT = false;
        INPUT_DOWN = false;
        INPUT_UP = false;
        INPUT_LEFT = false;
        INPUT_RIGHT = false;
        INPUT_SHIFT = false;
    }

    @Override
    public boolean keyDown(int keycode) {
        super.keyDown(keycode);

        switch(keycode) {
            case Input.Keys.W:
                INPUT_UP = true;
                break;

            case Input.Keys.S:
                INPUT_DOWN = true;
                break;

            case Input.Keys.D:
                INPUT_RIGHT = true;
                break;

            case Input.Keys.A:
                INPUT_LEFT = true;
                break;

            case Input.Keys.E:
                INPUT_INTERACT = true;
                interactCount = 0;
                break;

            case Input.Keys.SHIFT_LEFT:
                INPUT_SHIFT = true;
                break;

        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        super.keyUp(keycode);
        switch(keycode) {
            case Input.Keys.W:
                INPUT_UP = false;
                break;

            case Input.Keys.S:
                INPUT_DOWN = false;
                break;

            case Input.Keys.D:
                INPUT_RIGHT = false;
                break;

            case Input.Keys.A:
                INPUT_LEFT = false;
                break;

            case Input.Keys.E:
                INPUT_INTERACT = false;
                break;

            case Input.Keys.SHIFT_LEFT:
                INPUT_SHIFT = false;
                break;
        }

        return false;
    }

    @Override
    public void notify(GameEvent event) {
        if(event.getPayload() instanceof DialogStart) {
            clearInputs();
        }
    }
}
