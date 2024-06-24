package com.mygdx.scngame.entity.player;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.mygdx.scngame.entity.InputComponent;

public class PlayerInputComponent extends InputAdapter implements InputComponent<Player> {
    @Override
    public void update(Player container) {
        return;
    }

    @Override
    public boolean keyDown(int keycode) {
        System.out.println("Key pressed " + keycode);
        return false;
    }

    @Override
    public void dispose() {
        return;
    }
}
