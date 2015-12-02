package kodaLoss;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Controller implements Initializable, IController {

	@FXML
	private Button buttonHit;

	@FXML
	private Button buttonStay;

	@FXML
	private Button buttonDeal;

	@FXML
	private Button buttonDouble;

	@FXML
	private Button buttonInsurence;

	@FXML
	private Button buttonSplit;

	@FXML
	private Label labelPlayerName1, labelPlayerName2;

	@FXML
	private HBox playerCard1, playerCard2;

	@FXML
	private HBox dealerCard;

	@FXML
	private TextField dealersHandScore;

	@FXML
	private TextField playersHandScore, playersHandScore2;

	@FXML
	private Label labelWinnerText;

	@FXML
	private TextField TextFieldRounds;

	@FXML
	private TextField TextFieldBetts;

	@FXML
	private TextField TextFieldRoundBett;
	
	@FXML
	private ComboBox<String> ruleBox;
	
	@FXML
	private ComboBox<String> ComboBoxRules;
	
	@FXML
	private String BlackJacktext;
	

	// reference to UserChoiceAdapter for players button-events
	private UserChoiceAdapter uca = UserChoiceAdapter.getInstance();

	// reference to Bank singleton-object
	private Bank bank;

	private boolean hideDealers2ndCard = true;

	/**
	 * Setter for dealersHandScore
	 * 
	 * @param score
	 */
	public void setdealersHandScore(String score) {

//		Platform.runLater(() -> {
			dealersHandScore.setText(score);
//		});
	}

	/**
	 * Setter for labelWinnerText
	 * 
	 * @param winnerText
	 */
	public void setlabelWinnerText(String winnerText) {

		Platform.runLater(() -> {
			labelWinnerText.setText(winnerText);
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// Register Controller on Bank
		bank = Bank.getInstance();
		Bank.registerController(this);

		initControls();

		initButtonEffects();

		disableAdvancedButton();
	}

	private void initControls() {

		// Field for player making bets
		TextFieldRoundBett.setOnAction(e -> uca.playerChoosesToLayHisBet());

		TextFieldRoundBett.setDisable(true);

		// double click on bet-field sends bet to bank!
		TextFieldRoundBett.setOnMousePressed(new EventHandler<MouseEvent>(){

      @Override
      public void handle(MouseEvent event) {
       
        if(event.getClickCount() >= 2){
          uca.playerChoosesToLayHisBet();
        } 
      }
		});
		
		ruleBox.setOnAction( e -> {
		  if (ruleBox.getSelectionModel() == null){
		    System.out.println("NO MODEL!");
		  } else {
		  bank.setRule( ruleBox.getSelectionModel().getSelectedItem() );
		  }
		});
		
		
		buttonSplit.setOnAction(e -> uca.playerChoosesToSplit());
		
		buttonStay.setOnAction(e -> uca.playerChoosesToStay());

		buttonHit.setOnAction(e -> uca.playerChoosesToHit());

		buttonDeal.setOnAction(e -> bank.playOneRound());
		
		buttonDouble.setOnAction(e -> uca.playerChoosesToDouble());
		
		buttonInsurence.setOnAction(e -> uca.playerChoosesToTakeInsurance());
	}
	/**
	 * Text in the Black Jack Rules ComboBox
	 * 
	 * 
	 */
	public void blackJackRulesText(){
		//TODO Byta från en metod och lägga i textdokument senare
		BlackJacktext = 
				
				  "Black Jack Rules:\n\n"
				
				+ "Black Jack är ett spel där man inte spelar mot \n"
				+ "andra spelare utan mot givaren (dealern). \n\n"
				
				+ "Målet är att få högre summa än givaren men \n"
				+ "inte mer än 21 för då förlorar man och på \n"
				+ "Black Jack språk blir man ”tjock”. \n\n"
				
				+ "Varje spelare har några val de kan göra varje omgång. \n\n"
				+ "=> [HIT]		  Man väljer att ta ett till kort.\n"
				+ "=> [STAY]	  Man väljer att stanna (dvs nöja sig med de kort man har). \n"
				+ "=> [DOUBLE] 	  Man väljer att dubbla sitt bett och får ett kort till, \n"
				+ "  			  sen blir det automatiskt som att man klickar på Stay. \n"
				+ "=> [SPLIT]	  Man väljer att splitta sin hand till 2 händer, \n"
				+ "  			  detta kan göras om man har 2 kort av samma värde. \n"
				+ "=> [INSURANCE] Man väljer att ta en försäkring som blir halva ens bett, \n"
				+ "  			  vinner du får du tillbaka ditt bett men dealern tar din \n"
				+ "  			  insurance. Vinner dealern så får du din insurance x2. \n"
				+ "  			  Detta kan göras om dealers har ett ESS på första hand. \n\n"
				
				+ "  			  Väljer spelaren att splitta får man, \n"
				+ "  			  två händer att spela på och försöka \n"
				+ "  			  komma så nära 21 som möjligt. ";
			
			ComboBoxRules.getItems().add(BlackJacktext);

	}
	
	/*
	 * Button Effects
	 */
	public void initButtonEffects() {

		// Shadow Effect on all buttons
		DropShadow dropShadow = new DropShadow();
		dropShadow.setRadius(3.0);
		dropShadow.setOffsetX(3.0);
		dropShadow.setOffsetY(2.0);
		dropShadow.setColor(Color.BLACK);

		// Rotation effect - Button Stand
		RotateTransition buttonStandrotation = new RotateTransition(Duration.seconds(0.5), buttonStay);
		buttonStandrotation.setCycleCount(Animation.INDEFINITE);
		buttonStandrotation.setFromAngle(0);
		buttonStandrotation.setToAngle(-5);
		buttonStandrotation.setAutoReverse(true);
		buttonStandrotation.setCycleCount(100);

		// Effect on Hover - Buttond Stand
		buttonStay.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
			buttonStay.setEffect(dropShadow);
			buttonStay.setScaleX(1.2);
			buttonStay.setScaleY(1.2);
			// buttonStandrotation.play();
		});

		// Removes shadow effect - Button Stand
		buttonStay.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
			buttonStay.setEffect(null);
			buttonStay.setScaleX(1);
			buttonStay.setScaleY(1);
			// buttonStandrotation.pause();
		});

		// Rotation effect - Button Hit
		RotateTransition buttonHitRotation = new RotateTransition(Duration.seconds(0.5), buttonHit);
		buttonHitRotation.setCycleCount(Animation.INDEFINITE);
		buttonHitRotation.setFromAngle(0);
		buttonHitRotation.setToAngle(5);
		buttonHitRotation.setAutoReverse(true);
		buttonHitRotation.setCycleCount(100);
		// buttonHitRotation.setAxis(new Point3D(1, 2, 3));

		// Effect on Hover - Button Hit
		buttonHit.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
			buttonHit.setEffect(dropShadow);
			buttonHit.setScaleX(1.2);
			buttonHit.setScaleY(1.2);
			// buttonHitRotation.play();
		});

		// Removes shadow effect - Button Hit
		buttonHit.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
			buttonHit.setEffect(null);
			buttonHit.setScaleX(1);
			buttonHit.setScaleY(1);
			// buttonHitRotation.pause();

		});

		// Effect on Hover - Button Play
		buttonDeal.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
			buttonDeal.setEffect(dropShadow);
			buttonDeal.setScaleX(1.2);
			buttonDeal.setScaleY(1.2);
		});

		// Removes shadow effect - Button Play
		buttonDeal.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
			buttonDeal.setEffect(null);
			buttonDeal.setScaleX(1);
			buttonDeal.setScaleY(1);

		});
	}

	/**
	 * activate and deactivates hiding the dealers second card
	 * 
	 * @param hide
	 */
	public void setHideDealersSecondCard(boolean hide) {
		this.hideDealers2ndCard = hide;
	}

	/**
	 * updates the Players variables in gui
	 * 
	 * @param activePlayerOnGui
	 *            - Player-object received from Bank
	 */
	public void updatePlayer(Player activePlayerOnGui) {

		Platform.runLater(() -> {

		  HBox target;
		  
		  if (!activePlayerOnGui.isSplitPlayer()){
		    target = playerCard1;
		    setPlayersHandScore(activePlayerOnGui , false);
		    labelPlayerName1.setText(activePlayerOnGui.getName()); // name label
		    TextFieldBetts.setText(activePlayerOnGui.getPlayersCash() + "");
		  
		  } else {
		    target = playerCard2;
		    setPlayersHandScore(activePlayerOnGui , true );
		  }
		  
			// Sets the playerCash in the TextField
			TextFieldRoundBett.setText(activePlayerOnGui.getPlayersBet() + "");
			setPics(activePlayerOnGui, target );
		});
	}

	// must be called within main application thread, or Platform.runLater()
	private void setPlayersHandScore(Player player , boolean isSplitPlayer) {
		String handValue;

		if (Bank_HelpersAndTools.isPlayersHandABlackJack(player)) {
			handValue = "BJ!";
		} else {
			handValue = Bank_HelpersAndTools.calculateValueOfPlayersHand(player) + "";
		}
		
		Platform.runLater(() -> {
		if (isSplitPlayer){
		  playersHandScore2.setText(handValue);
		} else{
		  playersHandScore.setText(handValue);
		}
	});
	}
	
	
	/**
	 * updates the Dealers variables in GUI
	 * 
	 * @param dealer
	 *            - Player-object received from Bank
	 */

	public void updateDealer(Player dealer) {
		Platform.runLater(new Runnable() {

			public void run() {
			  
				setPics(dealer, dealerCard);

				String handValue = Bank_HelpersAndTools.isPlayersHandABlackJack(dealer) ? "BJ!"
						: Bank_HelpersAndTools.calculateValueOfPlayersHand(dealer) + "";

				if (!hideDealers2ndCard) {
					dealersHandScore.setText(handValue);
				}
			}
		});
	}


	/*
	 * adds a new request to update players hand i gui to the javafx application
	 * thread (via Platform.runLater()). Quite uneffectivly erases all card
	 * pictures first to then completly load and add pictures to hand again.
	 * Gets players hand directly from a Player object. HBOX TARGET parametern
	 * descides which HBox to update which makes this method usable for both
	 * players and dealers elements i GUI.
	 */
	private void setPics(Player player, HBox target) {
	  
		Platform.runLater(new Runnable() {
			public void run() {

				target.getChildren().clear();

				if (player != null) {
					List<Card> hand = player.getPlayersHand();

					for (int i = 0; i < hand.size(); i++) {
						Image image;

						try {
							String cardString = hand.get(i).toString();

							if (i > 0 && hideDealers2ndCard && target == dealerCard) {
								cardString = "BACKSIDE";
							}
							URL url = Controller.class.getResource("/CardPictures/" + cardString + ".png");

							image = new Image(url.toString() );
							ImageView view = new ImageView(image);
//							view.setScaleX(1.5);
//							view.setScaleY(1.5);
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
			buttonDeal.setDisable(true);

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
				buttonDeal.setDisable(false);
			}
		});
	}

	/**
	 * All buttons off, e.g. while dealer plays!
	 */
	public void allButtonsOff() {
		Platform.runLater(() -> {
			buttonHit.setDisable(true);
			buttonStay.setDisable(true);
			buttonDeal.setDisable(true);
		});
	}

	/**
	 * Bank opens players betting textfield for entering a bet
	 */
	public void activatePlayersBetField() {
		Platform.runLater(() -> {
			TextFieldRoundBett.setDisable(false);
		});
	}

	/**
	 * Activates the Insurance button
	 */
	public void activateInsuranceButton() {
		Platform.runLater(() -> {
			buttonInsurence.setVisible(true);
			buttonInsurence.setDisable(false);
		});
	}
	/**
	 * Activates the Double button
	 */
	public void activateDoubleButton() {
		Platform.runLater(() -> {
			buttonDouble.setVisible(true);
			buttonDouble.setDisable(false);
		});
	}

	/**
	 * Disable the Insurance, Double and split button <Br />
	 * Use on start up so you dont need <Br />
	 * to se them all the time
	 */
	@Override
	public void disableAdvancedButton() {
		Platform.runLater(() -> {
			buttonInsurence.setVisible(false);
			buttonInsurence.setDisable(true);

			buttonDouble.setVisible(false);
			buttonDouble.setDisable(true);
			// Undo after testing
			 buttonSplit.setVisible(false);
			 buttonSplit.setDisable(true);
		});
	}

	/**
	 * get the bet Player has entered and deactivate textField after that!
	 */
	public int getBetFromPlayersTextField() {
		Integer bet = null;

		do {
			try {

				bet = Integer.parseInt(TextFieldRoundBett.getText());

				if (bet < 0) {
					bet = new Integer(Math.abs(bet));
				}
				break;
			} catch (Exception e) {
			}
		} while (bet == null);

		Platform.runLater(() -> {
			TextFieldRoundBett.setDisable(true);
		});

		return bet;
	}

	
	
	/**
	 * Puts the Casino rules buttons (Split, Double, Insurance)
	 * on the game field in a deactivated state!
	 */
  @Override
  public void activateAdvancedButton() {
    
      Platform.runLater(() -> {
        buttonInsurence.setVisible(true);
        buttonInsurence.setDisable(true);

        buttonDouble.setVisible(true);
        buttonDouble.setDisable(true);
        // Undo after testing
         buttonSplit.setVisible(true);
         buttonSplit.setDisable(true);
      });
    }

  /**
   * sets the list model of selectable rules from the bank in the 
   * rules-combo box 
   */
  @Override
  public void setPlayableRules(Set<String> ruleNames) {
    Platform.runLater( () -> 
      ruleBox.setItems(FXCollections.observableArrayList(ruleNames))
    );
  }

  @Override
  public void activateSplitButton() {
    Platform.runLater(() -> {
      buttonSplit.setDisable(false);
    });
    
  }
  

}
