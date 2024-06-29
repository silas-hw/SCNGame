package com.mygdx.scngame.scene;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.Entity;
import com.mygdx.scngame.entity.context.EntityContext;

import java.util.Comparator;

/**
 * A collection of {@link com.mygdx.scngame.entity.Entity entities}. Holds a {@link Comparator renderComparator}
 * to determine the order by which entities should be drawn. By default, this is a {@link YComparator YComparator}
 * which sorts by the Y position of an entity.
 * <p>
 * If no {@link SpriteBatch sprite batch} and {@link ShapeRenderer shape renderer} are provided then new ones are
 * created, which are disposed of automatically.
 * <p>
 * If a batch and shape renderer <i>are</i> provided, the provider is responsible for disposing of them.
 * <p>
 * This implements an InputProcessor, and propagates all input events to registered entities. This works unlike a
 * {@link com.badlogic.gdx.InputMultiplexer input multiplexer}. Every single entity will receive the input event.
 *
 * @author Silas Hayes-Williams
 */
public class Scene implements Disposable, InputProcessor, EntityContext {
    protected SnapshotArray<Entity> entities;
    protected Comparator<Entity> renderComparator;

    protected SpriteBatch batch;
    protected ShapeRenderer shape;
    protected Viewport viewport;

    public float alpha = 1.0f;

    // if the scene created its own batch. Used to determine whether to dispose of the batch and shape renderer
    private boolean ownsBatch = false;

    public Scene(Viewport viewport) {
        this(viewport, new SpriteBatch(), new ShapeRenderer());
        ownsBatch = true;
    }

    public Scene(Viewport viewport, SpriteBatch batch, ShapeRenderer shape) {
        entities = new SnapshotArray(true, 4, Entity.class);
        renderComparator = new YComparator();

        this.batch = batch;
        this.shape = shape;
        this.viewport = viewport;
    }

    public void addEntity(Entity entity) {
        // change entity context to this if it currently belongs to another context
        if(entity.context != null) {
            entity.context.removeEntity(entity);
        }

        entity.context = this;

        this.entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        this.entities.removeValue(entity, false);
    }

    public void clearEntities() {
        this.entities.clear();
    }

    @Override
    public boolean hasEntity(Entity entity) {
        return entities.contains(entity, false);
    }

    public void update(World<Object> world, float delta) {
        Entity[] e = entities.begin();

        for(int i = 0; i<entities.size; i++) {
            if (e[i].isDead) {
                entities.removeValue(e[i], false);
                e[i].dispose();
                continue;
            }

            e[i].update(world, delta);
        }

        entities.end();
    }

    public void draw() {
        entities.sort(this.renderComparator);
        viewport.getCamera().update();
        viewport.apply();

        batch.setProjectionMatrix(viewport.getCamera().combined);
        shape.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        Entity[] e = entities.begin();
        for(int i = 0; i<entities.size; i++) {
            e[i].draw(batch, shape, alpha);
        }

        entities.end();

        batch.end();
    }

    @Override
    public void dispose() {
        for(Entity entity : entities) {
            entity.dispose();
        }

        clearEntities();

        if(ownsBatch) {
            batch.dispose();
            shape.dispose();
        }
    }

    // Propagate all key events to registered entities

    @Override
    public boolean keyDown(int i) {
        boolean handled = false;

        for(InputProcessor listener : entities) {
            handled = listener.keyDown(i) || handled;
        }

        return handled;
    }

    @Override
    public boolean keyUp(int i) {
        boolean handled = false;

        for(InputProcessor listener : entities) {
            handled = listener.keyUp(i) || handled;
        }

        return handled;
    }

    @Override
    public boolean keyTyped(char c) {
        boolean handled = false;

        for(InputProcessor listener : entities) {
            handled = listener.keyTyped(c) || handled;
        }

        return handled;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        boolean handled = false;

        for(InputProcessor listener : entities) {
            handled = listener.touchDown(i, i1, i2, i3) || handled;
        }

        return handled;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        boolean handled = false;

        for(InputProcessor listener : entities) {
            handled = listener.touchUp(i, i1, i2, i3) || handled;
        }

        return handled;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        boolean handled = false;

        for(InputProcessor listener : entities) {
            handled = listener.touchCancelled(i, i1, i2, i3) || handled;
        }

        return handled;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        boolean handled = false;

        for(InputProcessor listener : entities) {
            handled = listener.touchDragged(i, i1, i2) || handled;
        }

        return handled;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        boolean handled = false;

        for(InputProcessor listener : entities) {
            handled = listener.mouseMoved(i, i1) || handled;
        }

        return handled;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        boolean handled = false;

        for(InputProcessor listener : entities) {
            handled = listener.scrolled(v, v1) || handled;
        }

        return handled;
    }

    public static class YComparator implements Comparator<Entity> {

        @Override
        public int compare(Entity entity, Entity other) {
            return Float.compare(other.position.y, entity.position.y);
        }

        @Override
        public boolean equals(Object o) {
            return false;
        }
    }

}
