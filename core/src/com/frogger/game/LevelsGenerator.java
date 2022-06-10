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
        //It will be done only once
//        createLevels();

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
            }
        }

        float size = tiles[0][0].getSize();

        for (int i = 0; i < rows.length; i++) {
            RowsParameters rowsParameters = levelParameter.rowsParameters[i];
            MovingObject[] movingObjects = new MovingObject[rowsParameters.movingObjectParameters.length];


            for (int j = 0; j < movingObjects.length; j++) {
                MovingObjectParameters movingObjectParameters = rowsParameters.movingObjectParameters[j];
                if(rowsParameters.type == Util.TypeOfRow.LOG){
                    Tile startingTile = tiles[movingObjectParameters.startingTile.row][movingObjectParameters.startingTile.column];
                    if(movingObjectParameters.isFading) movingObjects[j] = new Log(size, startingTile.getX(), startingTile.getY(), movingObjectParameters.speed, movingObjectParameters.length, movingObjectParameters.direction, movingObjectParameters.isFading, movingObjectParameters.deltaTime);
                    else movingObjects[j] = new Log(size, startingTile.getX(), startingTile.getY(), movingObjectParameters.speed, movingObjectParameters.length, movingObjectParameters.direction);
                }else if(rowsParameters.type == Util.TypeOfRow.CAR){
                    Tile startingTile = tiles[movingObjectParameters.startingTile.row][movingObjectParameters.startingTile.column];
                    movingObjects[j] = new Car(size,  startingTile.getX(), startingTile.getY(), movingObjectParameters.speed, movingObjectParameters.length, movingObjectParameters.direction);
                }else if(rowsParameters.type == Util.TypeOfRow.TRAIN){
                    Tile startingTile = tiles[movingObjectParameters.startingTile.row][movingObjectParameters.startingTile.column];
                    movingObjects[j] = new Train(size,  startingTile.getX(), startingTile.getY());
                }else {
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

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        int nColumns = 15;
        int nRows = 30;

        RowsParameters[] rows = new RowsParameters[nRows];
        TileParameters[][] tiles = new TileParameters[nRows][nColumns];

        for (int row = 0; row < nRows; row++) {
            for (int column = 0; column < nColumns; column++) {
                tiles[row][column] = new TileParameters(row, column);
            }
        }

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

        TileParameters[] nontransparentTiles = new TileParameters[] {tiles[5][1], tiles[5][3], tiles[5][8], tiles[5][10], tiles[10][10], tiles[10][11], tiles[1][6]};
        for (TileParameters tile: nontransparentTiles) {
            tile.transparent = false;
        }

        for (int i = 0; i < 10; i++) {
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

        private static final long serialVersionUID = 1L;

        public int number;
        public int scores;
        public  int nColumns;
        public int nRows;
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

        public TileParameters(int row, int column) {
            this.row = row;
            this.column = column;
            transparent = true;
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

