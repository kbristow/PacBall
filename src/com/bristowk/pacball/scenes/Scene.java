package com.bristowk.pacball.scenes;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class Scene extends JPanel {

	protected double dt;
	protected long startTime;
	protected long endTime;
	protected double timeElapsed;

	public Scene(boolean b) {
		super(b);
	}

	public void update(double dt) {
	}

	public void loop() {
		dt = (endTime - startTime) / 1e9;
		timeElapsed += dt;
		startTime = System.nanoTime();
		this.update(dt);
		this.repaint();
		endTime = System.nanoTime();
	}

}
