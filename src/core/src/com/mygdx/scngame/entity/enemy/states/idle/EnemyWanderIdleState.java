package com.mygdx.scngame.entity.enemy.states.idle;

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
        }

        stateTime += delta;

        enemy.position.mulAdd(enemy.direction, speed*delta);

        return super.update(delta);
    }
}
