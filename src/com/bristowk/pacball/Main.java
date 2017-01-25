package com.bristowk.pacball;

import javax.swing.JFrame;

import com.bristowk.pacball.scenes.Level;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		// Create and configure the JFrame and level
		JFrame frame = new JFrame("Main");
		Level level = Level.createFromFile("l1");
		frame.add(level);
		frame.setSize(Level.WIDTH, Level.HEIGHT);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		level.setFocusable(true);
		level.requestFocusInWindow();

		// Run the game loop
		while (true) {
			level.loop();
		}
	}
}
