package com.frogger.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.frogger.game.utils.Audio;
import com.frogger.game.FroggerGame;

import java.util.HashMap;
import java.util.Map;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.frogger.game.utils.Const.*;

/**
 * Screen.java
 * @author stas-bukovskiy
 *
 * Class implements com.badlogic.gdx.Screen and sets all required options for further screen creating
 * It uses in all screen classe
 */
public abstract class Screen implements com.badlogic.gdx.Screen {
    protected FroggerGame game;

    protected SpriteBatch batch;
    protected Stage stage;
    protected Viewport viewport;
    protected OrthographicCamera camera;
    protected Map<String, BitmapFont> fonts;
    protected Skin skin;
    protected TextureAtlas buttonAtlas;
    protected Map<String, TextButton.TextButtonStyle> textButtonStyles;
    protected Map<String, Button.ButtonStyle> buttonStyles;
    protected Map<String, TextButton> buttons;
    protected Texture bgTexture;
    protected boolean isSwitching;

    /**
     * Constructor create Stage, OrthographicCamera instances and all Map collections
     * @param game - instance of FroggerGame class
     */
    public Screen(FroggerGame game) {
        this.game = game;

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);

        fonts = new HashMap<>();
        buttons = new HashMap<>();

        fonts.put("24", new BitmapFont(Gdx.files.internal("fonts/Pixellari_24.fnt")));
        fonts.put("36", new BitmapFont(Gdx.files.internal("fonts/Pixellari_36.fnt")));
        fonts.put("72", new BitmapFont(Gdx.files.internal("fonts/Pixellari_72.fnt")));
        fonts.put("100", new BitmapFont(Gdx.files.internal("fonts/Pixellari_100.fnt")));
        skin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.atlas"));
        skin.addRegions(buttonAtlas);
        textButtonStyles = new HashMap<>();
        buttonStyles = new HashMap<>();

        isSwitching = false;
    }

    /**
     * Method renders stage
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    /**
     * Method dispose all disposable instances
     */
    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        for (BitmapFont font : fonts.values()) font.dispose();
        skin.dispose();
        buttonAtlas.dispose();
        if(bgTexture != null) bgTexture.dispose();
    }

    /**
     * Method Sets the InputProcessor that will receive all touch and key input events.
     * It will be called before the ApplicationListener.render() method each frame.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.getRoot().addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.3f)));
    }

    /**
     * Method resizes stage
     * @param width - window width
     * @param height - window width
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    /**
     * Method switches screens with fading effect
     * @param newScreen - screen that will be set
     * @param duration - duration of fading effect in seconds
     */
    public void switchScreenWithFading(final Screen newScreen, float duration) {
        stage.getRoot().getColor().a = 1;
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(fadeOut(duration));
        sequenceAction.addAction(run(new Runnable() {
            @Override
            public void run() {
                ((Game)Gdx.app.getApplicationListener()).setScreen(newScreen);
            }
        }));
        stage.getRoot().addAction(sequenceAction);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    /**
     * Method fills textButtonStyles map with basic styles such as "green", "yellow", "red"
     */
    public void createMenuButtons(){
        textButtonStyles.put("green", new TextButton.TextButtonStyle());
        textButtonStyles.put("yellow", new TextButton.TextButtonStyle());
        textButtonStyles.put("red", new TextButton.TextButtonStyle());
        for (String key : new String[]{"green", "yellow", "red"}) {
            TextButton.TextButtonStyle buttonStyle = textButtonStyles.get(key);
            buttonStyle.font = fonts.get("36");
            buttonStyle.up = skin.getDrawable(key + "-btn-up");
            buttonStyle.down = skin.getDrawable(key + "-btn-down");
            buttonStyle.over = skin.getDrawable(key + "-btn-over");
        }

        Button.ButtonStyle soundsStyle = new Button.ButtonStyle();
        soundsStyle.up = skin.getDrawable("sound-on-btn-up");
        soundsStyle.down = skin.getDrawable("sound-on-btn-down");
        soundsStyle.over = skin.getDrawable("sound-on-btn-over");
        soundsStyle.checked = skin.getDrawable("sound-off-btn-up");
        soundsStyle.checkedOver = skin.getDrawable("sound-off-btn-over");
        soundsStyle.checkedDown = skin.getDrawable("sound-off-btn-down");
        buttonStyles.put("sounds", soundsStyle);
    }

    /**
     * Method creates back button
     * @param x - x-coordinate of back button
     * @param y - y-coordinate of back button
     * @param previousScreen - screen that will be changed
     * @return  back button
     */
    public TextButton getBackButton(float x, float y, final Screen previousScreen) {
        if(!textButtonStyles.containsKey("red")) createMenuButtons();
        TextButton backButton = new TextButton("Back", textButtonStyles.get("red"));
        backButton.setBounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Audio.playClickedSound();
                switchScreenWithFading(previousScreen, 0.3f);
            }
        });
        return backButton;
    }

    /**
     * Method sets standard background image
     */
    public void setBackground() {
        bgTexture = new Texture(Gdx.files.internal("backgrounds/bg.png"));
        Image bg = new Image(bgTexture);
        bg.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.addActor(bg);
    }
}
