package master.classes;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import battle.galaxy.BattleForTheGalaxy;
import controllers.ChatController;
import controllers.DataController;
import controllers.UserQueryController;

/**
 * The master screen class contains the elements that all screens in Battle For
 * The Galaxy share.
 */
public class MasterScreen implements Screen {

	protected static BattleForTheGalaxy game;
	protected OrthographicCamera camera;
	protected Texture background;
	protected Stage stage;
	protected Skin skin;
	protected static String user, alliance, chat_message;
	protected static Table master, chatWindow, messageDisplay;
	private ImageTextButton send;
	private ArrayList<TextField> messages;
	private TextFieldStyle style, style2;
	private ImageTextButtonStyle style_send;
	/**
	 * An empty constructor
	 */
	public MasterScreen() {

	}

	/**
	 * The master screen constructor that all game screens share
	 * 
	 * @param picture
	 *            The background picture the screen will have
	 * @param skin
	 *            The file containing default values for various items such as font,
	 *            text buttons, labels, etc...
	 * @throws UnknownHostException
	 */
	public MasterScreen(String picture, String skin) throws UnknownHostException {
		game = DataController.getGame();
		user = UserQueryController.getUser();
		alliance = UserQueryController.getAlliance();
		messages = new ArrayList<TextField>();
		style = MasterButtons.setTextFieldStyle("SansSerif.fnt", null, 1f, Color.WHITE);
		style2 = MasterButtons.setTextFieldStyle("SansSerif.fnt", null, 1f, Color.RED);
		style_send = MasterButtons.setButtonStyle("SansSerif.fnt", "button.png", 0.7f);
		
		stage = new Stage();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1600, 900); // false => y-axis 0 is bottom-left
		
		
		this.setSkin(skin);

		background = new Texture(Gdx.files.internal(picture));
		background.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		chatWindow = new Table();
		chatWindow.align(Align.bottomLeft);
		chatWindow.setHeight(300);
		chatWindow.setWidth(150);
		chatWindow();
	}

	/**
	 * The render options that are shared between all screens
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.05F, 0.05F, 0.05F, 0.05F);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.getBatch().setProjectionMatrix(camera.combined);

		game.getBatch().begin();
		game.getBatch().draw(background, 0, 0);
		game.getBatch().end();

		// Stage
		stage.act();
		stage.draw();
		updateChatWindow();

	}

	/**
	 * Sets the skin that the screen calling master screen will use
	 * 
	 * @param skin
	 *            The skin file name as a string that will be used by the screen
	 *            calling master screen.
	 */
	public void setSkin(String skin) {
		this.skin = new Skin(Gdx.files.internal(skin));
	}

	protected void updateChatWindow() {
		ChatController.getMessagesFromServer();
	
		TextField displayMsg;
		for (Iterator<String> iter = ChatController.getMessages().iterator(); iter.hasNext();) {
			String msg = iter.next();
			System.out.println(msg);
			iter.remove();
			if(msg.contains("SYSMSG")) {
				msg = msg.substring(0, msg.length() - 6);
				displayMsg = new TextField(msg, style2);
			}else {
				displayMsg = new TextField(msg, style);
			}

			// displayMsg.debug();
			displayMsg.setDisabled(true);
			
			if (messages.size() >= 4) {
				TextField temp = messages.get(0);
				messageDisplay.removeActor(temp);
				ArrayList<TextField> tempMess = new ArrayList<TextField>();
				for (int i = 1; i < messages.size(); i++) {
					tempMess.add(messages.get(i));
				}
				messages = tempMess;
			}
			
			messageDisplay.add(displayMsg).width(messageDisplay.getWidth()).bottom();
			messageDisplay.row();
			messages.add(displayMsg);

		}
	}

	/**
	 * Generates the options to select when entering a chat
	 * 
	 * @param table
	 *            The table used to generate the chat menu
	 * @param skin
	 *            The skin used to define defaults
	 * @param names
	 *            The names of the buttons
	 * @return The chat table populated with buttons
	 */
	private Table chatWindow() {

		messageDisplay = new Table();
		// messageDisplay.debug();

		final TextField sendBox = new TextField("", style);
		//sendBox.setColor(Color.DARK_GRAY);
		sendBox.addListener(new InputListener() {
			public boolean keyDown(InputEvent event, int keycode) {
				if(keycode == Keys.ENTER) {
					ChatController.SendMessage(sendBox.getText().replace("\n", ""), "all");
					sendBox.setText("");
					return true;
				}
				return false;
			}
		});

		send = new ImageTextButton("SEND", style_send);
		send.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				ChatController.SendMessage(sendBox.getText(), "all");
				sendBox.setText("");
			}
		});

		//Table chatOptions = new Table();
		Table sendOptions = new Table();
		sendOptions.add(sendBox).width(550);
		sendOptions.add(send).width(100);

		/*for (int i = 0; i < chatNames.length; i++) {
			chatOptions.add(new TextButton(chatNames[i], skin)).width(150);
		}*/

		//chatWindow.add(chatOptions).left();
		//chatWindow.row();
		chatWindow.add(messageDisplay).fill().height(150);
		chatWindow.row();
		chatWindow.add(sendOptions).left();
		return chatWindow;
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
		game.getDataController().getRxFromServer().clear();
		game.getDataController().getRawData().clear();
	}
}