package com.mygdx.scngame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dongbat.jbump.World;
import com.mygdx.scngame.SCNGame;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.scene.Scene;
import com.mygdx.scngame.screens.data.ScreenData;
import com.mygdx.scngame.settings.PrefSettings;
import com.mygdx.scngame.settings.Settings;
import com.mygdx.scngame.viewport.PixelFitScaling;

public class MainMenuScreen implements Screen {

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

    @Override
    public void show() {
        Skin skin = screenData.assets().get("skin/uiskin2.json", Skin.class);

        Label label = new Label(" ", skin);
        label.setText("Press E to Start");
        label.setFontScale(1/30f);

        Container<Label> container = new Container<>(label);
        container.center();

        stage = new Stage();
        stage.setViewport(viewport);

        stage.addActor(container);

        Gdx.app.log("MainMenu", "Music volume currently set to: " + screenData.settings().getMusicVolume());

        bg.setVolume(screenData.settings().getTrueMusicVolume());
        bg.setLooping(true);
        bg.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        stage.draw();

        if(Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            game.setScreen(new GameScreen(screenData));
        }
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
    }

    @Override
    public void dispose() {

    }
}
