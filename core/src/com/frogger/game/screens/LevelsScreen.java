package com.frogger.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.frogger.game.FroggerGame;
import com.frogger.game.Level;
import com.frogger.game.LevelsGenerator;

import java.util.HashMap;
import java.util.Map;

import static com.frogger.game.Const.WINDOW_HEIGHT;
import static com.frogger.game.Const.WINDOW_WIDTH;

public class LevelsScreen extends Screen {

    private final Map<String,  TextButton.TextButtonStyle> buttonStyles;
    private static Sound clickedSound = Gdx.audio.newSound(Gdx.files.internal("sounds/click-sound.mp3"));
    private static boolean soundPlaying = false;

    public LevelsScreen(FroggerGame game) {
        super(game);
        buttonStyles = new HashMap<>();
    }

    @Override
    public void show() {
        super.show();

        final Level[] levels = LevelsGenerator.getLevels();

        initButtons();

        TextButton[] buttons = new TextButton[levels.length];
        float distanceX = 0.8f * WINDOW_WIDTH / ((float)levels.length / 2);
        for (int i = 0; i < levels.length; i++) {

            buttons[i] = new TextButton(String.valueOf(i + 1), buttonStyles.get(String.valueOf(levels[i].getStarScore())));

            float button_size = 0.15f * WINDOW_HEIGHT;
            if (i <= 4) buttons[i].setBounds(0.1f * WINDOW_WIDTH + distanceX * i, 0.65f * WINDOW_HEIGHT - 0.5f*button_size, button_size, button_size);
            else buttons[i].setBounds(0.1f * WINDOW_WIDTH + distanceX * (i - 5), 0.65f * WINDOW_HEIGHT - 2f*button_size, button_size, button_size);
            final int finalI = i;
            buttons[i].addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    switchScreenWithFading(new FroggerGameScreen(game, levels[finalI]), 0.3f);
                    if (!soundPlaying) {
                        clickedSound.play(1.0f);
                        soundPlaying = true;
                    }
                }
            });
            stage.addActor(buttons[i]);
        }
    }

    private void initButtons() {
        soundPlaying = false;
        buttonStyles.put("0", new TextButton.TextButtonStyle());
        buttonStyles.put("1", new TextButton.TextButtonStyle());
        buttonStyles.put("2", new TextButton.TextButtonStyle());
        buttonStyles.put("3", new TextButton.TextButtonStyle());
        for (String key : buttonStyles.keySet()) {
            TextButton.TextButtonStyle buttonStyle = buttonStyles.get(key);
            buttonStyle.font = fonts.get("36");
            buttonStyle.up = skin.getDrawable("level-btn-up-" + key + "-stars");
            buttonStyle.down = skin.getDrawable("level-btn-down-" + key + "-stars");
            buttonStyle.over = skin.getDrawable("level-btn-over-" + key + "-stars");
        }
    }
}
