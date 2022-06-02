package com.frogger.game;

import com.badlogic.gdx.graphics.Texture;

public class Logs extends MovableRow {

    public Logs(Tile startingTile, float speed, float phase, float amountOfMovingObj, float movingObjWidth, float movingObjHeight, Texture movingObjTexture, Texture bg) {
        super(startingTile, speed, phase, amountOfMovingObj, movingObjWidth, movingObjHeight, movingObjTexture, bg);
    }
}

