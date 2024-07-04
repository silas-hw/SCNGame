package com.mygdx.scngame.physics;

import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;

/**
 * A class representing a physics box to be used within a {@link com.dongbat.jbump.World}.
 * <p>
 * This class makes uses of masks and layers to allow for more efficient collision filtering.
 * It is expected for an {@link com.mygdx.scngame.entity.Entity} to make use of the provided
 * static {@link CollisionFilter filter}, simply named {@link global_filter} for all collision.
 *
 * <p>
 *
 * Instead of an entity requiring its own filter, it can instead register its collision boxes
 * to specific a specific layer and mask.
 * <p>
 * Layers and masks are represented as a byte, allowing for 8 different collision layers.
 */
public class Box {

    /** A binary value representing what layers of other boxes that this box will collide with */
    public byte mask = 0b00000000;

    /** a binary value representing the layers on which the box will be collided with by other boxes */
    public byte layer =  0b00000000;

    /** the {@link com.dongbat.jbump.Response} returned when this box is collided with */
    public final Response response = Response.slide;

    public static class global_filter implements CollisionFilter {

        @Override
        public Response filter(Item item, Item other) {

            // if not a Box, escape
            if(!(item.userData instanceof Box) || !(other.userData instanceof Box)) {
                return null;
            }

            Box box = (Box) item.userData;
            Box otherBox = (Box) other.userData;

            if((box.layer & otherBox.mask) > 0) {
                return box.response;
            }

            return null;
        }
    }
}
