package com.frogger.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.frogger.game.objects.Car;
import com.frogger.game.objects.Log;
import com.frogger.game.objects.MovingObject;
import com.frogger.game.objects.Train;

public class FroggerGame extends ApplicationAdapter {

	/** initialize static fields */
	private static SpriteBatch gameBatch;
	public static OrthographicCamera gameCamera;
	private static SpriteBatch attributesBatch;

	public static final int nColumns = 15;
	public static final int nRows = 30;

	public static Row[] rows = new Row[nRows];
	public static Tile[][] tiles = new Tile[nRows][nColumns];

	private static Frog frog;

	Texture t2;

	/** variables for calculating time between frames in render() method */
	long now = 0;
	long last = 0;
	float dt;

	public static Frog getFrog() {
		return frog;
	}

	/**
	 * Method create() runs on the start of the program.
	 * Used to initialize all static objects
	 */
	@Override
	public void create () {

		// TODO: Create starting screen

		// temporary texture for attributes
		// TODO: create separate Java class for attributes (score, lives etc.)
		t2 = new Texture(Gdx.files.internal("temp2.jpg"));

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

		for (int row = 0; row < nRows; row++) {
			for (int column = 0; column < nColumns; column++) {
				tiles[row][column] = new Tile(nColumns, screenWidth, screenHeight, row, column);
			}
		}

		for (int i = 0; i < rows.length; i++) {
			rows[i] = new Row(i, Util.TypeOfRow.STATIC, new MovingObject[]{});
		}

		// create moving rows
		rows[6] = new Row(6, Util.TypeOfRow.CAR, new Car[]{
				new Car(tiles[0][0].getSize(), tiles[6][0].getX(), tiles[6][0].getY(), 15f, 3,Util.Direction.LEFT),
				new Car(tiles[0][0].getSize(), tiles[6][6].getX(), tiles[6][0].getY(), 15f, 2, Util.Direction.LEFT),
				new Car(tiles[0][0].getSize(), tiles[6][12].getX(), tiles[6][0].getY(), 15f, 3, Util.Direction.LEFT),
		});

		rows[3] = new Row(3, Util.TypeOfRow.LOG, new Log[]{
				new Log(tiles[0][0].getSize(), tiles[3][0].getX(), tiles[3][0].getY(), 20f, 3, Util.Direction.RIGHT),
				new Log(tiles[0][0].getSize(), tiles[3][5].getX(), tiles[3][0].getY(), 20f, 3, Util.Direction.RIGHT, true, 150000000),
				new Log(tiles[0][0].getSize(), tiles[3][10].getX(), tiles[3][0].getY(), 20f, 3, Util.Direction.RIGHT, true, 150000000),
		});
		rows[4] = new Row(4, Util.TypeOfRow.LOG, new Log[]{
				new Log(tiles[0][0].getSize(), tiles[4][0].getX(), tiles[4][0].getY(), 15f, 3, Util.Direction.LEFT),
				new Log(tiles[0][0].getSize(), tiles[3][5].getX(), tiles[4][0].getY(), 15f, 3, Util.Direction.LEFT),
				new Log(tiles[0][0].getSize(), tiles[3][10].getX(), tiles[4][0].getY(), 15f, 3, Util.Direction.LEFT),
		});

		rows[2] = new Row(2, Util.TypeOfRow.LOG, new Log[]{
				new Log(tiles[0][0].getSize(), tiles[2][0].getX(), tiles[2][0].getY(), 30f, 3, Util.Direction.LEFT),
				new Log(tiles[0][0].getSize(), tiles[3][5].getX(), tiles[2][0].getY(), 30f, 3, Util.Direction.LEFT),
				new Log(tiles[0][0].getSize(), tiles[3][10].getX(), tiles[2][0].getY(), 30f, 3, Util.Direction.LEFT),
		});

		rows[12] = new Row(10, Util.TypeOfRow.TRAIN, new Train[]{
				new Train(tiles[0][0].getSize(), tiles[12][0].getY())
		});


		// spawn frog in the center horizontally and at the bottom vertically
		frog = new Frog(tiles[0][nColumns / 2]);
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

		// render tiles
		for (int row = 0; row < nRows; row++) {
			for (int column = 0; column < nColumns; column++) {
				tiles[row][column].render(gameBatch);
			}
		}

		// render each row (if not static)
		for (Row row: rows) {
			if (row.getType() != Util.TypeOfRow.STATIC) row.render(gameBatch);
		}

		// render frog
		if (frog.isAlive()) frog.render(gameBatch);

		if (!frog.isAlive()) {
			frog = new Frog(tiles[0][nColumns / 2]);
		}

		// calculate delta time for each frame
		last = TimeUtils.nanoTime();

		// end game batch
		gameBatch.end();

		// attributes batch setup
		attributesBatch.begin();
		attributesBatch.draw(t2,0,0,Gdx.graphics.getWidth(), tiles[0][0].getY());
		attributesBatch.draw(t2,0,tiles[nColumns -1][0].getY() + tiles[0][0].getSize(), Gdx.graphics.getWidth(), tiles[0][0].getY());
		attributesBatch.draw(t2,0,0,tiles[0][0].getX(), Gdx.graphics.getHeight());
		attributesBatch.draw(t2,tiles[0][nColumns-1].getX() + tiles[0][0].getSize(),0, Gdx.graphics.getWidth() - tiles[0][nColumns-1].getX() + tiles[0][0].getSize(), Gdx.graphics.getHeight());

		attributesBatch.end();

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
}
