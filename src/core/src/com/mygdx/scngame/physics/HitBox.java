package com.mygdx.scngame.physics;

import com.badlogic.gdx.graphics.Color;

/**
 * Convenience class so that a physics component may describe some
 * {@link com.dongbat.jbump.Item item} as a hit box.
 */
public class HitBox extends Box {
    Color DEBUG_COLOR = Color.BLUE;
    public boolean solid = false;

    public HitBox() {
        super.setDebugColor(Color.BLUE);
    }
}
