package application;

import javafx.scene.text.Text;

public class ScoreManager {
	
	private Main myMainClass;
	private int myScore;
	private Text myScoreText;
	private Text myHighScoreText;
	
	public ScoreManager(Main mainClass, Text scoreText, Text highScoreText) {
		myMainClass = mainClass;
		myScore = 0;
		myScoreText = scoreText;
		myHighScoreText = highScoreText;
		myScoreText.setText("0");
		myHighScoreText.setText("" + myMainClass.updateHighScore(myScore));
	}
	
	protected void updateScore(int score) {
		myScore += score;
		myScoreText.setText("" + myScore);
		myHighScoreText.setText("" + myMainClass.updateHighScore(myScore));
	}
}