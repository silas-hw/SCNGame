package com.mygdx.scngame.entity.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.*;
import com.mygdx.scngame.SCNGame;
import com.mygdx.scngame.entity.EntityState;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.event.GlobalEventBus;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.physics.InteractBox;
import com.mygdx.scngame.settings.Controls;

import java.util.ArrayList;

public class PlayerMoveState implements EntityState<Player> {

    private float invisTimer = 0f;
    private final float invisTime = 0.3f;
    private boolean invis = false;
    
    protected Player container;
    protected World<Box> world;

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private Direction facing = Direction.LEFT;

    Vector2 rayStart = new Vector2();
    Vector2 rayEnd = new Vector2();

    @Override
    public EntityState<? super Player> update(float delta) {
        int dx = 0;
        int dy = 0;

        if(container.context.isKeyPressed(Controls.UP.getKeycode())) {
            dy++;
            facing = Direction.UP;
        }

        if(container.context.isKeyPressed(Controls.DOWN.getKeycode())) {
            dy--;
            facing = Direction.DOWN;
        }

        if(container.context.isKeyPressed(Controls.LEFT.getKeycode())) {
            dx--;
            facing = Direction.LEFT;
        }

        if(container.context.isKeyPressed(Controls.RIGHT.getKeycode())) {
            dx++;
            facing = Direction.RIGHT;
        }

        container.direction.set(dx, dy);
        container.direction.nor();

        if(container.context.isKeyJustPressed(Controls.DASH.getKeycode())) {
            return new PlayerDashState();
        }

        rayStart.y = container.position.y + container.HEIGHT/2;
        rayStart.x = container.position.x + container.WIDTH/2;
        rayEnd.x = rayStart.x;
        rayEnd.y = rayStart.y;

        switch(facing) {
            case UP:
                rayEnd.y += container.interactDistance;
                break;

            case DOWN:
                rayEnd.y += -container.interactDistance;
                break;

            case LEFT:
                rayEnd.x += -container.interactDistance;
                break;

            case RIGHT:
                rayEnd.x += container.interactDistance;
                break;

        }

        if(container.context.isKeyJustPressed(Controls.INTERACT.getKeycode())) {
            // query world for interact boxes

            ArrayList<Item> arr = new ArrayList<>();

            // ray cast from center of body
            world.querySegment(rayStart.x, rayStart.y, rayEnd.x, rayEnd.y, InteractBox.INTERACT_FILTER, arr);

            if(arr.size() > 0) {
                InteractBox interactBox = (InteractBox) arr.get(0).userData;
                interactBox.interact();
            }
        }

        container.position.mulAdd(container.direction, container.speed*delta);

        if(container.direction.isZero()) {
            container.position.x = MathUtils.round(container.position.x);
            container.position.y = MathUtils.round(container.position.y);
        }

        return null;
    }



    @Override
    public EntityState<? super Player> draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {


        batch.draw(container.texture, container.position.x, container.position.y);

        if(!Boolean.getBoolean("debugRender")) return null;

        // draw debug ray cast line

        batch.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.WHITE);

        shape.rectLine(rayStart, rayEnd, 1);

        shape.end();

        batch.begin();

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
