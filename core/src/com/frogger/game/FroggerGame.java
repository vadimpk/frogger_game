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

import static com.frogger.game.screens.FroggerGameScreen.level;

public class FroggerGame extends Game {

	/** initialize static fields */
	public static SpriteBatch gameBatch;
	public static OrthographicCamera gameCamera;
	public static SpriteBatch attributesBatch;

	public static Level[] levels;

	/**
	 * Method create() runs on the start of the program.
	 * Used to initialize all static objects
	 */
	@Override
	public void create () {
		//TODO: design levels and delete createLevelsTEMP method
		createLevelsTEMP();
		levels = loadLevelsFromFile("levels.txt");
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
		Level[] levels;
		try(ObjectInputStream out = new ObjectInputStream(Files.newInputStream(Paths.get(src)))) {
			levels = (Level[]) out.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		return levels;
	}

	private void createLevelsTEMP() {
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();
		int nColumns = 15;
		int nRows = 30;

		Row[] rows = new Row[nRows];
		Tile[][] tiles = new Tile[nRows][nColumns];

		for (int row = 0; row < nRows; row++) {
			for (int column = 0; column < nColumns; column++) {
				tiles[row][column] = new Tile(nColumns, screenWidth, screenHeight, row, column);
			}
		}

		for (int i = 0; i < rows.length; i++) {
			rows[i] = new Row(tiles[i], Util.TypeOfRow.STATIC, new MovingObject[]{});
		}

		// create moving rows
		rows[6] = new Row(tiles[6], Util.TypeOfRow.CAR, new Car[]{
				new Car(tiles[0][0].getSize(), tiles[6][0].getX(), tiles[6][0].getY(), 15f, 3,Util.Direction.LEFT),
				new Car(tiles[0][0].getSize(), tiles[6][6].getX(), tiles[6][0].getY(), 15f, 2, Util.Direction.LEFT),
				new Car(tiles[0][0].getSize(), tiles[6][12].getX(), tiles[6][0].getY(), 15f, 3, Util.Direction.LEFT),
		});

		rows[3] = new Row(tiles[3], Util.TypeOfRow.LOG, new Log[]{
				new Log(tiles[0][0].getSize(), tiles[3][0].getX(), tiles[3][0].getY(), 20f, 3, Util.Direction.RIGHT),
				new Log(tiles[0][0].getSize(), tiles[3][5].getX(), tiles[3][0].getY(), 20f, 3, Util.Direction.RIGHT, true, 150000000),
				new Log(tiles[0][0].getSize(), tiles[3][10].getX(), tiles[3][0].getY(), 20f, 3, Util.Direction.RIGHT, true, 150000000),
		});
		rows[4] = new Row(tiles[4], Util.TypeOfRow.LOG, new Log[]{
				new Log(tiles[0][0].getSize(), tiles[4][0].getX(), tiles[4][0].getY(), 15f, 3, Util.Direction.LEFT),
				new Log(tiles[0][0].getSize(), tiles[3][5].getX(), tiles[4][0].getY(), 15f, 3, Util.Direction.LEFT),
				new Log(tiles[0][0].getSize(), tiles[3][10].getX(), tiles[4][0].getY(), 15f, 3, Util.Direction.LEFT),
		});

		rows[2] = new Row(tiles[2], Util.TypeOfRow.LOG, new Log[]{
				new Log(tiles[0][0].getSize(), tiles[2][0].getX(), tiles[2][0].getY(), 30f, 3, Util.Direction.LEFT),
				new Log(tiles[0][0].getSize(), tiles[3][5].getX(), tiles[2][0].getY(), 30f, 3, Util.Direction.LEFT),
				new Log(tiles[0][0].getSize(), tiles[3][10].getX(), tiles[2][0].getY(), 30f, 3, Util.Direction.LEFT),
		});

		rows[12] = new Row(tiles[10], Util.TypeOfRow.TRAIN, new Train[]{
				new Train(tiles[0][0].getSize(), tiles[0][nColumns - 1].getX(), tiles[12][0].getY())
		});

		Tile[] nontransparentTiles = new Tile[] {tiles[5][1], tiles[5][3], tiles[5][8], tiles[5][10], tiles[10][10], tiles[10][11], tiles[1][6]};

		Level[] temp = new Level[10];
		for (int i = 0; i < 10; i++) {
			temp[i] = new Level(i + 1, new Map(rows, tiles, nontransparentTiles));
		}
		loadLevelsToFile("levels.txt", temp);
	}
}
