package com.frogger.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.frogger.game.DataIO;
import com.frogger.game.FroggerGame;
import com.frogger.game.utils.Audio;

import java.sql.DatabaseMetaData;

import static com.frogger.game.utils.Const.*;

/**
 * MainMenuScreen.java
 * @author stas-bukovskiy
 *
 * Class for main menu screen.
 * It gives player opportunity to go on level and skin screen or exit from game
 * Also consist button for turning off/on game and menu sounds
 */
public class MainMenuScreen extends Screen {

    public static boolean IS_SOUNDS_ON = true;
    private Button soundsButton;

    /**
     * Basic constructor
     * @param game - FroggerGame instance
     */
    public MainMenuScreen(FroggerGame game) {
        super(game);
    }

    /**
     * Method adds level buttons to stage
     */
    @Override
    public void show() {
        super.show();

        initButtons();

        bgTexture = new Texture(Gdx.files.internal("backgrounds/main-bg.png"));
        Image bg = new Image(bgTexture);
        bg.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.addActor(bg);

        //Add buttons to table
        stage.addActor(buttons.get("play"));
        stage.addActor(buttons.get("skins"));
        stage.addActor(buttons.get("exit"));
        stage.addActor(buttons.get("donate"));
        stage.addActor(soundsButton);

    }

    /**
     * Method creates buttons
     */
    private void initButtons() {

        createMenuButtons();

        //Create buttons
        float startingX = (WINDOW_WIDTH / 2) - (BUTTON_WIDTH / 2);
        soundsButton = new Button(buttonStyles.get("sounds"));
        soundsButton.setBounds(0.05f* WINDOW_HEIGHT, 0.85f*WINDOW_HEIGHT, 0.8f*BUTTON_HEIGHT, 0.8f*BUTTON_HEIGHT);

        float distance = BUTTON_HEIGHT *1.5f;
        float startingY = 0.5f*WINDOW_HEIGHT;
        buttons.put("play", new TextButton("Play", textButtonStyles.get("green")));
        buttons.get("play").setBounds(startingX,startingY, BUTTON_WIDTH, BUTTON_HEIGHT);
        buttons.put("skins", new TextButton("Skins", textButtonStyles.get("yellow")));
        buttons.get("skins").setBounds(startingX,startingY - distance, BUTTON_WIDTH, BUTTON_HEIGHT);
        buttons.put("exit",  new TextButton("Exit", textButtonStyles.get("red")));
        buttons.get("exit").setBounds(startingX, startingY - 2f*distance, BUTTON_WIDTH, BUTTON_HEIGHT);


        textButtonStyles.put("yellow-small", textButtonStyles.get("yellow"));
        textButtonStyles.get("yellow-small").font = fonts.get("24");
        buttons.put("donate",  new TextButton("Donate", textButtonStyles.get("yellow-small")));
        buttons.get("donate").setBounds((WINDOW_WIDTH / 2) - (0.7f*BUTTON_WIDTH / 2), 0.02f*WINDOW_WIDTH, 0.7f*BUTTON_WIDTH, 0.7f*BUTTON_HEIGHT);

        //Add listeners to buttons
        buttons.get("play").addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Audio.playClickedSound();
                switchScreen(new LevelsScreen(game));
            }
        });

        buttons.get("skins").addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Audio.playClickedSound();
                switchScreen(new TypeOfSkinChooser(game));
            }
        });

        buttons.get("exit").addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Audio.playClickedSound();
                Gdx.app.exit();
            }
        });

        buttons.get("donate").addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Audio.playClickedSound();
                Gdx.net.openURI("https://helpukraine.center/#donate");
            }
        });

        soundsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Audio.playClickedSound();
                IS_SOUNDS_ON = !IS_SOUNDS_ON;
            }
        });
    }

    /**
     * Method dispose all disposable instances
     */
    @Override
    public void dispose() {
        super.dispose();
        bgTexture.dispose();
    }
}
