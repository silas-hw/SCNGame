package com.mygdx.scngame.entity.player.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.EntityState;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.physics.DamageBox;

public class PlayerAttackState extends PlayerMoveState {

    private final Direction attackDirection;
    private final Item<Box> attackBox;

    private final Vector2 attackBoxOffset = new Vector2();
    private float attackBoxWidth = 5f;
    private float attackBoxHeight = 5f;

    public PlayerAttackState(Direction direction) {
        this.attackDirection = direction;

        Box attackBox = new DamageBox(1f, DamageBox.DamageType.DEFAULT);
        attackBox.setLayer(4, true);

        this.attackBox = new Item<>(attackBox);
        this.facing = direction;

        attackBoxWidth = 16f;
        attackBoxHeight = 18f;

        switch(direction) {
            case UP:
            case DOWN:
                attackBoxHeight*=0.5f;
                break;

            case LEFT:
            case RIGHT:
                attackBoxWidth*=0.5f;
                break;
        }
    }

    private float attackTimer = 0.5f;

    @Override
    public EntityState<? super Player> update(float delta) {

        // do what Move State does but disregard state changes
        super.update(delta);
        this.facing = attackDirection;

        attackTimer -= delta;


        if(attackTimer <= 0f) {
            PlayerMoveState newState = new PlayerDefaultState();
            newState.facing = facing;
            return newState;
        }



        world.move(attackBox, container.position.x + attackBoxOffset.x, container.position.y + attackBoxOffset.y,
                   Box.GLOBAL_FILTER);

        return null;
    }

    @Override
    public void setContainer(Player container) {
        super.setContainer(container);

        switch (this.attackDirection) {
            case LEFT:
                attackBoxOffset.set(- attackBoxWidth, (container.HEIGHT/2f - attackBoxHeight)/2f);
                break;

            case RIGHT:
                attackBoxOffset.set(container.WIDTH, (container.HEIGHT/2f - attackBoxHeight)/2f);
                break;

            case UP:
                attackBoxOffset.set((container.WIDTH - attackBoxWidth)/2f, container.HEIGHT/2f);
                break;

            case DOWN:
                attackBoxOffset.set((container.WIDTH - attackBoxWidth)/2f, -attackBoxHeight);
                break;
        }
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        boolean flipx = false;

        switch (facing) {
            case UP:
                this.lastFrame = container.attackUpAnimation.getKeyFrame(stateTime, true);
                break;

            case DOWN:
                this.lastFrame = container.attackDownAnimation.getKeyFrame(stateTime, true);
                break;

            case LEFT:
                flipx = true;

            case RIGHT:
                this.lastFrame = container.attackRightAnimation.getKeyFrame(stateTime, true);
                break;
        }

        lastFrame.flip(flipx, false);

        offsetX = (lastFrame.getRegionWidth() - container.WIDTH)/2f;
        offsetY = (lastFrame.getRegionHeight() - container.HEIGHT)/2f;

        batch.draw(lastFrame, container.position.x - offsetX, container.position.y - offsetY);

        lastFrame.flip(flipx, false);
    }

    @Override
    public void exit() {
        if(this.world != null) {
            if(this.world.hasItem(attackBox)) this.world.remove(attackBox);
        }

        super.exit();
    }

    @Override
    public void enter() {
        this.attackTimer = container.attackTime;
        stateTime = 0f;
    }

    @Override
    public void setWorld(World<Box> world) {
        if(this.world != null) {
            if(this.world.hasItem(attackBox)) this.world.remove(attackBox);
        }

        super.setWorld(world);

        this.world = world;
        this.world.add(attackBox, container.position.x + attackBoxOffset.x,
                    container.position.y + attackBoxOffset.y, attackBoxWidth, attackBoxHeight);
    }

    @Override
    public void removeWorldItems() {
        super.removeWorldItems();

        if(this.world != null) {
            if(this.world.hasItem(attackBox)) this.world.remove(attackBox);
        }
    }
}
