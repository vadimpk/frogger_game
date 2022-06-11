package com.frogger.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.frogger.game.*;

public class FroggerGameScreen extends ScreenAdapter {

    public static Level level;
    private FroggerGame game;
    private Timer timer;
    private Scorer scorer;
    private int lives;


    public FroggerGameScreen(FroggerGame game, Level level) {
        this.game = game;
        FroggerGameScreen.level = level;
        Frog.get().respawn(level.getMap().getTiles()[0][level.getMap().getnColumns() / 2]);

        timer = new Timer(level.getMap().getTiles()[0][0].getX(), level.getMap().getTiles()[level.getMap().getnColumns()][0].getY() + level.getMap().getTiles()[0][0].getSize() / 1.5f);
        scorer = new Scorer( level.getMap().getTiles()[0][level.getMap().getnColumns() - 1].getX() - 2f * level.getMap().getTiles()[0][0].getSize(), level.getMap().getTiles()[level.getMap().getnColumns()][0].getY(), level.getMap().getTiles()[0][0].getSize()*0.8f);
    }

    @Override
    public void render(float delta) {
        level.render(delta);

        FroggerGame.attributesBatch.begin();
        timer.render(delta, FroggerGame.attributesBatch);
        scorer.render(FroggerGame.attributesBatch);
        FroggerGame.attributesBatch.end();

        if (!Frog.get().isAlive()) {
            ((Game)Gdx.app.getApplicationListener()).setScreen(new DieScreen(game, level));
        }
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        super.dispose();
        timer.dispose();
        scorer.dispose();
    }
}
