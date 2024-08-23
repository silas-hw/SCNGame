package com.mygdx.scngame.controls;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.ControllerMapping;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;
import java.util.List;

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
        INTERACT (Input.Keys.E, ControllerButtons.buttonX),
        MENU (Input.Keys.ESCAPE, ControllerButtons.buttonStart);

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
            prefs.putInteger(this + "-key", keycode);
            prefs.flush();
        }

        public ControllerButtons getControllerButton() {
            return ControllerButtons.valueOf(prefs.getString(this + "-controller", defaultControllerButton.name()));
        }

        public void setControllerButton(ControllerButtons controllerButton) {
            prefs.putString(this + "-controller", controllerButton.name());
        }

        public static List<Actions> fromKeycode(int keycode) {
            return Arrays.stream(Actions.values()).filter(e -> e.getKeycode() == keycode).toList();
        }

        public static List<Actions> fromControllerButton(ControllerButtons controllerButton) {
            return Arrays.stream(Actions.values()).filter(e -> e.getControllerButton() == controllerButton).toList();
        }

        /** only set to something else for testing. Could be final and private, but this makes it easier
         * to give a mock preferences to test with
         */
        public static Preferences prefs = new MockPreferences();
    }

    public static void initPreferences() {
        Actions.prefs =  Gdx.app.getPreferences("scngame/controls");
    }

    private static Controls instance = null;

    public static Controls getInstance() {
        if (instance == null) {
            instance = new Controls();
        }

        return instance;
    }

    private final Array<ActionListener> listeners = new Array<>();
    private final Array<InputProcessor> inputProcessors = new Array<>();

    public void addActionListener(ActionListener listener) {
        listeners.add(listener);
    }

    public void removeActionListener(ActionListener listener) {
        listeners.removeValue(listener, true);
    }

    public void addInputProcessor(InputProcessor inputProcessor) {
        inputProcessors.add(inputProcessor);
    }

    public void removeInputProcessor(InputProcessor inputProcessor) {
        inputProcessors.removeValue(inputProcessor, true);
    }

    @Override
    public boolean keyDown(int keycode) {
        for(InputProcessor inputProcessor : inputProcessors) {
            inputProcessor.keyDown(keycode);
        }

        List<Actions> actions = Actions.fromKeycode(keycode);

        if(actions.isEmpty()) return false;

        for(Actions action : actions) {
            for(ActionListener listener : listeners) {
                if(listener.actionDown(action)) break;
            }
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        for(InputProcessor inputProcessor : inputProcessors) {
            inputProcessor.keyUp(keycode);
        }

        List<Actions> actions = Actions.fromKeycode(keycode);

        if(actions.isEmpty()) return false;

        for(Actions action : actions) {
            for(ActionListener listener : listeners) {
                if(listener.actionUp(action)) break;
            }
        }

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        for(InputProcessor inputProcessor : inputProcessors) {
            inputProcessor.keyTyped(character);
        }

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        for(InputProcessor inputProcessor : inputProcessors) {
            inputProcessor.touchDown(screenX, screenY, pointer, button);
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        for(InputProcessor inputProcessor : inputProcessors) {
            inputProcessor.touchUp(screenX, screenY, pointer, button);
        }

        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        for(InputProcessor inputProcessor : inputProcessors) {
            inputProcessor.touchCancelled(screenX, screenY, pointer, button);
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        for(InputProcessor inputProcessor : inputProcessors) {
            inputProcessor.touchDragged(screenX, screenY, pointer);
        }

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        for(InputProcessor inputProcessor : inputProcessors) {
            inputProcessor.mouseMoved(screenX, screenY);
        }

        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        for(InputProcessor inputProcessor : inputProcessors) {
            inputProcessor.scrolled(amountX, amountY);
        }

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
        List<Actions> actions = Actions.fromControllerButton(getControllerButton(controller, buttonCode));

        if(actions.isEmpty()) return false;

        for(Actions action : actions) {
            for(ActionListener listener : listeners) {
                if(listener.actionDown(action)) break;
            }
        }

        return true;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        List<Actions> actions = Actions.fromControllerButton(getControllerButton(controller, buttonCode));

        if(actions.isEmpty()) return false;

        for(Actions action : actions) {
            for(ActionListener listener : listeners) {
                if(listener.actionUp(action)) break;
            }
        }

        return true;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        return false;
    }

    public enum ControllerButtons {
        buttonA ("↧"),
        buttonB ("↦"),
        buttonX ("↤"),
        buttonY ("↥"),
        buttonDpadLeft ("↞") ,
        buttonDpadRight ("↠"),
        buttonDpadDown ("↡"),
        buttonDpadUp ("↟"),
        buttonR1 ("↱"),
        buttonL1 ("↰"),
        buttonBack ("⇷"),
        buttonStart ("⇸"),
        buttonLeftStick ("⇋"),
        buttonRightStick ("⇌");

        private final String displayText;

        ControllerButtons(String displayText){
            this.displayText = displayText;
        }

        public String getDisplayText(){return this.displayText;}
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