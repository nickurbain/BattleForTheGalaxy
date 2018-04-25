package controllers;

import java.net.UnknownHostException;
import java.util.HashMap;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import battle.galaxy.DeathMatchScreen;
import battle.galaxy.GalacticShopScreen;
import battle.galaxy.AllianceDeathMatchScreen;
import battle.galaxy.AllianceScreen;
import battle.galaxy.CaptureTheCoreScreen;
import battle.galaxy.HangerScreen;
import battle.galaxy.JuggernautMatchScreen;
import battle.galaxy.LoginScreen;
import battle.galaxy.MatchStatsScreen;
import battle.galaxy.MiningScreen;
import battle.galaxy.TeamDeathMatchScreen;
import master.classes.MasterScreen;

/**
 * The controller used to connect various buttons from the main menu using click
 * handlers
 */

public class MainMenuController extends MasterScreen {

	// The options available to the player
	enum options {
		ACCOUNT, SHOP, HANGER, ALLIANCE, CREW, EVENTS, LOGOUT;
	}

	// The various modes the game offers
	enum modes {
		ALLOUT_DM, ALLIANCE_DM, JUGGERNAUGHT, TEAM_DM, CTF, MINING
	}

	// The various chat options
	enum chat {
		GLOBAL, TEAM, PRIVATE
	}
	
	//MatchStatsTestString
	private String matchStats = "{\"jsonOrigin\":0,\"jsonType\":5,\"matchStats\":[{\"kills\":0,\"deaths\":0,\"hitPoints\":70,\"playerId\":1,\"damageDealt\":30},{\"kills\":2,\"deaths\":2,\"hitPoints\":40,\"playerId\":2,\"damageDealt\":60},{\"kills\":0,\"deaths\":0,\"hitPoints\":70,\"playerId\":3,\"damageDealt\":30}]}";
	private HashMap<Integer, String> map = new HashMap<Integer, String>();
	
	
	/**
	 * Sets click handlers to the option buttons
	 * 
	 * @param myButton
	 *            The button that will receive the click handler
	 * @param option
	 *            The option that lines up with the options enum ordinal value
	 * @return The button with an attached click handler
	 */
	public Button setOption(Button myButton, final int option) {
		myButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {

				// value = options enum value
				// Ex: option = 2 -> value = options.CREW
				// because CREW is index 2 in the enum list
				options value = options.values()[option];

				try {
					switch (value) {
					case ACCOUNT:
						map.put(1, "Nick");
						map.put(2, "Json");
						map.put(3, "SharkWeek");
						game.setScreen(new MatchStatsScreen(matchStats, map));
						System.out.println("ACCOUNT has been pressed");
						break;
					case ALLIANCE:
						game.setScreen(new AllianceScreen());
						break;
					case CREW:
						System.out.println("CREW button pushed");
						break;
					case EVENTS:
						System.out.println("EVENTS button pushed");
						break;
					case HANGER:
						game.setScreen(new HangerScreen());
						break;
					case LOGOUT:
						game.setScreen(new LoginScreen());
						break;
					case SHOP:
						game.setScreen(new GalacticShopScreen());
						System.out.println("GALACTIC SHOP button pushed");
						break;
					default:
						System.out.println("Not a valid screen selection");
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		});

		return myButton;
	}

	/**
	 * Sets click handlers to the game mode buttons
	 * 
	 * @param myButton
	 *            The button that will receive the click handler
	 * @param option
	 *            The option that lines up with the modes enum ordinal value
	 * @return The button with an attached click handler
	 */
	public ImageTextButton setMode(ImageTextButton myButton, final int option) {
		myButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {

				// value = modes enum value
				// Ex: option = 1 -> value = modes.ALLIANCE_DM
				// because ALLIANCE_DM is index 1 in the enum list
				modes value = modes.values()[option];
				try {
					switch (value) {
					case ALLIANCE_DM:
						if(alliance != null) {
							game.setScreen(new AllianceDeathMatchScreen());
						}else {
							//TODO say something in chat
						}
						System.out.println("ALLIANCE DEATH MATCH button pushed");
						break;
					case ALLOUT_DM:
						game.setScreen(new DeathMatchScreen());
						break;
					case CTF:
						game.setScreen(new CaptureTheCoreScreen());
						System.out.println("CAPTURE THE CORE button pushed");
						break;
					case JUGGERNAUGHT:
						game.setScreen(new JuggernautMatchScreen());
						System.out.println("JUGGERNAUGHT BATTLE button pushed");
						break;
					case MINING:
						game.setScreen(new MiningScreen());
						System.out.println("MINING button pushed");
						break;
					case TEAM_DM:
						game.setScreen(new TeamDeathMatchScreen());
						System.out.println("TEAM DEATHMATCH button pushed");
						break;
					default:
						break;
					}
				}catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		});

		return myButton;
	}

	/**
	 * Sets click handlers to the chat mode buttons
	 * 
	 * @param button
	 *            The button that will receive the click handler
	 * @param option
	 *            The option that lines up with the chat enum ordinal value
	 * @return The button with an attached click handler
	 */
	public TextButton setChat(TextButton button, final int option) {
		button.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {

				// value = chat enum value
				// Ex: option = 0 -> value = chat.GLOBAL
				// because GLOBAL is index 0 on the enum list
				chat value = chat.values()[option];

				switch (value) {
				case GLOBAL:
					break;
				case PRIVATE:
					break;
				case TEAM:
					break;
				default:
					break;
				}
			}
		});
		return button;
	}
}