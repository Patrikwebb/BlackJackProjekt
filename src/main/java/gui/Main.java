package gui;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import kodaLoss.Bank;
import kodaLoss.Card;
import kodaLoss.Player;
import static timTestaNat.BlackJackConstants.*;
import timTestaNat.CardPictureData;

public class Main extends Application {

    Stage window;
    Scene scene;
    
    /**
     * Referens till bank
     * @return bank
     */
    private Bank bank = new Bank(this);

    /**
     * Referens till player
     * @return player
     */
    private Player player = new Player(this);
    
    /**
     * Referens till card
     * @return player
     */
    // private Card card = new Card();
    
    // TO DO
    private CardPictureData pics = new CardPictureData();
    
    private Label 		playerScore;
    private ImageView 	testpic; 
    private Label 		dealerCard = new Label (" ");
    private Button 		buttonHit, buttonStand, buttonPlay;
	private boolean 	playable;

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
    
	  System.out.println(s);
	  Image image = new Image( s );
	  
	  System.out.println(image.toString());
	  testpic.setImage(image);
	  
	  System.out.println(testpic.isVisible());
	  System.out.println("TESTCARD SHOWN?!?!");
	}
    
    // DO NOT TOUCH! 
    public static void main(String[] args) {

        launch(args);
    }
 
    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        
        // Shadow Effekt på knapparna
        DropShadow dropShadow = new DropShadow();
        	dropShadow.setRadius(3.0);
        	dropShadow.setOffsetX(3.0);
        	dropShadow.setOffsetY(2.0);
        	//dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
        	dropShadow.setColor(Color.BLACK);
        
        //Button Stand
        	buttonStand = new Button("Stand");
        	buttonStand.setDisable(true);
        	buttonStand.setOnAction(e -> {
        			bank.testPrint();
        	});
        	// Rotation effect
        	RotateTransition buttonStandrotation = new RotateTransition(Duration.seconds(2.0), buttonStand);
        	buttonStandrotation.setCycleCount(Animation.INDEFINITE);
        	buttonStandrotation.setFromAngle(0);
        	buttonStandrotation.setToAngle(360);
        	buttonStandrotation.setAutoReverse(true);
        	buttonStandrotation.setCycleCount(1);
        	
        	// Buttond Stand Effect on Hover
        	buttonStand.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
        		buttonStand.setEffect(dropShadow);
        		buttonStand.setScaleX(1.5);
        		buttonStand.setScaleY(1.5);
        		buttonStandrotation.play();
        		});
        	
        	// Removes shadow effect
        	buttonStand.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
        		buttonStand.setEffect(null);
        		buttonStand.setScaleX(1);
        		buttonStand.setScaleY(1);
        		buttonStandrotation.pause();
        		});
        	
        //Button Hit
        	//addCardToHand(Card card)
        buttonHit = new Button("Hit");
    	buttonHit.setDisable(true);
        	buttonHit.setOnAction(e -> {
        		// TO DO
        		
        	});
        	
    	// Rotation effect
    	RotateTransition buttonHitRotation = new RotateTransition(Duration.seconds(2.0), buttonHit);
    	buttonHitRotation.setCycleCount(Animation.INDEFINITE);
    	buttonHitRotation.setFromAngle(0);
    	buttonHitRotation.setToAngle(360);
    	buttonHitRotation.setAutoReverse(true);
    	buttonHitRotation.setCycleCount(1);
    	
    	// Button HIT Effect on Hover
        buttonHit.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
        	buttonHit.setEffect(dropShadow);
        	buttonHit.setScaleX(1.5);
        	buttonHit.setScaleY(1.5);
        	buttonHitRotation.play();
    		});
    	
    	// Removes shadow effect
        buttonHit.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
        	buttonHit.setEffect(null);
        	buttonHit.setScaleX(1);
        	buttonHit.setScaleY(1);
        	buttonHitRotation.stop();
        		
			});
        // Button Play
        buttonPlay = new Button ("Play");
    		buttonPlay.setOnAction(event -> {
    		if (startNewGame(true)){
    			gameIsOn();
    			
    		} else {
    			gameIsOff();
    		}
    		});
        
		// Buttond Play Effect on Hover
    	buttonPlay.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
    		buttonPlay.setEffect(dropShadow);
    		buttonPlay.setScaleX(1.5);
    		buttonPlay.setScaleY(1.5);
    		});
    	
    	// Removes shadow effect
    	buttonPlay.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
    		buttonPlay.setEffect(null);
    		buttonPlay.setScaleX(1);
    		buttonPlay.setScaleY(1);
    		});
    	
        //Top Menu (header)
        HBox header = new HBox();
        	Label headerText = new Label("JavaFx BlackJack Gui!");
        	headerText.setId("headerText");
        	header.getChildren().add(headerText);
        
        //Left Menu
        VBox dealer = new VBox();
        	Label dealerText = new Label("Dealer: ");
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
        	
        	Image image = new Image("https://i.gyazo.com/51e42d8764b188d639eaa0fbe29f14d3.png");
		  	Label labelImage = new Label("", new ImageView(image));
        	
        	Label playerHandsSize = new Label ("Player Hands Size: ");
        	// getPlayerHandsSize()
        	playerText.setId("playerText");
        	playerScore.setId("playerScore");
        	playerCard.setId("playerCard");
        	playerHandsSize.setId("playerHandsSize");
        	player.getChildren().addAll(playerText, playerScore, playerCard, testpic, labelImage);
        
        //Bottom Menu (Buttons)
        HBox bottomMenu = new HBox();
        	bottomMenu.setAlignment(Pos.CENTER);
        	bottomMenu.setId("bottomMenu");
        	bottomMenu.getChildren().addAll(buttonStand, buttonHit, buttonPlay);
        
        //Fönstret
        BorderPane borderPane = new BorderPane();
        	borderPane.setTop(header);
        	borderPane.setLeft(dealer);
        	borderPane.setRight(player);
        	borderPane.setBottom(bottomMenu);
        		
        //Scene
        Scene scene = new Scene (borderPane, 700, 700);
        
        // StyleSheets
        scene.getStylesheets().add(getClass().getResource
        			("application.css").toExternalForm());
        
        //Display scene
        window.setScene(scene);
        window.setTitle("BlackJack Gui");
        window.show();
        
    }
    /**
	 * När spelet är slut så kan Play knappen användas igen
	 * @author PatrikWebb
	 * @return false
	 */
	private void gameIsOff() {
		buttonPlay.setDisable(false);	
	}
	/**
	 * När spelet har börjat så kopplats Play knappen bort
	 * @author PatrikWebb
	 * @return true
	 */
	private void gameIsOn() {
		buttonPlay.setDisable(true);
    	buttonHit.setDisable(false);
    	buttonStand.setDisable(false);
	}
	
	/**
	 * Ska senare köra igång spelet och dela ut dem första korten
	 * @author PatrikWebb
	 * 
	 */
	private boolean startNewGame(boolean startNewGame) {
		playable = true;
		return true;
		
	}

}
		// <-- Tips -->
		//
		// Välja antal CardShoes
		//		ChoiceBox CardShoeChoiceBox = new ChoiceBox(FXCollections.observableArrayList(
		//		"One Card Shoe", "Two Card Shoes", "Three Card Shoes", "Four Card Shoes")
		//);