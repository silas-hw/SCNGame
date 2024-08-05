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
public class Controls {

    public static final int DEFAULT_UPKEY = Input.Keys.W;
    public static final int DEFAULT_DOWNKEY = Input.Keys.S;
    public static final int DEFAULT_LEFTKEY = Input.Keys.A;
    public static final int DEFAULT_RIGHTKEY = Input.Keys.D;
    public static final int DEFAULT_ACTIONKEY = Input.Keys.E;
    public static final int DEFAULT_ATTACKKEY = Input.Keys.J;
    public static final int DEFAULT_DASHKEY = Input.Keys.SHIFT_LEFT;

    private static final String PREFS_UPKEY = "upKey";
    private static final String PREFS_DOWNKEY = "downKey";
    private static final String PREFS_LEFTKEY = "leftKey";
    private static final String PREFS_RIGHTKEY = "rightKey";
    private static final String PREFS_ACTIONKEY = "actionKey";
    private static final String PREFS_ATTACKKEY = "atackKey";
    private static final String PREFS_DASHKEY = "dashKey";

    private static final String PREFS_NAME = "scngame/controls";

    private static Preferences prefs;

    private static Preferences getPrefs() {
        if(prefs == null) {
            prefs = Gdx.app.getPreferences(PREFS_NAME);
        }

        return prefs;
    };

    public static int getUpKey() {
        return getPrefs().getInteger(PREFS_UPKEY, DEFAULT_UPKEY);
    }

    public static void setUpKey(int upKey) {
        getPrefs().putInteger(PREFS_UPKEY, upKey);
        getPrefs().flush();
    }

    public static int getDownKey() {
        return getPrefs().getInteger(PREFS_DOWNKEY, DEFAULT_DOWNKEY);
    }

    public static void setDownKey(int downKey) {
        getPrefs().putInteger(PREFS_DOWNKEY, downKey);
        getPrefs().flush();
    }

    public static int getLeftKey() {
        return getPrefs().getInteger(PREFS_LEFTKEY, DEFAULT_LEFTKEY);
    }

    public static void setLeftKey(int leftKey) {
        getPrefs().putInteger(PREFS_LEFTKEY, leftKey);
        getPrefs().flush();
    }

    public static int getRightKey() {
        return getPrefs().getInteger(PREFS_RIGHTKEY, DEFAULT_RIGHTKEY);
    }

    public static void setRightKey(int rightKey) {
        getPrefs().putInteger(PREFS_RIGHTKEY, rightKey);
        getPrefs().flush();
    }

    public static int getActionKey() {
        return getPrefs().getInteger(PREFS_ACTIONKEY, DEFAULT_ACTIONKEY);
    }

    public static void setActionKey(int actionKey) {
        getPrefs().putInteger(PREFS_ACTIONKEY, actionKey);
        getPrefs().flush();
    }

    public static int getDashKey() {
        return getPrefs().getInteger(PREFS_ATTACKKEY, DEFAULT_DASHKEY);
    }

    public static void setDashKey(int dashKey) {
        getPrefs().putInteger(PREFS_DASHKEY, dashKey);
        getPrefs().flush();
    }

    public static int getAttackKey() {
        return getPrefs().getInteger(PREFS_ATTACKKEY, DEFAULT_ATTACKKEY);
    }

    public static void setAttackKey(int attackKey) {
        getPrefs().putInteger(PREFS_ATTACKKEY, attackKey);
        getPrefs().flush();
    }
}