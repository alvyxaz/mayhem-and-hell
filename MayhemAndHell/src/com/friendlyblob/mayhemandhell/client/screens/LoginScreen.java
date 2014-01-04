package com.friendlyblob.mayhemandhell.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.entities.gui.GuiManager;
import com.friendlyblob.mayhemandhell.client.entities.gui.MenuBackground;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
import com.friendlyblob.mayhemandhell.client.network.packets.client.LoginPacket;
import com.friendlyblob.mayhemandhell.client.network.packets.client.RegisterPacket;

public class LoginScreen extends BaseScreen{
	private GameWorld world;
		
	// UI related
    private Skin skin;
    private Stage stage;
    private SpriteBatch batch;
	
    private MenuBackground background;
    
	public LoginScreen(MyGame game) {
		super(game);
		
		initGuiElements();
		
//		GameWorld.initialize();
//		world = GameWorld.getInstance();
//		world.setGame(game);
		
		game.connectToServer();
		
		// Temporary GUI implementation 
//		guiManager = new GuiManager();
	}

	private void initGuiElements() {

		background = new MenuBackground();
		
        batch = new SpriteBatch();
        stage = new Stage(MyGame.SCREEN_WIDTH, MyGame.SCREEN_HEIGHT);
        
        // A skin can be loaded via JSON or defined programmatically, either is fine. Using a skin is optional but strongly
        // recommended solely for the convenience of getting a texture, region, etc as a drawable, tinted drawable, etc.
        skin = new Skin(Gdx.files.internal("data/ui2/uiskin.json"));

        // Create a table that fills the screen. Everything else will go inside this table.
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);
        
        root.debug();
        
        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
        final TextField usernameField = new TextField("", skin);
        usernameField.setMessageText("Username");
        
        final TextField passwordField = new TextField("", skin);
        passwordField.setMessageText("Password");
        passwordField.setPasswordCharacter('*');
        passwordField.setPasswordMode(true);
        
        TextButtonStyle greenStyle = skin.get("green", TextButtonStyle.class);
        TextButtonStyle redStyle = skin.get("red", TextButtonStyle.class);
        
        final TextButton loginButton = new TextButton("Hop in!", greenStyle);
        final TextButton registerLabel = new TextButton("Register", redStyle);
        
        Image image = new Image(Assets.getTextureRegion("gui/logo"));
        root.add(image).padBottom(10).colspan(2);
        root.row();
        root.add(usernameField).colspan(2).padBottom(10).height(25);
        root.row();
        root.add(passwordField).colspan(2).padBottom(18).height(25);
        root.row();
        root.add(loginButton).colspan(1).width(60).height(25);
        root.add(registerLabel).colspan(1).width(60).height(25);
        
        usernameField.setHeight(30);
        
        loginButton.addListener(new ChangeListener() {
        		@Override
                public void changed (ChangeEvent event, Actor actor) {
                        // Send login request
        			System.out.println("clicked");
        			MyGame.connection.sendPacket(new LoginPacket(usernameField.getText(), passwordField.getText()));
                }

        });
        
        
        registerLabel.addListener(new ChangeListener() {
        	@Override
            public void changed (ChangeEvent event, Actor actor) {
        		System.out.println("clicked");
        		// show register
        		game.setScreen(game.screenRegister);
        	}

        });

	}

	@Override
	public void draw(float deltaTime) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Drawing a background
        spriteBatch.begin();
        background.draw(spriteBatch, deltaTime);
        spriteBatch.end();
        
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
//        Table.drawDebug(stage);
	}
	
	@Override
	public void update(float deltaTime) {

	}

	@Override
	public void show() {
		
	}
	
	@Override
	public void prepare() {
		Gdx.input.setInputProcessor(stage);
	}

}
