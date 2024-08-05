package com.mygdx.scngame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.scngame.screens.MainMenuScreen;
import com.mygdx.scngame.settings.Controls;

public class SCNGame extends Game {
	public SpriteBatch batch;
	public ShapeRenderer shape;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		shape = new ShapeRenderer();

		this.setScreen(new MainMenuScreen(this, batch, shape));
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
	}
}
