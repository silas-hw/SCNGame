package com.mygdx.scngame.entity.trigger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.Entity;
import com.mygdx.scngame.physics.Box;

import java.util.ArrayList;

public class Trigger extends Entity {

    private float width;
    private float height;

    private int bitMask = 0;

    private Runnable runnable;

    // could cache filters with the same mask but right now thats a premature optimisation
    private final CollisionFilter triggerFilter = new CollisionFilter() {
        @Override
        public Response filter(Item item, Item other) {
            if(!(item.userData instanceof Box)) {
                return null;
            }

            if((((Box) item.userData).layer & bitMask) == 0) return null;

            return Response.cross;

        }
    };

    public Trigger(float x, float y, float width, float height, int[] maskLayers, Runnable runnable) {
        position.x = x;
        position.y = y;

        this.width = width;
        this.height = height;

        this.bitMask = Box.getMaskFromIndices(maskLayers);
        this.runnable = runnable;
    }

    ArrayList<Item> queryResult = new ArrayList<>();

    private boolean triggered = false;

    @Override
    public void update(float delta) {
        assert world != null : "Can't update when world is null";

        queryResult.clear();
        world.queryRect(position.x, position.y, width, height, this.triggerFilter, queryResult);

        // only trigger upon entry
        if(!queryResult.isEmpty() && !triggered) {
            triggered = true;
            runnable.run();
        }

        if(queryResult.isEmpty()) {
            triggered = false;
        }
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        batch.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.PURPLE.r,  Color.PURPLE.g, Color.PURPLE.b, 0.5f);

        shape.rect(position.x, position.y, width, height);

        shape.end();

        batch.begin();
    }

    @Override
    public void dispose() {

    }
}
