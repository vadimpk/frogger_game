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

public class LevelsScreen implements Screen {

    private FroggerGame game;
    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;

    public LevelsScreen(FroggerGame game) {
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
        Level[] temp = new Level[10];
        for (int i = 0; i < 10; i++) {
            temp[i] = new Level(i + 1, new Map());
        }
        game.loadLevelsToFile("levels.txt", temp);
        System.out.println("loaded successfully");
        final Level[] levels = game.loadLevelsFromFile("levels.txt");
        System.out.println(levels.length);


        Gdx.input.setInputProcessor(stage);

        ImageButton[] buttons = new ImageButton[levels.length];
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        float distanceX = 0.8f * width / ((float)levels.length / 2);
        float btnWidth = width * 0.1f;
        float btnHeight = width * 0.1f;
        for (int i = 0; i < levels.length; i++) {
            buttons[i] = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("btn.png"))));
            if (i <= 4) buttons[i].setBounds(0.1f * width + distanceX * i, 0.6f * height, btnWidth, btnHeight);
            else buttons[i].setBounds(0.1f * width + distanceX * (i - 5), 0.4f * height, btnWidth, btnHeight);
            final int finalI = i;
            buttons[i].addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Level level = new Level(0, new Map());
                    ((Game)Gdx.app.getApplicationListener()).setScreen(new FroggerGameScreen(game, levels[finalI]));
                }
            });
            stage.addActor(buttons[i]);
        }

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
