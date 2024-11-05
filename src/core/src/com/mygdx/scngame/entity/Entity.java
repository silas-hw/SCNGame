package com.mygdx.scngame.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.context.EntityContextAdapter;
import com.mygdx.scngame.entity.context.EntityContext;
import com.mygdx.scngame.physics.Box;

/**
 * Represents a single 'Entity' within the Game.
 */
public abstract class Entity implements Disposable {
    public Vector2 position = new Vector2();
    public Vector2 direction = new Vector2();

    protected World<Box> world;

    /**
     * An empty {@link EntityContextAdapter} such that if the context is not set there is still
     * a context that can be interacted with
     */
    public final static EntityContext NULL_CONTEXT = new EntityContextAdapter();
    public EntityContext context = NULL_CONTEXT;

    public Entity() {};

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

    public void drawWaterReflection(SpriteBatch batch, ShapeRenderer shape, float alpha) {}

    /**
     * Sets the {@link World physics world} for this Entity to use.
     *
     * Do not call update without settings the world
     *
     * @param world the physics world to set to
     */
    public void setWorld(World<Box> world) {
        this.world = world;
    };

    /**
     * Remove the entities world {@link com.dongbat.jbump.Item items} from its currenlty set world, including any
     * world items added as a result of any of the entities components (e.g. {@link EntityState} or
     * {@link com.mygdx.scngame.entity.component.HurtBox}
     */
    public void removeWorldItems() {

    }

    public World<Box> getWorld() {return this.world;}
}
