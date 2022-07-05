package com.frogger.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.frogger.game.gameObjects.*;
import com.frogger.game.mapObjects.Tile;
import com.frogger.game.screens.SplashScreen;


public class FroggerGame extends Game {

	/** initialize static fields */
	public static SpriteBatch gameBatch;
	public static OrthographicCamera gameCamera;
	public static SpriteBatch attributesBatch;

	/**
	 * Method create() runs on the start of the program.
	 * Used to initialize all static objects
	 */
	@Override
	public void create () {
		// get screen size
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();

		// initialize batch for attributes
		attributesBatch = new SpriteBatch();

		// initialize batch and camera for game
		gameCamera = new OrthographicCamera();
		gameCamera.setToOrtho(false, screenWidth, screenHeight);
		gameBatch = new SpriteBatch();

		setScreen(new SplashScreen(this));
	}

	/**
	 * Method to dispose assets after closing game.
	 */
	@Override
	public void dispose () {
		gameBatch.dispose();
		attributesBatch.dispose();

		Frog.dispose();
		Log.dispose();
		Car.dispose();
		Train.dispose();

		Tile.dispose();
	}
}
