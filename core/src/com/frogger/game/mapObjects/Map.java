package com.frogger.game.mapObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.frogger.game.Util;
import com.frogger.game.attributeObjects.Score;
import com.frogger.game.gameObjects.Frog;

import java.util.ArrayList;
import java.util.List;

import static com.frogger.game.FroggerGame.*;


public class Map {

    private final int nColumns;
    private final int nRows;

    private final Row[] rows;
    private final Tile[][] tiles;
    private final Texture rightBgTexture;
    private final Texture leftBgTexture;
    private final Texture bottomBgTexture;
    private final Texture topBgTexture;
    private final Texture white;
    private Score[] scores;

    private static Frog frog;

    public Texture t2;

    public Map(Row[] rows, Tile[][] tiles) {
        scores = new Score[3];
        t2 = new Texture(Gdx.files.internal("temp2.jpg"));

        nColumns = tiles[0].length;
        nRows = tiles.length;

        this.rows = rows;
        this.tiles = tiles;

        // spawn frog in the center horizontally and at the bottom vertically
        frog = Frog.get();
        frog.setStartingTile(tiles[0][nColumns / 2]);

        List<Score> scoreList = new ArrayList<>();
        for(Tile[] row: tiles)
            for (Tile tile : row) if (tile.isScore()) scoreList.add(new Score(tile));
        scores = scoreList.toArray(new Score[0]);

        rightBgTexture = new Texture(Gdx.files.internal("backgrounds/right.png"));
        leftBgTexture = new Texture(Gdx.files.internal("backgrounds/left.png"));
        bottomBgTexture = new Texture(Gdx.files.internal("backgrounds/bottom.png"));
        topBgTexture = new Texture(Gdx.files.internal("backgrounds/top.png"));
        white = new Texture(Gdx.files.internal("backgrounds/white.png"));

    }

    public static Frog getFrog() {
        return frog;
    }

    public void render(){

        // set up batch and camera for the game
        gameCamera.update();
        gameBatch.setProjectionMatrix(gameCamera.combined);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClear(GL20.GL_ALPHA_BITS);


        // start game batch
        gameBatch.begin();

        // render tiles
        for (int row = 0; row < nRows; row++) {
            for (int column = 0; column < nColumns; column++) {
                tiles[row][column].render(gameBatch);
            }
        }

        // render each row (if not static)
        for (Row row : rows) {
            if (!frog.isGoingToDrown())
                if (row.getType() == Util.TypeOfRow.LOG) row.render(gameBatch);
        }

        // render frog
        if (frog.isAlive()) frog.render(gameBatch);

        // render each row (if not static)
        for (Row row : rows) {
            if (frog.isGoingToDrown())
               row.render(gameBatch);
            else if (row.getType() != Util.TypeOfRow.LOG) row.render(gameBatch);
        }

        for (Score s : scores) if(!s.isCollected()) s.render(gameBatch);

        // end game batch
        gameBatch.end();

        // attributes batch setup
        attributesBatch.begin();

        attributesBatch.draw(white, tiles[0][0].getX(), 0, tiles[0][nColumns - 1].getX() + tiles[0][0].getSize() - tiles[0][0].getX(), tiles[0][0].getY());
        attributesBatch.draw(white, tiles[0][0].getX(), tiles[nColumns - 1][0].getY() + tiles[0][0].getSize(), tiles[0][nColumns - 1].getX() + tiles[0][0].getSize() - tiles[0][0].getX(), tiles[0][0].getY());
        attributesBatch.draw(white, 0, 0, tiles[0][0].getX(), Gdx.graphics.getHeight());
        attributesBatch.draw(white, tiles[0][nColumns - 1].getX() + tiles[0][0].getSize(), 0, Gdx.graphics.getWidth() - tiles[0][nColumns - 1].getX() + tiles[0][0].getSize(), Gdx.graphics.getHeight());

        attributesBatch.draw(bottomBgTexture, tiles[0][0].getX(), 0, tiles[0][nColumns - 1].getX() + tiles[0][0].getSize() - tiles[0][0].getX(), tiles[0][0].getY());
        attributesBatch.draw(topBgTexture, tiles[0][0].getX(), tiles[nColumns - 1][0].getY() + tiles[0][0].getSize(), tiles[0][nColumns - 1].getX() + tiles[0][0].getSize() - tiles[0][0].getX(), tiles[0][0].getY());
        attributesBatch.draw(leftBgTexture, 0, 0, tiles[0][0].getX(), Gdx.graphics.getHeight());
        attributesBatch.draw(rightBgTexture, tiles[0][nColumns - 1].getX() + tiles[0][0].getSize(), 0, Gdx.graphics.getWidth() - tiles[0][nColumns - 1].getX() + tiles[0][0].getSize(), Gdx.graphics.getHeight());
        attributesBatch.end();
    }


    public void pausedRender() {

        // set up batch and camera for the game
        gameCamera.update();
        gameBatch.setProjectionMatrix(gameCamera.combined);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClear(GL20.GL_ALPHA_BITS);


        // start game batch
        gameBatch.begin();

        // render tiles
        for (int row = 0; row < nRows; row++)
            for (int column = 0; column < nColumns; column++) tiles[row][column].render(gameBatch);

        // render each row (if not static)
        for (Row row : rows) if (row.getType() == Util.TypeOfRow.LOG) row.pausedRender(gameBatch);

        // render frog
        if (frog.isAlive()) frog.render(gameBatch);

        for (Score s : scores) if(!s.isCollected()) s.render(gameBatch);

        gameBatch.end();

        // attributes batch setup
        // attributes batch setup
        attributesBatch.begin();

        attributesBatch.draw(white, tiles[0][0].getX(), 0, tiles[0][nColumns - 1].getX() + tiles[0][0].getSize() - tiles[0][0].getX(), tiles[0][0].getY());
        attributesBatch.draw(white, tiles[0][0].getX(), tiles[nColumns - 1][0].getY() + tiles[0][0].getSize(), tiles[0][nColumns - 1].getX() + tiles[0][0].getSize() - tiles[0][0].getX(), tiles[0][0].getY());
        attributesBatch.draw(white, 0, 0, tiles[0][0].getX(), Gdx.graphics.getHeight());
        attributesBatch.draw(white, tiles[0][nColumns - 1].getX() + tiles[0][0].getSize(), 0, Gdx.graphics.getWidth() - tiles[0][nColumns - 1].getX() + tiles[0][0].getSize(), Gdx.graphics.getHeight());

        attributesBatch.draw(bottomBgTexture, tiles[0][0].getX(), 0, tiles[0][nColumns - 1].getX() + tiles[0][0].getSize() - tiles[0][0].getX(), tiles[0][0].getY());
        attributesBatch.draw(topBgTexture, tiles[0][0].getX(), tiles[nColumns - 1][0].getY() + tiles[0][0].getSize(), tiles[0][nColumns - 1].getX() + tiles[0][0].getSize() - tiles[0][0].getX(), tiles[0][0].getY());
        attributesBatch.draw(leftBgTexture, 0, 0, tiles[0][0].getX(), Gdx.graphics.getHeight());
        attributesBatch.draw(rightBgTexture, tiles[0][nColumns - 1].getX() + tiles[0][0].getSize(), 0, Gdx.graphics.getWidth() - tiles[0][nColumns - 1].getX() + tiles[0][0].getSize(), Gdx.graphics.getHeight());
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

    public Score[] getScores() {
        return scores;
    }

    public void dispose() {
        rightBgTexture.dispose();
        leftBgTexture.dispose();
        bottomBgTexture.dispose();
        topBgTexture.dispose();
    }
}
