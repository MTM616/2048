package application;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.Group;

public class PhantomGrid {
	
	private Group myRoot;
	private Random myRandom;
	private boolean[][] myGrid;
	private ScoreManager myScore;
	private ArrayList<MovingTile> myAppearingTiles;
	private ArrayList<MovingTile> myDisappearingTiles;
	private boolean myContinue;
	private boolean my2048;
	
	public PhantomGrid(Group root, ScoreManager score, int gridSize) {
		myRandom = new Random(new Date().getTime());
		myGrid = new boolean[gridSize][gridSize];
		myContinue = false;
		my2048 = false;
		myRoot = root;
		myScore = score;
		myAppearingTiles = new ArrayList<MovingTile>();
		myDisappearingTiles = new ArrayList<MovingTile>();
		refreshGrid();
	}
	
	private ArrayList<Point2D> generateSafeArray() {
		ArrayList<Point2D> safeArray = new ArrayList<Point2D>();
		for (int y = 0; y < myGrid.length; y++) {
			for (int x = 0; x < myGrid[y].length; x++) {
				if (myGrid[y][x]) {
					safeArray.add(new Point2D(x, y));
				}
			}
		}
		return safeArray;
	}
	
	protected void placeInitialTiles(ArrayList<MovingTile> tileList) {
		MovingTile tileOne = placeNewTile();
		MovingTile tileTwo = placeNewTile();
		tileOne.reveal();
		tileTwo.reveal();
		tileList.add(tileOne);
		tileList.add(tileTwo);
		clearLists();
	}
	
	protected MovingTile placeNewTile() {
		ArrayList<Point2D> safeArray = generateSafeArray();
		Point2D locChosen = safeArray.get(myRandom.nextInt(safeArray.size()));
		int id = 1;
		if (myRandom.nextInt(10) == 0) {
			id = 2;
		}
		int x = (int)locChosen.getX();
		int y = (int)locChosen.getY();
		MovingTile tile = new MovingTile(id, x, y);
		myRoot.getChildren().add(tile.getImage());
		myAppearingTiles.add(tile);
		myGrid[(int)locChosen.getY()][(int)locChosen.getX()] = false;
		return tile;
	}
	
	protected void combineTiles(MovingTile tileOne, MovingTile tileTwo) {
		myScore.updateScore(tileOne.getValue() + tileTwo.getValue());
		myDisappearingTiles.add(tileOne);
		myDisappearingTiles.add(tileTwo);
		int id = tileOne.getID() + 1;
		if (id == 11) {
			my2048 = true;
		}
		MovingTile newTile = new MovingTile(id, tileTwo.getX(), tileTwo.getY());
		myRoot.getChildren().add(newTile.getImage());
		myAppearingTiles.add(newTile);
		tileOne.setCombined(true);
		tileTwo.setCombined(true);
		newTile.setCombined(true);
	}
	
	protected void clearLists() {
		myAppearingTiles.clear();
		myDisappearingTiles.clear();
	}
	
	protected ArrayList<MovingTile> getAppearingTiles() {
		return myAppearingTiles;
	}
	
	protected ArrayList<MovingTile> getDisappearingTiles() {
		return myDisappearingTiles;
	}
	
	protected void continueGame() {
		myContinue = true;
	}
	
	protected boolean get2048() {
		if (myContinue) {
			return false;
		}
		return my2048;
	}
	
	protected boolean gameOver(ArrayList<MovingTile> tileList) {
		if (generateSafeArray().size() > 0) {
			return false;
		}
		else {
			int[] nextX = {0, 0, -1, 1};
			int[] nextY = {-1, 1, 0, 0};
			for (MovingTile tile : tileList) {
				int x = tile.getX();
				int y = tile.getY();
				for (int z = 0; z < nextX.length; z++) {
					MovingTile otherTile = getTileByLocation(tileList, x + nextX[z], y + nextY[z]);
					if (otherTile != null && tile.getID() == otherTile.getID()) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private void refreshGrid() {
		for (int y = 0; y < myGrid.length; y++) {
			for (int x = 0; x < myGrid[y].length; x++) {
				myGrid[y][x] = true;
			}
		}
	}
	
	protected void shiftTilesUp(ArrayList<MovingTile> tileList) {
		for (MovingTile tile : tileList) {
			int x = tile.getX();
			for (int y = tile.getY(); y > 0; y--) {
				if (myGrid[y-1][x]) {
					myGrid[y][x] = true;
					myGrid[y-1][x] = false;
					tile.updateLocation(x, y-1);
				}
				else {
					MovingTile otherTile = getTileByLocation(tileList, x, y-1);
					if (tile.getID() == otherTile.getID() && !otherTile.getCombined()) {
						myGrid[y][x] = true;
						tile.updateLocation(x, y-1);
						combineTiles(tile, otherTile);
					}
					else {
						y = 0;
					}
				}
			}
		}
	}
	
	protected void shiftTilesDown(ArrayList<MovingTile> tileList) {
		for (MovingTile tile : tileList) {
			int x = tile.getX();
			for (int y = tile.getY(); y < myGrid.length - 1; y++) {
				if (myGrid[y+1][x]) {
					myGrid[y][x] = true;
					myGrid[y+1][x] = false;
					tile.updateLocation(x, y+1);
				}
				else {
					MovingTile otherTile = getTileByLocation(tileList, x, y+1);
					if (tile.getID() == otherTile.getID() && !otherTile.getCombined()) {
						myGrid[y][x] = true;
						tile.updateLocation(x, y+1);
						combineTiles(tile, otherTile);
					}
					else {
						y = myGrid.length;
					}
				}
			}
		}
	}
	
	protected void shiftTilesLeft(ArrayList<MovingTile> tileList) {
		for (MovingTile tile : tileList) {
			int y = tile.getY();
			for (int x = tile.getX(); x > 0; x--) {
				if (myGrid[y][x-1]) {
					myGrid[y][x] = true;
					myGrid[y][x-1] = false;
					tile.updateLocation(x-1, y);
				}
				else {
					MovingTile otherTile = getTileByLocation(tileList, x-1, y);
					if (tile.getID() == otherTile.getID() && !otherTile.getCombined()) {
						myGrid[y][x] = true;
						tile.updateLocation(x-1, y);
						combineTiles(tile, otherTile);
					}
					else {
						x = 0;
					}
				}
			}
		}
	}
	
	protected void shiftTilesRight(ArrayList<MovingTile> tileList) {
		for (MovingTile tile : tileList) {
			int y = tile.getY();
			for (int x = tile.getX(); x < myGrid.length - 1; x++) {
				if (myGrid[y][x+1]) {
					myGrid[y][x] = true;
					myGrid[y][x+1] = false;
					tile.updateLocation(x+1, y);
				}
				else {
					MovingTile otherTile = getTileByLocation(tileList, x+1, y);
					if (tile.getID() == otherTile.getID() && !otherTile.getCombined()) {
						myGrid[y][x] = true;
						tile.updateLocation(x+1, y);
						combineTiles(tile, otherTile);
					}
					else {
						x = myGrid.length;
					}
				}
			}
		}
	}
	
	private MovingTile getTileByLocation(ArrayList<MovingTile> tileList, int x, int y) {
		for (MovingTile tile : tileList) {
			if (x == tile.getX() && y == tile.getY()) {
				return tile;
			}
		}
		return null;
	}
}