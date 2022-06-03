package com.frogger.game;

import com.badlogic.gdx.graphics.Texture;

public class Log extends MovingObject {

    Log(float size, float x, float y, float speed, int length, Texture texture, Util.Direction direction) {
        super(size, x, y, speed, length, texture, true, direction);
    }
}
