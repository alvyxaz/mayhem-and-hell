package com.friendlyblob.mayhemandhell.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
import com.friendlyblob.mayhemandhell.client.network.packets.client.RegisterPacket;

public class RegistrationScreen extends BaseScreen{
	private GameWorld world;
	
	// UI related
    private Skin skin;
    private Stage stage;
    private SpriteBatch batch;
    
    // UI elements
    Image character;
    
    private int charId;
	TextureRegion [] textures;

    
	public RegistrationScreen(MyGame game) {
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

        batch = new SpriteBatch();
        stage = new Stage(MyGame.SCREEN_WIDTH, MyGame.SCREEN_HEIGHT);

        // A skin can be loaded via JSON or defined programmatically, either is fine. Using a skin is optional but strongly
        // recommended solely for the convenience of getting a texture, region, etc as a drawable, tinted drawable, etc.
        skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"));

        // Create a table that fills the screen. Everything else will go inside this table.
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);
        
        // Set up field table
        Table fieldTable = new Table();
        fieldTable.debug();
        root.add(fieldTable);
        // Set up character selection table
        Table charSelectionTable = new Table();
        charSelectionTable.debug();
        root.add(charSelectionTable);
        
        ImageButtonStyle imageButtonStyle = new ImageButtonStyle();
        imageButtonStyle.up = skin.newDrawable("white", Color.YELLOW);
        imageButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        skin.add("default", imageButtonStyle);
        
        
        Texture texture = Assets.manager.get("textures/characters/characters.png", Texture.class);
		textures = new TextureRegion[12];
        
		for (int i = 0; i < textures.length; i++) {
			textures[i] = new TextureRegion(texture, (i%3)*32, (i/3)*32, 32, 32);
		}
        
        ImageButton prevButton = new ImageButton(skin);
        prevButton.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (charId > 0) {
					charId--;
					System.out.println("prev " + charId);

				}
				
			}
		});
        
        ImageButton nextButton = new ImageButton(skin);
        nextButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (charId < textures.length) {
					character.setDrawable(new TextureRegionDrawable(textures[charId++]));
					System.out.println("next " + charId);
				}
				
			}
		});
        
        character = new Image(textures[charId]);
        
        charSelectionTable.add(prevButton).size(30);
        charSelectionTable.add(character);
        charSelectionTable.add(nextButton).size(30);

        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
        // TODO: modify fields to support placeholders
        final TextField usernameField = new TextField("Username", skin);
        final TextField passwordField = new TextField("Password", skin);
        final TextField passwordRepeatedField = new TextField("Repeat password", skin);
        passwordField.setPasswordMode(true);
        final TextButton registerButton = new TextButton("Register", skin);
        fieldTable.add(usernameField);
        fieldTable.row();
        fieldTable.add(passwordField);
        fieldTable.row();
        fieldTable.add(passwordRepeatedField);
        
        root.row();
        root.add(new Label("By registering..", skin)).colspan(2);
        root.row();
        root.add(registerButton).colspan(2);
        
        registerButton.addListener(new ChangeListener() {
        		@Override
                public void changed (ChangeEvent event, Actor actor) {
                        // validate inputs
        				// send
        			MyGame.connection.sendPacket(new RegisterPacket(usernameField.getText(), passwordField.getText(), passwordRepeatedField.getText(), charId));

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
	}
	
	public GameWorld getWorld() {
		return world;
	}

}
