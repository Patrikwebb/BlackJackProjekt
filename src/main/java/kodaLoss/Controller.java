package kodaLoss;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Controller implements Initializable {

  @FXML
  private Button buttonHit;

  @FXML
  private Button buttonStay;

  @FXML
  private Button buttonPlay;

  @FXML
  private Label labelPlayerName1, labelPlayerName2;

  @FXML
  private HBox playerCard1, playerCard2;

  @FXML
  private HBox dealerCard;
  
  @FXML
  private TextField dealersHandScore;

  @FXML
  private TextField playersHandScore;
  
  @FXML
  private Label labelWinnerText;
  
  @FXML
  private TextField TextFieldRounds;

  @FXML
  private TextField TextFieldBetts;

  // reference to UserChoiceAdapter for players button-events
  private UserChoiceAdapter uca = UserChoiceAdapter.getInstance();

  // reference to Bank singleton-object
  private Bank bank;
  
  private boolean hideDealers2ndCard = true;
  
  /**
   * Setter for playersHandScore
   * @param score
   */
  public void setplayersHandScore(String score){
	  
	  Platform.runLater(() -> { 
	  playersHandScore.setText(score);
	  });
  }
  
  /**
   * Setter for dealersHandScore
   * @param score
   */
  public void setdealersHandScore(String score){
	  
	  Platform.runLater(() -> { 
	  dealersHandScore.setText(score);
	  });
  }
  
  /**
   * Setter for labelWinnerText
   * @param winnerText
   */
  public void setlabelWinnerText(String winnerText){
	  
	  Platform.runLater(() -> { 
	  labelWinnerText.setText(winnerText);
	  });
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    // Register Controller on Bank
    bank = Bank.getInstance();
    Bank.registerController(this);

    buttonStay.setOnAction(e -> uca.playerChoosesToStay());

    buttonHit.setOnAction(e -> uca.playerChoosesToHit());
    
    buttonPlay.setOnAction(e -> bank.playOneRound());

   initButtonEffects(); 
  }
    
    
    /*
     * Button Effects
     */
    public void initButtonEffects(){
    
    
    // Shadow Effect on all buttons
    DropShadow dropShadow = new DropShadow();
    dropShadow.setRadius(3.0);
    dropShadow.setOffsetX(3.0);
    dropShadow.setOffsetY(2.0);
    dropShadow.setColor(Color.BLACK);

    // Rotation effect - Button Stand
    RotateTransition buttonStandrotation = new RotateTransition(
        Duration.seconds(0.5), buttonStay);
    buttonStandrotation.setCycleCount(Animation.INDEFINITE);
    buttonStandrotation.setFromAngle(0);
    buttonStandrotation.setToAngle(-5);
    buttonStandrotation.setAutoReverse(true);
    buttonStandrotation.setCycleCount(100);

    // Effect on Hover - Buttond Stand
    buttonStay.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
      buttonStay.setEffect(dropShadow);
      buttonStay.setScaleX(1.5);
      buttonStay.setScaleY(1.5);
      buttonStandrotation.play();
    });

    // Removes shadow effect - Button Stand
    buttonStay.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
      buttonStay.setEffect(null);
      buttonStay.setScaleX(1);
      buttonStay.setScaleY(1);
      buttonStandrotation.pause();
    });

    // Rotation effect - Button Hit
    RotateTransition buttonHitRotation = new RotateTransition(
        Duration.seconds(0.5), buttonHit);
    buttonHitRotation.setCycleCount(Animation.INDEFINITE);
    buttonHitRotation.setFromAngle(0);
    buttonHitRotation.setToAngle(5);
    buttonHitRotation.setAutoReverse(true);
    buttonHitRotation.setCycleCount(100);
    // buttonHitRotation.setAxis(new Point3D(1, 2, 3));

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
  }
    

    /** 
     * activate and deactivates hiding the dealers second card
     * @param hide
     */
    public void setHideDealersSecondCard(boolean hide){
      this.hideDealers2ndCard = hide;
    }
    
    
  /**
   * updates the Players variables in gui
   * 
   * @param activePlayerOnGui
   *          - Player-object received from Bank
   */
  public void updatePlayer(Player activePlayerOnGui) {

    Platform.runLater(new Runnable() {

      public void run() {
        String handValue= Bank_HelpersAndTools.isPlayersHandABlackJack(activePlayerOnGui) ? 
            "BJ!" : Bank_HelpersAndTools.calculateValueOfPlayersHand(activePlayerOnGui) + "";
        playersHandScore.setText(handValue);
        labelPlayerName1.setText(activePlayerOnGui.getName() ); // name label
        setPics(activePlayerOnGui, playerCard1);
      }
    });
  }

  /**
   * updates the Dealers variables in GUI
   * 
   * @param dealer
   *          - Player-object received from Bank
   */

  public void updateDealer(Player dealer) {
    Platform.runLater(new Runnable() {

      public void run() {
        setPics(dealer, dealerCard);
        
        String handValue= Bank_HelpersAndTools.isPlayersHandABlackJack(dealer) ? 
            "BJ!" : Bank_HelpersAndTools.calculateValueOfPlayersHand(dealer) + "";
        
        if (!hideDealers2ndCard){
          dealersHandScore.setText( handValue );
        }
      }
    });
  }

  /*
   * adds a new request to update players hand i gui to the javafx application
   * thread (via Platform.runLater()). Quite uneffectivly erases all card
   * pictures first to then completly load and add pictures to hand again. Gets
   * players hand directly from a Player object. HBOX TARGET parametern descides
   * which HBox to update which makes this method usable for both players and
   * dealers elements i GUI.
   */
  private void setPics(Player player, HBox target) {

    Platform.runLater(new Runnable() {
      public void run() {

        target.getChildren().clear();

        if (player != null) {
          List<Card> hand = player.getPlayersHand();

          for (int i = 0 ; i < hand.size() ; i++) {
            Image image;

            try {
              String cardString = hand.get(i).toString();
              
              if (i > 0 && hideDealers2ndCard && target == dealerCard){
                cardString = "BACKSIDE";
              }
              String pathToCardPicture = BlackJackConstantsAndTools
                  .getURLStringToFileInCardPictures(cardString);
              image = new Image(pathToCardPicture);
              ImageView view = new ImageView(image);
              target.getChildren().add(view);
              target.setSpacing(-45);
              
            } catch (Exception e) {
              System.out.println("Controller.setPics: Cannot load picture!");
            }
          }
        } else {
          System.out.println("listData null!");
        }
      }
    });
  }

  /// JUST A TEST

  public void test() {
    System.out.println("CONTROLLER TEST: Controller says hi!");

  }

  /**
   * <p>
   * Sets the default value on the buttons when the game is off.
   * </p>
   * <b>HIT</b>, <b>STAY</b> = enable <b>PLAY</b> = disable
   */
  public void gameIson() {

    Platform.runLater(() -> {
      buttonHit.setDisable(false);
      buttonStay.setDisable(false);
      buttonPlay.setDisable(true);
    });
  }

  /**
   * <p>
   * Sets the default value on the buttons when the game is off.
   * </p>
   * HIT, STAY = disable PLAY = enable
   */
  public void gameIsoff() {
    Platform.runLater(new Runnable() {
      public void run() {
        buttonHit.setDisable(true);
        buttonStay.setDisable(true);
        buttonPlay.setDisable(false);
      }
    });
  }
  
  /**
   * All buttons off, e.g. while dealer plays!
   */ 
  public void allButtonsOff(){
    Platform.runLater(() -> {
      buttonHit.setDisable(true);
      buttonStay.setDisable(true);
      buttonPlay.setDisable(true);
    });
  }
  
  
  
  
}
