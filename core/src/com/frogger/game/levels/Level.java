package com.frogger.game.levels;

import com.frogger.game.mapObjects.Map;

public class Level{
    private int number;
    private int bestScore;
    private int starScore;
    private int time;
    private Map map;
    private boolean isBlocked;
    private boolean isPassed;

    public Level(int number, int bestScore, int starScore, Map map, int time) {
        this.number = number;
        this.bestScore = bestScore;
        this.starScore = starScore;
        this.map = map;
        this.time = time;
        isBlocked = true;
        isPassed = false;
    }

    public Map getMap() {
        return map;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    public int getStarScore() {
        return starScore;
    }

    public void setStarScore(int starScore) {
        this.starScore = starScore;
    }


    public void setMap(Map map) {
        this.map = map;
    }

    public void render() {
        map.render();
    }

    public void pausedRender() {
        map.pausedRender();
    }

    public int getTime() {
        return time;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public void setPassed(boolean passed) {
        isPassed = passed;
    }
}
