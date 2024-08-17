package com.mygdx.scngame.controls;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.ControllerMapping;
import com.badlogic.gdx.utils.Array;

/**
 * Converts keycodes and controller codes into {@link Actions} for use by sub-systems.
 */
public class Controls implements InputProcessor, ControllerListener {

    /**
     * Manages aliases for keyboard inputs such that they can be remapped by the user. <p>
     *
     * Keymappings will be saved in the default {@link Preferences} directory, in the subdirectory
     * <code>scngame/controls</code>
     */
    public enum Actions {
        UP (Input.Keys.W, ControllerButtons.buttonDpadUp),
        DOWN (Input.Keys.S, ControllerButtons.buttonDpadDown),
        LEFT (Input.Keys.A, ControllerButtons.buttonDpadLeft),
        RIGHT (Input.Keys.D, ControllerButtons.buttonDpadRight),
        ATTACK (Input.Keys.J, ControllerButtons.buttonA),
        DASH (Input.Keys.SHIFT_LEFT, ControllerButtons.buttonR1),
        INTERACT (Input.Keys.E, ControllerButtons.buttonX);

        public final int defaultKey;
        public final ControllerButtons defaultControllerButton;

        Actions(int key, ControllerButtons control) {
            this.defaultKey = key;
            this.defaultControllerButton = control;
        }

        public int getKeycode() {
            return prefs.getInteger(this + "-key", defaultKey);
        }

        public void setKeycode(int keycode) {
            prefs.putInteger(this.toString(), keycode);
            prefs.flush();
        }

        public ControllerButtons getControllerButton() {
            return ControllerButtons.valueOf(prefs.getString(this + "-controller", defaultControllerButton.name()));
        }

        public void setControllerButton(ControllerButtons controllerButton) {
            prefs.putString(this + "-controller", controllerButton.name());
        }

        private final static Preferences prefs = Gdx.app.getPreferences("scngame/controls");
    }

    private static Controls instance = null;

    public static Controls getInstance() {
        if (instance == null) {
            instance = new Controls();
        }

        return instance;
    }

    private static Array<ActionListener> listeners = new Array<>();

    public void addActionListener(ActionListener listener) {
        listeners.add(listener);
    }

    public void removeActionListener(ActionListener listener) {
        listeners.removeValue(listener, true);
    }

    @Override
    public boolean keyDown(int keycode) {
        Actions action = null;

        for(Actions val : Actions.values()) {
            if (val.getKeycode() == keycode) {
                action = val;
                break;
            }
        }

        if(action == null) return false;

        for(ActionListener listener : listeners) {
            if(listener.actionDown(action)) break;
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        Actions action = null;

        for(Actions val : Actions.values()) {
            if (val.getKeycode() == keycode) {
                action = val;
                break;
            }
        }

        if(action == null) return false;
        for(ActionListener listener : listeners) {
            if(listener.actionUp(action)) break;
        }

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public void connected(Controller controller) {

    }

    @Override
    public void disconnected(Controller controller) {

    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        ControllerButtons button = getControllerButton(controller, buttonCode);

        Actions action = null;
        for(Actions val : Actions.values()) {
            if(val.getControllerButton() == button) {
                action = val;
                break;
            }
        }

        if(action == null) return false;

        for(ActionListener listener : listeners) {
            if(listener.actionDown(action)) break;
        }

        return true;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        ControllerButtons button = getControllerButton(controller, buttonCode);
        Actions action = null;
        for(Actions val : Actions.values()) {
            if(val.getControllerButton() == button) {
                action = val;
                break;
            }
        }

        if(action == null) return false;

        for(ActionListener listener : listeners) {
            if(listener.actionUp(action)) break;
        }

        return true;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        return false;
    }

    public enum ControllerButtons {
        buttonA,
        buttonB,
        buttonX,
        buttonY,
        buttonDpadLeft,
        buttonDpadRight,
        buttonDpadDown,
        buttonDpadUp,
        buttonR1,
        buttonL1,
        buttonBack,
        buttonStart,
        buttonLeftStick,
        buttonRightStick,
    }

    public static ControllerButtons getControllerButton(Controller controller, int code) {
        assert controller != null : "controller is null";

        ControllerMapping mapping = controller.getMapping();
        if(mapping.buttonA == code) return ControllerButtons.buttonA;
        if(mapping.buttonB == code) return ControllerButtons.buttonB;
        if(mapping.buttonX == code) return ControllerButtons.buttonX;
        if(mapping.buttonY == code) return ControllerButtons.buttonY;
        if(mapping.buttonDpadLeft == code) return ControllerButtons.buttonDpadLeft;
        if(mapping.buttonDpadRight == code) return ControllerButtons.buttonDpadRight;
        if(mapping.buttonDpadUp == code) return ControllerButtons.buttonDpadUp;
        if(mapping.buttonDpadDown == code) return ControllerButtons.buttonDpadDown;
        if(mapping.buttonR1 == code) return ControllerButtons.buttonR1;
        if(mapping.buttonL1 == code) return ControllerButtons.buttonL1;
        if(mapping.buttonLeftStick == code) return ControllerButtons.buttonLeftStick;
        if(mapping.buttonRightStick == code) return ControllerButtons.buttonRightStick;
        if(mapping.buttonBack == code) return ControllerButtons.buttonBack;
        if(mapping.buttonStart == code) return ControllerButtons.buttonStart;
        return null;
    }
}