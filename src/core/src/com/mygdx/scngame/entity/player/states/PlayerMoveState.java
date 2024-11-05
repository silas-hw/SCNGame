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
import java.util.Objects;

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

        container.position.mulAdd(container.direction, container.getSpeed()*delta);

        if(container.context.isActionJustPressed(Controls.Actions.DASH)) {
            return new PlayerDashState();
        }

        return null;
    }

    protected TextureAtlas.AtlasRegion lastFrame;
    float offsetX = 0f;
    float offsetY = 0f;


    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {

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

        offsetX = (lastFrame.getRegionWidth() - container.WIDTH)/2f;
        offsetY = (lastFrame.getRegionHeight() - container.HEIGHT)/2f;

        batch.draw(lastFrame, container.position.x - offsetX, container.position.y - offsetY);

        lastFrame.flip(flipx, flipy);


        // draw debug ray cast line
        if(!Boolean.getBoolean("debugRender")) return;

        batch.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.WHITE);

        shape.rectLine(rayStart, rayEnd, 1);

        shape.end();

        batch.begin();
    }

    @Override
    public void drawWaterReflection(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        boolean flipx = facing == Direction.LEFT;

        lastFrame.flip(flipx, true);
        batch.draw(lastFrame, (int) (container.position.x - offsetX), ((int) (container.position.y + offsetY)) - lastFrame.getRegionHeight());
        lastFrame.flip(flipx, true);
    }

    @Override
    public void setContainer(Player container) {
        this.container = container;
        lastFrame = container.walkDownAnim.getKeyFrame(0f);
    }

    @Override
    public void setWorld(World<Box> world) {
        this.world = world;
    }

    @Override
    public void removeWorldItems() {

    }

    @Override
    public void enter() {
        lastFrame = container.idleRightAnim.getKeyFrame(stateTime, true);
        container.hurtbox.setTakesDamage(true);
    }

    @Override
    public void exit() {}
}
