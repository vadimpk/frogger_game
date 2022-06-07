package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;
import com.frogger.game.objects.Car;
import com.frogger.game.objects.Log;
import com.frogger.game.objects.MovingObject;
import com.frogger.game.objects.Train;

import java.io.Serializable;

import static com.frogger.game.FroggerGame.*;
import static com.frogger.game.FroggerGame.attributesBatch;

public class Map  implements Serializable{

    public static final int nColumns = 15;
    public static final int nRows = 30;

    public static Row[] rows = new Row[nRows];
    public static Tile[][] tiles = new Tile[nRows][nColumns];

    private static Frog frog;

    public static Texture t2;

    /** variables for calculating time between frames in render() method */
    long now = 0;
    long last = 0;
    float dt;

    public Map() {
        // get screen size
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        t2 = new Texture(Gdx.files.internal("temp2.jpg"));
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

        tiles[5][1].setTransparent(false);
        tiles[5][3].setTransparent(false);
        tiles[5][8].setTransparent(false);
        tiles[5][10].setTransparent(false);
        tiles[10][10].setTransparent(false);
        tiles[10][11].setTransparent(false);
        tiles[1][6].setTransparent(false);


        // spawn frog in the center horizontally and at the bottom vertically
        frog = new Frog(tiles[0][nColumns / 2]);
    }

    public static Frog getFrog() {
        return frog;
    }

    public void render(float delta){

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
        for (Row row : rows) {
            row.render(gameBatch);
        }

        // render frog
        if (frog.isAlive()) frog.render(gameBatch);

        // calculate delta time for each frame
        last = TimeUtils.nanoTime();

        // end game batch
        gameBatch.end();

        // attributes batch setup
        attributesBatch.begin();
        attributesBatch.draw(t2, 0, 0, Gdx.graphics.getWidth(), tiles[0][0].getY());
        attributesBatch.draw(t2, 0, tiles[nColumns - 1][0].getY() + tiles[0][0].getSize(), Gdx.graphics.getWidth(), tiles[0][0].getY());
        attributesBatch.draw(t2, 0, 0, tiles[0][0].getX(), Gdx.graphics.getHeight());
        attributesBatch.draw(t2, tiles[0][nColumns - 1].getX() + tiles[0][0].getSize(), 0, Gdx.graphics.getWidth() - tiles[0][nColumns - 1].getX() + tiles[0][0].getSize(), Gdx.graphics.getHeight());

        attributesBatch.end();

    }

    public void setFrog(Frog frog) {
        this.frog = frog;
    }
}
