package com.mygdx.scngame.scene;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dongbat.jbump.World;
import com.mygdx.scngame.dialog.DialogStart;
import com.mygdx.scngame.entity.Entity;
import com.mygdx.scngame.entity.context.EntityContext;
import com.mygdx.scngame.event.GameEvent;
import com.mygdx.scngame.event.GameEventBus;
import com.mygdx.scngame.event.GameEventListener;
import com.mygdx.scngame.event.Global;
import com.mygdx.scngame.physics.Box;

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
 * This implements an InputProcessor, and stores keyboard input events such that they can
 * be polled locally by its entities, as opposed to globally via {@link com.badlogic.gdx.Input}.
 * Inputs are cleared upon certain events that are expected to pause the main gameplay,
 * such as {@link DialogStart}.
 *
 * @author Silas Hayes-Williams
 */
public class Scene extends InputAdapter implements Disposable, EntityContext, GameEventListener {
    protected SnapshotArray<Entity> entities;
    protected Comparator<Entity> renderComparator;

    protected SpriteBatch batch;
    protected ShapeRenderer shape;
    protected Viewport viewport;

    public float alpha = 1.0f;

    private boolean keyJustPressed;
    public boolean[] keyPressed = new boolean[Input.Keys.MAX_KEYCODE + 1];
    public boolean[] keysJustPressed = new boolean[Input.Keys.MAX_KEYCODE + 1];

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

        Global.bus.addEventListener(this);
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

    @Override
    public boolean isKeyPressed(int keycode) {
        return keyPressed[keycode];
    }

    @Override
    public boolean isKeyJustPressed(int keycode) {
        return keysJustPressed[keycode];
    }

    @Override
    public void fire(GameEvent event) {

    }

    @Override
    public void addEventListener(GameEventListener listener) {

    }

    @Override
    public void removeEventListener(GameEventListener listener) {

    }

    @Override
    public void clearEventListeners() {

    }

    public void update(float delta) {
        Entity[] e = entities.begin();

        for(int i = 0; i<entities.size; i++) {
            if (e[i].isDead) {
                entities.removeValue(e[i], false);
                e[i].dispose();
                continue;
            }

            e[i].update(delta);
        }

        entities.end();

        if(keyJustPressed) {
            keyJustPressed = false;
            for(int i = 0; i<keysJustPressed.length; i++) {
                keysJustPressed[i] = false;
            }
        }
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

    @Override
    public boolean keyDown(int i) {
        keyJustPressed = true;
        keysJustPressed[i] = true;
        keyPressed[i] = true;

        return true;
    }

    @Override
    public boolean keyUp(int i) {
        keyPressed[i] = false;
        return true;
    }

    @Override
    public void notify(GameEvent event) {
        if(event.getPayload() instanceof DialogStart) {
            keyJustPressed = false;
            for(int i = 0; i<keysJustPressed.length; i++) {
                keysJustPressed[i] = false;
            }

            for(int i = 0; i<keyPressed.length; i++) {
                keyPressed[i] = false;
            }
        }
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
