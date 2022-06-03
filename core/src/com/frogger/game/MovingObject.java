package com.frogger.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.frogger.game.Util.Direction;

public class MovingObject {

    public float x,y,size;
    public float speed;
    public int length;
    public Texture texture;
    public boolean safe;
    public Direction direction;

    MovingObject (float size, float x, float y, float speed, int length, Texture texture, boolean safe, Direction direction) {
        this.size = size;
        this.x = x;
        this.y = y;
        this.length = length;
        this.speed = speed;
        this.texture = texture;
        this.safe = safe;
        this.direction = direction;
    }

    public void draw(SpriteBatch batch, Frog frog) {
        for (int i = 0; i < length; i++) {
            batch.draw(texture, x + i * size, y, size, size);
        }
        if (!safe) {
            if (checkCollision(frog)) {
                frog.setAlive(false);
            }
        }
    }

    public void move() {

        if (direction == Direction.LEFT) {

            if ((x + size*length) < FroggerGame.rows[0].getTiles()[0].getX()) {
                x = FroggerGame.rows[FroggerGame.tilesPerRow-1].getTiles()[0].getX() + FroggerGame.rows[0].getTiles()[0].getSize();
            }

            x -= size / speed;
        } else if (direction == Direction.RIGHT) {

            if (x > FroggerGame.rows[FroggerGame.tilesPerRow-1].getTiles()[0].getX() + FroggerGame.rows[0].getTiles()[0].getSize()) {
                x = FroggerGame.rows[0].getTiles()[0].getX() - FroggerGame.rows[0].getTiles()[0].getSize() * length;
            }

            x += size / speed;
        }
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
}
