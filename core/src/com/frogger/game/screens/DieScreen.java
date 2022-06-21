package com.frogger.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.frogger.game.FroggerGame;
import com.frogger.game.Level;
import com.frogger.game.Score;

import static com.frogger.game.Const.*;

public class DieScreen extends Screen{

    private final Level currentLevel;


    public DieScreen(FroggerGame game, Level currentLevel) {
        super(game);
        this.currentLevel = currentLevel;
    }

    @Override
    public void show() {
       super.show();

        Label gameOver = new Label("Game Over", new Label.LabelStyle(fonts.get("100"), Color.BLACK));
        gameOver.setX(WINDOW_WIDTH / 2 - gameOver.getWidth() / 2);
        gameOver.setY(WINDOW_HEIGHT * 0.7f);
        gameOver.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(2f)));
        stage.addActor(gameOver);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = fonts.get("36");
        textButtonStyle.up = skin.getDrawable("green-btn-up");
        textButtonStyle.down = skin.getDrawable("green-btn-down");
        textButtonStyle.over = skin.getDrawable("green-btn-over");

        float distanceX = 0.1f * WINDOW_WIDTH;
        float startingX = (WINDOW_WIDTH - 2 * BUTTON_WIDTH - distanceX) / 2;
        TextButton backButton = new TextButton("Back", textButtonStyle);
        backButton.setBounds(startingX, WINDOW_HEIGHT / 2 - BUTTON_HEIGHT / 2, BUTTON_WIDTH, BUTTON_HEIGHT);
        TextButton restartButton = new TextButton("Restart", textButtonStyle);
        restartButton.setBounds(startingX + BUTTON_WIDTH + distanceX, WINDOW_HEIGHT / 2 - BUTTON_HEIGHT / 2, BUTTON_WIDTH, BUTTON_HEIGHT);

        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchScreenWithFading(new MainMenuScreen(game), 0.3f);
            }
        });
        restartButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchScreenWithFading(new FroggerGameScreen(game, currentLevel), 0.3f);
                for (Score score : currentLevel.getMap().getScores()) {
                    score.setUncollected();
                }
                FroggerGame.gameCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }
        });

        stage.addActor(restartButton);
        stage.addActor(backButton);
    }
}
