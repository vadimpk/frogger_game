package com.frogger.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.frogger.game.FroggerGame;
import com.frogger.game.Level;

import static com.frogger.game.Const.*;

public class LevelsScreen extends Screen {

    private TextButton.TextButtonStyle threeStarsButtonStyle;
    private TextButton.TextButtonStyle twoStarsButtonStyle;
    private TextButton.TextButtonStyle oneStarsButtonStyle;
    private TextButton.TextButtonStyle zeroStarsButonStyle;
    private BitmapFont font;
    private Skin skin;
    private TextureAtlas buttonAtlas;

    public LevelsScreen(FroggerGame game) {
        super(game);
    }

    @Override
    public void show() {
        // creating levels array

        final Level[] levels = FroggerGame.levels;

        Gdx.input.setInputProcessor(stage);

        font = new BitmapFont(Gdx.files.internal("fonts/Pixellari_36.fnt"));
        skin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.atlas"));
        skin.addRegions(buttonAtlas);
        threeStarsButtonStyle = new TextButton.TextButtonStyle();
        twoStarsButtonStyle = new TextButton.TextButtonStyle();
        oneStarsButtonStyle = new TextButton.TextButtonStyle();
        zeroStarsButonStyle = new TextButton.TextButtonStyle();
        threeStarsButtonStyle.font = new BitmapFont(Gdx.files.internal("fonts/Pixellari_36.fnt"), false);
        twoStarsButtonStyle.font = new BitmapFont(Gdx.files.internal("fonts/Pixellari_36.fnt"), false);
        oneStarsButtonStyle.font = new BitmapFont(Gdx.files.internal("fonts/Pixellari_36.fnt"), false);
        zeroStarsButonStyle.font = new BitmapFont(Gdx.files.internal("fonts/Pixellari_36.fnt"), false);

        threeStarsButtonStyle.up = skin.getDrawable("level-btn-up-3-stars");
        threeStarsButtonStyle.down = skin.getDrawable("level-btn-down-3-stars");
        threeStarsButtonStyle.over = skin.getDrawable("level-btn-over-3-stars");

        twoStarsButtonStyle.up = skin.getDrawable("level-btn-up-2-stars");
        twoStarsButtonStyle.down = skin.getDrawable("level-btn-down-2-stars");
        twoStarsButtonStyle.over = skin.getDrawable("level-btn-over-2-stars");

        oneStarsButtonStyle.up = skin.getDrawable("level-btn-up-1-stars");
        oneStarsButtonStyle.down = skin.getDrawable("level-btn-down-1-stars");
        oneStarsButtonStyle.over = skin.getDrawable("level-btn-over-1-stars");

        zeroStarsButonStyle.up = skin.getDrawable("level-btn-up-0-stars");
        zeroStarsButonStyle.down = skin.getDrawable("level-btn-down-0-stars");
        zeroStarsButonStyle.over = skin.getDrawable("level-btn-over-0-stars");


        TextButton[] buttons = new TextButton[levels.length];
        float distanceX = 0.8f * WINDOW_WIDTH / ((float)levels.length / 2);
        for (int i = 0; i < levels.length; i++) {

            buttons[i]  =  new TextButton(String.valueOf(i+1), zeroStarsButonStyle);
            float button_size = 0.15f * WINDOW_HEIGHT;
            if (i <= 4) buttons[i].setBounds(0.1f * WINDOW_WIDTH + distanceX * i, 0.65f * WINDOW_HEIGHT - 0.5f*button_size, button_size, button_size);
            else buttons[i].setBounds(0.1f * WINDOW_WIDTH + distanceX * (i - 5), 0.65f * WINDOW_HEIGHT - 2f*button_size, button_size, button_size);
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

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
        skin.dispose();
        buttonAtlas.dispose();
    }
}
