package battle.galaxy;

import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import controllers.AllianceController;
import controllers.UserQueryController;
import master.classes.MasterScreen;

public class AllianceScreen extends MasterScreen {

	private Label title, joinBanner, createBanner;
	private TextField allianceName;
	private Table allianceMenu, join, create;

	/**
	 * Creates a Alliance screen for the user to interact with
	 * 
	 * @throws UnknownHostException
	 */
	public AllianceScreen() throws UnknownHostException {
		// Calls the MasterScreen
		super("Login.jpg", "clean-crispy-ui.json");

		// A main table for this AllianceScreen
		allianceMenu = new Table();
		allianceMenu.setWidth(stage.getWidth());
		allianceMenu.align(Align.top);
		allianceMenu.setPosition(0, stage.getHeight());

		// The title for this AllianceScreen
		title = new Label("Battle for the Galaxy\nAlliance", skin);
		title.setFontScale(4f);
		createBanner = new Label("Create An Alliance", skin);
		joinBanner = new Label("Join An Alliance", skin);
		
		// TextBoxes for this AllianceScreen
		allianceName = TextBox(skin, "allianceName", "Alliance Name");
		
		// An alliance join and creation table
		join = new Table();
		join.add(joinBanner);
		join.row();
		join.add(allianceName);
		join.row();
		join.add(Button(skin, "JOIN")).width(100).height(30);
		
		create = new Table();
		create.add(createBanner);
		create.row();
		create.add(allianceName);
		create.row();
		create.add(listAlliances());
		create.add(Button(skin, "CREATE")).width(100).height(30);

		// Add all elements to this AllianceScreen
		allianceMenu.add(title).padTop((stage.getHeight() / 2) - 150);
		allianceMenu.row();
		allianceMenu.add(create).padTop(20).width(200).height(40);
		allianceMenu.add(join).padTop(20).width(200).height(40);
		
		stage.addActor(allianceMenu);
		Gdx.input.setInputProcessor(stage);
	}

	private Actor listAlliances() {
		// TODO Auto-generated method stub
		Table guilds = new Table();
		return guilds;
	}

	/**
	 * Renders the AllianceScreen
	 */
	public void render(float delta) {
		super.render(delta);

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			try {
				game.setScreen(new MainMenu());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * A generic method that creates a button for this AllianceScreen
	 * 
	 * @param skin
	 *            The skin used by the buttons
	 * @param name
	 *            The name for this button
	 * @return the TextButton
	 */
	public TextButton Button(Skin skin, final String name) {

		TextButton button = new TextButton(name, skin);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				if (name.equals("JOIN")) {
					System.out.println("Join Name: " + allianceName.getText());
					AllianceController.AllianceQuery(allianceName.getText(), user, "join");
					System.out.println("Join button Clicked");
				} else if (name.equals("CREATE")) {
					System.out.println("Create Name: " + allianceName.getText());
					AllianceController.AllianceQuery(allianceName.getText(), user, "create");
					System.out.println("Create button pushed");
				}
			}
		});

		return button;
	}

	/**
	 * A generic method that generates a text field for this AllianceScreen
	 * 
	 * @param skin
	 *            The skin used by this text field
	 * @param type
	 *            The type of text field to be generated
	 * @param message
	 *            The message displayed in this text field. Disappears when clicked
	 *            on
	 * @return the TextField
	 */
	public TextField TextBox(Skin skin, final String type, final String message) {

		final TextField field = new TextField(message, skin);
		field.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				field.setText("");

				if (type.equals("password")) {
					field.setPasswordMode(true);
					field.setPasswordCharacter('*');
				}
			}
		});
		return field;
	}
}
