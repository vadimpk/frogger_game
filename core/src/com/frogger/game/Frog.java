package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.frogger.game.Util.Direction;
import com.frogger.game.Util.TypeOfRow;

public class Frog {

    /** initialize frog attributes */
    private Tile tile;
    private float x,y,size;
    private Texture texture = new Texture(Gdx.files.internal("frog3.png"));
    private boolean alive;

    /** initialize fields for movements  */
    private long startedMovingTime;
    private boolean isMoving = false;
    private Direction movingDirection;
    private static final float SPEED = 5f;
    private static final long MOVE_TIME = 200000000;
    private int animationFrameCount = 0;

    private boolean onLog = false;
    private MovingObject log = null;
    private int logIndex = -1;
    private float distanceX = 0f;


    /**
     * Constructor that creates frog on a specific tile
     * @param tile tile
     */
    Frog(Tile tile) {
        this.tile = tile;
        x = tile.getX();
        y = tile.getY();
        size = tile.getSize();
        alive = true;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, size, size);
        update(0f);
    }

    /**
     * Method that runs every frame and updates the frog
     * @param dt delta time
     */
    public void update(float dt) {

        // check for collision with car (add something else as well later, for example train)
        if (FroggerGame.rows[tile.getRow()].getType() == TypeOfRow.CAR) {
            for (MovingObject car: FroggerGame.rows[tile.getRow()].objects) {
                if (car.checkCollision(this)) alive = false;
            }
        }

        // if frog is on a log then move it with log speed (and check if not behind screen)
        if (onLog) {
            if (x < FroggerGame.tiles[0][0].getX() || x > FroggerGame.tiles[0][FroggerGame.nColumns - 1].getX()) {
                alive = false;
            }
            if (log.direction == Direction.RIGHT) {
                x += log.size / log.speed;
            } else {
                x -= log.size / log.speed;
            }
        }

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

            // MOVE RIGHT
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
            {
                // if not in the last column
                if (tile.getColumn() < FroggerGame.nColumns - 1) {

                    // if on a log then check if won't land in water (if yes then die)
                    if (onLog) {
                        if (logIndex == log.length - 1) {
                            onLog = false;
                            alive = false;
                        } else {
                            logIndex++;
                            tile = FroggerGame.tiles[tile.getRow()][tile.getColumn()];
                        }
                    } else {
                        tile = FroggerGame.tiles[tile.getRow()][tile.getColumn() + 1];
                    }

                    startMoving(Direction.RIGHT);
                }
            }

            // MOVE LEFT
            else if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
            {
                // if not in the first column
                if (tile.getColumn() > 0) {

                    // if on a log then check if won't land in water (if yes then die)
                    if (onLog) {
                        if (logIndex == 0) {
                            onLog = false;
                            alive = false;
                        } else {
                            logIndex--;
                            tile = FroggerGame.tiles[tile.getRow()][tile.getColumn()];
                        }
                    } else {
                        tile = FroggerGame.tiles[tile.getRow()][tile.getColumn() -1];
                    }
                    startMoving(Direction.LEFT);

                }
            }

            // MOVE UP
            else if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))
            {
                // if not in the last row
                if (tile.getRow() < FroggerGame.nRows -1) {

                    // if next row is the row with logs (from any to log)
                    if (FroggerGame.rows[tile.getRow() + 1].getType() == TypeOfRow.LOG)
                    {
                        onLog = false; // set current log to false to check if frog lands on another log
                        for (MovingObject log: FroggerGame.rows[tile.getRow() + 1].objects) {
                            // method getLogWhenMovingUp() checks if frog has landed on a log
                            // if true, it saves information about log and information needed for animation in
                            // some variables, and it also defines tile from next row
                            if (getLogWhenMovingUpOrDown(log)) {
                                tile = FroggerGame.tiles[tile.getRow() + 1][tile.getColumn()];
                                break; // stop iterating as the log is already found
                            }
                        }
                        if (!onLog) alive = false; // if frog didn't land on the log then die
                    }
                    else // if next row is NOT the row with logs
                    {

                        // clear variables that are responsible for movement on logs
                        onLog = false;
                        log = null;

                        // if the row frog jumps FROM is log (from log to ground)
                        if (FroggerGame.rows[tile.getRow()].getType() == TypeOfRow.LOG) {
                            // find the tile to jump to
                            findTileByCoordinates(tile.getRow() + 1);

                        }
                        else {
                            // from ground to ground
                            distanceX = 0; // clear variables (just in case)
                            // define next tile
                            tile = FroggerGame.tiles[tile.getRow() + 1][tile.getColumn()];
                        }

                    }
                    // call method that changes variables responsible for frog movement
                    startMoving(Direction.UP);
                }
            }

            // MOVE DOWN
            else if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))
            {
                // if not in the first row
                if (tile.getRow() > 0) {

                    // from any to log
                    if (FroggerGame.rows[tile.getRow() - 1].getType() == TypeOfRow.LOG) {
                        onLog = false;
                        for (MovingObject log: FroggerGame.rows[tile.getRow() - 1].objects) {
                            if (getLogWhenMovingUpOrDown(log)) {
                                tile = FroggerGame.tiles[tile.getRow() - 1][tile.getColumn()];
                                break;
                            }
                        }
                        if (!onLog) alive = false;
                    } else {
                        // clear log
                        onLog = false;
                        log = null;

                        // from log to ground
                        if (FroggerGame.rows[tile.getRow()].getType() == TypeOfRow.LOG) {
                            // from log to ground
                            findTileByCoordinates(tile.getRow() - 1);

                        } else {
                            // from ground to ground
                            distanceX = 0;
                            tile = FroggerGame.tiles[tile.getRow() - 1][tile.getColumn()];
                        }

                    }
                    startMoving(Direction.DOWN);
                }
            }
        }
    }

    /**
     * Method to find the next tile when jumping from log
     * @param rowIndex next row index
     */
    private void findTileByCoordinates(int rowIndex) {
        for (Tile t : FroggerGame.tiles[rowIndex]) {
            if (x + size / 2 >= t.getX() && x + size / 2 < t.getX() + t.getSize()) {
                tile = FroggerGame.tiles[rowIndex][t.getColumn()];
                distanceX = t.getX() - x;
            }
        }
    }


    /**
     * Method that iterates through every piece of log (length)
     * and defines whether the frog will land on that piece or not
     * @param log log
     * @return true if lands
     */
    private boolean getLogWhenMovingUpOrDown(MovingObject log) {

        // if log is moving left
        if (log.direction == Direction.LEFT) {
            for (int i = 0; i < log.length; i++) {
                if (checkIfLandsOnLog(log, i)) return true;
            }
        } else { // if log is moving right
            for (int i = log.length - 1; i >= 0; i--) {
                if (checkIfLandsOnLog(log, i)) return true;
            }
        }
        return false;
    }

    /**
     * Method that defines whether the frog will land specific part of log
     * If lands, it also defines the parameters needed for animation and movement of the frog
     * @param log log
     * @param i index of part of log
     * @return true if lands
     */
    private boolean checkIfLandsOnLog(MovingObject log, int i) {
        if ((x + size / 2 >= log.x + i * size) && (x + size / 2 < log.x + (i + 1) * size)) {
            onLog = true;
            this.log = log; //set new log
            logIndex = i; // set new part of the log
            distanceX = log.x + i * size - x; // set dx for animation
            return true;
        }
        return false;
    }


    /**
     * Method that changes parameters when the movement starts
     * @param direction moving direction
     */
    private void startMoving (Direction direction) {
        startedMovingTime = TimeUtils.nanoTime();
        isMoving = true;
        movingDirection = direction;
        animationFrameCount = 0;
    }

    /**
     * Method to move up. Runs every frame
     */
    public void moveUp() {
        // counter of frames (for animation)
        animationFrameCount++;

        float dy = 0f;
        float dx = 0f;

        // don't let next move until time passes
        if (TimeUtils.nanoTime() - startedMovingTime > MOVE_TIME)
        {
            isMoving = false; // when time passes end moving
        }
        else
        {
            if (animationFrameCount == (int) SPEED) // check if it's the last animation leap (total is SPEED)
            {
                // when it's the last animation frame just set coordinates to what they have to be
                if (FroggerGame.rows[tile.getRow()].getType() != TypeOfRow.LOG) dx = tile.getX() - x;
                else dx = log.x + log.size * logIndex - x;

                dy = tile.getY() - y;

            }
            else if (animationFrameCount < (int) SPEED) // if not the last animation leap
            {
                dy = size / SPEED;
                dx = distanceX / SPEED;

            }

            y += dy;
            x += dx;

            // move camera
            if (tile.getRow() > (FroggerGame.nColumns / 2) && tile.getRow() < (FroggerGame.nRows - (FroggerGame.nColumns / 2)))
                FroggerGame.gameCamera.translate(0,dy,0);

        }
    }

    /**
     * Method to move down. Runs every frame
     */
    public void moveDown() {
        // counter of frames (for animation)
        animationFrameCount++;

        float dy = 0f;
        float dx = 0f;

        // don't let next move until time passes
        if (TimeUtils.nanoTime() - startedMovingTime > MOVE_TIME)
        {
            isMoving = false; // when time passes end moving
        }
        else
        {
            if (animationFrameCount == (int) SPEED) // check if it's the last animation leap (total is SPEED)
            {
                // when it's the last animation frame just set coordinates to what they have to be
                if (FroggerGame.rows[tile.getRow()].getType() != TypeOfRow.LOG) dx = tile.getX() - x;
                else dx = log.x + log.size * logIndex - x;

                dy = tile.getY() - y;

            }
            else if (animationFrameCount < (int) SPEED) // if not the last animation leap
            {
                dy = -size / SPEED;
                dx = distanceX / SPEED;
            }

            x += dx;
            y += dy;

            // move camera
            if (tile.getRow() >= FroggerGame.nColumns / 2 && tile.getRow() < (FroggerGame.nRows - (FroggerGame.nColumns / 2) - 1))
                FroggerGame.gameCamera.translate(0,dy,0);
        }
    }

    /**
     * Method to move right. Runs every frame
     */
    private void moveRight() {
        animationFrameCount++;

        if (TimeUtils.nanoTime() - startedMovingTime > MOVE_TIME) {
            isMoving = false;
        }
        else {
            if (animationFrameCount == (int) SPEED) {
                // when it's the last animation frame just set coordinates to what they have to be
                if (FroggerGame.rows[tile.getRow()].getType() != TypeOfRow.LOG) x = tile.getX();
                else x = log.x + log.size * logIndex;

            } else if (animationFrameCount < (int) SPEED) {
                if (onLog) {
                    if (log.direction == Direction.RIGHT) {
                        x -= log.size / log.speed;
                    } else {
                        x += log.size / log.speed;
                    }
                }
                x += size / SPEED;
            }
        }
    }

    /**
     * Method to move left. Runs every frame
     */
    public void moveLeft() {
        animationFrameCount++;

        if (TimeUtils.nanoTime() - startedMovingTime > MOVE_TIME) {
            isMoving = false;
        }
        else {
            if (animationFrameCount == (int) SPEED) {
                // when it's the last animation frame just set coordinates to what they have to be
                if (FroggerGame.rows[tile.getRow()].getType() != TypeOfRow.LOG) x = tile.getX();
                else x = log.x + log.size * logIndex;

            } else if (animationFrameCount < (int) SPEED) {
                x -= size / SPEED;
                if (onLog) {
                    if (log.direction == Direction.RIGHT) {
                        x -= log.size / log.speed;
                    } else {
                        x += log.size / log.speed;
                    }
                }
            }
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

    public boolean isAlive() {
        return alive;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setAlive(boolean alive) {this.alive = alive;}

    public void setTile(Tile tile) {
        this.tile = tile;
    }
}

