package com.mygdx.scngame.physics;

public abstract class InteractBox extends Box {
    public boolean solid = false;
    public abstract void interact();
}
