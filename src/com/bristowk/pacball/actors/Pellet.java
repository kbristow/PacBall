package com.bristowk.pacball.actors;

import com.bristowk.pacball.scenes.Level;

public class Pellet extends Actor {

	private static final int PELLET_SIZE = 10;
	// Pellets cannot move
	private static final int PELLET_DEFAULT_SPEED = 0;

	public Pellet(Level level, int cellX, int cellY) {
		super(level, cellX, cellY, PELLET_SIZE, PELLET_DEFAULT_SPEED);
	}

}
