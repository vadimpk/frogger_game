package com.frogger.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.frogger.game.FroggerGame;
import com.frogger.game.Level;
import com.frogger.game.LevelsGenerator;
import com.frogger.game.Score;

import static com.frogger.game.Const.*;
import static com.frogger.game.Scorer.FILLED_STAR;
import static com.frogger.game.Scorer.UNFILLED_STAR;
import static com.frogger.game.screens.FroggerGameScreen.level;

public class WinScreen extends Screen{

    private final Level currentLevel;
    private String time;

    public WinScreen(FroggerGame game, Level currentLevel, String time) {
        super(game);
        this.currentLevel = currentLevel;
        this.time = time;

        int starScore = 0;
        for (Score score : currentLevel.getMap().getScores()) if (score.isCollected()) starScore++;
        System.out.println(starScore);
        LevelsGenerator.updateLevel(currentLevel.getNumber() - 1, 100 * starScore, starScore);
    }

    @Override
    public void show() {
        super.show();

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = fonts.get("36");
        textButtonStyle.up = skin.getDrawable("green-btn-up");
        textButtonStyle.down = skin.getDrawable("green-btn-down");
        textButtonStyle.over = skin.getDrawable("green-btn-over");

        Label label = new Label("YOU WIN!", new Label.LabelStyle(fonts.get("100"), Color.BLACK));
        label.setX(WINDOW_WIDTH / 2 - label.getWidth() / 2);
        label.setY(WINDOW_HEIGHT * 0.7f);

        int score = 0;
        for (Score s: level.getMap().getScores()) if (s.isCollected()) score++;
        Image[] stars = new Image[3];
        switch (score) {
            case 0:
                stars[0] = new Image(UNFILLED_STAR);
                stars[1] = new Image(UNFILLED_STAR);
                stars[2] = new Image(UNFILLED_STAR);
                break;
            case 1:
                stars[0] = new Image(FILLED_STAR);
                stars[1] = new Image(UNFILLED_STAR);
                stars[2] = new Image(UNFILLED_STAR);
                break;
            case 2:
                stars[0] = new Image(FILLED_STAR);
                stars[1] = new Image(FILLED_STAR);
                stars[2] = new Image(UNFILLED_STAR);
                break;
            case 3:
                stars[0] = new Image(FILLED_STAR);
                stars[1] = new Image(FILLED_STAR);
                stars[2] = new Image(FILLED_STAR);
                break;
        }
        float size = 0.15f * WINDOW_HEIGHT;
        float distance = 0.05f * WINDOW_HEIGHT;
        stars[0].setBounds(WINDOW_WIDTH / 2 - 1.5f*size - distance, 0.55f*WINDOW_HEIGHT, size, size);
        stars[1].setBounds(WINDOW_WIDTH / 2 - 0.5f*size, 0.57f*WINDOW_HEIGHT, size, size);
        stars[2].setBounds(WINDOW_WIDTH / 2 + 0.5f*size + distance, 0.55f*WINDOW_HEIGHT, size, size);

        Label.LabelStyle labelStyle = new Label.LabelStyle(fonts.get("36"), Color.BLACK);
        Label yourScore = new Label("Your Score: " , labelStyle);
        Label bestScore = new Label("The Best Score: " + currentLevel.getBestScore(), labelStyle);
        Label timeLabel = new Label("Time: " + time, labelStyle);
        yourScore.setX(WINDOW_WIDTH / 2 - yourScore.getWidth() / 2);
        yourScore.setY(WINDOW_HEIGHT * 0.54f - yourScore.getHeight());
        bestScore.setX(WINDOW_WIDTH / 2 - bestScore.getWidth() / 2);
        bestScore.setY(WINDOW_HEIGHT * 0.52f - 2f*bestScore.getHeight());
        timeLabel.setX(WINDOW_WIDTH / 2 - timeLabel.getWidth() / 2);
        timeLabel.setY(WINDOW_HEIGHT * 0.5f - 3f*bestScore.getHeight());

        float distanceX = 0.1f * WINDOW_WIDTH;
        float startingX = (WINDOW_WIDTH - 2 * BUTTON_WIDTH - distanceX) / 2;
        TextButton backButton = new TextButton("Back", textButtonStyle);
        backButton.setBounds(startingX, 0.9f*timeLabel.getY() - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
        TextButton restartButton = new TextButton("Restart", textButtonStyle);
        restartButton.setBounds(startingX + BUTTON_WIDTH + distanceX, 0.9f*timeLabel.getY() - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);

        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (Score score : currentLevel.getMap().getScores()) {
                    score.setUncollected();
                }
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

        stage.addActor(stars[0]);
        stage.addActor(stars[1]);
        stage.addActor(stars[2]);
        stage.addActor(label);
        stage.addActor(yourScore);
        stage.addActor(bestScore);
        stage.addActor(timeLabel);
    }

}
