package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;
import com.frogger.game.Util.Direction;
import com.frogger.game.Util.TypeOfRow;
import com.frogger.game.objects.MovingObject;
import com.frogger.game.screens.FroggerGameScreen;

/**
 * Frog.java
 * @author vadympolishchuk
 * This is a class of the main character of the game - Frog.
 * This class implements movement mechanichs of the main character, default animations and its sounds.
 * Method update() runs every frame of the application and is responsible for character changes.
 */

public class Frog {

    private static Frog instance;
    
    /** initialize frog attributes */
    private Tile tile;
    private float x, y, size;
    private boolean alive;
    private TextureRegion texture;
    private int textureRotation;
    private CharacterTexture character = new CharacterTexture();

    /** initialize fields for movement mechanic  */
    private long startedMovingTime;
    private boolean isMoving = false;
    private Direction movingDirection;
    private static final float SPEED = 5f;
    private static final long MOVE_TIME = 200000000;
    private static final long MOVE_ANIMATION_TIME = 150000000;
    private int animationFrameCount = 0;
    private boolean moveToTheWall = false;
    private boolean goingToDrown = false;
    private boolean goingToDie = false;
    private boolean moveCamera = true;

    /** initialize fields for movement mechanic on logs  */
    private boolean onLog = false;
    private MovingObject log = null;
    private int logIndex = -1;
    private float distanceX = 0f;


    /**
     * Get instance of a frog
     */
    public static Frog get() {
        if(instance == null) instance = new Frog();
        return instance;
    }

    /**
     * Constructor of a frog
     */
    private Frog() {
        alive = true;
        texture = character.standing;
        textureRotation = 0;
    }

    /**
     * Method to set a starting tile of a frog
     * @param tile tile to spawn at
     */
    public void setStartingTile(Tile tile){
        this.tile = tile;
        x = tile.getX();
        y = tile.getY();
        size = tile.getSize();
    }

    /**
     * Method that changes parameters of a frog when it is respawned
     * @param tile tile to respawn at
     */
    public void respawn(Tile tile) {
        setStartingTile(tile);
        alive = true;
        texture = character.standing;
        onLog = false;
        log = null;
        logIndex = -1;
        isMoving = false;
        moveToTheWall = false;
        goingToDrown = false;
        goingToDie = false;
        Audio.setPlayingFrogSplashing(false);
        Audio.setPlayingFrogDying(false);
        Audio.setPlayingFrogJumping(false);
        Audio.setPlayingFrogOnLog(false);
        Audio.setPlayingTraffic(false);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, size/2, size/2 , size, size, 1, 1, textureRotation);
        update(0f);
    }

    public static void dispose() {
        CharacterTexture.dispose();
    }

    /**
     * Method that runs every frame and updates the frog
     * @param dt delta time
     */
    public void update(float dt) {
        if(!FroggerGameScreen.isPaused) {

            Row[] rows = FroggerGameScreen.level.getMap().getRows();
            Tile[][] tiles = FroggerGameScreen.level.getMap().getTiles();
            int nColumns = FroggerGameScreen.level.getMap().getnColumns();
            int nRows = FroggerGameScreen.level.getMap().getnRows();

            // if frog is on the last row (finish line) - don't let it move
            if (tile.getROW() == nRows - 1 && !isMoving) {
                isMoving = true;
                movingDirection = Direction.NONE;
                Audio.stopPlayingTrafficSound();
            }


            // check for collision with car or train (if frog is on row with cars or train)
            // if collided, play sound of dying and set frog to animate death
            if (rows[tile.getROW()].getType() == TypeOfRow.CAR || rows[tile.getROW()].getType() == TypeOfRow.TRAIN) {
                for (MovingObject carOrTrain : rows[tile.getROW()].getMovingObjects()) {
                    if (carOrTrain.checkCollision(this)) {
                        Audio.playFrogDyingSound();
                        if (!goingToDie) {
                            goingToDie = true;
                            startedMovingTime = TimeUtils.nanoTime();
                        }
                        animateDying();
                    }
                }
            }

            // if frog is on a row with lily pads and frog is not on a lily pad, make it drown
            if (rows[tile.getROW()].getType() == TypeOfRow.LILY) {
                if (!tile.isSafe()) {
                    goingToDrown = true;
                    Audio.playFrogSplashingSound();
                }
            }

            // if next or current or previous row is a row with cars, play sound of traffic
            if (tile.getROW() >= 1 && rows.length - tile.getROW() >= 2) {
                if (rows[tile.getROW() + 1].getType() == TypeOfRow.CAR ||
                        rows[tile.getROW()].getType() == TypeOfRow.CAR ||
                        rows[tile.getROW() - 1].getType() == TypeOfRow.CAR && alive) {
                    Audio.startPlayingTrafficSound();
                } else Audio.stopPlayingTrafficSound();
            } else Audio.stopPlayingTrafficSound();

            // if frog is on a log then move it with log speed (and check if not behind screen)
            if (onLog) {
                if (!log.isSafe() && !isMoving) { // if log broke and became not safe then make frog drown
                    if (!goingToDrown) {
                        startedMovingTime = TimeUtils.nanoTime();
                        animationFrameCount = 0;
                        goingToDrown = true;
                        Audio.playFrogSplashingSound();
                    }
                    drown();
                }
                // if out of screen
                if (x < tiles[0][0].getX() - tiles[0][0].getSize() * 0.5f || x > tiles[0][nColumns - 1].getX() + tiles[0][0].getSize() * 0.5f && !isMoving) {
                    alive = false;
                    Audio.playFrogDyingSound();
                }
                // move with log speed
                if (log.getDirection() == Direction.RIGHT) {
                    x += log.getSize() / log.getSpeed();
                } else {
                    x -= log.getSize() / log.getSpeed();
                }
            }

            // movement mechanics
            if (!goingToDie) {
                if (isMoving) {

                    if (movingDirection == Direction.UP) {
                        animateMovingUp(rows, nColumns, nRows);
                    } else if (movingDirection == Direction.DOWN) {
                        animateMovingDown(rows, nColumns, nRows);
                    } else if (movingDirection == Direction.RIGHT) {
                        animateMovingRight(rows);
                    } else if (movingDirection == Direction.LEFT) {
                        animateMovingLeft(rows);
                    }

                    // play sound if moving (plays once for a move)
                    Audio.playFrogJumpingSound();
                    if (onLog) {
                        Audio.playFrogOnLogSound();
                    }

                } else {
                    // stop playing sound after finished moving
                    Audio.setPlayingFrogJumping(false);
                    Audio.setPlayingFrogOnLog(false);

                    // MOVE RIGHT
                    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                        moveRight(tiles, nColumns);
                    }

                    // MOVE LEFT
                    else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
                        moveLeft(tiles);
                    }

                    // MOVE UP
                    else if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
                        moveUp(rows, tiles, nRows);
                    }

                    // MOVE DOWN
                    else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                        moveDown(rows, tiles);
                    }
                }
            }
        }
    }

    /**
     * Method that handles moving right action when player pressed "D" or "->" button.
     */
    private void moveRight(Tile[][] tiles, int nColumns) {

        // rotate the texture of a frog to face right
        textureRotation = 270;
        // check if can move right (if not in the last column)
        if (tile.getCOLUMN() < nColumns - 1) {

            // if on a log then check if won't land in water (if yes then make frog die)
            if (onLog) {
                if (logIndex == log.getLength() - 1) {
                    onLog = false;
                    goingToDrown = true;
                    Audio.playFrogSplashingSound();
                } else {
                    logIndex++;
                    tile = tiles[tile.getROW()][tile.getCOLUMN()];
                }
                startMoving(Direction.RIGHT);
            } else {
                // if not on log then check if tile to the right is transparent and set next tile accordingly
                startMoving(Direction.RIGHT, !tiles[tile.getROW()][tile.getCOLUMN() + 1].isTransparent());
                if (tiles[tile.getROW()][tile.getCOLUMN() + 1].isTransparent())
                    tile = tiles[tile.getROW()][tile.getCOLUMN() + 1];
                else
                    tile = tiles[tile.getROW()][tile.getCOLUMN()];

            }
        }
        // if in last column that animate moving to the wall
        else if (tile.getCOLUMN() == nColumns) {
            startMoving(Direction.RIGHT, true);
        }
    }

    /**
     * Method that handles moving left action when player pressed "A" or "<-" button.
     */
    private void moveLeft(Tile[][] tiles) {

        // rotate the texture of a frog to face left
        textureRotation = 90;
        // check if can move left (if not in the first column)
        if (tile.getCOLUMN() > 0) {

            // if on a log then check if won't land in water (if yes then die)
            if (onLog) {
                if (logIndex == 0) {
                    onLog = false;
                    goingToDrown = true;
                    Audio.playFrogSplashingSound();
                } else {
                    logIndex--;
                    tile = tiles[tile.getROW()][tile.getCOLUMN()];
                }
                startMoving(Direction.LEFT);
            } else {
                // if not on log then check if tile to the left is transparent and set next tile accordingly
                startMoving(Direction.LEFT, !tiles[tile.getROW()][tile.getCOLUMN() - 1].isTransparent());
                if (tiles[tile.getROW()][tile.getCOLUMN() - 1].isTransparent())
                    tile = tiles[tile.getROW()][tile.getCOLUMN() - 1];
                else
                    tile = tiles[tile.getROW()][tile.getCOLUMN()];
            }
        }
        // if in the first column that animate moving to the wall
        else if (tile.getCOLUMN() == 0) {
            startMoving(Direction.LEFT, true);
        }
    }

    /**
     * Method that handles moving up action when player pressed "W" or "^" button.
     */
    private void moveUp(Row[] rows, Tile[][] tiles, int nRows) {

        // rotate the texture of a frog to face up
        textureRotation = 0;
        // check if can move up (if not in the last row)
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
                        textureRotation = 0;
                        break; // stop iterating as the log is already found
                    }
                }
                if (!onLog) { // if frog didn't land on the log then die
                    startMoving(Direction.UP);
                    goingToDrown = true;
                    Audio.playFrogSplashingSound();
                }
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
    private void moveDown(Row[] rows, Tile[][] tiles) {

        // rotate the texture of a frog to face down
        textureRotation = 180;
        // check if can move down (if not in the first row)
        if (tile.getROW() > 0) {

            // from any to log
            if (rows[tile.getROW() - 1].getType() == TypeOfRow.LOG) {
                onLog = false;
                for (MovingObject log: rows[tile.getROW() - 1].getMovingObjects()) {
                    if (getLogWhenMovingUpOrDown(log)) {
                        tile = tiles[tile.getROW() - 1][tile.getCOLUMN()];
                        startMoving(Direction.DOWN);
                        textureRotation = 180;
                        break;
                    }
                }
                if (!onLog) { // if frog didn't land on the log then die
                    startMoving(Direction.DOWN);
                    goingToDrown = true;
                    Audio.playFrogSplashingSound();
                }
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
        } else if (tile.getROW() == 0) {
            startMoving(Direction.DOWN, true);
        }
    }

    /**
     * Method to find the next tile when jumping from log to ground
     * @param rowIndex index of next row
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
     * Method that iterates through every part of log (length)
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
     * Method that defines whether the frog will land on a specific part of log
     * If lands, it also defines the parameters needed for animation and movement of the frog
     * @param log log
     * @param i index of part of log
     * @return true if lands
     */
    private boolean checkIfLandsOnLog(MovingObject log, int i) {

        float coef;

        if (log.getDirection() == Direction.LEFT){
             coef = -0.3f*size;
        } else {
            coef = 0.3f*size;
        }
        if (x + size / 2 >= log.getX() + i * size + coef && x + size / 2 < log.getX() + (i + 1) * size + coef) {
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
     * @param toTheWall is frog moving to the wall
     */
    private void startMoving (Direction direction, boolean toTheWall) {
        startedMovingTime = TimeUtils.nanoTime();
        isMoving = true;
        movingDirection = direction;
        animationFrameCount = 0;
        moveToTheWall = toTheWall;
        moveCamera = !toTheWall;
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
     * Method to animate moving up. Runs every frame. Also changes camera
     */
    public void animateMovingUp(Row[] rows, int nColumns, int nRows) {

        // don't let next move until time passes
        if (TimeUtils.nanoTime() - startedMovingTime > MOVE_TIME) {
            endAnimation();
        }

        // counter of frames (for animation)
        animationFrameCount++;
        textureRotation = 0;

        float dy = 0f;
        float dx = 0f;

        if (moveToTheWall) {
            animateMovingToTheWall();
        } else {
            if (animationFrameCount == (int) SPEED) // check if it's the last animation leap (total is SPEED)
            {
                // when it's the last animation frame just set coordinates to what they have to be
                if (rows[tile.getROW()].getType() != TypeOfRow.LOG) dx = tile.getX() - x;
                else if (!goingToDrown) dx = log.getX() + log.getSize() * logIndex - x;
                else dx = distanceX / SPEED;

                if (!goingToDrown) dy = tile.getY() - y;
                else dy = size / SPEED;

            }
            else if (animationFrameCount < (int) SPEED) // if not the last animation leap
            {
                dy = size / SPEED;
                dx = distanceX / SPEED;
            }

            y += dy;
            x += dx;

            // move camera
            if (tile.getROW() > (nColumns / 2) && tile.getROW() < (nRows - (nColumns / 2)) && moveCamera)
                FroggerGame.gameCamera.translate(0,dy,0);

            animate();
        }
    }

    /**
     * Method to animate moving down. Runs every frame. Also changes camera
     */
    public void animateMovingDown(Row[] rows, int nColumns, int nRows) {

        // don't let next move until time passes
        if (TimeUtils.nanoTime() - startedMovingTime > MOVE_TIME) {
            endAnimation();
        }

        // counter of frames (for animation)
        animationFrameCount++;
        textureRotation = 180;

        float dy = 0f;
        float dx = 0f;


        if (moveToTheWall) {
            animateMovingToTheWall();
        } else {
            if (animationFrameCount == (int) SPEED) // check if it's the last animation leap (total is SPEED)
            {
                // when it's the last animation frame just set coordinates to what they have to be
                if (rows[tile.getROW()].getType() != TypeOfRow.LOG) dx = tile.getX() - x;
                else dx = log.getX() + log.getSize() * logIndex - x;

                if (!goingToDrown) dy = tile.getY() - y;
                else dy = -size / SPEED;


            }
            else if (animationFrameCount < (int) SPEED) // if not the last animation leap
            {
                dy = -size / SPEED;
                dx = distanceX / SPEED;
            }

            x += dx;
            y += dy;

            // move camera
            if (tile.getROW() >= nColumns / 2 && tile.getROW() < (nRows - (nColumns / 2) - 1) && moveCamera)
                FroggerGame.gameCamera.translate(0,dy,0);

            animate();
        }
    }

    /**
     * Method to animate moving right. Runs every frame.
     */
    private void animateMovingRight(Row[] rows) {

        if (TimeUtils.nanoTime() - startedMovingTime > MOVE_TIME) {
            endAnimation();
        }

        animationFrameCount++;
        textureRotation = 270;

        if (moveToTheWall) {
            animateMovingToTheWall();
        } else {
            if (animationFrameCount == (int) SPEED) {
                // when it's the last animation frame just set coordinates to what they have to be
                if (rows[tile.getROW()].getType() != TypeOfRow.LOG) x = tile.getX();
                else if (!goingToDrown) x = log.getX() + log.getSize() * logIndex;
                else x += size / SPEED;

            } else if (animationFrameCount < (int) SPEED) {
                x += size / SPEED;
                if (onLog) {
                    if (log.getDirection() == Direction.RIGHT) {
                        x -= log.getSize() / log.getSpeed();
                    } else {
                        x += log.getSize() / log.getSpeed();
                    }
                }
            }
        }
        animate();
    }

    /**
     * Method to animate moving left. Runs every frame.
     */
    public void animateMovingLeft(Row[] rows) {

        if (TimeUtils.nanoTime() - startedMovingTime > MOVE_TIME) {
            endAnimation();
        }
        animationFrameCount++;
        textureRotation = 90;

        if (moveToTheWall) {
            animateMovingToTheWall();
        } else {
            if (animationFrameCount == (int) SPEED) {
                // when it's the last animation frame just set coordinates to what they have to be
                if (rows[tile.getROW()].getType() != TypeOfRow.LOG) x = tile.getX();
                else if (!goingToDrown) x = log.getX() + log.getSize() * logIndex;
                else x -= size / SPEED;

            } else if (animationFrameCount < (int) SPEED) {
                x -= size / SPEED;
                if (onLog) {
                    if (log.getDirection() == Direction.RIGHT) {
                        x -= log.getSize() / log.getSpeed();
                    } else {
                        x += log.getSize() / log.getSpeed();
                    }
                }
            }
            animate();
        }
    }

    /**
     * Method that runs default texture animation when moving
     */
    private void animate() {
        // animate
        if (!goingToDrown) {
            if (animationFrameCount == (int) SPEED / 4) {
                texture = character.jumping;
            }
            if (TimeUtils.nanoTime() - startedMovingTime > MOVE_ANIMATION_TIME) {
                texture = character.standing;
            }
        } else {
            animateDrowning();
        }
        if (tile.isLily() && animationFrameCount < 2) {
            tile.setSmallLily();
        }
        if (onLog) log.setFlooded(true);
    }

    /**
     * Method that finishes animation after each move
     */
    private void endAnimation() {
        if (onLog) log.setFlooded(false);
        if (goingToDrown) alive = false;
        isMoving = false;
        moveCamera = true;
        if (tile.isLily()) {
            tile.setLily();
        }
    }

    /**
     * Method that animates moving to the wall
     */
    private void animateMovingToTheWall() {
        texture = character.jumping;
        if (animationFrameCount > 2) {
            texture = character.standing;
            moveToTheWall = false;
        }
    }

    /**
     * Method that animates dying
     */
    private void animateDying(){
        texture = character.dead;
        if (TimeUtils.nanoTime() - startedMovingTime > MOVE_TIME) {
            alive = false;
        }
    }

    /**
     * Method that animates drowning
     */
    private void animateDrowning() {

        if (animationFrameCount <= (int) SPEED * 0.25f) {
            texture = character.drowning1;
        }
        else if (animationFrameCount <= (int) SPEED * 0.5f) {
            texture = character.drowning2;
        }
        else if (animationFrameCount <= (int) SPEED * 0.75f) {
            texture = character.drowning3;
        }
        else if (animationFrameCount <= (int) SPEED) {
            texture = character.drowning4;
        }
        else texture = character.drowning5;

    }

    /**
     * Method that animates drowning
     */
    private void drown() {
        animationFrameCount ++;
        if (TimeUtils.nanoTime() - startedMovingTime > MOVE_TIME) {
            alive = false;
        }
        animateDrowning();
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

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public boolean isGoingToDrown() {
        return goingToDrown;
    }

    public boolean isMoving() {
        return isMoving;
    }
}

class CharacterTexture {

    private static final Texture FROG_PACK = new Texture(Gdx.files.internal("characters/frog/frog.png"));
    private static final Texture TURTLE = new Texture(Gdx.files.internal("characters/frog/turtle.png"));
    private static final Texture CAT = new Texture(Gdx.files.internal("characters/frog/cat.png"));
    private static final Texture BIRD = new Texture(Gdx.files.internal("characters/frog/bird.png"));
    private static final Texture EGG = new Texture(Gdx.files.internal("characters/frog/egg.png"));

    private Texture texturePack;

    public TextureRegion standing;
    public TextureRegion jumping;
    public TextureRegion drowning1;
    public TextureRegion drowning2;
    public TextureRegion drowning3;
    public TextureRegion drowning4;
    public TextureRegion drowning5;
    public TextureRegion dead;

    CharacterTexture() {
        setCharacter(Util.Character.FROG);
    }

    public void setCharacter(Util.Character character) {
        if (character == Util.Character.FROG) texturePack = FROG_PACK;
        else if (character == Util.Character.TURTLE) texturePack = TURTLE;
        else if (character == Util.Character.CAT) texturePack = CAT;
        else if (character == Util.Character.BIRD) texturePack = BIRD;
        else if (character == Util.Character.EGG) texturePack = EGG;

        standing = new TextureRegion(texturePack, 300, 150, 150, 150);
        jumping = new TextureRegion(texturePack, 150, 150, 150, 150);
        drowning1 = new TextureRegion(texturePack, 0, 0, 150, 150);
        drowning2 = new TextureRegion(texturePack, 150, 0, 150, 150);
        drowning3 = new TextureRegion(texturePack, 300, 0, 150, 150);
        drowning4 = new TextureRegion(texturePack, 0, 150, 150, 150);
        drowning5 = new TextureRegion(texturePack, 0, 300, 150, 150);
        dead = new TextureRegion(texturePack, 150, 300, 150, 150);
    }

    public static void dispose() {
        FROG_PACK.dispose();
        TURTLE.dispose();
        CAT.dispose();
        BIRD.dispose();
        EGG.dispose();
    }
}
