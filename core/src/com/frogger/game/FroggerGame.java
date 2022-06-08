package com.frogger.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.frogger.game.screens.MainMenuScreen;
import com.frogger.game.objects.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

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

		// create tiles (default)
		// TODO: create levels and simplify creation of tiles in separate Java class

		setScreen(new MainMenuScreen(this));
	}


	/**
	 * Method to dispose assets after closing game.
	 */
	@Override
	public void dispose () {
		gameBatch.dispose();
		attributesBatch.dispose();

		Frog.dispose();
		MovingObject.dispose();
		Log.dispose();
		Car.dispose();
		Train.dispose();

		Tile.dispose();
	}

	public void loadLevelsToFile(String src, Level[] levels) {
		try(ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(Paths.get(src)))) {
			out.writeObject(levels);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Level[] loadLevelsFromFile(String src) {
		Level[] levels = null;
		try(ObjectInputStream out = new ObjectInputStream(Files.newInputStream(Paths.get(src)))) {
			levels = (Level[]) out.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		return levels;
	}
}
