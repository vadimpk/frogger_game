package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.frogger.game.objects.Car;
import com.frogger.game.objects.Log;
import com.frogger.game.objects.MovingObject;
import com.frogger.game.objects.Train;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LevelsGenerator implements Serializable{

    public LevelsGenerator() {
        createLevels();
    }

    public Level[] getLevels() {
        LevelParameters[] levelParameters = loadFromFile("levels.txt");
        Level[] levels = new Level[10];

        for (int i = 0; i < levels.length; i++) {
            levels[i] = convert(levelParameters[i]);
        }

        return levels;
    }

    private Level convert(LevelParameters levelParameter) {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        int nRows = levelParameter.nRows;
        int nColumns = levelParameter.nColumns;

        Row[] rows = new Row[nRows];
        Tile[][] tiles = new Tile[nRows][nColumns];

        for (int row = 0; row < nRows; row++) {
            for (int column = 0; column < nColumns; column++) {
                tiles[row][column] = new Tile(nColumns, screenWidth, screenHeight, row, column);
            }
        }

        for (RowsParameters rowsParameter : levelParameter.rowsParameters) {
            for (TileParameters tileParameter : rowsParameter.tileParameters) {
                if(!tileParameter.transparent) tiles[tileParameter.row][tileParameter.column].setTransparent(false);
                if(tileParameter.isScore) tiles[tileParameter.row][tileParameter.column].setScore(true);
            }
        }

        float size = tiles[0][0].getSize();

        for (int i = 0; i < rows.length; i++) {
            RowsParameters rowsParameters = levelParameter.rowsParameters[i];
            MovingObject[] movingObjects = new MovingObject[rowsParameters.movingObjectParameters.length];


                for (int j = 0; j < movingObjects.length; j++) {
                    MovingObjectParameters movingObjectParameters = rowsParameters.movingObjectParameters[j];
                    if (rowsParameters.type == Util.TypeOfRow.LOG) {
                        Tile startingTile = tiles[movingObjectParameters.startingTile.row][movingObjectParameters.startingTile.column];
                        if (movingObjectParameters.isFading)
                            movingObjects[j] = new Log(size, startingTile.getX(), startingTile.getY(), movingObjectParameters.speed, movingObjectParameters.length, movingObjectParameters.direction, movingObjectParameters.isFading, movingObjectParameters.deltaTime);
                        else
                            movingObjects[j] = new Log(size, startingTile.getX(), startingTile.getY(), movingObjectParameters.speed, movingObjectParameters.length, movingObjectParameters.direction);
                    } else if (rowsParameters.type == Util.TypeOfRow.CAR) {
                        Tile startingTile = tiles[movingObjectParameters.startingTile.row][movingObjectParameters.startingTile.column];
                        movingObjects[j] = new Car(size, startingTile.getX(), startingTile.getY(), movingObjectParameters.speed, movingObjectParameters.length, movingObjectParameters.direction);
                    } else if (rowsParameters.type == Util.TypeOfRow.TRAIN) {
                        Tile startingTile = tiles[movingObjectParameters.startingTile.row][movingObjectParameters.startingTile.column];
                        movingObjects[j] = new Train(size, startingTile.getX(), startingTile.getY());
                    } else {
                        System.out.println("u stupid");
                    }
                }

                rows[i] = new Row(tiles[i], rowsParameters.type, movingObjects);



        }

        Map map = new Map(rows, tiles);
        return new Level(levelParameter.number, levelParameter.scores, map);
    }

    public void createLevels(){
        LevelParameters[] levelParameters = new LevelParameters[10];

        RowsParameters[] rows;
        TileParameters[][] tiles;
        TileParameters[] nontransparentTiles;
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        int nColumns;
        int nRows;

        // Level 1
        nColumns = 11;
        nRows = 11 ;
        rows = new RowsParameters[nRows];
        tiles = new TileParameters[nRows][nColumns];

        for (int row = 0; row < nRows; row++) {
            for (int column = 0; column < nColumns; column++) {
                tiles[row][column] = new TileParameters(row, column);
            }
        }

        tiles[2][5].isScore = true;
        tiles[4][0].isScore = true;
        tiles[7][10].isScore = true;

        for (int i = 0; i < rows.length; i++) {
            rows[i] = new RowsParameters(tiles[i], Util.TypeOfRow.STATIC, new MovingObjectParameters[]{});
        }

        // create moving rows
        rows[3] = new RowsParameters(tiles[3], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[3][0], 20f, 3,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[3][7], 20f, 3, Util.Direction.LEFT),
        });

        rows[7] = new RowsParameters(tiles[7], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[7][2], 18f, 3, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[7][9], 18f, 3, Util.Direction.RIGHT, true, 200000000),
        });


        nontransparentTiles = new TileParameters[] {tiles[1][5], tiles[2][3], tiles[2][7], tiles[4][1], tiles[4][10], tiles[6][3], tiles[6][5],tiles[6][7]};
        for (TileParameters tile: nontransparentTiles) {
            tile.transparent = false;
        }

        levelParameters[0] = new LevelParameters(1, nColumns, rows);

        //Level 2

        nColumns = 11;
        nRows = 12;
        rows = new RowsParameters[nRows];
        tiles = new TileParameters[nRows][nColumns];

        for (int row = 0; row < nRows; row++) {
            for (int column = 0; column < nColumns; column++) {
                tiles[row][column] = new TileParameters(row, column);
            }
        }

        tiles[2][2].isScore = true;
        tiles[4][0].isScore = true;
        tiles[8][10].isScore = true;

        for (int i = 0; i < rows.length; i++) {
            rows[i] = new RowsParameters(tiles[i], Util.TypeOfRow.STATIC, new MovingObjectParameters[]{});
        }

        // create moving rows
        rows[2] = new RowsParameters(tiles[2], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[2][0], 18f, 3,Util.Direction.RIGHT, true, 180000000),
                new MovingObjectParameters(tiles[2][7], 18f, 3, Util.Direction.RIGHT),
        });

        rows[3] = new RowsParameters(tiles[3], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[3][2], 18f, 3, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[3][9], 18f, 3, Util.Direction.LEFT, true, 180000000),
        });

        rows[8] = new RowsParameters(tiles[8], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[8][5], 17f, 2, Util.Direction.RIGHT),
        });

        rows[9] = new RowsParameters(tiles[9], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[9][0], 17f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[9][7], 17f, 2, Util.Direction.LEFT),
        });


        nontransparentTiles = new TileParameters[] {tiles[0][8], tiles[1][3], tiles[2][7], tiles[4][1], tiles[5][8], tiles[6][4], tiles[10][8]};
        for (TileParameters tile: nontransparentTiles) {
            tile.transparent = false;
        }

        levelParameters[1] = new LevelParameters(2, nColumns, rows);


        // Level 3

        nColumns = 13;
        nRows = 13;
        rows = new RowsParameters[nRows];
        tiles = new TileParameters[nRows][nColumns];

        for (int row = 0; row < nRows; row++) {
            for (int column = 0; column < nColumns; column++) {
                tiles[row][column] = new TileParameters(row, column);
            }
        }

        tiles[11][6].isScore = true;
        tiles[7][9].isScore = true;
        tiles[4][3].isScore = true;

        for (int i = 0; i < rows.length; i++) {
            rows[i] = new RowsParameters(tiles[i], Util.TypeOfRow.STATIC, new MovingObjectParameters[]{});
        }

        // create moving rows
        rows[3] = new RowsParameters(tiles[3], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[3][1], 18f, 3,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[3][9], 18f, 3, Util.Direction.RIGHT),
        });

        rows[4] = new RowsParameters(tiles[4], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[4][2], 20f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[4][7], 20f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[4][12], 20f, 2, Util.Direction.LEFT, true, 180000000),
        });

        rows[5] = new RowsParameters(tiles[5], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[5][2], 16f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[5][10], 16f, 2, Util.Direction.RIGHT, true, 160000000),
        });


        rows[8] = new RowsParameters(tiles[8], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[8][0], 18f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[8][5], 18f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[8][10], 18f, 2, Util.Direction.LEFT),
        });

        rows[9] = new RowsParameters(tiles[9], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[9][0], 17f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[9][7], 17f, 2, Util.Direction.RIGHT),
        });

        rows[11] = new RowsParameters(tiles[11], Util.TypeOfRow.TRAIN, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[11][0], 0, 0, null),
        });


        nontransparentTiles = new TileParameters[] {tiles[2][0], tiles[2][2], tiles[2][4], tiles[2][6], tiles[2][8], tiles[2][10], tiles[2][12],
                                                    tiles[6][3], tiles[6][6], tiles[6][9],
                                                    tiles[7][0], tiles[7][4], tiles[7][8], tiles[7][12]};
        for (TileParameters tile: nontransparentTiles) {
            tile.transparent = false;
        }

        levelParameters[2] = new LevelParameters(3, nColumns, rows);

        //Level 4


        nColumns = 13;
        nRows = 16;
        rows = new RowsParameters[nRows];
        tiles = new TileParameters[nRows][nColumns];

        for (int row = 0; row < nRows; row++) {
            for (int column = 0; column < nColumns; column++) {
                tiles[row][column] = new TileParameters(row, column);
            }
        }

        tiles[1][10].isScore = true;
        tiles[5][7].isScore = true;
        tiles[9][2].isScore = true;

        for (int i = 0; i < rows.length; i++) {
            rows[i] = new RowsParameters(tiles[i], Util.TypeOfRow.STATIC, new MovingObjectParameters[]{});
        }

        // create moving rows
        rows[1] = new RowsParameters(tiles[1], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[1][1], 16f, 3,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[1][9], 16f, 3, Util.Direction.RIGHT),
        });
        rows[2] = new RowsParameters(tiles[2], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[2][3], 16.5f, 3,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[2][12], 16.5f, 3, Util.Direction.LEFT),
        });

        rows[4] = new RowsParameters(tiles[4], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[4][2], 16f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[4][7], 16f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[4][12], 16f, 2, Util.Direction.LEFT, true, 180000000),
        });

        rows[5] = new RowsParameters(tiles[5], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[5][2], 15f, 2, Util.Direction.RIGHT, true, 150000000),
                new MovingObjectParameters(tiles[5][10], 15f, 2, Util.Direction.RIGHT),
        });

        rows[6] = new RowsParameters(tiles[6], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[6][0], 14f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[6][8], 14f, 2, Util.Direction.LEFT, true, 180000000),
        });

        rows[9] = new RowsParameters(tiles[9], Util.TypeOfRow.TRAIN, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[9][0], 0, 0, null),
        });


        rows[11] = new RowsParameters(tiles[11], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[11][0], 18f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[11][7], 18f, 2, Util.Direction.LEFT),
        });

        rows[12] = new RowsParameters(tiles[12], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[12][1], 17f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[12][8], 17f, 2, Util.Direction.LEFT),
        });

        rows[13] = new RowsParameters(tiles[13], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[13][3], 16f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[13][12], 16f, 2, Util.Direction.RIGHT),
        });


        nontransparentTiles = new TileParameters[] {tiles[14][0], tiles[14][2], tiles[14][4], tiles[14][6], tiles[14][8], tiles[14][10], tiles[14][12],
                tiles[3][3], tiles[3][6], tiles[3][9],
                tiles[7][0], tiles[7][3], tiles[7][6], tiles[7][9],tiles[7][12],
                tiles[8][1], tiles[8][4], tiles[8][7], tiles[8][10],
                tiles[10][2], tiles[10][5], tiles[10][8], tiles[10][11]};
        for (TileParameters tile: nontransparentTiles) {
            tile.transparent = false;
        }

        levelParameters[3] = new LevelParameters(4, nColumns, rows);


        // Level 5


        nColumns = 15;
        nRows = 20;
        rows = new RowsParameters[nRows];
        tiles = new TileParameters[nRows][nColumns];

        for (int row = 0; row < nRows; row++) {
            for (int column = 0; column < nColumns; column++) {
                tiles[row][column] = new TileParameters(row, column);
            }
        }

        tiles[4][12].isScore = true;
        tiles[8][7].isScore = true;
        tiles[16][0].isScore = true;

        for (int i = 0; i < rows.length; i++) {
            rows[i] = new RowsParameters(tiles[i], Util.TypeOfRow.STATIC, new MovingObjectParameters[]{});
        }

        // create moving rows
        rows[1] = new RowsParameters(tiles[1], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[1][1], 14f, 2,Util.Direction.RIGHT, true, 120000000),
                new MovingObjectParameters(tiles[1][10], 14f, 2, Util.Direction.RIGHT, true, 130000000),
        });


        rows[3] = new RowsParameters(tiles[3], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[3][0], 17f, 2,Util.Direction.LEFT, true, 150000000),
                new MovingObjectParameters(tiles[3][7], 17f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[3][14], 17f, 2, Util.Direction.LEFT),
        });

        rows[4] = new RowsParameters(tiles[4], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[4][1], 16f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[4][8], 16f, 2, Util.Direction.RIGHT, true, 150000000),
                new MovingObjectParameters(tiles[4][14], 16f, 2, Util.Direction.RIGHT),
        });

        rows[5] = new RowsParameters(tiles[5], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[5][2], 17, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[5][5], 17f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[5][8], 17f, 1, Util.Direction.RIGHT, true, 150000000),
                new MovingObjectParameters(tiles[5][11], 17f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[5][14], 17f, 1, Util.Direction.RIGHT, true, 150000000),
        });

        rows[6] = new RowsParameters(tiles[6], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[6][1], 16.5f, 1, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[6][4], 16.5f, 1, Util.Direction.LEFT, true, 150000000),
                new MovingObjectParameters(tiles[6][7], 16.5f, 1, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[6][10], 16.5f, 1, Util.Direction.LEFT, true, 150000000),
                new MovingObjectParameters(tiles[6][13], 16.5f, 1, Util.Direction.LEFT),
        });

        rows[8] = new RowsParameters(tiles[8], Util.TypeOfRow.TRAIN, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[8][0], 0, 0, null),
        });


        rows[10] = new RowsParameters(tiles[10], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[10][0], 18f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[10][7], 18f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[10][14], 18f, 2, Util.Direction.LEFT),
        });

        rows[11] = new RowsParameters(tiles[11], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[11][1], 17f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[11][8], 17f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[11][14], 17f, 2, Util.Direction.RIGHT),
        });

        rows[12] = new RowsParameters(tiles[12], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[12][3], 16f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[12][10], 16f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[12][14], 16f, 2, Util.Direction.LEFT),
        });


        rows[14] = new RowsParameters(tiles[14], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[14][0], 17f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[14][7], 17f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[14][14], 17f, 2, Util.Direction.LEFT),
        });

        rows[15] = new RowsParameters(tiles[15], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[15][1], 16f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[15][8], 16f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[15][14], 16f, 2, Util.Direction.RIGHT, true, 160000000),
        });

        rows[18] = new RowsParameters(tiles[8], Util.TypeOfRow.TRAIN, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[8][0], 0, 0, null),
        });

        nontransparentTiles = new TileParameters[] {tiles[2][1], tiles[2][5], tiles[2][9], tiles[2][14],
                                                    tiles[7][2], tiles[7][8], tiles[9][0], tiles[13][1], tiles[13][5],
                                                    tiles[17][0], tiles[16][1], tiles[16][3], tiles[17][4], tiles[17][6], tiles[16][7], tiles[17][8],
                                                    tiles[17][10], tiles[16][11], tiles[16][13] , tiles[16][14]};
        for (TileParameters tile: nontransparentTiles) {
            tile.transparent = false;
        }

        levelParameters[4] = new LevelParameters(5, nColumns, rows);


        // Level 6

        nColumns = 15;
        nRows = 23;
        rows = new RowsParameters[nRows];
        tiles = new TileParameters[nRows][nColumns];

        for (int row = 0; row < nRows; row++) {
            for (int column = 0; column < nColumns; column++) {
                tiles[row][column] = new TileParameters(row, column);
            }
        }

        tiles[0][3].isScore = true;
        tiles[10][7].isScore = true;
        tiles[16][2].isScore = true;

        for (int i = 0; i < rows.length; i++) {
            rows[i] = new RowsParameters(tiles[i], Util.TypeOfRow.STATIC, new MovingObjectParameters[]{});
        }

        // create moving rows
        rows[1] = new RowsParameters(tiles[1], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[1][1], 15f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[1][6], 15f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[1][11], 15f, 2, Util.Direction.LEFT),
        });

        rows[2] = new RowsParameters(tiles[2], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[2][2], 15f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[2][7], 15f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[2][12], 15f, 2, Util.Direction.LEFT),
        });

        rows[3] = new RowsParameters(tiles[3], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[3][3], 15.5f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[3][8], 15.5f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[3][13], 15.5f, 2, Util.Direction.RIGHT),
        });

        rows[4] = new RowsParameters(tiles[4], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[4][2], 14f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[4][7], 14f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[4][12], 14f, 2, Util.Direction.RIGHT),
        });

        rows[8] = new RowsParameters(tiles[8], Util.TypeOfRow.TRAIN, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[8][0], 0, 0, null),
        });

        rows[9] = new RowsParameters(tiles[9], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[9][1], 15f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[9][6], 15f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[9][11], 15f, 2, Util.Direction.RIGHT),
        });

        rows[10] = new RowsParameters(tiles[10], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[10][2], 15.5f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[10][7], 15.5f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[10][12], 15.5f, 2, Util.Direction.LEFT),
        });

        rows[11] = new RowsParameters(tiles[11], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[11][2], 15f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[11][7], 15f, 2, Util.Direction.LEFT, true, 150000000),
                new MovingObjectParameters(tiles[11][12], 15f, 2, Util.Direction.LEFT),
        });

        rows[12] = new RowsParameters(tiles[12], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[12][1], 14f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[12][6], 14f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[12][11], 14f, 2, Util.Direction.RIGHT),
        });


        rows[15] = new RowsParameters(tiles[15], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[15][1], 16.5f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[15][5], 16.5f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[15][9], 16.5f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[15][13], 16.5f, 2, Util.Direction.RIGHT),
        });

        rows[16] = new RowsParameters(tiles[16], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[16][2], 16f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[16][8], 16f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[16][11], 16f, 2, Util.Direction.LEFT),
        });

        rows[17] = new RowsParameters(tiles[17], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[17][1], 13f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[17][10], 13f, 2, Util.Direction.RIGHT),
        });

        rows[20] = new RowsParameters(tiles[20], Util.TypeOfRow.TRAIN, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[20][0], 0, 0, null),
        });

        rows[21] = new RowsParameters(tiles[21], Util.TypeOfRow.TRAIN, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[21][0], 0, 0, null),
        });



        nontransparentTiles = new TileParameters[] {tiles[0][4], tiles[5][3], tiles[5][10], tiles[6][7],
                tiles[7][1], tiles[7][12], tiles[13][2], tiles[13][11], tiles[13][14],
                tiles[18][1], tiles[18][7], tiles[18][12], tiles[19][10], tiles[20][5], tiles[21][2]};
        for (TileParameters tile: nontransparentTiles) {
            tile.transparent = false;
        }

        levelParameters[5] = new LevelParameters(6, nColumns, rows);




        // Level 7

        nColumns = 15;
        nRows = 23;
        rows = new RowsParameters[nRows];
        tiles = new TileParameters[nRows][nColumns];

        for (int row = 0; row < nRows; row++) {
            for (int column = 0; column < nColumns; column++) {
                tiles[row][column] = new TileParameters(row, column);
            }
        }

        tiles[10][14].isScore = true;
        tiles[16][11].isScore = true;
        tiles[19][2].isScore = true;

        for (int i = 0; i < rows.length; i++) {
            rows[i] = new RowsParameters(tiles[i], Util.TypeOfRow.STATIC, new MovingObjectParameters[]{});
        }


        // create moving rows

        //TODO: BRIDGE 12
//        rows[1] = new RowsParameters(tiles[1], Util.TypeOfRow.BRIDGE

        rows[2] = new RowsParameters(tiles[2], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[2][4], 13f, 2, Util.Direction.LEFT, true, 150_000_000),
                new MovingObjectParameters(tiles[2][12], 13f, 2, Util.Direction.LEFT),
        });

        rows[3] = new RowsParameters(tiles[3], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[3][1], 11f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[3][9], 11f, 2, Util.Direction.RIGHT, true, 160_000_000),
        });

        rows[4] = new RowsParameters(tiles[4], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[4][2], 11f, 2,Util.Direction.LEFT, true, 150_000_000),
                new MovingObjectParameters(tiles[4][10], 11f, 2, Util.Direction.LEFT),
        });

        rows[5] = new RowsParameters(tiles[5], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[5][1], 12.5f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[5][5], 12.5f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[5][9], 12.5f, 2, Util.Direction.RIGHT, true, 130_000_000),
                new MovingObjectParameters(tiles[5][13], 12.5f, 2, Util.Direction.RIGHT),
        });

        //TODO: BRIDGE 4
//        rows[6] = new

        rows[7] = new RowsParameters(tiles[7], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[7][1], 13f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[7][6], 13f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[7][11], 13f, 2, Util.Direction.LEFT),
        });

        rows[8] = new RowsParameters(tiles[8], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[8][2], 13.5f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[8][7], 13.5f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[8][12], 13.5f, 2, Util.Direction.LEFT),
        });

        rows[9] = new RowsParameters(tiles[9], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[9][1], 14f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[9][6], 14f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[9][10], 14f, 2, Util.Direction.RIGHT),
        });


        rows[12] = new RowsParameters(tiles[12], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[12][1], 12f, 1,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[12][4], 12f, 1, Util.Direction.RIGHT, true, 160_000_000),
                new MovingObjectParameters(tiles[12][7], 12f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[12][10], 12f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[12][13], 12f, 1, Util.Direction.RIGHT, true, 130_000_000),
        });


        rows[13] = new RowsParameters(tiles[13], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[13][2], 15f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[13][8], 15f, 2, Util.Direction.LEFT, true, 140_000_000),
                new MovingObjectParameters(tiles[13][13], 15f, 2, Util.Direction.LEFT),
        });

        rows[14] = new RowsParameters(tiles[14], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[14][2], 11f, 1,Util.Direction.RIGHT, true, 120_000_000),
                new MovingObjectParameters(tiles[14][6], 11f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[14][10], 11f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[14][14], 11f, 1, Util.Direction.RIGHT),
        });

        rows[15] = new RowsParameters(tiles[15], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[15][1], 13.5f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[15][8], 13.5f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[15][13], 13.5f, 2, Util.Direction.LEFT, true, 150_000_000),
        });

        // TODO RIDGE 11
//        rows[16] =

        rows[17] = new RowsParameters(tiles[17], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[17][2], 10f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[17][9], 10f, 2, Util.Direction.RIGHT),
        });

        rows[18] = new RowsParameters(tiles[18], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[18][6], 13f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[18][10], 13f, 2, Util.Direction.RIGHT),
        });

        rows[19] = new RowsParameters(tiles[19], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[19][0], 12.5f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[19][9], 12.5f, 2, Util.Direction.LEFT),
        });

        rows[20] = new RowsParameters(tiles[20], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[20][7], 10f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[20][13], 10f, 2, Util.Direction.LEFT),
        });


        nontransparentTiles = new TileParameters[] {tiles[10][1], tiles[10][7], tiles[10][13],
                tiles[11][10], tiles[11][14], tiles[22][13], tiles[21][3], tiles[21][9]};
        for (TileParameters tile: nontransparentTiles) {
            tile.transparent = false;
        }

        levelParameters[6] = new LevelParameters(7, nColumns, rows);


        // Level 8

        nColumns = 15;
        nRows = 31;
        rows = new RowsParameters[nRows];
        tiles = new TileParameters[nRows][nColumns];

        for (int row = 0; row < nRows; row++) {
            for (int column = 0; column < nColumns; column++) {
                tiles[row][column] = new TileParameters(row, column);
            }
        }

        tiles[3][14].isScore = true;
        tiles[14][14].isScore = true;
        tiles[27][2].isScore = true;

        for (int i = 0; i < rows.length; i++) {
            rows[i] = new RowsParameters(tiles[i], Util.TypeOfRow.STATIC, new MovingObjectParameters[]{});
        }

        // create moving rows
        rows[2] = new RowsParameters(tiles[2], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[2][4], 10f, 2, Util.Direction.LEFT, true, 150_000_000),
                new MovingObjectParameters(tiles[2][12], 10f, 2, Util.Direction.LEFT),
        });

        rows[3] = new RowsParameters(tiles[3], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[3][1], 11f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[3][7], 11f, 2, Util.Direction.RIGHT, true, 170_000_000),
                new MovingObjectParameters(tiles[3][12], 11f, 2, Util.Direction.RIGHT, true, 130_000_000),
        });

        rows[4] = new RowsParameters(tiles[4], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[4][1], 9f, 1,Util.Direction.LEFT, true, 150_000_000),
                new MovingObjectParameters(tiles[4][4], 9f, 1, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[4][7], 9f, 1, Util.Direction.LEFT, true, 120_000_000),
                new MovingObjectParameters(tiles[4][10], 9f, 1, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[4][13], 9f, 1, Util.Direction.LEFT),
        });

        // TODO RIDGE 11
//        rows[5] =

        // TODO RIDGE 1
//        rows[10] =

        rows[11] = new RowsParameters(tiles[11], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[11][1], 10f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[11][6], 10f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[11][11], 10f, 2, Util.Direction.RIGHT),
        });

        rows[12] = new RowsParameters(tiles[12], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[12][2], 9f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[12][7], 9f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[12][12], 9f, 2, Util.Direction.RIGHT),
        });

        rows[14] = new RowsParameters(tiles[14], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[14][0], 10f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[14][5], 10f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[14][10], 10f, 2, Util.Direction.RIGHT),
        });

        rows[15] = new RowsParameters(tiles[15], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[15][2], 8f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[15][9], 8f, 2, Util.Direction.LEFT),
        });

        rows[19] = new RowsParameters(tiles[19], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[19][0], 0, 0, null)
        });
        rows[20] = new RowsParameters(tiles[20], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[20][0], 0, 0, null)
        });
        rows[21] = new RowsParameters(tiles[21], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[21][0], 0, 0, null)
        });

        rows[22] = new RowsParameters(tiles[22], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[22][1], 9f, 1,Util.Direction.RIGHT, true, 150_000_000),
                new MovingObjectParameters(tiles[22][4], 9f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[22][7], 9f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[22][10], 9f, 1, Util.Direction.RIGHT, true, 120_000_000),
                new MovingObjectParameters(tiles[22][13], 9f, 1, Util.Direction.RIGHT),
        });

        rows[23] = new RowsParameters(tiles[23], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[23][2], 11f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[23][7], 11f, 2, Util.Direction.LEFT, true, 180_000_000),
                new MovingObjectParameters(tiles[23][12], 11f, 2, Util.Direction.LEFT),
        });

        rows[24] = new RowsParameters(tiles[24], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[24][1], 10f, 3,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[24][7], 10f, 3,Util.Direction.LEFT, true, 110_000_000),
        });

        rows[26] = new RowsParameters(tiles[26], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[26][0], 0, 0, null)
        });

        rows[27] = new RowsParameters(tiles[27], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[27][3], 9f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[27][7], 9f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[27][13], 9f, 2, Util.Direction.RIGHT),
        });

        rows[28] = new RowsParameters(tiles[28], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[28][6], 8f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[28][12], 8f, 2, Util.Direction.LEFT),
        });



        nontransparentTiles = new TileParameters[] {tiles[0][1], tiles[0][13], tiles[1][2], tiles[1][5],
                tiles[6][1], tiles[9][2], tiles[8][3], tiles[7][5], tiles[6][6],
                tiles[8][8], tiles[9][9], tiles[6][10], tiles[9][12], tiles[7][13],
                tiles[13][2], tiles[13][13], tiles[16][2], tiles[20][3], tiles[18][7], tiles[17][10], tiles[21][12],
                tiles[25][2], tiles[29][4], tiles[30][13]};
        for (TileParameters tile: nontransparentTiles) {
            tile.transparent = false;
        }

        levelParameters[7] = new LevelParameters(8, nColumns, rows);



        // Other levels
        nColumns = 15;
        nRows = 30;

        rows = new RowsParameters[nRows];
        tiles = new TileParameters[nRows][nColumns];

        for (int row = 0; row < nRows; row++) {
            for (int column = 0; column < nColumns; column++) {
                tiles[row][column] = new TileParameters(row, column);
            }
        }

        tiles[0][6].isScore = true;
        tiles[10][9].isScore = true;
        tiles[14][1].isScore = true;

        for (int i = 0; i < rows.length; i++) {
            rows[i] = new RowsParameters(tiles[i], Util.TypeOfRow.STATIC, new MovingObjectParameters[]{});
        }

        // create moving rows
        rows[6] = new RowsParameters(tiles[6], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[6][0], 15f, 3,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[6][6], 15f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[6][12], 15f, 3, Util.Direction.LEFT),
        });

        rows[3] = new RowsParameters(tiles[3], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[3][0], 20f, 3, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[3][5], 20f, 3, Util.Direction.RIGHT,  true, 150000000),
                new MovingObjectParameters(tiles[3][10], 20f, 3, Util.Direction.RIGHT,  true, 150000000),
        });
        rows[4] = new RowsParameters(tiles[4], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[4][0], 15f, 3, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[4][5], 15f, 3, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[4][10], 15f, 3, Util.Direction.LEFT),
        });

        rows[2] = new RowsParameters(tiles[2], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[2][0], 30f, 3, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[2][5], 30f, 3, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[2][10], 30f, 3, Util.Direction.LEFT),
        });

        rows[12] = new RowsParameters(tiles[10], Util.TypeOfRow.TRAIN, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[12][nColumns - 1], 0, 0, null)
        });


        nontransparentTiles = new TileParameters[] {tiles[5][1], tiles[5][3], tiles[5][8], tiles[5][10], tiles[10][10], tiles[10][11], tiles[1][6]};
        for (TileParameters tile: nontransparentTiles) {
            tile.transparent = false;
        }

        for (int i = 8; i < 10; i++) {
            levelParameters[i] = new LevelParameters(i + 1, nColumns, rows);
        }

        loadToFile("levels.txt", levelParameters);
    }

    public void loadToFile(String src, LevelParameters[] levels) {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(Paths.get(src)))) {
            out.writeObject(levels);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public LevelParameters[] loadFromFile(String src) {
        LevelParameters[] levels;
        try(ObjectInputStream out = new ObjectInputStream(Files.newInputStream(Paths.get(src)))) {
            levels = (LevelParameters[]) out.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return levels;
    }

    class LevelParameters  implements Serializable {
        public int nRows;

        private static final long serialVersionUID = 1L;

        public int number;
        public int scores;
        public  int nColumns;
        public  RowsParameters[] rowsParameters;

        public LevelParameters(int number, int nColumns, RowsParameters[] rowsParameters) {
            this.number = number;
            this.nColumns = nColumns;
            this.rowsParameters = rowsParameters;
            this.nRows = rowsParameters.length;
            this.scores = 0;
        }
    }

    class RowsParameters  implements Serializable {

        private static final long serialVersionUID = 1L;

        public TileParameters[] tileParameters;
        public int rowIndex;
        public Util.TypeOfRow type;
        public MovingObjectParameters[] movingObjectParameters;

        public RowsParameters(TileParameters[] tileParameters, Util.TypeOfRow type, MovingObjectParameters[] movingObjectParameters) {
            this.tileParameters = tileParameters;
            this.type = type;
            this.movingObjectParameters = movingObjectParameters;
        }
    }

    class TileParameters  implements Serializable {

        private static final long serialVersionUID = 1L;

        public boolean transparent;
        public int row;
        public int column;
        public Util.TypeOfTile type;
        public boolean isScore;

        public TileParameters(int row, int column) {
            this.row = row;
            this.column = column;
            transparent = true;
            isScore = false;
        }
    }

    class MovingObjectParameters  implements Serializable {

        private static final long serialVersionUID = 1L;

        public long deltaTime;
        public boolean isFading;
        public float speed;
        public int length;
        public Util.Direction direction;
        public boolean safe;
        public TileParameters startingTile;

        public MovingObjectParameters(TileParameters startingTile, float speed, int length, Util.Direction direction) {
            this.startingTile = startingTile;
            this.speed = speed;
            this.length = length;
            this.direction = direction;
            this.isFading = false;
            this.deltaTime = 0;
        }

        public MovingObjectParameters(TileParameters startingTile, float speed, int length, Util.Direction direction, boolean isFading, long deltaTime) {
            this.startingTile = startingTile;
            this.speed = speed;
            this.length = length;
            this.direction = direction;
            this.isFading =isFading;
            this.deltaTime = deltaTime;
        }

    }


}

