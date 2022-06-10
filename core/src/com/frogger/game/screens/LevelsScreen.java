package com.frogger.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.frogger.game.FroggerGame;
import com.frogger.game.Level;
import com.frogger.game.LevelsGenerator;
import com.frogger.game.Map;

import static com.frogger.game.Const.*;

public class LevelsScreen extends Screen {

    public LevelsScreen(FroggerGame game) {
        super(game);
    }

    @Override
    public void show() {
        // creating levels array

        final Level[] levels = FroggerGame.levels;

        Gdx.input.setInputProcessor(stage);

        ImageButton[] buttons = new ImageButton[levels.length];
        float distanceX = 0.8f * WINDOW_WIDTH / ((float)levels.length / 2);
        for (int i = 0; i < levels.length; i++) {
            buttons[i] = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("buttons/btn.png"))));
            if (i <= 4) buttons[i].setBounds(0.1f * WINDOW_WIDTH + distanceX * i, 0.6f * WINDOW_HEIGHT, BUTTON_WIDTH, BUTTON_WIDTH);
            else buttons[i].setBounds(0.1f * WINDOW_WIDTH + distanceX * (i - 5), 0.4f * WINDOW_HEIGHT, BUTTON_WIDTH, BUTTON_WIDTH);
            final int finalI = i;
            buttons[i].addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    ((Game)Gdx.app.getApplicationListener()).setScreen(new FroggerGameScreen(game, levels[finalI]));
                }
            });
            stage.addActor(buttons[i]);
        }

    }
}
