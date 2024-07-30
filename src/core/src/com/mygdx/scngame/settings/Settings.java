package com.mygdx.scngame.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

    private final static String PREF_SFX_VOL = "sfxVol";
    private final static String PREF_MUSIC_VOL = "musicVol";
    private final static String PREF_SFX_ON = "sfxOn";
    private final static String PREF_MUSIC_ON = "musicOn";

    private final static String PREF_DIALOG_SCALE = "dialogScale";
    private final static String PREF_MENU_SCALE = "menuScale";
    private final static String PREF_HUD_SCALE = "hudScale";

    private final static String PREFS_NAME = "scngame/user_settings";

    private static Preferences prefs;

    private static Preferences getPrefs() {
        if(prefs == null) {
            prefs = Gdx.app.getPreferences(PREFS_NAME);
        }

        return prefs;
    };

    public static float getSfxVol() {
        return getPrefs().getFloat(PREF_SFX_VOL, 1.0f);
    }

    public static void setSfxVol(float sfxVol) {
        getPrefs().putFloat(PREF_SFX_VOL, sfxVol);
        getPrefs().flush();
    }

    public static float getMusicVolume() {
        return getPrefs().getFloat(PREF_MUSIC_VOL, 1.0f);
    }

    public static void setMusicVolume(float musicVolume) {
        getPrefs().putFloat(PREF_MUSIC_VOL, musicVolume);
        getPrefs().flush();
    }

    public static boolean isSfxOn() {
        return getPrefs().getBoolean(PREF_SFX_ON, true);
    }

    public static void setSfxOn(boolean sfxOn) {
        getPrefs().putBoolean(PREF_SFX_ON, sfxOn);
        getPrefs().flush();
    }

    public static boolean isMusicOn() {
        return getPrefs().getBoolean(PREF_MUSIC_ON, true);
    }

    public static void setMusicOn(boolean musicOn) {
        getPrefs().putBoolean(PREF_MUSIC_ON, musicOn);
        getPrefs().flush();
    }

    public static float getDialogScale() {
        return getPrefs().getFloat(PREF_DIALOG_SCALE, 1.0f);
    }

    public static void setDialogScale(float dialogScale) {
        getPrefs().putFloat(PREF_DIALOG_SCALE, dialogScale);
        getPrefs().flush();
    }

    public static float getMenuScale() {
        return getPrefs().getFloat(PREF_MENU_SCALE, 1.0f);
    }

    public static void setMenuScale(float menuScale) {
        getPrefs().putFloat(PREF_MENU_SCALE, menuScale);
        getPrefs().flush();
    }

    public static float getHudScale() {
        return getPrefs().getFloat(PREF_HUD_SCALE, 1.0f);
    }

    public static void setHudScale(float hudScale) {
        getPrefs().putFloat(PREF_HUD_SCALE, hudScale);
        getPrefs().flush();
    }
}
