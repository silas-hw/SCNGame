package com.mygdx.scngame.entity.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

        if(container.context.isKeyJustPressed(Controls.INTERACT.getKeycode())) {
            // query world for interact boxes
            float y = container.position.y + container.HEIGHT/2;
            float x = container.position.x + container.WIDTH/2;
            float dirX = x;
            float dirY = y;

            switch(facing) {
                case UP:
                    dirY += container.interactDistance;
                    break;

                case DOWN:
                    dirY += -container.interactDistance;
                    break;

                case LEFT:
                    dirX += -container.interactDistance;
                    break;

                    case RIGHT:
                        dirX += container.interactDistance;
                        break;

            }
            ArrayList<Item> arr = new ArrayList<>();

            // ray cast from center of body
            world.querySegment(x, y, dirX, dirY, InteractBox.INTERACT_FILTER, arr);

            if(arr.size() > 0) {
                InteractBox interactBox = (InteractBox) arr.get(0).userData;
                interactBox.interact();
            }
        }

        container.position.mulAdd(container.direction, container.speed*delta);

        return null;
    }



    @Override
    public EntityState<? super Player> draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        batch.draw(container.texture, container.position.x, container.position.y);

        if(!Boolean.getBoolean("debugRender")) return null;

        // draw debug ray cast line


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
