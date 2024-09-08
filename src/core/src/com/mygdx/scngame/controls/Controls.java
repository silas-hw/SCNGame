package com.mygdx.scngame.controls;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.ControllerMapping;
import com.badlogic.gdx.graphics.Cursor;
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
            keyBindings.get(this.getKeycode()).removeValue(this, false);
            keyBindings.get(keycode).add(this);

            prefs.putInteger(this + "-key", keycode);
            prefs.flush();
        }

        public ControllerButtons getControllerButton() {
            return ControllerButtons.valueOf(prefs.getString(this + "-controller", defaultControllerButton.name()));
        }

        public void setControllerButton(ControllerButtons controllerButton) {
            controllerBindings.get(this.getControllerButton().ordinal()).removeValue(this, false);
            controllerBindings.get(controllerButton.ordinal()).add(this);

            prefs.putString(this + "-controller", controllerButton.name());
            prefs.flush();
        }

        public static Array<Actions> fromKeycode(int keycode) {
            return keyBindings.get(keycode);
        }

        public static Array<Actions> fromControllerButton(ControllerButtons controllerButton) {
            return controllerBindings.get(controllerButton.ordinal());
        }

        /** only set to something else for testing. Could be final and private, but this makes it easier
         * to give a mock preferences to test with
         */
        public static Preferences prefs = new MockPreferences();

        public static final Array<Array<Actions>> keyBindings = new Array<>();

        static {
            for(int i = 0; i <= Input.Keys.MAX_KEYCODE; i++) {
                keyBindings.add(new Array<>());
            }

            for(Actions action : Actions.values()) {
                keyBindings.get(action.getKeycode()).add(action);
            }
        }

        public static final Array<Array<Actions>> controllerBindings = new Array<>();

        static {
            for(int i = 0; i< ControllerButtons.values().length; i++) {
                controllerBindings.add(new Array<>());
            }

            for(Actions action : Actions.values()) {
                controllerBindings.get(action.getControllerButton().ordinal()).add(action);
            }
        }
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
    public Actions leftMouseAlias = Actions.ATTACK;
    public Actions rightMouseAlias = Actions.DASH;

    private final Array<ActionListener> listeners = new Array<>();
    private final Array<InputProcessor> inputProcessors = new Array<>();
    private final Array<ActionListener> mouseListeners = new Array<>();

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

    public void addMouseActionListener(ActionListener listener) {
        mouseListeners.add(listener);
    }

    public void removeMouseActionListener(ActionListener listener) {
        mouseListeners.removeValue(listener, true);
    }

    @Override
    public boolean keyDown(int keycode) {
        Array<Actions> actions = Actions.fromKeycode(keycode);

        for(Actions action : actions) {
            for(ActionListener listener : listeners) {
                if(listener.actionDown(action)) break;
            }
        }

        for(InputProcessor inputProcessor : inputProcessors) {
            if(inputProcessor.keyDown(keycode)) break;
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        Array<Actions> actions = Actions.fromKeycode(keycode);

        for(Actions action : actions) {
            for(ActionListener listener : listeners) {
                if(listener.actionUp(action)) break;
            }
        }

        for(InputProcessor inputProcessor : inputProcessors) {
            if(inputProcessor.keyUp(keycode)) break;
        }

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        for(InputProcessor inputProcessor : inputProcessors) {
            if(inputProcessor.keyTyped(character)) break;
        }

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        switch(button) {
            case Input.Buttons.LEFT:
                for(ActionListener listener : mouseListeners) {
                    listener.actionDown(leftMouseAlias);
                }

                break;

            case Input.Buttons.RIGHT:
                for(ActionListener listener : mouseListeners) {
                    listener.actionDown(rightMouseAlias);
                }

                break;
        }

        for(InputProcessor inputProcessor : inputProcessors) {
            if(inputProcessor.touchDown(screenX, screenY, pointer, button)) break;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        switch(button) {
            case Input.Buttons.LEFT:
                for(ActionListener listener : mouseListeners) {
                    listener.actionUp(leftMouseAlias);
                }

                break;

            case Input.Buttons.RIGHT:
                for(ActionListener listener : mouseListeners) {
                    listener.actionUp(rightMouseAlias);
                }

                break;
        }

        for(InputProcessor inputProcessor : inputProcessors) {
            if(inputProcessor.touchUp(screenX, screenY, pointer, button)) break;
        }


        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        for(InputProcessor inputProcessor : inputProcessors) {
            if(inputProcessor.touchCancelled(screenX, screenY, pointer, button)) break;
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        for(InputProcessor inputProcessor : inputProcessors) {
            if(inputProcessor.touchDragged(screenX, screenY, pointer)) break;
        }

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        for(InputProcessor inputProcessor : inputProcessors) {
            if(inputProcessor.mouseMoved(screenX, screenY)) break;
        }

        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        for(InputProcessor inputProcessor : inputProcessors) {
            if(inputProcessor.scrolled(amountX, amountY)) break;
        }

        return false;
    }

    private final Array<ControllerListener> controllerListeners = new Array<>();

    public void addControllerListener(ControllerListener listener) {
        controllerListeners.add(listener);
    }

    public void removeControllerListener(ControllerListener listener) {
        controllerListeners.removeValue(listener, true);
    }

    @Override
    public void connected(Controller controller) {
        for(ControllerListener listener : controllerListeners) {
            listener.connected(controller);
        }
    }

    @Override
    public void disconnected(Controller controller) {
        for(ControllerListener listener : controllerListeners) {
            listener.disconnected(controller);
        }
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        Array<Actions> actions = Actions.fromControllerButton(getControllerButton(controller, buttonCode));

        for(Actions action : actions) {
            for(ActionListener listener : listeners) {
                if(listener.actionDown(action)) break;
            }
        }

        for(ControllerListener listener : controllerListeners) {
            if(listener.buttonDown(controller, buttonCode)) break;
        }

        return true;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        Array<Actions> actions = Actions.fromControllerButton(getControllerButton(controller, buttonCode));

        for(Actions action : actions) {
            for(ActionListener listener : listeners) {
                if(listener.actionUp(action)) break;
            }
        }

        for(ControllerListener listener : controllerListeners) {
            if(listener.buttonUp(controller, buttonCode)) break;
        }

        return true;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        for(ControllerListener listener : controllerListeners) {
            if(listener.axisMoved(controller, axisCode, value)) break;
        }

        return true;
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