package battle.galaxy;

import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import data.Ship;
import master.classes.MasterScreen;

/**
 * Screen used for ship customization. Extends MasterScreen.
 */
public class HangerScreen extends MasterScreen {
	
	private Label screenTitle;
	private Table hanger, customDropDowns, shipStats;
	private TextButton backButton;
	private Ship ship;

	/**
	 * Constructor that sets up UI elements.
	 * @throws UnknownHostException
	 */
	public HangerScreen() throws UnknownHostException {
		super("Login.jpg", "clean-crispy-ui.json");
		//ship = getShipFromDB(game.id);
		ship = getTempShip();
		ship.calcStats();
		
		screenTitle = new Label("Hanger", skin);
		
		hanger = new Table();
		hanger.setWidth(stage.getWidth());
		hanger.align(Align.top);
		hanger.setPosition(0, Gdx.graphics.getHeight());
		
		customDropDowns = new Table();
		customDropDowns.align(Align.center);
		String[] slotNames = {"Blaster", "Shield", "Armor", "Thruster"};
		
		shipStats = new Table();
		shipStats.align(Align.center);
		String[] statNames = {"Health", "Shield", "Armor", "Damage", "Range", "Velocity"};
		
		backButton = new TextButton("Exit", skin);
		backButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				try {
					game.setScreen(new MainMenu());
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} finally {
					dispose();
				}
				
			}
		});
		
		//DEBUG
		//hanger.setDebug(true);
		
		hanger.add(screenTitle).pad(15).expandX().align(Align.center).row();
		hanger.row();
		hanger.add(customDropDowns(customDropDowns, skin, slotNames)).padTop(100).align(Align.left);
		hanger.add(shipStats(shipStats, skin, statNames)).align(Align.right).row();
		hanger.add(backButton).align(Align.center);
		
		stage.addActor(hanger);
		Gdx.input.setInputProcessor(stage);
		
	}

	/**
	 * Calls super.render()
	 */
	@Override
	public void render(float delta) {
		super.render(delta);
	}
	
	/**
	 * Populates the SelectBoxes with relevant data and adds listeners to them.
	 * @param table The table to add the SelectBoxes to.
	 * @param skin The skin to use for the SelectBoxes
	 * @param names The names of the SelectBoxes
	 * @return
	 */
	public Table customDropDowns(Table table, Skin skin, String[] names) {
		for(final String s: names) {
			Label l = new Label(s, skin);
			l.setFontScale(1.25f);
			final SelectBox<String> selectBox = new SelectBox<String>(skin);
			selectBox.setName(s);
			String[] standard = {"Default", "Light", "Heavy"};
			String[] blaster = {"Default", "Short", "Long"};
			if(s == "Blaster") {
				selectBox.setItems(blaster);
			}else {
				selectBox.setItems(standard);
			}
			selectBox.addListener(new ChangeListener() {

				@Override
				public void changed(ChangeEvent event, Actor actor) {
					switch(s) {
						case("Blaster"):
							ship.setBlasterType(selectBox.getSelectedIndex());
							ship.calcStats();
							break;
						case("Shield"):
							ship.setShieldType(selectBox.getSelectedIndex());
							ship.calcStats();
							break;
						case("Armor"):
							ship.setArmorType(selectBox.getSelectedIndex());
							ship.calcStats();
							break;
						case("Thruster"):
							ship.setThrusterType(selectBox.getSelectedIndex());
							ship.calcStats();
							break;
					}
				}
			});
			table.add(l).padBottom(10);
			table.add(selectBox).fill().padBottom(10).padLeft(10).padRight(10).row();
		}
		
		TextButton saveButton = new TextButton("Save", skin);
		saveButton.addListener(new ClickListener() {
		
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				saveTempShip();
				//sendShipToDB(game.id);
			}
		});
		
		table.add(saveButton);
		return table;
	}
	
	/**
	 * Set up the ship statistics table for display
	 * @param table the table containing the ship statistics
	 * @param skin the skin
	 * @param statNames the names of the statistics
	 * @return table
	 */
	public Table shipStats(Table table, Skin skin, String[] statNames) {
		for(String s: statNames) {
			Label name = new Label(s + ": ", skin);
			name.setFontScale(1.25f);
			table.add(name).padRight(5);
			Label stat;
			switch(s){
				case("Health"):
					stat = new Label(Integer.toString(ship.getHealth()), skin);
					stat.setFontScale(1.25f);
					table.add(stat).row();;
					break;
				case("Shield"):
					stat = new Label(Integer.toString(ship.getShield()), skin);
					stat.setFontScale(1.25f);
					table.add(stat).row();;
					break;
				case("Armor"):
					stat = new Label(Integer.toString(ship.getArmorVal()), skin);
					stat.setFontScale(1.25f);
					table.add(stat).row();;
					break;
				case("Damage"):
					stat = new Label(Integer.toString(ship.getDamage()), skin);
					stat.setFontScale(1.25f);
					table.add(stat).row();;
					break;
				case("Range"):
					stat = new Label(Float.toString(ship.getRange()), skin);
					stat.setFontScale(1.25f);
					table.add(stat).row();
					break;
				case("Velocity"):
					stat = new Label(Integer.toString(ship.getVelocity()), skin);
					stat.setFontScale(1.25f);
					table.add(stat).row();;
					break;
			}
		}
		return table;
	}
	
	/**
	 * @return the ship statistics
	 */
	private Table getStats() {
		return shipStats;
	}
	
	/**
	 * @return a new, default ship
	 */
	private Ship getTempShip() {
		return new Ship();
	}
	
	/**
	 * Gets the player's ship fromt the database
	 * @param id the players id
	 * @return player's ship
	 */
	private Ship getShipFromDB(int id) {
		//TODO
		return new Ship();
	}
	
	/**
	 * Sends the player's ship changes to the database for storage.
	 * @param id the id of the player
	 */
	private void sendShipToDB(int id) {
		game.getDataController().sendToServer(ship);

	}
}