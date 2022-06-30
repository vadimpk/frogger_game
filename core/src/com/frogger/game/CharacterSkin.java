package com.frogger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

public class CharacterSkin {

    private static final Texture FROG_PACK = new Texture(Gdx.files.internal("characters/frog/frog.png"));
    private static final Texture TURTLE_PACK = new Texture(Gdx.files.internal("characters/frog/turtle.png"));
    private static final Texture BIRD_PACK = new Texture(Gdx.files.internal("characters/frog/bird.png"));
    private static final Texture EGG_PACK = new Texture(Gdx.files.internal("characters/frog/egg.png"));
    private static final Texture FISH_PACK = new Texture(Gdx.files.internal("characters/frog/fish.png"));
    private static final Texture PIZZA_PACK = new Texture(Gdx.files.internal("characters/frog/pizza.png"));
    private static final Texture BOTTLE_OF_WINE_PACK = new Texture(Gdx.files.internal("characters/frog/wine.png"));
    private static final Texture BOTTLE_OF_COKE_PACK = new Texture(Gdx.files.internal("characters/frog/cola.png"));

    public TextureRegion standing;
    public TextureRegion jumping;
    public TextureRegion drowning1;
    public TextureRegion drowning2;
    public TextureRegion drowning3;
    public TextureRegion drowning4;
    public TextureRegion drowning5;
    public TextureRegion dead;

    private Texture texturePack;
    private int textureRotation;

    private final String NAME;
    private final int PRICE;
    private boolean unlocked;
    private boolean active;
    private final Util.Character CHARACTER;

    public CharacterSkin(String name, int price, boolean isUnlocked, boolean isActive, Util.Character character) {
        this.NAME = name;
        this.PRICE = price;
        this.unlocked = isUnlocked;
        this.active = isActive;
        this.CHARACTER = character;
        setCharacter(character);
        textureRotation = 0;
    }

    /**
     * Method to set current skin active and also set all the other skins inactive
     * @param active active
     */
    public void setActive(boolean active) {
        if(active) {
            Frog.get().setCharacterSkin(this);
            for (CharacterSkin skin: DataIO.getSkins()) {
                if (skin.isActive()) {
                    skin.setActive(false);
                }
            }
        }
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public String getName() {
        return NAME;
    }

    public int getPrice() {
        return PRICE;
    }

    public void setCharacter(Util.Character character) {
        if (character == Util.Character.FROG) texturePack = FROG_PACK;
        else if (character == Util.Character.TURTLE) texturePack = TURTLE_PACK;
        else if (character == Util.Character.BIRD) texturePack = BIRD_PACK;
        else if (character == Util.Character.EGG) texturePack = EGG_PACK;
        else if (character == Util.Character.FISH) texturePack = FISH_PACK;
        else if (character == Util.Character.PIZZA) texturePack = PIZZA_PACK;
        else if (character == Util.Character.BOTTLE_OF_WINE) texturePack = BOTTLE_OF_WINE_PACK;
        else if (character == Util.Character.BOTTLE_OF_COKE) texturePack = BOTTLE_OF_COKE_PACK;

        standing = new TextureRegion(texturePack, 300, 150, 150, 150);
        jumping = new TextureRegion(texturePack, 150, 150, 150, 150);
        drowning1 = new TextureRegion(texturePack, 0, 0, 150, 150);
        drowning2 = new TextureRegion(texturePack, 150, 0, 150, 150);
        drowning3 = new TextureRegion(texturePack, 300, 0, 150, 150);
        drowning4 = new TextureRegion(texturePack, 0, 150, 150, 150);
        drowning5 = new TextureRegion(texturePack, 0, 300, 150, 150);
        dead = new TextureRegion(texturePack, 150, 300, 150, 150);
    }

    public static void dispose() {
        FROG_PACK.dispose();
        TURTLE_PACK.dispose();
        BIRD_PACK.dispose();
        EGG_PACK.dispose();
        FISH_PACK.dispose();
        PIZZA_PACK.dispose();
        BOTTLE_OF_WINE_PACK.dispose();
        BOTTLE_OF_COKE_PACK.dispose();
    }

    public Util.Character getCharacter() {
        return CHARACTER;
    }

    public void rotate(int degree) {
        textureRotation = degree;

        if (texturePack == BOTTLE_OF_COKE_PACK || texturePack == BOTTLE_OF_WINE_PACK) {
            textureRotation = 0;
        }
    }

    public int getTextureRotation() {
        return textureRotation;
    }
}
