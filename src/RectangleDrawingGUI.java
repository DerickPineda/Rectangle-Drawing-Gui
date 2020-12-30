/*
 * CIS 255 PROJECT 5
 * WRITTEN BY DERICK PINEDA AND TYLER LOPEZ
*/

import javafx.application.*;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.*;
import java.lang.Math;

public class RectangleDrawingGUI extends Application {
	
	private Pane drawingPane;
	private FlowPane pane;
	private Rectangle rectangle;
	private Circle circle;
	private boolean shapeBeingDrawn;
	private boolean circleMode = false;
	
	//Had to move all of these declarations to a wider scope to make manipulation and reference easier throughout
	RadioButton redButton = new RadioButton();
	RadioButton yellowButton = new RadioButton();
	RadioButton blueButton = new RadioButton();
	RadioButton thinBorderButton = new RadioButton();
	RadioButton thickBorderButton = new RadioButton();
	CheckBox fillCheckBox = new CheckBox();
	CheckBox circleCheckBox = new CheckBox();
	
	/* Check the handleMouseClicks() method as to why these are here */
	private Color borderColor = Color.RED;
	private Color filler = Color.TRANSPARENT;
	private double strokeWidth = 1;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		/* The FlowPane (pane) will hold the drawing space and the buttons, 
		 * The Pane (drawingPane) is nested within the FlowPane to help draw the shapes within the drawing space */
		pane = new FlowPane();
		drawingPane = new Pane();
		
		pane.setVgap(10);
		pane.setAlignment(Pos.CENTER);
		drawingPane.setMaxWidth(575);
		drawingPane.setMaxHeight(400);
		
		/* Making the space where Rectangles will be drawn */
		Rectangle drawingSpace = new Rectangle(575,400);
		drawingSpace.setFill(Color.TRANSPARENT);
		drawingSpace.setStroke(Color.BLACK);
		
		drawingPane.getChildren().add(drawingSpace);
		pane.getChildren().add(drawingPane);
		
		
		/* These methods draw the rectangles when the user clicks within the drawing space */
		drawingPane.setOnMouseClicked(this::handleMouseClicks);
		drawingPane.setOnMouseMoved(this::handleMouseMovement);
		
		
		/* Making the space where the buttons will go. The color, border and clear buttons will be separated vertically
		 * using a Vbox which will contain two HBox's used to separate the buttons horizontally  */
		VBox buttonsVBox = new VBox();
		HBox colorButtonHBox = new HBox();
		HBox fillButtonHBox = new HBox();
		
		colorButtonHBox.setSpacing(20);
		colorButtonHBox.setAlignment(Pos.TOP_CENTER);
		
		fillButtonHBox.setSpacing(20);
		fillButtonHBox.setAlignment(Pos.TOP_CENTER);
		
		buttonsVBox.setSpacing(8);
		buttonsVBox.setAlignment(Pos.TOP_CENTER);
		buttonsVBox.getChildren().add(colorButtonHBox);
		buttonsVBox.getChildren().add(fillButtonHBox);
		pane.getChildren().add(buttonsVBox);
		
			
		/* Making/adding color Buttons to colorButtonHBox */
		redButton.setText("Red");
		redButton.setOnAction(this::handleColorRadioButton);
		yellowButton.setText("Yellow");
		yellowButton.setOnAction(this::handleColorRadioButton);
		blueButton.setText("Blue");
		blueButton.setOnAction(this::handleColorRadioButton);
		
		ToggleGroup colors = new ToggleGroup();
		redButton.setToggleGroup(colors);
		yellowButton.setToggleGroup(colors);
		blueButton.setToggleGroup(colors);
		redButton.setSelected(true);
	
		
		circleCheckBox.setText("Circle Mode");
		circleCheckBox.setOnAction(this::handleCircle);
		
		colorButtonHBox.getChildren().add(redButton);
		colorButtonHBox.getChildren().add(yellowButton);
		colorButtonHBox.getChildren().add(blueButton);
		colorButtonHBox.getChildren().add(circleCheckBox);
		
		
		/* Making/adding Border/Fill Buttons */
		thinBorderButton.setText("Thin Border");
		thinBorderButton.setSelected(true);
		thinBorderButton.setOnAction(this::handleStrokeWidth);
		thickBorderButton.setText("Thick Border");
		thickBorderButton.setOnAction(this::handleStrokeWidth);
		
		fillCheckBox.setText("Fill");
		fillCheckBox.setOnAction(this::handleRectangleFill);
		
		ToggleGroup border = new ToggleGroup();
		thinBorderButton.setToggleGroup(border);
		thickBorderButton.setToggleGroup(border);
		
		fillButtonHBox.getChildren().add(thinBorderButton);
		fillButtonHBox.getChildren().add(thickBorderButton);
		fillButtonHBox.getChildren().add(fillCheckBox);	
		
		
		/* Making/adding the Clear button */
		Button clearButton = new Button();
		clearButton.setText("Clear");
		clearButton.setOnAction(this::handleClearButton);
		
		buttonsVBox.getChildren().add(clearButton);
		
		Scene scene = new Scene(pane, 600, 500);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Rectangles and Circles!");
		primaryStage.show();
	}
	
	/*In order to add the functionality of drawing a circle, I created a boolean that says whether or not we are about to
	 * draw a circle (set to false by default)+. Inside both the handleMouseClicks and handleMouseMovement, I nested all 
	 * of the primary drawing functions inside an if statement that checks if the next shape to be drawn will be a circle 
	 * or not. I pretty much followed the methods you used to draw a rectangle, just adapted for drawing a circle.  */
	
	public void handleMouseClicks(MouseEvent event){
		if (circleMode) {
			if(!shapeBeingDrawn) {
				shapeBeingDrawn = true;
				
				double x = event.getX();
				double y = event.getY();
				
				circle = new Circle(x, y, 0, filler);
				drawingPane.getChildren().add(circle);
				
				circle.setStroke(borderColor);
				circle.setStrokeWidth(strokeWidth);
			} else {
				shapeBeingDrawn = false;
			}
			
		} else {
		/* If shapeBeingDrawn is false, a shape is not being drawn so it will draw a new one once the user clicks */
			if(!shapeBeingDrawn) {
				shapeBeingDrawn = true;
			
				double x = event.getX();
				double y = event.getY();

				rectangle = new Rectangle(x, y, -1, -1);
				drawingPane.getChildren().add(rectangle);
			
			
			/*
			 * I made a variable for the stroke color, it can also be used for the fill when
			 * the fill button is checked. I made the variable thinking it could be changed
			 * when the user selects a color option which would then change the color of the
			 * rectangle made here I'm not sure how you wanted to go about changing the
			 * color so I figured this would be one way of doing it; also holds true for the
			 * stroke variable, however if you find a better/easier way of doing it then by
			 * all means change the code
			 */

			/* You can edit this when making the functionality for the buttons */
				rectangle.setFill(filler);
				rectangle.setStroke(borderColor);
				rectangle.setStrokeWidth(strokeWidth);
			
				} else { /* If shapeBeingDrawn is true, then we are in the middle of drawing a rectangle */
			/* Because the user clicked again, we are assuming they clicked where they want their rectangle end to be */
					shapeBeingDrawn = false;
		
			}
		}
	}
	
	public void handleMouseMovement(MouseEvent event) {
		if (circleMode) {
			if(shapeBeingDrawn) {
				/* I subtract the new x and y coordinates from the center of the circle so that I get the change in x and y instead of a raw
				 * value that does not yield the correct radius. I also do not have to check for negative or backwards values since the relevant
				 * values of radius are only the magnitude, and not the direction. Also, the java.lang.Math import is only used here.
				*/
				double xSquared = Math.pow(event.getX() - circle.getCenterX(), 2);
				double ySquared = Math.pow(event.getY() - circle.getCenterY(), 2);
				
				double radius = Math.sqrt(xSquared + ySquared);
				
				circle.setRadius(radius);
			}
			
		} else {
			if(shapeBeingDrawn) {
				double x = event.getX();
				double y = event.getY();
			
			/* The current x/y point of the mouse is subtracted from the starting x/y point of the rectangle because if you dont 
			 * it sets the width/height to the value of the x/y point of the mouse, which makes it longer than it should be*/
				double widthValue = x - rectangle.getX();
				double heightValue = y - rectangle.getY();
			
			//Draw square
				rectangle.setHeight(heightValue);
				rectangle.setWidth(widthValue);
			
			/* Each if statement accounts for when the rectangle is being drawn backwards, therefore making the values of widthValue and heightValue negative*/ 
				if(widthValue < 0 && heightValue < 0) {
					rectangle.setTranslateX(widthValue);
					rectangle.setWidth(-widthValue);
					rectangle.setTranslateY(heightValue);
					rectangle.setHeight(-heightValue);
				} else if(widthValue < 0){
					rectangle.setTranslateX(widthValue);
					rectangle.setWidth(-widthValue);

				}else if(heightValue < 0) {
					rectangle.setTranslateY(heightValue);
					rectangle.setHeight(-heightValue);
				
				} 		
			}
		}
	}
	
	private void handleClearButton(ActionEvent event) {
		
		/* Using remove() instead of clear() in order to not remove the rectangle that defines the drawing space within drawingPane (position 0) */
		drawingPane.getChildren().remove(1, drawingPane.getChildren().size());
	}
		
	//this method checks for the color selected and changes the rectagleColor variable accordingly
	private void handleColorRadioButton(ActionEvent event) {
		if (redButton.isSelected()) {
			borderColor = Color.RED;
		} else if (yellowButton.isSelected()) {
			borderColor = Color.YELLOW;
		} else /*blueButton*/ {
			borderColor = Color.BLUE;
		}
		
		/*This if statement checks if the fill box is selected and changes the filler color to the color of the
		 * border if so. And if the box is not selected, then it makes sense that the filler color should remain
		 * transparent
		 */
		if (fillCheckBox.isSelected()) {
			filler = borderColor;
		}
	}
	
	//checks for whether or not the user wants a thin or thick border and manipulates the strokeWidth variable
	private void handleStrokeWidth(ActionEvent event) {
		if (thinBorderButton.isSelected()) {
			strokeWidth = 1;
		} else {
			strokeWidth = 5;
		}
	}
	
	//created a new variable to hold the 'color' of the fill and this methods changes it depending on the choice of the user
	private void handleRectangleFill(ActionEvent event) {
		if (fillCheckBox.isSelected()) {
			filler = borderColor;
		} else {
			filler = Color.TRANSPARENT;
		}
	}
	
	//simply changes a boolean that says whether or not we want the next shape to be a circle
	private void handleCircle(ActionEvent event) {
		if (circleCheckBox.isSelected()) {
			circleMode = true;
		} else {
			circleMode = false;
		}
	}
}
