package com.mygdx.scngame.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dongbat.jbump.World;
import com.mygdx.scngame.SCNGame;
import com.mygdx.scngame.controls.ActionListener;
import com.mygdx.scngame.controls.Controls;
import com.mygdx.scngame.dialog.DialogFile;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.save.SaveFile;
import com.mygdx.scngame.scene.Scene;
import com.mygdx.scngame.screens.data.ScreenData;
import com.mygdx.scngame.settings.PrefSettings;
import com.mygdx.scngame.settings.Settings;
import com.mygdx.scngame.settings.SettingsMenu;
import com.mygdx.scngame.ui.TruetypeLabel;
import com.mygdx.scngame.viewport.PixelFitScaling;

import java.net.http.WebSocket;
import java.time.Instant;

public class MainMenuScreen implements Screen, ActionListener {

    private Stage stage;

    Viewport viewport;
    OrthographicCamera camera;

    Game game;

    Music bg;

    ScreenData screenData;

    SettingsMenu settingsMenu;

    public MainMenuScreen(ScreenData screenData) {
        this.game = screenData.game();

        camera = new OrthographicCamera();
        viewport = new ScreenViewport();

        bg = screenData.assets().get("music/bgtest1.mp3", Music.class);

        this.screenData = screenData;

        this.settingsMenu = new SettingsMenu(screenData);
    }

    final Array<SaveFile> saves = new Array<>();
    private SaveFile newSave = null;

    private Array<Actor> focusableArray = new Array<>();
    private int focusIndex = 0;

    float scale = 1f;
    @Override
    public void show() {
        screenData.controls().addActionListener(this.settingsMenu);
        screenData.controls().addInputProcessor(this.settingsMenu);

        scale = screenData.settings().getUIScale();

        Skin skin = screenData.assets().get("skin/uiskin2.json", Skin.class);

        FreeTypeFontGenerator font = screenData.assets().get("skin/MyFont2.ttf", FreeTypeFontGenerator.class);

        Table saveList = new Table();
        saveList.center().top();

        ScrollPane saveScroll = new ScrollPane(saveList);

        Table saveRoot = new Table();

        Label saveLabel = new TruetypeLabel(font, 16);
        saveLabel.setText("Select Save: ");
        saveLabel.setFontScale(scale);

        saveRoot.add(saveLabel).top().left().expandX().pad(5f).row();
        saveRoot.add(saveScroll).grow().center();

        saveRoot.pad(200f);

        saveRoot.setFillParent(true);
        saveRoot.center();

        Table messageStack = new Table();
        messageStack.left().top().setScale(scale);
        messageStack.setFillParent(true);

        messageStack.addAction(Actions.sequence(Actions.delay(3f), Actions.fadeOut(0.6f)));

        stage = new Stage();
        stage.setViewport(viewport);
        //stage.setDebugAll(true);

        stage.addActor(saveRoot);
        stage.addActor(messageStack);

        bg.setVolume(screenData.settings().getTrueMusicVolume());
        bg.setLooping(true);
        bg.play();

        try {
            newSave = SaveFile.loadXMLSaveFile(Gdx.files.internal("saves/0newGame.save"));
            saves.add(newSave);
        } catch (SaveFile.InvalidSaveFileException e) {
            Gdx.app.error("SAVE FILE", "Failed to new save savefile");

            TruetypeLabel msg = new TruetypeLabel(font, 14);
            msg.setText("Failed to load new save savefile (can't create new saves): " + e.getMessage());
            msg.setFontScale(scale);
            msg.setColor(Color.RED);

            msg.setAlignment(Align.left);

            messageStack.add(msg).left().expandX().row();
        }

        for(FileHandle save : Gdx.files.internal("saves/").list()) {

            try {
                Gdx.app.log("MainMenuScreen","Loading debug save (doesn't persist): " + save.file());
                SaveFile sf = SaveFile.loadXMLSaveFile(save);
                sf.persist = false;
                saves.add(sf);
            } catch (SaveFile.InvalidSaveFileException e) {
                Gdx.app.error("SAVE FILE ERROR: ", e.getMessage() + " " + save.name());

                TruetypeLabel msg = new TruetypeLabel(font, 12);
                msg.setText("Failed to debug save: " + save.name() + "   -   " + e.getMessage());
                msg.setFontScale(scale);
                msg.setColor(Color.YELLOW);

                msg.setAlignment(Align.left);

                messageStack.add(msg).left().expandX().row();
            }

        }

        for(FileHandle save : Gdx.files.local("save/").list()) {
            try {
                SaveFile sf = SaveFile.loadXMLSaveFile(save);
                saves.add(sf);
            } catch(SaveFile.InvalidSaveFileException e) {
                Gdx.app.error("SAVE ERROR", e.getMessage() + " " + save.name());

                TruetypeLabel msg = new TruetypeLabel(font, 12);
                msg.setText("Failed to load save: " + save.name() + "   -   " + e.getMessage());
                msg.setFontScale(scale);
                msg.setColor(Color.ORANGE);
                msg.setAlignment(Align.left);

                messageStack.add(msg).left().expandX().row();
            }
        }

        for(int i = 0; i < saves.size; i++) {
            SaveFile save = saves.get(i);

            Button butt = new Button(skin);

            Label displayLabel = new TruetypeLabel(font, 12);
            displayLabel.setText("[" + i + "] " + save.displayName + "\t " + Instant.ofEpochSecond(save.saveDateEpoch).toString());
            displayLabel.setFontScale(scale);

            displayLabel.setAlignment(Align.left);

            butt.add(displayLabel).left().expandX().padTop(50f).padBottom(50f);

            butt.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // if new save, create a local file
                    if(save == newSave) {
                        save.file = Gdx.files.local("save/" + "prealphasave-" + Instant.now().getEpochSecond() + ".save");
                    }

                    game.setScreen(new GameScreen(screenData, save));
                }
            });

            butt.addListener(new FocusListener() {
                @Override
                public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                    if(focused) {
                        float x = actor.getX();
                        float y = actor.getY();
                        float width = actor.getWidth();
                        float height = actor.getHeight();

                        y += height;

                        saveScroll.scrollTo(x, y, width, height, true, false);
                    }
                }
            });

            focusableArray.add(butt);

            saveList.add(butt).left().growX().space(5f).row();
        }

        for(Actor actor : focusableArray) {

            actor.addListener(new FocusListener() {
                @Override
                public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                    if(focused) {
                        actor.setColor(Color.CYAN);
                    } else {
                        actor.setColor(Color.WHITE);
                    }
                }
            });

            actor.addListener(new ClickListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    focusIndex = focusableArray.indexOf(actor, true);
                    stage.setKeyboardFocus(focusableArray.get(focusIndex));
                }
            });
        }

        stage.setScrollFocus(saveScroll);
        stage.setKeyboardFocus(saveRoot);

        screenData.controls().addInputProcessor(stage);
        screenData.controls().addActionListener(this);
    }

    @Override
    public void render(float delta) {
        bg.setVolume(screenData.settings().getTrueMusicVolume());
        ScreenUtils.clear(Color.BLACK);
        stage.act();
        stage.draw();

        settingsMenu.draw();
    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height, true);
        settingsMenu.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        stage.dispose();
        bg.stop();

        screenData.controls().removeInputProcessor(stage);

        screenData.controls().removeInputProcessor(this.settingsMenu);
        screenData.controls().removeActionListener(this.settingsMenu);
        screenData.controls().removeActionListener(this);

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean actionDown(Controls.Actions action) {
        switch (action) {
            case DOWN:
                focusIndex++;
                focusIndex = focusIndex%focusableArray.size;
                break;

            case UP:
                focusIndex--;
                if(focusIndex < 0) focusIndex = focusableArray.size-1;
                break;

            case ATTACK:
                Actor actor = focusableArray.get(focusIndex);

                if(actor instanceof Button button) {
                    for(EventListener listener : button.getListeners()) {
                        if(listener instanceof ClickListener click) {
                            click.clicked(null, 0, 0);
                        }
                    }
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
