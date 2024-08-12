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
	public SpriteBatch batch;
	public ShapeRenderer shape;

	private static AssetManager assetManager;

	public static final AssetManager getAssetManager() {
		if(assetManager == null) {
			assetManager = new AssetManager();
		}

		return assetManager;
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		shape = new ShapeRenderer();

		ScreenData screenData = new ScreenData(
				this,
				batch,
				shape,
				PrefSettings.getInstance()
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

		if(assetManager != null) {
			assetManager.clear();
			assetManager.dispose();
		}
	}
}
