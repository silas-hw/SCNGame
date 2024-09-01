package com.mygdx.scngame.entity.component;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.dongbat.jbump.Collision;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.physics.DamageBox;
import com.mygdx.scngame.physics.HitBox;
import org.jetbrains.annotations.NotNull;

public class HurtBox {

    public interface HurtListener {
        void onHit();
    }

    private HealthComponent health;
    private Item<Box> hitbox;

    private World<Box> world = null;

    private float width;
    private float height;

    Vector2 pos;

    private boolean invincible = false;
    private float invinceTime;
    private float invinceTimer = 0f;

    private boolean takesDamage = true;

    public HurtBox(@NotNull HealthComponent health, float width, float height, float invinceTime) {
        this.health = health;

        Box hit = new HitBox();
        hit.internalFilter = Box.CROSS_FILTER;
        hitbox = new Item<>(hit);

        this.width = width;
        this.height = height;

        this.invinceTime = invinceTime;

        pos = Vector2.Zero;
    }

    public void setCollisionMask(int maskLayer, boolean set) {
        hitbox.userData.setMask(maskLayer, set);
    }

    private final Array<HurtListener> listeners = new Array<>();

    public void addHurtListener(HurtListener listener) {
        listeners.add(listener);
    }

    public void removeHurtListener(HurtListener listener) {
        listeners.removeValue(listener, true);
    }

    public void update(float delta, Vector2 position) {
        assert world != null : "Are you daft? You gotta set world before updating a hurtbox otherwise its got nothin'" +
                "to move about in";

        this.pos = position;

        Response.Result res = world.move(hitbox, position.x, position.y, Box.GLOBAL_FILTER);

        if(!takesDamage) {
            return;
        }

        if(invincible && invinceTimer <= 0f) {
            invincible = false;
            return;
        }

        if(invincible) {
            invinceTimer -= delta;
            return;
        }

        for(int i = 0; i<res.projectedCollisions.size(); i++) {
            Collision col = res.projectedCollisions.get(i);

            if(col.other.userData instanceof DamageBox dBox) {
                health.applyDamage(dBox.damage);
                invincible = true;
                invinceTimer = invinceTime;

                for(HurtListener listener : listeners) {
                    listener.onHit();
                }
            }
        }
    }

    public void setTakesDamage(boolean takesDamage) {
        this.takesDamage = takesDamage;
    }

    public void setWorld(World<Box> world) {
        this.removeWorldItems();

        this.world = world;
        this.world.add(hitbox, pos.x, pos.y, width, height);
    }

    public void removeWorldItems(){
        if(this.world != null) {
            if(this.world.hasItem(hitbox)) this.world.remove(hitbox);
        }
    }
}
