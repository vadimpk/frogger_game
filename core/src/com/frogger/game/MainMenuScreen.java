package com.frogger.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenuScreen implements Screen {

    private FroggerGame game;

    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;

    public MainMenuScreen(FroggerGame game) {
        this.game = game;

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);
    }


    @Override
    public void show() {
        //Stage should control input:
        Gdx.input.setInputProcessor(stage);

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        float btnWidth = width * 0.15f;
        float btnHeight = height * 0.15f;

        //Create buttons
        ImageButton playButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("play_btn.png"))));
        playButton.setBounds(width / 2 - btnWidth /2, 0.6f * height - btnHeight*0.2f, btnWidth, btnHeight);
        ImageButton levelsButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("btn.png"))));
        levelsButton.setBounds(width / 2 - btnWidth /2,0.6f * height - btnHeight*1.2f , btnWidth, btnHeight);
        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("exit_btn.png"))));
        exitButton.setBounds(width / 2 - btnWidth /2, 0.6f * height - btnHeight*2.2f, btnWidth, btnHeight);



        //Add listeners to buttons
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Level level = new Level(0, new Map());
                ((Game)Gdx.app.getApplicationListener()).setScreen(new FroggerGameScreen(game, level));
            }
        });
        try {
            levelsButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    ((Game)Gdx.app.getApplicationListener()).setScreen(new LevelsScreen(game));
                }
            });
        }catch (Exception r){
            throw new RuntimeException(r);
        }


        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        //Add buttons to table
        stage.addActor(playButton);
        stage.addActor(levelsButton);
        stage.addActor(exitButton);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
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

    @Override
    public void dispose() {

    }
}
