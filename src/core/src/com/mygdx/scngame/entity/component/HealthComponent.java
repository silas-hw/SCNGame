package com.mygdx.scngame.entity.component;

import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class HealthComponent {
    private float maxHealth;
    private float health;

    public HealthComponent(float maxHealth) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public float getMaxHealth() {return maxHealth;}
    public float getHealth() {return health;}
    public boolean isDead() {return health <= 0;}

    public void applyDamage(float damage) {
        this.health -= damage;

        if(this.health > 0) {
            return;
        }

        for(DeathListener listener : deathListeners) {
            listener.onDeath();
        }
    }

    public void heal(float amount) {
        this.health = Math.max(this.health + amount, maxHealth);
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
        this.health = Math.min(this.health, this.maxHealth);
    }

    public interface DeathListener {
        void onDeath();
    }

    final Array<DeathListener> deathListeners = new Array<>();

    public void addDeathListener(DeathListener listener) {
        deathListeners.add(listener);
    }

    public void removeDeathListener(DeathListener listener) {
        deathListeners.removeValue(listener, true);
    }
}
