package com.frogger.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.frogger.game.*;

import static com.frogger.game.Const.WINDOW_HEIGHT;


public class FroggerGameScreen extends Screen {

    public static Level level;
    private final Timer timer;
    private final Scorer scorer;
    public static boolean isPaused;


    public FroggerGameScreen(FroggerGame game, Level level) {
        super(game);
        FroggerGameScreen.level = level;
        Frog.get().respawn(level.getMap().getTiles()[0][level.getMap().getnColumns() / 2]);


//        float mapWidth = level.getMap().getnColumns() * level.getMap().getTiles()[0][0].getSize();
//        float mapHeight = level.getMap().getnColumns() * level.getMap().getTiles()[0][0].getSize();
        timer = new Timer(level.getMap().getTiles()[0][0].getX(), level.getMap().getTiles()[level.getMap().getnColumns() - 1][0].getY() + 1.5f*level.getMap().getTiles()[0][0].getSize());
        scorer = new Scorer( level.getMap().getTiles()[0][level.getMap().getnColumns() - 1].getX() - 2f * WINDOW_HEIGHT*0.08f, WINDOW_HEIGHT*0.932f, WINDOW_HEIGHT*0.08f);

        isPaused = false;
    }

    @Override
    public void show() {
        super.show();


        float startingY = level.getMap().getTiles()[level.getMap().getnColumns() - 1][0].getY() + level.getMap().getTiles()[0][0].getSize();

        Label levelLabel = new Label("Level " + level.getNumber(), new Label.LabelStyle(fonts.get("36"), Color.BLACK));
        levelLabel.setX(Const.WINDOW_WIDTH  / 2 - levelLabel.getWidth() / 2);
        levelLabel.setY(startingY);

        stage.addActor(levelLabel);
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
            level.render(delta);

            FroggerGame.attributesBatch.begin();
            timer.render(delta, FroggerGame.attributesBatch);
            scorer.render(FroggerGame.attributesBatch);
            FroggerGame.attributesBatch.end();

            if (!Frog.get().isAlive() && !isSwitching) {
                isSwitching = true;
                switchScreenWithFading(new DieScreen(game, level), 3f);
                timer.stop();
            }
            if (Frog.get().getTile().isFinish() && !Frog.get().isMoving() && !isSwitching) {
                isSwitching = true;
                switchScreenWithFading(new WinScreen(game, level, timer.getTime()), 1f);
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
