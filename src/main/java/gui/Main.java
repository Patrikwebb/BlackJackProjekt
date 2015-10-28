package gui;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import kodaLoss.BlackJackConstantsAndTools;
import kodaLoss.Card;
import kodaLoss.Rank;
import kodaLoss.Suite;
import kodaLoss.UserChoiceAdapter;

import org.omg.DynamicAny.NameDynAnyPair;

import gui.NameAlertBox;

/**
 * @author PatrikWebb, Tim kod kungen
 * @version 1.0
 */
public class Main extends Application {

    Stage window;
    Scene scene;
    
    /**
     * Reference to bank
     * @return bank
     * 
     */
    private Bank bank = new Bank(this);
   
    private Label 		playerScore = new Label("");
    private ImageView 	cardPicture; 
    private Button 		buttonHit, buttonStand, buttonPlay, buttonRegister;
	private SimpleBooleanProperty playable = new SimpleBooleanProperty();
	
    private HBox dealerCardsHBox = new HBox(-45);
    private HBox playerCardsHBox = new HBox(-45);

    public void setPlayerScore( int newScore ){
    	playerScore.setText("" + newScore);
    }

    /**
     * get the picture corresponding to the game card! Using the absolute path 
     * of the picture file in users system.
     * @param card - The card to show
     */
    
	public void setTestPic(Card card ){
	  //create absolute path to cardpicture!
	  String cardString = card.toString();
	  String pathToCardPicture = BlackJackConstantsAndTools.
			  getURLStringToFileInCardPictures(cardString);
	  Image image = new Image( pathToCardPicture );
	  cardPicture = new ImageView();
	  cardPicture.setImage(image);
	}
    
    // DO NOT TOUCH! 
    public static void main(String[] args) {
        launch(args);
    } 
    
    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        
    /*
     * 		Buttons
     */
        
        // Button Stand
    	buttonStand = new Button("Stand");
    	buttonStand.setDisable(true);
    	buttonStand.setOnAction(e -> {
    		UserChoiceAdapter.playerChoosesToStay();
    			
    		// TODO
    		// Ska innehålla
    			// Om dealerns värde på korten är mindre än 17:
    			// Dela ut en kort till dealern (While Loop)
    			// endGame();
    			
    	});
    	
    	//Button Hit
        buttonHit = new Button("Hit");
    	buttonHit.setDisable(true);
        	buttonHit.setOnAction(e -> {
        		UserChoiceAdapter.playerChoosesToHit();
        	  
        		setTestPic(getRandomCard());
        	  
        		// Add the Image to player HBox
        		playerCardsHBox.getChildren().add(cardPicture);
        		
        		// TODO
        		// Ska innehålla
        			// Dela ut ett kort till spelare
          	
        });
        	
    	// Button Play
        buttonPlay = new Button ("Play");
        	buttonPlay.setDisable(true);
    		buttonPlay.setOnAction(event -> {
    			
    		startNewGame();
    		
    		gameIsOn();
    		
    	});
    		
		// Button Register
    	buttonRegister = new Button ("Registrera Spelare");
    		buttonRegister.setOnAction(event -> {
 			
    			NameAlertBox name = new NameAlertBox();
    			// Kör igång NameAlertBox();
    			name.NameDisplay();
    			// JavaDoc -->
    			registreraSpelare();
    			System.out.println(bank.registeredPlayers);
 		
 		});
    		
    /*
     * 		Button Effects
     */
    		
        // Shadow Effect on all buttons
        DropShadow dropShadow = new DropShadow();
        	dropShadow.setRadius(3.0);
        	dropShadow.setOffsetX(3.0);
        	dropShadow.setOffsetY(2.0);
        	dropShadow.setColor(Color.BLACK);
        
    	// Rotation effect - Button Stand
    	RotateTransition buttonStandrotation = 
    			new RotateTransition(Duration.seconds(0.5), buttonStand);
    		buttonStandrotation.setCycleCount(Animation.INDEFINITE);
    		buttonStandrotation.setFromAngle(0);
    		buttonStandrotation.setToAngle(-5);
    		buttonStandrotation.setAutoReverse(true);
    		buttonStandrotation.setCycleCount(100);
        	
    	// Effect on Hover - Buttond Stand 
    	buttonStand.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
    		buttonStand.setEffect(dropShadow);
    		buttonStand.setScaleX(1.5);
    		buttonStand.setScaleY(1.5);
    		buttonStandrotation.play();
    		});
    	
    	// Removes shadow effect - Button Stand
    	buttonStand.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
    		buttonStand.setEffect(null);
    		buttonStand.setScaleX(1);
    		buttonStand.setScaleY(1);
    		buttonStandrotation.pause();
    		});
        	
    	// Rotation effect - Button Hit
    	RotateTransition buttonHitRotation = 
    			new RotateTransition(Duration.seconds(0.5), buttonHit);
    		buttonHitRotation.setCycleCount(Animation.INDEFINITE);
    		buttonHitRotation.setFromAngle(0);
    		buttonHitRotation.setToAngle(5);
    		buttonHitRotation.setAutoReverse(true);
    		buttonHitRotation.setCycleCount(100);
    		//buttonHitRotation.setAxis(new Point3D(1, 2, 3));
    	
    	// Effect on Hover - Button Hit
        buttonHit.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
        	buttonHit.setEffect(dropShadow);
        	buttonHit.setScaleX(1.5);
        	buttonHit.setScaleY(1.5);
        	buttonHitRotation.play();
    		});
    	
    	// Removes shadow effect - Button Hit
        buttonHit.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
        	buttonHit.setEffect(null);
        	buttonHit.setScaleX(1);
        	buttonHit.setScaleY(1);
        	buttonHitRotation.pause();
        		
			});
    		
		// Effect on Hover - Button Play
    	buttonPlay.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
    		buttonPlay.setEffect(dropShadow);
    		buttonPlay.setScaleX(1.5);
    		buttonPlay.setScaleY(1.5);
    		});
    	
    	// Removes shadow effect - Button Play
    	buttonPlay.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
    		buttonPlay.setEffect(null);
    		buttonPlay.setScaleX(1);
    		buttonPlay.setScaleY(1);
    		
    	});
    
    /*
     *		BorderPane
     */
    	
        //Top Menu (header)
        HBox headerHBox = new HBox();
        	Label headerText = new Label("JavaFx BlackJack Gui!");
        	headerHBox.setId("headerHBox");
        	headerText.setId("headerText");
        	headerHBox.getChildren().add(headerText);
        
        //Left Menu
        VBox dealerVBox = new VBox();
        	dealerVBox.setPrefWidth(300);
        	dealerVBox.setAlignment(Pos.TOP_CENTER);
        	Label dealerText = new Label("Dealer: ");
        	dealerVBox.setId("dealerVBox");
        	dealerText.setId("dealerText");
        	dealerCardsHBox.setId("dealerCard");
        	dealerVBox.getChildren().addAll(dealerText, dealerCardsHBox);
        
        //Right Menu
        VBox playerVBox = new VBox();
        	playerVBox.setPrefWidth(400);
        	playerVBox.setAlignment(Pos.TOP_CENTER);
        	Label playerText = new Label("Player: ");
        	// Add new ImageView
        	// Set image to the ImageView
        	//Label labelImage = new Label("<-- Card", testpic);
        	Label playerHandsSize = new Label ("Player Hands Size: ");
        	playerVBox.setId("playerVBox");
        	playerText.setId("playerText");
        	playerScore.setId("playerScore");
        	playerCardsHBox.setId("playerCardsHBox");
        	playerHandsSize.setId("playerHandsSize");
        	playerVBox.getChildren().addAll(playerText, playerCardsHBox, playerScore);
        
        //Bottom Menu (Buttons)
        HBox bottomMenuHBox = new HBox();
	        bottomMenuHBox.setAlignment(Pos.CENTER);
	        bottomMenuHBox.setId("bottomMenuHBox");
	        bottomMenuHBox.getChildren().addAll(buttonStand, buttonHit, 
	        									buttonPlay, buttonRegister);
        
        //Fönstret
        BorderPane borderPane = new BorderPane();
        	borderPane.setTop(headerHBox);
        	borderPane.setLeft(dealerVBox);
        	borderPane.setRight(playerVBox);
        	borderPane.setBottom(bottomMenuHBox);
        		
        //Scene
        Scene scene = new Scene (borderPane, 1000, 700);
        
        // StyleSheets
        scene.getStylesheets().add(getClass().getResource
        			("application.css").toExternalForm());
        
        //Display scene
        window.setScene(scene);
        window.setTitle("BlackJack Gui");
        window.show();
        
    }
    
    private Card getRandomCard() {
  // TODO Auto-generated method stub
      
      int r = (int)(Math.random() * 13) ;
      int s = (int)(Math.random() * 4 ) ;  
      return new Card(Suite.values()[s]  ,  Rank.values()[r]) ;
    
}
    /**
     * Så länge en spelare inte har registrerat 
     * sig så kan man inte börja spela
     * @author PatrikWebb
     */
    private void registreraSpelare(){
    	buttonPlay.setDisable(false);
    	// TODO
    }
    
    /**
	 * När spelet är slut så kan Play knappen användas igen
	 * @author PatrikWebb
	 * 
	 */
	private void gameIsOff() {
		buttonPlay.setDisable(false);	
		// TODO
		
	}
	/**
	 * När spelet har börjat så kopplats Play knappen bort
	 * @author PatrikWebb
	 * 
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
	private void startNewGame() {
		playable.set(true);
		
		// TODO
		// Ska innehålla
			// Dela ut 2 kort vardera till Player och Dealer
		
	}
	private void endGame(){
		playable.set(false);
		// TODO
		// Ska innehålla
			// Kolla poängen bland Player och Dealer ständigt
			// Om någon är bust eller båda har BlackJack
			// Skriv ut vinnaren...
			// playable.(false);
			// gameIsOff();
	}

}
		// <-- Tips -->
		//
		// Välja antal CardShoes
		//		ChoiceBox CardShoeChoiceBox = 
		//			new ChoiceBox(FXCollections.observableArrayList(
		//		"One Card Shoe", 
		//		"Two Card Shoes", 
		//		"Three Card Shoes", 
		//		"Four Card Shoes")
		//);
