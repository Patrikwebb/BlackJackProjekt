	package gui;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import com.sun.javafx.scene.EnteredExitedHandler;

import javafx.application.Platform;
import javafx.geometry.*;


/**
 * This is an input window for the players name </Br >
 * It will popup when you start our application
 * @author PatrikWebb
 */
public class NameAlertBox {

	TextField nameInput;
	String name;
    
	/**
	 * Getter for name Input
	 * @return name
	 */
    public String getName() {
		return name;
	}
    
    /**
	 * Setter for name Input
	 * @return name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Here is our whole stage with layout, </Br >
	 * ( Grid, TextField and Buttons)
	 * 
	 * @author PatrikWebb
	 */
	public void NameDisplay() {
        Stage nameWindow = new Stage();

        //Block events to other windows
        nameWindow.initModality(Modality.APPLICATION_MODAL);
        nameWindow.setTitle("Enter your name");
        nameWindow.setMinWidth(250);
        
        // GridPane Form
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        
        //Name Label 
        Label nameLabel = new Label("Player Name:");
        GridPane.setConstraints(nameLabel, 0, 0);
        
        //Name Input
        nameInput = new TextField();
        nameInput.setPromptText("Name");
        GridPane.setConstraints(nameInput, 1, 0);
        
        // Enter button
        Button enterButton = new Button("Enter");
        //setPercentWidth(50);
        GridPane.setConstraints(enterButton, 1, 2);
        
        // Cancel button
        Button cancelButton = new Button("Cancel");
        GridPane.setConstraints(cancelButton, 2, 2);
        
        enterButton.setOnAction(e -> {

        	Platform.runLater(() -> {
        	// Get the input from the TextField
        	String input = nameInput.getText();
        	// Set the input in our setter for setName metod
        	setName(input);
        	
        	//TODO
        	/*
        	 * **************
        	 * Add the name input to the registred player array list
        	 * **************
        	 * If Succes ! ! ---->
        	 * nameWindow.close();
        	 * **************
        	 */
        	
        	System.out.println(getName());
        	});
        });
        
        cancelButton.setOnAction(e -> {
        	// TODO
        	Platform.runLater(() -> {
        	nameWindow.close();
        	});
        });

        //Add everything to grid
        grid.getChildren().addAll(nameLabel, nameInput, enterButton, cancelButton);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(grid, 400, 150);
        nameWindow.setScene(scene);
        nameWindow.showAndWait();
    }

}