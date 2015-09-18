package application;
	
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Main extends Application {
	
	private int myHighScore;
	
	@Override
	public void start(Stage stage) {
		Group root = new Group();
		Scene scene = new Scene(root, 520, 620, Color.WHITESMOKE);
		stage.setTitle("2048");
		stage.setScene(scene);
		stage.show();
		
		ImageView titleDisplay = new ImageView(new Image("/resources/Title.png"));
		titleDisplay.setLayoutX(20);
		titleDisplay.setLayoutY(15);
		
		Text scoreText = new Text();
		scoreText.setLayoutX(450);
		scoreText.setLayoutY(100);
		
		Text highScoreText = new Text();
		highScoreText.setLayoutX(100);
		highScoreText.setLayoutY(100);
		
		Main selfReference = this;
		myHighScore = 0;
		
		Button playButton = new Button("Play");
		playButton.setPrefSize(120, 60);
		playButton.setLayoutX(200);
		playButton.setLayoutY(300);
		playButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				root.getChildren().clear();
				playButton.setText("Replay");
				playButton.setLayoutX(200);
				playButton.setLayoutY(500);
				ScoreManager score = new ScoreManager(selfReference, scoreText, highScoreText);
				root.getChildren().addAll(titleDisplay, scoreText, highScoreText);
				new Setup(scene, root, playButton, score);
			}
		});
		
		root.getChildren().addAll(titleDisplay, playButton);
	}
	
	protected int updateHighScore(int score) {
		if (score > myHighScore) {
			myHighScore = score;
		}
		return myHighScore;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}