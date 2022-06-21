package com.frogger.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.frogger.game.*;

import static com.frogger.game.Const.WINDOW_HEIGHT;


public class FroggerGameScreen extends Screen {

    public static Level level;
    private final Timer timer;
    private final Scorer scorer;


    public FroggerGameScreen(FroggerGame game, Level level) {
        super(game);
        FroggerGameScreen.level = level;
        Frog.get().respawn(level.getMap().getTiles()[0][level.getMap().getnColumns() / 2]);


//        float mapWidth = level.getMap().getnColumns() * level.getMap().getTiles()[0][0].getSize();
//        float mapHeight = level.getMap().getnColumns() * level.getMap().getTiles()[0][0].getSize();
        timer = new Timer(level.getMap().getTiles()[0][0].getX(), level.getMap().getTiles()[level.getMap().getnColumns() - 1][0].getY() + 1.5f*level.getMap().getTiles()[0][0].getSize());
        scorer = new Scorer( level.getMap().getTiles()[0][level.getMap().getnColumns() - 1].getX() - 2f * WINDOW_HEIGHT*0.08f, WINDOW_HEIGHT*0.932f, WINDOW_HEIGHT*0.08f);
    }

    @Override
    public void show() {
        super.show();


        float startingY = level.getMap().getTiles()[level.getMap().getnColumns() - 1][0].getY() + 1.5f*level.getMap().getTiles()[0][0].getSize();

        Label levelLabel = new Label("Level " + level.getNumber(), new Label.LabelStyle(fonts.get("36"), Color.BLACK));
        levelLabel.setX(Const.WINDOW_WIDTH  / 2 - levelLabel.getWidth() / 2);
        levelLabel.setY(level.getMap().getTiles()[level.getMap().getnColumns() - 1][0].getY() + 1.5f*level.getMap().getTiles()[0][0].getSize());

        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        buttonStyle.up = skin.getDrawable("green-btn-up");
        buttonStyle.down = skin.getDrawable("green-btn-down");
        buttonStyle.over = skin.getDrawable("green-btn-over");

        Button pauseButton = new Button(buttonStyle);
        float size = 0.9f * (WINDOW_HEIGHT - level.getMap().getTiles()[level.getMap().getnColumns() - 1][0].getY() + 1.5f*level.getMap().getTiles()[0][0].getSize());
        pauseButton.setBounds(level.getMap().getTiles()[0][0].getX(), level.getMap().getTiles()[level.getMap().getnColumns() - 1][0].getY() + 1.5f*level.getMap().getTiles()[0][0].getSize(), size, size);
        pauseButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
            }
        });

        stage.addActor(levelLabel);
        stage.addActor(pauseButton);
    }

    @Override
    public void render(float delta) {
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
        if (Frog.get().getTile().isFinish() && !Frog.get().isMoving() && !isSwitching){
            isSwitching = true;
            switchScreenWithFading(new WinScreen(game, level, timer.getTime()), 1f);
            timer.stop();
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
