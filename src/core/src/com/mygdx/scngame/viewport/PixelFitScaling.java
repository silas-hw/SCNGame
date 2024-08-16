package com.mygdx.scngame.viewport;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;

public class PixelFitScaling extends Scaling {
    private final Vector2 temp = new Vector2();

    @Override
    public Vector2 apply(float sourceWidth, float sourceHeight, float targetWidth, float targetHeight) {
        float targetRatio = targetHeight / targetWidth;
        float sourceRatio = sourceHeight / sourceWidth;

        // make the source width slighty bigger so it has integer dimensions
        // ensuring there is an exact mapping for pixels
        int scale = (int) Math.ceil(targetRatio > sourceRatio ? targetWidth / sourceWidth : targetHeight / sourceHeight);
        temp.x = sourceWidth * scale;
        temp.y = sourceHeight * scale;
        return temp;
    }
}