package com.frogger.game.skins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.frogger.game.DataIO;
import com.frogger.game.Util;
import com.frogger.game.gameObjects.Frog;

public class CharacterSkin {

    private static final Texture FROG_PACK = new Texture(Gdx.files.internal("characters/frog.png"));
    private static final Texture TURTLE_PACK = new Texture(Gdx.files.internal("characters/turtle.png"));
    private static final Texture BIRD_PACK = new Texture(Gdx.files.internal("characters/bird.png"));
    private static final Texture EGG_PACK = new Texture(Gdx.files.internal("characters/egg.png"));
    private static final Texture FISH_PACK = new Texture(Gdx.files.internal("characters/fish.png"));
    private static final Texture PIZZA_PACK = new Texture(Gdx.files.internal("characters/pizza.png"));
    private static final Texture BOTTLE_OF_WINE_PACK = new Texture(Gdx.files.internal("characters/wine.png"));
    private static final Texture BOTTLE_OF_COKE_PACK = new Texture(Gdx.files.internal("characters/cola.png"));

    public TextureRegion standing;
    public TextureRegion jumping;
    public TextureRegion drowning1;
    public TextureRegion drowning2;
    public TextureRegion drowning3;
    public TextureRegion drowning4;
    public TextureRegion drowning5;
    public TextureRegion dead;

    private static final Texture OAK_FOREST_TILES_PACK = new Texture(Gdx.files.internal("objects/tiles/oak-forest-tiles.png"));
    private static final Texture FIR_FOREST_TILES_PACK = new Texture(Gdx.files.internal("objects/tiles/fir-forest-tiles.png"));
    private static final Texture BEACH_TILES_PACK = new Texture(Gdx.files.internal("objects/tiles/beach-tiles.png"));
    private static final Texture DARK_FOREST_TILES_PACK = new Texture(Gdx.files.internal("objects/tiles/dark-forest-tiles.png"));

    public TextureRegion transparentTile1;
    public TextureRegion transparentTile2;
    public TextureRegion notTransparentTile;

    private Texture texturePack;
    private int textureRotation;

    private final String NAME;
    private final int PRICE;
    private boolean unlocked;
    private boolean active;
    private Util.Character characterSkin;
    private Util.TileSkin tileSkin;
    private boolean forTiles;

    public CharacterSkin(String name, int price, boolean isUnlocked, boolean isActive, Util.Character characterSkin) {
        this.NAME = name;
        this.PRICE = price;
        this.unlocked = isUnlocked;
        this.active = isActive;
        this.characterSkin = characterSkin;
        setCharacterSkin(characterSkin);
        textureRotation = 0;
        forTiles = false;
    }

    public CharacterSkin(String name, int price, boolean isUnlocked, boolean isActive, Util.TileSkin tileSkin) {
        this.NAME = name;
        this.PRICE = price;
        this.unlocked = isUnlocked;
        this.active = isActive;
        this.tileSkin = tileSkin;
        setTileSkin(tileSkin);
        textureRotation = 0;
        forTiles = true;
    }

    /**
     * Method to set current skin active and also set all the other skins inactive
     * @param active active
     */
    public void setActive(boolean active) {
        if (!forTiles) {
            if (active) {
                Frog.get().setCharacterSkin(this);
                for (CharacterSkin skin : DataIO.getCharacterSkins()) {
                    if (skin.isActive()) {
                        skin.setActive(false);
                    }
                }
            }
        } else {

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

    public void setCharacterSkin(Util.Character characterSkin) {
        if (characterSkin == Util.Character.FROG) texturePack = FROG_PACK;
        else if (characterSkin == Util.Character.TURTLE) texturePack = TURTLE_PACK;
        else if (characterSkin == Util.Character.BIRD) texturePack = BIRD_PACK;
        else if (characterSkin == Util.Character.EGG) texturePack = EGG_PACK;
        else if (characterSkin == Util.Character.FISH) texturePack = FISH_PACK;
        else if (characterSkin == Util.Character.PIZZA) texturePack = PIZZA_PACK;
        else if (characterSkin == Util.Character.BOTTLE_OF_WINE) texturePack = BOTTLE_OF_WINE_PACK;
        else if (characterSkin == Util.Character.BOTTLE_OF_COKE) texturePack = BOTTLE_OF_COKE_PACK;

        standing = new TextureRegion(texturePack, 300, 150, 150, 150);
        jumping = new TextureRegion(texturePack, 150, 150, 150, 150);
        drowning1 = new TextureRegion(texturePack, 0, 0, 150, 150);
        drowning2 = new TextureRegion(texturePack, 150, 0, 150, 150);
        drowning3 = new TextureRegion(texturePack, 300, 0, 150, 150);
        drowning4 = new TextureRegion(texturePack, 0, 150, 150, 150);
        drowning5 = new TextureRegion(texturePack, 0, 300, 150, 150);
        dead = new TextureRegion(texturePack, 150, 300, 150, 150);
    }

    public void setTileSkin(Util.TileSkin tileSkin) {
        if (tileSkin == Util.TileSkin.OAK_FOREST) texturePack = OAK_FOREST_TILES_PACK;
        else if (tileSkin == Util.TileSkin.FIR_FOREST) texturePack = FIR_FOREST_TILES_PACK;
        else if (tileSkin == Util.TileSkin.BEACH) texturePack = BEACH_TILES_PACK;
        else if (tileSkin == Util.TileSkin.DARK_FOREST) texturePack = DARK_FOREST_TILES_PACK;


        transparentTile1 = new TextureRegion(texturePack, 300, 0, 300, 300);
        transparentTile2 = new TextureRegion(texturePack, 600, 0, 300, 300);
        notTransparentTile = new TextureRegion(texturePack, 0, 0, 300, 300);
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

        OAK_FOREST_TILES_PACK.dispose();
        FIR_FOREST_TILES_PACK.dispose();
        BEACH_TILES_PACK.dispose();
        DARK_FOREST_TILES_PACK.dispose();
    }

    public Util.Character getCharacterSkin() {
        return characterSkin;
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
