package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;
import com.frogger.game.objects.Car;
import com.frogger.game.objects.Log;
import com.frogger.game.objects.MovingObject;
import com.frogger.game.objects.Train;
import jdk.internal.joptsimple.internal.Rows;

import java.io.Serializable;

import static com.frogger.game.FroggerGame.*;
import static com.frogger.game.FroggerGame.attributesBatch;

public class Map  implements Serializable{

    private int nColumns;
    private int nRows;

    private Row[] rows;
    private Tile[][] tiles;

    private static Frog frog;

    public static Texture t2;

    /** variables for calculating time between frames in render() method */
    long now = 0;
    long last = 0;
    float dt;

    public Map(Row[] rows, Tile[][] tiles, Tile[] nontransparentTiles) {
        t2 = new Texture(Gdx.files.internal("temp2.jpg"));

        nColumns = tiles[0].length;
        nRows = tiles.length;

        this.rows = rows;
        this.tiles = tiles;

        for(Tile tile: nontransparentTiles) tile.setTransparent(false);

        // spawn frog in the center horizontally and at the bottom vertically
        frog = Frog.get();
        System.out.println(nColumns);
        System.out.println(tiles[0].length);
        frog.setStartingTile(tiles[0][nColumns / 2]);

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

    public int getnColumns() {
        return nColumns;
    }

    public int getnRows() {
        return nRows;
    }

    public Row[] getRows() {
        return rows;
    }

    public Tile[][] getTiles() {
        return tiles;
    }
}
