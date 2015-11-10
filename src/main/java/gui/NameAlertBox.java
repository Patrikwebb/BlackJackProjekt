	package gui;

import javafx.stage.*;
import kodaLoss.Bank;
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

	TextField nameInput, playerCashInput;
	String name;
	int playerCash;
	
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
        playerCashInput = new TextField();
        playerCashInput.setFocusTraversable(false);
        playerCashInput.setPromptText("Betting amount");
        GridPane.setConstraints(playerCashInput, 1, 1);
        
        // Enter button
        Button enterButton = new Button("Enter");
        enterButton.setFocusTraversable(false);
        //setPercentWidth(50);
        GridPane.setConstraints(enterButton, 1, 2);
        
        // Cancel button
        Button cancelButton = new Button("Cancel");
        cancelButton.setFocusTraversable(false);
        GridPane.setConstraints(cancelButton, 2, 2);
        
        
        // 
        enterButton.setOnAction(e -> {

        	Platform.runLater(() -> {
        	// Get the input from the nameInput TextField
        	name = nameInput.getText();
        	// Get the betting input from the bettsInput TextField
        	playerCash = Integer.parseInt(playerCashInput.getText());
        	// Add name and playerCash input to a new player
        	Bank.getInstance().addPlayerToBank(name, playerCash);
        	// Add the player to the table
        	Bank.getInstance().addPlayersToTheTable();
        	// Close the stage
        	nameWindow.close();
        	
        	System.out.println("\nPlayer Name: " + name);
        	System.out.println("Betting Amount: " + playerCash + "\n");
        	
        	});
        });
        
        cancelButton.setOnAction(e -> {
        	// TODO
        	Platform.runLater(() -> {
        	nameWindow.close();
        	});
        });

        //Add everything to grid
        grid.getChildren().addAll(nameLabel, nameInput, bettingLabel, playerCashInput, enterButton, cancelButton);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(grid, 420, 150);
        nameWindow.setScene(scene);
        nameWindow.showAndWait();
    }

}