package gui;

import javax.swing.JOptionPane;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import kodaLoss.Player;

/**
 * @author PatrikWebb
 * @Gui Main.java
 */
public class Main extends Application {

    Stage window;
    Scene scene;

    public static void main(String[] args) {

        launch(args);
    }
    
    //Exempel för setOnAction
    /**
     * @return text name
     * @param text
     */
    
    
    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        //Button 1
        Button buttonStand = new Button("Stand");
        	buttonStand.setOnAction(e ->JOptionPane.showMessageDialog(null, 
        									"OnClick Message: Jag står kvar"));
        
    	//Button Test
        Button buttonTest = new Button("Test");
        	buttonTest.setOnAction(e -> JOptionPane.showMessageDialog(null, 
											"OnClick Message: Jag står kvar"));
        
        //Button 2
        	//addCardToHand(Card card)
        Button buttonHit = new Button("Hit");
        	buttonHit.setOnAction(e ->JOptionPane.showMessageDialog(null, 
        								"OnClick Message: Jag kommer vinna"));
        	
        //Top Menu (header)
        HBox header = new HBox();
        	Label headerText = new Label("JavaFx BlackJack Gui!");
        	headerText.setId("headerText");
        	header.getChildren().add(headerText);
        
        //Left Menu
        VBox dealer = new VBox();
        	Label dealerText = new Label("Dealer: ");
        	Label dealerCard = new Label (" ");
        	// getDealersHandSize? ()
        	dealerText.setId("dealerText");
        	dealerCard.setId("dealerCard");
        	dealer.getChildren().addAll(dealerText, dealerCard);
        
        //Right Menu
        VBox player = new VBox();
        	Label playerText = new Label("Player: ");
        	// String getName()
        	Label playerCard = new Label (" ");
        	Label playerHandsSize = new Label ("Player Hands Size: ");
        	// getPlayerHandsSize()
        	playerText.setId("playerText");
        	playerCard.setId("playerCard");
        	playerHandsSize.setId("playerHandsSize");
        	player.getChildren().addAll(playerText, playerCard, playerHandsSize);
        
        //Bottom Menu (Buttons)
        HBox bottomMenu = new HBox();
        	bottomMenu.setId("bottomMenu");
        	bottomMenu.getChildren().addAll(buttonStand, buttonHit, buttonTest);
        
        //Fönstret
        BorderPane borderPane = new BorderPane();
        	borderPane.setTop(header);
        	borderPane.setLeft(dealer);
        	borderPane.setRight(player);
        	borderPane.setBottom(bottomMenu);
        	
        	
        //Scene
        Scene scene = new Scene (borderPane, 600, 600);
        
        // StyleSheets
        scene.getStylesheets().add(getClass().getResource
        			("application.css").toExternalForm());
        // ex.2
        //scene.getStylesheets().add("application.css");
        
        //Display scene
        window.setScene(scene);
        window.setTitle("BlackJack Gui");
        window.show();
        
    }
    	// -- > Tips < --
    
	    	// Button kicka igång en metod
	    		// button.setOnAction(e -> Klassen.Metoden());
    			// button.setOnAction(e -> Metoden());
	    
    		// Id
    			// nameLabel.setId("id-label");
    
    		// StÃ¤nga ner programmet
    			// window.setOnCloseRequest(e -> {
    			//		e.consume();
    			//		closeProgram();
    			//	});
    			
    		//	private void closeProgram(){
    		//		TO DO
    		//		windows.close();
    		//	}
    		
		 	//An empty label
		 	//  Label label1 = new Label();
		    //A label with the text element
		  	//  Label label2 = new Label("Search");
		    //A label with the text element and graphical icon
    		//  Image image = new Image(getClass().getResourceAsStream("labels.jpg"));
		  	//  Label label3 = new Label("Search", new ImageView(image));
    
}