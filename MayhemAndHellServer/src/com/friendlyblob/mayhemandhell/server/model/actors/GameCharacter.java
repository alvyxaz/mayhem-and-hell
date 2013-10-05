package com.friendlyblob.mayhemandhell.server.model.actors;

import com.friendlyblob.mayhemandhell.server.GameTimeController;
import com.friendlyblob.mayhemandhell.server.model.GameObject;
import com.friendlyblob.mayhemandhell.server.model.World;
import com.friendlyblob.mayhemandhell.server.model.stats.BaseStats;
import com.friendlyblob.mayhemandhell.server.model.stats.CharacterStats;
import com.friendlyblob.mayhemandhell.server.model.stats.StatsSet;
import com.friendlyblob.mayhemandhell.server.network.GameClient;
import com.friendlyblob.mayhemandhell.server.network.packets.ClientPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.ServerPacket;
import com.friendlyblob.mayhemandhell.server.network.packets.server.NotifyCharacterMovement;
import com.friendlyblob.mayhemandhell.server.utils.ObjectPosition;

/*
 * Parent of players and npc's
 */
public class GameCharacter extends GameObject{

	private int health;
	
	private int energy;
	private int mana;
	
	private boolean alive;
	
	private CharacterStats stats;
	
	private MovementData movement;
	
	private CharacterTemplate template;
	
	public GameCharacter(int objectId, CharacterTemplate template) {
		this.objectId = objectId;
		this.template = template;
		
		// Initialize stats
		stats = new CharacterStats(this);
	}
	
	/**
	 * Updating character position.
	 * @param gameTicks current game tick
	 * @return true if character has reached its destination
	 */
	public boolean updatePosition (int gameTicks) {
		float prevX = getPosition().getX();
		float prevY = getPosition().getY();
		
		float angle = (float) Math.atan2(movement.destinationY - prevY, movement.destinationX - prevX);
		
		// TODO check if running or walking
		int movementSpeed = getMovementSpeed();
		float distanceCovered = GameTimeController.DELTA_TIME*movementSpeed;
		
		getPosition().offset((float) Math.cos(angle) * distanceCovered, 
				(float) Math.sin(angle) * distanceCovered);
		
		int dX = (int)(movement.destinationX - getPosition().getX());
		int dY = (int)(movement.destinationY - getPosition().getY());
		
		// Updating destination if following a target
		if (movement.isFollowing()) {
			movement.updateDestination();
			
			if (!movement.isWalkingBy()) {
				return false;
			}
			
		}
		
		// Check if destination is reached
		if (dX * dX + dY * dY < distanceCovered * distanceCovered) {
			getPosition().set(movement.destinationX, movement.destinationY);
			System.out.println("destination reached");
			movement = null;
			return true;
		}
		
		return false;
	}
	
	private boolean checkCollisions(float prevX, float prevY) {
		int[][] collisionLayer = World.getInstance().getZone(0).getCollisionMap();
		
		float oldX = prevX, oldY = prevY;
		boolean collisionX = false, collisionY = false;
		
		System.out.println("oldX: " + oldX + " oldY: " + oldY);
		System.out.println("newX: " + getPosition().getX() + " newY: " + getPosition().getY());
		
		// collision checking
		if (getPosition().getX() - oldX > 0) {
			// top right
			collisionX =  collisionLayer[(int) ((getPosition().getX() + 15) / 16)][(int) ((getPosition().getY() + 28) / 16)] == 1;

			// middle right
			if (!collisionX) {
				collisionX =  collisionLayer[(int) ((getPosition().getX() + 15) / 16)][(int) ((getPosition().getY() + 28/2) / 16)] == 1;

			}
			// bottom right
			if (!collisionX) {
				collisionX =  collisionLayer[(int) ((getPosition().getX() + 15) / 16)][(int) ((getPosition().getY()) / 16)] == 1;

			}
		} else {
			// top left
			collisionX =  collisionLayer[(int) ((getPosition().getX()) / 16)][(int) ((getPosition().getY() + 28) / 16)] == 1;

			// middle left
			if (!collisionX) {
				collisionX =  collisionLayer[(int) ((getPosition().getX()) / 16)][(int) ((getPosition().getY() + 28/2) / 16)] == 1;
			}
			// bottom left
			if (!collisionX) {
				collisionX =  collisionLayer[(int) ((getPosition().getX()) / 16)][(int) ((getPosition().getY()) / 16)] == 1;

			}
		}
		
		if (collisionX) {
			getPosition().setX(oldX);
//			stop(oldX, oldY);
		}
		
		if (getPosition().getY() - oldY > 0) {
			// top left
			collisionY =  collisionLayer[(int) ((getPosition().getX()) / 16)][(int) ((getPosition().getY() + 28) / 16)] == 1;

			//top middle
			if (!collisionY) {
				collisionY =  collisionLayer[(int) ((getPosition().getX() + 15/2) / 16)][(int) ((getPosition().getY() + 28) / 16)] == 1;

			}
			//top right
			if (!collisionY) {
				collisionY =  collisionLayer[(int) ((getPosition().getX() + 15) / 16)][(int) ((getPosition().getY() + 28) / 16)] == 1;

			}
		} else {
			//bottom left
			collisionY =  collisionLayer[(int) ((getPosition().getX()) / 16)][(int) ((getPosition().getY()) / 16)] == 1;

			// bottom middle
			if (!collisionY) {
				collisionY =  collisionLayer[(int) ((getPosition().getX() + 15/2) / 16)][(int) ((getPosition().getY()) / 16)] == 1;

			}
			// bottom right
			if (!collisionY) {
				collisionY =  collisionLayer[(int) ((getPosition().getX() + 15) / 16)][(int) ((getPosition().getY()) / 16)] == 1;

			}
		}
		
		if (collisionY) {
			getPosition().setY(oldY);
//			stop(oldX, oldY);
		}
		
		System.out.println("CollisionX" + collisionX);
		System.out.println("CollisionY" + collisionY);
		return collisionX || collisionY;
	}

	/**
	 * Registers a new movement destination
	 * Called only once when user requests to move a character.
	 * 
	 * Actions:
	 * 	Checking for collisions
	 * 	If character is about to collide with something, move it to collision point
	 * 
	 * @param x requested destination x
	 * @param y requested destination y
	 * @return true if movement is initialized, false if impossible to move
	 */
	public boolean moveCharacterTo(int x, int y) {
		// TODO check boundaries and collisions. If out of bounds - return false
		Point p = null;
		try {
			p = recalculateTarget(x, y);
			System.out.println("POINT: " + p.x + " " + p.y);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		MovementData movementData = new MovementData();
		movementData.destinationX = p.x;
		movementData.destinationY = p.y;
		movementData.movementSpeed = getMovementSpeed();
		movementData.timeStamp = GameTimeController.getInstance().getGameTicks();
		
		moveCharacterTo(movementData);
		
		return true;
	}
	
	public void walkBy(GameObject character) {
		MovementData movementData = new MovementData();
		movementData.movementSpeed = getMovementSpeed();
		movementData.timeStamp = GameTimeController.getInstance().getGameTicks();
		movementData.follow(character, true);
		moveCharacterTo(movementData);
	}
	
	/**
	 * Initializes a movement from a given MovementData
	 * @param movementData
	 * @return
	 */
	public boolean moveCharacterTo(MovementData movementData) {
		this.movement = movementData;
		
		GameTimeController.getInstance().registerMovingObject(this);
		
		// Notify nearby characters about movement
		getRegion().broadcastToCloseRegions(new NotifyCharacterMovement(getObjectId(), movementData, getPosition()));
		
		return true;
	}
	
	public Point recalculateTarget(int x, int y) {
		// Results
		int verticalX = x;
		int verticalY = y;
		int horizontalX = x;
		int horizontalY = y;
		
				
		// Directions
		int xDirection = getPosition().getX() < x ? 1 : -1;
		int yDirection = getPosition().getY() < y ? 1 : -1;

		// vertical x - 0, vertical y - 1, horizontal x - 2, horizontal y - 3
		// calculate currentPoints of the bounding box
		int[][] currentPoints = new int[][] {
			{ (int) getPosition().getX(), (int) getPosition().getY() },
			{ (int) getPosition().getX() - 8, (int) getPosition().getY() },
			{ (int) getPosition().getX() - 8, (int) getPosition().getY() + 12},
			{ (int) getPosition().getX() - 8, (int) getPosition().getY() + 24},
			{ (int) getPosition().getX(), (int) getPosition().getY() + 24},
			{ (int) getPosition().getX() + 8, (int) getPosition().getY() + 24},
			{ (int) getPosition().getX() + 8, (int) getPosition().getY() + 12},
			{ (int) getPosition().getX() + 8, (int) getPosition().getY() },
		};
		
		System.out.println("CURRENT POINT");
		for (int i = 0; i < currentPoints.length; i++) {
			System.out.println(i + ":" + currentPoints[i][0] + " " + currentPoints[i][1]);
		}
		
		// calculate targetPoints of the bounding box
		int[][] targetPoints = new int[][] {
			{ x, y },
			{ x- 8, y },
			{ x - 8, y + 12},
			{ x - 8, y + 24},
			{ x, y + 24},
			{ x + 8, y + 24},
			{ x + 8, y + 12},
			{ x + 8, y },
		};
		
		System.out.println("TARGET POINT");
		for (int i = 0; i < targetPoints.length; i++) {
			System.out.println(i + ":" + targetPoints[i][0] + " " + targetPoints[i][1]);
		}

		// array for potential points
		int[][] potentialPoints = new int[8][2];
		// array for tile iterations
		int[][] tileIters = new int[8][];
		
		// visiems vienodas
		int tilesDX = Math.abs(getTileXAt(x) - getTileXAt((int) getPosition().getX()));
		int tilesDY = Math.abs(getTileYAt(y) - getTileYAt((int) getPosition().getY()));	


		
	
		/**---------------------------------------------------
		 * VERTICAL
		 */

		// atskiri del x
		if (xDirection == 1) {
			// 16 = tileWidth
			for (int i = 0; i < potentialPoints.length; i++) {
				potentialPoints[i][0] = (int) ((currentPoints[i][0]/16) * 16 + 16); 
			}
		} else {
			for (int i = 0; i < potentialPoints.length; i++) {
				potentialPoints[i][0] = (int) ((currentPoints[i][0]/16) * 16 - 1); // bottom middle
			}
		}
		

		
		// atskiri del y
		for (int i = 0; i < potentialPoints.length; i++) {
			potentialPoints[i][1] = getLinearYAtX(potentialPoints[i][0], currentPoints[i][0], currentPoints[i][1], targetPoints[i][0], targetPoints[i][1]); 
		}
		
		System.out.println(xDirection);
		System.out.println("VERTICAL POTENTIAL POINTS:");
		for (int i = 0; i < potentialPoints.length; i++) {
			System.out.println(i + ":" + potentialPoints[i][0] + " " + potentialPoints[i][1]);
		}


		int[][] collisionLayer = World.getInstance().getZone(0).getCollisionMap();

		if (tilesDX != 0) {
			// tileIterxx masyvas
			for (int i = 0; i < tileIters.length; i++) {
				tileIters[i] = new int[] { getTileXAt(potentialPoints[i][0]), getTileYAt(potentialPoints[i][1]) };
			}
			
			System.out.println("VERTICAL TILE ITERATIONS:");
			for (int i = 0; i < tileIters.length; i++) {
				System.out.println(i + ":" + tileIters[i][0] + " " + tileIters[i][1]);
			}

			// Checking if current tile is collision tile
			// TODO real collision check
			
			// if
			boolean found = false;
			for (int i = 0; i < tileIters.length; i++) {
				if (collisionLayer[tileIters[i][1]][tileIters[i][0]] == 1) {
					verticalX = potentialPoints[0][0];
					verticalY = potentialPoints[0][1];
					found = true;
					break;
				}
			}
			// else
			if (!found) {
				System.out.println("NMOT FOUND");
				// 16 = tileWidth
				int dX = (xDirection == 1) ? 16 : -16;

				vertical_loop:
				for (int i = 1; i < tilesDX; i++) {
					for (int j = 0; j < potentialPoints.length; j++) {
						potentialPoints[j][0] += dX;
						potentialPoints[j][1] = getLinearYAtX(potentialPoints[j][0], currentPoints[j][0], currentPoints[j][1], targetPoints[j][0], targetPoints[j][1]);
					}
					
					System.out.println("VERTICAL POTENTIAL POINTS:");
					for (int l = 0; l < potentialPoints.length; l++) {
						System.out.println(l + ":" + potentialPoints[l][0] + " " + potentialPoints[l][1]);
					}

					for (int k = 0; k < tileIters.length; k++) {
						tileIters[k] = new int[] { getTileXAt(potentialPoints[k][0]), getTileYAt(potentialPoints[k][1]) };
						
						System.out.println("VERTICAL TILE ITERATIONS:");
						for (int m = 0; m < tileIters.length; m++) {
							System.out.println(m + ":" + tileIters[m][0] + " " + tileIters[m][1]);
						}

						if (collisionLayer[tileIters[k][1]][tileIters[k][0]] == 1 ) {
							// jei kazkuriam taske kolizija, priskiriam potentialPoints[0] taska
							System.out.println("SOLID AT: " + tileIters[k][1] + " " + tileIters[k][0]);
							verticalX = potentialPoints[0][0];
							verticalY = potentialPoints[0][1];
							break vertical_loop;
						}
					}
					
					
				}
			}
		}
		
		/**---------------------------------------------------
		 * HORIZONTAL
		 */
		
		if (yDirection == 1) {
			// 16 = tileHeight
			for (int i = 0; i < potentialPoints.length; i++) {
				potentialPoints[i][1] = (int) ((currentPoints[i][1]/16) * 16 + 16); // bottom middle
			}
		} else {
			for (int i = 0; i < potentialPoints.length; i++) {
				potentialPoints[i][1] = (int) ((currentPoints[i][1]/16) * 16 - 1); // bottom middle
			}
		}
		
		for (int i = 0; i < potentialPoints.length; i++) {
			potentialPoints[i][0] = getLinearXAtY(potentialPoints[i][1], currentPoints[i][0], currentPoints[i][1], targetPoints[i][0], targetPoints[i][1]); 
		}
		
		System.out.println("HORIZONTAL POTENTIAL POINTS:");
		for (int l = 0; l < potentialPoints.length; l++) {
			System.out.println(l + ":" + potentialPoints[l][0] + " " + potentialPoints[l][1]);
		}

		

		if (tilesDY != 0) {
			for (int i = 0; i < tileIters.length; i++) {
				tileIters[i] = new int[] { getTileXAt(potentialPoints[i][0]), getTileYAt(potentialPoints[i][1]) };
			}
			
			System.out.println("HORIZONTAL TILE ITERATIONS:");
			for (int m = 0; m < tileIters.length; m++) {
				System.out.println(m + ":" + tileIters[m][0] + " " + tileIters[m][1]);
			}
			
			// Checking if current tile is collision tile
			// TODO real collision check
			// if
			boolean found = false;
			for (int i = 0; i < tileIters.length; i++) {
				if (collisionLayer[tileIters[i][1]][tileIters[i][0]] == 1) {
					horizontalX = potentialPoints[0][0];
					horizontalY = potentialPoints[0][1];
					found = true;
					break;
				}
			}
			// else
			if (!found) {
				// 16 = tileWidth
				int dY = (yDirection == 1) ? 16 : -16;

				horizontal_loop:
				for (int i = 1; i < tilesDY; i++) {
					for (int j = 0; j < potentialPoints.length; j++) {
						potentialPoints[j][1] += dY;
						potentialPoints[j][0] = getLinearXAtY(potentialPoints[j][1], currentPoints[j][0], currentPoints[j][1], targetPoints[j][0], targetPoints[j][1]);
					}
					
					
					System.out.println("HORIZONTAL POTENTIAL POINTS:");
					for (int l = 0; l < potentialPoints.length; l++) {
						System.out.println(l + ":" + potentialPoints[l][0] + " " + potentialPoints[l][1]);
					}

					for (int k = 0; k < tileIters.length; k++) {
						tileIters[k] = new int[] { getTileXAt(potentialPoints[k][0]), getTileYAt(potentialPoints[k][1]) };
						
						System.out.println("HORIZONTAL TILE ITERATIONS:");
						for (int m = 0; m < tileIters.length; m++) {
							System.out.println(m + ":" + tileIters[m][0] + " " + tileIters[m][1]);
						}
						
						if (collisionLayer[tileIters[k][1]][tileIters[k][0]] == 1 ) {
							// jei kazkuriam taske kolizija, priskiriam potentialPoints[0] taska							
							System.out.println("SOLID AT: " + tileIters[k][1] + " " + tileIters[k][0]);

							horizontalX = potentialPoints[0][0];
							horizontalY = potentialPoints[0][1];
							break horizontal_loop;
						}
					}
					
					
				}
			}
		}

		
		System.out.println("original target: " + x + " " + y);
		System.out.println("recalculated target: " + verticalX + " " + verticalY);
		System.out.println("recalculated target: " + horizontalX + " " + horizontalY);
		
		// calculate targetPoints of the bounding box

		int[][] potentialPoint;
		if (sqrDistanceBetween(getPosition().getX(), getPosition().getY(), verticalX, verticalY) < 
				sqrDistanceBetween(getPosition().getX(), getPosition().getY(), horizontalX, horizontalY)) {
			potentialPoint = new int[][] {
					{ verticalX, verticalY },
					{ verticalX- 8, verticalY },
					{ verticalX - 8, verticalY + 12},
					{ verticalX - 8, verticalY + 24},
					{ verticalX, verticalY + 24},
					{ verticalX + 8, verticalY + 24},
					{ verticalX + 8, verticalY + 12},
					{ verticalX + 8, verticalY },
				};
				
		} else {
			potentialPoint = new int[][] {
					{ horizontalX, horizontalY },
					{ horizontalX- 8, horizontalY },
					{ horizontalX - 8, horizontalY + 12},
					{ horizontalX - 8, horizontalY + 24},
					{ horizontalX, horizontalY + 24},
					{ horizontalX + 8, horizontalY + 24},
					{ horizontalX + 8, horizontalY + 12},
					{ horizontalX + 8, horizontalY },
				};
				
		}
		
		for (int i = 0; i< potentialPoint.length; i++) {
			if (collisionLayer[getTileYAt(potentialPoint[i][1])][getTileXAt(potentialPoint[i][0])] == 1) {
				System.out.println(getTileYAt(potentialPoint[i][1]) + " " );
				return new Point((int) getPosition().getX(), (int) getPosition().getY());

			}
		}
		
		return new Point(potentialPoint[0][0], potentialPoint[0][1]);
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
	}
	
	public int getTileXAt(int x) {
		return x/16;
	}
	
	public int getTileYAt(int y) {
		return y/16;
	}
	
	public int getTileAt(int x, int y) {
		return 0;
	}
	
	public int sqrDistanceBetween(float f, float g, int endX, int endY) {
		return (int) ((endX - f)*(endX - f) + (endY-g)*(endY-g));
	}	
	
	public int getLinearYAtX(int x, int currentX, int currentY, int targetX, int targetY) {
		System.out.println("LINEAR Y AT X");
		System.out.println("x: " + x + " currentX: " + currentX + " currentY: " + currentY + " targetX: " + targetX + "targetY: " + targetY);
		
		int temp = (targetX-currentX);
		
		if (temp == 0) {
			temp = 1;
		}
		
		return (int)Math.abs((((targetY-currentY)/temp)*(x-currentX) + currentY));
	}
	
	public int getLinearXAtY(int y, int currentX, int currentY, int targetX, int targetY) {
		System.out.println("LINEAR X AT Y");
		System.out.println("y: " + y + " currentX: " + currentX + " currentY: " + currentY + " targetX: " + targetX + "targetY: " + targetY);

		int temp = (targetY-currentY);
		
		if (temp == 0) {
			temp = 1;
		}
		
		return (int)((targetX-currentX)*(y-currentY)/temp + currentX);
	}
	
	public float angleBetween(int currentX, int currentY, int targetX, int targetY) {
		return (float)Math.atan2(targetY-currentY, targetX-currentX);
	}

	public CharacterStats getStats() {
		return stats;
	}
	
	public static class MovementData {
		public float destinationX;
		public float destinationY;
		public int movementSpeed;
		public int timeStamp;

		private boolean walkBy;
		private GameObject targetToFollow;
		
		public void follow(GameObject character, boolean walkBy) {
			this.walkBy = walkBy;
			this.targetToFollow = character;
			destinationX = character.getPosition().getX();
			destinationY = character.getPosition().getY();
		}
		
		public boolean isFollowing() {
			return targetToFollow != null;
		}
		
		public void updateDestination() {
			destinationX = targetToFollow.getPosition().getX();
			destinationY = targetToFollow.getPosition().getY();
		}
		
		/**
		 * Checks whether our intention is to walk by, or follow "forever"
		 * @return true if walking by.
		 */
		public boolean isWalkingBy() {
			return walkBy;
		}
		
	}
	
	/**
	 * Restores health, mana and energy to maximum
	 */
	public void restoreVitals() {
		this.health = getMaxHealth();
		this.energy = getMaxEnergy();
		this.mana = getMaxMana();
	}
	
	public void revive() {
		this.alive = true;
	}
	
	/**
	 * TODO implement
	 */
	public void removeEffects() {
	}
	
	public MovementData getMovement() {
		return movement;
	}
	
	public int getMovementSpeed() {
		// TODO Check whether running or walking, and return what's necessary
		return getWalkingSpeed();
	}
	
	private int getMaxHealth() {
		return stats.getMaxHealth();
	}
	
	private int getMaxMana() {
		return stats.getMaxMana();
	}
	
	private int getMaxEnergy() {
		return stats.getMaxEnergy();
	}
	
	private int getWalkingSpeed() {
		return stats.getWalkingSpeed();
	}
	

	public void sendPacket(ServerPacket packet) {
		
	}
	
	public CharacterTemplate getTemplate() {
		return template;
	}
	
	/**
	 * @return the health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Restores health to maximum amount
	 */
	public void restoreHealth() {
		this.health = getMaxHealth();
	}
	
	/**
	 * @param health the health to set
	 */
	public void setHealth(int health) {
		this.health = health;
	}

	/**
	 * @return the energy
	 */
	public int getEnergy() {
		return energy;
	}

	/**
	 * @param energy the energy to set
	 */
	public void setEnergy(int energy) {
		this.energy = energy;
	}

	/**
	 * @return the mana
	 */
	public int getMana() {
		return mana;
	}

	/**
	 * @param mana the mana to set
	 */
	public void setMana(int mana) {
		this.mana = mana;
	}
	
}
