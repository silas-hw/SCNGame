package com.mygdx.scngame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Rect;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.Entity;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.physics.DamageBox;
import com.mygdx.scngame.physics.HitBox;
import com.mygdx.scngame.scene.Scene;

public class SCNGame extends ApplicationAdapter {
	SpriteBatch batch;
	ShapeRenderer shape;
	Texture img;
	Scene scene;
	OrthographicCamera cam;
	Viewport viewport;

	Texture texture;
	World<Object> world;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		shape = new ShapeRenderer();

		world = new World<>();


		cam = new OrthographicCamera();

		viewport = new ScreenViewport(cam);

		scene = new Scene(viewport, batch, shape);
		scene.addEntity(new Player());

		Player player2 = new Player();
		player2.position.set(500, 500);

		scene.addEntity(player2);

		Gdx.input.setInputProcessor(scene);
	}

	@Override
	public void render () {
		Gdx.graphics.setTitle("" + Gdx.graphics.getFramesPerSecond());

		ScreenUtils.clear(Color.WHITE);
		scene.draw();
		scene.update(world, Gdx.graphics.getDeltaTime());

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

		if(Gdx.input.isKeyPressed(Input.Keys.E)) {
			world = new World<>();
		}
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
