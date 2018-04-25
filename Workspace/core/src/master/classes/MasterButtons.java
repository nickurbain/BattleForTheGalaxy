package master.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MasterButtons {

	public static ImageTextButtonStyle setButtonStyle(String fontFile, String image, Float scale) {
		BitmapFont font = new BitmapFont(Gdx.files.internal(fontFile));
		font.getData().setScale(scale);
		Texture background = new Texture(Gdx.files.internal(image));
		Drawable drawable = new TextureRegionDrawable(new TextureRegion(background));
		ImageTextButtonStyle style = new ImageTextButtonStyle(drawable, drawable, drawable, font);
		return style;
	}

	public static TextFieldStyle setTextFieldStyle(String fontFile, String image, Float scale, Color color) {
		BitmapFont font = new BitmapFont(Gdx.files.internal(fontFile));
		font.getData().setScale(scale);
		Texture background;
		Drawable drawable = null;
		if (image != null) {
			background = new Texture(Gdx.files.internal(image));
			drawable = new TextureRegionDrawable(new TextureRegion(background));
		}
		TextFieldStyle style = new TextFieldStyle(font, color, null, null, drawable);
		return style;
	}
}