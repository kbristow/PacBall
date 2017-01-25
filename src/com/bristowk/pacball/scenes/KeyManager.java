package com.bristowk.pacball.scenes;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

public class KeyManager implements KeyListener {

	private HashSet<String> pressedKeys = new HashSet<String>();

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		pressedKeys.add(KeyEvent.getKeyText(e.getKeyCode()));
	}

	@Override
	public void keyReleased(KeyEvent e) {
		pressedKeys.remove(KeyEvent.getKeyText(e.getKeyCode()));
	}

	public boolean isKeyPressed(String key) {
		return pressedKeys.contains(key);
	}

}
