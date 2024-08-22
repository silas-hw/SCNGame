package com.mygdx.scngame.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PrefSettings implements Settings {

    private final static String PREF_SFX_VOL = "sfxVol";
    private final static String PREF_MUSIC_VOL = "musicVol";
    private final static String PREF_SFX_ON = "sfxOn";
    private final static String PREF_MUSIC_ON = "musicOn";

    private final static String PREF_UI_SCALE = "uiScale";
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
        if(sfxVol < 0f) return;

        getPrefs().putFloat(PREF_SFX_VOL, sfxVol);
        getPrefs().flush();
    }

    public float getMusicVolume() {
        return getPrefs().getFloat(PREF_MUSIC_VOL, 1.0f);
    }

    public void setMusicVolume(float musicVolume) {
        if(musicVolume < 0f) return;

        getPrefs().putFloat(PREF_MUSIC_VOL, musicVolume);
        getPrefs().flush();
    }

    public float getTrueMusicVolume() {
        if(!isMusicOn()) return 0f;

        return getMusicVolume();
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

    public float getUIScale() {
        return getPrefs().getFloat(PREF_UI_SCALE, 1.0f);
    }

    public void setUIScale(float dialogScale) {
        dialogScale = Math.max(dialogScale, 0.3f);
        getPrefs().putFloat(PREF_UI_SCALE, dialogScale);
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
