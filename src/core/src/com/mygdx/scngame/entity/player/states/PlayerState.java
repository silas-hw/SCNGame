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

public class PlayerState extends InputAdapter implements EntityState<Player> {

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
        return null;
    }

    @Override
    public EntityState<? super Player> draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        batch.draw(texture, container.position.x, container.position.y);
        return null;
    }

    @Override
    public void setContainer(Player container) {
        this.container = container;
    }

    @Override
    public void enter() {
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
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
