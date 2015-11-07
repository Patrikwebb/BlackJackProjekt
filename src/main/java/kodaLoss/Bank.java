package kodaLoss;

import java.util.ArrayList;
import java.util.List;

import static kodaLoss.UserChoiceAdapter.*;

import static kodaLoss.Bank_HelpersAndTools.*;

public class Bank {

  // MEMBERS

  /* Reference to UserChoiceAdapter just because its easier to write 'uca' than 
   * 'UserChoiceAdapter.getInstance().xxx' all the time
   */
  private static UserChoiceAdapter uca = UserChoiceAdapter.getInstance();

  // Singleton object of Bank
  private static Bank bank = new Bank();

  // Banks reference to the Controller object
  private static Controller controller;

  public List<Player> registeredPlayers = new ArrayList<Player>();
  
  // 4 Decks of shuffled cards  
  private CardShoe cardShoe = new CardShoe();

  // a round plays in its own thread for GUI-responsivity
  private Thread roundThread = null;
  
  /* reference to the Player objects of dealer and the active player 
   * to be shown on the GUI. Needed for updating the GUI. 
  */
  public Player dealer;
  private Player activePlayerOnGui;

  
  // Default value for dealer & playersHandScore
  String dealerscore = "0";
  String playerscore = "0";
  
  // CONSTRUCTORS
  private Bank() {

    // DEMO VERSION
    System.out.println("Bank started in a testMode for Demo!!!");

    registeredPlayers.add(new Player("TESTPLAYER"));
    activePlayerOnGui = registeredPlayers.get(0);
    dealer = new Player("Dealer");
    System.out.println("TestPlayer and Dealer added...");
    System.out.println("Number of Players: " + registeredPlayers.size());
  }

  /**
   * Returns a reference to the one and only Bank object
   * @return reference to Bank-object singleton
   */
  public static Bank getInstance() {
    return bank;
  }

  // METHODS TO REGISTER CONTROLLER
/**
 * Adds a reference to the controller class to the Bank object. Needed for 
 * Bank to be able to request changes in GUI. 
 * @param control - a controller object
 */
  public static void registerController(Controller control) {

    controller = control;
    controller.test();
  }

  
  // METHODS FOR GUI

  /*
   * called after changes in player or dealer model. Updates the GUI by directly
   * calling methods in Controller. Bank decides this way when GUI should be changed
   */
  private void updateGuiAfterChangeInDataModel() {
   
    if (controller != null) {
      controller.updatePlayer(activePlayerOnGui);
      controller.updateDealer(dealer);
    } else
      System.out.println("ERROR: Controller not set in bank!");
  }

  /**
   * This method controls the sequence of actions to play one round, control of
   * the gameplay! Has to run in its own thread because method blocks while 
   * waiting for user input! 
   */
  public void playOneRound() {

    //TODO is there a better solution than userChoiceAdapter for user inputs? blocking?
    
    if (  roundThread == null ||  !roundThread.isAlive()) {
      
      roundThread = new Thread(new Runnable() {

        @Override
        public void run() {

          System.out.println("Round started...");
          

          // clear last hand from clean Table!
          for (Player p : registeredPlayers) {
            p.clearPlayersHand();
          }
          dealer.clearPlayersHand();
          
          // buttons off while dealing cards
          controller.allButtonsOff();

          // deal a card to all players and dealer
          dealOneCardToAll();
          updateGuiAfterChangeInDataModel();

          
          BlackJackConstantsAndTools.sleepFor1Second();
          
          // deal second card and hide dealers second card
          controller.setHideDealersSecondCard(true);
          dealOneCardToAll();
          updateGuiAfterChangeInDataModel();

          // TODO dealers other card to gui shall be covered!
          
          // Set Winner Text Label empty
          controller.setlabelWinnerText("");

          // Get the dealer and players handscore in a toString metod
          controller.setdealersHandScore("");
          controller.setplayersHandScore(playersHandScore(playerscore));
          
          // System out Dealer score
          System.out.println("Dealer score " + 
        		  Bank_HelpersAndTools.calculateValueOfPlayersHand(dealer));
          // System out Player score
          System.out.println("Player score " + 
        		  Bank_HelpersAndTools.calculateValueOfPlayersHand(activePlayerOnGui));
          
          
          // check if a player has a BlackJack from start!
          for (Player p : registeredPlayers) {

            // if (isPlayersHandABlackJack(p)) {
            // p.setPlayerActiveInRound(false);
            // } else{
            // p.setPlayerActiveInRound(true);
            // }

            // tänkte visa nåt snyggt!
            boolean active = isPlayerBust(p) ? true : false;
            p.setPlayerActiveInRound(active);
          }

          // each player without a Blackjack plays against bank
          for (Player p : registeredPlayers) {

            if (!isPlayersHandABlackJack(p)) {
              // TODO HIT, STAY = enable PLAY = disable
               controller.gameIson();
              playerPlays(p);
            } else {
              // TODO inform player about BlackJack!!!
            }
          }

          // dealer plays after one second break
          // deactivate all buttons while dealer plays
          controller.allButtonsOff();
          BlackJackConstantsAndTools.sleepFor1Second();
          dealerPlays();

          /*
           * 
           * Checks if dealer and players isn't bust, Checks the higher hand and
           * give us the winner
           * 
           * If dealer is bust => all players that isn't bust win
           */
          controller.setdealersHandScore(dealersHandScore(dealerscore));
          controller.setplayersHandScore(playersHandScore(playerscore));
          
          calculateWinners();

          // TODO HIT, STAY = disable PLAY = enable
          controller.gameIsoff();
          // reset Thread and Players for next round!
          // roundThread = null;
        }
      });
      roundThread.start();

    } else {
      // roundThread is still alive!
      System.out.println("Already running a round");
      controller.gameIson();
    }

  }

  /*
   * Player plays against Bank in one round. Sets player inactive if bust.
   * 
   * Uses class UserChoiceAdapter to get user events from the user interface
   */
  private void playerPlays(Player player) {
    // TODO alternative to UserCHoiceAdapter???
    System.out.println("Player plays started...");
    
    // activate players buttons
    controller.gameIson();

    uca.resetUserChoice(); // prepare for input

    while (uca.getUserChoice() != UserChoice.STAY) {

      // has to be run!?!? Why is that?
      // TODO Why does whileLoop not run without some statement here?
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      if (uca.getUserChoice() == UserChoice.HIT) {
    	
        dealOneCardToPlayer(player);
        
        controller.setplayersHandScore(playersHandScore(playerscore));
        updateGuiAfterChangeInDataModel();

        System.out.println("PLAYER HIT");
        player.printHandToConsole();
        uca.resetUserChoice();

        if (isPlayersHandOver21(player)) {
          System.out.println("PLAYER IS BUST!");
          controller.setlabelWinnerText("PLAYER IS BUST!");
          updateGuiAfterChangeInDataModel();
          break;
        }
      }
    }
    // finally reset last choice
    uca.resetUserChoice();
  }

  /*
   * dealer plays. Takes cards until its hand is over 16
   */
  protected void dealerPlays() {
    
    try {
      // show dealers second card
      controller.setHideDealersSecondCard(false);
      updateGuiAfterChangeInDataModel();

      // take cards while hand less than 17
      while (calculateValueOfPlayersHand(dealer) < 17) {


      if (isPlayersHandOver21(dealer)) {
        // temporary until we send to GUI
        System.out.println("DEALER IS BUST!");
        controller.setlabelWinnerText("DEALER IS BUST!");
        setPlayerToBust(dealer, true);
      	}
        Thread.sleep(2000);
        controller.setdealersHandScore(dealersHandScore(dealerscore));

        dealOneCardToPlayer(dealer);

        // update gui now
        updateGuiAfterChangeInDataModel();

        setPlayerToBust(dealer, false);

        if (isPlayersHandOver21(dealer)) {
          // temporary until we send to GUI
          System.out.println("DEALER IS BUST!");
          setPlayerToBust(dealer, true);
          updateGuiAfterChangeInDataModel();

        } else {
          // temporary until we send to GUI
          System.out
              .println("DEALER has " + calculateValueOfPlayersHand(dealer));
        }
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /*
   * deals one card to all players and dealer
   */
  protected void dealOneCardToAll() {

    for (Player p : registeredPlayers) {
      dealOneCardToPlayer(p);
    }
    dealOneCardToPlayer(dealer);
  }

  /*
   * Calculate winners
   */
  // TODO update GUI who won
  // TODO update Player CASH i annan metod => returnTyp måste ändras!
  public void calculateWinners() {
    int playerpoints;

    if (isPlayerBust(dealer)) {

      for (Player p : registeredPlayers) {
        playerpoints = calculateValueOfPlayersHand(p);

        if (playerpoints <= 21) {
          // TO GUI Player WINNS
          System.out.println("Congratulations! You won.");
          controller.setlabelWinnerText("Congratulations! You won.");
          p.setRoundResult(RoundResult.WIN);
        } else {
          // To Gui YOU lost!
          System.out.println("Sorry, you lost.");
          controller.setlabelWinnerText("Sorry, you lost.");
          p.setRoundResult(RoundResult.LOOSE);

        }
      }
    } else {

      for (Player p : registeredPlayers) {
        playerpoints = calculateValueOfPlayersHand(p);

        if (playerpoints <= 21
            && playerpoints > calculateValueOfPlayersHand(dealer)) {
          // TO GUI Player WINNS
          System.out.println("Congratulations! You won.");
          controller.setlabelWinnerText("Congratulations! You won.");
          p.setRoundResult(RoundResult.WIN);

        } else {
          // To Gui YOU lost!
          System.out.println("Sorry, you lost.");
          controller.setlabelWinnerText("Sorry, you lost.");
          p.setRoundResult(RoundResult.LOOSE);
        }
      }
    }
  }

  // NON STATIC HELPER FUNCTIONS

  /*
   * bank deals one card from the card shoe to a player who takes the card and
   * adds it to his hand. (Then the gui is updated for the player)
   */
  private void dealOneCardToPlayer(Player player) {
    player.addCardToHand(cardShoe.getACardFromCardShoe());
    // TODO call gui to update players hand!
  }

  /*
   * Method to reset Bank to a default start state. Clears all players and
   * dealer, gets a new Card Shoe.
   */
  public void clearAllPlayersAndDealer() {
    registeredPlayers.clear();
    dealer = null;
    cardShoe = new CardShoe();

  }

  /**
   * Reads the Bank_HelpersAndTools.calculateValueOfPlayersHand metod </br >
   * and converts it from an int to String
   * @return dealerscore
   */
  public String dealersHandScore(String dealerscore){
	  
	int input = Bank_HelpersAndTools.calculateValueOfPlayersHand(dealer);
	dealerscore = Integer.toString(input);
	return dealerscore;
	  
  }
  /**
   * Reads the Bank_HelpersAndTools.calculateValueOfPlayersHand metod </br >
   * and converts it from an int to String
   * @return playerscore
   */
  public String playersHandScore(String playerscore){
	  
		int input = Bank_HelpersAndTools.calculateValueOfPlayersHand(activePlayerOnGui);
		playerscore = Integer.toString(input);
		return playerscore;
		  
	  }
}
