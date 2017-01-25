package com.bristowk.pacball.actors;

import java.awt.Graphics2D;

import com.bristowk.pacball.scenes.Level;

public class Wall extends Actor {

	// Walls cannot move
	private static final int WALL_DEFAULT_SPEED = 0;

	public Wall(Level level, int cellX, int cellY) {
		super(level, cellX, cellY, Level.BLOCK_SIZE, WALL_DEFAULT_SPEED);
	}

	@Override
	public void render(Graphics2D g2d) {
		g2d.fillRect((int) x, (int) y, (int) size, (int) size);
	}

}
