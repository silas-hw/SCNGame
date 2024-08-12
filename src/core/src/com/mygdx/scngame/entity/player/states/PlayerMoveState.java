package com.mygdx.scngame.entity.player.states;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dongbat.jbump.*;
import com.mygdx.scngame.SCNGame;
import com.mygdx.scngame.entity.EntityState;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.event.GlobalEventBus;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.settings.Controls;

public class PlayerMoveState implements EntityState<Player> {

    private float invisTimer = 0f;
    private final float invisTime = 0.3f;
    private boolean invis = false;
    
    protected Player container;
    protected World<Box> world;

    @Override
    public EntityState<? super Player> update(float delta) {
        int dx = 0;
        int dy = 0;

        if(container.context.isKeyPressed(Controls.UP.getKeycode())) {
            dy++;
        }

        if(container.context.isKeyPressed(Controls.DOWN.getKeycode())) {
            dy--;
        }

        if(container.context.isKeyPressed(Controls.LEFT.getKeycode())) {
            dx--;
        }

        if(container.context.isKeyPressed(Controls.RIGHT.getKeycode())) {
            dx++;
        }

        container.direction.set(dx, dy);
        container.direction.nor();

        if(container.context.isKeyJustPressed(Controls.DASH.getKeycode())) {
            return new PlayerDashState();
        }

        if(container.context.isKeyJustPressed(Input.Keys.E)) {
            GlobalEventBus.getInstance().startDialog("test_dialog_1");
        }

        container.position.mulAdd(container.direction, 500f*delta);

        return null;
    }

    @Override
    public EntityState<? super Player> draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        batch.draw(container.texture, container.position.x, container.position.y);
        return null;
    }

    @Override
    public void setContainer(Player container) {
        this.container = container;
    }

    @Override
    public void setWorld(World<Box> world) {
        this.world = world;
    }

    @Override
    public void enter() {
        container.hurtbox.setTakesDamage(true);
    }

    @Override
    public void exit() {}
}
