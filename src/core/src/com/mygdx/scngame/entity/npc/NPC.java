package com.mygdx.scngame.entity.npc;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Rect;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.Entity;
import com.mygdx.scngame.event.DialogEventListener;
import com.mygdx.scngame.event.GlobalEventBus;
import com.mygdx.scngame.path.PathNode;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.physics.InteractBox;

/**
 * Simple NPC that randomly moves between a predefined set of {@link com.mygdx.scngame.path.PathNodes}
 * and contains an {@link com.mygdx.scngame.physics.InteractBox} that upon interaction fires of a
 * pre-defined dialog event ({@link com.mygdx.scngame.event.DialogEventListener})
 */
public class NPC extends Entity implements DialogEventListener {

    private PathNode nextPathNode;

    public Animation<TextureAtlas.AtlasRegion> walkUpAnim;
    public Animation<TextureAtlas.AtlasRegion> walkDownAnim;
    public Animation<TextureAtlas.AtlasRegion> walkRightAnim;

    private final Item<Box> interactItem;
    private final Item<Box> collisionItem;

    public NPC(PathNode startingPathNode, String dialogID, AssetManager assets) {
        float animDuration = 0.2f;

        TextureAtlas atlas = assets.get("animations/animation_atlas.atlas", TextureAtlas.class);

        walkRightAnim = new Animation<>(animDuration, atlas.findRegions("player/test_walk_right"), Animation.PlayMode.LOOP);
        walkDownAnim = new Animation<>(animDuration, atlas.findRegions("player/test_walk_down"), Animation.PlayMode.LOOP);
        walkUpAnim = new Animation<>(animDuration, atlas.findRegions("player/test_walk_up"), Animation.PlayMode.LOOP);

        InteractBox interactBox = new InteractBox() {
            @Override
            public void interact() {
                GlobalEventBus.getInstance().startDialog(dialogID);
            }
        };

        interactItem = new Item<>(interactBox);

        position.x = startingPathNode.position.x;
        position.y = startingPathNode.position.y;

        Box collider = new Box();
        collider.setLayer(0, true);
        collider.setMask(0, true);
        collider.solid = true;
        collider.internalFilter = Box.TOUCH_FILTER;

        collisionItem = new Item<>(collider);

        nextPathNode = startingPathNode.getNeighbour(0);
        direction = nextPathNode.position.cpy().sub(position).nor();

        GlobalEventBus.getInstance().addDialogListener(this);
    }

    private float speed = 50f;
    private boolean freeze = false;

    @Override
    public void update(float delta) {
        if(freeze) return;

        boolean passedNextNode = direction.hasOppositeDirection(nextPathNode.position.cpy().sub(position).nor());

        if(passedNextNode) {
            position.x = nextPathNode.position.x;
            position.y = nextPathNode.position.y;

            nextPathNode = nextPathNode.getNeighbour(0);
            direction = nextPathNode.position.cpy().sub(position).nor();
        }

        position.mulAdd(direction, delta*speed);

        world.move(collisionItem, position.x, position.y, Box.GLOBAL_FILTER);
        world.move(interactItem, position.x + 1, position.y + 1, Box.GLOBAL_FILTER);

        Rect rect = world.getRect(collisionItem);

        position.x = rect.x;
        position.y = rect.y;
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        TextureAtlas.AtlasRegion frame = walkDownAnim.getKeyFrame(0f, true);

        batch.draw(frame, position.x, position.y);
    }

    @Override
    public void setWorld(World<Box> world) {
        if(this.world != null) {
            if(this.world.hasItem(interactItem)) this.world.remove(interactItem);
            if(this.world.hasItem(collisionItem)) this.world.remove(collisionItem);
        }

        this.world = world;

        this.world.add(interactItem, position.x + 1, position.y + 1, 14, 14);
        this.world.add(collisionItem, position.x, position.y, 16, 16);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void onDialogStart(String id) {
        freeze = true;
    }

    @Override
    public void onDialogEnd() {
        freeze = false;
    }
}
