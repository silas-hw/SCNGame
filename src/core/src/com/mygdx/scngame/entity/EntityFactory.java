package com.mygdx.scngame.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.scngame.entity.player.Player;
import com.mygdx.scngame.entity.player.PlayerGraphicsComponent;
import com.mygdx.scngame.entity.player.PlayerInputComponent;
import com.mygdx.scngame.entity.player.PlayerPhysicsComponent;

public class EntityFactory {
    private static final Texture player1 = new Texture(Gdx.files.internal("sprites/test.png"));
    private static final Texture player2 = new Texture(Gdx.files.internal("sprites/badlogic.jpg"));

    public static Player createPlayer() {
        Player player =  new Player();

        player.setPhysicsComponent(new PlayerPhysicsComponent(player));
        player.setInputComponent(new PlayerInputComponent());
        player.setGraphicsComponent(new PlayerGraphicsComponent(player1));

        return player;
    }
}
