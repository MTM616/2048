package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MovingTile {
	
	private int myID;
	private int myValue;
	private int myX;
	private int myY;
	private int myXCoordinate;
	private int myYCoordinate;
	private ImageView myImage;
	private boolean myCombined;
	
	public MovingTile(int id, int x, int y) {
		myID = id;
		myValue = (int) Math.pow(2, id);
		myCombined = false;
		if (myValue > 2048) {
			myImage = new ImageView(new Image("/resources/Higher.jpg"));
		}
		else {
			myImage = new ImageView(new Image("/resources/" + myValue + ".jpg"));
		}
		updateLocation(x, y);
		myImage.setLayoutX(myXCoordinate);
		myImage.setLayoutY(myYCoordinate);
		myImage.setOpacity(0);
		myImage.setScaleX(0);
		myImage.setScaleY(0);
	}
	
	protected int getID() {
		return myID;
	}
	
	protected int getValue() {
		return myValue;
	}
	
	protected ImageView getImage() {
		return myImage;
	}
	
	protected int getX() {
		return myX;
	}
	
	protected int getY() {
		return myY;
	}
	
	protected void updateLocation(int x, int y) {
		myX = x;
		myY = y;
		myXCoordinate = 30 + (120 * x);
		myYCoordinate = 130 + (120 * y);
	}
	
	protected void reveal() {
		myImage.setScaleX(1.0);
		myImage.setScaleY(1.0);
		myImage.setOpacity(1.0);
	}
	
	protected boolean shift() {
		int coordinate = (int) myImage.getLayoutX();
		if (coordinate > myXCoordinate) {
			myImage.setLayoutX(coordinate - 10);
			return true;
		}
		if (coordinate < myXCoordinate) {
			myImage.setLayoutX(coordinate + 10);
			return true;
		}
		coordinate = (int) myImage.getLayoutY();
		if (coordinate > myYCoordinate) {
			myImage.setLayoutY(coordinate - 10);
			return true;
		}
		if (coordinate < myYCoordinate) {
			myImage.setLayoutY(coordinate + 10);
			return true;
		}
		return false;
	}
	
	protected boolean appear() {
		double opacity = myImage.getOpacity();
		if (opacity < 1.0) {
			myImage.setOpacity(opacity + 0.1);
			myImage.setScaleX(opacity + 0.1);
			myImage.setScaleY(opacity + 0.1);
			return true;
		}
		myImage.setScaleX(1.0);
		myImage.setScaleY(1.0);
		return false;
	}
	
	protected boolean disappear() {
		double opacity = myImage.getOpacity();
		if (opacity > 0) {
			myImage.setOpacity(opacity - 0.1);
			myImage.setScaleX(2 - opacity);
			myImage.setScaleY(2 - opacity);
			return true;
		}
		return false;
	}
	
	protected boolean getCombined() {
		return myCombined;
	}
	
	protected void setCombined(boolean combined) {
		myCombined = combined;
	}
}