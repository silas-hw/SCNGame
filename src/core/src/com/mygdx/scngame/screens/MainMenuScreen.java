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
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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

    @Override
    public void show() {
        Skin skin = screenData.assets().get("skin/uiskin2.json", Skin.class);

        TruetypeLabel label = new TruetypeLabel(screenData.assets().get("skin/MyFont2.ttf", FreeTypeFontGenerator.class), 16);

        Container<Label> container = new Container<>(label);
        container.center();

        stage = new Stage();
        stage.setViewport(viewport);

        stage.addActor(container);

        bg.setVolume(screenData.settings().getTrueMusicVolume());
        bg.setLooping(true);
        bg.play();

        newSave = SaveFile.loadXMLSaveFile(Gdx.files.internal("saves/0newGame.save"));
        saves.add(newSave);

        for(FileHandle save : Gdx.files.internal("saves/").list()) {
            Gdx.app.log("MainMenuScreen","Loading debug save (doesn't persist): " + save.file());
            SaveFile sf = SaveFile.loadXMLSaveFile(save);
            sf.persist = false;
            saves.add(sf);
        }

        for(FileHandle save : Gdx.files.local("save/").list()) {
            saves.add(SaveFile.loadXMLSaveFile(save));
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
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
