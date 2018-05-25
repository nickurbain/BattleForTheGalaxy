package battle.galaxy;

import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import master.classes.MasterScreen;

public class GalacticShopScreen extends MasterScreen{
	
	private Table table;
	private TextButton button, backButton;
	private Label amount;
	private int doubloons;
	
	public GalacticShopScreen() throws UnknownHostException {
		super("Login.jpg", "clean-crispy-ui.json");
		stage.addActor(chatWindow);
		doubloons = game.getDataController().getJsonController().getJsonReader().parse(
				(String) game.getDataController().sendToServerWaitForResponse("{jsonOrigin:1,jsonType:20}", false)).getInt("doubloons");
		
		table = new Table();
		table.setHeight(stage.getHeight());
		table.setWidth(stage.getWidth());
		table.align(Align.top);
		table.setPosition(0, 0);
		
		amount = new Label("Amount: " + doubloons, game.getSkin());
		amount.setFontScale(3f);
		table.add(amount).align(Align.center);
		table.row();
		
		button = new TextButton("Wish in a Well", skin);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				doubloons -= 100;
				game.getDataController().sendToServer("{jsonOrigin:1,jsonType:19,amount:" + doubloons + "}");
				amount.setText("Amount: " + doubloons);
				
			}
		});
		
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
		
		table.add(button);
		table.add(backButton);
		
		stage.addActor(table);
		
		Gdx.input.setInputProcessor(stage);
	}
	
}
