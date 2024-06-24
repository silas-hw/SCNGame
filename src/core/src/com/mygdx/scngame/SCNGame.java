package com.mygdx.scngame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.scene.Scene;

public class SCNGame extends ApplicationAdapter {
	SpriteBatch batch;
	ShapeRenderer shape;
	Texture img;
	Scene scene;
	OrthographicCamera cam;
	Viewport viewport;

	Texture texture;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		shape = new ShapeRenderer();

		cam = new OrthographicCamera();

		viewport = new ScreenViewport(cam);

		scene = new Scene(viewport, batch, shape);
		scene.addEntity(new Player());

		Gdx.input.setInputProcessor(scene);
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 0);
		scene.draw();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
