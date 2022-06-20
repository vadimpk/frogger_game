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
        if (typeOfRow == TypeOfRow.CAR) {
            for (Tile tile: tiles) {
                tile.setRoadTexture();
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

    public TypeOfRow getType() {
        return type;
    }

    public MovingObject[] getMovingObjects() {
        return movingObjects;
    }
}
