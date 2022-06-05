package com.frogger.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.frogger.game.Util.TypeOfRow;
import com.frogger.game.objects.MovingObject;

public class Row {

    public int rowIndex;
    public TypeOfRow type;
    public MovingObject[] objects;


    protected Row(int index, TypeOfRow typeOfRow, MovingObject[] movingObjects) {
        rowIndex = index;
        type = typeOfRow;
        objects = movingObjects;
    }

    public void render(SpriteBatch batch) {
        for (MovingObject object: objects) {
            object.render(batch);
        }
    }

    public TypeOfRow getType() {
        return type;
    }
}
