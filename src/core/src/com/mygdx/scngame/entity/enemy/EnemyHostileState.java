package com.mygdx.scngame.entity.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.EntityState;
import com.mygdx.scngame.physics.Box;

import java.util.ArrayList;

public class EnemyHostileState implements EntityState<Enemy> {

    Enemy enemy;
    World<Box> world;

    float detectionRadius = 0;
    CollisionFilter detectionFilter;

    ArrayList<Item> results = new ArrayList<>();

    float timer = 0f;
    @Override
    public EntityState<? super Enemy> update(float delta) {
        float rectx = enemy.position.x - detectionRadius/2;
        float recty = enemy.position.y - detectionRadius/2;

        results.clear();

        if(!world.queryRect(rectx, recty, detectionRadius, detectionRadius, detectionFilter, results).isEmpty()) {
            timer = 0f;
            return null;
        }

        // if hostile cooldown is complete, turn idle
        if(timer >= enemy.type.hostileCooldown()) {
            return enemy.type.idleState();
        }

        timer += delta;
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
        this.enemy = container;
        this.detectionFilter = enemy.detectionFilter;
        this.detectionRadius = enemy.type.detectionRadius();

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
