package com.mygdx.scngame.entity.player;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Rect;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.*;
import com.mygdx.scngame.entity.component.HealthComponent;
import com.mygdx.scngame.entity.component.HurtBox;
import com.mygdx.scngame.entity.context.EntityContext;
import com.mygdx.scngame.entity.player.states.PlayerMoveState;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.physics.HitBox;

// TODO: add comments
public class Player extends Entity {
    private EntityState<? super Player> state;

    public Item<Box> collisionItem;
    public Item<Box> hitbox;

    public HealthComponent health;
    public HurtBox hurtbox;

    public Texture texture;
    public Animation<TextureAtlas.AtlasRegion> anim;

    public final int WIDTH = 16;
    public final int HEIGHT = 16;

    public float speed = 100;

    // from center of body, so account for half of width/height
    public final float interactDistance = 14f;

    public Player(AssetManager assets) {
        texture = assets.get("sprites/wizardguy.png", Texture.class);

        TextureAtlas atlas = assets.get("animations/animation_atlas.atlas", TextureAtlas.class);
        anim = new Animation<>(0.3f, atlas.findRegions("testanim"), Animation.PlayMode.LOOP);

        Box foot;
        foot = new Box();
        foot.solid = true;

        // players collision mask is set to true for layer 0!!!
        foot.setMask(0, true);
        foot.setLayer(0, true);
        foot.internalFilter = Box.SLIDE_FILTER;

        collisionItem = new Item<>(foot);

        health = new HealthComponent(500f);
        hurtbox = new HurtBox(health, WIDTH, HEIGHT, 5f);
        hurtbox.setCollisionMask(0, true);

        this.state = new PlayerMoveState();
        this.state.setContainer(this);
    }

    @Override
    public void update(float delta) throws IllegalStateException {
        if(this.world == null) {
            throw new IllegalStateException("World must be set before calling update on Player");
        }

        EntityState<? super Player> newState =  state.update(delta);

        Response.Result res = world.move(collisionItem, position.x, position.y, Box.GLOBAL_FILTER);

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
        state.draw( batch, shape, alpha);
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

        this.world.add(collisionItem, position.x, position.y, WIDTH, HEIGHT/2);

        this.hurtbox.setWorld(world);
    }

    @Override
    public void dispose() {
        this.state.exit();
    }
}
