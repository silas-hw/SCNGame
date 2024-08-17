package com.mygdx.scngame.settings;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerMapping;
import com.badlogic.gdx.controllers.Controllers;

/**
 * Manages aliases for keyboard inputs such that they can be remapped by the user. <p>
 *
 * Keymappings will be saved in the default {@link Preferences} directory, in the subdirectory
 * <code>scngame/controls</code>
 */
public enum Controls {

    UP (Input.Keys.W, ControllerButtons.buttonDpadUp),
    DOWN (Input.Keys.S, ControllerButtons.buttonDpadDown),
    LEFT (Input.Keys.A, ControllerButtons.buttonDpadLeft),
    RIGHT (Input.Keys.D, ControllerButtons.buttonDpadRight),
    ATTACK (Input.Keys.J, ControllerButtons.buttonA),
    DASH (Input.Keys.SHIFT_LEFT, ControllerButtons.buttonR1),
    INTERACT (Input.Keys.E, ControllerButtons.buttonX);

    public final int defaultKey;
    public final ControllerButtons defaultControllerButton;

    Controls(int key, ControllerButtons control) {
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