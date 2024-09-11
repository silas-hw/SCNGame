package com.mygdx.scngame.physics;

import com.badlogic.gdx.graphics.Color;

import java.awt.*;

/**
 * Used by the physics engine to apply damage to an entity. An entity can detect its own
 * collision with a DamageBox and determine how to apply the damage to itself.
 */
public class DamageBox extends Box {
    public float damage;
    public float knockback = 0f;
    public DamageType type;

    public boolean solid = false;

    public enum DamageType {
        DEFAULT
    }

    public DamageBox(float damage, DamageType type) {
        this(damage, 15f, type);
    }

    public DamageBox(float damage, float knockback, DamageType type) {
        this.damage = damage;
        this.type = type;
        this.knockback = knockback;

        super.setDebugColor(Color.RED);
    }
}
