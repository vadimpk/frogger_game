package com.frogger.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

public class FroggerGame extends ApplicationAdapter {

	/** initialize static fields */
	private static SpriteBatch gameBatch;
	public static OrthographicCamera gameCamera;
	private static SpriteBatch attributesBatch;

	public static final int tilesPerRow = 15;
	public static final int tilesPerColumn = 30;

	public static Row[] rows = new Row[tilesPerColumn];

	private static Frog frog;

	Texture t2;
	Texture t3;

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
		t3 = new Texture(Gdx.files.internal("tile2.png"));

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
			for (int row = 0; row < tilesPerColumn; row++) {
				if(row != 2) rows[row] = new StaticRow(row, "tile.png");
				else rows[row] = new MovingRow(row, "tile2.png", "water.png", 10, 0, 2, 2);
			}


//		m = new MovingObject(tiles[0][0].getSize(), tiles[12][0].getX(), tiles[0][7].getY(), 10f,3,t3,false, Util.Direction.RIGHT);
//		m2 = new MovingObject(tiles[0][0].getSize(), tiles[5][0].getX(), tiles[0][7].getY(), 10f,3,t3,false, Util.Direction.RIGHT);



		// spawn frog in the center horizontally and at the bottom vertically
		frog = new Frog(rows[0].getTiles()[tilesPerRow / 2]);
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



		// start game batch
		gameBatch.begin();

		// calculate delta time for each frame
		now = TimeUtils.nanoTime();
		dt = now - last;

		// draw tiles
		for (Row row : rows) {
			row.update(0);
			row.render(gameBatch);
		}


		// TODO: draw logs

		// draw frog
		if (frog.isAlive()) gameBatch.draw(frog.getTexture(), frog.getX(), frog.getY(), frog.getSize(), frog.getSize());

		// call function that handles frog logics
		if (frog.isAlive()) frog.update(dt);

		// calculate delta time for each frame
		last = TimeUtils.nanoTime();

		// end game batch
		gameBatch.end();

		// attributes batch setup
		attributesBatch.begin();
		attributesBatch.draw(t2,0,0,Gdx.graphics.getWidth(), rows[0].getTiles()[0].getY());
		attributesBatch.draw(t2,0,rows[tilesPerRow -1].getTiles()[0].getY() + rows[0].getTiles()[0].getSize(),Gdx.graphics.getWidth(), rows[0].getTiles()[0].getY());
		attributesBatch.end();

	}


	/**
	 * Method to dispose assets after closing game.
	 */
	@Override
	public void dispose () {
		gameBatch.dispose();
		attributesBatch.dispose();

		for (Row staticRow : rows) {
			for (Tile tile: staticRow.getTiles()) {
				tile.getTexture().dispose();
			}
		}

		frog.getTexture().dispose();
	}
}
