package com.friendlyblob.mayhemandhell.client.mapeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.controls.Input;
import com.friendlyblob.mayhemandhell.client.entities.EnvironmentObject;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class MapEditor {
	public static boolean enabled = false;
	
	public static int selectedTileTexture = 1; // Tile texture index
	public static int selectedObject = 1;
	
	public static MapEditorWindow editorWindow;
	
	public static void update() {
		switch (MapEditor.editorWindow.tabbedPane.getSelectedIndex()) {
			case 1:
				if (Input.isReleasing()) {
					if (MapEditor.editorWindow.collisionModeButton.isSelected()) {
					}
				}
				
				if (Gdx.input.isTouched()) {
					if (!MapEditor.editorWindow.collisionModeButton.isSelected()) {
					} 
				}
				break;
			case 2:
				if (Input.isReleasing()) {
				}
				break;
			default:
		}
	}
	
	/*
	 * Method that allows moving camera freely. Used when map editor is enabled
	 */
	public static void cameraUpdate(OrthographicCamera worldCam, float deltaTime) {
		final int cameraSpeed = 1000;
		if(Gdx.input.isKeyPressed(Keys.A)){
			worldCam.position.x -= cameraSpeed * deltaTime;
		}
		if(Gdx.input.isKeyPressed(Keys.D)){
			worldCam.position.x += cameraSpeed * deltaTime;
		}
		if(Gdx.input.isKeyPressed(Keys.W)){
			worldCam.position.y += cameraSpeed * deltaTime;
		}
		if(Gdx.input.isKeyPressed(Keys.S)){
			worldCam.position.y -= cameraSpeed * deltaTime;
		}
		if(Gdx.input.isKeyPressed(Keys.UP)){
			worldCam.zoom -= deltaTime*2;
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN)){
			worldCam.zoom += deltaTime*2;
		}
		worldCam.update();
	}
	
	public static void drawInfo(SpriteBatch spriteBatch){
		// TODO String builder, or make sure android does not execute this
		Assets.defaultFont.drawWrapped(spriteBatch, 
				"Map editor mode (F1)\n " +
				"Texture selected: " + selectedTileTexture, 
				MyGame.SCREEN_HALF_WIDTH, 
				MyGame.SCREEN_HEIGHT-20, 
				MyGame.SCREEN_HALF_WIDTH-20, 
				HAlignment.RIGHT);
	}
	
	/*
	 * Method for turning map editor on and off 
	 */
	public static void toggle(){
		enabled = !enabled;
		
		if (enabled) {
			editorWindow = new MapEditorWindow();
		} else {
			if (editorWindow != null) {
				editorWindow.setVisible(false);
				editorWindow.dispose();
			}
		}
	}
}
