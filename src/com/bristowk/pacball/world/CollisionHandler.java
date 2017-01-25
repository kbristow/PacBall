package com.bristowk.pacball.world;

import java.util.ArrayList;

import com.bristowk.pacball.actors.Actor;
import com.bristowk.pacball.actors.Enemy;
import com.bristowk.pacball.actors.Pellet;
import com.bristowk.pacball.actors.Player;

public class CollisionHandler {
	public static boolean TestForCollision(Player player, Actor actor) {
		double distance2 = Math.pow(player.getX() - actor.getX(), 2)
				+ Math.pow(player.getY() - actor.getY(), 2);
		double radius2 = Math
				.pow(player.getSize() / 2 + actor.getSize() / 2, 2);
		return radius2 > distance2;
	}

	public static Pellet TestForPelletCollision(Player player,
			ArrayList<Pellet> pellets) {
		for (Pellet pellet : pellets) {
			if (TestForCollision(player, pellet)) {
				return pellet;
			}
		}
		return null;
	}

	public static Enemy TestForEnemyCollision(Player player,
			ArrayList<Enemy> enemies) {
		for (Enemy enemy : enemies) {
			if (TestForCollision(player, enemy)) {
				return enemy;
			}
		}
		return null;
	}
}
