package battle.galaxy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class HangerScreen implements Screen{
	
	private BattleForTheGalaxy game;
	private OrthographicCamera camera;
	private Stage stage;
	private Skin skin;
	private Texture bg_texture;
	private Label screenTitle;
	
	private Table hanger, customDropDowns;

	public HangerScreen(BattleForTheGalaxy game) {
		this.game = game;
		stage = new Stage();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1600, 900);  // false => y-axis 0 is bottom-left
		
		skin = game.skin;		
		bg_texture = new Texture(Gdx.files.internal("Login.jpg"));
		bg_texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);  // smoother textures
		screenTitle = new Label("Hanger", skin);
		
		
		hanger = new Table();
		hanger.setWidth(stage.getWidth());
		hanger.align(Align.top);
		hanger.setPosition(0, Gdx.graphics.getHeight());
		
		customDropDowns = new Table();
		customDropDowns.align(Align.center);
		String[] slotNames = {"Blasters", "Shield", "Armor"};
		
		hanger.setDebug(true);
		
		hanger.add(screenTitle).pad(15).expandX();
		hanger.row();
		hanger.add(customDropDowns(customDropDowns, skin, slotNames)).padTop(350);
		
		stage.addActor(hanger);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.05F, 0.05F, 0.05F, 0.05F);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
			game.batch.draw(bg_texture, 0, 0);
		game.batch.end();
		
		stage.draw();
	}
	
	public Table customDropDowns(Table table, Skin skin, String[] names) {
		for(String s: names) {
			SelectBox<String> selectBox = new SelectBox<String>(skin);
			selectBox.setName(s);
			selectBox.setItems("default");
			selectBox.addListener(new ChangeListener() {

				@Override
				public void changed(ChangeEvent event, Actor actor) {
					System.out.println(selectBox.getSelected());
				}
			});
			
			table.add(selectBox).fill().padBottom(10).padLeft(10).padRight(10).row();
		}
		
		return table;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}
