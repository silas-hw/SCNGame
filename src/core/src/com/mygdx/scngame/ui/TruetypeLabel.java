package com.mygdx.scngame.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

// TODO: add caching and shared resources to improve performance (IMPORTANT)

/**
 * A hackjob solution to true type font labels. Really horrible. Like. Really really horrible.
 */
public class TruetypeLabel extends Label {

    private final FreeTypeFontGenerator fontGen;
    private int baseFontSize = 16;

    private static final LabelStyle labelStyle = new LabelStyle();

    static {
        labelStyle.font = new BitmapFont();
    }

    public TruetypeLabel(FreeTypeFontGenerator fontGenerator, int baseFontSize) {
        super("", labelStyle);

        this.fontGen = fontGenerator;
        this.baseFontSize = baseFontSize;
        this.setFontSize(baseFontSize);
    }

    private final int superSampleRate = 8;

    private int currentSize = 0;

    public void setFontSize(int fontSize) {
        if(fontSize == currentSize) return;
        currentSize = fontSize;

        int superSampleSize = fontSize * superSampleRate;

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        // super sample lol
        parameter.size = superSampleSize;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.hinting = FreeTypeFontGenerator.Hinting.Full;
        parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetX = 3 * fontSize / 16;
        parameter.shadowOffsetY = 3 * fontSize / 16;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2 * fontSize / 16;
        parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789↧↦↤↥↞↠↡↟↱↰⇷⇸⇋⇌()!@#$%^&*_+-=><,./?'\":;[]{}\\|~";

        BitmapFont font = fontGen.generateFont(parameter);

        this.getStyle().font.dispose();
        LabelStyle newStyle = new Label.LabelStyle();
        newStyle.font = font;

        super.setStyle(newStyle);
        super.setFontScale(1f/superSampleRate);
    }

    @Override
    public void setFontScale(float fontScale) {
        this.setFontSize((int) (fontScale * this.baseFontSize));
    }
}
