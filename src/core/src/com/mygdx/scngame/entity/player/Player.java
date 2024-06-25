package com.mygdx.scngame.entity.player;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.Entity;
import com.mygdx.scngame.entity.GraphicsComponent;
import com.mygdx.scngame.entity.InputComponent;
import com.mygdx.scngame.entity.PhysicsComponent;
import com.sun.tools.jdeps.Graph;

public class Player extends Entity {
    protected InputComponent<? super Player> inputComponent;
    protected GraphicsComponent<? super Player> graphicsComponent;
    protected PhysicsComponent<? super Player> physicsComponent;

    public Player() {
        this.inputComponent = new PlayerInputComponent();
        this.graphicsComponent = new PlayerGraphicsComponent();
        this.physicsComponent = new PlayerPhysicsComponent(this);
        addListener(inputComponent);

        position.x = 0;
        position.y = 0;
    }

    @Override
    public void update(World<Object> world, float delta) {
        this.inputComponent.update(this);
        this.physicsComponent.update(this, world, delta);
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        graphicsComponent.draw(this, batch, shape, alpha);
    }

    @Override
    public void dispose() {

    }
}
