package com.mygdx.scngame.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.HashMap;

/**
 * A hackjob solution to true type font labels. Generates a new bitmap font based on a given FreeTypeFontGenerator and
 * a given base font size every time the label is rescaled.
 * <p>
 * Fonts generated using the same font generator, even across different labels, are cached, meaning 12 labels with the
 * same font generator at the same size (base size * font scale) will all share the same bitmap font.
 * <p>
 * This will likely be a memory bottleneck, so should be on the list of places worth looking when optimising.
 *
 * @author Silas Hayes-Williams
 */
public class TruetypeLabel extends Label {

    private final FreeTypeFontGenerator fontGen;
    private int baseFontSize = 16;

    // this is only here so scene2d.ui doesn't whine. We end up changing the style to the new font each time, this
    // is just to give it something at construction time before the new font has been generated
    private static final LabelStyle labelStyle = new LabelStyle();
    static {
        labelStyle.font = new BitmapFont();
    }

    public TruetypeLabel(FreeTypeFontGenerator fontGenerator, int baseFontSize) {
        super("", labelStyle);

        if(!fontCache.containsKey(fontGenerator)) {
            fontCache.put(fontGenerator, new HashMap<>());
        }

        this.fontGen = fontGenerator;
        this.baseFontSize = baseFontSize;
        this.setFontSize(baseFontSize);
    }

    // caches bitmap fonts already generated at a particular size for later use. Prevents un-needed regeneration,
    // and is shared statically such that all TrueType Labels with the same font generator can share resources.
    // Reduces memory footprint by up to 75% in worst cases
    private static final HashMap<FreeTypeFontGenerator, HashMap<Integer, BitmapFont>> fontCache = new HashMap<FreeTypeFontGenerator, HashMap<Integer, BitmapFont>>();

    // how many times larger to generate the font. We then set the font scale to a factor below this.
    // reduces jaggies and makes for smoother text, but increases memory usage
    // could make configurable, but right now having a constant is fine
    private final int superSampleRate = 4;

    private int currentSize = 0;

    public void setFontSize(int fontSize) {
        if(fontSize == currentSize) return;
        currentSize = fontSize;

        BitmapFont newFont;
        HashMap<Integer, BitmapFont> map = fontCache.get(fontGen);

        if(map.containsKey(fontSize)) {
            newFont = map.get(fontSize);
        } else {
            int superSampleSize = fontSize * superSampleRate;

            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

            parameter.size = superSampleSize;
            parameter.magFilter = Texture.TextureFilter.MipMapLinearLinear;
            parameter.minFilter = Texture.TextureFilter.MipMapLinearLinear;
            parameter.hinting = FreeTypeFontGenerator.Hinting.AutoFull;
            parameter.kerning = false;
            parameter.genMipMaps = true;
            parameter.shadowColor = Color.BLACK;
            parameter.shadowOffsetX = superSampleSize / 16;
            parameter.shadowOffsetY = superSampleSize / 16;
            parameter.borderColor = Color.BLACK;
            parameter.borderWidth = 3;
            parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789↧↦↤↥↞↠↡↟↱↰⇷⇸⇋⇌()!@#$%^&*_+-=><,./?'\":;[]{}\\|~";

            newFont = fontGen.generateFont(parameter);

            // currently un-used fonts still stay in the cache.
            // we could add some reference counting so as soon as a cached bitmap font isn't being used
            // we can dispose of it. Currently, not of significant importance.
            fontCache.get(fontGen).put(fontSize, newFont);
        }

        LabelStyle newStyle = new Label.LabelStyle();
        newStyle.font = newFont;

        super.setStyle(newStyle);
        super.setFontScale(1f/superSampleRate);
    }

    @Override
    public void setFontScale(float fontScale) {
        this.setFontSize((int) (fontScale * this.baseFontSize));
    }
}
