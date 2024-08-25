package com.mygdx.scngame.settings;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.scngame.controls.ActionListener;
import com.mygdx.scngame.controls.Controls;
import com.mygdx.scngame.screens.data.ScreenData;
import com.mygdx.scngame.ui.TruetypeLabel;

/**
 * The UI menu to allow the user to manipulate a given Settings instance.
 */
public class SettingsMenu implements ActionListener {

    private final Stage stage;

    private int focusIndex = 0;

    private boolean inFocus = false;

    private final Array<Actor> focusableArray = new Array<Actor>();

    private final SpriteBatch batch;

    private final Window window;
    private final Slider uiScaleSlider;
    private final Slider musicSlider;
    private final Slider sfxSlider;
    private final CheckBox musicOn;
    private final CheckBox sfxOn;
    private final TextButton okButton;

    private final Array<Runnable> postRunners = new Array<>();


    private final Settings settings;
    public SettingsMenu(ScreenData screenData) {
        FreeTypeFontGenerator fontGenerator = screenData.assets().get("skin/MyFont2.ttf", FreeTypeFontGenerator.class);

        FocusListener focusListener = new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                if (focused) {
                    actor.setColor(Color.CYAN);
                } else {
                    actor.setColor(Color.WHITE);
                }
            }
        };

        ChangeListener setFocusListener = new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                focusIndex = focusableArray.indexOf(actor, true);
                stage.setKeyboardFocus(actor);
            }
        };

        this.settings = screenData.settings();
        this.batch = screenData.batch();
        SpriteBatch batch = screenData.batch();

        Skin skin = screenData.assets().get("skin/uiskin2.json", Skin.class);

        stage = new Stage(new ScreenViewport(), batch);

        Table root = new Table();
        root.setFillParent(true);

        stage.addActor(root);

        window = new Window("Settings", skin);
        window.setResizable(false);
        window.keepWithinStage();

        window.getTitleTable().getCell(window.getTitleLabel()).setActor(new TruetypeLabel( fontGenerator, 16));
        window.getTitleLabel().setText("Settings");

        window.setScale(settings.getUIScale());

        root.add(window);

        musicOn = new CheckBox("music enabled", skin);
        musicOn.setChecked(settings.isMusicOn());

        musicOn.addListener(setFocusListener);
        musicOn.getStyle().focusedFontColor = Color.CYAN;

        musicOn.setLabel(new TruetypeLabel(fontGenerator, 16));
        musicOn.getLabel().setText("music enabled");

        focusableArray.add(musicOn);
        window.add(musicOn).expandX().left().row();

        musicSlider = new Slider(0f, 1f, 0.1f, false, skin);
        musicSlider.setValue(settings.getMusicVolume());

        musicSlider.addListener(setFocusListener);
        musicSlider.addListener(focusListener);

        focusableArray.add(musicSlider);
        window.add(musicSlider).row();

        sfxOn = new CheckBox("sfx enabled", skin);
        sfxOn.setChecked(settings.isSfxOn());

        sfxOn.addListener(setFocusListener);
        sfxOn.getStyle().focusedFontColor = Color.CYAN;

        sfxOn.setLabel(new TruetypeLabel(fontGenerator, 16));
        sfxOn.getLabel().setText("sfx enabled");

        sfxOn.pack();

        focusableArray.add(sfxOn);
        window.add(sfxOn).left().row();

        sfxSlider = new Slider(0f, 1f, 0.1f, false, skin);
        sfxSlider.setValue(settings.getSfxVol());

        sfxSlider.addListener(setFocusListener);
        sfxSlider.addCaptureListener(focusListener);

        focusableArray.add(sfxSlider);
        window.add(sfxSlider).row();

        uiScaleSlider = new Slider(0.3f, 3f, 0.1f, false, skin);
        uiScaleSlider.setValue(settings.getUIScale());

        uiScaleSlider.addListener(setFocusListener);
        uiScaleSlider.addListener(focusListener);

        focusableArray.add(uiScaleSlider);
        window.add(uiScaleSlider).expandX().fillX().row();

        okButton = new TextButton("OK", skin);
        okButton.addCaptureListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                inFocus = false;
                Controls.getInstance().removeInputProcessor(stage);
            }
        });

        okButton.addListener(focusListener);

        okButton.setLabel(new TruetypeLabel(fontGenerator, 16));
        okButton.getLabel().setText("OK");
        okButton.getLabel().setAlignment(Align.center);

        Table buttons = new Table();
        focusableArray.add(okButton);
        buttons.add(okButton).colspan(1).expandX().fillX().pad(5f);

        TextButton applyButton = new TextButton("Apply", skin);
        applyButton.setLabel(new TruetypeLabel(fontGenerator, 16));
        applyButton.getLabel().setText("Apply");
        applyButton.getLabel().setAlignment(Align.center);

        applyButton.addListener(focusListener);

        applyButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setMusicVolume(musicSlider.getValue());
                settings.setSfxVol(sfxSlider.getValue());
                settings.setSfxOn(sfxOn.isChecked());
                settings.setMusicOn(musicOn.isChecked());
                settings.setUIScale(uiScaleSlider.getValue());

                window.setScale(settings.getUIScale());
            }
        });

        focusableArray.add(applyButton);

        buttons.add(applyButton).colspan(1).expandX().fillX().pad(5f);
        window.add(buttons).expandX().fillX().row();

        stage.setKeyboardFocus(focusableArray.get(focusIndex));

        window.padRight(10f).padLeft(10f);
        window.pack();
    }

    public void draw() {
        if(!inFocus) return;

        batch.setColor(Color.WHITE);

        stage.act();
        stage.getViewport().apply(true);
        stage.getViewport().getCamera().update();

        stage.draw();

        // reset tint
        batch.setColor(Color.WHITE);
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public boolean actionDown(Controls.Actions action) {
        if (action == Controls.Actions.MENU) {
            inFocus = !inFocus;
        }

        if(!inFocus) {
            Controls.getInstance().removeInputProcessor(stage);
            return false;
        }

        Controls.getInstance().addInputProcessor(stage);

        Actor currentActor = focusableArray.get(focusIndex);
        switch(action) {
            case ATTACK:
                if(currentActor instanceof CheckBox) {
                    ((CheckBox) currentActor).setChecked(!((CheckBox) currentActor).isChecked());
                }

                break;

            case UP:
                focusIndex = MathUtils.clamp(focusIndex-1, 0, focusableArray.size-1);
                break;

            case DOWN:
                focusIndex = MathUtils.clamp(focusIndex+1, 0, focusableArray.size-1);
                break;

            case LEFT:
                if(currentActor instanceof Slider slider) {
                    slider.setValue(Math.max(slider.getValue()-0.1f, slider.getMinValue()));
                }

                break;

            case RIGHT:
                if(currentActor instanceof Slider slider) {
                    slider.setValue(Math.min(slider.getValue()+0.1f,slider.getMaxValue()));
                }

                break;
        }

        stage.setKeyboardFocus(focusableArray.get(focusIndex));
        return true;
    }

    @Override
    public boolean actionUp(Controls.Actions action) {
        return false;
    }
}
