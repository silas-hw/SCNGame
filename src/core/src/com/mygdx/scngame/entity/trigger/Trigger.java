package com.mygdx.scngame.entity.trigger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import com.mygdx.scngame.entity.Entity;
import com.mygdx.scngame.physics.Box;

import java.util.ArrayList;

public class Trigger extends Entity {

    private final float width;
    private final float height;

    private final CollisionFilter queryFilter;

    private final Runnable runnable;


    public Trigger(float x, float y, float width, float height, int[] maskLayers, Runnable runnable) {
        position.x = x;
        position.y = y;

        this.width = width;
        this.height = height;

        int bitMask = Box.getMaskFromIndices(maskLayers);

        this.queryFilter = new Box.QueryFilter(bitMask);
        this.runnable = runnable;
    }

    ArrayList<Item> queryResult = new ArrayList<>();

    private boolean triggered = false;

    @Override
    public void update(float delta) {
        assert world != null : "Can't update when world is null";

        queryResult.clear();
        world.queryRect(position.x, position.y, width, height, this.queryFilter, queryResult);

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
