package com.frogger.game.skins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.frogger.game.DataIO;
import com.frogger.game.attributeObjects.Scorer;

import static com.frogger.game.utils.Const.WINDOW_HEIGHT;
import static com.frogger.game.utils.Const.WINDOW_WIDTH;

/**
 * SkinPanel.java
 * Method that implements skin panel when player is choosing a skin.
 * It draws skin image, name, price and amount of stars on a screen.
 * It has methods to change skin (next or previous) and to buy a skin
 */

public class SkinPanel {

    private final CharacterSkin[] skins;
    private CharacterSkin currentSkin;
    private int skinId;
    private TextureRegion thumbnail;
    private final Stage stage;
    private Label starNumberLabel;
    private Label nameLabel;
    private Label priceLabel;
    private Image thumbnailImg;
    private Image star;
    private Image smallStar;
    private final TextureRegion STAR_TEXTURE = Scorer.FILLED_STAR;
    private final BitmapFont FONT_FOR_TEXT = new BitmapFont(Gdx.files.internal("fonts/Pixellari_36.fnt"));
    private final BitmapFont FONT_FOR_NUMBERS = new BitmapFont(Gdx.files.internal("fonts/Pixellari_72.fnt"));

    public SkinPanel(int skinId, CharacterSkin[] skins, Stage stage) {
        this.skins = skins;
        this.skinId = skinId;
        this.stage = stage;
        setCurrentSkin(skinId);
    }

    /**
     * Method to choose a new skin (from a list)
     */

    public void nextSkin() {
        skinId = (skinId + 1 >= skins.length) ? 0 : skinId + 1;
        setCurrentSkin(skinId);
    }

    /**
     * Method to choose previous skin (from a list)
     */
    public void previousSkin() {
        skinId = (skinId == 0) ? skins.length - 1 : skinId - 1;
        setCurrentSkin(skinId);
    }

    /**
     * Method to set new skin to show
     * @param skinId which skin
     */
    public void setCurrentSkin(int skinId) {
        this.currentSkin = skins[skinId];

        // if skin is unlocked set it to active
        if (currentSkin.isUnlocked()) {
            currentSkin.setActive(true);
            DataIO.updateSkins(currentSkin.isForTiles(), skinId);
        }

        thumbnail = currentSkin.thumbnail;
    }

    /**
     * Method to buy new skin
     */
    public void buySkin() {
        if (DataIO.getStarNumber() >= currentSkin.getPrice()) {
            currentSkin.setUnlocked(true);
            currentSkin.setActive(true);

            DataIO.updateSkins(currentSkin.isForTiles(), skinId);
        }
    }

    /**
     * Method to draw skin panel
     */
    public void render() {
        // draw amount of available stars (top right corner)
        starNumberLabel.setText(String.valueOf(DataIO.getStarNumber()));
        starNumberLabel.setPosition(star.getX() - 1.2f*starNumberLabel.getWidth(), star.getY() + star.getHeight()  / 2 - starNumberLabel.getHeight() / 2);

        // draw thumbnail
        thumbnailImg.setDrawable(new TextureRegionDrawable(thumbnail));

        // draw name
        nameLabel.setText(currentSkin.getName());
        nameLabel.setPosition((WINDOW_WIDTH / 2 - nameLabel.getWidth() / 2), thumbnailImg.getY() + thumbnailImg.getHeight() + 0.05f*WINDOW_HEIGHT);

        // draw price (if is not unlocked already)
        if (!currentSkin.isUnlocked()) {
            priceLabel.setText(String.valueOf(currentSkin.getPrice()));
            smallStar.setBounds(WINDOW_WIDTH / 2 - priceLabel.getWidth() / 2 + 0.11f*WINDOW_HEIGHT , WINDOW_HEIGHT * 0.325f, 0.1f*WINDOW_HEIGHT, 0.1f*WINDOW_HEIGHT);
            priceLabel.setPosition(WINDOW_WIDTH / 2 - priceLabel.getWidth() / 2, WINDOW_HEIGHT * 0.325f);
        }
        smallStar.setVisible(!currentSkin.isUnlocked());
        priceLabel.setVisible(!currentSkin.isUnlocked());
    }

    public void show() {

        star = new Image(STAR_TEXTURE);
        star.setBounds(WINDOW_WIDTH - 0.2f*WINDOW_HEIGHT, 0.8f*WINDOW_HEIGHT, 0.15f*WINDOW_HEIGHT, 0.15f*WINDOW_HEIGHT);

        starNumberLabel = new Label(String.valueOf(DataIO.getStarNumber()), new Label.LabelStyle(FONT_FOR_NUMBERS, Color.BLACK));
        starNumberLabel.setPosition(star.getX() - 1.2f*starNumberLabel.getWidth(), star.getY() + star.getHeight()  / 2 - starNumberLabel.getHeight() / 2);

        thumbnailImg = new Image(thumbnail);
        thumbnailImg.setBounds(WINDOW_WIDTH * 0.4f, WINDOW_HEIGHT * 0.45f,
                            WINDOW_WIDTH * 0.2f, WINDOW_WIDTH * 0.2f);

        nameLabel = new Label(currentSkin.getName(), new Label.LabelStyle(FONT_FOR_NUMBERS, Color.BLACK));
        nameLabel.setPosition((WINDOW_WIDTH / 2 - nameLabel.getWidth() / 2), thumbnailImg.getY() + thumbnailImg.getHeight() + 0.05f*WINDOW_HEIGHT);

        smallStar = new Image(STAR_TEXTURE);
        priceLabel = new Label(String.valueOf(currentSkin.getPrice()), new Label.LabelStyle(FONT_FOR_NUMBERS, Color.BLACK));

        smallStar.setBounds(WINDOW_WIDTH / 2 - priceLabel.getWidth() / 2, WINDOW_HEIGHT * 0.325f, 0.1f*WINDOW_HEIGHT, 0.1f*WINDOW_HEIGHT);
        priceLabel.setPosition(WINDOW_WIDTH / 2 - priceLabel.getWidth() / 2, WINDOW_HEIGHT * 0.325f);
        if (currentSkin.isUnlocked()) {
            priceLabel.setVisible(false);
            smallStar.setVisible(false);
        }

        stage.addActor(starNumberLabel);
        stage.addActor(thumbnailImg);
        stage.addActor(nameLabel);
        stage.addActor(priceLabel);
        stage.addActor(star);
        stage.addActor(smallStar);

    }

    public void dispose() {
        FONT_FOR_NUMBERS.dispose();
        FONT_FOR_TEXT.dispose();

    }

    public CharacterSkin getCurrentSkin() {
        return currentSkin;
    }
}
