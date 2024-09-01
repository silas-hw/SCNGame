package com.mygdx.scngame.entity.player;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.dongbat.jbump.*;
import com.mygdx.scngame.entity.*;
import com.mygdx.scngame.entity.component.HealthComponent;
import com.mygdx.scngame.entity.component.HurtBox;
import com.mygdx.scngame.entity.player.states.PlayerMoveState;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.physics.TerrainBox;

// TODO: add comments
public class Player extends Entity implements HurtBox.HurtListener {
    private EntityState<? super Player> state;

    public Item<Box> collisionItem;
    public Item<Box> hitbox;

    public HealthComponent health;
    public HurtBox hurtbox;

    public Texture texture;

    public Animation<TextureAtlas.AtlasRegion> idleRightAnim;
    public Animation<TextureAtlas.AtlasRegion> walkRightAnim;
    public Animation<TextureAtlas.AtlasRegion> walkDownAnim;
    public Animation<TextureAtlas.AtlasRegion> walkUpAnim;

    public final int WIDTH = 16;
    public final int HEIGHT = 16;

    private float speed = 100;
    private float speedCoeff = 1f;

    public float getSpeed() {
        return speed * speedCoeff;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    // from center of body, so account for half of width/height
    public final float interactDistance = 14f;

    public Player(AssetManager assets) {
        float animDuration = 0.1f;

        texture = assets.get("sprites/wizardguy.png", Texture.class);

        TextureAtlas atlas = assets.get("animations/animation_atlas.atlas", TextureAtlas.class);

        idleRightAnim = new Animation<>(animDuration, atlas.findRegions("player/test_idle"), Animation.PlayMode.LOOP);
        walkRightAnim = new Animation<>(animDuration, atlas.findRegions("player/test_walk_right"), Animation.PlayMode.LOOP);
        walkDownAnim = new Animation<>(animDuration, atlas.findRegions("player/test_walk_down"), Animation.PlayMode.LOOP);
        walkUpAnim = new Animation<>(animDuration, atlas.findRegions("player/test_walk_up"), Animation.PlayMode.LOOP);

        Box foot;
        foot = new Box();
        foot.solid = true;

        // players collision mask is set to true for layer 0!!!
        foot.setMask(0, true);
        foot.setLayer(0, true);
        foot.internalFilter = Box.SLIDE_FILTER;

        collisionItem = new Item<>(foot);

        health = new HealthComponent(500f);
        hurtbox = new HurtBox(health, WIDTH, HEIGHT, 0.7f);
        hurtbox.setCollisionMask(0, true);

        hurtbox.addHurtListener(this);

        this.state = new PlayerMoveState();
        this.state.setContainer(this);
    }

    private static final float hurtColorTime = 0.4f;
    public float hurtColorTimer = 0f;

    @Override
    public void update(float delta) throws IllegalStateException {
        if(this.world == null) {
            throw new IllegalStateException("World must be set before calling update on Player");
        }

        if(hurtColorTimer > 0f) hurtColorTimer -= delta;

        EntityState<? super Player> newState =  state.update(delta);

        Response.Result res = world.move(collisionItem, position.x, position.y, Box.GLOBAL_FILTER);

        Collisions cols = res.projectedCollisions;

        speedCoeff = 1f;
        for(int i = 0; i <cols.size(); i++) {
            Item<?> collided = cols.get(i).other;
            if(collided.userData instanceof TerrainBox terrainBox) {

                speedCoeff = terrainBox.speedCoefficient;

                // we only want to count one terrain
                break;
            }
        }

        Rect rect = world.getRect(collisionItem);
        position.x = rect.x;
        position.y = rect.y;

        if(newState != null) {
            this.state.exit();

            this.state = newState;

            this.state.setContainer(this);
            this.state.setWorld(this.world);
            this.state.enter();
        }

        hurtbox.update(delta, this.position);
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {

        if(hurtColorTimer > 0f) {
            batch.setColor(Color.RED);
        }

        state.draw( batch, shape, alpha);

        batch.setColor(Color.WHITE);
    }

    @Override
    public void drawWaterReflection(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        state.drawWaterReflection(batch, shape, alpha);
    }

    @Override
    public void setWorld(World<Box> world) throws IllegalArgumentException {
        if(world == null) throw new IllegalArgumentException("World cannot be null");

        this.state.setWorld(world);

        // if it's the first time settings the world, we can now enter the initial state
        if(this.world == null) {
            this.state.enter();
        }

        if(this.world != null) {
            if(this.world.hasItem(collisionItem)) this.world.remove(collisionItem);
            if(this.world.hasItem(hitbox)) this.world.remove(hitbox);
        }

        this.world = world;

        this.world.add(collisionItem, position.x, position.y, WIDTH, HEIGHT/2f);

        this.hurtbox.setWorld(world);
    }

    @Override
    public void removeWorldItems() {
        if(this.world != null) {
            if(this.world.hasItem(collisionItem)) this.world.remove(collisionItem);
            if(this.world.hasItem(hitbox)) this.world.remove(hitbox);

            this.state.exit();
        }
    }

    public void resetState() {
        this.state.exit();

        this.state = new PlayerMoveState();
        this.state.setContainer(this);
        this.state.setWorld(this.world);

        this.state.enter();
    }

    @Override
    public void dispose() {
        this.state.exit();
    }

    @Override
    public void onHit() {
        hurtColorTimer = hurtColorTime;
    }
}
