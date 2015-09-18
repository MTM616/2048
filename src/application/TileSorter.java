package application;

import java.util.ArrayList;

public class TileSorter {
	
	private int myGridArea;
	
	public TileSorter(int gridSize) {
		myGridArea = gridSize * gridSize;
	}
	
	protected ArrayList<MovingTile> sortTilesUp(ArrayList<MovingTile> unsortedTiles) {
		MovingTile[] tileArray = new MovingTile[myGridArea];
		for (MovingTile tile : unsortedTiles) {
			int arrayIndex = (4 * tile.getX()) + tile.getY();
			tileArray[arrayIndex] = tile;
		}
		return condenseArray(tileArray);
	}
	
	protected ArrayList<MovingTile> sortTilesDown(ArrayList<MovingTile> unsortedTiles) {
		MovingTile[] tileArray = new MovingTile[myGridArea];
		for (MovingTile tile : unsortedTiles) {
			int arrayIndex = (4 * tile.getX()) - tile.getY() + 3;
			tileArray[arrayIndex] = tile;
		}
		return condenseArray(tileArray);
	}
	
	protected ArrayList<MovingTile> sortTilesLeft(ArrayList<MovingTile> unsortedTiles) {
		MovingTile[] tileArray = new MovingTile[myGridArea];
		for (MovingTile tile : unsortedTiles) {
			int arrayIndex = tile.getX() + (4 * tile.getY());
			tileArray[arrayIndex] = tile;
		}
		return condenseArray(tileArray);
	}
	
	protected ArrayList<MovingTile> sortTilesRight(ArrayList<MovingTile> unsortedTiles) {
		MovingTile[] tileArray = new MovingTile[myGridArea];
		for (MovingTile tile : unsortedTiles) {
			int arrayIndex = - tile.getX() + (4 * tile.getY()) + 3;
			tileArray[arrayIndex] = tile;
		}
		return condenseArray(tileArray);
	}
	
	private ArrayList<MovingTile> condenseArray(MovingTile[] tileArray) {
		ArrayList<MovingTile> sortedTiles = new ArrayList<MovingTile>();
		for (int i = 0; i < tileArray.length; i++) {
			if (!(tileArray[i] == null)) {
				sortedTiles.add(tileArray[i]);
			}
		}
		return sortedTiles;
	}
}