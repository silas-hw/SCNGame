package com.mygdx.scngame.hud;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.scngame.entity.component.HealthComponent;
import com.mygdx.scngame.screens.data.ScreenData;
import com.mygdx.scngame.settings.Settings;
import com.mygdx.scngame.ui.TruetypeLabel;

public class HUD implements HealthComponent.HealthDamageListener {

    SpriteBatch batch;
    Settings settings;

    Stage stage;

    ScreenViewport viewport;

    Label healthLabel;

    public HUD(ScreenData screenData, float initialMaxHealth) {
        this.batch = screenData.batch();

        this.settings = screenData.settings();

        viewport = new ScreenViewport();

        stage = new Stage(viewport, screenData.batch());

        FreeTypeFontGenerator font = screenData.assets().get("skin/MyFont2.ttf", FreeTypeFontGenerator.class);

        healthLabel = new TruetypeLabel(font, 14);
        healthLabel.setText(initialMaxHealth + " / " + initialMaxHealth);

        healthLabel.setScale(settings.getHudScale());

        Table root = new Table();
        root.setFillParent(true);
        root.pad(10f);
        root.add(healthLabel).bottom().left();


        stage.addActor(root);
    }

    public void draw() {
        stage.act();
        stage.draw();
    }

    @Override
    public void onDamage(float damage, float currentHealth, float maxHealth) {
        healthLabel.setText(currentHealth + " / " + maxHealth);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
