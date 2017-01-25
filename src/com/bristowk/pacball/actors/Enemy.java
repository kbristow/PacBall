package com.bristowk.pacball.actors;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.bristowk.pacball.scenes.Level;
import com.bristowk.pacball.world.Positioning;
import com.bristowk.pacball.world.XYLocation;

public class Enemy extends Actor {

	protected XYLocation target;
	protected List<XYLocation> pathToFollow;

	private static final int ENEMY_SIZE = 25;
	private static final int ENEMY_BASE_SPEED = 60;
	private static final int ENEMY_RANDOM_SPEED_MULTIPLIER = 40;

	public Enemy(Level level, int cellX, int cellY) {
		// Randomise the enemy speeds to prevent enemies clumping on top of one
		// another forever
		super(level, cellX, cellY, ENEMY_SIZE,
				((int) (Math.random() * ENEMY_RANDOM_SPEED_MULTIPLIER))
						+ ENEMY_BASE_SPEED);
		target = new XYLocation(cellX * Level.BLOCK_SIZE, cellY
				* Level.BLOCK_SIZE);
		pathToFollow = new ArrayList<XYLocation>();
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

		if (reachedTarget && pathToFollow.size() == 0) {
			List<XYLocation> newTargets = level.getPathing().findPathBF(
					getCellX(), getCellY(), level.getPlayer().getCellX(),
					level.getPlayer().getCellY());

			if (newTargets != null) {
				// 8 is chosen randomly. Essentially every 8 moves the paths
				// must be recalculated.
				pathToFollow = newTargets.subList(0,
						Math.min(newTargets.size(), 8));
			}
		} else if (reachedTarget) {
			target = pathToFollow.remove(0);
			target.scale(Level.BLOCK_SIZE);
		}
	}

	@Override
	public void render(Graphics2D g2d) {
		g2d.setColor(Color.YELLOW);
		super.render(g2d);
		g2d.setColor(Color.WHITE);
	}

}
