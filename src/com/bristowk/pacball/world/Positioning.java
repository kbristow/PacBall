package com.bristowk.pacball.world;

import com.bristowk.pacball.scenes.Level;

public class Positioning {

	public static XYLocation getCellRight(double x, double y) {
		XYLocation coords = new XYLocation(((int) x / Level.BLOCK_SIZE) + 1,
				(int) y / Level.BLOCK_SIZE);
		return coords;
	}

	public static XYLocation getCellLeft(double x, double y) {
		XYLocation coords = new XYLocation(((int) x / Level.BLOCK_SIZE) - 1,
				(int) y / Level.BLOCK_SIZE);
		return coords;
	}

	public static XYLocation getCellUp(double x, double y) {
		XYLocation coords = new XYLocation((int) x / Level.BLOCK_SIZE,
				((int) y / Level.BLOCK_SIZE) - 1);
		return coords;
	}

	public static XYLocation getCellDown(double x, double y) {
		XYLocation coords = new XYLocation((int) x / Level.BLOCK_SIZE,
				((int) y / Level.BLOCK_SIZE) + 1);
		return coords;
	}

	public static XYLocation moveToTarget(double currentX, double currentY,
			double targetX, double targetY, double speed, double dt) {
		double dx = Math.signum(targetX - currentX) * speed * dt;
		double dy = Math.signum(targetY - currentY) * speed * dt;

		if (reachedTarget(currentX, dx, targetX)) {
			currentX = targetX;
		} else {
			currentX += dx;
		}
		if (reachedTarget(currentY, dy, targetY)) {
			currentY = targetY;
		} else {
			currentY += dy;
		}

		return new XYLocation(currentX, currentY);
	}

	public static boolean reachedTarget(double loc, double dLoc, double target) {
		return Math.signum(target - loc) != Math.signum(target - (loc + dLoc));
	}

}
