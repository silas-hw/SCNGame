package com.mygdx.scngame.entity.player;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.mygdx.scngame.entity.InputComponent;

public class PlayerInputComponent extends InputListener implements InputComponent<Player> {
    @Override
    public void update(Player container) {
        System.out.println("Player is being updated...");
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        System.out.println("Key pressed " + keycode);
        return false;
    }

    @Override
    public boolean keyUp(InputEvent event, int keycode) {
        return super.keyUp(event, keycode);
    }

    @Override
    public boolean keyTyped(InputEvent event, char character) {
        return super.keyTyped(event, character);
    }
}
