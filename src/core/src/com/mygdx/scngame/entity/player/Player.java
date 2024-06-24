package com.mygdx.scngame.entity.player;

import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.Entity;
import com.mygdx.scngame.entity.InputComponent;

public class Player extends Entity {
    InputComponent<Player> inputComponent;
    public Player() {
        this.inputComponent = new PlayerInputComponent();
    }
    @Override
    public void update(World<?> world, float delta) {
        this.inputComponent.update(this);
    }
}
