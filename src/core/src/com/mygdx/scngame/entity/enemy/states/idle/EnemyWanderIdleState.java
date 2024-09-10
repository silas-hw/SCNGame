package com.mygdx.scngame.entity.enemy.states.idle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.scngame.entity.EntityState;
import com.mygdx.scngame.entity.enemy.Enemy;
import com.mygdx.scngame.entity.enemy.EnemyIdleState;

public class EnemyWanderIdleState extends EnemyIdleState {

    float stateTime = 0f;
    float maxWanderTime = 2f;
    float minWanderTime = 0.5f;

    float currentWanderTime;

    float speed = 10f;

    public EnemyWanderIdleState() {
        this.currentWanderTime = MathUtils.random(minWanderTime, maxWanderTime);
    }

    @Override
    public EntityState<? super Enemy> update(float delta) {

        if(stateTime >= currentWanderTime) {
            stateTime = 0f;

            float dx = MathUtils.random(-1, 1);
            float dy = MathUtils.random(-1, 1);

            enemy.direction.set(dx, dy);
            enemy.direction.nor();

            enemy.position.x = MathUtils.round(enemy.position.x);
            enemy.position.y = MathUtils.round(enemy.position.y);
        }

        stateTime += delta;

        enemy.position.mulAdd(enemy.direction, speed*delta);

        return super.update(delta);
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        float walkTime = enemy.direction.isZero() ? 0f : stateTime;

        boolean flipx = false;

        TextureAtlas.AtlasRegion frame = enemy.type.walkDownAnimation().getKeyFrame(0f);

        if(enemy.direction.x != 0) {
            frame = enemy.type.walkRightAnimation().getKeyFrame(walkTime, true);

            if(enemy.direction.x < 0) {
                flipx = true;
            }
        } else if(enemy.direction.y > 0) {
            frame = enemy.type.walkUpAnimation().getKeyFrame(walkTime, true);
        } else if(enemy.direction.y <= 0) {
            frame = enemy.type.walkDownAnimation().getKeyFrame(walkTime, true);
        }

        float offsetx = (frame.getRegionWidth() - enemy.type.width())/2f;
        float offsety = (frame.getRegionHeight() - enemy.type.height())/2f;

        frame.flip(flipx, false);
        batch.draw(frame, enemy.position.x - offsetx, enemy.position.y - offsety);
        frame.flip(flipx, false);
    }
}
