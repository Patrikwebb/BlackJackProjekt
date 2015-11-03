package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Gui extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		try {
			BorderPane page = (BorderPane) FXMLLoader.load(Gui.class.getResource("BlackJackFXML.fxml"));
																				
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
