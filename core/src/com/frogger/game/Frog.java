package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

enum Direction {UP,DOWN,LEFT,RIGHT}

public class Frog {

    /** initialize frog attributes */
    private Tile tile;
    private float x,y,size;
    private Texture texture = new Texture(Gdx.files.internal("frog3.png"));

    /** initialize fields for movements  */
    private long startedMovingTime;
    private boolean isMoving = false;
    private Direction movingDirection;
    private static final float SPEED = 5f;
    private static final long MOVE_TIME = 150000000;
    private int animationLeapCount = 0;


    /**
     * Constructor that creates frog on a specific tile
     * @param tile tile
     */
    Frog(Tile tile) {
        this.tile = tile;
        x = tile.getX();
        y = tile.getY();
        size = tile.getSize();
    }

    /**
     * Method that runs every frame and updates the frog
     * @param dt delta time
     */
    public void update(float dt) {


        // movement
        if (isMoving) {

            if (movingDirection == Direction.UP) {
                moveUp();
            } else if (movingDirection == Direction.DOWN) {
                moveDown();
            } else if (movingDirection == Direction.RIGHT) {
                moveRight();
            } else if (movingDirection == Direction.LEFT) {
                moveLeft();
            }

        } else {

            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))
            {
                if (tile.getRow() < FroggerGame.tilesPerColumn -1) {

                    tile = FroggerGame.tiles[tile.getColumn()][tile.getRow() +1];
                    startMoving(Direction.UP);

                }
            }
            else if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))
            {
                if (tile.getRow() > 0) {

                    tile = FroggerGame.tiles[tile.getColumn()][tile.getRow() -1];
                    startMoving(Direction.DOWN);

                }
            }
            else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
            {
                if (tile.getColumn() < FroggerGame.tilesPerRow - 1) {

                    tile = FroggerGame.tiles[tile.getColumn() +1][tile.getRow()];
                    startMoving(Direction.RIGHT);

                }
            }
            else if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
            {
                if (tile.getColumn() > 0) {

                    tile = FroggerGame.tiles[tile.getColumn() -1][tile.getRow()];
                    startMoving(Direction.LEFT);

                }
            }
        }
    }


    /**
     * Method that changes parameters when the movement starts
     * @param direction moving direction
     */
    private void startMoving (Direction direction) {
        startedMovingTime = TimeUtils.nanoTime();
        isMoving = true;
        movingDirection = direction;
        animationLeapCount = 0;
    }

    /**
     * Method to move up. Runs every frame
     */
    public void moveUp() {
        // fields for smooth movement animation
        animationLeapCount++;
        float distance;

        // don't let next move until time passes
        if (TimeUtils.nanoTime() - startedMovingTime > MOVE_TIME)
        {
            isMoving = false; // when time passes end moving
        }
        else
        {
            if (animationLeapCount == (int) SPEED - 1) // check if it's the last animation leap (total is SPEED)
            {
                distance = tile.getY() - y; // set distance to difference between destination tile and current y coordinates
            }
            else if (animationLeapCount < (int) SPEED - 1) // if not the last animation leap
            {
                distance = size / SPEED; // set distance to size / speed
            }
            else // no animation left means frog has moved so
            {
                distance = 0; // set distance to zero
            }
            // move frog
            y += distance;

            // move camera
            if (tile.getRow() > (FroggerGame.tilesPerRow / 2) && tile.getRow() < (FroggerGame.tilesPerColumn - (FroggerGame.tilesPerRow / 2)))
                FroggerGame.gameCamera.translate(0,distance,0);
        }
    }

    /**
     * Method to move down. Runs every frame
     */
    public void moveDown() {
        animationLeapCount++;
        float distance;

        if (TimeUtils.nanoTime() - startedMovingTime > MOVE_TIME) {
            isMoving = false;
        }
        else {
            if (animationLeapCount == (int) SPEED - 1) {
                distance = y - tile.getY();
            }
            else if (animationLeapCount < (int) SPEED - 1) {
                distance = size / SPEED;
            }
            else {
                distance = 0;
            }

            y -= distance;
            if (tile.getRow() > (FroggerGame.tilesPerRow / 2 - 1) && tile.getRow() < (FroggerGame.tilesPerColumn - (FroggerGame.tilesPerRow / 2 + 1)))
               FroggerGame.gameCamera.translate(0,-distance,0);
        }
    }

    /**
     * Method to move right. Runs every frame
     */
    public void moveRight() {
        animationLeapCount++;
        float distance;

        if (TimeUtils.nanoTime() - startedMovingTime > MOVE_TIME) {
            isMoving = false;
        }
        else {
            if (animationLeapCount == (int) SPEED - 1) {
                distance = tile.getX() - x;
            } else if (animationLeapCount < (int) SPEED - 1) {
                distance = size / SPEED;
            } else {
                distance = 0;
            }

            x += distance;
        }
    }

    /**
     * Method to move left. Runs every frame
     */
    public void moveLeft() {
        animationLeapCount++;
        float distance;

        if (TimeUtils.nanoTime() - startedMovingTime > MOVE_TIME) {
            isMoving = false;
        }
        else {
            if (animationLeapCount == (int) SPEED - 1) {
                distance = x - tile.getX();
            } else if (animationLeapCount < (int) SPEED - 1) {
                distance = size / SPEED;
            } else {
                distance = 0;
            }

            x -= distance;
        }
    }


    /**
     * Get tile that frogs stands on
     * @return tile
     */
    public Tile getTile() {return tile;}

    /**
     * Get x coordinate of a frog
     * @return x
     */
    public float getX() {return x;}

    /**
     * Get y coordinate of a frog
     * @return y
     */
    public float getY() {return y;}

    /**
     * Get size of a frog
     * @return size
     */
    public float getSize() {return size;}

    /**
     * Get texture of a frog
     * @return texture
     */
    public Texture getTexture() {return texture;}


    public void setX(float x) {
        this.x = x;
    }
}
