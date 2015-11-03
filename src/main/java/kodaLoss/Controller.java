package kodaLoss;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class Controller implements Initializable {

  @FXML
  private Button buttonHit;

  @FXML
  private Button buttonStay;

  @FXML
  private Button buttonPlay;

  @FXML
  private Label labelPlayerName;

  @FXML
  private HBox playerCard;

  @FXML
  private HBox dealerCard;

  private UserChoiceAdapter uca = UserChoiceAdapter.getInstance();

  private Bank bank;
  
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {


    // Register Controller on Bank
    bank = Bank.getInstance();
    Bank.registerController(this);

    buttonStay.setOnAction( e -> uca.playerChoosesToStay());
      
    buttonHit.setOnAction( e ->  uca.playerChoosesToHit());
    
    buttonPlay.setOnAction( e -> bank.playOneRound());
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

        labelPlayerName.setText(activePlayerOnGui.getName()); // name label
        setPics(activePlayerOnGui, playerCard);
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

          for (Card card : hand) {
            Image image;
            
            try {
              String cardString = card.toString();
              String pathToCardPicture = BlackJackConstantsAndTools
                  .getURLStringToFileInCardPictures(cardString);
              image = new Image(pathToCardPicture);
              ImageView view = new ImageView(image);
              target.getChildren().add(view);
              target.setSpacing(-45);

            } catch (Exception e) {
              System.out.println("pic Funkade inte");
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

}