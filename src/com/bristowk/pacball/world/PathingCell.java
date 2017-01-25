package com.bristowk.pacball.world;

import java.util.ArrayList;
import java.util.List;

public class PathingCell {

	private boolean isOpen = true;
	private int x, y;
	private List<PathingCell> neighbours;

	public PathingCell(int x, int y) {
		this.x = x;
		this.y = y;
		this.neighbours = new ArrayList<PathingCell>();
	}

	public void addNeighbour(PathingCell neighbour) {
		this.neighbours.add(neighbour);
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public List<PathingCell> getNeighboursClosestTo(int x, int y) {
		ArrayList<PathingCell> closestNeighbours = new ArrayList<PathingCell>();
		for (PathingCell neighbourCell : neighbours) {
			if (closestNeighbours.size() == 0) {
				closestNeighbours.add(neighbourCell);
			} else {
				boolean added = false;
				for (PathingCell currentCell : closestNeighbours) {
					if (distance2(currentCell.x, currentCell.y, x, y) > distance2(
							neighbourCell.x, neighbourCell.y, x, y)) {
						closestNeighbours.add(
								closestNeighbours.indexOf(currentCell),
								neighbourCell);
						added = true;
						break;
					}
				}
				if (!added) {
					closestNeighbours.add(neighbourCell);
				}
			}
		}
		return closestNeighbours;
	}

	public List<PathingCell> getNeighbours() {
		return this.neighbours;
	}

	public XYLocation getXYLocation() {
		return new XYLocation(x, y);
	}

	private double distance2(int x, int y, int x2, int y2) {
		return Math.pow(x - x2, 2) + Math.pow(y - y2, 2);
	}

}
