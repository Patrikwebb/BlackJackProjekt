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

	TextField nameInput, bettsInput;
	String name, bettingAmount;
	
	/**
	 * Getter for the betting amount the player entered
	 * @return bettingAmount
	 */
	public String getBettingAmount() {
		return bettingAmount;
	}
	
	/**
	 * Setter for the betting amount the player entered
	 * @return bettingAmount
	 */
	public void setBettingAmount(String bettingAmount) {
		this.bettingAmount = bettingAmount;
	}

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
        nameInput.setFocusTraversable(false);
        GridPane.setConstraints(nameInput, 1, 0);
        
        // Betting Label
        Label bettingLabel = new Label("Betting amount: ");
        GridPane.setConstraints(bettingLabel, 0, 1);
        
        // Bett Input
        bettsInput = new TextField();
        bettsInput.setFocusTraversable(false);
        bettsInput.setPromptText("Betting amount");
        GridPane.setConstraints(bettsInput, 1, 1);
        
        // Enter button
        Button enterButton = new Button("Enter");
        enterButton.setFocusTraversable(false);
        //setPercentWidth(50);
        GridPane.setConstraints(enterButton, 1, 2);
        
        // Cancel button
        Button cancelButton = new Button("Cancel");
        cancelButton.setFocusTraversable(false);
        GridPane.setConstraints(cancelButton, 2, 2);
        
        enterButton.setOnAction(e -> {

        	Platform.runLater(() -> {
        	// Get the input from the nameInput TextField
        	String input = nameInput.getText();
        	// Set the input in our setter for setName metod
        	setName(input);
        	
        	// Get the betting input from the bettsInput TextField
        	String bettInput = bettsInput.getText();
        	// Set the betting input in our setter for setBettingAmount metod
        	setBettingAmount(bettInput);
        	
        	//TODO
        	/*
        	 * **************
        	 * Add the name input to the registred player array list
        	 * **************
        	 * If Succes ! ! ---->
        	 * nameWindow.close();
        	 * **************
        	 */
        	
        	System.out.println("\nPlayer Name: " + getName());
        	System.out.println("\nBetting Amount: " + getBettingAmount());
        	});
        });
        
        cancelButton.setOnAction(e -> {
        	// TODO
        	Platform.runLater(() -> {
        	nameWindow.close();
        	});
        });

        //Add everything to grid
        grid.getChildren().addAll(nameLabel, nameInput, bettingLabel, bettsInput, enterButton, cancelButton);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(grid, 420, 150);
        nameWindow.setScene(scene);
        nameWindow.showAndWait();
    }

}