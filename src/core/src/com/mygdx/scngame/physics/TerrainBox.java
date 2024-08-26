package com.mygdx.scngame.physics;

import com.badlogic.gdx.graphics.Color;

public class TerrainBox extends Box {

    public float speedCoefficient = 0f;

    public TerrainBox(float speedCoefficient) {
        this.speedCoefficient = speedCoefficient;

        super.setDebugColor(Color.BROWN);
        super.solid = false;
    }
}
