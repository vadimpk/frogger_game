package com.frogger.game.skins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.frogger.game.DataIO;
import com.frogger.game.FroggerGame;
import com.frogger.game.attributeObjects.Scorer;

import static com.frogger.game.utils.Const.WINDOW_HEIGHT;
import static com.frogger.game.utils.Const.WINDOW_WIDTH;
import static com.frogger.game.DataIO.getSkins;

/**
 * SkinPanel.java
 * Method that implements skin panel when player is choosing a skin.
 * It draws skin image, name, price and amount of stars on a screen.
 * It has methods to change skin (next or previous) and to buy a skin
 */

public class SkinPanel {

    private CharacterSkin skin;
    private int skinId;
    private TextureRegion thumbnail;
    private float nameTextWidth;
    private float priceTextWidth;
    private float priceTextHeight;
    private float starsAmountTextWidth;
    private float starsAmountTextHeight;


    private final TextureRegion STAR_TEXTURE = Scorer.FILLED_STAR;
    private final Texture WHITE_BACKGROUND = new Texture(Gdx.files.internal("characters/white.png"));
    private final BitmapFont FONT_FOR_TEXT = new BitmapFont(Gdx.files.internal("fonts/Pixellari_36.fnt"));
    private final BitmapFont FONT_FOR_NUMBERS = new BitmapFont(Gdx.files.internal("fonts/Pixellari_100.fnt"));

    public SkinPanel(int skinId) {
        this.skinId = skinId;
        setSkin(skinId);
    }

    /**
     * Method to choose a new skin (from a list)
     */
    public void nextSkin() {
        skinId = (skinId + 1 >= getSkins().length) ? 0 : skinId + 1;
        setSkin(skinId);
    }

    /**
     * Method to choose previous skin (from a list)
     */
    public void previousSkin() {
        skinId = (skinId == 0) ? getSkins().length - 1 : skinId - 1;
        setSkin(skinId);
    }

    /**
     * Method to set new skin to show
     * @param skinId which skin
     */
    public void setSkin(int skinId) {
        this.skin = DataIO.getSkins()[skinId];

        // if skin is unlocked set it to active
        if (skin.isUnlocked()) {
            skin.setActive(true);
        }

        thumbnail = skin.standing;

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

    /**
     * Method to buy new skin
     */
    public void buySkin() {
        if (DataIO.getStarNumber() >= skin.getPrice()) {
            skin.setUnlocked(true);
            skin.setActive(true);

            GlyphLayout layout = new GlyphLayout();
            layout.setText(FONT_FOR_NUMBERS, String.valueOf(DataIO.getStarNumber()));
            starsAmountTextWidth = layout.width;
            starsAmountTextHeight = layout.height;
        }
    }

    /**
     * Method to draw skin panel
     */
    public void render() {
        FroggerGame.skinPanelBatch.begin();

        FroggerGame.skinPanelBatch.draw(WHITE_BACKGROUND, 0.9f * WINDOW_WIDTH - 4 * starsAmountTextWidth, WINDOW_HEIGHT - 0.1f * WINDOW_WIDTH - starsAmountTextHeight,
                0.1f * WINDOW_WIDTH + 4 * starsAmountTextWidth, 0.1f * WINDOW_WIDTH + starsAmountTextHeight);
        FroggerGame.skinPanelBatch.draw(STAR_TEXTURE, 0.9f * WINDOW_WIDTH, WINDOW_HEIGHT - 0.1f * WINDOW_WIDTH, 1.5f * starsAmountTextHeight, 1.5f * starsAmountTextHeight);
        FONT_FOR_NUMBERS.draw(FroggerGame.skinPanelBatch, String.valueOf(DataIO.getStarNumber()), 0.9f * WINDOW_WIDTH - starsAmountTextWidth, WINDOW_HEIGHT - 0.1f * WINDOW_WIDTH + 1.3f * starsAmountTextHeight);

        FroggerGame.skinPanelBatch.draw(WHITE_BACKGROUND, WINDOW_WIDTH * 0.4f, WINDOW_HEIGHT * 0.2f,
                WINDOW_WIDTH * 0.2f, WINDOW_WIDTH * 0.7f);
        FroggerGame.skinPanelBatch.draw(thumbnail, WINDOW_WIDTH * 0.4f, WINDOW_HEIGHT * 0.4f,
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
