package com.mygdx.scngame.entity.enemy;

/*
Current Plan/Idea:
    - use some form of type object for different enemies
    - this includes animations, as well as strategies for movement patterns and attack type
    - movement patterns can be simple Entity States, one for idle and one for hostile

    - attacks may be a bit more complicated. Some attack we would want as their own state, such as sword
      attacks or projectiles. However, I also plan on having a simple 'bash' attack where the enemy constantly
      has a damage box around them
 */

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.scngame.entity.Entity;
import com.mygdx.scngame.entity.EntityState;

public class Enemy extends Entity {

    public record EnemyType(
            Animation<TextureAtlas.AtlasRegion> walkUpAnimation,
            Animation<TextureAtlas.AtlasRegion> walkDownAnimation,
            Animation<TextureAtlas.AtlasRegion> walkRightAnimation,
            Animation<TextureAtlas.AtlasRegion> idleAnimation,
            Animation<TextureAtlas.AtlasRegion> attackUpAnimation,
            Animation<TextureAtlas.AtlasRegion> attackDownAnimation,
            Animation<TextureAtlas.AtlasRegion> attackRightAnimation,

            EntityState<Enemy> idleState,
            EntityState<Enemy> hostileState,

            float detectionRadius,
            float hostileCooldown,

            int playerDetectionMask
    ) {}

    public final EnemyType type;

    public Enemy(EnemyType type) {
        this.type = type;
    }

    @Override
    public void dispose() {

    }
}
