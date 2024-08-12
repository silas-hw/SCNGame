package com.mygdx.scngame.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PrefSettings implements Settings {

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

    private static PrefSettings instance = null;

    private PrefSettings() {};
    public static PrefSettings getInstance() {
        if(instance == null) {
            instance = new PrefSettings();
        }

        return instance;
    }

    public float getSfxVol() {
        return getPrefs().getFloat(PREF_SFX_VOL, 1.0f);
    }

    public void setSfxVol(float sfxVol) {
        getPrefs().putFloat(PREF_SFX_VOL, sfxVol);
        getPrefs().flush();
    }

    public float getMusicVolume() {
        return getPrefs().getFloat(PREF_MUSIC_VOL, 1.0f);
    }

    public void setMusicVolume(float musicVolume) {
        getPrefs().putFloat(PREF_MUSIC_VOL, musicVolume);
        getPrefs().flush();
    }

    public boolean isSfxOn() {
        return getPrefs().getBoolean(PREF_SFX_ON, true);
    }

    public void setSfxOn(boolean sfxOn) {
        getPrefs().putBoolean(PREF_SFX_ON, sfxOn);
        getPrefs().flush();
    }

    public boolean isMusicOn() {
        return getPrefs().getBoolean(PREF_MUSIC_ON, true);
    }

    public void setMusicOn(boolean musicOn) {
        getPrefs().putBoolean(PREF_MUSIC_ON, musicOn);
        getPrefs().flush();
    }

    public float getDialogScale() {
        return getPrefs().getFloat(PREF_DIALOG_SCALE, 1.0f);
    }

    public void setDialogScale(float dialogScale) {
        dialogScale = Math.max(dialogScale, 0.8f);
        getPrefs().putFloat(PREF_DIALOG_SCALE, dialogScale);
        getPrefs().flush();
    }

    public float getMenuScale() {
        return getPrefs().getFloat(PREF_MENU_SCALE, 1.0f);
    }

    public void setMenuScale(float menuScale) {
        getPrefs().putFloat(PREF_MENU_SCALE, menuScale);
        getPrefs().flush();
    }

    public float getHudScale() {
        return getPrefs().getFloat(PREF_HUD_SCALE, 1.0f);
    }

    public void setHudScale(float hudScale) {
        getPrefs().putFloat(PREF_HUD_SCALE, hudScale);
        getPrefs().flush();
    }
}
