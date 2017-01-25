package com.bristowk.pacball.scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.bristowk.pacball.actors.Actor;
import com.bristowk.pacball.actors.Enemy;
import com.bristowk.pacball.actors.Pellet;
import com.bristowk.pacball.actors.Player;
import com.bristowk.pacball.actors.Wall;
import com.bristowk.pacball.world.CollisionHandler;
import com.bristowk.pacball.world.Pathing;

@SuppressWarnings("serial")
public class Level extends Scene {
	// Dimensions of the screen
	public static final int WIDTH = 465;
	public static final int HEIGHT = 515;
	// Screen will be partitioned into squares of this size
	public static final int BLOCK_SIZE = 30;

	// How many squares/cells wide(high) the screen is
	private int cellsWide;
	private int cellsHigh;

	// Stores the list of all actors for easy updating
	private ArrayList<Actor> actors = new ArrayList<Actor>();
	// Store all actors in specific lists, for specific manipulation
	private ArrayList<Wall> walls = new ArrayList<Wall>();
	private ArrayList<Pellet> pellets = new ArrayList<Pellet>();
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	// Store the player controlled actor
	private Player player;

	// State of the game
	private GameState currentState;

	// Enemy movement AI controller
	private Pathing pathing;

	// Easy way to tell which keys are being pressed
	private KeyManager keyPresses = new KeyManager();

	public Level() {
		super(true);
		this.addKeyListener(keyPresses);
		this.cellsHigh = HEIGHT / BLOCK_SIZE;
		this.cellsWide = WIDTH / BLOCK_SIZE;

		pathing = new Pathing(this.cellsWide, this.cellsHigh);
		currentState = GameState.COUNT_DOWN;
	}

	public void initialise() {
		pathing.calculatePaths();
	}

	// ////////////////////
	// /// Render /////////
	// ////////////////////

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Render everything to an image, then draw that image to the screen
		BufferedImage bImage = new BufferedImage(WIDTH, HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D) bImage.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// Make sure this does not run at the same time as the removal of the
		// actors in update
		synchronized (this) {
			for (Pellet pellet : pellets) {
				pellet.render(g2d);
			}

			for (Enemy enemy : enemies) {
				enemy.render(g2d);
			}

			for (Wall wall : walls) {
				wall.render(g2d);
			}

			player.render(g2d);
		}

		// Rend state specific graphics
		if (currentState == GameState.COUNT_DOWN) {
			renderCountDown(g2d);
		} else if (currentState == GameState.VICTORY) {
			renderVictory(g2d);
		} else if (currentState == GameState.DEFEAT) {
			renderLoss(g2d);
		}

		g2d.dispose();
		g.drawImage(bImage, 0, 0, null);
	}

	private void renderCountDown(Graphics2D g2d) {
		Font font = new Font("Arial", Font.BOLD, 96);
		g2d.setFont(font);
		int time = (int) (3.99999999 - timeElapsed);
		g2d.setColor(Color.ORANGE);
		g2d.drawString("" + time, WIDTH / 2 - 30, HEIGHT / 2);
		g2d.setColor(Color.WHITE);
	}

	private void renderVictory(Graphics2D g2d) {
		Font font = new Font("Arial", Font.BOLD, 96);
		g2d.setFont(font);
		g2d.setColor(Color.GREEN);
		g2d.drawString("VICTORY", WIDTH / 2 - 217, HEIGHT / 2);
		g2d.setColor(Color.WHITE);
	}

	private void renderLoss(Graphics2D g2d) {
		Font font = new Font("Arial", Font.BOLD, 96);
		g2d.setFont(font);
		g2d.setColor(Color.RED);
		g2d.drawString("DEFEAT", WIDTH / 2 - 197, HEIGHT / 2);
		g2d.setColor(Color.WHITE);
	}

	// ////////////////////
	// /// Update /////////
	// ////////////////////

	@Override
	public void update(double dt) {
		if (currentState == GameState.PLAY) {
			// Update all actors when current state is PLAY
			for (Actor actor : this.actors) {
				actor.update(dt);
			}
			// Look for pellets being eaten
			Pellet eatenPellet = CollisionHandler.TestForPelletCollision(
					player, pellets);
			synchronized (this) {
				if (eatenPellet != null) {
					actors.remove(eatenPellet);
					pellets.remove(eatenPellet);
					if (pellets.size() == 0) {
						currentState = GameState.VICTORY;
					}
				}
			}

			// Look for enemies hitting the player
			Enemy collisionEnemy = CollisionHandler.TestForEnemyCollision(
					player, enemies);
			if (collisionEnemy != null) {
				currentState = GameState.DEFEAT;
			}
		}
		if (currentState == GameState.COUNT_DOWN && timeElapsed > 3) {
			currentState = GameState.PLAY;
		}
	}

	// //////////////////////////////
	// /// Utility Functions ////////
	// /////////////////////////////

	// Create a level from a level file
	public static Level createFromFile(String fileName) {
		int rows = 0;
		int columns = 0;
		// Read in all the lines
		ArrayList<String> lines = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;

			while ((line = br.readLine()) != null) {
				lines.add(line.trim());
				columns = line.trim().length();
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		rows = lines.size();

		// Parse the lines into level actors
		Level level = new Level();
		for (int i = rows - 1; i >= 0; i--) {
			for (int j = 0; j < columns; j++) {
				char tile = lines.get(i).charAt(j);
				if (tile == 'X') {
					level.addWall(new Wall(level, j, i));
				} else if (tile == '.') {
					level.addPellet(new Pellet(level, j, i));
				} else if (tile == 'S') {
					level.setPlayer(new Player(level, j, i));
				} else if (tile == 'E') {
					level.addEnemy(new Enemy(level, j, i));
					level.addPellet(new Pellet(level, j, i));
				}
			}
		}
		level.initialise();
		return level;
	}

	// Test if the location points to a wall
	public boolean isWall(double x, double y) {
		for (Wall wall : walls) {
			if (wall.getX() == x && wall.getY() == y) {
				return true;
			}
		}
		return false;
	}

	// ///////////////////////////////
	// /// Adders and Setters ////////
	// ///////////////////////////////

	public void addActor(Actor newActor) {
		this.actors.add(newActor);
	}

	public void addPellet(Pellet newPellet) {
		this.actors.add(newPellet);
		this.pellets.add(newPellet);
	}

	public void addWall(Wall newWall) {
		this.actors.add(newWall);
		this.walls.add(newWall);
		// Mark the cell containing the wall as non traversable
		this.pathing.setCellClosed(newWall.getCellX(), newWall.getCellY());
	}

	public void addEnemy(Enemy newEnemy) {
		this.actors.add(newEnemy);
		this.enemies.add(newEnemy);
	}

	public void setPlayer(Player player) {
		this.player = player;
		this.actors.add(player);
	}

	// ////////////////////
	// /// Getters ////////
	// ////////////////////

	public KeyManager getKeyPresses() {
		return keyPresses;
	}

	public Pathing getPathing() {
		return this.pathing;
	}

	public Player getPlayer() {
		return this.player;
	}

}
