package com.mygdx.scngame.entity.player.states;

import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.EntityState;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.physics.DamageBox;

public class PlayerAttackState extends PlayerMoveState {

    private Direction attackDirection;
    private Item<Box> attackBox;

    private Vector2 attackBoxOffset = new Vector2();
    private float attackBoxWidth = 5f;
    private float attackBoxHeight = 5f;

    public PlayerAttackState(Direction direction) {
        this.attackDirection = direction;

        Box attackBox = new DamageBox(5f, DamageBox.DamageType.DEFAULT);

        this.attackBox = new Item<>(attackBox);
        this.facing = direction;
    }

    private float attackTimer = 0.5f;

    @Override
    public EntityState<? super Player> update(float delta) {

        // do what Move State does but disregard state changes
        super.update(delta);
        this.facing = attackDirection;

        if(attackTimer <= 0f) {
            PlayerMoveState newState = new PlayerMoveState();
            newState.facing = facing;
            return newState;
        }

        attackTimer -= delta;

        world.move(attackBox, container.position.x + attackBoxOffset.x, container.position.y + attackBoxOffset.y,
                   Box.GLOBAL_FILTER);

        return null;
    }

    @Override
    public void setContainer(Player container) {
        super.setContainer(container);

        this.attackBoxWidth = container.WIDTH;
        this.attackBoxHeight = container.HEIGHT;

        switch (this.attackDirection) {
            case LEFT:
                attackBoxOffset.set(- attackBoxWidth, 0);
                break;

            case RIGHT:
                attackBoxOffset.set(container.WIDTH,0);
                break;

            case UP:
                attackBoxOffset.set(0, container.HEIGHT);
                break;

            case DOWN:
                attackBoxOffset.set(0, -attackBoxHeight);
                break;
        }
    }

    @Override
    public void exit() {
        if(this.world != null) {
            if(this.world.hasItem(attackBox)) this.world.remove(attackBox);

        }

        super.exit();
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
}
