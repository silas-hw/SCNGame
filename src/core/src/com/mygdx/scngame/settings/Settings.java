package com.mygdx.scngame.settings;

/**
 * Defines an interface for retrieving general configuration settings. Most of the time,
 * {@link PrefSettings} will be used to retrieve and store settings in a persistent file.
 *
 * Using an interface with dependency injection into sub-systems that require access to settings, however.
 * allows those sub-systems to be tested with mock data.
 */
public interface Settings {
    float getSfxVol();
    void setSfxVol(float sfxVol);

    float getMusicVolume();
    void setMusicVolume(float musicVolume);

    float getTrueMusicVolume();

    boolean isSfxOn();
    void setSfxOn(boolean sfxOn);

    boolean isMusicOn();
    void setMusicOn(boolean musicOn);

    float getUIScale();
    void setUIScale(float uiScale);

    float getHudScale();
    void setHudScale(float hudScale);
}
