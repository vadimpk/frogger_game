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
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.addAll;

public class LevelsGenerator{

    private static Level[] levels;
    private static LevelParameters[] levelParameters;
    private static LevelParameters bigLevelParameters;
    private static Level bigLevel;


    public static void updateLevel(int levelIndex, int bestScore, int starScore) {
        Level level = levels[levelIndex];
        boolean isChanging = false;
        if(level.getBestScore() < bestScore) {
            level.setBestScore(bestScore);
            isChanging = true;
        }
        if(level.getStarScore() < starScore) {
            level.setStarScore(starScore);
            isChanging = true;
        }
        if(isChanging) {
            levelParameters[levelIndex].starScore = starScore;
            levelParameters[levelIndex].bestScore = bestScore;
            loadToFile(levelParameters, "levels.txt");
        }
    }

    public static Level[] getLevels() {
        createLevels();
        if(levels == null) {
            levelParameters = loadFromFile("levels.txt");
            levels = new Level[10];

            for (int i = 0; i < levels.length; i++) {
                levels[i] = convertToLevel(levelParameters[i]);
            }
        }
        return levels;
    }

    public static Level getBigLevel() {
        try {
            createBigLevel();
            if(bigLevel == null) {
                bigLevelParameters = loadFromFile("big-level.txt")[0];
                bigLevel = convertToLevel(bigLevelParameters);
                bigLevel.setBig(true);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return bigLevel;
    }

    private static void createBigLevel() {
        LevelParameters BigLevelParameters;

        int nColumns = 15;
        int nRows = 280;

        RowsParameters[]  rows = new RowsParameters[nRows];
        TileParameters[][] tiles = new TileParameters[nRows][nColumns];
        List<TileParameters> nontransparentTiles = new ArrayList<>();

        for (int row = 0; row < nRows; row++) {
            for (int column = 0; column < nColumns; column++) {
                tiles[row][column] = new TileParameters(row, column);
            }
        }

        for (int i = 0; i < rows.length; i++) {
            rows[i] = new RowsParameters(tiles[i], Util.TypeOfRow.STATIC, new MovingObjectParameters[]{});
        }

        int startingX = 0;

        // Level 1
        nRows = 11 ;

        tiles[2][5].isScore = true;
        tiles[4][0].isScore = true;
        tiles[7][10].isScore = true;

        // create moving rows
        rows[3] = new RowsParameters(tiles[3], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[3][0], 20f, 3,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[3][7], 20f, 3, Util.Direction.LEFT),
        });

        rows[7] = new RowsParameters(tiles[7], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[7][2], 18f, 3, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[7][9], 18f, 3, Util.Direction.RIGHT, true, 200000000),
        });


        addAll(nontransparentTiles, tiles[1][5], tiles[2][3], tiles[2][7], tiles[4][1], tiles[4][10], tiles[6][3], tiles[6][5], tiles[6][7]);

        //Level 2

        nRows = 12;
        startingX =+ 11;

        tiles[2 + startingX][2].isScore = true;
        tiles[4 + startingX][0].isScore = true;
        tiles[8 + startingX][10].isScore = true;


        // create moving rows
        rows[2 + startingX] = new RowsParameters(tiles[2 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[2 + startingX][0], 18f, 3,Util.Direction.RIGHT, true, 180000000),
                new MovingObjectParameters(tiles[2 + startingX][7], 18f, 3, Util.Direction.RIGHT),
        });

        rows[3 + startingX] = new RowsParameters(tiles[3 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[3 + startingX][2], 18f, 3, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[3 + startingX][9], 18f, 3, Util.Direction.LEFT, true, 180000000),
        });

        rows[8 + startingX] = new RowsParameters(tiles[8 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[8 + startingX][5], 17f, 2, Util.Direction.RIGHT),
        });

        rows[9 + startingX] = new RowsParameters(tiles[9 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[9 + startingX][0], 17f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[9 + startingX][7], 17f, 2, Util.Direction.LEFT),
        });


        addAll(nontransparentTiles, tiles[startingX][8], tiles[1 + startingX][3], tiles[2 + startingX][7], tiles[4 + startingX][1], tiles[5 + startingX][8], tiles[6 + startingX][4], tiles[10 + startingX][8]);



        // Level 3

        nRows = 13;
        startingX += 13;

        tiles[11 + startingX][6].isScore = true;
        tiles[7+ startingX][9].isScore = true;
        tiles[4 + startingX][3].isScore = true;


        // create moving rows
        rows[3 + startingX] = new RowsParameters(tiles[3 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[3 + startingX][1], 18f, 3,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[3 + startingX][9], 18f, 3, Util.Direction.RIGHT),
        });

        rows[4 + startingX] = new RowsParameters(tiles[4 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[4 + startingX][2], 20f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[4 + startingX][7], 20f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[4 + startingX][12], 20f, 2, Util.Direction.LEFT, true, 180000000),
        });

        rows[5 + startingX] = new RowsParameters(tiles[5 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[5 + startingX][2], 16f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[5 + startingX][10], 16f, 2, Util.Direction.RIGHT, true, 160000000),
        });


        rows[8 + startingX] = new RowsParameters(tiles[8 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[8 + startingX][0], 18f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[8 + startingX][5], 18f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[8 + startingX][10], 18f, 2, Util.Direction.LEFT),
        });

        rows[9 + startingX] = new RowsParameters(tiles[9 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[9 + startingX][0], 17f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[9 + startingX][7], 17f, 2, Util.Direction.RIGHT),
        });

        rows[11 + startingX] = new RowsParameters(tiles[11 + startingX], Util.TypeOfRow.TRAIN, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[11 + startingX][0], 0, 0, null),
        });


        addAll(nontransparentTiles, tiles[2 + startingX][0], tiles[2 + startingX][2], tiles[2 + startingX][4], tiles[2 + startingX][6], tiles[2 + startingX][8], tiles[2 + startingX][10], tiles[2 + startingX][12],
                tiles[6 + startingX][3], tiles[6 + startingX][6], tiles[6 + startingX][9],
                tiles[7 + startingX][0], tiles[7 + startingX][4], tiles[7 + startingX][8], tiles[7 + startingX][12]);

        //Level 4


        nRows = 16;
        startingX += 14;

        tiles[1 + startingX][10].isScore = true;
        tiles[5 + startingX][7].isScore = true;
        tiles[9 + startingX][2].isScore = true;


        // create moving rows
        rows[1 + startingX] = new RowsParameters(tiles[1], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[1 + startingX][1], 16f, 3,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[1 + startingX][9], 16f, 3, Util.Direction.RIGHT),
        });
        rows[2 + startingX] = new RowsParameters(tiles[2], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[2 + startingX][3], 16.5f, 3,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[2 + startingX][12], 16.5f, 3, Util.Direction.LEFT),
        });

        rows[4 + startingX] = new RowsParameters(tiles[4], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[4 + startingX][2], 16f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[4 + startingX][7], 16f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[4 + startingX][12], 16f, 2, Util.Direction.LEFT, true, 180000000),
        });

        rows[5 + startingX] = new RowsParameters(tiles[5 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[5 + startingX][2], 15f, 2, Util.Direction.RIGHT, true, 150000000),
                new MovingObjectParameters(tiles[5 + startingX][10], 15f, 2, Util.Direction.RIGHT),
        });

        rows[6 + startingX] = new RowsParameters(tiles[6 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[6 + startingX][0], 14f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[6 + startingX][8], 14f, 2, Util.Direction.LEFT, true, 180000000),
        });

        rows[9 + startingX] = new RowsParameters(tiles[9 + startingX], Util.TypeOfRow.TRAIN, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[9 + startingX][0], 0, 0, null),
        });


        rows[11 + startingX] = new RowsParameters(tiles[11 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[11 + startingX][0], 18f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[11 + startingX][7], 18f, 2, Util.Direction.LEFT),
        });

        rows[12 + startingX] = new RowsParameters(tiles[12 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[12 + startingX][1], 17f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[12 + startingX][8], 17f, 2, Util.Direction.LEFT),
        });

        rows[13 + startingX] = new RowsParameters(tiles[13 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[13 + startingX][3], 16f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[13 + startingX][12], 16f, 2, Util.Direction.RIGHT),
        });


        addAll(nontransparentTiles, tiles[14 + startingX][0], tiles[14 + startingX][2], tiles[14 + startingX][4], tiles[14 + startingX][6], tiles[14 + startingX][8], tiles[14 + startingX][10], tiles[14 + startingX][12],
                tiles[3 + startingX][3], tiles[3 + startingX][6], tiles[3 + startingX][9],
                tiles[7 + startingX][0], tiles[7 + startingX][3], tiles[7 + startingX][6], tiles[7 + startingX][9],tiles[7 + startingX][12],
                tiles[8 + startingX][1], tiles[8 + startingX][4], tiles[8 + startingX][7], tiles[8 + startingX][10],
                tiles[10 + startingX][2], tiles[10 + startingX][5], tiles[10 + startingX][8], tiles[10 + startingX][11]);

        // Level 5

        nRows = 20;
        startingX += 19;

        tiles[4 + startingX][12].isScore = true;
        tiles[8 + startingX][7].isScore = true;
        tiles[16 + startingX][0].isScore = true;


        // create moving rows
        rows[1 + startingX] = new RowsParameters(tiles[1 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[1 + startingX][1], 14f, 2,Util.Direction.RIGHT, true, 120000000),
                new MovingObjectParameters(tiles[1 + startingX][10], 14f, 2, Util.Direction.RIGHT, true, 130000000),
        });


        rows[3 + startingX] = new RowsParameters(tiles[3 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[3 + startingX][0], 17f, 2,Util.Direction.LEFT, true, 150000000),
                new MovingObjectParameters(tiles[3 + startingX][5], 17f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[3 + startingX][10], 17f, 2, Util.Direction.LEFT),
        });

        rows[4 + startingX] = new RowsParameters(tiles[4 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[4 + startingX][1], 16f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[4 + startingX][8], 16f, 2, Util.Direction.RIGHT, true, 150000000),
                new MovingObjectParameters(tiles[4 + startingX][14], 16f, 2, Util.Direction.RIGHT),
        });

        rows[5 + startingX] = new RowsParameters(tiles[5 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[5 + startingX][2], 17, 1, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[5 + startingX][5], 17f, 1, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[5 + startingX][8], 17f, 1, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[5 + startingX][11], 17f, 1, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[5 + startingX][14], 17f, 1, Util.Direction.LEFT, true, 150000000),
        });

        rows[6 + startingX] = new RowsParameters(tiles[6 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[6 + startingX][1], 16.5f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[6 + startingX][4], 16.5f, 1, Util.Direction.RIGHT, true, 150000000),
                new MovingObjectParameters(tiles[6 + startingX][7], 16.5f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[6 + startingX][10], 16.5f, 1, Util.Direction.RIGHT, true, 150000000),
                new MovingObjectParameters(tiles[6 + startingX][13], 16.5f, 1, Util.Direction.RIGHT),
        });

        rows[8 + startingX] = new RowsParameters(tiles[8 + startingX], Util.TypeOfRow.TRAIN, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[8 + startingX][0], 0, 0, null),
        });


        rows[10 + startingX] = new RowsParameters(tiles[10 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[10 + startingX][0], 18f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[10 + startingX][7], 18f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[10 + startingX][14], 18f, 2, Util.Direction.LEFT),
        });

        rows[11 + startingX] = new RowsParameters(tiles[11 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[11 + startingX][1], 17f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[11 + startingX][8], 17f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[11 + startingX][14], 17f, 2, Util.Direction.RIGHT),
        });

        rows[12 + startingX] = new RowsParameters(tiles[12 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[12 + startingX][3], 16f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[12 + startingX][10], 16f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[12 + startingX][14], 16f, 2, Util.Direction.LEFT),
        });


        rows[14 + startingX] = new RowsParameters(tiles[14 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[14 + startingX][0], 17f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[14 + startingX][7], 17f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[14 + startingX][14], 17f, 2, Util.Direction.RIGHT),
        });

        rows[15 + startingX] = new RowsParameters(tiles[15 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[15 + startingX][1], 16f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[15 + startingX][8], 16f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[15 + startingX][14], 16f, 2, Util.Direction.LEFT, true, 160000000),
        });

        rows[18 + startingX] = new RowsParameters(tiles[8 + startingX], Util.TypeOfRow.TRAIN, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[8 + startingX][0], 0, 0, null),
        });

        addAll(nontransparentTiles, tiles[2 + startingX][1], tiles[2 + startingX][5], tiles[2 + startingX][9], tiles[2 + startingX][14],
                tiles[7 + startingX][2], tiles[7 + startingX][8], tiles[9 + startingX][0], tiles[13 + startingX][1], tiles[13 + startingX][5],
                tiles[16 + startingX][1], tiles[16 + startingX][3], tiles[17 + startingX][4], tiles[17 + startingX][6], tiles[16 + startingX][7], tiles[17 + startingX][8],
                tiles[17 + startingX][10], tiles[16 + startingX][11], tiles[16 + startingX][13], tiles[16 + startingX][14]);


        // Level 6

        nRows = 23;
        startingX += 21;

        tiles[startingX][3].isScore = true;
        tiles[10 + startingX][7].isScore = true;
        tiles[16 + startingX][2].isScore = true;


        // create moving rows
        rows[1 + startingX] = new RowsParameters(tiles[1 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[1 + startingX][1], 15f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[1 + startingX][6], 15f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[1 + startingX][11], 15f, 2, Util.Direction.LEFT),
        });

        rows[2 + startingX] = new RowsParameters(tiles[2 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[2 + startingX][2], 15f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[2 + startingX][7], 15f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[2 + startingX][12], 15f, 2, Util.Direction.LEFT),
        });

        rows[3 + startingX] = new RowsParameters(tiles[3 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[3 + startingX][3], 15.5f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[3 + startingX][8], 15.5f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[3 + startingX][13], 15.5f, 2, Util.Direction.RIGHT),
        });

        rows[4 + startingX] = new RowsParameters(tiles[4 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[4 + startingX][2], 14f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[4 + startingX][7], 14f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[4 + startingX][12], 14f, 2, Util.Direction.RIGHT),
        });

        rows[8 + startingX] = new RowsParameters(tiles[8 + startingX], Util.TypeOfRow.TRAIN, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[8 + startingX][0], 0, 0, null),
        });

        rows[9 + startingX] = new RowsParameters(tiles[9 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[9 + startingX][1], 15f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[9 + startingX][6], 15f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[9 + startingX][11], 15f, 2, Util.Direction.RIGHT),
        });

        rows[10 + startingX] = new RowsParameters(tiles[10 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[10 + startingX][2], 15.5f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[10 + startingX][7], 15.5f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[10 + startingX][12], 15.5f, 2, Util.Direction.LEFT),
        });

        rows[11 + startingX] = new RowsParameters(tiles[11 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[11 + startingX][2], 15f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[11 + startingX][7], 15f, 2, Util.Direction.LEFT, true, 150000000),
                new MovingObjectParameters(tiles[11 + startingX][12], 15f, 2, Util.Direction.LEFT),
        });

        rows[12 + startingX] = new RowsParameters(tiles[12 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[12 + startingX][1], 14f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[12 + startingX][6], 14f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[12 + startingX][11], 14f, 2, Util.Direction.RIGHT),
        });


        rows[15 + startingX] = new RowsParameters(tiles[15 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[15 + startingX][1], 16.5f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[15 + startingX][5], 16.5f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[15 + startingX][9], 16.5f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[15 + startingX][13], 16.5f, 2, Util.Direction.RIGHT),
        });

        rows[16 + startingX] = new RowsParameters(tiles[16 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[16 + startingX][2], 16f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[16 + startingX][8], 16f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[16 + startingX][11], 16f, 2, Util.Direction.LEFT),
        });

        rows[17 + startingX] = new RowsParameters(tiles[17 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[17 + startingX][1], 13f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[17 + startingX][10], 13f, 2, Util.Direction.RIGHT),
        });

        rows[20 + startingX] = new RowsParameters(tiles[20 + startingX], Util.TypeOfRow.TRAIN, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[20 + startingX][0], 0, 0, null),
        });

        rows[21 + startingX] = new RowsParameters(tiles[21 + startingX], Util.TypeOfRow.TRAIN, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[21 + startingX][0], 0, 0, null),
        });



        addAll(nontransparentTiles, tiles[startingX][4], tiles[5 + startingX][3], tiles[5 + startingX][10], tiles[6 + startingX][7],
                tiles[7 + startingX][1], tiles[7 + startingX][12], tiles[13 + startingX][2], tiles[13 + startingX][11], tiles[13 + startingX][14],
                tiles[18 + startingX][1], tiles[18 + startingX][7], tiles[18 + startingX][12], tiles[19 + startingX][10], tiles[20 + startingX][5], tiles[21 + startingX][2]);


        // Level 7

        nRows = 23;
        startingX += 24;

        tiles[10 + startingX][14].isScore = true;
        tiles[16 + startingX][11].isScore = true;
        tiles[19 + startingX][2].isScore = true;


        // create moving rows
        rows[1 + startingX] = new RowsParameters(tiles[1 + startingX], Util.TypeOfRow.LILY, new int[]{2, 12});

        rows[2 + startingX] = new RowsParameters(tiles[2 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[2 + startingX][4], 13f, 2, Util.Direction.LEFT, true, 150_000_000),
                new MovingObjectParameters(tiles[2 + startingX][12], 13f, 2, Util.Direction.LEFT),
        });

        rows[3 + startingX] = new RowsParameters(tiles[3 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[3 + startingX][1], 11f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[3 + startingX][9], 11f, 2, Util.Direction.RIGHT, true, 160_000_000),
        });

        rows[4 + startingX] = new RowsParameters(tiles[4 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[4 + startingX][2], 11f, 2,Util.Direction.LEFT, true, 150_000_000),
                new MovingObjectParameters(tiles[4 + startingX][10], 11f, 2, Util.Direction.LEFT),
        });

        rows[5 + startingX] = new RowsParameters(tiles[5 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[5 + startingX][1], 12.5f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[5 + startingX][5], 12.5f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[5 + startingX][9], 12.5f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[5 + startingX][13], 12.5f, 2, Util.Direction.LEFT),
        });

        rows[6 + startingX] = new RowsParameters(tiles[6 + startingX], Util.TypeOfRow.LILY, new int[]{4, 5, 14});

        rows[7 + startingX] = new RowsParameters(tiles[7 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[7 + startingX][1], 13f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[7 + startingX][6], 13f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[7 + startingX][11], 13f, 2, Util.Direction.LEFT),
        });

        rows[8 + startingX] = new RowsParameters(tiles[8 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[8 + startingX][2], 13.5f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[8 + startingX][7], 13.5f, 2, Util.Direction.LEFT),
        });

        rows[9 + startingX] = new RowsParameters(tiles[9 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[9 + startingX][1], 14f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[9 + startingX][6], 14f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[9 + startingX][10], 14f, 2, Util.Direction.RIGHT),
        });


        rows[12 + startingX] = new RowsParameters(tiles[12 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[12 + startingX][1], 12f, 1,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[12 + startingX][4], 12f, 1, Util.Direction.RIGHT, true, 160_000_000),
                new MovingObjectParameters(tiles[12 + startingX][7], 12f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[12 + startingX][10], 12f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[12 + startingX][13], 12f, 1, Util.Direction.RIGHT, true, 130_000_000),
        });


        rows[13 + startingX] = new RowsParameters(tiles[13 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[13 + startingX][2], 15f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[13 + startingX][8], 15f, 2, Util.Direction.LEFT, true, 140_000_000),
                new MovingObjectParameters(tiles[13 + startingX][13], 15f, 2, Util.Direction.LEFT),
        });

        rows[14 + startingX] = new RowsParameters(tiles[14 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[14 + startingX][2], 11f, 1,Util.Direction.RIGHT, true, 120_000_000),
                new MovingObjectParameters(tiles[14 + startingX][6], 11f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[14 + startingX][10], 11f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[14 + startingX][14], 11f, 1, Util.Direction.RIGHT),
        });

        rows[15 + startingX] = new RowsParameters(tiles[15 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[15 + startingX][1], 13.5f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[15 + startingX][8], 13.5f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[15 + startingX][13], 13.5f, 2, Util.Direction.RIGHT),
        });

        rows[16 + startingX] = new RowsParameters(tiles[16 + startingX], Util.TypeOfRow.LILY, new int[]{11});

        rows[17 + startingX] = new RowsParameters(tiles[17 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[17 + startingX][2], 10f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[17 + startingX][9], 10f, 2, Util.Direction.RIGHT),
        });

        rows[18 + startingX] = new RowsParameters(tiles[18 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[18 + startingX][6], 13f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[18 + startingX][10], 13f, 2, Util.Direction.RIGHT),
        });

        rows[19 + startingX] = new RowsParameters(tiles[19 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[19 + startingX][0], 12.5f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[19 + startingX][9], 12.5f, 2, Util.Direction.LEFT),
        });

        rows[20 + startingX] = new RowsParameters(tiles[20 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[20 + startingX][7], 10f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[20 + startingX][13], 10f, 2, Util.Direction.LEFT),
        });


        addAll(nontransparentTiles, tiles[10 + startingX][1], tiles[10 + startingX][7], tiles[10 + startingX][13],
                tiles[11 + startingX][10], tiles[11 + startingX][14], tiles[22 + startingX][13], tiles[21 + startingX][3], tiles[21 + startingX][9]);

        // Level 8

        nRows = 31;
        startingX += 24;


        tiles[3 + startingX][4].isScore = true;
        tiles[14 + startingX][14].isScore = true;
        tiles[27 + startingX][2].isScore = true;

        // create moving rows
        rows[2 + startingX] = new RowsParameters(tiles[2 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[2 + startingX][4], 11f, 2, Util.Direction.RIGHT, true, 150_000_000),
                new MovingObjectParameters(tiles[2 + startingX][12], 11f, 2, Util.Direction.RIGHT),
        });

        rows[3 + startingX] = new RowsParameters(tiles[3 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[3 + startingX][1], 12f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[3 + startingX][7], 12f, 2, Util.Direction.LEFT, true, 170_000_000),
                new MovingObjectParameters(tiles[3 + startingX][12], 12f, 2, Util.Direction.LEFT, true, 130_000_000),
        });

        rows[4 + startingX] = new RowsParameters(tiles[4 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[4 + startingX][1], 10f, 1,Util.Direction.RIGHT, true, 150_000_000),
                new MovingObjectParameters(tiles[4 + startingX][4], 10f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[4 + startingX][7], 10f, 1, Util.Direction.RIGHT, true, 120_000_000),
                new MovingObjectParameters(tiles[4 + startingX][10], 10f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[4 + startingX][13], 10f, 1, Util.Direction.RIGHT),
        });

        rows[5 + startingX] = new RowsParameters(tiles[4 + startingX], Util.TypeOfRow.LILY, new int[]{11});

        rows[10 + startingX] = new RowsParameters(tiles[4 + startingX], Util.TypeOfRow.LILY, new int[]{1});

        rows[11 + startingX] = new RowsParameters(tiles[11 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[11 + startingX][1], 11f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[11 + startingX][6], 11f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[11 + startingX][11], 11f, 2, Util.Direction.LEFT),
        });

        rows[12 + startingX] = new RowsParameters(tiles[12 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[12 + startingX][2], 10f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[12 + startingX][7], 10f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[12 + startingX][12], 10f, 2, Util.Direction.LEFT),
        });

        rows[14 + startingX] = new RowsParameters(tiles[14 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[14 + startingX][0], 11f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[14 + startingX][5], 11f, 2,Util.Direction.RIGHT),
        });

        rows[15 + startingX] = new RowsParameters(tiles[15 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[15 + startingX][2], 9f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[15 + startingX][9], 9f, 2, Util.Direction.RIGHT),
        });

        rows[19 + startingX] = new RowsParameters(tiles[19 + startingX], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[19 + startingX][0], 0, 0, null)
        });
        rows[20 + startingX] = new RowsParameters(tiles[20 + startingX], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[20 + startingX][0], 0, 0, null)
        });
        rows[21 + startingX] = new RowsParameters(tiles[21 + startingX], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[21 + startingX][0], 0, 0, null)
        });

        rows[22 + startingX] = new RowsParameters(tiles[22 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[22 + startingX][1], 10f, 1,Util.Direction.RIGHT, true, 150_000_000),
                new MovingObjectParameters(tiles[22 + startingX][4], 10f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[22 + startingX][7], 10f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[22 + startingX][10], 10f, 1, Util.Direction.RIGHT, true, 120_000_000),
                new MovingObjectParameters(tiles[22 + startingX][13], 10f, 1, Util.Direction.RIGHT),
        });

        rows[23 + startingX] = new RowsParameters(tiles[23 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[23 + startingX][2], 12f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[23 + startingX][7], 12f, 2, Util.Direction.LEFT, true, 180_000_000),
                new MovingObjectParameters(tiles[23 + startingX][12], 12f, 2, Util.Direction.LEFT),
        });

        rows[24 + startingX] = new RowsParameters(tiles[24 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[24 + startingX][1], 11f, 3,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[24 + startingX][7], 11f, 3,Util.Direction.LEFT, true, 110_000_000),
        });

        rows[26 + startingX] = new RowsParameters(tiles[26 + startingX], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[26 + startingX][0], 0, 0, null)
        });

        rows[27 + startingX] = new RowsParameters(tiles[27 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[27 + startingX][3], 10f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[27 + startingX][7], 10f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[27 + startingX][13], 10f, 2, Util.Direction.LEFT),
        });

        rows[28 + startingX] = new RowsParameters(tiles[28 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[28 + startingX][6], 9f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[28 + startingX][12], 9f, 2, Util.Direction.RIGHT),
        });



        addAll(nontransparentTiles, tiles[startingX][1], tiles[startingX][13], tiles[1 + startingX][2], tiles[1 + startingX][5],
                tiles[6 + startingX][1], tiles[9 + startingX][2], tiles[8 + startingX][3], tiles[7 + startingX][5], tiles[6 + startingX][6],
                tiles[8 + startingX][8], tiles[9 + startingX][9], tiles[6 + startingX][10], tiles[9 + startingX][12], tiles[7 + startingX][13],
                tiles[13 + startingX][5], tiles[13 + startingX][13], tiles[16 + startingX][2], tiles[20 + startingX][3], tiles[18 + startingX][7], tiles[17 + startingX][10], tiles[21 + startingX][12],
                tiles[25 + startingX][2], tiles[29][4], tiles[30 + startingX][13]);


        //Level 9

        nRows = 33;
        startingX += 32;

        tiles[10 + startingX][2].isScore = true;
        tiles[31 + startingX][12].isScore = true;
        tiles[26 + startingX][7].isScore = true;

        // create moving rows
        rows[3 + startingX] = new RowsParameters(tiles[3 + startingX], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[3 + startingX][0], 0, 0, null)
        });

        rows[4 + startingX] = new RowsParameters(tiles[4 + startingX], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[4 + startingX][0], 0, 0, null)
        });

        rows[5 + startingX] = new RowsParameters(tiles[5 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[5 + startingX][1], 12f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[5 + startingX][6], 12f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[5 + startingX][11], 12f, 2, Util.Direction.RIGHT),
        });

        rows[6 + startingX] = new RowsParameters(tiles[6 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[6 + startingX][2], 12f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[6 + startingX][7], 12f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[6 + startingX][12], 12f, 2, Util.Direction.LEFT),
        });

        rows[7 + startingX] = new RowsParameters(tiles[7 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[7 + startingX][4], 9f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[7 + startingX][9], 9f, 2, Util.Direction.RIGHT),
        });

        rows[8 + startingX] = new RowsParameters(tiles[8 + startingX], Util.TypeOfRow.LILY, new int[]{11});

        rows[9 + startingX] = new RowsParameters(tiles[9 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[9 + startingX][2], 11f, 2,Util.Direction.LEFT),
        });

        rows[10 + startingX] = new RowsParameters(tiles[10 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[10 + startingX][3], 10f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[10 + startingX][10], 10f, 2,Util.Direction.LEFT),
        });


        rows[11 + startingX] = new RowsParameters(tiles[11 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[11 + startingX][1], 10f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[11 + startingX][8], 10f, 2, Util.Direction.RIGHT),
        });

        rows[12 + startingX] =  new RowsParameters(tiles[12 + startingX], Util.TypeOfRow.LILY, new int[]{5});

        rows[14 + startingX] = new RowsParameters(tiles[14 + startingX], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[14 + startingX][0], 0, 0, null)
        });


        rows[17 + startingX] = new RowsParameters(tiles[17 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[17 + startingX][1], 12f, 1,Util.Direction.RIGHT, true, 150_000_000),
                new MovingObjectParameters(tiles[17 + startingX][6], 12f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[17 + startingX][11], 12f, 1, Util.Direction.RIGHT),
        });

        rows[18 + startingX] = new RowsParameters(tiles[18 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[18 + startingX][0], 11f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[18 + startingX][5], 11f, 2, Util.Direction.LEFT, true, 180_000_000),
                new MovingObjectParameters(tiles[18 + startingX][10], 11f, 2, Util.Direction.LEFT),
        });

        rows[19 + startingX] = new RowsParameters(tiles[19 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[19 + startingX][1], 10f, 3,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[19 + startingX][9], 10f, 3,Util.Direction.RIGHT, true, 110_000_000),
        });

        rows[20 + startingX] = new RowsParameters(tiles[20 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[20 + startingX][0], 11f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[20 + startingX][5], 11f, 2, Util.Direction.RIGHT, true, 180_000_000),
                new MovingObjectParameters(tiles[20 + startingX][10], 11f, 2, Util.Direction.RIGHT),
        });


        rows[22 + startingX] = new RowsParameters(tiles[22 + startingX], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[22 + startingX][0], 0, 0, null)
        });

        rows[28 + startingX] = new RowsParameters(tiles[28 + startingX], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[28 + startingX][0], 0, 0, null)
        });

        rows[29 + startingX] = new RowsParameters(tiles[29 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[29 + startingX][1], 12f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[29 + startingX][6], 12f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[29 + startingX][11], 12f, 2, Util.Direction.RIGHT),
        });

        rows[30 + startingX] = new RowsParameters(tiles[30 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[30 + startingX][0], 9f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[30 + startingX][5], 9f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[30 + startingX][10], 9f, 2, Util.Direction.LEFT),
        });

        rows[31 + startingX] = new RowsParameters(tiles[31 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[31 + startingX][2], 11f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[31 + startingX][7], 11f, 2,Util.Direction.RIGHT),
        });


        addAll(nontransparentTiles, tiles[1+ startingX][3], tiles[startingX][10], tiles[13 + startingX][1], tiles[13 + startingX][12],
                tiles[16 + startingX][2], tiles[15 + startingX][4], tiles[16 + startingX][13], tiles[21 + startingX][2], tiles[21 + startingX][14],
                tiles[23 + startingX][1], tiles[23 + startingX][5], tiles[24 + startingX][9], tiles[25 + startingX][2], tiles[26 + startingX][6],
                tiles[26 + startingX][11]);

        // Level 10


        nRows = 45;
        startingX += 34;

        tiles[11 + startingX][11].isScore = true;
        tiles[25 + startingX][4].isScore = true;
        tiles[41 + startingX][11].isScore = true;

        // create moving rows

        rows[startingX] = new RowsParameters(tiles[startingX], Util.TypeOfRow.LILY, new int[]{0,4,6,7,8,9});

        rows[1 + startingX] = new RowsParameters(tiles[1 + startingX], Util.TypeOfRow.LILY, new int[]{1,3, 7, 8,10,11});

        rows[2 + startingX]  = new RowsParameters(tiles[2 + startingX], Util.TypeOfRow.LILY, new int[]{7, 8, 9});

        rows[3 + startingX] = new RowsParameters(tiles[3 + startingX], Util.TypeOfRow.LILY, new int[]{8, 9, 10});

        rows[4 + startingX] = new RowsParameters(tiles[4 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[4 + startingX][2], 10f, 2, Util.Direction.RIGHT, true, 150_000_000),
                new MovingObjectParameters(tiles[4 + startingX][7], 10f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[4 + startingX][12], 10f, 2, Util.Direction.RIGHT),
        });

        rows[5 + startingX] = new RowsParameters(tiles[5 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[5 + startingX][3], 11f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[5 + startingX][8], 11f, 2, Util.Direction.LEFT, true, 170_000_000),
                new MovingObjectParameters(tiles[5 + startingX][13], 11f, 2, Util.Direction.LEFT, true, 130_000_000),
        });

        rows[6 + startingX] = new RowsParameters(tiles[6 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[6 + startingX][1], 12f, 3, Util.Direction.RIGHT, true, 150_000_000),
                new MovingObjectParameters(tiles[6 + startingX][9], 12f, 3, Util.Direction.RIGHT),
        });

        rows[9 + startingX] = new RowsParameters(tiles[9 + startingX], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[9 + startingX][0], 0, 0, null)
        });

        rows[10 + startingX] = new RowsParameters(tiles[10 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[10 + startingX][1], 10f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[10 + startingX][7], 10f, 2, Util.Direction.RIGHT),
        });

        rows[11 + startingX] = new RowsParameters(tiles[11 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[11 + startingX][8], 11f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[11 + startingX][13], 11f, 2, Util.Direction.RIGHT),
        });

        rows[12 + startingX] = new RowsParameters(tiles[12 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[12 + startingX][6], 12f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[12 + startingX][11], 12f, 2, Util.Direction.LEFT),
        });

        rows[13 + startingX] =  new RowsParameters(tiles[13 + startingX], Util.TypeOfRow.LILY, new int[]{3, 4, 5, 10});

        rows[14 + startingX]  =  new RowsParameters(tiles[14 + startingX], Util.TypeOfRow.LILY, new int[]{4, 5, 10, 13});

        rows[15 + startingX]  =  new RowsParameters(tiles[15 + startingX], Util.TypeOfRow.LILY, new int[]{1, 5, 6, 10, 11});

        rows[16 + startingX]  =  new RowsParameters(tiles[16 + startingX], Util.TypeOfRow.LILY, new int[]{6, 11});

        rows[17 + startingX] = new RowsParameters(tiles[17 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[17 + startingX][2], 12f, 1,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[17 + startingX][5], 12f, 1,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[17 + startingX][8], 12f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[17 + startingX][12], 12f, 1, Util.Direction.RIGHT),
        });

        rows[18 + startingX] = new RowsParameters(tiles[18 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[18 + startingX][1], 10f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[18 + startingX][6], 10f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[18 + startingX][11], 10f, 2, Util.Direction.LEFT),
        });

        rows[19 + startingX] = new RowsParameters(tiles[19 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[19 + startingX][1], 10f, 1,Util.Direction.RIGHT, true, 150_000_000),
                new MovingObjectParameters(tiles[19 + startingX][3], 10f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[19 + startingX][5], 10f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[19 + startingX][7], 10f, 1, Util.Direction.RIGHT, true, 120_000_000),
                new MovingObjectParameters(tiles[19 + startingX][9], 10f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[19 + startingX][11], 10f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[19 + startingX][13], 10f, 1, Util.Direction.RIGHT),
        });

        rows[20 + startingX] = new RowsParameters(tiles[20 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[20 + startingX][2], 12f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[20 + startingX][7], 12f, 2, Util.Direction.LEFT, true, 180_000_000),
                new MovingObjectParameters(tiles[20 + startingX][12], 12f, 2, Util.Direction.LEFT),
        });


        rows[23 + startingX] = new RowsParameters(tiles[23 + startingX], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[23 + startingX][0], 0, 0, null)
        });

        rows[24 + startingX] = new RowsParameters(tiles[24 + startingX], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[24 + startingX][0], 0, 0, null)
        });

        rows[26 + startingX] = new RowsParameters(tiles[26 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[26 + startingX][1], 11f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[26 + startingX][8], 11f, 2,Util.Direction.RIGHT),
        });

        rows[27 + startingX] = new RowsParameters(tiles[27 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[27 + startingX][4], 9f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[27 + startingX][12], 9f, 2, Util.Direction.RIGHT),
        });

        rows[28 + startingX] = new RowsParameters(tiles[28 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[28 + startingX][2], 10f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[28 + startingX][9], 10f, 2, Util.Direction.LEFT),
        });

        rows[29 + startingX]  =  new RowsParameters(tiles[29 + startingX], Util.TypeOfRow.LILY, new int[]{1, 4, 7});

        rows[30 + startingX]  =  new RowsParameters(tiles[30 + startingX], Util.TypeOfRow.LILY, new int[]{1, 2, 7, 11});

        rows[31 + startingX]  =  new RowsParameters(tiles[31 + startingX], Util.TypeOfRow.LILY, new int[]{2, 7, 8, 13});

        rows[32 + startingX] = new RowsParameters(tiles[32 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[32 + startingX][2], 11f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[32 + startingX][7], 11f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[32 + startingX][12], 11f, 2, Util.Direction.RIGHT, true, 150_000_000),
        });

        rows[33 + startingX] = new RowsParameters(tiles[33 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[33 + startingX][3], 11f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[33 + startingX][8], 11f, 2, Util.Direction.LEFT, true, 150_000_000),
                new MovingObjectParameters(tiles[33 + startingX][13], 11f, 2, Util.Direction.LEFT),
        });

        rows[34 + startingX] = new RowsParameters(tiles[34 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[34 + startingX][3], 11f, 1,Util.Direction.RIGHT, true, 150_000_000),
                new MovingObjectParameters(tiles[34 + startingX][9], 11f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[34 + startingX][14], 11f, 1, Util.Direction.RIGHT),
        });

        rows[35 + startingX] = new RowsParameters(tiles[35 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[35 + startingX][2], 9f, 3,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[35 + startingX][10], 9f, 3, Util.Direction.LEFT, true, 180_000_000),
        });

        rows[36 + startingX] = new RowsParameters(tiles[36 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[36 + startingX][4], 10f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[36 + startingX][11], 10f, 2, Util.Direction.RIGHT),
        });

        rows[37 + startingX] = new RowsParameters(tiles[37 + startingX], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[37 + startingX][2], 9f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[37 + startingX][2], 9f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[37 + startingX][11], 9f, 2, Util.Direction.LEFT, true, 180_000_000),
        });

        rows[38 + startingX] =   new RowsParameters(tiles[38 + startingX], Util.TypeOfRow.LILY, new int[]{5, 11});

        rows[39 + startingX] = new RowsParameters(tiles[39 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[39 + startingX][1], 8f, 2,Util.Direction.RIGHT),
        });

        rows[40 + startingX] = new RowsParameters(tiles[40 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[40 + startingX][5], 11f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[40 + startingX][1], 11f, 2, Util.Direction.LEFT),
        });

        rows[41 + startingX] = new RowsParameters(tiles[41 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[41 + startingX][1], 12f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[41 + startingX][6], 12f, 2, Util.Direction.RIGHT),
        });

        rows[42 + startingX] = new RowsParameters(tiles[42 + startingX], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[42 + startingX][3], 10f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[42 + startingX][13], 10f, 2, Util.Direction.LEFT),
        });

        rows[43 + startingX] =   new RowsParameters(tiles[43 + startingX], Util.TypeOfRow.LILY, new int[]{3, 7});

        addAll(nontransparentTiles, tiles[2 + startingX][7], tiles[7 + startingX][1], tiles[7 + startingX][13], tiles[8 + startingX][10], tiles[21 + startingX][1], tiles[21 + startingX][9],
                tiles[22 + startingX][7], tiles[25 + startingX][3]);


        for (TileParameters tile: nontransparentTiles) {
            tile.transparent = false;
        }

        System.out.println(startingX + 44);


        bigLevelParameters = new LevelParameters(0, 15, rows, 0, 0, 0);
        loadToFile(new LevelParameters[] {bigLevelParameters}, "big-level.txt");
    }

    private static Level convertToLevel(LevelParameters levelParameter) {
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

            if (rowsParameters.movingObjectParameters != null) {
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

            } else {
                int[] lilyIndexes = rowsParameters.lilyIndexes;
                rows[i] = new Row(tiles[i], rowsParameters.type, lilyIndexes, nColumns);
            }


        for (Tile tile: tiles[nRows-1])
            tile.setFinishTexture();
        }

        Map map = new Map(rows, tiles);
        return new Level(levelParameter.number, levelParameter.bestScore, levelParameter.starScore, map, levelParameter.time);
    }

    public static void createLevels(){
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
        
        levelParameters[0] = new LevelParameters(1, nColumns, rows, 0, 0, 15);
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

        levelParameters[1] = new LevelParameters(2, nColumns, rows, 0, 0, 15);


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

        levelParameters[2] = new LevelParameters(3, nColumns, rows, 0, 0, 15);

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

        levelParameters[3] = new LevelParameters(4, nColumns, rows, 0, 0, 25);


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
                new MovingObjectParameters(tiles[3][5], 17f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[3][10], 17f, 2, Util.Direction.LEFT),
        });

        rows[4] = new RowsParameters(tiles[4], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[4][1], 16f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[4][8], 16f, 2, Util.Direction.RIGHT, true, 150000000),
                new MovingObjectParameters(tiles[4][14], 16f, 2, Util.Direction.RIGHT),
        });

        rows[5] = new RowsParameters(tiles[5], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[5][2], 17, 1, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[5][5], 17f, 1, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[5][8], 17f, 1, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[5][11], 17f, 1, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[5][14], 17f, 1, Util.Direction.LEFT, true, 150000000),
        });

        rows[6] = new RowsParameters(tiles[6], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[6][1], 16.5f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[6][4], 16.5f, 1, Util.Direction.RIGHT, true, 150000000),
                new MovingObjectParameters(tiles[6][7], 16.5f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[6][10], 16.5f, 1, Util.Direction.RIGHT, true, 150000000),
                new MovingObjectParameters(tiles[6][13], 16.5f, 1, Util.Direction.RIGHT),
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
                new MovingObjectParameters(tiles[14][0], 17f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[14][7], 17f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[14][14], 17f, 2, Util.Direction.RIGHT),
        });

        rows[15] = new RowsParameters(tiles[15], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[15][1], 16f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[15][8], 16f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[15][14], 16f, 2, Util.Direction.LEFT, true, 160000000),
        });

        rows[18] = new RowsParameters(tiles[8], Util.TypeOfRow.TRAIN, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[8][0], 0, 0, null),
        });

        nontransparentTiles = new TileParameters[] {tiles[2][1], tiles[2][5], tiles[2][9], tiles[2][14],
                                                    tiles[7][2], tiles[7][8], tiles[9][0], tiles[13][1], tiles[13][5],
                                                    tiles[16][1], tiles[16][3], tiles[17][4], tiles[17][6], tiles[16][7], tiles[17][8],
                                                    tiles[17][10], tiles[16][11], tiles[16][13] , tiles[16][14]};
        for (TileParameters tile: nontransparentTiles) {
            tile.transparent = false;
        }

        levelParameters[4] = new LevelParameters(5, nColumns, rows, 0, 0, 28);


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

        levelParameters[5] = new LevelParameters(6, nColumns, rows, 0, 0, 28);




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
        rows[1] = new RowsParameters(tiles[1], Util.TypeOfRow.LILY, new int[]{2, 12});

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
                new MovingObjectParameters(tiles[5][1], 12.5f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[5][5], 12.5f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[5][9], 12.5f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[5][13], 12.5f, 2, Util.Direction.LEFT),
        });

        rows[6] = new RowsParameters(tiles[6], Util.TypeOfRow.LILY, new int[]{4, 5, 14});

        rows[7] = new RowsParameters(tiles[7], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[7][1], 13f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[7][6], 13f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[7][11], 13f, 2, Util.Direction.LEFT),
        });

        rows[8] = new RowsParameters(tiles[8], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[8][2], 13.5f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[8][7], 13.5f, 2, Util.Direction.LEFT),
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
                new MovingObjectParameters(tiles[15][1], 13.5f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[15][8], 13.5f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[15][13], 13.5f, 2, Util.Direction.RIGHT),
        });

        rows[16] = new RowsParameters(tiles[16], Util.TypeOfRow.LILY, new int[]{11});

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

        levelParameters[6] = new LevelParameters(7, nColumns, rows, 0, 0, 50);


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

        tiles[3][4].isScore = true;
        tiles[14][14].isScore = true;
        tiles[27][2].isScore = true;

        for (int i = 0; i < rows.length; i++) {
            rows[i] = new RowsParameters(tiles[i], Util.TypeOfRow.STATIC, new MovingObjectParameters[]{});
        }

        // create moving rows
        rows[2] = new RowsParameters(tiles[2], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[2][4], 11f, 2, Util.Direction.RIGHT, true, 150_000_000),
                new MovingObjectParameters(tiles[2][12], 11f, 2, Util.Direction.RIGHT),
        });

        rows[3] = new RowsParameters(tiles[3], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[3][1], 12f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[3][7], 12f, 2, Util.Direction.LEFT, true, 170_000_000),
                new MovingObjectParameters(tiles[3][12], 12f, 2, Util.Direction.LEFT, true, 130_000_000),
        });

        rows[4] = new RowsParameters(tiles[4], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[4][1], 10f, 1,Util.Direction.RIGHT, true, 150_000_000),
                new MovingObjectParameters(tiles[4][4], 10f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[4][7], 10f, 1, Util.Direction.RIGHT, true, 120_000_000),
                new MovingObjectParameters(tiles[4][10], 10f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[4][13], 10f, 1, Util.Direction.RIGHT),
        });

        rows[5] = new RowsParameters(tiles[4], Util.TypeOfRow.LILY, new int[]{11});

        rows[10] = new RowsParameters(tiles[4], Util.TypeOfRow.LILY, new int[]{1});

        rows[11] = new RowsParameters(tiles[11], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[11][1], 11f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[11][6], 11f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[11][11], 11f, 2, Util.Direction.LEFT),
        });

        rows[12] = new RowsParameters(tiles[12], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[12][2], 10f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[12][7], 10f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[12][12], 10f, 2, Util.Direction.LEFT),
        });

        rows[14] = new RowsParameters(tiles[14], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[14][0], 11f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[14][5], 11f, 2,Util.Direction.RIGHT),
        });

        rows[15] = new RowsParameters(tiles[15], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[15][2], 9f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[15][9], 9f, 2, Util.Direction.RIGHT),
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
                new MovingObjectParameters(tiles[22][1], 10f, 1,Util.Direction.RIGHT, true, 150_000_000),
                new MovingObjectParameters(tiles[22][4], 10f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[22][7], 10f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[22][10], 10f, 1, Util.Direction.RIGHT, true, 120_000_000),
                new MovingObjectParameters(tiles[22][13], 10f, 1, Util.Direction.RIGHT),
        });

        rows[23] = new RowsParameters(tiles[23], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[23][2], 12f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[23][7], 12f, 2, Util.Direction.LEFT, true, 180_000_000),
                new MovingObjectParameters(tiles[23][12], 12f, 2, Util.Direction.LEFT),
        });

        rows[24] = new RowsParameters(tiles[24], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[24][1], 11f, 3,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[24][7], 11f, 3,Util.Direction.LEFT, true, 110_000_000),
        });

        rows[26] = new RowsParameters(tiles[26], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[26][0], 0, 0, null)
        });

        rows[27] = new RowsParameters(tiles[27], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[27][3], 10f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[27][7], 10f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[27][13], 10f, 2, Util.Direction.LEFT),
        });

        rows[28] = new RowsParameters(tiles[28], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[28][6], 9f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[28][12], 9f, 2, Util.Direction.RIGHT),
        });



        nontransparentTiles = new TileParameters[] {tiles[0][1], tiles[0][13], tiles[1][2], tiles[1][5],
                tiles[6][1], tiles[9][2], tiles[8][3], tiles[7][5], tiles[6][6],
                tiles[8][8], tiles[9][9], tiles[6][10], tiles[9][12], tiles[7][13],
                tiles[13][5], tiles[13][13], tiles[16][2], tiles[20][3], tiles[18][7], tiles[17][10], tiles[21][12],
                tiles[25][2], tiles[29][4], tiles[30][13]};
        for (TileParameters tile: nontransparentTiles) {
            tile.transparent = false;
        }

        levelParameters[7] = new LevelParameters(8, nColumns, rows, 0, 0, 50);

        //Level 9

        nColumns = 15;
        nRows = 33;
        rows = new RowsParameters[nRows];
        tiles = new TileParameters[nRows][nColumns];

        for (int row = 0; row < nRows; row++) {
            for (int column = 0; column < nColumns; column++) {
                tiles[row][column] = new TileParameters(row, column);
            }
        }

        tiles[10][2].isScore = true;
        tiles[31][12].isScore = true;
        tiles[26][7].isScore = true;

        for (int i = 0; i < rows.length; i++) {
            rows[i] = new RowsParameters(tiles[i], Util.TypeOfRow.STATIC, new MovingObjectParameters[]{});
        }

        // create moving rows
        rows[3] = new RowsParameters(tiles[3], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[3][0], 0, 0, null)
        });

        rows[4] = new RowsParameters(tiles[4], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[4][0], 0, 0, null)
        });

        rows[5] = new RowsParameters(tiles[5], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[5][1], 12f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[5][6], 12f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[5][11], 12f, 2, Util.Direction.RIGHT),
        });

        rows[6] = new RowsParameters(tiles[6], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[6][2], 12f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[6][7], 12f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[6][12], 12f, 2, Util.Direction.LEFT),
        });

        rows[7] = new RowsParameters(tiles[7], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[7][4], 9f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[7][9], 9f, 2, Util.Direction.RIGHT),
        });

        rows[8] = new RowsParameters(tiles[8], Util.TypeOfRow.LILY, new int[]{11});

        rows[9] = new RowsParameters(tiles[9], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[9][2], 11f, 2,Util.Direction.LEFT),
        });

        rows[10] = new RowsParameters(tiles[10], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[10][3], 10f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[10][10], 10f, 2,Util.Direction.LEFT),
        });


        rows[11] = new RowsParameters(tiles[11], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[11][1], 10f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[11][8], 10f, 2, Util.Direction.RIGHT),
        });

        rows[12] =  new RowsParameters(tiles[12], Util.TypeOfRow.LILY, new int[]{5});

        rows[14] = new RowsParameters(tiles[14], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[14][0], 0, 0, null)
        });


        rows[17] = new RowsParameters(tiles[17], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[17][1], 12f, 1,Util.Direction.RIGHT, true, 150_000_000),
                new MovingObjectParameters(tiles[17][6], 12f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[17][11], 12f, 1, Util.Direction.RIGHT),
        });

        rows[18] = new RowsParameters(tiles[18], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[18][0], 11f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[18][5], 11f, 2, Util.Direction.LEFT, true, 180_000_000),
                new MovingObjectParameters(tiles[18][10], 11f, 2, Util.Direction.LEFT),
        });

        rows[19] = new RowsParameters(tiles[19], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[19][1], 10f, 3,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[19][9], 10f, 3,Util.Direction.RIGHT, true, 110_000_000),
        });

        rows[20] = new RowsParameters(tiles[20], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[20][0], 11f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[20][5], 11f, 2, Util.Direction.RIGHT, true, 180_000_000),
                new MovingObjectParameters(tiles[20][10], 11f, 2, Util.Direction.RIGHT),
        });


        rows[22] = new RowsParameters(tiles[22], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[22][0], 0, 0, null)
        });

        rows[28] = new RowsParameters(tiles[28], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[28][0], 0, 0, null)
        });

        rows[29] = new RowsParameters(tiles[29], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[29][1], 12f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[29][6], 12f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[29][11], 12f, 2, Util.Direction.RIGHT),
        });

        rows[30] = new RowsParameters(tiles[30], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[30][0], 9f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[30][5], 9f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[30][10], 9f, 2, Util.Direction.LEFT),
        });

        rows[31] = new RowsParameters(tiles[31], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[31][2], 11f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[31][7], 11f, 2,Util.Direction.RIGHT),
        });


        nontransparentTiles = new TileParameters[] {tiles[1][3], tiles[0][10], tiles[13][1], tiles[13][12],
                tiles[16][2], tiles[15][4], tiles[16][13], tiles[21][2], tiles[21][14],
                tiles[23][1], tiles[23][5], tiles[24][9], tiles[25][2], tiles[26][6],
                tiles[26][11]};
        for (TileParameters tile: nontransparentTiles) {
            tile.transparent = false;
        }

        levelParameters[8] = new LevelParameters(9, nColumns, rows, 0, 0, 30);

        // Level 10


        nColumns = 15;
        nRows = 45;
        rows = new RowsParameters[nRows];
        tiles = new TileParameters[nRows][nColumns];

        for (int row = 0; row < nRows; row++) {
            for (int column = 0; column < nColumns; column++) {
                tiles[row][column] = new TileParameters(row, column);
            }
        }

        tiles[11][11].isScore = true;
        tiles[25][4].isScore = true;
        tiles[41][11].isScore = true;

        for (int i = 0; i < rows.length; i++) {
            rows[i] = new RowsParameters(tiles[i], Util.TypeOfRow.STATIC, new MovingObjectParameters[]{});
        }



        // create moving rows

        rows[0] = new RowsParameters(tiles[0], Util.TypeOfRow.LILY, new int[]{0,4,6,7,8,9});

        rows[1] = new RowsParameters(tiles[1], Util.TypeOfRow.LILY, new int[]{1,3, 7, 8,10,11});

        rows[2]  = new RowsParameters(tiles[2], Util.TypeOfRow.LILY, new int[]{7, 8, 9});

        rows[3] = new RowsParameters(tiles[3], Util.TypeOfRow.LILY, new int[]{8, 9, 10});

        rows[4] = new RowsParameters(tiles[4], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[4][2], 10f, 2, Util.Direction.RIGHT, true, 150_000_000),
                new MovingObjectParameters(tiles[4][7], 10f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[4][12], 10f, 2, Util.Direction.RIGHT),
        });

        rows[5] = new RowsParameters(tiles[5], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[5][3], 11f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[5][8], 11f, 2, Util.Direction.LEFT, true, 170_000_000),
                new MovingObjectParameters(tiles[5][13], 11f, 2, Util.Direction.LEFT, true, 130_000_000),
        });

        rows[6] = new RowsParameters(tiles[6], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[6][1], 12f, 3, Util.Direction.RIGHT, true, 150_000_000),
                new MovingObjectParameters(tiles[6][9], 12f, 3, Util.Direction.RIGHT),
        });

        rows[9] = new RowsParameters(tiles[9], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[9][0], 0, 0, null)
        });

        rows[10] = new RowsParameters(tiles[10], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[10][1], 10f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[10][7], 10f, 2, Util.Direction.RIGHT),
        });

        rows[11] = new RowsParameters(tiles[11], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[11][8], 11f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[11][13], 11f, 2, Util.Direction.RIGHT),
        });

        rows[12] = new RowsParameters(tiles[12], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[12][6], 12f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[12][11], 12f, 2, Util.Direction.LEFT),
        });

        rows[13] =  new RowsParameters(tiles[13], Util.TypeOfRow.LILY, new int[]{3, 4, 5, 10});

        rows[14]  =  new RowsParameters(tiles[14], Util.TypeOfRow.LILY, new int[]{4, 5, 10, 13});

        rows[15]  =  new RowsParameters(tiles[15], Util.TypeOfRow.LILY, new int[]{1, 5, 6, 10, 11});

        rows[16]  =  new RowsParameters(tiles[16], Util.TypeOfRow.LILY, new int[]{6, 11});

        rows[17] = new RowsParameters(tiles[17], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[17][2], 12f, 1,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[17][5], 12f, 1,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[17][8], 12f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[17][12], 12f, 1, Util.Direction.RIGHT),
        });

        rows[18] = new RowsParameters(tiles[18], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[18][1], 10f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[18][6], 10f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[18][11], 10f, 2, Util.Direction.LEFT),
        });

        rows[19] = new RowsParameters(tiles[19], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[19][1], 10f, 1,Util.Direction.RIGHT, true, 150_000_000),
                new MovingObjectParameters(tiles[19][3], 10f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[19][5], 10f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[19][7], 10f, 1, Util.Direction.RIGHT, true, 120_000_000),
                new MovingObjectParameters(tiles[19][9], 10f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[19][11], 10f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[19][13], 10f, 1, Util.Direction.RIGHT),
        });

        rows[20] = new RowsParameters(tiles[20], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[20][2], 12f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[20][7], 12f, 2, Util.Direction.LEFT, true, 180_000_000),
                new MovingObjectParameters(tiles[20][12], 12f, 2, Util.Direction.LEFT),
        });


        rows[23] = new RowsParameters(tiles[23], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[23][0], 0, 0, null)
        });

        rows[24] = new RowsParameters(tiles[24], Util.TypeOfRow.TRAIN, new MovingObjectParameters[] {
                new MovingObjectParameters(tiles[24][0], 0, 0, null)
        });

        rows[26] = new RowsParameters(tiles[26], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[26][1], 11f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[26][8], 11f, 2,Util.Direction.RIGHT),
        });

        rows[27] = new RowsParameters(tiles[27], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[27][4], 9f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[27][12], 9f, 2, Util.Direction.RIGHT),
        });

        rows[28] = new RowsParameters(tiles[28], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[28][2], 10f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[28][9], 10f, 2, Util.Direction.LEFT),
        });

        rows[29]  =  new RowsParameters(tiles[29], Util.TypeOfRow.LILY, new int[]{1, 4, 7});

        rows[30]  =  new RowsParameters(tiles[30], Util.TypeOfRow.LILY, new int[]{1, 2, 7, 11});

        rows[31]  =  new RowsParameters(tiles[31], Util.TypeOfRow.LILY, new int[]{2, 7, 8, 13});

        rows[32] = new RowsParameters(tiles[32], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[32][2], 11f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[32][7], 11f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[32][12], 11f, 2, Util.Direction.RIGHT, true, 150_000_000),
        });

        rows[33] = new RowsParameters(tiles[33], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[33][3], 11f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[33][8], 11f, 2, Util.Direction.LEFT, true, 150_000_000),
                new MovingObjectParameters(tiles[33][13], 11f, 2, Util.Direction.LEFT),
        });

        rows[34] = new RowsParameters(tiles[34], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[34][3], 11f, 1,Util.Direction.RIGHT, true, 150_000_000),
                new MovingObjectParameters(tiles[34][9], 11f, 1, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[34][14], 11f, 1, Util.Direction.RIGHT),
        });

        rows[35] = new RowsParameters(tiles[35], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[35][2], 9f, 3,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[35][10], 9f, 3, Util.Direction.LEFT, true, 180_000_000),
        });

        rows[36] = new RowsParameters(tiles[36], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[36][4], 10f, 2,Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[36][11], 10f, 2, Util.Direction.RIGHT),
        });

        rows[37] = new RowsParameters(tiles[37], Util.TypeOfRow.LOG, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[37][2], 9f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[37][2], 9f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[37][11], 9f, 2, Util.Direction.LEFT, true, 180_000_000),
        });

        rows[38] =   new RowsParameters(tiles[38], Util.TypeOfRow.LILY, new int[]{5, 11});

        rows[39] = new RowsParameters(tiles[39], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[39][1], 8f, 2,Util.Direction.RIGHT),
        });

        rows[40] = new RowsParameters(tiles[40], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[40][5], 11f, 2,Util.Direction.LEFT),
                new MovingObjectParameters(tiles[40][1], 11f, 2, Util.Direction.LEFT),
        });

        rows[41] = new RowsParameters(tiles[41], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[41][1], 12f, 2, Util.Direction.RIGHT),
                new MovingObjectParameters(tiles[41][6], 12f, 2, Util.Direction.RIGHT),
        });

        rows[42] = new RowsParameters(tiles[42], Util.TypeOfRow.CAR, new MovingObjectParameters[]{
                new MovingObjectParameters(tiles[42][3], 10f, 2, Util.Direction.LEFT),
                new MovingObjectParameters(tiles[42][13], 10f, 2, Util.Direction.LEFT),
        });

        rows[43] =   new RowsParameters(tiles[43], Util.TypeOfRow.LILY, new int[]{3, 7});

        nontransparentTiles = new TileParameters[] {tiles[2][7], tiles[7][1], tiles[7][13], tiles[8][10], tiles[21][1], tiles[21][9],
        tiles[22][7], tiles[25][3]};
        for (TileParameters tile: nontransparentTiles) {
            tile.transparent = false;
        }

        levelParameters[9] = new LevelParameters(10, nColumns, rows, 0, 0, 45);

        loadToFile(levelParameters, "levels.txt");
    }

    public static void loadToFile(LevelParameters[] levels, String src) {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(Paths.get(src)))) {
            out.writeObject(levels);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static LevelParameters[] loadFromFile(String src) {
        LevelParameters[] levels;
        try(ObjectInputStream out = new ObjectInputStream(Files.newInputStream(Paths.get(src)))) {
            levels = (LevelParameters[]) out.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return levels;
    }

    static class LevelParameters implements Serializable {
        public int nRows;

        private static final long serialVersionUID = 1L;

        public int number;
        public int bestScore;
        public int starScore;
        public int nColumns;
        public int time;
        public RowsParameters[] rowsParameters;

        public LevelParameters(int number, int nColumns, RowsParameters[] rowsParameters, int bestScore, int starScore, int time) {
            this.number = number;
            this.nColumns = nColumns;
            this.rowsParameters = rowsParameters;
            this.nRows = rowsParameters.length;
            this.starScore = starScore;
            this.bestScore = bestScore;
            this.time = time;
        }
        
        
    }

    static class RowsParameters  implements Serializable {

        private static final long serialVersionUID = 1L;

        public TileParameters[] tileParameters;
        public int rowIndex;
        public Util.TypeOfRow type;
        public MovingObjectParameters[] movingObjectParameters;
        public int[] lilyIndexes;

        public RowsParameters(TileParameters[] tileParameters, Util.TypeOfRow type, MovingObjectParameters[] movingObjectParameters) {
            this.tileParameters = tileParameters;
            this.type = type;
            this.movingObjectParameters = movingObjectParameters;
        }

        public RowsParameters(TileParameters[] tileParameters, Util.TypeOfRow type, int[] lilyIndexes) {
            this.tileParameters = tileParameters;
            this.type = type;
            this.lilyIndexes = lilyIndexes;
        }
    }

    static class TileParameters implements Serializable {

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

    static class MovingObjectParameters  implements Serializable {

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

