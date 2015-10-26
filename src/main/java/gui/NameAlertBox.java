package gui;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;


public class NameAlertBox {

    public static void display(String title, String message) {
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);
        Button enterButton = new Button("Enter");
        Button cancelButton = new Button("Cancel");
        
        enterButton.setOnAction(e -> {
        	
        	// TODO
        	
        });
        
        cancelButton.setOnAction(e -> {
        
        	window.close();
        	
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, enterButton, cancelButton);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

}