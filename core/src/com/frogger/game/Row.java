package com.frogger.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.frogger.game.Util.TypeOfRow;
import com.frogger.game.objects.*;

public class Row  {

    private int rowIndex;
    private TypeOfRow type;
    private MovingObject[] movingObjects;


    protected Row(Tile[] tiles, TypeOfRow typeOfRow, MovingObject[] movingObjects){
        type = typeOfRow;
        this.movingObjects = movingObjects;

        if (typeOfRow == TypeOfRow.LOG) {
            for (Tile tile: tiles) {
                tile.setWaterTexture();
            }
        }
        else if (typeOfRow == TypeOfRow.CAR) {
            for (Tile tile: tiles) {
                tile.setRoadTexture();
            }
        }
        else if (typeOfRow == TypeOfRow.TRAIN) {
            for (Tile tile: tiles) {
                tile.setRailRoadTexture();
            }
        }
    }

    protected Row(Tile[] tiles, TypeOfRow typeOfRow, int[] lilyIndexes, int nColumns){
        type = typeOfRow;

        if (typeOfRow == TypeOfRow.LILY) {
            for (Tile tile: tiles) {
                tile.setWaterTexture();
            }
            for (int index: lilyIndexes) {
                if (index >= 0 && index < nColumns) {
                    tiles[index].setLily();
                    tiles[index].setRandomRotation();
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        if (movingObjects != null && movingObjects.length > 0) {
            for (MovingObject object : movingObjects) {
                if (object != null) object.render(batch);
            }
        }
    }

    public void pausedRender(SpriteBatch batch) {
        if (movingObjects != null && movingObjects.length > 0) {
            for (MovingObject object : movingObjects) {
                if (object != null) object.pausedRender(batch);
            }
        }
    }

    public TypeOfRow getType() {
        return type;
    }

    public MovingObject[] getMovingObjects() {
        return movingObjects;
    }
}
