package battle.galaxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SplashScreen implements Screen {
	
	BattleForTheGalaxy game;
	OrthographicCamera camera;
	Texture bg_texture;
	Sprite bg_sprite;
	Skin skin;
	Stage stage;
	
	Label title;
	
	//Login Info
	String playerID = "";
	TextField idInput;
	String playerPass = "";
	TextField passInput;
	TextButton button;
	
	// Networking is now part of BattleForTheGalaxy game class
//	SocketHints hints = new SocketHints();
//	InetAddress address;
//	Socket client;
	
	public SplashScreen(BattleForTheGalaxy incomingGame) throws UnknownHostException {
		this.game = incomingGame;
		stage = new Stage();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1600, 900);  // false => y-axis 0 is bottom-left
		
		bg_texture = new Texture(Gdx.files.internal("supernova-background.jpg"));
		bg_texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);  // smoother textures
		bg_sprite = new Sprite(bg_texture);
		skin = new Skin(Gdx.files.internal("clean-crispy-ui.json"));
		
		// Initialize Title text
		title = new Label("Battle for the Galaxy", skin);
		title.setFontScale(2f);
		title.setPosition(1600/2 - 2*title.getWidth()/2, 900 - 2*title.getHeight() - 200);
		
		// Initialize Username input box
		idInput = new TextField("", skin);
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
		passInput = new TextField("", skin);
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
		button = new TextButton("Login", skin);
		button.setPosition(passInput.getX() + passInput.getWidth()/2 - button.getWidth()/2, passInput.getY() - 50);
		button.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				boolean connectionEstablished = false;
				String id = idInput.getText();
				String pass = passInput.getText();
				
				// Try to make client-server connection when Login button is clicked
				try {
					makeConnection(id, pass);
					connectionEstablished = true;
				} catch(Exception e) {
					Dialog dialog = new Dialog("Connection Failed", skin) {
						public void result(Object obj) {
							remove();
						}
					};
					dialog.text("Server couldn't be reached");
					dialog.button("OK", false);
					dialog.key(Keys.ENTER, false);
					dialog.show(stage);
					
				}
				
				if(connectionEstablished) {
					game.setScreen(game.gamescreen);
					dispose();
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
	
	public void makeConnection(String id, String pass) {
		// Create server-client connection
		try {
			game.address = InetAddress.getByName("proj-309-vc-2.cs.iastate.edu");
			game.client = Gdx.net.newClientSocket(Protocol.TCP, game.address.getHostAddress(), 8081, game.hints);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			System.out.println("Server connection could not be made.");
		}
		
		// Create the login-JSON
		game.credInfo.setCreds(id, pass);
		//System.out.println(game.json.toJson(game.credInfo));
		
		// Send the login-JSON to the server
		PrintWriter writer;
		try {
//			client.getOutputStream().write(game.json.toJson(game.credInfo).getBytes());
			writer = new PrintWriter(game.client.getOutputStream(), true);
			writer.println(game.json.toJson(game.credInfo));
//			client.getOutputStream().flush();
//			client.dispose();
		} catch(Exception e2) {
			e2.printStackTrace();
		}
		
		// Testing server output - printing the "OK" that the server sent
		String answer = "";
		BufferedReader in;
		in = new BufferedReader(new InputStreamReader(game.client.getInputStream()));
		while(answer.isEmpty()) {
			try {
				answer = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(answer);
		
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
			game.setScreen(game.gamescreen);
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
