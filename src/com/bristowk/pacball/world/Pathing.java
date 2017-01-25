package com.bristowk.pacball.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pathing {

	private PathingCell[][] grid;
	private int width;
	private int height;

	public Pathing(int width, int height) {
		grid = new PathingCell[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				grid[i][j] = new PathingCell(i, j);
			}
		}
		this.width = width;
		this.height = height;
	}

	public void setCellClosed(int x, int y) {
		grid[x][y].setOpen(false);
	}

	public PathingCell getOpenCell() {
		int startX = (int) (Math.random() * width);
		int startY = (int) (Math.random() * height);
		for (int i = startX; i < width * 2; i++) {
			for (int j = startY; j < height * 2; j++) {
				if (grid[i % width][j % height].isOpen()) {
					return grid[i % width][j % height];
				}
			}
		}

		return null;
	}

	public void calculatePaths() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				PathingCell currentCell = grid[i][j];
				if (currentCell.isOpen()) {
					ArrayList<XYLocation> neighbours = new ArrayList<XYLocation>();
					neighbours.add(new XYLocation(i - 1, j));
					neighbours.add(new XYLocation(i + 1, j));
					neighbours.add(new XYLocation(i, j - 1));
					neighbours.add(new XYLocation(i, j + 1));

					for (XYLocation xyLocation : neighbours) {
						int xLoc = (int) xyLocation.getX();
						int yLoc = (int) xyLocation.getY();
						if (inBoundsX(xLoc) && inBoundsY(yLoc)) {
							grid[xLoc][yLoc].addNeighbour(currentCell);
						}
					}
				}
			}
		}
	}

	public List<XYLocation> findPathBF(int x, int y, int x2, int y2) {
		return findPathBF(new XYLocation(x, y), new XYLocation(x2, y2));
	}

	public List<XYLocation> findPathBF(XYLocation start, XYLocation end) {
		List<PathingCell> openList = new ArrayList<PathingCell>();
		List<PathingCell> closedList = new ArrayList<PathingCell>();
		openList.add(grid[(int) start.getX()][(int) start.getY()]);
		XYLocation[][] pathDetails = new XYLocation[width][height];

		boolean pathFound = false;

		while (openList.size() > 0 && !pathFound) {
			PathingCell workingLocation = openList.remove(0);
			closedList.add(workingLocation);

			if (workingLocation.getXYLocation().equals(end)) {
				pathFound = true;
			} else {
				List<PathingCell> neighbours = workingLocation.getNeighbours();
				for (PathingCell neighbour : neighbours) {
					if (!closedList.contains(neighbour)
							&& !openList.contains(neighbour)) {
						pathDetails[neighbour.getX()][neighbour.getY()] = workingLocation
								.getXYLocation();
						openList.add(neighbour);
					}
				}
			}
		}

		List<XYLocation> path = new ArrayList<XYLocation>();
		if (pathFound) {
			path.add(end);
			while (!path.get(path.size() - 1).equals(start)) {
				XYLocation workingLocation = path.get(path.size() - 1);
				path.add(pathDetails[(int) workingLocation.getX()][(int) workingLocation
						.getY()]);
			}
			path.remove(path.size() - 1);
			Collections.reverse(path);
		}
		return path;
	}

	public boolean inBoundsX(int x) {
		return x >= 0 && x < width;
	}

	public boolean inBoundsY(int y) {
		return y >= 0 && y < height;
	}

}
