package com.mygdx.scngame.entity.player.states;

import com.badlogic.gdx.math.MathUtils;
import com.dongbat.jbump.Item;
import com.mygdx.scngame.controls.Controls;
import com.mygdx.scngame.entity.EntityState;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.physics.InteractBox;
import com.mygdx.scngame.physics.Interactable;

import java.util.ArrayList;

public class PlayerDefaultState extends PlayerMoveState {

    @Override
    public EntityState<? super Player> update(float delta) {
        EntityState<? super Player> nextState = super.update(delta);

        rayStart.y = container.position.y + container.HEIGHT/2f;
        rayStart.x = container.position.x + container.WIDTH/2f;
        rayEnd.x = rayStart.x;
        rayEnd.y = rayStart.y;

        switch(facing) {
            case UP:
                rayEnd.y += container.interactDistance;
                break;

            case DOWN:
                rayEnd.y += -container.interactDistance;
                break;

            case LEFT:
                rayEnd.x += -container.interactDistance;
                break;

            case RIGHT:
                rayEnd.x += container.interactDistance;
                break;

        }

        if(container.context.isActionJustPressed(Controls.Actions.INTERACT)) {
            // query world for interact boxes

            ArrayList<Item> arr = new ArrayList<>();

            // ray cast from center of body
            world.querySegment(rayStart.x, rayStart.y, rayEnd.x, rayEnd.y, InteractBox.INTERACT_FILTER, arr);

            if(arr.size() > 0) {
                Interactable interactBox = (Interactable) arr.get(0).userData;
                interactBox.interact();
            }
        }

        if(container.direction.isZero()) {
            container.position.x = MathUtils.round(container.position.x);
            container.position.y = MathUtils.round(container.position.y);
        }

        return nextState;
    }
}
