package com.mygdx.scngame.screens.data;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.scngame.settings.Settings;

public record ScreenData(Game game, SpriteBatch batch, ShapeRenderer shapeRenderer, Settings settings) {

}