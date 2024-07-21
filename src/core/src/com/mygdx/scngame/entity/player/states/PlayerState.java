package com.mygdx.scngame.entity.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.mygdx.scngame.dialog.DialogStart;
import com.mygdx.scngame.entity.EntityState;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.event.GameEvent;
import com.mygdx.scngame.event.GameEventListener;
import com.mygdx.scngame.event.Global;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.physics.HitBox;

public class PlayerState extends InputAdapter implements EntityState<Player>, GameEventListener {
    protected static boolean INPUT_UP;
    protected static boolean INPUT_DOWN;
    protected static boolean INPUT_RIGHT;
    protected static boolean INPUT_LEFT;
    protected static boolean INPUT_SHIFT;

    protected static boolean INPUT_INTERACT;
    private int interactCount = 0;

    protected Item<Box> collisionItem;
    protected Item<Box> hitbox;

    protected World<Box> world;

    protected Player container;

    Texture texture;

    public PlayerState() {
        texture = new Texture(Gdx.files.internal("sprites/test.png"));

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
    public EntityState<? super Player> update(float delta) {

        // only allow interact inputs to be captured within 1 frame of the input
        if(interactCount > 1) {
            INPUT_INTERACT = false;
        }
        else if(INPUT_INTERACT) {
            interactCount++;
        }

        return null;
    }

    @Override
    public EntityState<? super Player> draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        batch.draw(texture, container.position.x, container.position.y);
        return null;
    }

    @Override
    public void setContainer(Player container) {
        if(this.container != null) this.container.removeInputListener(this);

        this.container = container;

        this.container.addInputListener(this);
    }

    @Override
    public void enter() {
        Global.bus.addEventListener(this);
    }

    @Override
    public void setWorld(World<Box> world) {
        if(this.world != null) {
            if(this.world.hasItem(collisionItem)) this.world.remove(collisionItem);
            if(this.world.hasItem(hitbox)) this.world.remove(hitbox);
        }

        this.world = world;

        this.world.add(collisionItem, container.position.x, container.position.y, 16, 16);
        this.world.add(hitbox, container.position.x, container.position.y, 16, 32);
    }

    @Override
    public void exit() {
        world.remove(collisionItem);
        world.remove(hitbox);

        Global.bus.removeEventListener(this);
    }

    protected void clearInputs() {
        INPUT_DOWN = false;
        INPUT_UP = false;
        INPUT_LEFT = false;
        INPUT_RIGHT = false;
        INPUT_SHIFT = false;
        INPUT_INTERACT = false;
    }

    @Override
    public void dispose() {
        texture.dispose();
        container.removeInputListener(this);
    }

    @Override
    public boolean keyUp(int keycode) {
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
    public boolean keyDown(int keycode) {
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
    public void notify(GameEvent event) {
        if(event.getPayload() instanceof DialogStart) {
            clearInputs();
        }
    }
}
