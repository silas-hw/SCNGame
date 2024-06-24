package com.mygdx.scngame.entity.player;

import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.PhysicsComponent;

public class PlayerPhysicsComponent implements PhysicsComponent<Player> {
    @Override
    public void update(Player container, World<?> world, float delta) {
        container.position.mulAdd(container.direction, 1000f*delta);
    }

    @Override
    public void dispose() {

    }
}
