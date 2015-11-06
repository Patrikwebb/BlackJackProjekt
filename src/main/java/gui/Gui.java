package gui;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Loads BlackJack FXML file and Launches the Gui Application
 * @author PatrikWebb
 */
public class Gui extends Application{
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		try {
			BorderPane page = (BorderPane) FXMLLoader.load(Gui.class.getResource("BlackJackFXMLVersion2.fxml"));
																				
			Scene scene = new Scene(page);
				primaryStage.setScene(scene); 
				primaryStage.show();
			    	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		Application.launch(Gui.class, (java.lang.String[])null);
	}
}
