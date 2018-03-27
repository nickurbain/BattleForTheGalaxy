package battle.galaxy;

import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SplashScreen implements Screen {
	
	BattleForTheGalaxy game;
	OrthographicCamera camera;
	Texture bg_texture;
	Sprite bg_sprite;
	Stage stage;
	
	Label title;
	
	//Login Info
	String playerID = "";
	TextField idInput;
	String playerPass = "";
	TextField passInput;
	TextButton button;
	
	public SplashScreen(BattleForTheGalaxy incomingGame) throws UnknownHostException {
		this.game = incomingGame;
		stage = new Stage();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1600, 900);  // false => y-axis 0 is bottom-left
		
		bg_texture = new Texture(Gdx.files.internal("supernova-background.jpg"));
		bg_texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);  // smoother textures
		bg_sprite = new Sprite(bg_texture);
		
		// Initialize Title text
		title = new Label("Battle for the Galaxy", game.skin);
		title.setFontScale(2f);
		title.setPosition(1600/2 - 2*title.getWidth()/2, 900 - 2*title.getHeight() - 200);
		
		// Initialize Username input box
		idInput = new TextField("", game.skin);
		idInput.setText("Username");
		idInput.setPosition(1600/2 - idInput.getWidth()/2, 900/2 - idInput.getHeight()/2);
		idInput.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				idInput.setText("");
			}
		});
		
		// Initialize Password input box
		passInput = new TextField("", game.skin);
		passInput.setText("Password");
		passInput.setPosition(1600/2 - passInput.getWidth()/2,  900/2 - 50 - passInput.getHeight()/2);
		passInput.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				passInput.setText("");
				passInput.setPasswordMode(true);
				passInput.setPasswordCharacter('*');
			}
		});
		
		// Initialize Login button
		button = new TextButton("Login", game.skin);
		button.setPosition(passInput.getX() + passInput.getWidth()/2 - button.getWidth()/2, passInput.getY() - 50);
		button.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				String id = idInput.getText();
				String pass = passInput.getText();
				
				// Try to make client-server connection when Login button is clicked
				if(game.dataController.login(id, pass)) {
					//game.setScreen(game.gamescreen);
					try {
						game.setScreen(new MainMenu(game));
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("SplashScreen - ERROR: Connection Failed");
					Dialog dialog = new Dialog("Connection Failed", game.skin) {
						public void result(Object obj) {
							remove();
						}
					};
					dialog.text("Server couldn't be reached");
					dialog.button("OK", false);
					dialog.key(Keys.ENTER, false);
					dialog.show(stage);	
				}
			}
		});
		
		// Stage setup
		stage.addActor(idInput);
		stage.addActor(passInput);
		stage.addActor(button);
		stage.addActor(title);
		stage.setKeyboardFocus(idInput);
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
		
		//Stage
		stage.draw();
		
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			//game.setScreen(game.gamescreen);
			try {
				game.setScreen(new MainMenu(game));
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dispose();
		}	
		
		if(Gdx.input.isKeyJustPressed(Keys.TAB)) {
			passInput.setText("");
			passInput.setPasswordMode(true);
			passInput.setPasswordCharacter('*');
		}
		
		if(playerID.isEmpty()) {
			
		}
	}
	
	@Override
	public void show() {

	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void hide() {
		
	}

}
