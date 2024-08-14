package com.mygdx.scngame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
		String version = getClass().getPackage().getImplementationVersion();
		if(version == null || version.isEmpty()) version = "DEVELOPMENT";

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
