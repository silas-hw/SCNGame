package com.mygdx.scngame.entity.player.states;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.*;
import com.mygdx.scngame.entity.EntityState;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.physics.InteractBox;
import com.mygdx.scngame.controls.Controls;
import com.mygdx.scngame.physics.Interactable;

import java.util.ArrayList;

public class PlayerMoveState implements EntityState<Player> {

    private float invisTimer = 0f;
    private final float invisTime = 0.3f;
    private boolean invis = false;
    
    protected Player container;
    protected World<Box> world;

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    protected Direction facing = Direction.LEFT;

    Vector2 rayStart = new Vector2();
    Vector2 rayEnd = new Vector2();

    public float stateTime = 0f;

    @Override
    public EntityState<? super Player> update(float delta) {
        stateTime += delta;
        int dx = 0;
        int dy = 0;

        if(container.context.isActionPressed(Controls.Actions.LEFT)) {
            dx--;
            facing = Direction.LEFT;
        }

        if(container.context.isActionPressed(Controls.Actions.RIGHT)) {
            dx++;
            facing = Direction.RIGHT;
        }

        if(container.context.isActionPressed(Controls.Actions.UP)) {
            dy++;
            facing = Direction.UP;
        }

        if(container.context.isActionPressed(Controls.Actions.DOWN)) {
            dy--;
            facing = Direction.DOWN;
        }

        if(container.context.isActionJustPressed(Controls.Actions.ATTACK)) {
            return new PlayerAttackState(facing);
        }

        container.direction.set(dx, dy);
        container.direction.nor();

        if(container.context.isActionJustPressed(Controls.Actions.DASH)) {
            return new PlayerDashState();
        }

        rayStart.y = container.position.y + container.HEIGHT/2f;
        rayStart.x = container.position.x + container.WIDTH/2f;
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

        if(container.context.isActionJustPressed(Controls.Actions.INTERACT)) {
            // query world for interact boxes

            ArrayList<Item> arr = new ArrayList<>();

            // ray cast from center of body
            world.querySegment(rayStart.x, rayStart.y, rayEnd.x, rayEnd.y, InteractBox.INTERACT_FILTER, arr);

            if(arr.size() > 0) {
                Interactable interactBox = (Interactable) arr.get(0).userData;
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

    private TextureAtlas.AtlasRegion lastFrame;


    @Override
    public EntityState<? super Player> draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {

        boolean flipx = false;
        boolean flipy = false;

        float animationTime = container.direction.isZero() ? 0f : stateTime;

        switch(facing) {
            case LEFT:
                flipx = true;

            case RIGHT:
                lastFrame = container.walkRightAnim.getKeyFrame(animationTime, true);
                break;

            case DOWN:
                lastFrame = container.walkDownAnim.getKeyFrame(animationTime, true);
                break;

            case UP:
                lastFrame = container.walkUpAnim.getKeyFrame(animationTime, true);
                break;
        }

        lastFrame.flip(flipx, flipy);

        batch.draw(lastFrame, container.position.x, container.position.y);

        lastFrame.flip(flipx, flipy);


        // draw debug ray cast line
        if(!Boolean.getBoolean("debugRender")) return null;

        batch.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.WHITE);

        shape.rectLine(rayStart, rayEnd, 1);

        shape.end();

        batch.begin();

        return null;
    }

    @Override
    public void drawWaterReflection(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        boolean flipx = false;

        switch(facing) {
            case LEFT:
                flipx = true;
                break;
        }

        lastFrame.flip(flipx, true);
        batch.draw(lastFrame, (int) container.position.x, ((int) container.position.y) - lastFrame.getRegionHeight());
        lastFrame.flip(flipx, true);
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
        lastFrame = container.idleRightAnim.getKeyFrame(stateTime, true);
        container.hurtbox.setTakesDamage(true);
    }

    @Override
    public void exit() {}
}
