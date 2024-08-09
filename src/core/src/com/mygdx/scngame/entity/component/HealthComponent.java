package com.mygdx.scngame.entity.component;

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
    }

    public void heal(float amount) {
        this.health = Math.max(this.health + amount, maxHealth);
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
        this.health = Math.min(this.health, this.maxHealth);
    }
}
