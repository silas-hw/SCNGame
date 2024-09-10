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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.Entity;
import com.mygdx.scngame.entity.EntityState;
import com.mygdx.scngame.entity.StateManager;
import com.mygdx.scngame.entity.component.HealthComponent;
import com.mygdx.scngame.entity.component.HurtBox;
import com.mygdx.scngame.physics.Box;

public class Enemy extends Entity implements HealthComponent.DeathListener {

    @Override
    public void onDeath() {
        this.stateManager.setState(new EnemyDeathState());
    }

    public record EnemyType(
            Animation<TextureAtlas.AtlasRegion> walkUpAnimation,
            Animation<TextureAtlas.AtlasRegion> walkDownAnimation,
            Animation<TextureAtlas.AtlasRegion> walkRightAnimation,
            Animation<TextureAtlas.AtlasRegion> idleAnimation,
            Animation<TextureAtlas.AtlasRegion> attackUpAnimation,
            Animation<TextureAtlas.AtlasRegion> attackDownAnimation,
            Animation<TextureAtlas.AtlasRegion> attackRightAnimation,

            Animation<TextureAtlas.AtlasRegion> deathAnimation,

            EnemyIdleState idleState,
            EnemyHostileState hostileState,

            float detectionRadius,
            float hostileCooldown,

            int playerDetectionMask,

            float width,
            float height

    ) {}

    public final EnemyType type;
    public final CollisionFilter detectionFilter;

    HurtBox hurtBox;
    HealthComponent health;

    StateManager<Enemy> stateManager;

    public Enemy(EnemyType type) {
        this.type = type;
        this.detectionFilter = new Box.QueryFilter(type.playerDetectionMask);

        this.type.idleState.setContainer(this);
        this.type.hostileState.setContainer(this);

        this.stateManager = new StateManager<>(this.type.idleState(), this);

        this.health = new HealthComponent(6);
        this.hurtBox = new HurtBox(health, type.width(), type.height(), 0.2f);
        this.hurtBox.setCollisionMask(4, true);

        health.addDeathListener(this);
    }

    public Vector2 getCenterPoint() {
        return new Vector2(
                position.x + this.type.width/2f,
                position.y + this.type.height/2f
        );
    }

    @Override
    public void update(float delta) {
        stateManager.update(delta);
        hurtBox.update(delta, this.position);
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        this.stateManager.draw(batch, shape, alpha);
    }

    @Override
    public void setWorld(World<Box> world) {
        this.stateManager.setWorld(this.world);
        this.hurtBox.setWorld(world);
        this.world = world;
    }

    @Override
    public void removeWorldItems() {
        this.hurtBox.removeWorldItems();
        this.stateManager.removeWorldItems();
    }

    @Override
    public void dispose() {

    }
}
