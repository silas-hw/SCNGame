package com.mygdx.scngame.settings;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;

/**
 * Manages aliases for keyboard inputs such that they can be remapped by the user. <p>
 *
 * Keymappings will be saved in the default {@link Preferences} directory, in the subdirectory
 * <code>scngame/controls</code>
 */
public enum Controls {

    UP (Input.Keys.W),
    DOWN (Input.Keys.S),
    LEFT (Input.Keys.A),
    RIGHT (Input.Keys.D),
    ATTACK (Input.Keys.J),
    DASH (Input.Keys.SHIFT_LEFT),
    INTERACT (Input.Keys.E);

    public final int defaultKey;

    Controls(int key) {
        this.defaultKey = key;
    }

    public int getKeycode() {
        return prefs.getInteger(this.toString(), defaultKey);
    }

    public void setKeycode(int keycode) {
        prefs.putInteger(this.toString(), keycode);
        prefs.flush();
    }



    private final static Preferences prefs = Gdx.app.getPreferences("scngame/controls");
}