package com.frogger.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.frogger.game.Util.TypeOfRow;
import com.frogger.game.objects.*;
import com.frogger.game.screens.FroggerGameScreen;

import java.io.Serializable;

public class Row  implements Serializable {

    private int rowIndex;
    private TypeOfRow type;
    private MovingObject[] movingObjects = new MovingObject[0];


    protected Row(Tile[] tiles, TypeOfRow typeOfRow, MovingObject[] movingObjects){
        type = typeOfRow;
        this.movingObjects = movingObjects;

        if (typeOfRow == TypeOfRow.LOG) {
            for (Tile tile: tiles) {
                tile.setWater(true);
            }
        }
    }

    public void render(SpriteBatch batch) {

        if (movingObjects != null) {
            for (MovingObject object : movingObjects) {
                object.render(batch);
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
