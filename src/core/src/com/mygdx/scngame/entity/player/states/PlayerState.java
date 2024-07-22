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
    protected World<Box> world;

    protected Player container;

    private Texture texture;

    public PlayerState() {
        texture = new Texture(Gdx.files.internal("sprites/test.png"));
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
    public void enter(World<Box> world, Player container) {
        setWorld(world);
        setContainer(container);
    }

    @Override
    public void exit() {
    }

    @Override
    public void setWorld(World<Box> world) {
        this.world = world;
    }

    @Override
    public void setContainer(Player container) {
        this.container = container;
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
