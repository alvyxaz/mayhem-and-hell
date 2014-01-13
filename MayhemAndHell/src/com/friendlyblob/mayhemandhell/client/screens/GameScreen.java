package com.friendlyblob.mayhemandhell.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.controls.Input;
import com.friendlyblob.mayhemandhell.client.entities.gui.EventNotifications;
import com.friendlyblob.mayhemandhell.client.entities.gui.GuiManager;
import com.friendlyblob.mayhemandhell.client.entities.gui.LiveNotifications;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
import com.friendlyblob.mayhemandhell.client.network.packets.client.ChatMessagePacket;

public class GameScreen extends BaseScreen{
	private GameWorld gameWorld;
	
	public LiveNotifications notifications;
	public EventNotifications eventNotifications;
	
	public GuiManager guiManager;
	
	// Music stuff
	private Music music;
	private final float MAX_VOLUME = .2f;
	private final float VOLUME_GROWTH_SPEED = .05f;
	private float currVolume;
		
	// UI related
    private Skin skin;
    private Stage stage;
    private Stage resumeStage;
    
    // TODO: find better solution
    private boolean touchedUiElement;
    
    private Table root;
    
    private int state;
    private static final int STATE_PLAYING = 0;
    private static final int STATE_PAUSE = 1;
	
	public GameScreen(MyGame game) {
		super(game);
		
		initGuiElements();
		initResumeGui();
		
		GameWorld.initialize();
		gameWorld = GameWorld.getInstance();
		gameWorld.setGame(game);
		
		notifications = new LiveNotifications();
		eventNotifications = new EventNotifications();
	}

	private void initResumeGui() {
		resumeStage = new Stage(MyGame.SCREEN_WIDTH, MyGame.SCREEN_HEIGHT);
		
		Table root = new Table();
        root.setFillParent(true);
        root.padTop(5);
        resumeStage.addActor(root);
        
        Label title = new Label("Tap to continue", skin);
        title.setColor(1f, 0.8f, 0.4f, 1);
        title.setFontScale(2);
        
        final TextButtonStyle redStyle = skin.get("red", TextButtonStyle.class);
        
        final TextButton button = new TextButton("Log out", redStyle);
        button.setSize(55, 25);
        button.setScale(2);
        
        button.addListener(new ChangeListener() {
    		@Override
            public void changed (ChangeEvent event, Actor actor) {
    			game.connection.setShutdownMessage("Successfully logged out");
    			game.connection.closeConnection();
            }
        });
        
        root.add(title).colspan(1);
        root.row();
        root.add(button).colspan(1);
        
	}
	
	private void initGuiElements() {		
        stage = new Stage(MyGame.SCREEN_WIDTH, MyGame.SCREEN_HEIGHT);
        
        skin = new Skin(Gdx.files.internal("data/ui2/uiskin.json"));

        // Create a table that fills the screen. Everything else will go inside this table.
        root = new Table();
        root.setFillParent(true);
        root.center().top();
        root.padTop(5);
        stage.addActor(root);
        
        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
        TextFieldStyle whiteStyle = skin.get("input_white", TextFieldStyle.class);
        final TextField chatMessageField = new TextField("", whiteStyle);
        chatMessageField.setMessageText("Say something!");
        chatMessageField.setVisible(false);
        
        chatMessageField.addListener(new ClickListener() {
        	@Override
        	public void clicked (InputEvent event, float x, float y) {
        		super.clicked(event, x, y);
        		touchedUiElement = true;
        	}
        });
        
        TextButtonStyle greenStyle = skin.get("green", TextButtonStyle.class);        
        final TextButton sayButton = new TextButton("Say", greenStyle);
        sayButton.setWidth(35);
        sayButton.setVisible(false);
        
		sayButton.addListener(new ChangeListener() {
				@Override
		        public void changed (ChangeEvent event, Actor actor) {
		    		touchedUiElement = true;
		    		// Send the packet with broadcast type
					MyGame.connection.sendPacket(new ChatMessagePacket(chatMessageField.getText()));
					// Lose the focus
					chatMessageField.setText("");
					stage.unfocusAll();
				}
		});

		Group textGroup = new Group();
		textGroup.addActor(chatMessageField);
		textGroup.size(chatMessageField.getWidth(), chatMessageField.getHeight());
		
		Group sayButtonGroup = new Group();
		sayButtonGroup.addActor(sayButton);
		sayButtonGroup.size(sayButton.getWidth(), sayButton.getHeight());
		
		HorizontalGroup horizontalGroup = new HorizontalGroup();
		horizontalGroup.addActor(textGroup);
		horizontalGroup.addActor(sayButtonGroup);
		horizontalGroup.setSpacing(5);
		
		root.add(horizontalGroup).expandX().padLeft(25);
		
        ButtonStyle toggleStyle = skin.get("button_chat", ButtonStyle.class);        
        final Button chatToggleButton = new Button(toggleStyle);
        
        chatToggleButton.addListener(new ChangeListener() {
				@Override
		        public void changed (ChangeEvent event, Actor actor) {
		    		touchedUiElement = true;
					
		    		boolean visible = chatMessageField.isVisible();
		    		
		    		chatMessageField.setVisible(!visible);
		    		sayButton.setVisible(!visible);
				}
		});
        
        root.add(chatToggleButton).right().padRight(5);
	}

	@Override
	public void draw(float deltaTime) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		spriteBatch.begin();
		/*---------------------------------------
		 * World
		 */
		gameWorld.draw(spriteBatch);
		
		spriteBatch.end();
		/*---------------------------------------
		 * GUI Elements
		 */
		spriteBatch.begin();
		spriteBatch.setProjectionMatrix(guiCam.combined);
		
//		Assets.defaultFont.draw(spriteBatch, fpsText, 20, 20);
		
		if (state == STATE_PAUSE) {
			spriteBatch.setColor(0, 0, 0, 0.7f);
			spriteBatch.draw(Assets.px, 0, 0, MyGame.SCREEN_WIDTH, MyGame.SCREEN_HEIGHT);
			spriteBatch.setColor(Color.WHITE);
		}
		
		spriteBatch.end();
		
        switch (state) {
			case STATE_PLAYING:
				stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
				stage.draw();
				break;
			case STATE_PAUSE:
				resumeStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
				resumeStage.draw();
				break;
		}
		
	}
	
	public void startPauseState() {
		state = STATE_PAUSE;
		Gdx.input.setInputProcessor(resumeStage);
	}
	
	public void startGameplayState() {
		state = STATE_PLAYING;
		Gdx.input.setInputProcessor(stage);
	}
	
	@Override
	public void update(float deltaTime) {
		if (Gdx.input.isKeyPressed(Keys.BACK) || Gdx.input.isKeyPressed(Keys.ESCAPE)){
			startPauseState();
		}
		
		gameWorld.update(deltaTime);

		
		if (currVolume < MAX_VOLUME) {
			currVolume += deltaTime * VOLUME_GROWTH_SPEED;
			music.setVolume(currVolume);	
		}
		
		switch (state) {
			case STATE_PLAYING:
				if (!touchedUiElement) {
					gameWorld.updateWorldInput();
				}
				
				touchedUiElement = false;
				break;
			case STATE_PAUSE:
				if (Input.isReleasing()) {
					startGameplayState();
				}
				break;
		}
	}
	
	@Override
	public void resume() {
		startPauseState();
	}

	@Override
	public void prepare() {
		currVolume = 0;
		
		music = Assets.manager.get("sounds/bg.wav");
		music.setLooping(true);
		music.setVolume(currVolume);
		music.stop();
		music.play();
		
		Gdx.input.setInputProcessor(stage);
	}
	
	public GameWorld getWorld() {
		return gameWorld;
	}

}
