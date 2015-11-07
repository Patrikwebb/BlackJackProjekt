package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Loads BlackJack FXML file and Launches the Gui Application
 * @author PatrikWebb
 */
public class Gui extends Application{
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		try {
			BorderPane page = (BorderPane) FXMLLoader.load(Gui.class.getResource("BlackJackFXMLVersion2.fxml"));
			
			Scene scene = new Scene(page, 1280, 720);
				primaryStage.setScene(scene); 
				primaryStage.setResizable(false);
				primaryStage.show();
				
				NameAlertBox nameAlertBox = new NameAlertBox();
				nameAlertBox.NameDisplay();
			    	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		Application.launch(Gui.class, (java.lang.String[])null);
	}
}
