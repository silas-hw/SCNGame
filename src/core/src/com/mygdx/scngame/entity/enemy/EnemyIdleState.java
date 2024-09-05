package com.mygdx.scngame.entity.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.EntityState;
import com.mygdx.scngame.physics.Box;

import java.util.ArrayList;

public class EnemyIdleState implements EntityState<Enemy> {

    World<Box> world;

    Enemy enemy;

    float detectionRadius = 0;
    CollisionFilter detectionFilter;

    ArrayList<Item> results = new ArrayList<>();


    @Override
    public EntityState<? super Enemy> update(float delta) {
        float rectx = enemy.position.x - detectionRadius/2;
        float recty = enemy.position.y - detectionRadius/2;

        results.clear();

        if(!world.queryRect(rectx, recty, detectionRadius, detectionRadius, detectionFilter, results).isEmpty()) {
            return enemy.type.hostileState();
        }

        return null;
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {

    }

    @Override
    public void drawWaterReflection(SpriteBatch batch, ShapeRenderer shape, float alpha) {

    }

    @Override
    public void setContainer(Enemy container) {
        this.detectionFilter = container.detectionFilter;
        this.detectionRadius = container.type.detectionRadius();
    }

    @Override
    public void setWorld(World<Box> world) {
        this.world = world;
    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }
}
