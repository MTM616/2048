package application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Setup {
	
	public Setup(Scene scene, Group root, Button replayButton, ScoreManager score) {
		Text scoreDisplay = new Text("Score:");
		scoreDisplay.setLayoutX(400);
		scoreDisplay.setLayoutY(100);
		
		Text highScoreDisplay = new Text("High Score:");
		highScoreDisplay.setLayoutX(20);
		highScoreDisplay.setLayoutY(100);
		
		Rectangle gridBorder = new Rectangle(500, 500, Color.GREY);
		gridBorder.setLayoutX(10);
		gridBorder.setLayoutY(110);
		gridBorder.setArcHeight(10);
		gridBorder.setArcWidth(10);
		
		Rectangle[] gridArray = new Rectangle[16];
		for (int i = 0; i < gridArray.length; i++) {
			gridArray[i] = new Rectangle(110, 110, Color.WHITE);
			gridArray[i].setLayoutX(25 + (120 * (i % 4)));
			gridArray[i].setLayoutY(125 + (120 * (i / 4)));
			gridArray[i].setArcHeight(5);
			gridArray[i].setArcWidth(5);
		}
		
		root.getChildren().addAll(scoreDisplay, highScoreDisplay, gridBorder);
		root.getChildren().addAll(gridArray);
		new GameExecutor(scene, root, score, replayButton);
	}
}