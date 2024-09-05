package com.mygdx.scngame.entity.enemy;

/*
Current Plan/Idea:
    - use some form of type object for different enemies
    - this includes animations, as well as strategies for movement patterns and attack type
    - movement patterns can be simple Entity States, one for idle and one for hostile

    - attacks may be a bit more complicated. Some attack we would want as their own state, such as sword
      attacks or projectiles. However, I also plan on having a simple 'bash' attack where the enemy constantly
      has a damage box around them

    - simplest solution for attacks would be them being their own state, and having attacks triggered by
      its own 'attack radius' similar to the detection radius for going hostile.

      Perhaps in the base hostile state there could be an 'attack' timer, with 'attack speed' being set in
      the EnemyType as well. If the player is in the 'attack radius' they attack, and continue attacking according to
      the speed. Perhaps we could have some more complicated directional logic so some enemies only attack if facing
      the player? Or that could be overkill. Enemies that might want to *always* be attacking, such as a ranged enemy
      that shoots in every direction at a constant rate, could just be set to have a very large attack radius.
 */

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.dongbat.jbump.CollisionFilter;
import com.mygdx.scngame.entity.Entity;
import com.mygdx.scngame.entity.EntityState;
import com.mygdx.scngame.physics.Box;

public class Enemy extends Entity {

    public record EnemyType(
            Animation<TextureAtlas.AtlasRegion> walkUpAnimation,
            Animation<TextureAtlas.AtlasRegion> walkDownAnimation,
            Animation<TextureAtlas.AtlasRegion> walkRightAnimation,
            Animation<TextureAtlas.AtlasRegion> idleAnimation,
            Animation<TextureAtlas.AtlasRegion> attackUpAnimation,
            Animation<TextureAtlas.AtlasRegion> attackDownAnimation,
            Animation<TextureAtlas.AtlasRegion> attackRightAnimation,

            EnemyIdleState idleState,
            EnemyHostileState hostileState,

            float detectionRadius,
            float hostileCooldown,

            int playerDetectionMask
    ) {}

    public final EnemyType type;
    public final CollisionFilter detectionFilter;

    public Enemy(EnemyType type) {
        this.type = type;
        this.detectionFilter = new Box.QueryFilter(type.playerDetectionMask);
    }

    @Override
    public void dispose() {

    }
}
