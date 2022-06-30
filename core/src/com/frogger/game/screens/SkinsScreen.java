package com.frogger.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.frogger.game.*;

import static com.frogger.game.Const.*;
import static com.frogger.game.DataIO.*;

public class SkinsScreen extends Screen {

    private SkinPanel skinPanel;

    public SkinsScreen(FroggerGame game) {
        super(game);
        createButtonStyles();
    }

    @Override
    public void show() {
        super.show();

        skinPanel = new SkinPanel(0);

        for (int i = 0; i < getSkins().length; i++) {
            if (getSkins()[i].isActive()) {
                skinPanel = new SkinPanel(i);
            }
        }


        TextButton rightArrow = new TextButton("", textButtonStyles.get("right-arrow"));
        TextButton leftArrow = new TextButton("", textButtonStyles.get("left-arrow"));

        leftArrow.setBounds(0.2f * WINDOW_WIDTH, WINDOW_HEIGHT * 0.45f, 0.1f * WINDOW_HEIGHT, 0.1f * WINDOW_HEIGHT);

        rightArrow.setBounds(0.8f * WINDOW_WIDTH - 0.1f * WINDOW_HEIGHT, WINDOW_HEIGHT * 0.45f, 0.1f * WINDOW_HEIGHT, 0.1f * WINDOW_HEIGHT);

        final TextButton buyButton = new TextButton("Buy", textButtonStyles.get("buy"));

        buyButton.setBounds((WINDOW_WIDTH - BUTTON_WIDTH) / 2, 0.2f * WINDOW_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
        buyButton.setDisabled(defineBuyButtonAvailability());


        rightArrow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                skinPanel.nextSkin();
                buyButton.setDisabled(defineBuyButtonAvailability());
            }
        });

        leftArrow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                skinPanel.previousSkin();
                buyButton.setDisabled(defineBuyButtonAvailability());
            }
        });

        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!defineBuyButtonAvailability()) Audio.playClickedSound();
                skinPanel.buySkin();
                buyButton.setDisabled(defineBuyButtonAvailability());
            }
        });

        stage.addActor(rightArrow);
        stage.addActor(leftArrow);
        stage.addActor(buyButton);
        stage.addActor(getBackButton(WINDOW_WIDTH / 2 - BUTTON_WIDTH / 2, 0.08f * WINDOW_HEIGHT));
    }

    @Override
    public void render(float delta) {

        skinPanel.render();

        stage.act();
        stage.draw();
    }

    private boolean defineBuyButtonAvailability() {
        return skinPanel.getSkin().isUnlocked() || DataIO.getStarNumber() < skinPanel.getSkin().getPrice();
    }

    private void createButtonStyles() {

        TextButton.TextButtonStyle buyButtonStyle = new TextButton.TextButtonStyle();
        buyButtonStyle.font = fonts.get("36");
        buyButtonStyle.up = skin.getDrawable("yellow-btn-up");
        buyButtonStyle.down = skin.getDrawable("yellow-btn-down");
        buyButtonStyle.over = skin.getDrawable("yellow-btn-over");
        buyButtonStyle.disabled = skin.getDrawable("btn-disabled");


        TextButton.TextButtonStyle rightButtonStyle = new TextButton.TextButtonStyle();
        rightButtonStyle.font = fonts.get("24");
        rightButtonStyle.up = skin.getDrawable("arrow-btn-up");
        rightButtonStyle.down = skin.getDrawable("arrow-btn-down");
        rightButtonStyle.over = skin.getDrawable("arrow-btn-over");
        rightButtonStyle.disabled = skin.getDrawable("arrow-btn-disabled");


        textButtonStyles.put("left-arrow", rightButtonStyle);
        textButtonStyles.put("right-arrow", rightButtonStyle);
        textButtonStyles.put("buy", buyButtonStyle);
    }
}

class SkinPanel {

    private CharacterSkin skin;
    private int skinId;
    private TextureRegion promo;
    private float nameTextWidth;
    private float priceTextWidth;
    private float priceTextHeight;
    private float starsAmountTextWidth;
    private float starsAmountTextHeight;


    private final TextureRegion STAR_TEXTURE = Scorer.FILLED_STAR;
    private final Texture WHITE_BACKGROUND = new Texture(Gdx.files.internal("characters/frog/white.png"));
    private final BitmapFont FONT_FOR_TEXT = new BitmapFont(Gdx.files.internal("fonts/Pixellari_36.fnt"));
    private final BitmapFont FONT_FOR_NUMBERS = new BitmapFont(Gdx.files.internal("fonts/Pixellari_100.fnt"));

    SkinPanel(int skinId) {
        this.skinId = skinId;
        setSkin(skinId);
    }

    public void nextSkin() {
        skinId = (skinId + 1 >= getSkins().length) ? 0 : skinId+1;
        setSkin(skinId);
    }

    public void previousSkin() {
        skinId = (skinId == 0) ? getSkins().length - 1 : skinId-1;
        setSkin(skinId);
    }

    public void setSkin(int skinId) {
        this.skin = DataIO.getSkins()[skinId];

        if (skin.isUnlocked()) {
            skin.setActive(true);
        }

        promo = skin.standing;

        GlyphLayout layout = new GlyphLayout();

        layout.setText(FONT_FOR_NUMBERS, String.valueOf(skin.getPrice()));
        priceTextWidth = layout.width;
        priceTextHeight = layout.height;

        layout.setText(FONT_FOR_TEXT, skin.getName());
        nameTextWidth = layout.width;

        layout.setText(FONT_FOR_NUMBERS, String.valueOf(DataIO.getStarNumber()));
        starsAmountTextWidth = layout.width;
        starsAmountTextHeight = layout.height;
    }

    public void buySkin() {
        if (DataIO.getStarNumber() >= skin.getPrice()) {
            skin.setUnlocked(true);

            GlyphLayout layout = new GlyphLayout();
            layout.setText(FONT_FOR_NUMBERS, String.valueOf(DataIO.getStarNumber()));
            starsAmountTextWidth = layout.width;
            starsAmountTextHeight = layout.height;
        }
    }

    public void render() {
        FroggerGame.skinPanelBatch.begin();

        FroggerGame.skinPanelBatch.draw(WHITE_BACKGROUND,0.9f * WINDOW_WIDTH - 4 * starsAmountTextWidth, WINDOW_HEIGHT - 0.1f * WINDOW_WIDTH - starsAmountTextHeight,
                0.1f * WINDOW_WIDTH + 4 * starsAmountTextWidth, 0.1f * WINDOW_WIDTH + starsAmountTextHeight);
        FroggerGame.skinPanelBatch.draw(STAR_TEXTURE, 0.9f * WINDOW_WIDTH, WINDOW_HEIGHT - 0.1f * WINDOW_WIDTH, 1.5f * starsAmountTextHeight, 1.5f * starsAmountTextHeight);
        FONT_FOR_NUMBERS.draw(FroggerGame.skinPanelBatch, String.valueOf(DataIO.getStarNumber()), 0.9f * WINDOW_WIDTH - starsAmountTextWidth, WINDOW_HEIGHT - 0.1f * WINDOW_WIDTH + 1.3f * starsAmountTextHeight);

        FroggerGame.skinPanelBatch.draw(WHITE_BACKGROUND,WINDOW_WIDTH * 0.4f, WINDOW_HEIGHT * 0.2f,
                WINDOW_WIDTH * 0.2f, WINDOW_WIDTH * 0.7f);
        FroggerGame.skinPanelBatch.draw(promo,WINDOW_WIDTH * 0.4f, WINDOW_HEIGHT * 0.4f,
                WINDOW_WIDTH * 0.2f, WINDOW_WIDTH * 0.2f);

        FONT_FOR_TEXT.draw(FroggerGame.skinPanelBatch, skin.getName(), (WINDOW_WIDTH - nameTextWidth) / 2, WINDOW_HEIGHT * 0.8f);

        if (!skin.isUnlocked()) {
            FONT_FOR_NUMBERS.draw(FroggerGame.skinPanelBatch, String.valueOf(skin.getPrice()), (WINDOW_WIDTH - priceTextWidth) / 2, WINDOW_HEIGHT * 0.425f);
            FroggerGame.skinPanelBatch.draw(STAR_TEXTURE, (WINDOW_WIDTH + priceTextWidth) / 2, WINDOW_HEIGHT * 0.325f, 1.5f * priceTextHeight, 1.5f * priceTextHeight);
        }

        FroggerGame.skinPanelBatch.end();
    }

    public CharacterSkin getSkin() {
        return skin;
    }
}