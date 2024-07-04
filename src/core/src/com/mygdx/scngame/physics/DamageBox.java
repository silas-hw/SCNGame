package com.mygdx.scngame.physics;

/**
 * Used by the physics engine to apply damage to an entity. An entity can detect its own
 * collision with a DamageBox and determine how to apply the damage to itself.
 */
public class DamageBox extends Box {
    public float damage;
    public Source source;
    public DamageType type;

    public boolean solid = false;

    public enum Source {
        PLAYER,
        ENEMY,
        NPC,
        ENVIRONMENT
    }

    public enum DamageType {
        DEFAULT
    }

    public DamageBox(float damage, Source source, DamageType type) {
        this.damage = damage;
        this.source = source;
        this.type = type;
    }
}
