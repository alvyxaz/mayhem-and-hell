package com.friendlyblob.mayhemandhell.server.model.logic;

public class CollisionManager {
	
	private static final int tileWidth = 16;
	private static final int tileHeight = 16;
	
	public static Point rayTrace(int [][] collisions, 
			int currentX, int currentY, int targetX, int targetY) {
		
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
		
		if (sqrDistanceBetween(currentX, currentY, verticalX, verticalY) < 
				sqrDistanceBetween(currentX, currentY, horizontalX, horizontalY)) {
			return new Point(verticalX, verticalY); 
		} else {
			return new Point(horizontalX, horizontalY); 
		}
	}
	
	
	public static int getTileXAt(int x) {
		return x/16;
	}
	
	public static int getTileYAt(int y) {
		return y/16;
	}
	
	public static int getTileAt(int x, int y) {
		return 0;
	}
	
	public static int sqrDistanceBetween(float f, float g, int endX, int endY) {
		return (int) ((endX - f)*(endX - f) + (endY-g)*(endY-g));
	}	
	
	public static int getLinearYAtX(int x, float currentX, int currentY, int targetX, int targetY) {
		return (int)(((targetY-currentY)/(targetX-currentX))*(x-currentX) + currentY);
	}
	
	public static int getLinearXAtY(int y, int currentX, float currentY, int targetX, int targetY) {
		return (int)((targetX-currentX)*(y-currentY)/(targetY-currentY) + currentX);
	}
	
	public static float angleBetween(int currentX, int currentY, int targetX, int targetY) {
		return (float)Math.atan2(targetY-currentY, targetX-currentX);
	}
	
	public static Point shortenLineByX(int distanceX, int xDirection, int currentX, int currentY, int targetX, int targetY) {
		targetX -= distanceX * xDirection;
		targetY = getLinearYAtX(targetX, currentX, currentY, targetX, targetY);
		return new Point(targetX, targetY);
	}
	
	public static Point shortenLineByY(int distanceY, int yDirection, int currentX, int currentY, int targetX, int targetY) {
		targetY -= distanceY * yDirection;
		targetX = getLinearXAtY(targetY, currentX, currentY, targetX, targetY);
		return new Point(targetX, targetY);
	}
	
	public static class Point {
		public int x;
		public int y;
		
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public boolean equals(Object obj) {
			if (obj == this) {
			    return true;
			}
			if (obj == null) {
			    return false;
			}
			if (obj instanceof Point) {
				Point other = (Point) obj;
			    return other.x == this.x && other.y == this.y;
			} else {
			    return false;
			}
		}
		
		public String toString() {
			return "[Point: " + x + " " + y+"]";
		}
	}
}
