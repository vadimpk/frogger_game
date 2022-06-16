package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.frogger.game.Util.Direction;
import com.frogger.game.Util.TypeOfRow;
import com.frogger.game.objects.MovingObject;
import com.frogger.game.screens.FroggerGameScreen;

public class Frog {

    private static Frog instance;
    
    /** initialize frog attributes */
    private Tile tile;
    private float x,y,size;
    private boolean alive;
    private Texture texture;

    private static final Texture FROG_LOOKING_UP_TEXTURE = new Texture(Gdx.files.internal("characters/frog/frog-looking-up.png"));
    private static final Texture FROG_LOOKING_DOWN_TEXTURE = new Texture(Gdx.files.internal("characters/frog/frog-looking-down.png"));
    private static final Texture FROG_LOOKING_RIGHT_TEXTURE = new Texture(Gdx.files.internal("characters/frog/frog-looking-right.png"));
    private static final Texture FROG_LOOKING_LEFT_TEXTURE = new Texture(Gdx.files.internal("characters/frog/frog-looking-left.png"));

    private static final Texture FROG_JUMPING_UP_TEXTURE = new Texture(Gdx.files.internal("characters/frog/frog-jumping-up2.png"));
    private static final Texture FROG_JUMPING_DOWN_TEXTURE = new Texture(Gdx.files.internal("characters/frog/frog-jumping-down2.png"));
    private static final Texture FROG_JUMPING_RIGHT_TEXTURE = new Texture(Gdx.files.internal("characters/frog/frog-jumping-right2.png"));
    private static final Texture FROG_JUMPING_LEFT_TEXTURE = new Texture(Gdx.files.internal("characters/frog/frog-jumping-left2.png"));


    /** initialize fields for movement mechanic  */
    private long startedMovingTime;
    private boolean isMoving = false;
    private Direction movingDirection;
    private static final float SPEED = 5f;
    private static final long MOVE_TIME = 200000000;
    private static final long MOVE_ANIMATION_TIME = 150000000;
    private int animationFrameCount = 0;
    private boolean moveToTheWall;

    /** initialize fields for movement mechanic on logs  */
    private boolean onLog = false;
    private MovingObject log = null;
    private int logIndex = -1;
    private float distanceX = 0f;


    public static Frog get() {
        if(instance == null) instance = new Frog();
        return instance;
    }
    
    private Frog() {
        alive = true;
        texture = FROG_LOOKING_UP_TEXTURE;
    }
    
    public void setStartingTile(Tile tile){
        this.tile = tile;
        x = tile.getX();
        y = tile.getY();
        size = tile.getSize();
    }

    public void respawn(Tile tile) {
        this.tile = tile;
        x = tile.getX();
        y = tile.getY();
        size = tile.getSize();
        alive = true;
        texture = FROG_LOOKING_UP_TEXTURE;
        onLog = false;
        log = null;
        logIndex = -1;
        isMoving = false;
        moveToTheWall = false;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, size, size);
        update(0f);
    }

    public static void dispose() {
        FROG_LOOKING_UP_TEXTURE.dispose();
        FROG_LOOKING_DOWN_TEXTURE.dispose();
        FROG_LOOKING_LEFT_TEXTURE.dispose();
        FROG_LOOKING_RIGHT_TEXTURE.dispose();
        FROG_JUMPING_UP_TEXTURE.dispose();
        FROG_JUMPING_DOWN_TEXTURE.dispose();
        FROG_JUMPING_LEFT_TEXTURE.dispose();
        FROG_JUMPING_RIGHT_TEXTURE.dispose();
    }

    /**
     * Method that runs every frame and updates the frog
     * @param dt delta time
     */
    public void update(float dt) {
        
        Row[] rows = FroggerGameScreen.level.getMap().getRows();
        Tile[][] tiles = FroggerGameScreen.level.getMap().getTiles();
        int nColumns = FroggerGameScreen.level.getMap().getnColumns();

        // check for collision with car
        if (rows[tile.getROW()].getType() == TypeOfRow.CAR) {
            for (MovingObject car: rows[tile.getROW()].getMovingObjects()) {
                if (car.checkCollision(this)) alive = false;
            }
            for (MovingObject car: rows[tile.getROW() - 1].getMovingObjects()) {
                if (car.checkCollision(this)) alive = false;
            }
        }

        // check for collision with train
        if (rows[tile.getROW()].getType() == TypeOfRow.TRAIN) {
            for (MovingObject train: rows[tile.getROW()].getMovingObjects()) {
                if (train.checkCollision(this)) alive = false;
            }
        }

        // if frog is on a log then move it with log speed (and check if not behind screen)
        if (onLog) {
            if (!log.isSafe()) {
                alive = false;
            }
            if (x < tiles[0][0].getX() - tiles[0][0].getSize()*0.5f || x > tiles[0][nColumns - 1].getX() + tiles[0][0].getSize()*0.5f) {
                alive = false;
            }
            if (log.getDirection() == Direction.RIGHT) {
                x += log.getSize() / log.getSpeed();
            } else {
                x -= log.getSize() / log.getSpeed();
            }
        }

        // movement mechanics
        if (isMoving) {

            if (movingDirection == Direction.UP) {
                animateMovingUp();
            } else if (movingDirection == Direction.DOWN) {
                animateMovingDown();
            } else if (movingDirection == Direction.RIGHT) {
                animateMovingRight();
            } else if (movingDirection == Direction.LEFT) {
                animateMovingLeft();
            }

        } else {

            // MOVE RIGHT
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
            {
                moveRight();
            }

            // MOVE LEFT
            else if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
            {
                moveLeft();
            }

            // MOVE UP
            else if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))
            {
                moveUp();
            }

            // MOVE DOWN
            else if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))
            {
                moveDown();
            }
        }
    }

    /**
     * Method that handles moving right action when player pressed "D" or "->" button.
     */
    private void moveRight() {
        Tile[][] tiles = FroggerGameScreen.level.getMap().getTiles();
        int nColumns = FroggerGameScreen.level.getMap().getnColumns();
        
        texture = FROG_LOOKING_RIGHT_TEXTURE;
        // if not in the last column
        if (tile.getCOLUMN() < nColumns - 1) {

            // if on a log then check if won't land in water (if yes then die)
            if (onLog) {
                if (logIndex == log.getLength() - 1) {
                    onLog = false;
                    alive = false;
                } else {
                    logIndex++;
                    tile = tiles[tile.getROW()][tile.getCOLUMN()];
                }
                startMoving(Direction.RIGHT);
            } else {
                startMoving(Direction.RIGHT, !tiles[tile.getROW()][tile.getCOLUMN() + 1].isTransparent());
                if (tiles[tile.getROW()][tile.getCOLUMN() + 1].isTransparent())
                    tile = tiles[tile.getROW()][tile.getCOLUMN() + 1];
                else
                    tile = tiles[tile.getROW()][tile.getCOLUMN()];

            }
        }
    }

    /**
     * Method that handles moving left action when player pressed "A" or "<-" button.
     */
    private void moveLeft() {
        Tile[][] tiles = FroggerGameScreen.level.getMap().getTiles();
        
        texture = FROG_LOOKING_LEFT_TEXTURE;
        // if not in the first column
        if (tile.getCOLUMN() > 0) {

            // if on a log then check if won't land in water (if yes then die)
            if (onLog) {
                if (logIndex == 0) {
                    onLog = false;
                    alive = false;
                } else {
                    logIndex--;
                    tile = tiles[tile.getROW()][tile.getCOLUMN()];
                }
                startMoving(Direction.LEFT);
            } else {
                startMoving(Direction.LEFT, !tiles[tile.getROW()][tile.getCOLUMN() - 1].isTransparent());
                if (tiles[tile.getROW()][tile.getCOLUMN() - 1].isTransparent())
                    tile = tiles[tile.getROW()][tile.getCOLUMN() - 1];
                else
                    tile = tiles[tile.getROW()][tile.getCOLUMN()];
            }
        }
    }

    /**
     * Method that handles moving up action when player pressed "W" or "^" button.
     */
    private void moveUp() {
        Row[] rows = FroggerGameScreen.level.getMap().getRows();
        Tile[][] tiles = FroggerGameScreen.level.getMap().getTiles();
        int nRows = FroggerGameScreen.level.getMap().getnRows();
        
        texture = FROG_LOOKING_UP_TEXTURE;
        // if not in the last row
        if (tile.getROW() < nRows -1) {

            // if next row is the row with logs (from any to log)
            if (rows[tile.getROW() + 1].getType() == TypeOfRow.LOG)
            {
                onLog = false; // set current log to false to check if frog lands on another log
                for (MovingObject log: rows[tile.getROW() + 1].getMovingObjects()) {
                    // method getLogWhenMovingUp() checks if frog has landed on a log
                    // if true, it saves information about log and information needed for animation in
                    // some variables, and it also defines tile from next row
                    if (getLogWhenMovingUpOrDown(log)) {
                        tile = tiles[tile.getROW() + 1][tile.getCOLUMN()];
                        startMoving(Direction.UP);
                        texture = FROG_LOOKING_UP_TEXTURE;
                        break; // stop iterating as the log is already found
                    }
                }
                if (!onLog) alive = false; // if frog didn't land on the log then die
            }
            else // if next row is NOT the row with logs
            {

                // if the row frog jumps FROM is log (from log to ground)
                if (rows[tile.getROW()].getType() == TypeOfRow.LOG) {
                    // find the tile to jump to
                    if (findTileByCoordinates(tile.getROW() + 1))
                        // call method that changes variables responsible for frog movement
                        startMoving(Direction.UP);
                }
                else {
                    // from ground to ground
                    // clear variables
                    onLog = false;
                    log = null;
                    distanceX = 0;
                    // define next tile and start animation
                    startMoving(Direction.UP, !tiles[tile.getROW() + 1][tile.getCOLUMN()].isTransparent());
                    if (tiles[tile.getROW() + 1][tile.getCOLUMN()].isTransparent())
                        tile = tiles[tile.getROW() + 1][tile.getCOLUMN()];
                    else
                        tile = tiles[tile.getROW()][tile.getCOLUMN()];
                }

            }

        }
    }

    /**
     * Method that handles moving left action when player pressed "S" or "down" button.
     */
    private void moveDown() {
        Row[] rows = FroggerGameScreen.level.getMap().getRows();
        Tile[][] tiles = FroggerGameScreen.level.getMap().getTiles();

        texture = FROG_LOOKING_DOWN_TEXTURE;
        // if not in the first row
        if (tile.getROW() > 0) {

            // from any to log
            if (rows[tile.getROW() - 1].getType() == TypeOfRow.LOG) {
                onLog = false;
                for (MovingObject log: rows[tile.getROW() - 1].getMovingObjects()) {
                    if (getLogWhenMovingUpOrDown(log)) {
                        tile = tiles[tile.getROW() - 1][tile.getCOLUMN()];
                        startMoving(Direction.DOWN);
                        texture = FROG_LOOKING_DOWN_TEXTURE;
                        break;
                    }
                }
                if (!onLog) alive = false;
            } else {

                // from log to ground
                if (rows[tile.getROW()].getType() == TypeOfRow.LOG) {
                    // from log to ground
                    if (findTileByCoordinates(tile.getROW() - 1))
                        startMoving(Direction.DOWN);

                } else {
                    // from ground to ground
                    onLog = false;
                    log = null;
                    distanceX = 0;
                    startMoving(Direction.DOWN, !tiles[tile.getROW() - 1][tile.getCOLUMN()].isTransparent());
                    if (tiles[tile.getROW() - 1][tile.getCOLUMN()].isTransparent())
                        tile = tiles[tile.getROW() - 1][tile.getCOLUMN()];
                    else
                        tile = tiles[tile.getROW()][tile.getCOLUMN()];
                }

            }
        }
    }

    /**
     * Method to find the next tile when jumping from log
     * @param rowIndex next row index
     */
    private boolean findTileByCoordinates(int rowIndex) {
        Tile[][] tiles = FroggerGameScreen.level.getMap().getTiles();

        for (Tile t : tiles[rowIndex]) {
            if (x + size / 2 >= t.getX() && x + size / 2 < t.getX() + t.getSize() && t.isTransparent()) {
                tile = tiles[rowIndex][t.getCOLUMN()];
                distanceX = t.getX() - x;
                // clear variables that are responsible for movement on logs
                onLog = false;
                log = null;
                return true;
            }
        }
        return false;
    }


    /**
     * Method that iterates through every piece of log (length)
     * and defines whether the frog will land on that piece or not
     * @param log log
     * @return true if lands
     */
    private boolean getLogWhenMovingUpOrDown(MovingObject log) {

        // if log is moving left
        if (log.getDirection() == Direction.LEFT) {
            for (int i = 0; i < log.getLength(); i++) {
                if (checkIfLandsOnLog(log, i)) return true;
            }
        } else { // if log is moving right
            for (int i = log.getLength() - 1; i >= 0; i--) {
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

        boolean gotLog = false;

        if (log.getDirection() == Direction.LEFT){
            if ((x + size / 2 >= log.getX() + i * size - 0.4f*size) && (x + size / 2 < log.getX() + (i + 1) * size - 0.4f*size)) gotLog = true;
        } else {
            if ((x + size / 2 >= log.getX() + i * size  + 0.4f*size) && (x + size / 2 < log.getX() + (i + 1) * size + 0.4f*size)) gotLog = true;
        }
        if (gotLog) {
            onLog = true;
            this.log = log; //set new log
            logIndex = i; // set new part of the log
            distanceX = log.getX() + i * size - x; // set dx for animation
            return true;
        }
        return false;
    }


    /**
     * Method that changes parameters when the movement starts
     * @param direction moving direction
     */
    private void startMoving (Direction direction, boolean toTheWall) {
        startedMovingTime = TimeUtils.nanoTime();
        isMoving = true;
        movingDirection = direction;
        animationFrameCount = 0;
        moveToTheWall = toTheWall;
    }

    private void startMoving (Direction direction) {
        startedMovingTime = TimeUtils.nanoTime();
        isMoving = true;
        movingDirection = direction;
        animationFrameCount = 0;
    }


    /**
     * Method to move up. Runs every frame
     */
    public void animateMovingUp() {
        Row[] rows = FroggerGameScreen.level.getMap().getRows();
        int nColumns = FroggerGameScreen.level.getMap().getnColumns();
        int nRows = FroggerGameScreen.level.getMap().getnRows();

        // don't let next move until time passes
        if (TimeUtils.nanoTime() - startedMovingTime > MOVE_TIME) {
            isMoving = false; // when time passes end moving
            if (onLog) log.setFlooded(false);
        }

        // counter of frames (for animation)
        animationFrameCount++;

        float dy = 0f;
        float dx = 0f;

        if (moveToTheWall) {
            texture = FROG_JUMPING_UP_TEXTURE;
            if (animationFrameCount > 2) {
                texture = FROG_LOOKING_UP_TEXTURE;
                moveToTheWall = false;
            }
        } else {
            if (animationFrameCount == (int) SPEED) // check if it's the last animation leap (total is SPEED)
            {
                // when it's the last animation frame just set coordinates to what they have to be
                if (rows[tile.getROW()].getType() != TypeOfRow.LOG) dx = tile.getX() - x;
                else dx = log.getX() + log.getSize() * logIndex - x;

                dy = tile.getY() - y;

            }
            else if (animationFrameCount < (int) SPEED) // if not the last animation leap
            {
                dy = size / SPEED;
                dx = distanceX / SPEED;
                if (onLog) log.setFlooded(true);
            }

            y += dy;
            x += dx;

            // move camera
            if (tile.getROW() > (nColumns / 2) && tile.getROW() < (nRows - (nColumns / 2)))
                FroggerGame.gameCamera.translate(0,dy,0);

            // animate
            if (animationFrameCount == 2) {
                texture = FROG_JUMPING_UP_TEXTURE;
            }
            if (TimeUtils.nanoTime() - startedMovingTime > MOVE_ANIMATION_TIME) {
                texture = FROG_LOOKING_UP_TEXTURE;
            }


        }
    }

    /**
     * Method to move down. Runs every frame
     */
    public void animateMovingDown() {
        Row[] rows = FroggerGameScreen.level.getMap().getRows();
        int nColumns = FroggerGameScreen.level.getMap().getnColumns();
        int nRows = FroggerGameScreen.level.getMap().getnRows();

        // don't let next move until time passes
        if (TimeUtils.nanoTime() - startedMovingTime > MOVE_TIME) {
            isMoving = false; // when time passes end moving
            if (onLog) log.setFlooded(false);
        }

        // counter of frames (for animation)
        animationFrameCount++;

        float dy = 0f;
        float dx = 0f;


        if (moveToTheWall) {
            texture = FROG_JUMPING_DOWN_TEXTURE;
            if (animationFrameCount > 2) {
                texture = FROG_LOOKING_DOWN_TEXTURE;
                moveToTheWall = false;
            }
        }
        else
        {
            if (animationFrameCount == (int) SPEED) // check if it's the last animation leap (total is SPEED)
            {
                // when it's the last animation frame just set coordinates to what they have to be
                if (rows[tile.getROW()].getType() != TypeOfRow.LOG) dx = tile.getX() - x;
                else dx = log.getX() + log.getSize() * logIndex - x;

                dy = tile.getY() - y;

            }
            else if (animationFrameCount < (int) SPEED) // if not the last animation leap
            {
                dy = -size / SPEED;
                dx = distanceX / SPEED;
                if (onLog) log.setFlooded(true);
            }

            x += dx;
            y += dy;

            // move camera
            if (tile.getROW() >= nColumns / 2 && tile.getROW() < (nRows - (nColumns / 2) - 1))
                FroggerGame.gameCamera.translate(0,dy,0);

            // animate
            if (animationFrameCount == 2) {
                texture = FROG_JUMPING_DOWN_TEXTURE;
            } if (TimeUtils.nanoTime() - startedMovingTime > MOVE_ANIMATION_TIME) {
                texture = FROG_LOOKING_DOWN_TEXTURE;
            }
        }
    }

    /**
     * Method to move right. Runs every frame
     */
    private void animateMovingRight() {
        Row[] rows = FroggerGameScreen.level.getMap().getRows();

        if (TimeUtils.nanoTime() - startedMovingTime > MOVE_TIME) {
            isMoving = false;
            if (onLog) log.setFlooded(false);
        }

        animationFrameCount++;

        if (moveToTheWall) {
            texture = FROG_JUMPING_RIGHT_TEXTURE;
            if (animationFrameCount > 2) {
                texture = FROG_LOOKING_RIGHT_TEXTURE;
                moveToTheWall = false;
            }
        } else {
            if (animationFrameCount == (int) SPEED) {
                // when it's the last animation frame just set coordinates to what they have to be
                if (rows[tile.getROW()].getType() != TypeOfRow.LOG) x = tile.getX();
                else x = log.getX() + log.getSize() * logIndex;

            } else if (animationFrameCount < (int) SPEED) {
                x += size / SPEED;
                if (onLog) {
                    log.setFlooded(true);
                    if (log.getDirection() == Direction.RIGHT) {
                        x -= log.getSize() / log.getSpeed();
                    } else {
                        x += log.getSize() / log.getSpeed();
                    }
                }
            }
            // animate
            if (animationFrameCount == 2) {
                texture = FROG_JUMPING_RIGHT_TEXTURE;
            } if (TimeUtils.nanoTime() - startedMovingTime > MOVE_ANIMATION_TIME) {
                texture = FROG_LOOKING_RIGHT_TEXTURE;
            }
        }
    }

    /**
     * Method to move left. Runs every frame
     */
    public void animateMovingLeft() {
        Row[] rows = FroggerGameScreen.level.getMap().getRows();

        if (TimeUtils.nanoTime() - startedMovingTime > MOVE_TIME) {
            isMoving = false;
            if (onLog) log.setFlooded(false);
        }
        animationFrameCount++;

        if (moveToTheWall) {
            texture = FROG_JUMPING_LEFT_TEXTURE;
            if (animationFrameCount > 2) {
                texture = FROG_LOOKING_LEFT_TEXTURE;
                moveToTheWall = false;
            }
        } else {
            if (animationFrameCount == (int) SPEED) {
                // when it's the last animation frame just set coordinates to what they have to be
                if (rows[tile.getROW()].getType() != TypeOfRow.LOG) x = tile.getX();
                else x = log.getX() + log.getSize() * logIndex;

            } else if (animationFrameCount < (int) SPEED) {
                x -= size / SPEED;
                if (onLog) {
                    log.setFlooded(true);
                    if (log.getDirection() == Direction.RIGHT) {
                        x -= log.getSize() / log.getSpeed();
                    } else {
                        x += log.getSize() / log.getSpeed();
                    }
                }
            }
            // animate
            if (animationFrameCount == 2) {
                texture = FROG_JUMPING_LEFT_TEXTURE;
            } if (TimeUtils.nanoTime() - startedMovingTime > MOVE_ANIMATION_TIME) {
                texture = FROG_LOOKING_LEFT_TEXTURE;
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

