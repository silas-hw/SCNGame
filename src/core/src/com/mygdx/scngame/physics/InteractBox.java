package com.mygdx.scngame.physics;

import com.badlogic.gdx.graphics.Color;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;

public abstract class InteractBox extends Box {
    public abstract void interact();

    public InteractBox() {
        super.setDebugColor(Color.GREEN);
        this.solid = false;
    }

    public static final CollisionFilter INTERACT_FILTER = new globalFilter();

    private static class globalFilter implements CollisionFilter {

        @Override
        public Response filter(Item item, Item other) {
            if(item.userData instanceof InteractBox) {
                return Response.cross;
            }

            return null;
        }
    }
}
