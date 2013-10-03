package com.friendlyblob.mayhemandhell.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.controls.Input;
import com.friendlyblob.mayhemandhell.client.entities.Player;
import com.friendlyblob.mayhemandhell.client.entities.gui.GuiElement.GuiPriority;
import com.friendlyblob.mayhemandhell.client.entities.gui.GuiManager;
import com.friendlyblob.mayhemandhell.client.entities.gui.GuiManager.GuiPositionHorizontal;
import com.friendlyblob.mayhemandhell.client.entities.gui.GuiManager.GuiPositionVertical;
import com.friendlyblob.mayhemandhell.client.entities.gui.SkillBar;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.gameworld.Map;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
import com.friendlyblob.mayhemandhell.client.mapeditor.MapEditor;

public class GameScreen extends BaseScreen{
	private GameWorld world;
	
	public GuiManager guiManager;
	
	public GameScreen(MyGame game) {
		super(game);
		
		// Temporary GUI implementation 
		guiManager = new GuiManager();
//		guiManager.addGuiElement(
//				new SkillBar(GuiPriority.LOW), 
//				GuiPositionHorizontal.RIGHT, 
//				GuiPositionVertical.MIDDLE);
		
		GameWorld.initialize();
		world = GameWorld.getInstance();
		world.setGame(game);
		
		game.connectToServer();
	}

	@Override
	public void draw(float deltaTime) {
		spriteBatch.begin();
		/*---------------------------------------
		 * World
		 */
		world.draw(spriteBatch);
		
		spriteBatch.end();
		/*---------------------------------------
		 * GUI Elements
		 */
		spriteBatch.begin();
		spriteBatch.setProjectionMatrix(guiCam.combined);
		
		if (MapEditor.enabled){
			MapEditor.drawInfo(spriteBatch);
		}

		guiManager.draw(spriteBatch);
		
		Assets.defaultFont.draw(spriteBatch, fpsText, 20, 20);
		
		spriteBatch.end();
	}
	
	@Override
	public void update(float deltaTime) {
		world.update(deltaTime);
		
		// If GUI didn't catch touch input
		if(!guiManager.update(deltaTime)) {
			// Pass touch event to other entities
			if (!MapEditor.enabled) {
				world.updateWorldInput();
			}
		}
		
	}

	@Override
	public void prepare() {
		// TODO Auto-generated method stub
	}
	
	public GameWorld getWorld() {
		return world;
	}

}
