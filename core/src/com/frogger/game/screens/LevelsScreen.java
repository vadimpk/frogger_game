package com.frogger.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.frogger.game.Audio;
import com.frogger.game.FroggerGame;
import com.frogger.game.Level;
import com.frogger.game.LevelsGenerator;

import static com.frogger.game.Const.*;

public class LevelsScreen extends Screen {

    public LevelsScreen(FroggerGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        final Level[] levels = LevelsGenerator.getLevels();

        initButtons();

        TextButton[] buttons = new TextButton[levels.length];
        float distanceX = 0.08f*WINDOW_WIDTH;
        float buttonSize = 0.15f * WINDOW_HEIGHT;
        float startingX = WINDOW_WIDTH * 0.5f - 2.5f*buttonSize - 2f*distanceX;
        for (int i = 0; i < levels.length; i++) {

            buttons[i] = new TextButton(String.valueOf(i + 1), textButtonStyles.get(String.valueOf(levels[i].getStarScore())));

            if (i <= 4)
                buttons[i].setBounds(startingX + (distanceX + buttonSize) * i, 0.7f * WINDOW_HEIGHT - 0.5f * buttonSize, buttonSize, buttonSize);
            else
                buttons[i].setBounds(startingX + (distanceX + buttonSize) * (i - 5), 0.7f * WINDOW_HEIGHT - 2f * buttonSize, buttonSize, buttonSize);
            final int finalI = i;
            buttons[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Audio.playClickedSound();
                    switchScreenWithFading(new FroggerGameScreen(game, levels[finalI]), 0.3f);
                }
            });
            stage.addActor(buttons[i]);
        }

        createMenuButtons();
        TextButton backButton = new TextButton("Back", textButtonStyles.get("red"));
        backButton.setBounds(WINDOW_WIDTH / 2 - BUTTON_WIDTH / 2, 0.15f*WINDOW_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Audio.playClickedSound();
                switchScreenWithFading(new MainMenuScreen(game), 0.3f);
            }
        });

        stage.addActor(backButton);
    }

    private void initButtons() {
        textButtonStyles.put("0", new TextButton.TextButtonStyle());
        textButtonStyles.put("1", new TextButton.TextButtonStyle());
        textButtonStyles.put("2", new TextButton.TextButtonStyle());
        textButtonStyles.put("3", new TextButton.TextButtonStyle());
        for (String key : new String[]{"0", "1", "2", "3"}) {
            TextButton.TextButtonStyle buttonStyle = textButtonStyles.get(key);
            buttonStyle.font = fonts.get("36");
            buttonStyle.up = skin.getDrawable("level-btn-up-" + key + "-stars");
            buttonStyle.down = skin.getDrawable("level-btn-down-" + key + "-stars");
            buttonStyle.over = skin.getDrawable("level-btn-over-" + key + "-stars");
        }
    }
}
