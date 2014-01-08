package com.friendlyblob.mayhemandhell.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.FloatAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.tablelayout.Cell;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.entities.gui.MenuBackground;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
import com.friendlyblob.mayhemandhell.client.network.packets.client.LoginPacket;

public class LoginScreen extends BaseScreen{
	private GameWorld world;
		
	// UI related
    private Skin skin;
    private Stage stage;
    private SpriteBatch batch;
	
    private MenuBackground background;
    
    // gui elements
    private Label errorLabel;
    
    private Table root;
    private Cell errorLabelCell;
    
	public LoginScreen(MyGame game) {
		super(game);
		
		initGuiElements();
		
//		GameWorld.initialize();
//		world = GameWorld.getInstance();
//		world.setGame(game);
		
//		game.connectToServer();
		
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
        root = new Table();
        root.setFillParent(true);
        stage.addActor(root);
        
        root.debug();
        
        // add logo
        
        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
        final TextField usernameField = new TextField("", skin);
        usernameField.setMessageText("Username");
        usernameField.setText("Alvys");

        final TextField passwordField = new TextField("", skin);
        passwordField.setMessageText("Password");
        passwordField.setPasswordCharacter('*');
        passwordField.setPasswordMode(true);
        passwordField.setText("alvys");
        
        TextButtonStyle greenStyle = skin.get("green", TextButtonStyle.class);
        TextButtonStyle redStyle = skin.get("red", TextButtonStyle.class);
        
        final TextButton loginButton = new TextButton("Hop in!", greenStyle);
        final TextButton registerButton = new TextButton("Register", redStyle);
        
        errorLabel = new Label("Wrong username and/or password", skin);
        
        Image image = new Image(Assets.getTextureRegion("gui/logo"));
        
        // move up
        MoveByAction moveByAction = Actions.moveBy(0, 2, .7f, Interpolation.sineIn);
        MoveByAction moveByAction2 = Actions.moveBy(0, 2, .7f, Interpolation.sineOut);

        // move down
        MoveByAction moveByActionBack = Actions.moveBy(0, -2, .7f, Interpolation.sineIn);
        MoveByAction moveByActionBack2 = Actions.moveBy(0, -2, .7f, Interpolation.sineOut);

        // move up then down
        SequenceAction sequence = Actions.sequence(moveByAction, moveByAction2, moveByActionBack, moveByActionBack2);
        // repeat forever
        RepeatAction foreverAction = Actions.forever(sequence);
        image.addAction(foreverAction);
        
        
        
        root.add(image).padBottom(10).colspan(2);
        root.row();
        root.add(usernameField).colspan(2).padBottom(10).height(25);
        root.row();
        root.add(passwordField).colspan(2).padBottom(10).height(25);
        root.row();
        errorLabelCell = root.add().colspan(2).padBottom(5);
        root.row();
        root.add(loginButton).width(60).height(25);
        root.add(registerButton).width(60).height(25);
        
        usernameField.setHeight(30);
        
        loginButton.addListener(new ChangeListener() {
        		@Override
                public void changed (ChangeEvent event, Actor actor) {
        			hideErrorMessage();

        			MyGame.connection.sendPacket(new LoginPacket(usernameField.getText(), passwordField.getText()));
//        			MyGame.connection.sendPacket(new ClientVersion(5));
        		}

        });
        
        
        registerButton.addListener(new ChangeListener() {
        	@Override
            public void changed (ChangeEvent event, Actor actor) {
        		game.setScreen(game.screenRegister);
        	}

        });

	}
	
	public void showErrorMessage(String message) {
		// TODO: reuse label?
		errorLabelCell.setWidget(new Label(message, skin));
	}
	
	public void hideErrorMessage() {
		if (errorLabelCell.getWidget() != null) {
			errorLabelCell.setWidget(null);
		}
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
