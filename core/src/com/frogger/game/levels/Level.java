package com.frogger.game.levels;

import com.frogger.game.mapObjects.Map;

/**
 * Level.java
 * @author stas-bukovskiy
 * Class saves level information such as map, time, level number, star and best score and level time
 */
public class Level{
    private final int number;
    private int bestScore;
    private int starScore;
    private final int time;
    private final Map map;
    private boolean isBlocked;
    private boolean isPassed;

    /**
     * @param number - level number
     * @param bestScore - best score that player reached
     * @param starScore - number of collected star
     * @param map - map of level
     * @param time - level time
     */
    public Level(int number, int bestScore, int starScore, Map map, int time) {
        this.number = number;
        this.bestScore = bestScore;
        this.starScore = starScore;
        this.map = map;
        this.time = time;
        isBlocked = true;
        isPassed = false;
    }

    /**
     * Map getter
     * @return level map
     */
    public Map getMap() {
        return map;
    }

    /**
     * Level number getter
     * @return level number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Best score getter
     * @return best score
     */
    public int getBestScore() {
        return bestScore;
    }

    /**
     * Best score setter
     * @param bestScore - new best score
     */
    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    /**
     * Method returns number of collected stars
     * @return number of collected stars
     */
    public int getStarScore() {
        return starScore;
    }

    /**
     * Method sets new number of collected stars
     * @param starScore - new number of collected stars
     */
    public void setStarScore(int starScore) {
        this.starScore = starScore;
    }

    /**
     * Method invokes map rendering
     */
    public void render() {
        map.render();
    }

    /**
     * Method invokes pause map rendering
     */
    public void pausedRender() {
        map.pausedRender();
    }

    /**
     * Time getter
     * @return time
     */
    public int getTime() {
        return time;
    }

    /**
     * Method returns true if level is blocked otherwise false
     * @return if level is blocked otherwise false
     */
    public boolean isBlocked() {
        return isBlocked;
    }

    /**
     * Method sets if level is blocked
     * @param blocked - if level is blocked otherwise false
     */
    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    /**
     * Method returns true if level has already passed otherwise false
     * @return true if level has already passed otherwise false
     */
    public boolean isPassed() {
        return isPassed;
    }

    /**
     * Method sets true if level has already passed otherwise false
     * @param passed - true if level has already passed otherwise false
     */
    public void setPassed(boolean passed) {
        isPassed = passed;
    }
}
