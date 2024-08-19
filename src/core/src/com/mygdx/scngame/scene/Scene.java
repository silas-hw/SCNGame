package com.mygdx.scngame.scene;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dongbat.jbump.World;
import com.mygdx.scngame.controls.ActionListener;
import com.mygdx.scngame.entity.Entity;
import com.mygdx.scngame.entity.context.EntityContext;
import com.mygdx.scngame.event.DialogEventListener;
import com.mygdx.scngame.event.GlobalEventBus;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.controls.Controls;

import java.util.Arrays;
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
 * such as starting dialog.
 *
 * @author Silas Hayes-Williams
 */
public class Scene extends InputAdapter implements Disposable, EntityContext, DialogEventListener, ActionListener {
    protected SnapshotArray<Entity> entities;
    protected Comparator<Entity> renderComparator;

    protected SpriteBatch batch;
    protected ShapeRenderer shape;
    protected Viewport viewport;

    public float alpha = 1.0f;

    private World<Box> world;

    private boolean actionJustPressed = false;
    private final boolean[] actionsPressed = new boolean[Controls.Actions.values().length];
    private final boolean[] actionsJustPressed = new boolean[Controls.Actions.values().length];

    // if the scene created its own batch. Used to determine whether to dispose of the batch and shape renderer
    private boolean ownsBatch = false;

    public Scene(Viewport viewport, World<Box> world) {
        this(viewport, new SpriteBatch(), new ShapeRenderer(), world);
        ownsBatch = true;
    }

    public Scene(Viewport viewport, SpriteBatch batch, ShapeRenderer shape, World<Box> world) {
        entities = new SnapshotArray<>(true, 4, Entity.class);
        renderComparator = new YComparator();

        this.batch = batch;
        this.shape = shape;
        this.viewport = viewport;

        GlobalEventBus.getInstance().addDialogListener(this);

        this.world = world;
    }

    public void update(float delta) {
        Entity[] e = entities.begin();

        for(int i = 0; i<entities.size; i++) {
            e[i].update(delta);
        }

        entities.end();

        if(actionJustPressed) {
            actionJustPressed = false;
            for(int i = 0; i<actionsJustPressed.length; i++) {
                actionsJustPressed[i] = false;
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

    public void addEntity(Entity entity) {
        // change entity context to this if it currently belongs to another context
        if(entity.context != null) {
            entity.context.removeEntity(entity);
        }

        entity.context = this;

        // change entity world if it doesn't match the scenes world
        entity.setWorld(this.world);

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
    public void setWorld(World<Box> world) {
        this.world = world;

        for(Entity entity : entities) {
            entity.setWorld(world);
        }
    }

    @Override
    public World<Box> getWorld() {
        return this.world;
    }

    public void setViewport(Viewport view) {
        this.viewport = view;
    }

    @Override
    public boolean isActionPressed(Controls.Actions action) {
        return actionsPressed[action.ordinal()];
    }

    @Override
    public boolean isActionJustPressed(Controls.Actions action) {
        return actionsJustPressed[action.ordinal()];
    }

    /**
     * Disposes all held entities as well as the batch and shape renderer if one was not provided.
     * <p>
     * If you wish for an entity to persist and not be disposed of when the scene gets disposed,
     * remove that entity from the scene before disposing.
     */
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
    public void onDialogStart(String id) {
        Arrays.fill(actionsJustPressed, false);
        Arrays.fill(actionsPressed, false);
    }

    @Override
    public void onDialogEnd() {
    }

    @Override
    public boolean actionDown(Controls.Actions action) {
        actionJustPressed = true;
        actionsJustPressed[action.ordinal()] = true;
        actionsPressed[action.ordinal()] = true;
        return true;
    }

    @Override
    public boolean actionUp(Controls.Actions action) {
        actionsPressed[action.ordinal()] = false;
        return true;
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