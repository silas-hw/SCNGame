package com.mygdx.scngame.physics;

import com.badlogic.gdx.graphics.Color;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;

/**
 * A class representing a physics box to be used within a {@link com.dongbat.jbump.World}.
 * <p>
 * This class makes uses of masks and layers to allow for more efficient collision filtering.
 */
public class Box {
    private Color DEBUG_COLOR = Color.WHITE;

    public Color getDebugColor() {
        return this.DEBUG_COLOR;
    }

    // package private so other boxes can set their debug color
    void setDebugColor(Color debugColor) {
        this.DEBUG_COLOR = debugColor;
    }

    /** A binary value representing what layers of other boxes that this box will collide with */
    public int mask = 0b00000000;

    /** a binary value representing the layers on which the box will be collided with by other boxes */
    public int layer =  0b00000000;

    /**
     *
     * @param maskIndex the 0th indexed collision layer to set
     * @param set whether to set or unset the given layer index
     */
    public void setMask(int maskIndex, boolean set) {
        int bitmask = 1 << maskIndex;

        if(set) {
            mask = mask | bitmask;
        } else {
            mask = mask & ~bitmask;
        }
    }

    public void setMasks(int[] maskIndices, boolean set) {
        for(int index : maskIndices) {
            setMask(index, set);
        }
    }

    /**
     *
     * @param layerIndex the 0th indexed collision layer to set
     * @param set whether to set or unset the given layer index
     */
    public void setLayer(int layerIndex, boolean set) {
        int bitmask = 1 << layerIndex;

        if(set) {
            layer = layer | bitmask;
        } else {
            layer = layer & ~bitmask;
        }
    }

    public void setLayers(int[] layerIndices, boolean set) {
        for(int index : layerIndices) {
            setLayer(index, set);
        }
    }

    /**
     * determines whether a colliding Box will have a hard or soft collision with this Box
     * <p>
     * A 'soft' collision will always involve a 'cross' {@link Response}, whereas a 'hard' collision
     * involves the colliding object responding in its required way.
     */
    public boolean solid = false;

    /** The filter used if a collision occurs with a solid object whose layer matches this box's mask */
    public CollisionFilter internalFilter = CROSS_FILTER;

    public static final CollisionFilter CROSS_FILTER = new CollisionFilter() {

        @Override
        public Response filter(Item item, Item other) {
            return Response.cross;
        }
    };

    public static final CollisionFilter SLIDE_FILTER = new CollisionFilter() {

        @Override
        public Response filter(Item item, Item other) {
            return Response.slide;
        }
    };

    public static final CollisionFilter BOUNCE_FILTER = new CollisionFilter() {

        @Override
        public Response filter(Item item, Item other) {
            return Response.bounce;
        }
    };

    public static final CollisionFilter TOUCH_FILTER = new CollisionFilter() {

        @Override
        public Response filter(Item item, Item other) {
            return Response.touch;
        }
    };

    /**
     * Converst an array of mask indices into a single integer bitmask. For example, giving
     * <code>{0, 1, 2, 6}</code> would return <code>1000111</code>. <p>
     *
     * Indices given multiple times are only counted once.
     *
     * @param indices a list of mask indexes to set
     * @return the resulting bit mask
     */
    public static final int getMaskFromIndices(int[] indices) {
        int out = 0;

        for(int index : indices) {
            // prevents an index being put in twice messing things up
            if((out & 1 << index) != 0) continue;
            out += 1 << index;
        }

        return out;
    }

    public static final CollisionFilter GLOBAL_FILTER = new globalFilter();

    /**
     * The expected filter to be used by any collision involving a {@link Box}.
     * <p>
     * If the box being collided with is solid, then the colliding box's {@link Response}
     * is returned. Otherwise, simply a 'cross' response is returned.
     * <p>
     * The mask and layer of each box is checked first to determine if a collision <i>should</i>
     * occur.
     */
    private static class globalFilter implements CollisionFilter {

        @Override
        public Response filter(Item item, Item other) {
            if(item == null) return null;
            if(other == null) return Response.cross;

            // if not a Box, escape
            if(!(item.userData instanceof Box) || !(other.userData instanceof Box)) {
                return null;
            }

            Box box = (Box) item.userData;
            Box otherBox = (Box) other.userData;

            // if the colliding box's mask doesn't match the collided box's layer

            boolean intersects = (otherBox.layer & box.mask) != 0;
            boolean collides = intersects & otherBox.solid;

            if(collides) {
                return box.internalFilter.filter(item, other);
            } else if(intersects) {
                return Response.cross;
            }

            return null;
        }
    }
}