package com.frogger.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.frogger.game.*;
import com.frogger.game.attributeObjects.Scorer;
import com.frogger.game.attributeObjects.Timer;
import com.frogger.game.gameObjects.Frog;
import com.frogger.game.levels.Level;
import com.frogger.game.utils.Const;

import static com.frogger.game.utils.Const.WINDOW_HEIGHT;


public class FroggerGameScreen extends Screen {

    public static Level level;
    private final Timer timer;
    private final Scorer scorer;
    public static boolean isPaused;


    public FroggerGameScreen(FroggerGame game, Level level) {
        super(game);
        FroggerGameScreen.level = level;
        Frog.get().respawn(level.getMap().getTiles()[0][level.getMap().getnColumns() / 2]);
        FroggerGame.gameCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        timer = new Timer((float) (level.getMap().getTiles()[0][0].getX() + 0.1 * level.getMap().getTiles()[0][0].getSize()),
                level.getMap().getTiles()[level.getMap().getnColumns() - 1][0].getY() + level.getMap().getTiles()[0][0].getSize(),
                level.getTime(), stage);

        scorer = new Scorer(level.getMap().getTiles()[0][level.getMap().getnColumns() - 1].getX() - 2f * WINDOW_HEIGHT*0.08f, WINDOW_HEIGHT*0.932f, WINDOW_HEIGHT*0.08f);
        isPaused = false;
    }

    @Override
    public void show() {
        super.show();

        float startingY = level.getMap().getTiles()[level.getMap().getnColumns() - 1][0].getY() + level.getMap().getTiles()[0][0].getSize();

        Label levelLabel = new Label("Level " + level.getNumber(), new Label.LabelStyle(fonts.get("36"), Color.BLACK));
        levelLabel.setX(Const.WINDOW_WIDTH / 2 - levelLabel.getWidth() / 2);
        levelLabel.setY(startingY);

        stage.addActor(levelLabel);
        timer.show();
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isPaused = !isPaused;
            if(timer.isStopped()) timer.start();
            else timer.stop();
            switchScreenWithFading(new PauseScreen(game, this), 0.1f);
        }

        if(isPaused){
            level.pausedRender();

            FroggerGame.attributesBatch.begin();
            timer.render(delta, FroggerGame.attributesBatch);
            scorer.render(FroggerGame.attributesBatch);
            FroggerGame.attributesBatch.end();
        }else {
            level.render();

            FroggerGame.attributesBatch.begin();
            timer.render(delta, FroggerGame.attributesBatch);
            scorer.render(FroggerGame.attributesBatch);
            FroggerGame.attributesBatch.end();

            if (!Frog.get().isAlive() && !isSwitching) {
                isSwitching = true;
                switchScreenWithFading(new GameOverScreen(game, level, timer.getTimer(), false), 2f);
                timer.stop();
            }
            if (Frog.get().getTile().isFinish() && !Frog.get().isMoving() && !isSwitching) {
                isSwitching = true;
                switchScreenWithFading(new GameOverScreen(game, level, timer.getTimer(), true), 1f);
                timer.stop();
            }

            if (timer.getTimer() == 0 && !isSwitching) {
                isSwitching = true;
                switchScreenWithFading(new GameOverScreen(game, level, timer.getTimer(), false), 2f);
                isPaused = !isPaused;
                timer.stop();

            }
        }

        stage.act();
        stage.draw();
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
