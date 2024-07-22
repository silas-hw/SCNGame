package com.mygdx.scngame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dongbat.jbump.*;
import com.mygdx.scngame.dialog.Dialog;
import com.mygdx.scngame.dialog.DialogEnd;
import com.mygdx.scngame.dialog.DialogStart;
import com.mygdx.scngame.entity.Entity;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.event.GameEvent;
import com.mygdx.scngame.event.GameEventListener;
import com.mygdx.scngame.event.Global;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.physics.DamageBox;
import com.mygdx.scngame.physics.HitBox;
import com.mygdx.scngame.scene.Scene;

public class SCNGame extends ApplicationAdapter implements GameEventListener {
	SpriteBatch batch;
	ShapeRenderer shape;
	Texture img;
	Scene scene;
	OrthographicCamera cam;
	Viewport viewport;

	Texture texture;
	World<Box> world;

	Dialog dialog;

	private boolean pause = false;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		shape = new ShapeRenderer();

		world = new World<>();

		Box wall = new Box();
		wall.solid = true;
		wall.layer = (byte) 0b10000000;

		world.add(new Item<>(wall), 150, 150, 150, 150);

		DamageBox damage = new DamageBox(5f, DamageBox.DamageType.DEFAULT);
		damage.layer = (byte) 0b10000000;

		world.add(new Item<>(damage), -150, -150, 150, 150);

		cam = new OrthographicCamera();

		viewport = new ScreenViewport(cam);

		scene = new Scene(viewport, batch, shape);

		Player player = new Player(world);
		scene.addEntity(player);

		dialog = new Dialog();

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(dialog);
		multiplexer.addProcessor(scene);

		Gdx.input.setInputProcessor(multiplexer);

		Global.bus.addEventListener(this);
		Global.bus.addEventListener(dialog);
	}

	@Override
	public void render () {
		Gdx.graphics.setTitle("" + Gdx.graphics.getFramesPerSecond());

		ScreenUtils.clear(Color.BLACK);

		scene.update(Gdx.graphics.getDeltaTime());

		scene.draw();
		dialog.draw();

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_BLEND);

		shape.setProjectionMatrix(cam.combined);
		shape.begin(ShapeRenderer.ShapeType.Filled);

		for(Item item : world.getItems()) {
			Rect rec = world.getRect(item);

			if(item.userData instanceof Entity) {
				shape.setColor(255/256f, 140/256f, 0, 0.4f);
			} else if(item.userData instanceof HitBox) {
				shape.setColor(36/256f, 122/256f, 227/256f, 0.4f);
			} else if(item.userData instanceof DamageBox) {
				shape.setColor(227/256f, 36/256f, 36/256f, 0.4f);
			} else {
				shape.setColor(1, 1, 1, 0.4f);
			}

			shape.rect(rec.x, rec.y, rec.w, rec.h);
		}

		shape.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		dialog.getViewport().update(width, height);
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	@Override
	public void notify(GameEvent event) {
	}
}
