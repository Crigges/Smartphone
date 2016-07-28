package systems.crigges.smartphone.util;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AssetFactory {
	private static Map<String, Texture> textures = new HashMap<String, Texture>();
	private static Map<String, FreeTypeFontGenerator> fonts = new HashMap<String, FreeTypeFontGenerator>();
	private static Map<String, Animation> animations = new HashMap<String, Animation>();
	private static Map<String, Music> musics = new HashMap<String, Music>();
	private static Map<String, Sound> sounds = new HashMap<String, Sound>();
	private static TextButtonStyle defaultTextButtonStyle;
	private static TextFieldStyle defaultTextFieldStyle;
	private static float viewportWidth;
	private static float viewportHeight;
		
	public static void loadAllRessources(){
		addFont("normal", "normal.otf");
		addTexture("button_normal", "button_normal.png");
		addTexture("button_disabled", "button_disabled.png");
		addTexture("button_pressed", "button_pressed.png");
		addTexture("button_focus", "button_focus.png");
		addTexture("textarea", "textarea.png");
		
		
		defaultTextButtonStyle = genDefaultButtonStyle();
		defaultTextFieldStyle = genDefaultTextFieldStyle();
	}
	
	public static TextButtonStyle getDefaultButtonStyle(){
		return defaultTextButtonStyle;
	}
	
	private static TextButtonStyle genDefaultButtonStyle(){
		TextButtonStyle style = new TextButtonStyle();
		NinePatch p = new NinePatch(getTexture("button_normal"), 10, 10, 10, 10);
		style.up = new NinePatchDrawable(p);
		style.fontColor = Color.WHITE;
		p = new NinePatch(getTexture("button_disabled"), 10, 10, 10, 10);
		style.disabled = new NinePatchDrawable(p);
		style.disabledFontColor = Color.GRAY;
		p = new NinePatch(getTexture("button_pressed"), 10, 10, 10, 10);
		style.down = new NinePatchDrawable(p);
		p = new NinePatch(getTexture("button_focus"), 10, 10, 10, 10);
		style.over = new NinePatchDrawable(p);
		BitmapFont font = getFont("normal", 80);
		style.font = font;
		return style;
	}
	
	public static TextFieldStyle getDefaultTextFieldStyle(){
		return defaultTextFieldStyle;
	}
	
	private static TextFieldStyle genDefaultTextFieldStyle(){
		TextFieldStyle style = new TextFieldStyle();
		style.background = new NinePatchDrawable(new NinePatch(getTexture("textarea"), 10, 10, 10, 10));
		style.font = getFont("normal", 36);
		style.fontColor = Color.WHITE;
		return style;
	}
	
	public static Drawable getTextureDrawable(String name){
		return new TextureRegionDrawable(new TextureRegion(AssetFactory.getTexture(name)));
	}

	public static Color getDefaultRed() {
		return new Color(245f / 255f, 168f / 255f, 1f / 255f, 1);
	}

	private static void addTexture(String title, String name) {
		Texture t = new Texture("textures/" + name);
		t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		textures.put(title, t);
	}
	
	public static Texture getTexture(String title)	{
		return textures.get(title);
	}
	
	private static void addFont(String title, String path)	{
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/" + path));
		fonts.put(title, gen);
	}
	
	public static void setViewportBounds(int width, int height){
		viewportWidth = width;
		viewportHeight = height;
	}
	
	public static float getFontScaleX(){
		return (viewportWidth / Gdx.graphics.getWidth());
	}
	
	public static float getFontScaleY(){
		return (viewportHeight / Gdx.graphics.getHeight());
	}
	
	public static float getFontScale(){
		return ((viewportWidth / Gdx.graphics.getWidth()) + (viewportHeight / Gdx.graphics.getHeight())) / 2;
	}

	public static BitmapFont getFont(String title, int size)	{
		FreeTypeFontGenerator gen = fonts.get(title);
		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.magFilter = TextureFilter.Linear;
		param.minFilter = TextureFilter.Linear;
//		param.shadowColor = new Color(0, 0, 0, 1);
//		param.shadowOffsetX = shaddowOffset;
//		param.shadowOffsetY = shaddowOffset;
		//Adjust for real screen size
		param.size = (int) (size / getFontScale());
		BitmapFont font = gen.generateFont(param);
		font.getData().setScale(getFontScaleX(), getFontScaleY());
		return font;
	}
	
	private static void addMusic(String title, Music music){
		musics.put(title, music);
	}
	
	public static Music getMusic(String title){
		return musics.get(title);
	}
	
	private static void addSound(String title, Sound sound){
		sounds.put(title, sound);
	}
	
	public static Sound getSound(String title){
		return sounds.get(title);
	}
	
	private static void addAnimation(String title, Animation anim){
		animations.put(title, anim);
	}
	
	public static Animation getAnimation(String title){
		return animations.get(title);
	}

}
