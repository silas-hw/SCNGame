package com.mygdx.scngame.entity.npc;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Rect;
import com.dongbat.jbump.World;
import com.mygdx.scngame.dialog.DialogFile;
import com.mygdx.scngame.entity.Entity;
import com.mygdx.scngame.event.DialogEventBus;
import com.mygdx.scngame.path.PathNode;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.physics.InteractBox;

/**
 * Simple NPC that randomly moves between a predefined set of {@link com.mygdx.scngame.path.PathNodes}
 * and contains an {@link com.mygdx.scngame.physics.InteractBox} that upon interaction fires of a
 * pre-defined dialog event ({@link com.mygdx.scngame.event.DialogEventListener})
 */
public class NPC extends Entity {

    private PathNode nextPathNode;
    private PathNode currentPathNode;

    private final Item<Box> interactItem;
    private final Item<Box> collisionItem;

    public static class NPCBreed {

        public Animation<TextureAtlas.AtlasRegion> walkUpAnim;
        public Animation<TextureAtlas.AtlasRegion> walkDownAnim;
        public Animation<TextureAtlas.AtlasRegion> walkRightAnim;
        public PathNode startingPathNode;
        public String dialogID = "";
        public DialogFile dialogFile;

        public float walkingSpeed = 50f;

        public boolean valid() {
            return walkUpAnim != null && walkDownAnim != null && walkRightAnim != null && startingPathNode != null;
        }

    }

    public NPCBreed breed;

    public NPC(NPCBreed breed, DialogEventBus dialogBus) {
        assert breed != null;
        assert breed.valid();

        this.breed = breed;

        InteractBox interactBox = new InteractBox() {
            @Override
            public void interact() {

                dialogBus.startDialog(breed.dialogFile.getDialogNode(breed.dialogID));
            }
        };

        interactItem = new Item<>(interactBox);

        position.x = breed.startingPathNode.position.x;
        position.y = breed.startingPathNode.position.y;

        Box collider = new Box();
        collider.setLayer(0, true);
        collider.setMask(0, true);
        collider.solid = true;
        collider.internalFilter = Box.TOUCH_FILTER;

        collisionItem = new Item<>(collider);

        currentPathNode = breed.startingPathNode;

        if(!breed.startingPathNode.neighbours.isEmpty()) {
            nextPathNode = breed.startingPathNode.neighbours.get(0);
            direction = nextPathNode.position.cpy().sub(position).nor();
        }
    }

    private float stateTime = 0f;

    @Override
    public void update(float delta) {
        stateTime += delta;

        if(currentPathNode.neighbours.isEmpty()) {
            position.x = (int) nextPathNode.position.x;
            position.y = (int) nextPathNode.position.y;
            direction.set(0, 0);
        } else {
            boolean passedNextNode = direction.hasOppositeDirection(nextPathNode.position.cpy().sub(position).nor());

            if(passedNextNode) {
                position.x = nextPathNode.position.x;
                position.y = nextPathNode.position.y;

                currentPathNode = nextPathNode;

                if(!nextPathNode.neighbours.isEmpty()) {
                    nextPathNode = nextPathNode.neighbours.get(0);
                    direction = nextPathNode.position.cpy().sub(position).nor();
                }
            }
        }

        position.mulAdd(direction, delta*breed.walkingSpeed);

        world.move(collisionItem, position.x, position.y, Box.GLOBAL_FILTER);
        world.move(interactItem, position.x + 1, position.y + 1, Box.GLOBAL_FILTER);

        Rect rect = world.getRect(collisionItem);

        position.x = rect.x;
        position.y = rect.y;
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        TextureAtlas.AtlasRegion frame = breed.walkUpAnim.getKeyFrame(0f, true);
        boolean flipx = false;

        if(direction.isZero()) {
            frame = breed.walkDownAnim.getKeyFrame(0f, true);
        }

        if(direction.x > 0f) {
            frame = breed.walkRightAnim.getKeyFrame(stateTime, true);
        } else if(direction.x < 0f) {
            frame = breed.walkRightAnim.getKeyFrame(stateTime, true);
            flipx = true;
        }

        if(direction.y > 0f && Math.abs(direction.y) > Math.abs(direction.x)) {
            frame = breed.walkUpAnim.getKeyFrame(stateTime, true);
        } else if(direction.y < 0f && Math.abs(direction.y) > Math.abs(direction.x)) {
            frame = breed.walkDownAnim.getKeyFrame(stateTime, true);
        }

        frame.flip(flipx, false);
        batch.draw(frame, position.x, position.y);
        frame.flip(flipx, false);
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
}
