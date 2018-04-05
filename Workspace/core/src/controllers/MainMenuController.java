package controllers;

import java.net.UnknownHostException;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import battle.galaxy.GameScreen;
import battle.galaxy.HangerScreen;
import battle.galaxy.LoginScreen;
import master.classes.MasterScreen;

/**
 * The controller used to connect various buttons from the main menu using click
 * handlers
 */
public class MainMenuController {

	private MasterScreen screen = new MasterScreen();

	// The options available to the player
	enum options {
		ACCOUNT, SHOP, HANGER, FACTION, ALLIANCE, CREW, EVENTS, LOGOUT
	}

	// The various modes the game offers
	enum modes {
		ALLOUT_DM, ALLIANCE_DM, FACTION, TEAM_DM, CONSTRUCTION, MINING
	}

	// The various chat options
	enum chat {
		GLOBAL, TEAM, PRIVATE
	}

	/**
	 * Sets click handlers to the option buttons
	 * 
	 * @param button
	 *            The button that will receive the click handler
	 * @param option
	 *            The option that lines up with the options enum ordinal value
	 * @return The button with an attached click handler
	 */
	public TextButton setOption(TextButton button, final int option) {
		button.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {

				// value = options enum value
				// Ex: option = 5 -> value = options.CREW
				// because CREW is index 5 in the enum list
				options value = options.values()[option];

				try {
					switch (value) {
					case ACCOUNT:
						System.out.println("ACCOUNT has been pressed");
						break;
					case ALLIANCE:
						System.out.println("ALLIANCE button pushed");
						break;
					case CREW:
						System.out.println("CREW button pushed");
						break;
					case EVENTS:
						System.out.println("EVENTS button pushed");
						break;
					case FACTION:
						System.out.println("FACTION button pushed");
						break;
					case HANGER:
						screen.getGame().setScreen(new HangerScreen());
						break;
					case SHOP:
						System.out.println("GALACTIC SHOP button pushed");
						break;
					case LOGOUT:
						screen.getGame().setScreen(new LoginScreen(screen.getGame()));
						break;
					default:
						System.out.println("Not a valid screen selection");
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		});

		return button;
	}

	/**
	 * Sets click handlers to the game mode buttons
	 * 
	 * @param button
	 *            The button that will receive the click handler
	 * @param option
	 *            The option that lines up with the modes enum ordinal value
	 * @return The button with an attached click handler
	 */
	public TextButton setMode(TextButton button, final int option) {
		button.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {

				// value = modes enum value
				// Ex: option = 1 -> value = modes.ALLIANCE_DM
				// because ALLIANCE_DM is index 1 in the enum list
				modes value = modes.values()[option];

				switch (value) {
				case ALLIANCE_DM:
					System.out.println("ALLIANCE DEATH MATCH button pushed");
					break;
				case ALLOUT_DM:
					screen.getGame().setScreen(new GameScreen(screen.getGame()));
					break;
				case CONSTRUCTION:
					System.out.println("CONSTRUCTION button pushed");
					break;
				case FACTION:
					System.out.println("FACTION BATTLE button pushed");
					break;
				case MINING:
					System.out.println("MINING button pushed");
					break;
				case TEAM_DM:
					System.out.println("TEAM DEATH MATCH button pushed");
					break;
				default:
					break;

				}
			}
		});

		return button;
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