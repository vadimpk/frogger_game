package com.frogger.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.frogger.game.*;

import static com.frogger.game.Const.*;
import static com.frogger.game.Scorer.FILLED_STAR;
import static com.frogger.game.Scorer.UNFILLED_STAR;
import static com.frogger.game.screens.FroggerGameScreen.level;

public class GameOverScreen extends Screen{

    private final Level currentLevel;
    private final float timer;
    private final int score;
    private final boolean isWon;

    private static Sound clickedSound = Gdx.audio.newSound(Gdx.files.internal("sounds/click-sound.mp3"));
    private static boolean soundPlaying = false;

    public GameOverScreen(FroggerGame game, Level currentLevel, float timer, boolean isWon) {
        super(game);
        this.currentLevel = currentLevel;
        this.timer = timer;
        this.isWon = isWon;

        int starScore = 0;
        for (Score score : currentLevel.getMap().getScores()) if (score.isCollected()) starScore++;
        if(currentLevel.isBig()) {
            score = (int) (329 * starScore + 456 * (1 - (timer / 330)));
            if(isWon) LevelsGenerator.updateBigLevel(score);
        }else {
            score = (int) (((isWon) ? 2 : 1) * (329 * starScore + 456 * (timer / currentLevel.getTime())));
            if(isWon) LevelsGenerator.updateLevel(currentLevel.getNumber() - 1, score, starScore);
        }
    }

    @Override
    public void show() {
        super.show();
        soundPlaying = false;

        createMenuButtons();

        Label label = new Label(isWon ? "You Won" : "Game Over", new Label.LabelStyle(fonts.get("100"), Color.BLACK));
        label.setX(WINDOW_WIDTH / 2 - label.getWidth() / 2);
        label.setY(WINDOW_HEIGHT * 0.7f);

        float deltaY = 0f;
        if(!currentLevel.isBig() && isWon) {
            int starScore = 0;
            for (Score s : level.getMap().getScores()) if (s.isCollected()) starScore++;
            Image[] stars = new Image[3];
            switch (starScore) {
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
            stars[0].setBounds(WINDOW_WIDTH / 2 - 1.5f * size - distance, 0.55f * WINDOW_HEIGHT, size, size);
            stars[1].setBounds(WINDOW_WIDTH / 2 - 0.5f * size, 0.57f * WINDOW_HEIGHT, size, size);
            stars[2].setBounds(WINDOW_WIDTH / 2 + 0.5f * size + distance, 0.55f * WINDOW_HEIGHT, size, size);

            stage.addActor(stars[0]);
            stage.addActor(stars[1]);
            stage.addActor(stars[2]);

            deltaY = -0.06f;
        }

        Label.LabelStyle labelStyle = new Label.LabelStyle(fonts.get("36"), Color.BLACK);
        Label levelLabel = null;
        if (!currentLevel.isBig()) {
            levelLabel = new Label("Level " + currentLevel.getNumber(), labelStyle);
            levelLabel.setX(WINDOW_WIDTH / 2 - levelLabel.getWidth() / 2);
            levelLabel.setY(label.getY() + label.getHeight() + 0.05f* WINDOW_HEIGHT);
        }
        Label yourScore = new Label("Your Score: " + score, labelStyle);
        Label bestScore = new Label("The Best Score: " + currentLevel.getBestScore(), labelStyle);
        Label timeLabel = new Label("Time: " + Timer.convert((currentLevel.isBig() ? timer: level.getTime() - timer)) , labelStyle);
        yourScore.setX(WINDOW_WIDTH / 2 - yourScore.getWidth() / 2);
        yourScore.setY(WINDOW_HEIGHT * (0.6f + deltaY) - yourScore.getHeight());
        bestScore.setX(WINDOW_WIDTH / 2 - bestScore.getWidth() / 2);
        bestScore.setY(WINDOW_HEIGHT * (0.58f + deltaY) - 2f*bestScore.getHeight());
        timeLabel.setX(WINDOW_WIDTH / 2 - timeLabel.getWidth() / 2);
        timeLabel.setY(WINDOW_HEIGHT * (0.56f + deltaY) - 3f*bestScore.getHeight());

        float distanceX = 0.1f * WINDOW_WIDTH;
        float startingX = (WINDOW_WIDTH - 2 * BUTTON_WIDTH - distanceX) / 2;
        TextButton backButton = new TextButton("Back", textButtonStyles.get("red"));
        TextButton restartButton = new TextButton("Restart", textButtonStyles.get("yellow"));
        restartButton.setBounds(startingX, 0.9f*timeLabel.getY() - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
        backButton.setBounds(startingX + BUTTON_WIDTH + distanceX, 0.9f*timeLabel.getY() - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);

        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (Score score : currentLevel.getMap().getScores()) {
                    score.setUncollected();
                }
                switchScreenWithFading(new MainMenuScreen(game), 0.3f);
                if (!soundPlaying) {
                    clickedSound.play(1.0f);
                    soundPlaying = true;
                }
            }
        });
        restartButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchScreenWithFading(new FroggerGameScreen(game, currentLevel), 0.3f);
                for (Score score : currentLevel.getMap().getScores()) {
                    score.setUncollected();
                }
                if (!soundPlaying) {
                    clickedSound.play(1.0f);
                    soundPlaying = true;
                }
            }
        });

        stage.addActor(restartButton);
        stage.addActor(backButton);

        if(!currentLevel.isBig()) stage.addActor(levelLabel);
        stage.addActor(label);
        stage.addActor(yourScore);
        stage.addActor(bestScore);
        stage.addActor(timeLabel);
    }
}