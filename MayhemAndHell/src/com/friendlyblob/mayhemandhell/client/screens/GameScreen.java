package com.friendlyblob.mayhemandhell.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.entities.gui.ChatBubbleNotifications;
import com.friendlyblob.mayhemandhell.client.entities.gui.EventNotifications;
import com.friendlyblob.mayhemandhell.client.entities.gui.GuiManager;
import com.friendlyblob.mayhemandhell.client.entities.gui.LiveNotifications;
import com.friendlyblob.mayhemandhell.client.entities.gui.MenuBackground;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
import com.friendlyblob.mayhemandhell.client.mapeditor.MapEditor;
import com.friendlyblob.mayhemandhell.client.network.packets.client.ChatMessagePacket;
import com.friendlyblob.mayhemandhell.client.network.packets.client.LoginPacket;

public class GameScreen extends BaseScreen{
	private GameWorld gameWorld;
	
	public GuiManager guiManager;
	
	public LiveNotifications notifications;
	public EventNotifications eventNotifications;
		
	// UI related
    private Skin skin;
    private Stage stage;
    private SpriteBatch batch;
    
    // TODO: find better solution
    private boolean touchedUiElement;
    
    private Table root;
	
	public GameScreen(MyGame game) {
		super(game);
		
		initGuiElements();

		
		GameWorld.initialize();
		gameWorld = GameWorld.getInstance();
		gameWorld.setGame(game);
		
		
		notifications = new LiveNotifications();
		eventNotifications = new EventNotifications();
//		
//		game.connectToServer();
//		
//		// Temporary GUI implementation 
//		guiManager = new GuiManager();
		
	}

	private void initGuiElements() {		
        batch = new SpriteBatch();
        stage = new Stage(MyGame.SCREEN_WIDTH, MyGame.SCREEN_HEIGHT);
        
        skin = new Skin(Gdx.files.internal("data/ui2/uiskin.json"));

        // Create a table that fills the screen. Everything else will go inside this table.
        root = new Table();
        root.setFillParent(true);
        root.center().top();
        root.padTop(5);
        stage.addActor(root);
        
        root.debug();
        
        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
        final TextField chatMessageField = new TextField("", skin);
        chatMessageField.setMessageText("Say something!");

        TextButtonStyle greenStyle = skin.get("green", TextButtonStyle.class);        
        final TextButton loginButton = new TextButton("Say", greenStyle);
        
        root.add(chatMessageField).padRight(5);
        root.add(loginButton).width(35);
                
        loginButton.addListener(new ChangeListener() {
        		@Override
                public void changed (ChangeEvent event, Actor actor) {
            		touchedUiElement = true;
            		// Send the packet with broadcast type
        			MyGame.connection.sendPacket(new ChatMessagePacket("/b " + chatMessageField.getText()));
        			// Lose the focus
        			chatMessageField.setText("");
        			stage.unfocusAll();
        		}
        });
        
        chatMessageField.addListener(new ClickListener() {
        	
        	@Override
        	public void clicked (InputEvent event, float x, float y) {
        		super.clicked(event, x, y);
        		touchedUiElement = true;
        	}
        });
	}

	@Override
	public void draw(float deltaTime) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		spriteBatch.begin();
		/*---------------------------------------
		 * World
		 */
		gameWorld.draw(spriteBatch);
		
		notifications.draw(spriteBatch, deltaTime);
		
		spriteBatch.end();
		/*---------------------------------------
		 * GUI Elements
		 */
		spriteBatch.begin();
		spriteBatch.setProjectionMatrix(guiCam.combined);
		
		Assets.defaultFont.draw(spriteBatch, fpsText, 20, 20);
		
		spriteBatch.end();
		
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
	}
	
	@Override
	public void update(float deltaTime) {
		gameWorld.update(deltaTime);

		if (!touchedUiElement) {
			gameWorld.updateWorldInput();
		}
		
		touchedUiElement = false;
	}

	@Override
	public void prepare() {
		Gdx.input.setInputProcessor(stage);
	}
	
	public GameWorld getWorld() {
		return gameWorld;
	}

}
