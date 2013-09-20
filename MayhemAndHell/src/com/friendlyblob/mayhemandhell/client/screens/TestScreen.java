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
	
	public TestScreen(MyGame game) {
		super(game);	
		
		collisions[3][3] = 1;
		collisions[2][3] = 1;
		
		redraw(3, 3, 60, 60);
	}
	
	public void redraw(int startX, int startY, int testX, int testY) {
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
		
		
		for (int i = 0; i < 10; i++) {
			int endX = testX; // (int)(startX + Math.random()* (maxX-startX));
			int endY = testY; //(int)(startY + Math.random()* (maxY-startY));
		
			pixmap.setColor(Color.RED);
			pixmap.drawLine(startX, maxY-startY, endX, maxY-endY);
			pixmap.setColor(Color.GREEN);
			
			System.out.println("Data: " + startX + " " + startY + " -  " + endX + " " + endY);
			
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
		// Getting tile information
		final int currentTileX = getTileXAt(currentX);
		final int currentTileY = getTileYAt(currentY);
		final int targetTileX = getTileXAt(targetX);
		final int targetTileY = getTileYAt(targetY);
		
		// Square distances (no need for root)
		int sqrDstToTarget = (int)sqrDistanceBetween(currentX, currentY, targetX, targetY);
		
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
		
		System.out.println(Math.toDegrees(angle));
		
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
		
		
		if (pointX < collisions[0].length * tileWidth && pointX > 0) {

			tileIterX = getTileXAt(pointX);
			tileIterY = getTileYAt(pointY);
			
			// Checking if current tile is collision tile
			// TODO real collision check
			if (collisions[tileIterY][tileIterX] == 1 ) {
				horizontalX = pointX;
				horizontalY = pointY;
			} else {
				// 2-3 differences per vertical tile
				int dY = (yDirection == 1) ? tileHeight : -tileHeight;
				int dX = (int)(tileHeight/Math.tan(angle));
				
				System.out.println("---HOR---");
				
				// Looping until the end
				while (true) {
					pointX += dX;
					pointY += dY;

					tileIterX = getTileXAt(pointX);
					tileIterY = getTileYAt(pointY);
					
					System.out.println(tileIterX + " " + tileIterY);
					
					// Checking if it's time to exit the loop 
					// (went further than targeted to)
					if(sqrDistanceBetween(currentX, currentY, pointX, pointY) > sqrDstToTarget) {
						horizontalX = targetX;
						horizontalY = targetY;
						break;
					}
					
					// If reached the limit of tilemap
					// Move a step back to the place where it wasn't out of bounds
					if (tileIterX >= collisions[0].length || tileIterY >= collisions.length
							|| tileIterX < 0 || tileIterY < 0) {
						horizontalX = pointX - dX;
						horizontalY = pointY - dY;
						break;
					}
					
					// Checking if collision detected
					// TODO real collision check
					if (collisions[tileIterY][tileIterX] == 1) {
						horizontalX = pointX;
						horizontalY = pointY;
						break;
					} else {
						
					}
				}
			}
		}
		
		/**
		 * VERTICAL INTERSECTION CHECK
		 */
		
		System.out.println("---VERT");
		// 1. Finding a potential collision point 
		if (xDirection == 1) {
			pointX = (currentX/tileWidth) * tileWidth + tileWidth;
		} else {
			pointX = (currentX/tileWidth) * tileWidth -1;
		}
		
		pointY = (int)(currentY + (currentX - pointX) * -Math.tan(angle));

		if (pointY < collisions.length*tileHeight && pointY > 0) {
			
			
			tileIterX = getTileXAt(pointX);
			tileIterY = getTileYAt(pointY);
			
			// Checking if current tile is collision tile
			// TODO real collision check
			if (collisions[tileIterY][tileIterX] == 1 ) {
				verticalX = pointX;
				verticalY = pointY;
			} else {
				// 2-3 differences per horizontal tile
				int dX = (xDirection == 1) ? tileWidth : -tileWidth;
				int dY = (int)(tileHeight*Math.tan(angle));
				
				// Looping until the end
				while (true) {
					pointX += dX;
					pointY += dY;
					tileIterX = getTileXAt(pointX);
					tileIterY = getTileYAt(pointY);
					
					System.out.println(tileIterX + " " + tileIterY);
					
					// Checking if it's time to exit the loop 
					// (went further than targeted to)
					if(sqrDistanceBetween(currentX, currentY, pointX, pointY) > sqrDstToTarget) {
						verticalX = targetX;
						verticalY = targetY;
						break;
					}
					
					// If reached the limit of tilemap
					// Move a step back to the place where it wasn't out of bounds
					if (tileIterX > collisions[0].length || tileIterY > collisions.length
							|| tileIterX < 0 || tileIterY < 0) {
						verticalX = pointX - dX;
						verticalY = pointY - dY;
						break;
					}
					
					// Checking if collision detected
					// TODO real collision check
					if (collisions[tileIterY][tileIterX] == 1) {
						verticalX = pointX;
						verticalY = pointY;
						break;
					} else {
						
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
		if (Input.isReleasing()) {
			redraw(3,3, Input.getX(), Input.getY());
		}
		
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
