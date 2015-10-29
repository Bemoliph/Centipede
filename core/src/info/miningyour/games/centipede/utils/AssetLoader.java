package info.miningyour.games.centipede.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import java.util.HashMap;
import java.util.Random;

public class AssetLoader {

    public static Preferences prefs;

    public static Random rng;

    public static BitmapFont font;

    private static TextureAtlas atlas;
    private static HashMap<String, Animation> cachedAnims;

    private static Texture colorPalette;

    public static Leaderboard leaderboard;

    public static void load() {
        prefs = Gdx.app.getPreferences("Centipede");

        rng = new Random();

        font = new BitmapFont(Gdx.files.internal("atari.fnt"));

        atlas = new TextureAtlas(Gdx.files.internal("sprite_sheet.atlas"));

        colorPalette = new Texture(Gdx.files.internal("colortable.png"));

        cachedAnims = new HashMap<String, Animation>();

        leaderboard = new Leaderboard();
    }

    public static Animation getAnimation(String animationName) {
        /*
         * .findRegions() is slow and we don't want to recreate the animation
         * every frame, so we'll cache it in a hashmap for speed.
         */
        if (!cachedAnims.containsKey(animationName)) {
            Animation anim = new Animation(0.08f, atlas.findRegions(animationName));
            anim.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

            cachedAnims.put(animationName, anim);
        }

        return cachedAnims.get(animationName);
    }

    public static TextureAtlas getTextureAtlas() {
        return atlas;
    }

    public static Texture getColorPalette() {
        return colorPalette;
    }

    public static void dispose() {
        font.dispose();
        atlas.dispose();
        colorPalette.dispose();
    }
}
