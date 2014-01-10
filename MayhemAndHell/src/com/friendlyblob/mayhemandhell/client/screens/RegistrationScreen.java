package com.friendlyblob.mayhemandhell.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.tablelayout.Cell;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.entities.gui.CharacterViewer;
import com.friendlyblob.mayhemandhell.client.entities.gui.MenuBackground;
import com.friendlyblob.mayhemandhell.client.gameworld.GameWorld;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;
import com.friendlyblob.mayhemandhell.client.network.packets.client.RegistrationPacket;

public class RegistrationScreen extends BaseScreen{
	private GameWorld world;
	
	// UI related
    private Skin skin;
    private Stage stage;
    private SpriteBatch batch;
    
    // UI elements
    private Image character;
    private Cell notificationLabelCell;
    private Label notificationLabel;
    
    private CharacterViewer characterViewer;

	private MenuBackground background;
    
	public RegistrationScreen(MyGame game) {
		super(game);
		
		initGuiElements();
	}

	private void initGuiElements() {
		background = new MenuBackground();
		
        stage = new Stage(MyGame.SCREEN_WIDTH, MyGame.SCREEN_HEIGHT);

        skin = new Skin(Gdx.files.internal("data/ui2/uiskin.json"));

        // Set up root table
        Table root = new Table();
        root.setFillParent(true);
//        root.debug();
        stage.addActor(root);
        
        // Set up field table
        Table fieldTable = new Table();
        root.add(fieldTable).left();
        fieldTable.debug();
        // Set up character selection table

        // Add field elements
        final TextField usernameField = new TextField("", skin);
        usernameField.setMessageText("Username");
		usernameField.setMaxLength(15);
        
        final TextField passwordField = new TextField("", skin);
        passwordField.setMessageText("Password");
        passwordField.setPasswordCharacter('*');
        passwordField.setPasswordMode(true);
        passwordField.setMaxLength(15);
        
        final TextField passwordRepeatedField = new TextField("", skin);
        passwordRepeatedField.setMessageText("Repeat password");
        passwordRepeatedField.setPasswordCharacter('*');
        passwordRepeatedField.setPasswordMode(true);
        passwordRepeatedField.setMaxLength(15);
        
        Label title = new Label("User Registration", skin);
        title.setColor(0.8f, 0.58f, 0.33f, 1);
        title.setFontScale(2);
        
        ImageButtonStyle imageButtonStyle = new ImageButtonStyle();
        skin.add("default", imageButtonStyle);
        
		final Image leftArrow = new Image(Assets.getTextureRegion("gui/arrow_left"));
        final Image rightArrow = new Image(Assets.getTextureRegion("gui/arrow_right"));
		
        ButtonStyle btnStyle = new ButtonStyle();
        
        final Button prevButton = new Button(btnStyle);
        prevButton.add(leftArrow);
        
        prevButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				characterViewer.prev();
			}
		});
        
        final Button nextButton = new Button(btnStyle);
        nextButton.add(rightArrow);
        
        nextButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				characterViewer.next();
			}
		});
        
        fieldTable.add(title).colspan(4).left().padBottom(6);
        fieldTable.row();
        fieldTable.add(usernameField).colspan(4).size(120, 25).padBottom(10).left();
        fieldTable.row();
        fieldTable.add(passwordField).size(120, 25).padBottom(10).left();
        
        fieldTable.add(prevButton).size(30).padRight(2).padTop(-15).padLeft(25);
        
        characterViewer = new CharacterViewer("textures/characters/characters-select.png");
        fieldTable.add(characterViewer).padTop(-15);
        
        fieldTable.add(nextButton).size(30).padLeft(2).padTop(-15);
        
        fieldTable.row();
        fieldTable.add(passwordRepeatedField).colspan(4).size(120, 25).left();
        
        final TextButtonStyle redStyle = skin.get("red", TextButtonStyle.class);
        
        final TextButton backButton = new TextButton("I'm out!", redStyle);
        backButton.setSize(55, 25);
        
        backButton.addListener(new ChangeListener() {
    		@Override
            public void changed (ChangeEvent event, Actor actor) {
    			game.setScreen(game.screenLogin);
            }
        });
        
        final TextButton registerButton = new TextButton("Complete registration", skin);
        registerButton.setSize(130, 25);
        
        registerButton.addListener(new ChangeListener() {
    		@Override
            public void changed (ChangeEvent event, Actor actor) {
    			hideNoticeMessage();
    			MyGame.connection.sendPacket(new RegistrationPacket(usernameField.getText(), passwordField.getText(), passwordRepeatedField.getText(), characterViewer.getCharId()));
            }
        });
        
        root.row().padTop(5);
        notificationLabel = new Label("", skin);
        notificationLabel.setWrap(true);
        notificationLabel.setWidth(250);
        
        notificationLabelCell = root.add().colspan(2).padBottom(5).left().width(notificationLabel.getWidth());
        root.row();
        root.add(new Label("By registering, you agree that you understand that this \n" +
        		"app is just for fun, and it is not a subject of bashing. Be cool.", skin)).colspan(2).padBottom(5);
        root.row();
        
        // TODO make this cleaner?
        Group backButtonContainer = new Group();
        backButtonContainer.addActor(backButton);
        backButtonContainer.size(backButton.getWidth(), backButton.getHeight());
        
        Group registerButtonContainer = new Group();
        registerButtonContainer.addActor(registerButton);
        registerButtonContainer.size(registerButton.getWidth(), registerButton.getHeight());
        
        final HorizontalGroup buttonGroup = new HorizontalGroup();
        buttonGroup.addActor(backButtonContainer);
        buttonGroup.addActor(registerButtonContainer);
        buttonGroup.setSpacing(8);
        
        root.add(buttonGroup).colspan(2).left();
	}

	public void showNoticeMessage(String message) {
		notificationLabel.setText(message);
		notificationLabelCell.setWidget(notificationLabel);
	}
	
	public void hideNoticeMessage() {
		if (notificationLabelCell.getWidget() != null) {
			notificationLabelCell.setWidget(null);
		}
	}
	
	@Override
	public void draw(float deltaTime) {
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
	public void prepare() {
		Gdx.input.setInputProcessor(stage);
	}
	
	

}
