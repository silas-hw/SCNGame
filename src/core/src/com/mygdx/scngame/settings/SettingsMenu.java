package com.mygdx.scngame.settings;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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

    private final FocusListener focusListener = new FocusListener() {
        @Override
        public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
            if(focused) {
                actor.setColor(Color.CYAN);
            } else {
                actor.setColor(Color.WHITE);
            }
            System.out.println(actor);
        }
    };

    private SpriteBatch batch;


    private final Settings settings;
    public SettingsMenu(ScreenData screenData) {
        this.settings = screenData.settings();
        this.batch = screenData.batch();
        SpriteBatch batch = screenData.batch();

        Skin skin = screenData.assets().get("skin/uiskin.json", Skin.class);

        stage = new Stage(new ScreenViewport(), batch);

        root = new Table();
        root.setFillParent(true);

        stage.addActor(root);

        Window window = new Window("Settings", skin);
        window.setResizable(false);
        window.setMovable(true);
        window.keepWithinStage();

        root.add(window);

        CheckBox musicOn = new CheckBox("music enabled", skin);
        musicOn.setChecked(settings.isMusicOn());

        musicOn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setMusicOn(musicOn.isChecked());

                focusIndex = focusableArray.indexOf(musicOn, true);
                stage.setKeyboardFocus(musicOn);
            }
        });

        musicOn.getStyle().focusedFontColor = Color.CYAN;

        focusableArray.add(musicOn);
        window.add(musicOn).expandX().fillX().row();

        Slider musicSlider = new Slider(0f, 1f, 0.1f, false, skin);
        musicSlider.setValue(settings.getMusicVolume());

        musicSlider.addCaptureListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setMusicVolume(musicSlider.getValue());

                focusIndex = focusableArray.indexOf(musicSlider, true);
                stage.setKeyboardFocus(musicSlider);
            }
        });

        musicSlider.addListener(this.focusListener);

        focusableArray.add(musicSlider);
        window.add(musicSlider).row();

        CheckBox sfxOn = new CheckBox("sfx enabled", skin);
        sfxOn.setChecked(settings.isSfxOn());

        sfxOn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setSfxOn(sfxOn.isChecked());

                focusIndex = focusableArray.indexOf(sfxOn, true);
                stage.setKeyboardFocus(sfxOn);
            }
        });

        sfxOn.getStyle().focusedFontColor = Color.CYAN;

        focusableArray.add(sfxOn);
        window.add(sfxOn).expandX().fillX().row();

        Slider sfxSlider = new Slider(0f, 1f, 0.1f, false, skin);
        sfxSlider.setValue(settings.getSfxVol());

        sfxSlider.addCaptureListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setSfxVol(sfxSlider.getValue());

                focusIndex = focusableArray.indexOf(sfxSlider, true);
                stage.setKeyboardFocus(sfxSlider);
            }
        });

        sfxSlider.addCaptureListener(this.focusListener);

        focusableArray.add(sfxSlider);
        window.add(sfxSlider).row();

        Slider menuScaleSlider = new Slider(1f, 3f, 0.1f, false, skin);
        menuScaleSlider.setValue(settings.getMenuScale());

        menuScaleSlider.addCaptureListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setMenuScale(menuScaleSlider.getValue());
                window.setScale(menuScaleSlider.getValue());
                focusIndex = focusableArray.indexOf(menuScaleSlider, true);
                stage.setKeyboardFocus(menuScaleSlider);
            }
        });

        menuScaleSlider.addListener(this.focusListener);

        focusableArray.add(menuScaleSlider);
        window.add(menuScaleSlider).row();

        Slider dialogScaleSlider = new Slider(0.5f, 4f, 0.1f, false, skin);
        dialogScaleSlider.setValue(settings.getDialogScale());

        dialogScaleSlider.addCaptureListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setDialogScale(dialogScaleSlider.getValue());
                focusIndex = focusableArray.indexOf(dialogScaleSlider, true);
                stage.setKeyboardFocus(dialogScaleSlider);
            }
        });

        dialogScaleSlider.addListener(this.focusListener);

        focusableArray.add(dialogScaleSlider);
        window.add(dialogScaleSlider).row();

        HorizontalGroup buttons = new HorizontalGroup();

        TextButton okButton = new TextButton("OK", skin);
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
