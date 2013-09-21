package com.friendlyblob.mayhemandhell.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.controls.Input;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class TestScreen extends BaseScreen {

	private TextureRegion slotTexture;
	
	public int tileWidth = 16;
	public int tileHeight = 16;
	
	public int [][] collisions = new int[8][9];
	public Pixmap pixmap;
	public Texture texture;
	
	public static int tempVerticalX;
	public static int tempVerticalY;
	public static int tempHorizontalX;
	public static int tempHorizontalY;
	
	public static int activeTileX;
	public static int activeTileY;
	
	public TestScreen(MyGame game) {
		super(game);	
		
		collisions[3][3] = 1;
		collisions[2][3] = 1;
		collisions[2][4] = 1;
		collisions[2][5] = 1;
		collisions[3][5] = 1;
		
		redraw(3, 3);
	}
	
	public void redraw(int startX, int startY) {
		/* --------------------------------------------------------
		 * GRAPHICAL REPRESENTATION (not finished)
		 */
		// Drawing grid
		pixmap = new Pixmap(128, 128, Pixmap.Format.RGBA8888);
		
		int maxX = collisions[0].length*tileWidth -1;
		int maxY = collisions.length*tileWidth -1;
		
		pixmap.setColor(Color.DARK_GRAY);
		for (int y = 0; y < collisions.length; y++) {
			pixmap.drawLine(0, y*tileHeight, tileWidth*collisions[0].length, y*tileHeight);
			
		}
		
		for (int x = 0; x < collisions[0].length; x++) {
			pixmap.drawLine(x*tileWidth, 0, x*tileWidth, tileWidth * collisions.length);
		}
		
		for (int y = 0; y < collisions.length; y++) {
			for (int x = 0; x < collisions[0].length; x++) {
				if (collisions[y][x] == 1) {
					pixmap.fillRectangle(x*tileWidth, maxY - y*tileHeight - tileHeight, tileWidth, tileHeight);
				}
			}
		}

		pixmap.setColor(Color.GREEN);
		
		
		for (int i = 0; i < 60; i++) {
			int endX = (int)(Math.random()*maxX);
			int endY = (int)(Math.random()*maxY);
		
			pixmap.setColor(Color.RED);
			pixmap.drawLine(startX, maxY-startY, endX, maxY-endY);
			pixmap.setColor(Color.GREEN);
			
//			System.out.println("Data: " + startX + " " + startY + " -  " + endX + " " + endY);
			
			calculateDestination(startX, startY, endX, endY);
			
			if (sqrDistanceBetween(startX, startY, tempVerticalX, tempVerticalY) < 
					sqrDistanceBetween(startX, startY, tempHorizontalX, tempHorizontalY)) {
				pixmap.drawLine(startX, maxY-startY, tempVerticalX, maxY-tempVerticalY);
			} else {
				pixmap.drawLine(startX, maxY-startY, tempHorizontalX, maxY-tempHorizontalY);
			}
		}
				
		texture = new Texture(pixmap);
	}
	
	public float calculateDestination( int currentX, int currentY, int targetX, int targetY) {
		// Tile that we have iterated to 
		int tileIterX;
		int tileIterY;
		
		// Results
		int verticalX = targetX;
		int verticalY = targetY;
		int horizontalX = targetX;
		int horizontalY = targetY;
				
		// Directions
		int xDirection = currentX < targetX ? 1 : -1;
		int yDirection = currentY < targetY ? 1 : -1;
		
		// Potential collision point
		int pointX;
		int pointY;
		
		int tilesDX = Math.abs(getTileXAt(targetX) - getTileXAt(currentX));
		int tilesDY = Math.abs(getTileYAt(targetY) - getTileYAt(currentY));
		
	
		/**---------------------------------------------------
		 * VERTICAL
		 */
		
		if (xDirection == 1) {
			pointX = (currentX/tileWidth) * tileWidth + tileWidth;
		} else {
			pointX = (currentX/tileWidth) * tileWidth -1;
		}
		
		pointY = getLinearYAtX(pointX, currentX, currentY, targetX, targetY);
		
		if (tilesDX != 0) {
			tileIterX = getTileXAt(pointX);
			tileIterY = getTileYAt(pointY);

			// Checking if current tile is collision tile
			// TODO real collision check
			if (collisions[tileIterY][tileIterX] == 1 ) {
				verticalX = pointX;
				verticalY = pointY;
			} else {
				int dX = (xDirection == 1) ? tileWidth : -tileWidth;

				for (int i = 1; i < tilesDX; i++) {
					pointX += dX;
					pointY = getLinearYAtX(pointX, currentX, currentY, targetX, targetY);
					
					tileIterX = getTileXAt(pointX);
					tileIterY = getTileYAt(pointY);
					
					if (collisions[tileIterY][tileIterX] == 1 ) {
						verticalX = pointX;
						verticalY = pointY;
						break;
					}
					
				}
			}
		}
		
		/**---------------------------------------------------
		 * HORIZONTAL
		 */
		
		if (yDirection == 1) {
			pointY = (currentY/tileHeight) * tileHeight + tileHeight;
		} else {
			pointY = (currentY/tileHeight) * tileHeight -1;
		}
		
		pointX = getLinearXAtY(pointY, currentX, currentY, targetX, targetY);
		
		if (tilesDY != 0) {
			tileIterX = getTileXAt(pointX);
			tileIterY = getTileYAt(pointY);
			
			// Checking if current tile is collision tile
			// TODO real collision check
			if (collisions[tileIterY][tileIterX] == 1 ) {
				horizontalX = pointX;
				horizontalY = pointY;
			} else {
				int dY = (yDirection == 1) ? tileHeight : -tileHeight;
				
				for (int i = 1; i < tilesDY; i++) {
					pointY += dY;
					pointX = getLinearXAtY(pointY, currentX, currentY, targetX, targetY);
					
					tileIterX = getTileXAt(pointX);
					tileIterY = getTileYAt(pointY);
					
					if (collisions[tileIterY][tileIterX] == 1 ) {
						horizontalX = pointX;
						horizontalY = pointY;
						break;
					}
				}
			}
		}
		
		// TODO REMOVE
		tempHorizontalX = horizontalX;
		tempVerticalX = verticalX;
		tempHorizontalY = horizontalY;
		tempVerticalY = verticalY;
		
		return 0;
	}
	
	public int getTileXAt(int x) {
		return x/tileWidth;
	}
	
	public int getTileYAt(int y) {
		return y/tileHeight;
	}
	
	public int getTileAt(int x, int y) {
		return 0;
	}
	
	public float sqrDistanceBetween(float startX, float startY, float endX, float endY) {
		return (endX - startX)*(endX - startX) + (endY-startY)*(endY-startY);
	}
	
	public int getLinearYAtX(int x, float currentX, int currentY, int targetX, int targetY) {
		return (int)(((targetY-currentY)/(targetX-currentX))*(x-currentX) + currentY);
	}
	
	public int getLinearXAtY(int y, int currentX, float currentY, int targetX, int targetY) {
		return (int)((targetX-currentX)*(y-currentY)/(targetY-currentY) + currentX);
	}
	
	public float angleBetween(int currentX, int currentY, int targetX, int targetY) {
		return (float)Math.atan2(targetY-currentY, targetX-currentX);
	}

	@Override
	public void draw(float deltaTime) {
		spriteBatch.begin();
		
		spriteBatch.draw(texture, 0, 0);
		
		spriteBatch.setColor(Color.CYAN);
		spriteBatch.draw(Assets.px, activeTileX, activeTileY, 16, 16);
		spriteBatch.setColor(Color.WHITE);
		
		spriteBatch.end();
	}
	
	@Override
	public void update(float deltaTime) {
		if (Gdx.input.isTouched()) {
			redraw(Input.getX(),Input.getY());
		}

	}

	@Override
	public void prepare() {
		// TODO Auto-generated method stub
		
	}

}
