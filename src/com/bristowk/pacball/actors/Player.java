package com.bristowk.pacball.actors;

import com.bristowk.pacball.scenes.Level;
import com.bristowk.pacball.world.Positioning;
import com.bristowk.pacball.world.XYLocation;

public class Player extends Actor {

	private static final int PLAYER_SIZE = 25;
	private static final int PLAYER_DEFAULT_SPEED = 120;

	protected XYLocation target;

	public Player(Level level, int cellX, int cellY) {
		super(level, cellX, cellY, PLAYER_SIZE, PLAYER_DEFAULT_SPEED);
		target = new XYLocation(cellX * Level.BLOCK_SIZE, cellY
				* Level.BLOCK_SIZE);
	}

	@Override
	public void update(double dt) {
		XYLocation newLocation = Positioning.moveToTarget(x, y, target.getX(),
				target.getY(), speed, dt);

		x = newLocation.getX();
		y = newLocation.getY();

		boolean reachedTarget = false;
		if (x == target.getX() && y == target.getY()) {
			reachedTarget = true;
		}

		if (reachedTarget) {
			XYLocation newTarget = null;
			if (level.getKeyPresses().isKeyPressed("D")) {
				newTarget = Positioning.getCellRight(x, y);
			} else if (level.getKeyPresses().isKeyPressed("A")) {
				newTarget = Positioning.getCellLeft(x, y);
			} else if (level.getKeyPresses().isKeyPressed("W")) {
				newTarget = Positioning.getCellUp(x, y);
			} else if (level.getKeyPresses().isKeyPressed("S")) {
				newTarget = Positioning.getCellDown(x, y);
			}

			if (newTarget != null) {
				newTarget.scale(Level.BLOCK_SIZE);
				if (!level.isWall(newTarget.getX(), newTarget.getY())) {
					target = newTarget;
				}
			}
		}
	}

}
