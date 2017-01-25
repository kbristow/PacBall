package com.bristowk.pacball.actors;

import java.awt.Graphics2D;

import com.bristowk.pacball.scenes.Level;

public class Actor {

	protected double x, y;
	protected double vx, vy;
	protected double size;
	protected double speed;
	protected Level level;

	public Actor(Level level, int cellX, int cellY, double size, double speed) {
		this.x = cellX * Level.BLOCK_SIZE;
		this.y = cellY * Level.BLOCK_SIZE;
		this.size = size;
		this.speed = speed;
		this.level = level;
	}

	protected void move(double dt) {
		double dx = vx * dt;
		x += dx;

		double dy = vy * dt;
		y += dy;
	}

	public void update(double dt) {
		move(dt);
	}

	public void render(Graphics2D g2d) {
		g2d.fillOval((int) (Level.BLOCK_SIZE / 2 + x - size / 2),
				(int) (Level.BLOCK_SIZE / 2 + y - size / 2), (int) size,
				(int) size);
	}

	public int getCellX() {
		return (int) x / Level.BLOCK_SIZE;
	}

	public int getCellY() {
		return (int) y / Level.BLOCK_SIZE;
	}

	public double getSize() {
		return size;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}
