package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;
import com.frogger.game.Util.Direction;
import com.frogger.game.Util.TypeOfRow;
import com.frogger.game.objects.Car;
import com.frogger.game.objects.MovingObject;
import com.frogger.game.screens.FroggerGameScreen;

import java.util.Objects;

public class Frog {

    private static Frog instance;
    
    /** initialize frog attributes */
    private Tile tile;
    private float x,y,size;
    private boolean alive;
    private TextureRegion texture;
    private int textureRotation;

    private static final Texture FROG_PACK = new Texture(Gdx.files.internal("characters/frog/frog.png"));


    private static final TextureRegion FROG_LOOKING_UP = new TextureRegion(FROG_PACK, 300, 150, 150, 150);
    private static final TextureRegion FROG_JUMPING_UP = new TextureRegion(FROG_PACK, 150, 150, 150, 150);

    private static final TextureRegion FROG_DROWNING_UP_1 = new TextureRegion(FROG_PACK, 0, 0, 150, 150);
    private static final TextureRegion FROG_DROWNING_UP_2 = new TextureRegion(FROG_PACK, 150, 0, 150, 150);
    private static final TextureRegion FROG_DROWNING_UP_3 = new TextureRegion(FROG_PACK, 300, 0, 150, 150);
    private static final TextureRegion FROG_DROWNING_UP_4 = new TextureRegion(FROG_PACK, 0, 150, 150, 150);
    private static final TextureRegion FROG_DROWNING_UP_5 = new TextureRegion(FROG_PACK, 0, 300, 150, 150);
    private static final TextureRegion FROG_DEAD = new TextureRegion(FROG_PACK, 150, 300, 150, 150);

    private Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/frog-jumping.mp3"));
    private Sound sound2 = Gdx.audio.newSound(Gdx.files.internal("sounds/splash.mp3"));
    private Sound sound4 = Gdx.audio.newSound(Gdx.files.internal("sounds/dead.mp3"));
    private Sound sound5 = Gdx.audio.newSound(Gdx.files.internal("sounds/log.mp3"));


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
    private boolean soundPlaying = false;
    private boolean dyingSoundPlaying = false;

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
        texture = FROG_LOOKING_UP;
        textureRotation = 0;
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
        texture = FROG_LOOKING_UP;
        onLog = false;
        log = null;
        logIndex = -1;
        isMoving = false;
        moveToTheWall = false;
        goingToDrown = false;
        goingToDie = false;
        dyingSoundPlaying = false;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, size/2, size/2 , size, size, 1, 1, textureRotation);
        update(0f);
    }

    public static void dispose() {
        FROG_PACK.dispose();
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


            if (tile.getROW() == nRows - 1 && !isMoving) {
                isMoving = true;
                movingDirection = Direction.NONE;
                Car.stopSound();
            }


            // check for collision with car or train
            if (rows[tile.getROW()].getType() == TypeOfRow.CAR || rows[tile.getROW()].getType() == TypeOfRow.TRAIN) {
                for (MovingObject carOrTrain : rows[tile.getROW()].getMovingObjects()) {
                    if (carOrTrain.checkCollision(this)) {
                        if (!dyingSoundPlaying) {
                            sound4.play(1.0f);
                            dyingSoundPlaying = true;
                            Car.stopSound();
                        }
                        if (!goingToDie) {
                            goingToDie = true;
                            startedMovingTime = TimeUtils.nanoTime();
                        }
                        animateDying();
                    }
                }
            }


            if (tile.getROW() >= 1 && rows.length - tile.getROW() >= 2) {
                if (rows[tile.getROW() + 1].getType() == TypeOfRow.CAR ||
                        rows[tile.getROW()].getType() == TypeOfRow.CAR ||
                        rows[tile.getROW() - 1].getType() == TypeOfRow.CAR && alive) {
                    Car.playSound();
                } else Car.stopSound();
            } else Car.stopSound();

            if (rows[tile.getROW()].getType() == TypeOfRow.LILY) {
                if (!tile.isSafe()) {
                    goingToDrown = true;
                    if (!soundPlaying) {
                        sound2.play(1.0f);
                        soundPlaying = true;
                    }
                }
            }

            // if frog is on a log then move it with log speed (and check if not behind screen)
            if (onLog) {
                if (!log.isSafe() && !isMoving) {
                    if (!goingToDrown) {
                        startedMovingTime = TimeUtils.nanoTime();
                        animationFrameCount = 0;
                        goingToDrown = true;
                        sound2.play(1.0f);
                        soundPlaying = true;
                    }

                    drown();
                }
                if (x < tiles[0][0].getX() - tiles[0][0].getSize() * 0.5f || x > tiles[0][nColumns - 1].getX() + tiles[0][0].getSize() * 0.5f && !isMoving) {
                    alive = false;
                    if (!dyingSoundPlaying) {
                        sound4.play(1.0f);
                        dyingSoundPlaying = true;
                        Car.stopSound();
                    }
                }
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

                    if (!soundPlaying) {
                        sound.play(0.4f);
                        soundPlaying = true;
                        if (onLog) {
                            sound5.play(1.0f);
                        }
                    }

                } else {
                    soundPlaying = false;

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

        textureRotation = 270;
        // if not in the last column
        if (tile.getCOLUMN() < nColumns - 1) {

            // if on a log then check if won't land in water (if yes then die)
            if (onLog) {
                if (logIndex == log.getLength() - 1) {
                    onLog = false;
                    goingToDrown = true;
                    sound2.play(1.0f);
                    soundPlaying = true;
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
    private void moveLeft(Tile[][] tiles) {

        textureRotation = 90;
        // if not in the first column
        if (tile.getCOLUMN() > 0) {

            // if on a log then check if won't land in water (if yes then die)
            if (onLog) {
                if (logIndex == 0) {
                    onLog = false;
                    goingToDrown = true;
                    sound2.play(1.0f);
                    soundPlaying = true;
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
    private void moveUp(Row[] rows, Tile[][] tiles, int nRows) {

        textureRotation = 0;
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
                        textureRotation = 0;
                        break; // stop iterating as the log is already found
                    }
                }
                if (!onLog) { // if frog didn't land on the log then die
                    startMoving(Direction.UP);
                    goingToDrown = true;
                    sound2.play(1.0f);
                    soundPlaying = true;
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

        textureRotation = 180;
        // if not in the first row
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
                    sound2.play(1.0f);
                    soundPlaying = true;
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
    public void animateMovingUp(Row[] rows, int nColumns, int nRows) {

        // don't let next move until time passes
        if (TimeUtils.nanoTime() - startedMovingTime > MOVE_TIME) {
            isMoving = false; // when time passes end moving
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
                if (onLog) log.setFlooded(true);
            }

            y += dy;
            x += dx;

            // move camera
            if (tile.getROW() > (nColumns / 2) && tile.getROW() < (nRows - (nColumns / 2)))
                FroggerGame.gameCamera.translate(0,dy,0);

            animate();
        }
    }

    /**
     * Method to move down. Runs every frame
     */
    public void animateMovingDown(Row[] rows, int nColumns, int nRows) {

        // don't let next move until time passes
        if (TimeUtils.nanoTime() - startedMovingTime > MOVE_TIME) {
            isMoving = false; // when time passes end moving
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
                if (onLog) log.setFlooded(true);
            }

            x += dx;
            y += dy;

            // move camera
            if (tile.getROW() >= nColumns / 2 && tile.getROW() < (nRows - (nColumns / 2) - 1))
                FroggerGame.gameCamera.translate(0,dy,0);

            animate();
        }
    }

    /**
     * Method to move right. Runs every frame
     */
    private void animateMovingRight(Row[] rows) {

        if (TimeUtils.nanoTime() - startedMovingTime > MOVE_TIME) {
            isMoving = false;
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
                    log.setFlooded(true);
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
     * Method to move left. Runs every frame
     */
    public void animateMovingLeft(Row[] rows) {

        if (TimeUtils.nanoTime() - startedMovingTime > MOVE_TIME) {
            isMoving = false;
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
                    log.setFlooded(true);
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

    private void animate() {
        // animate
        if (!goingToDrown) {
            if (animationFrameCount == (int) SPEED / 4) {
                texture = FROG_JUMPING_UP;
            }
            if (TimeUtils.nanoTime() - startedMovingTime > MOVE_ANIMATION_TIME) {
                texture = FROG_LOOKING_UP;
            }
        } else {
            animateDrowning();
        }
        if (tile.isLily() && animationFrameCount < 2) {
            tile.setSmallLily();
        }
    }

    private void endAnimation() {
        if (onLog) log.setFlooded(false);
        if (goingToDrown) alive = false;
        if (tile.isLily()) {
            tile.setLily();
        }
    }

    private void animateMovingToTheWall() {
        texture = FROG_JUMPING_UP;
        if (animationFrameCount > 2) {
            texture = FROG_LOOKING_UP;
            moveToTheWall = false;
        }
    }


    private void animateDying(){
        texture = FROG_DEAD;
        if (TimeUtils.nanoTime() - startedMovingTime > MOVE_TIME) {
            alive = false;
        }
    }

    private void animateDrowning() {
        if (animationFrameCount <= (int) SPEED * 0.25f) {
            texture = FROG_DROWNING_UP_1;
        }
        else if (animationFrameCount <= (int) SPEED * 0.5f) {
            texture = FROG_DROWNING_UP_2;
        }
        else if (animationFrameCount <= (int) SPEED * 0.75f) {
            texture = FROG_DROWNING_UP_3;
        }
        else if (animationFrameCount <= (int) SPEED) {
            texture = FROG_DROWNING_UP_4;
        }
        else texture = FROG_DROWNING_UP_5;
    }

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

    public void setAlive(boolean alive) {this.alive = alive;}

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

