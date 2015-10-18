package gui;

import javax.swing.JOptionPane;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kodaLoss.Bank;
import kodaLoss.Card;
import static timTestaNat.BlackJackConstants.*;
import timTestaNat.CardPictureData;

/**
 * @author PatrikWebb
 * @Gui Main.java
 */
public class Main extends Application {

    Stage window;
    Scene scene;
private Bank bank = new Bank(this);

private CardPictureData pics = new CardPictureData();
    

private Label playerScore;
private ImageView testpic;    

public void setPlayerScore( int newScore ){
  playerScore.setText("" + newScore);
}

public void setTestPic(Card card ){
  
  String s = 
        FILE_PICTURE_PROTOKOLL 
        + FILE_PICTURE_PATH
        + card.getSuite().toString() + " " 
        + card.getRank().toString() 
        + FILE_PICTURE_FILEENDING;
    
  Image image = new Image( s );
  
  System.out.println(image.toString());
  testpic.setImage(image);
  
  
  System.out.println(testpic.isVisible());
  System.out.println("TESTCARD SHOWN?!?!");
}

public void testPrint(){
  System.out.println("Main.testPrint() called in Main!");
}
    
    
    // DO NOT TOUCH! 
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
//        Button buttonStand = new Button("Stand");
//        	buttonStand.setOnAction(new EventHandler<ActionEvent>() {
//            
//            @Override
//            public void handle(ActionEvent event) {
//              // TODO Auto-generated method stub
//              bank.testPrint();
//            }
//          });
        
        //Button 1
        	Button buttonStand = new Button("Stand");
        	buttonStand.setOnAction(e -> bank.testPrint());
        //Button 2
        	//addCardToHand(Card card)
        Button buttonHit = new Button("Hit");
        	buttonHit.setOnAction(e -> testPrint());
        	
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
        	playerScore = new Label("");
        	// String getName()
        	Label playerCard = new Label ("Text: DIAMONDS ACE");
        	testpic = new ImageView();
        	
        	
        	
        	Label playerHandsSize = new Label ("Player Hands Size: ");
        	// getPlayerHandsSize()
        	playerText.setId("playerText");
        	playerScore.setId("playerScore");
        	playerCard.setId("playerCard");
        	playerHandsSize.setId("playerHandsSize");
        	player.getChildren().addAll(playerText, playerScore, playerCard, testpic );
        
        //Bottom Menu (Buttons)
        HBox bottomMenu = new HBox();
        	bottomMenu.setId("bottomMenu");
        	bottomMenu.getChildren().addAll(buttonStand, buttonHit);
        
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