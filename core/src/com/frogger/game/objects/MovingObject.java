package com.frogger.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.frogger.game.Frog;
import com.frogger.game.FroggerGame;
import com.frogger.game.Map;
import com.frogger.game.Util.Direction;


public class MovingObject {

    private static final Texture DEFAULT_MOVING_OBJECT_TEXTURE = new Texture(Gdx.files.internal("tile2.png"));

    private float x,y,size;
    private float speed;
    private int length;
    private Direction direction;

    private boolean safe;

    MovingObject (float size, float x, float y, float speed, int length, Direction direction) {
        this.size = size;
        this.x = x;
        this.y = y;
        this.length = length;
        this.speed = speed;
        this.direction = direction;
    }

    public void render(SpriteBatch batch) {

    }

    public void move() {

        if (direction == Direction.LEFT) {

            if ((x + size*length) < Map.tiles[0][0].getX()) {
                x = Map.tiles[0][Map.nColumns -1].getX() + Map.tiles[0][0].getSize();
            }

            x -= size / speed;
        } else if (direction == Direction.RIGHT) {

            if (x > Map.tiles[0][Map.nColumns -1].getX() + Map.tiles[0][0].getSize()) {
                x = Map.tiles[0][0].getX() - Map.tiles[0][0].getSize() * length;
            }

            x += size / speed;
        }
    }

    public static void dispose(){
        DEFAULT_MOVING_OBJECT_TEXTURE.dispose();
    }

    public boolean checkCollision(Frog frog) {
        if ((frog.getX() + frog.getSize()) >= x && frog.getX() <= (x + size*length) &&
                 frog.getY() >= y && (frog.getY() + frog.getSize()) <= (y + size)) {
            return true;
        }
        return false;
    }

    public float getSpeed() {
        return speed;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setSafe(boolean safe) {
        this.safe = safe;
    }

    public boolean isSafe() {
        return safe;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getSize() {
        return size;
    }

    public int getLength() {
        return length;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

}