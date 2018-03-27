package com.demoui;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class DemoUI extends ApplicationAdapter {

	
	private Stage stage;
	/**
	 * Skin is the information contained in the uiskin.json file
	 */
	private Skin skin;
	
	/**
	 * Creates a button with that displays a message that lasts
	 * 5 seconds.
	 */
	/*@Override
	public void create () {
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage(new ScreenViewport());
		
		final TextButton button = new TextButton("Click Me", skin, "default");
		button.setWidth(200);
		button.setHeight(50);
		
		final Dialog dialog = new Dialog("Click Message", skin);
		
		button.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				dialog.show(stage);
				Timer.schedule(new Timer.Task() {
					@Override
					public void run() {
						dialog.hide();
					}
				}, 5);
			}
		});
		
		stage.addActor(button);
		Gdx.input.setInputProcessor(stage);
	}*/
	
	private Table table;
	private TextButton startButton;
	private TextButton quitButton;
	
	private SpriteBatch batch;
	private Sprite sprite;
	
	@Override
	public void create () {
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage(new ScreenViewport());
		
		table = new Table();
		table.setWidth(stage.getWidth());
		table.align(Align.center|Align.top);
		table.setPosition(0, Gdx.graphics.getHeight());
		
		startButton = new TextButton("New Game", skin);
		quitButton = new TextButton("Quit Game", skin);
		
		table.padTop(100);
		table.add(startButton);
		table.row();
		table.add(quitButton).padTop(60);
		
		stage.addActor(table);
		
		batch = new SpriteBatch();
		sprite = new Sprite(new Texture(Gdx.files.internal("badlogic.jpg")));
		sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		/*The batch code needs to be before the stage otherwise the sprite
		 * (badlogic.jpg) will be drawn over the buttons*/
		batch.begin();
		sprite.draw(batch);
		batch.end();
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}
}