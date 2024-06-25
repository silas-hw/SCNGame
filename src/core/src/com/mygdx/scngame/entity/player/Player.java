package com.mygdx.scngame.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
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
        position.x = 0;
        position.y = 0;
    }

    public Player(InputComponent<? super Player> input,
                  GraphicsComponent<? super Player> graphics,
                  PhysicsComponent<? super Player> physics) {
        this.inputComponent = input;
        this.graphicsComponent = graphics;
        this.physicsComponent = physics;

        addListener(this.inputComponent);
        position.x = 0;
        position.y = 0;
    }

    public void setInputComponent(InputComponent<? super Player> input) {
        if(this.inputComponent != null) {
            removeListener(this.inputComponent);
            this.inputComponent.dispose();
        }

        this.inputComponent = input;
        addListener(this.inputComponent);
    }

    public void setPhysicsComponent(PhysicsComponent<? super Player> physics) {
        if(this.physicsComponent != null) {
            this.physicsComponent.dispose();
        }

        this.physicsComponent = physics;
    }

    public void setGraphicsComponent(GraphicsComponent<? super Player> graphics) {
        if(this.graphicsComponent != null) {
            this.graphicsComponent.dispose();
        }

        this.graphicsComponent = graphics;
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
