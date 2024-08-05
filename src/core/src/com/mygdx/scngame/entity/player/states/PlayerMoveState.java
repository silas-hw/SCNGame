package com.mygdx.scngame.entity.player.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dongbat.jbump.*;
import com.mygdx.scngame.dialog.DialogStart;
import com.mygdx.scngame.entity.EntityState;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.event.GameEvent;
import com.mygdx.scngame.event.Global;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.physics.DamageBox;
import com.mygdx.scngame.settings.Controls;

public class PlayerMoveState implements EntityState<Player> {

    private float invisTimer = 0f;
    private final float invisTime = 0.3f;
    private boolean invis = false;
    
    protected Player container;
    protected World<Box> world;

    private Texture texture;

    @Override
    public EntityState<? super Player> update(float delta) {
        int dx = 0;
        int dy = 0;

        if(container.context.isKeyPressed(Controls.getUpKey())) {
            dy++;
        }

        if(container.context.isKeyPressed(Controls.getDownKey())) {
            dy--;
        }

        if(container.context.isKeyPressed(Controls.getLeftKey())) {
            dx--;
        }

        if(container.context.isKeyPressed(Controls.getRightKey())) {
            dx++;
        }

        container.direction.set(dx, dy);
        container.direction.nor();

        if(container.context.isKeyJustPressed(Controls.getDashKey())) {
            return new PlayerDashState();
        }

        if(container.context.isKeyJustPressed(Input.Keys.E)) {
            Global.bus.fire(new GameEvent(container, new DialogStart("test_dialog_1")));
        }

        container.position.mulAdd(container.direction, 500f*delta);

        world.move(container.collisionItem, container.position.x, container.position.y, Box.GLOBAL_FILTER);
        Rect rect = world.getRect(container.collisionItem);
        container.position.x = rect.x;
        container.position.y = rect.y;

        Response.Result res = world.move(container.hitbox, container.position.x, container.position.y, Box.GLOBAL_FILTER);

        if(invis && invisTimer < invisTime) {
            invisTimer += delta;
        } else if(invis) {
            invis = false;
            invisTimer = 0f;
        }

        if(!invis) {
            for(int i = 0; i<res.projectedCollisions.size(); i++) {
                Collision col = res.projectedCollisions.get(i);

                if(col.other.userData instanceof DamageBox) {
                    DamageBox dBox = (DamageBox) col.other.userData;

                    container.health -= dBox.damage;
                    invis = true;
                }

            }
        }

        return null;
    }

    @Override
    public EntityState<? super Player> draw(SpriteBatch batch, ShapeRenderer shape, float alpha) {
        batch.draw(texture, container.position.x, container.position.y);
        return null;
    }

    @Override
    public void setContainer(Player container) {
        this.container = container;
    }

    @Override
    public void setWorld(World<Box> world) {
        this.world = world;
    }

    @Override
    public void enter() {
        texture = new Texture(Gdx.files.internal("sprites/test.png"));
    }

    @Override
    public void exit() {
        texture.dispose();
    }
}
