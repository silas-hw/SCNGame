package com.mygdx.scngame.screens.data;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.scngame.settings.Settings;

/**
 * Holds all the data that is required between different game screens.
 *
 * This is used instead of individual arguments for a number of reasons:
 * <ol>
 *     <li>Records are immutable. Although not forced, it encourages
 *         a ScreenData instance to persist across screens instead of
 *         re-instantiating everything
 *     </li>
 *
 *     <li>
 *         Reduces the number of arguments in each screen constructor,
 *         especially since every screen will require these values
 *     </li>
 *
 *     <li>
 *         Still allows for testing screens with fake values. Using singletons
 *         or static values held in the main game class wouldn't allow for this, and could
 *         create some gnarly inter-dependencies between sub-systems.
 *     </li>
 * </ol>
 *
 * @author Silas Hayes-Williams
 */
public record ScreenData(Game game,
                         SpriteBatch batch,
                         ShapeRenderer shapeRenderer,
                         Settings settings,
                         AssetManager assets
) {}