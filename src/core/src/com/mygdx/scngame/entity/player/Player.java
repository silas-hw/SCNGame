package com.mygdx.scngame.entity.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.*;
import com.mygdx.scngame.entity.component.GraphicsComponent;
import com.mygdx.scngame.entity.component.InputComponent;
import com.mygdx.scngame.entity.component.PhysicsComponent;
import com.mygdx.scngame.entity.context.EntityContext;
import com.mygdx.scngame.physics.Box;

public class Player extends Entity {
    protected InputComponent<? super Player> inputComponent;
    protected GraphicsComponent<? super Player> graphicsComponent;
    protected PhysicsComponent<? super Player> physicsComponent;

    public boolean isDying = false;

    public Player() {
        position.x = 0;
        position.y = 0;
    }

    public Player(EntityContext context,
                  InputComponent<? super Player> input,
                  GraphicsComponent<? super Player> graphics,
                  PhysicsComponent<? super Player> physics) {
        this.inputComponent = input;
        this.graphicsComponent = graphics;
        this.physicsComponent = physics;

        addListener(this.inputComponent);
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
    public void update(World<Box> world, float delta) {
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
