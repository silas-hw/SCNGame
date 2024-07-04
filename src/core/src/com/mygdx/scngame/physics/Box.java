package com.mygdx.scngame.physics;

import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;

/**
 * A class representing a physics box to be used within a {@link com.dongbat.jbump.World}.
 * <p>
 * This class makes uses of masks and layers to allow for more efficient collision filtering.
 */
public class Box {

    /** A binary value representing what layers of other boxes that this box will collide with */
    public byte mask = 0b00000000;

    /** a binary value representing the layers on which the box will be collided with by other boxes */
    public byte layer =  0b00000000;

    /** determines whether a colliding Box will have a hard or soft collision with this Box */
    public boolean solid = false;

    /** The response returned if this Box has a hard collision with another Box */
    public Response response = Response.cross;

    public static final CollisionFilter GLOBAL_FILTER = new globalFilter();

    /**
     * The expected filter to be used by any collision involving a {@link Box}.
     *
     * If the box being collided with is solid, then the colliding box's response
     * is returned. Otherwise, simply a 'cross' response is returned.
     */
    public static class globalFilter implements CollisionFilter {

        @Override
        public Response filter(Item item, Item other) {
            // if not a Box, escape
            if(!(item.userData instanceof Box) || !(other.userData instanceof Box)) {
                return null;
            }

            Box box = (Box) item.userData;
            Box otherBox = (Box) other.userData;

            // if the colliding box's mask doesn't match the collided box's layer
            if((otherBox.layer & box.mask) == 0) {
                return null;
            }

            System.out.println("Collision occuring!");

            if(otherBox.solid) return box.response;
            return Response.cross;
        }
    }

}
