package com.frogger.game.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.frogger.game.FroggerGame;
import com.frogger.game.utils.Audio;

import static com.frogger.game.utils.Const.*;

/**
 * TypeOfSkinChooser.java
 * @author vadympolishchuk
 * Screen that opens when player clicks on "Skins" button on Main Menu.
 * It helps player to choose what type of skin he wants to change, either
 * skin for character of for tiles (so it has two buttons)
 * Both buttons call SkinsScreen, just with different parameters (different skins to show)
 */

public class TypeOfSkinChooser extends Screen {

    public TypeOfSkinChooser(FroggerGame game) {
        super(game);
        createButtonStyles();
    }

    /**
     * Show two buttons (one for character skins and other for tiles skins)
     */
    @Override
    public void show() {
        super.show();

        TextButton chooseCharacterSkin = new TextButton("characters", textButtonStyles.get("buy"));
        TextButton chooseTileSkin = new TextButton("tiles", textButtonStyles.get("buy"));

        chooseCharacterSkin.setBounds(300,300, BUTTON_WIDTH, BUTTON_HEIGHT);
        chooseTileSkin.setBounds(900,300, BUTTON_WIDTH, BUTTON_HEIGHT);

        chooseCharacterSkin.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Audio.playClickedSound();
                switchScreenWithFading(new SkinsScreen(game, true), 0.3f);
            }
        });

        chooseTileSkin.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Audio.playClickedSound();
                switchScreenWithFading(new SkinsScreen(game, false), 0.3f);
            }
        });

        stage.addActor(chooseCharacterSkin);
        stage.addActor(chooseTileSkin);
    }

    private void createButtonStyles() {

        TextButton.TextButtonStyle chooseButtonStyle = new TextButton.TextButtonStyle();
        chooseButtonStyle.font = fonts.get("36");
        chooseButtonStyle.up = skin.getDrawable("yellow-btn-up");
        chooseButtonStyle.down = skin.getDrawable("yellow-btn-down");
        chooseButtonStyle.over = skin.getDrawable("yellow-btn-over");
        chooseButtonStyle.disabled = skin.getDrawable("btn-disabled");

        textButtonStyles.put("buy", chooseButtonStyle);
    }
}
