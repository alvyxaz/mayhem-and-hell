package com.friendlyblob.mayhemandhell.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.friendlyblob.mayhemandhell.client.MyGame;

public abstract class BaseScreen implements Screen {
	MyGame game;
	SpriteBatch spriteBatch;
	
	int firstFramesToSkip = 5;
	
	protected StringBuilder fpsText;
	private BaseScreen screenToSwitchTo;
	public boolean isSwitchInProgress = false;
	
	public OrthographicCamera guiCam;
	
	public BaseScreen(MyGame game){
		this.game = game;
		spriteBatch = new SpriteBatch(300);
		fpsText = new StringBuilder();

		// Setting up GUI camera so that bottom left corner is 0 0
		guiCam = new OrthographicCamera(MyGame.SCREEN_WIDTH, MyGame.SCREEN_HEIGHT);
		guiCam.translate(MyGame.SCREEN_WIDTH/2, MyGame.SCREEN_HEIGHT/2);
		guiCam.update();
		
		spriteBatch.setProjectionMatrix(guiCam.combined);
	}
	
	public abstract void draw(float deltaTime);
	
	public abstract void update(float deltaTime);
	
	public abstract void prepare();
	
	@Override
	public void render(float deltaTime) {
		if(firstFramesToSkip > 0){
			firstFramesToSkip--;
			return;
		}
		
		update(deltaTime);
		
		Gdx.graphics.getGLCommon().glClear(GL10.GL_COLOR_BUFFER_BIT);
		draw(deltaTime);
		
		timePassed += Gdx.graphics.getDeltaTime();
		fpsCount++;
		if(timePassed > 1f){
			timePassed -= 1f;
			fps = fpsCount;
			fpsCount = 0;
			
			// Updating FPS string
			fpsText.setLength(0);
			fpsText.append("FPS: ");
			fpsText.append(fps);
		}
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
	}
	
	float timePassed = 0.0f;
	int fpsCount = 0;
	public int fps = 0;

}
