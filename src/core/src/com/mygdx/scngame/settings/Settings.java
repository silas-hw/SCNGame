package com.mygdx.scngame.settings;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

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

    /** sets a label have its font regenerated upon the UI scale being set, alongside a base font size for the
     * new font size to be calculated from.
     * @param label
     */
    void addLabelScaleListener(Label label);
    void removeLabelScaleListener(Label label);

    float getHudScale();
    void setHudScale(float hudScale);
}
