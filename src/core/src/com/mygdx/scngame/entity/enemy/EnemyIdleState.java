package com.mygdx.scngame.entity.enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.EntityState;
import com.mygdx.scngame.physics.Box;

import java.util.ArrayList;

public class EnemyIdleState implements EntityState<Enemy> {

    World<Box> world;

    protected Enemy enemy;

    float detectionRadius = 0;
    CollisionFilter detectionFilter;

    ArrayList<Item> results = new ArrayList<>();


    @Override
    public EntityState<? super Enemy> update(float delta) {
        Vector2 center = enemy.getCenterPoint();

        float rectx = center.x - detectionRadius/2;
        float recty = center.y - detectionRadius/2;

        results.clear();

        if(!world.queryRect(rectx, recty, detectionRadius, detectionRadius, detectionFilter, results).isEmpty()) {
            return enemy.type.hostileState();
        }

        return null;
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        Color ogColor = batch.getColor();

        batch.setColor(Color.GREEN);

        batch.draw(enemy.type.walkDownAnimation().getKeyFrame(0f), enemy.position.x, enemy.position.y);

        batch.setColor(Color.WHITE);

        batch.end();

        shape.begin(ShapeRenderer.ShapeType.Line);

        Vector2 center = enemy.getCenterPoint();

        float rectx = center.x - detectionRadius/2;
        float recty = center.y - detectionRadius/2;

        shape.rect(rectx, recty, detectionRadius, detectionRadius);

        shape.end();

        batch.begin();

    }

    @Override
    public void drawWaterReflection(SpriteBatch batch, ShapeRenderer shape, float alpha) {

    }

    @Override
    public void setContainer(Enemy container) {
        this.detectionFilter = container.detectionFilter;
        this.detectionRadius = container.type.detectionRadius();

        this.enemy = container;
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

    }

    @Override
    public void exit() {

    }
}
