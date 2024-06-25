package com.mygdx.scngame.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.mygdx.scngame.entity.InputComponent;

public class PlayerInputComponent extends InputAdapter implements InputComponent<Player> {
    private boolean UP = false;
    private boolean DOWN = false;
    private boolean LEFT = false;
    private boolean RIGHT = false;

    @Override
    public void update(Player container) {
        int dx = 0;
        int dy = 0;

        if(UP) {
            dy++;
        }

        if(DOWN) {
            dy--;
        }

        if(LEFT) {
            dx--;
        }

        if(RIGHT) {
            dx++;
        }

        container.direction.set(dx, dy);
        container.direction.nor();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode) {
            case Input.Keys.W:
                UP = true;
                break;

            case Input.Keys.S:
                DOWN = true;
                break;

            case Input.Keys.D:
                RIGHT = true;
                break;

            case Input.Keys.A:
                LEFT = true;
                break;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode) {
            case Input.Keys.W:
                UP = false;
                break;

            case Input.Keys.S:
                DOWN = false;
                break;

            case Input.Keys.D:
                RIGHT = false;
                break;

            case Input.Keys.A:
                LEFT = false;
                break;
        }

        return false;
    }

    @Override
    public void dispose() {
        return;
    }
}
