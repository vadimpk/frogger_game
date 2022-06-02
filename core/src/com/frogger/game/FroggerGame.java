package com.frogger.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class FroggerGame extends ApplicationAdapter {

	/** initialize static fields */
	private static SpriteBatch gameBatch;
	public static OrthographicCamera gameCamera;
	private static SpriteBatch attributesBatch;

	public static final int tilesPerRow = 15;
	public static final int tilesPerColumn = 30;

	public static Tile[][] tiles = new Tile[tilesPerRow][tilesPerColumn];

	private MovableRow movableRow;
	private Row row;
	private static Frog frog;


	Texture t2;

	/** variables for calculating time between frames in render() method */
	long now = 0;
	long last = 0;
	float dt;

	/**
	 * Method create() runs on the start of the program.
	 * Used to initialize all static objects
	 */
	@Override
	public void create () {

		// TODO: Create starting screen

		// temporary texture for attributes
		// TODO: create separate Java class for attributes (score, lives etc.)
		t2 = new Texture(Gdx.files.internal("temp.png"));

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
		for (int column = 0; column < tilesPerRow; column ++) {
			for (int row = 0; row < tilesPerColumn; row++) {

				tiles[column][row] = new Tile(tilesPerRow, screenWidth, screenHeight, column, row);

			}
		}

		row = new Row(tiles[0][0], "grass_tile.png");
		movableRow = new Logs(tiles[0][1], 5, 0, 3, 200,
							tiles[0][1].getSize() - 20, new Texture(Gdx.files.internal("log.png")), new Texture(Gdx.files.internal("water.png")));


		// spawn frog in the center horizontally and at the bottom vertically
		frog = new Frog(tiles[tilesPerRow / 2][0]);
	}


	/**
	 * Method render() runs every frame
	 */
	@Override
	public void render () {

		// set up batch and camera for the game
		gameCamera.update();
		gameBatch.setProjectionMatrix(gameCamera.combined);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClear(GL20.GL_ALPHA_BITS);


		movableRow.update(0);
		if(movableRow.isCollision(frog)) {
			frog.setX(frog.getX() + movableRow.speed);
		}
		// start game batch
		gameBatch.begin();

		// calculate delta time for each frame
		now = TimeUtils.nanoTime();
		dt = now - last;

		// draw tiles
		for (Tile[] row: tiles) {
			for (Tile tile: row) {

				gameBatch.draw(tile.getTexture(), tile.getX(), tile.getY(), tile.getSize(), tile.getSize());

			}
		}
		movableRow.render(gameBatch);

		row.render(gameBatch);

		// TODO: draw logs

		// draw frog
		gameBatch.draw(frog.getTexture(), frog.getX(), frog.getY(), frog.getSize(), frog.getSize());

		// call function that handles frog logics
		frog.update(dt);

		// calculate delta time for each frame
		last = TimeUtils.nanoTime();

		// end game batch
		gameBatch.end();

		// attributes batch setup
		attributesBatch.begin();
		attributesBatch.draw(t2,0,0,Gdx.graphics.getWidth(), tiles[0][0].getY());
		attributesBatch.draw(t2,0,tiles[0][tilesPerRow -1].getY() + tiles[0][0].getSize(),Gdx.graphics.getWidth(), tiles[0][0].getY());
		attributesBatch.end();

	}


	/**
	 * Method to dispose assets after closing game.
	 */
	@Override
	public void dispose () {
		gameBatch.dispose();
		attributesBatch.dispose();

		for (Tile[] row: tiles) {
			for (Tile tile: row) {

				tile.getTexture().dispose();

			}
		}

		frog.getTexture().dispose();
	}
}
