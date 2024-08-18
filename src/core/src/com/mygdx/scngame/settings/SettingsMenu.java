package com.mygdx.scngame.settings;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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


    private final Settings settings;
    public SettingsMenu(ScreenData screenData) {
        this.settings = screenData.settings();
        SpriteBatch batch = screenData.batch();

        Skin skin = screenData.assets().get("skin/uiskin.json", Skin.class);

        stage = new Stage(new ScreenViewport(), batch);

        root = new Table();
        root.setFillParent(true);

        stage.addActor(root);

        CheckBox musicOn = new CheckBox("music enabled", skin);
        musicOn.setChecked(settings.isMusicOn());

        musicOn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setMusicOn(musicOn.isChecked());
            }
        });

        musicOn.getStyle().focusedFontColor = Color.CYAN;

        root.add(musicOn).expandX().fillX().row();

        Slider musicSlider = new Slider(0f, 1f, 0.1f, false, skin);
        musicSlider.setValue(settings.getMusicVolume());

        musicSlider.addCaptureListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setMusicVolume(musicSlider.getValue());
                System.out.println(settings.getMusicVolume());
            }
        });

        root.add(musicSlider).row();

        CheckBox sfxOn = new CheckBox("sfx enabled", skin);
        sfxOn.setChecked(settings.isSfxOn());

        sfxOn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setSfxOn(sfxOn.isChecked());
            }
        });

        sfxOn.getStyle().focusedFontColor = Color.CYAN;

        root.add(sfxOn).expandX().fillX().row();

        stage.setKeyboardFocus(root.getChild(focusIndex));
    }

    public void draw() {
        if(!inFocus) return;

        stage.getViewport().apply(true);
        stage.draw();
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
            return false;
        }

        Actor currentActor = root.getChild(focusIndex);
        switch(action) {
            case ATTACK:
                if(currentActor instanceof CheckBox) {
                    ((CheckBox) currentActor).setChecked(!((CheckBox) currentActor).isChecked());
                }

                break;

            case UP:
                focusIndex = MathUtils.clamp(focusIndex-1, 0, root.getChildren().size-1);
                break;

            case DOWN:
                focusIndex = MathUtils.clamp(focusIndex+1, 0, root.getChildren().size-1);
                break;

            case LEFT:
                if(currentActor instanceof Slider) {
                    ((Slider) currentActor).setValue(Math.max(settings.getMusicVolume()-0.1f, 0f));
                }

                break;

            case RIGHT:
                if(currentActor instanceof Slider) {
                    ((Slider) currentActor).setValue(Math.min(settings.getMusicVolume()+0.1f, 1f));
                }

                break;
        }

        stage.setKeyboardFocus(root.getChild(focusIndex));
        return true;
    }

    @Override
    public boolean actionUp(Controls.Actions action) {
        return false;
    }
}
