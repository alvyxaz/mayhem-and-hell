package com.friendlyblob.mayhemandhell.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.entities.gui.GuiManager;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class RegisterScreen extends BaseScreen{
	private GameWorld world;
	
	// UI related
    private Skin skin;
    private Stage stage;
    private SpriteBatch batch;
	
	public RegisterScreen(MyGame game) {
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

        batch = new SpriteBatch();
        stage = new Stage();

        // A skin can be loaded via JSON or defined programmatically, either is fine. Using a skin is optional but strongly
        // recommended solely for the convenience of getting a texture, region, etc as a drawable, tinted drawable, etc.
        skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"));

        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        
        table.debug();

        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
        final Label usernameLabel = new Label("Username:", skin);
        final TextField usernameField = new TextField("", skin);
        final Label passwordLabel = new Label("Password:", skin);
        final TextField passwordField = new TextField("", skin);
        final Label passwordRepeatedLabel = new Label("Repeat password:", skin);
        final TextField passwordRepeatedField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        final TextButton registerButton = new TextButton("Register", skin);
        table.add(usernameLabel);
        table.add(usernameField);
        table.row();
        table.add(passwordLabel);
        table.add(passwordField);
        table.row();
        table.add(passwordRepeatedLabel);
        table.add(passwordRepeatedField);
        table.row();
        table.add(registerButton).colspan(2);
        
        registerButton.addListener(new ChangeListener() {
        		@Override
                public void changed (ChangeEvent event, Actor actor) {
                        System.out.println("click");
                }

        });

	}

	@Override
	public void draw(float deltaTime) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        Table.drawDebug(stage);
	}
	
	@Override
	public void update(float deltaTime) {

	}

	@Override
	public void prepare() {
		Gdx.input.setInputProcessor(stage);
		System.out.println("PREPARE");
	}
	
	public GameWorld getWorld() {
		return world;
	}

}
