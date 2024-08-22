package com.mygdx.scngame.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.scngame.controls.ActionListener;
import com.mygdx.scngame.controls.Controls;
import com.mygdx.scngame.screens.data.ScreenData;

/**
 * The UI menu to allow the user to manipulate a given Settings instance.
 */
public class SettingsMenu implements ActionListener {

    private Stage stage;
    private Table root;

    private int focusIndex = 0;

    private boolean inFocus = false;

    private final Array<Actor> focusableArray = new Array<Actor>();

    private SpriteBatch batch;

    private Window window;
    private Slider uiScaleSlider;
    private Slider musicSlider;
    private Slider sfxSlider;
    private CheckBox musicOn;
    private CheckBox sfxOn;
    private TextButton okButton;

    private Skin skin;


    private final Settings settings;
    public SettingsMenu(ScreenData screenData) {
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

        skin = screenData.assets().get("skin/uiskin2.json", Skin.class);

        stage = new Stage(new ScreenViewport(), batch);

        root = new Table();
        root.setFillParent(true);

        stage.addActor(root);

        window = new Window("Settings", skin);
        window.setResizable(false);
        window.setMovable(true);
        window.keepWithinStage();

        window.setScale(settings.getUIScale());

        root.add(window);

        musicOn = new CheckBox("music enabled", skin);
        musicOn.setChecked(settings.isMusicOn());

        musicOn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setMusicOn(musicOn.isChecked());
            }
        });

        musicOn.addListener(setFocusListener);
        musicOn.getStyle().focusedFontColor = Color.CYAN;

        focusableArray.add(musicOn);
        window.add(musicOn).expandX().fillX().left().row();

        musicSlider = new Slider(0f, 1f, 0.1f, false, skin);
        musicSlider.setValue(settings.getMusicVolume());

        musicSlider.addCaptureListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setMusicVolume(musicSlider.getValue());
            }
        });

        musicSlider.addListener(setFocusListener);
        musicSlider.addListener(focusListener);

        focusableArray.add(musicSlider);
        window.add(musicSlider).row();

        sfxOn = new CheckBox("sfx enabled", skin);
        sfxOn.setChecked(settings.isSfxOn());

        sfxOn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setSfxOn(sfxOn.isChecked());
            }
        });

        sfxOn.addListener(setFocusListener);
        sfxOn.getStyle().focusedFontColor = Color.CYAN;

        focusableArray.add(sfxOn);
        window.add(sfxOn).left().row();

        sfxSlider = new Slider(0f, 1f, 0.1f, false, skin);
        sfxSlider.setValue(settings.getSfxVol());

        sfxSlider.addCaptureListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setSfxVol(sfxSlider.getValue());
            }
        });

        sfxSlider.addListener(setFocusListener);
        sfxSlider.addCaptureListener(focusListener);

        focusableArray.add(sfxSlider);
        window.add(sfxSlider).row();

        uiScaleSlider = new Slider(0.3f, 3f, 0.1f, false, skin);
        uiScaleSlider.setValue(settings.getUIScale());

        uiScaleSlider.addCaptureListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setUIScale(uiScaleSlider.getValue());

                window.setScale(uiScaleSlider.getValue());
            }
        });

        uiScaleSlider.addListener(setFocusListener);
        uiScaleSlider.addListener(focusListener);

        focusableArray.add(uiScaleSlider);
        window.add(uiScaleSlider).row();

        HorizontalGroup buttons = new HorizontalGroup();

        okButton = new TextButton("OK", skin);
        okButton.addCaptureListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                inFocus = false;
                Controls.getInstance().removeInputProcessor(stage);
            }
        });

        okButton.addListener(focusListener);
        okButton.setFillParent(true);

        focusableArray.add(okButton);

        buttons.addActor(okButton);

        buttons.rowCenter();

        window.add(buttons).growX().row();

        stage.setKeyboardFocus(focusableArray.get(focusIndex));
    }

    public void draw() {
        if(!inFocus) return;

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
