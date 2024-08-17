package com.mygdx.scngame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.scngame.screens.AssetLoadingScreen;
import com.mygdx.scngame.screens.MainMenuScreen;
import com.mygdx.scngame.screens.data.ScreenData;
import com.mygdx.scngame.settings.Controls;
import com.mygdx.scngame.settings.PrefSettings;

public class SCNGame extends Game {
	String logTag = "MAIN GAME CLASS";

	public SpriteBatch batch;
	public ShapeRenderer shape;

	private AssetManager assetManager;
	
	@Override
	public void create () {
		String version = Gdx.files.internal("version.info").readString();
		Gdx.app.log(logTag, "Version num: " + version);

		batch = new SpriteBatch();
		shape = new ShapeRenderer();

		assetManager = new AssetManager();

		ScreenData screenData = new ScreenData(
				this,
				batch,
				shape,
				PrefSettings.getInstance(),
				assetManager
		);

		this.setScreen(new AssetLoadingScreen(screenData));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		this.getScreen().resize(width, height);
	}

	@Override
	public void dispose () {
		batch.dispose();
		shape.dispose();

		assetManager.clear();
		assetManager.dispose();
	}
}
