package com.mygdx.scngame.viewport;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class PixelPerfectExtendViewport extends ExtendViewport {
    private static PixelFitScaling scale = new PixelFitScaling();

    public PixelPerfectExtendViewport(float minWorldWidth, float minWorldHeight) {
        super(minWorldWidth, minWorldHeight);
    }

    public PixelPerfectExtendViewport(float minWorldWidth, float minWorldHeight, Camera camera) {
        super(minWorldWidth, minWorldHeight, camera);
    }

    public PixelPerfectExtendViewport(float minWorldWidth, float minWorldHeight, float maxWorldWidth, float maxWorldHeight) {
        super(minWorldWidth, minWorldHeight, maxWorldWidth, maxWorldHeight);
    }

    public PixelPerfectExtendViewport(float minWorldWidth, float minWorldHeight, float maxWorldWidth, float maxWorldHeight, Camera camera) {
        super(minWorldWidth, minWorldHeight, maxWorldWidth, maxWorldHeight, camera);

        this.setScaling(scale);
    }
}
