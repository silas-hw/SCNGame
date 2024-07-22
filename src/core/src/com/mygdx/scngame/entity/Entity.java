package com.mygdx.scngame.entity;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.context.EntityContextAdapter;
import com.mygdx.scngame.entity.context.EntityContext;
import com.mygdx.scngame.event.GameEventListener;
import com.mygdx.scngame.physics.Box;

/**
 * Represents a single 'Entity' within the Game.
 */
public abstract class Entity implements Disposable {
    public Vector2 position = new Vector2();
    public Vector2 direction = new Vector2();

    // used by Scene to remove dead entities without coupling Entity to Scene
    public boolean isDead = false;

    protected World<Box> world;

    /**
     * An empty {@link EntityContextAdapter} such that if the context is not set there is still
     * a context that can be interacted with
     */
    public final static EntityContext NULL_CONTEXT = new EntityContextAdapter();
    public EntityContext context = NULL_CONTEXT;


    public void init(World<Box> world, EntityContext context) {
        this.world = world;
        this.context = context;
    }

    /**
     * Invoked when the entity is to update its state and handle input
     *
     * @param delta the delta time since the last call to update
     */
    public void update(float delta) {}

    /**
     *
     * @param batch the {@link SpriteBatch sprite batch} to draw sprites to
     * @param shape the {@link ShapeRenderer shape renderer} to draw shapes to
     * @param alpha
     */
    public void draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {}

    public void setWorld(World<Box> world) {
        this.world = world;
    };
}
