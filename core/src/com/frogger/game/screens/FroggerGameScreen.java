package com.frogger.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.frogger.game.*;

public class FroggerGameScreen extends Screen {

    public static Level level;
    private FroggerGame game;
    private Timer timer;
    private Scorer scorer;


    public FroggerGameScreen(FroggerGame game, Level level) {
        super(game);
        this.game = game;
        FroggerGameScreen.level = level;
        Frog.get().respawn(level.getMap().getTiles()[0][level.getMap().getnColumns() / 2]);


        float mapWidth = level.getMap().getnColumns() * level.getMap().getTiles()[0][0].getSize();
        float mapHeight = level.getMap().getnColumns() * level.getMap().getTiles()[0][0].getSize();
        timer = new Timer(level.getMap().getTiles()[0][0].getX(), level.getMap().getTiles()[level.getMap().getnColumns() - 1][0].getY() + 1.5f*level.getMap().getTiles()[0][0].getSize());
        scorer = new Scorer( level.getMap().getTiles()[0][level.getMap().getnColumns() - 1].getX() - 2f * Const.WINDOW_HEIGHT*0.08f, Const.WINDOW_HEIGHT*0.932f, Const.WINDOW_HEIGHT*0.08f);
    }

    @Override
    public void show() {

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
        if (Frog.get().getTile().isFinish() && !Frog.get().isMoving()){
            ((Game)Gdx.app.getApplicationListener()).setScreen(new WinScreen(game, level, timer.getTime()));
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
        Scorer.dispose();
    }
}
