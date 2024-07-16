package com.mygdx.scngame.entity.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.scngame.dialog.DialogStart;
import com.mygdx.scngame.entity.component.InputComponent;
import com.mygdx.scngame.event.GameEvent;
import com.mygdx.scngame.event.Global;

public class PlayerInputComponent extends InputAdapter implements InputComponent<Player> {
    private boolean UP = false;
    private boolean DOWN = false;
    private boolean LEFT = false;
    private boolean RIGHT = false;
    public boolean SHIFT = false;

    public PlayerInputComponent() {
        Global.bus.addEventListener(this);
    }

    @Override
    public void update(Player container) {
        int dx = 0;
        int dy = 0;

        switch(container.getState()) {
            case DASHING:
                break;

            case MOVING:
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

                if(SHIFT) {
                    container.setState(Player.PlayerState.DASHING);
                    SHIFT = false;
                }

                container.direction.set(dx, dy);
                container.direction.nor();
        }
    }

    private final GameEvent _dialogTest = new GameEvent(this, new DialogStart("test_dialog_1"));

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

            case Input.Keys.E:
                Global.bus.fire(_dialogTest);
                break;

            case Input.Keys.SHIFT_LEFT:
                SHIFT = true;
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

            case Input.Keys.SHIFT_LEFT:
                SHIFT = false;
                break;
        }

        return false;
    }

    @Override
    public void dispose() {
        return;
    }

    @Override
    public void notify(GameEvent event) {
        if(event.getPayload() instanceof DialogStart) {
            System.out.println("Input captured dialog start");
            LEFT = false;
            RIGHT = false;
            UP = false;
            DOWN = false;
            SHIFT = false;
        }
    }
}
