package com.mygdx.scngame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dongbat.jbump.*;
import com.mygdx.scngame.dialog.Dialog;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.event.GameEvent;
import com.mygdx.scngame.event.GameEventListener;
import com.mygdx.scngame.event.Global;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.scene.Scene;
import com.mygdx.scngame.screens.GameScreen;
import com.mygdx.scngame.screens.MainMenuScreen;

public class SCNGame extends Game implements GameEventListener {
	public SpriteBatch batch;
	public ShapeRenderer shape;

	public Scene scene;
	public OrthographicCamera cam;
	public Viewport viewport;

	public World<Box> world;

	public Dialog dialog;

	public Player player;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		shape = new ShapeRenderer();

		world = new World<>();

		cam = new OrthographicCamera();

		viewport = new ExtendViewport(400, 400, cam);

		scene = new Scene(viewport, batch, shape, world);

		player = new Player(scene.getWorld(), scene);

		dialog = new Dialog();

		Global.bus.addEventListener(this);
		Global.bus.addEventListener(dialog);

		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		dialog.resize(width, height);
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

	@Override
	public void notify(GameEvent event) {
	}
}
