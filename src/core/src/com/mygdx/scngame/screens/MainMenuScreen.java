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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dongbat.jbump.World;
import com.mygdx.scngame.SCNGame;
import com.mygdx.scngame.controls.Controls;
import com.mygdx.scngame.dialog.DialogFile;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.save.SaveFile;
import com.mygdx.scngame.scene.Scene;
import com.mygdx.scngame.screens.data.ScreenData;
import com.mygdx.scngame.settings.PrefSettings;
import com.mygdx.scngame.settings.Settings;
import com.mygdx.scngame.ui.TruetypeLabel;
import com.mygdx.scngame.viewport.PixelFitScaling;

import java.time.Instant;

public class MainMenuScreen extends InputAdapter implements Screen {

    private Stage stage;

    Viewport viewport;
    OrthographicCamera camera;

    Game game;

    Music bg;

    ScreenData screenData;

    public MainMenuScreen(ScreenData screenData) {
        this.game = screenData.game();

        camera = new OrthographicCamera();
        viewport = new ScreenViewport();

        bg = screenData.assets().get("music/bgtest1.mp3", Music.class);

        this.screenData = screenData;
    }

    final Array<SaveFile> saves = new Array<SaveFile>();
    private SaveFile newSave = null;

    float scale = 1f;
    @Override
    public void show() {
        Skin skin = screenData.assets().get("skin/uiskin2.json", Skin.class);

        FreeTypeFontGenerator font = screenData.assets().get("skin/MyFont2.ttf", FreeTypeFontGenerator.class);
        TruetypeLabel label = new TruetypeLabel(font, 12);

        Container<Label> container = new Container<>(label);
        container.center();

        Table messageStack = new Table();
        messageStack.left().top().setScale(scale);
        messageStack.setFillParent(true);

        messageStack.addAction(Actions.sequence(Actions.delay(3f), Actions.fadeOut(0.6f)));

        scale = screenData.settings().getUIScale();

        container.setScale(scale);
        container.setFillParent(true);
        label.setFontScale(scale);

        stage = new Stage();
        stage.setViewport(viewport);
        stage.setDebugAll(true);

        stage.addActor(container);
        stage.addActor(messageStack);

        bg.setVolume(screenData.settings().getTrueMusicVolume());
        bg.setLooping(true);
        bg.play();

        try {
            newSave = SaveFile.loadXMLSaveFile(Gdx.files.internal("saves/0newGame.save"));
            saves.add(newSave);
        } catch (SaveFile.InvalidSaveFileException e) {
            Gdx.app.error("SAVE FILE", "Failed to new save savefile");

            TruetypeLabel msg = new TruetypeLabel(font, 12);
            msg.setText("Failed to load new save savefile (can't create new saves)");
            msg.setFontScale(scale);
            msg.setColor(Color.RED);

            messageStack.add(msg).row();
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
                msg.setText("Failed to debug save: " + save.name());
                msg.setFontScale(scale);
                msg.setColor(Color.YELLOW);

                messageStack.add(msg).row();
            }

        }

        for(FileHandle save : Gdx.files.local("save/").list()) {
            try {
                SaveFile sf = SaveFile.loadXMLSaveFile(save);
                saves.add(sf);
            } catch(SaveFile.InvalidSaveFileException e) {
                Gdx.app.error("SAVE ERROR", e.getMessage() + " " + save.name());

                TruetypeLabel msg = new TruetypeLabel(font, 12);
                msg.setText("Failed to load save: " + save.name());
                msg.setFontScale(scale);
                msg.setColor(Color.ORANGE);

                messageStack.add(msg).row();
            }
        }

        String labelText = "SELECT SAVE: \n";

        for(int i = 0; i < saves.size; i++) {
            SaveFile save = saves.get(i);

            labelText += "[" + i + "] " + save.displayName + "\t " + Instant.ofEpochSecond(save.saveDateEpoch).toString() + "\n";
        }

        label.setText(labelText);

        Controls.getInstance().addInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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

        Controls.getInstance().removeInputProcessor(this);
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode) {
            case Input.Keys.NUM_0:
            case Input.Keys.NUM_1:
            case Input.Keys.NUM_2:
            case Input.Keys.NUM_3:
            case Input.Keys.NUM_4:
            case Input.Keys.NUM_5:
            case Input.Keys.NUM_6:
            case Input.Keys.NUM_7:
            case Input.Keys.NUM_8:
            case Input.Keys.NUM_9:
                int index = keycode - Input.Keys.NUM_0;

                if(index >= saves.size) return false;

                SaveFile save = saves.get(index);

                // if new save, create a local file
                if(save == newSave) {
                    save.file = Gdx.files.local("save/" + "prealphasave-" + Instant.now().getEpochSecond() + ".save");
                }

                game.setScreen(new GameScreen(this.screenData, save));
        }

        return false;
    }
}
