package com.friendlyblob.mayhemandhell.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.helpers.Assets;

public class TestScreen extends BaseScreen {

	private TextureRegion slotTexture;
	
	public int tileWidth = 16;
	public int tileHeight = 16;
	
	public int [][] collisions = new int[9][9];
	public Pixmap pixmap;
	public Texture texture;
	
	public static int tempVerticalX;
	public static int tempVerticalY;
	public static int tempHorizontalX;
	public static int tempHorizontalY;
	
	public TestScreen(MyGame game) {
		super(game);	
		
		
		
		collisions[3][3] = 1;
		collisions[2][3] = 1;
		
		calculateDestination(2*tileWidth, 1*tileHeight, 4*tileWidth, 4*tileHeight);
		
		/* --------------------------------------------------------
		 * GRAPHICAL REPRESENTATION (not finished)
		 */
		// Drawing grid
		pixmap = new Pixmap(128, 128, Pixmap.Format.RGBA8888);
		
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
					pixmap.fillRectangle(x*tileWidth, y*tileWidth, tileWidth, tileHeight);
				}
			}
		}

		pixmap.setColor(Color.GREEN);
		
		// Line data
		int startX = 3;
		int startY = 3;
		int maxX = collisions[0].length*tileWidth -1;
		int maxY = collisions.length*tileWidth -1;
		
		for (int i = 0; i < 1; i++) {
			int endX = (int)(startX + Math.random()* (maxX-startX));
			int endY = (int)(startY + Math.random()* (maxY-startY));
		
			calculateDestination(startX, startY, endX, endY);
			
			if (sqrDistanceBetween(startX, startY, tempVerticalX, tempVerticalY) < 
					sqrDistanceBetween(startX, startY, tempHorizontalX, tempHorizontalY)) {
				pixmap.drawLine(startX, startY, tempVerticalX, tempVerticalY);
			} else {
				pixmap.drawLine(startX, startY, tempHorizontalX, tempHorizontalY);
			}
		}
		
		
		texture = new Texture(pixmap);
		
		
		/* --------------------------------------------------------
		 * 
		 */
	}
	
	public float calculateDestination( int currentX, int currentY, int targetX, int targetY) {
		// Getting tile information
		final int currentTileX = getTileXAt(currentX);
		final int currentTileY = getTileYAt(currentY);
		final int targetTileX = getTileXAt(targetX);
		final int targetTileY = getTileYAt(targetY);
		
		// Square distances (no need for root)
		int sqrDstToTarget = sqrDistanceBetween(currentX, currentY, targetX, targetY);
				
		// Tile that we have iterated to 
		int tileIterX;
		int tileIterY;
		
		// Results
		int verticalX = targetX;
		int verticalY = targetY;
		int horizontalX = targetX;
		int horizontalY = targetY;
		
		// TODO check if calculates an angle correctly
		double angle = Math.atan2(targetY - currentY, targetX - currentX);
		
		// Directions
		int xDirection = currentX < targetX ? 1 : -1;
		int yDirection = currentY < targetY ? 1 : -1;
		
		// Potential collision point
		int pointX;
		int pointY;
		
		/**
		 * HORIZONTAL INTERSECTION CHECK
		 */
		// 1. Finding a potential collision point 
		if (yDirection == 1) {
			pointY = (currentY/tileHeight) * tileHeight + tileHeight;
		} else {
			pointY = (currentY/tileHeight) * tileHeight +1;
		}
		
		pointX = (int)(currentX + (currentY - pointY) / -Math.tan(angle));
		
		// Checking if current tile is collision tile
		// TODO real collision check
		if (collisions[getTileYAt(pointY)][getTileXAt(pointX)] == 1 ) {
			horizontalX = pointX;
			horizontalY = pointY;
		} else {
			// 2-3 differences per vertical tile
			int dY = (yDirection == 1) ? tileHeight : -tileHeight;
			int dX = (int)(tileHeight/Math.tan(angle));
			
			// Looping until the end
			while (true) {
				pointX += dX;
				pointY += dY;
				tileIterX = getTileXAt(pointX);
				tileIterY = getTileYAt(pointY);
				
				// Checking if collision detected
				// TODO real collision check
				if (collisions[tileIterY][tileIterX] == 1) {
					horizontalX = pointX;
					horizontalY = pointY;
					break;
				} else {
					// Checking if it's time to exit the loop 
					// (went further than targeted to)
					if(sqrDistanceBetween(currentX, currentY, pointX, pointY) > sqrDstToTarget) {
						horizontalX = targetX;
						horizontalY = targetY;
						break;
					}
				}
			}
		}
		
		/**
		 * VERTICAL INTERSECTION CHECK
		 */
		// 1. Finding a potential collision point 
		if (xDirection == 1) {
			pointX = (currentX/tileWidth) * tileWidth + tileWidth;
		} else {
			pointX = (currentX/tileWidth) * tileWidth -1;
		}
		
		pointY = (int)(currentY + (currentX - pointX) * -Math.tan(angle));

		//pointY = getLinearYAtX(pointX, currentX, currentY, targetX, targetY); 
		
		// Checking if current tile is collision tile
		// TODO real collision check
		if (collisions[getTileYAt(pointY)][getTileXAt(pointX)] == 1 ) {
			verticalX = pointX;
			verticalY = pointY;
		} else {
			// 2-3 differences per horizontal tile
			int dX = (yDirection == 1) ? tileWidth : -tileWidth;
			int dY = (int)(tileHeight*Math.tan(angle));
			
			// Looping until the end
			while (true) {
				pointX += dX;
				pointY += dY;
				tileIterX = getTileXAt(pointX);
				tileIterY = getTileYAt(pointY);
				
				// Checking if collision detected
				// TODO real collision check
				if (collisions[tileIterY][tileIterX] == 1) {
					verticalX = pointX;
					verticalY = pointY;
					break;
				} else {
					// Checking if it's time to exit the loop 
					// (went further than targeted to)
					if(sqrDistanceBetween(currentX, currentY, pointX, pointY) > sqrDstToTarget) {
						verticalX = targetX;
						verticalY = targetY;
						break;
					}
				}
			}
		}
		
//		System.out.println("VERTICAL: " + verticalX + " " + verticalY);
//		System.out.println("HORIZONTAL: " + horizontalX + " " + horizontalY);
		
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
	
	public int sqrDistanceBetween(int currentX, int currentY, int targetX, int targetY) {
		return (targetX - currentX)*(targetX - currentX) + (targetY-currentY)*(targetY-currentY);
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
		
		spriteBatch.end();
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepare() {
		// TODO Auto-generated method stub
		
	}

}
