package com.frogger.game.gameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.frogger.game.utils.Util.Direction;
import com.frogger.game.screens.FroggerGameScreen;

/**
 * MovingObject.java
 * @author vadympolishchuk
 * Parent class for moving game objects (Log, Car and Train)
 * Stores default information of each object (coordinates, speed, length, direction, safe status) that every object has
 * Method move() implements moving object
 */

public class MovingObject {


    private float x,y,size;
    private final float speed;
    private final int length;
    private final Direction direction;

    private boolean safe;

    /**
     * Default constructor
     * @param size size of an object
     * @param x x coordinate
     * @param y y coordinate
     * @param speed moving speed of an object
     * @param length length of an object (amount of tiles it takes)
     * @param direction moving direction
     */
    MovingObject (float size, float x, float y, float speed, int length, Direction direction) {
        this.size = size;
        this.x = x;
        this.y = y;
        this.length = length;
        this.speed = speed;
        this.direction = direction;
    }

    public void render(SpriteBatch batch) {}

    public void pausedRender(SpriteBatch batch) {}

    /**
     * Method that implements object moving
     */
    public void move() {

        // moving left
        if (direction == Direction.LEFT) {

            if ((x + size*length) < FroggerGameScreen.level.getMap().getTiles()[0][0].getX()) {
                x = FroggerGameScreen.level.getMap().getTiles()[0][FroggerGameScreen.level.getMap().getnColumns() -1].getX() + FroggerGameScreen.level.getMap().getTiles()[0][0].getSize();
            }

            x -= size / speed;

        // moving right
        } else if (direction == Direction.RIGHT) {

            if (x > FroggerGameScreen.level.getMap().getTiles()[0][FroggerGameScreen.level.getMap().getnColumns() -1].getX() + FroggerGameScreen.level.getMap().getTiles()[0][0].getSize()) {
                x = FroggerGameScreen.level.getMap().getTiles()[0][0].getX() - FroggerGameScreen.level.getMap().getTiles()[0][0].getSize() * length;
            }

            x += size / speed;
        }
    }

    /**
     * Method to check for collision with a frog
     * @param frog frog
     * @return whether collide
     */
    public boolean checkCollision(Frog frog) {
        if (direction == Direction.LEFT) {
            return (frog.getX() + frog.getSize()) >= x + 0.3f*size && frog.getX() <= (x + size * length - 0.5f*size) &&
                    frog.getY() >= y && (frog.getY() + frog.getSize()) <= (y + size);
        }
        return (frog.getX() + frog.getSize()) >= x + 0.5f*size && frog.getX() <= (x + size * length - 0.3f*size) &&
                frog.getY() >= y && (frog.getY() + frog.getSize()) <= (y + size);

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

    public void setSize(float size) {
        this.size = size;
    }

    public void setFlooded(boolean flooded) {}
}
