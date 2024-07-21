package com.mygdx.scngame.entity.player.states;

import com.dongbat.jbump.*;
import com.mygdx.scngame.dialog.DialogStart;
import com.mygdx.scngame.entity.EntityState;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.event.GameEvent;
import com.mygdx.scngame.event.Global;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.physics.DamageBox;

public class PlayerMoveState extends PlayerState {


    private float health = 500f;

    private float invisTimer = 0f;
    private final float invisTime = 0.3f;
    private boolean invis = false;

    @Override
    public EntityState<? super Player> update(float delta) {
        super.update(delta);

        int dx = 0;
        int dy = 0;

        if(container.INPUT_UP) {
            dy++;
        }

        if(container.INPUT_DOWN) {
            dy--;
        }

        if(container.INPUT_LEFT) {
            dx--;
        }

        if(container.INPUT_RIGHT) {
            dx++;
        }

        container.direction.set(dx, dy);
        container.direction.nor();

        if(container.INPUT_SHIFT) {
            container.INPUT_SHIFT = false;
            return new PlayerDashState();
        }

        if(container.INPUT_INTERACT) {
            Global.bus.fire(new GameEvent(container, new DialogStart("test_dialog_1")));
        }

        container.position.mulAdd(container.direction, 500f*delta);

        world.move(collisionItem, container.position.x, container.position.y, Box.GLOBAL_FILTER);
        Rect rect = world.getRect(collisionItem);
        container.position.x = rect.x;
        container.position.y = rect.y;

        Response.Result res = world.move(hitbox, container.position.x, container.position.y, Box.GLOBAL_FILTER);

        if(invis && invisTimer < invisTime) {
            invisTimer += delta;
        } else if(invis) {
            invis = false;
            invisTimer = 0f;
        }

        if(!invis) {
            for(int i = 0; i<res.projectedCollisions.size(); i++) {
                Collision col = res.projectedCollisions.get(i);

                if(col.other.userData instanceof DamageBox) {
                    DamageBox dBox = (DamageBox) col.other.userData;

                    health -= dBox.damage;
                    invis = true;
                }

            }
        }

        return null;
    }
}
