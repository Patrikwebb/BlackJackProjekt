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

//    /**
//     * Referens till player
//     * @return player
//     */
//    private Player player = new Player(this);
  
   
    private Label 		playerScore;
    private Image		image1;
    private ImageView 	cardPicture; 
    private Button 		buttonHit, buttonStand, buttonPlay;
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
        			UserChoiceAdapter.playerChoosesToStay();
        	});
        	// Rotation effect
        	RotateTransition buttonStandrotation = 
        			new RotateTransition(Duration.seconds(0.5), buttonStand);
        	buttonStandrotation.setCycleCount(Animation.INDEFINITE);
        	buttonStandrotation.setFromAngle(0);
        	buttonStandrotation.setToAngle(-5);
        	buttonStandrotation.setAutoReverse(true);
        	buttonStandrotation.setCycleCount(100);
        	
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
        	  UserChoiceAdapter.playerChoosesToHit();
        	  
        	  setTestPic( getRandomCard() );
        	  
        	  // Add the Image to player HBox
        	  playerCardsHBox.getChildren().add(cardPicture);
        	  
          	
        	});
        	
    	// Rotation effect
    	RotateTransition buttonHitRotation = 
    			new RotateTransition(Duration.seconds(0.5), buttonHit);
    	buttonHitRotation.setCycleCount(Animation.INDEFINITE);
    	buttonHitRotation.setFromAngle(0);
    	buttonHitRotation.setToAngle(5);
    	buttonHitRotation.setAutoReverse(true);
    	buttonHitRotation.setCycleCount(100);
    	//buttonHitRotation.setAxis(new Point3D(1, 2, 3));
    	
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
        	buttonHitRotation.pause();
        		
			});
        // Button Play
        buttonPlay = new Button ("Play");
    		buttonPlay.setOnAction(event -> {
    		
    		startNewGame();
    		
    		gameIsOn();
    		
    		//playerCards.getChildren().add(cardPicture);
    		
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
        	playerVBox.setPrefWidth(300);
        	playerVBox.setAlignment(Pos.TOP_CENTER);
        	Label playerText = new Label("Player: Dude");
        	playerScore = new Label("");
        	// Add new ImageView
        	// Set image to the ImageView
        	//Label labelImage = new Label("<-- Card", testpic);
        	Label playerHandsSize = new Label ("Player Hands Size: ");
        	playerVBox.setId("playerVBox");
        	playerText.setId("playerText");
        	playerScore.setId("playerScore");
        	playerCardsHBox.setId("playerCardsHBox");
        	playerHandsSize.setId("playerHandsSize");
        	playerVBox.getChildren().addAll(playerText, playerScore, playerCardsHBox);
        
        //Bottom Menu (Buttons)
        HBox bottomMenuHBox = new HBox();
	        bottomMenuHBox.setAlignment(Pos.CENTER);
	        bottomMenuHBox.setId("bottomMenuHBox");
	        bottomMenuHBox.getChildren().addAll(buttonStand, buttonHit, buttonPlay);
        
        //Fönstret
        BorderPane borderPane = new BorderPane();
        	borderPane.setTop(headerHBox);
        	borderPane.setLeft(dealerVBox);
        	borderPane.setRight(playerVBox);
        	borderPane.setBottom(bottomMenuHBox);
        		
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
    private Card getRandomCard() {
  // TODO Auto-generated method stub
      
      int r = (int)(Math.random() * 13) ;
      int s = (int)(Math.random() * 4 ) ;  
      return new Card(Suite.values()[s]  ,  Rank.values()[r]) ;
    
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
	private void startNewGame() {
		playable.set(true);
		
		// TODO
		// Dela ut 2 kort vardera till Player och Dealer
	}
	private void endGame(){
		playable.set(false);
		// TODO
		// Kolla poängen bland Player och Dealer ständigt
		// Om någon är bust eller båda har BlackJack
		// Skriv ut vinnaren...
		// playable = false;
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
