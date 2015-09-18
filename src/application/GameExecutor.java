package application;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

public class GameExecutor {
	
	private Group myRoot;
	private Scene myScene;
	private PhantomGrid myGrid;
	private TileSorter mySorter;
	private Timeline myMainTimeline;
	private Timeline myEndGameTimeline;
	private ArrayList<MovingTile> myTiles;
	private ImageView myOverlay;
	private Button myWinButton;
	private Button myLoseButton;
	private boolean myGameOver;
	private boolean myTilesMoved;
	private int myDelayCounter;
	
	public GameExecutor(Scene scene, Group root, ScoreManager score, Button replayButton) {
		int gridSize = 4;
		
		myRoot = root;
		myScene = scene;
		myTiles = new ArrayList<MovingTile>();
		myGrid = new PhantomGrid(root, score, gridSize);
		myGrid.placeInitialTiles(myTiles);
		mySorter = new TileSorter(gridSize);
		myMainTimeline = new Timeline();
		myEndGameTimeline = new Timeline();
		myMainTimeline.setCycleCount(Timeline.INDEFINITE);
		myEndGameTimeline.setCycleCount(Timeline.INDEFINITE);
		myScene.setOnKeyPressed(keyHandler);
		myLoseButton = replayButton;
		myOverlay = new ImageView();
		myOverlay.setOpacity(0.0);
		myGameOver = false;
	}
	
	private void resetTiles() {
		myGrid.clearLists();
		for (MovingTile tile : myTiles) {
			tile.setCombined(false);
		}
	}
	
	private EventHandler<KeyEvent> keyHandler = new EventHandler<KeyEvent>() {
		@Override
		public void handle(KeyEvent event) {
			if (myGameOver || myMainTimeline.getKeyFrames().size() > 0) {
				return;
			}
			myTilesMoved = false;
			resetTiles();
			if (event.getCode() == KeyCode.UP) {
				myTiles = mySorter.sortTilesUp(myTiles);
				myGrid.shiftTilesUp(myTiles);
			}
			else if (event.getCode() == KeyCode.DOWN) {
				myTiles = mySorter.sortTilesDown(myTiles);
				myGrid.shiftTilesDown(myTiles);
			}
			else if (event.getCode() == KeyCode.LEFT) {
				myTiles = mySorter.sortTilesLeft(myTiles);
				myGrid.shiftTilesLeft(myTiles);
			}
			else if (event.getCode() == KeyCode.RIGHT) {
				myTiles = mySorter.sortTilesRight(myTiles);
				myGrid.shiftTilesRight(myTiles);
			}
			else {
				return;
			}
			updateTiles();
			myMainTimeline.getKeyFrames().add(movingTilesFrame);
			myMainTimeline.play();
		}
	};
	
	private void updateTiles() {
		for (MovingTile tile : myGrid.getAppearingTiles()) {
			myTiles.add(tile);
		}
		for (MovingTile tile : myGrid.getDisappearingTiles()) {
			myTiles.remove(tile);
		}
	}
	
	private KeyFrame movingTilesFrame = new KeyFrame(Duration.millis(5), new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			boolean shifted = false;
			for (MovingTile tile : myTiles) {
				if (tile.shift()) {
					shifted = true;
					myTilesMoved = true;
				}
			}
			if (!shifted) {
				myMainTimeline.stop();
				myMainTimeline.getKeyFrames().remove(movingTilesFrame);
				if (myTilesMoved || myGrid.getDisappearingTiles().size() > 0) {
					myTiles.add(myGrid.placeNewTile());
				}
				myMainTimeline.getKeyFrames().add(changingTilesFrame);
				myMainTimeline.play();
			}
		}
	});
	
	private KeyFrame changingTilesFrame = new KeyFrame(Duration.millis(5), new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			boolean changed = false;
			for (MovingTile tile : myGrid.getDisappearingTiles()) {
				if (tile.disappear()) {
					changed = true;
				}
			}
			for (MovingTile tile : myGrid.getAppearingTiles()) {
				if (tile.appear()) {
					changed = true;
				}
			}
			if (!changed) {
				myMainTimeline.stop();
				myMainTimeline.getKeyFrames().remove(changingTilesFrame);
				for (MovingTile tile : myGrid.getDisappearingTiles()) {
					myRoot.getChildren().remove(tile.getImage());
				}
				if (myGrid.get2048()) {
					winGame();
				}
				else if (myGrid.gameOver(myTiles)) {
					loseGame();
				}
			}
		}
	});
	
	private void winGame() {
		myGameOver = true;
		myDelayCounter = 0;
		myWinButton = new Button("Continue");
		myWinButton.setPrefSize(120, 60);
		myWinButton.setLayoutX(200);
		myWinButton.setLayoutY(500);
		myWinButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				myGrid.continueGame();
				myRoot.getChildren().remove(myWinButton);
				myGameOver = false;
				myEndGameTimeline.getKeyFrames().add(continueGameFrame);
				myEndGameTimeline.play();
			}
		});
		myOverlay.setImage(new Image("/resources/Winner.jpg"));
		myRoot.getChildren().add(myOverlay);
		myEndGameTimeline.getKeyFrames().add(winGameFrame);
		myEndGameTimeline.play();
	}
	
	private void loseGame() {
		myGameOver = true;
		myDelayCounter = 0;
		myOverlay.setImage(new Image("/resources/Over.jpg"));
		myRoot.getChildren().add(myOverlay);
		myEndGameTimeline.getKeyFrames().add(loseGameFrame);
		myEndGameTimeline.play();
	}
	
	private KeyFrame winGameFrame = new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			if (myDelayCounter < 10) {
				myDelayCounter += 1;
				return;
			}
			double opacity = myOverlay.getOpacity();
			if (opacity < 1) {
				myOverlay.setOpacity(opacity + 0.1);
			}
			else {
				myEndGameTimeline.stop();
				myEndGameTimeline.getKeyFrames().remove(winGameFrame);
				myRoot.getChildren().add(myWinButton);
			}
		}
	});
	
	private KeyFrame continueGameFrame = new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			double opacity = myOverlay.getOpacity();
			if (opacity > 0) {
				myOverlay.setOpacity(opacity - 0.1);
			}
			else {
				myEndGameTimeline.stop();
				myRoot.getChildren().remove(myOverlay);
				myEndGameTimeline.getKeyFrames().remove(continueGameFrame);
				if (myGrid.gameOver(myTiles)) {
					loseGame();
				}
			}
		}
	});
	
	private KeyFrame loseGameFrame = new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			if (myDelayCounter < 10) {
				myDelayCounter += 1;
				return;
			}
			double opacity = myOverlay.getOpacity();
			if (opacity < 0.75) {
				myOverlay.setOpacity(opacity + 0.075);
			}
			else {
				myEndGameTimeline.stop();
				myRoot.getChildren().add(myLoseButton);
			}
		}
	});
}