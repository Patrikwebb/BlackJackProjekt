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
          
          //TODO HIT, STAY = enable		PLAY = disable
          controller.gameIson();
          
          // clear last hand from clean Table!
          dealer.clearPlayersHand();
          for (Player p : registeredPlayers){
            p.clearPlayersHand();
          }
          
          // deal a card to all players and dealer
          dealOneCardToAll();
          updateGuiAfterChangeInDataModel();

          dealOneCardToAll();
          updateGuiAfterChangeInDataModel();
          // TODO dealers other card to gui shall be covered!

          // check if a player has a BlackJack from start!
          for (Player p : registeredPlayers) {

            if (isPlayersHandABlackJack(p)) {
              p.setPlayerActiveInRound(false);
            }
          }

          // each active player plays against bank
          // FOR DEMO: ALL PLAYERS ARE ACTIVE!
          System.out.println(activePlayerOnGui.isActive());
          for (Player p : registeredPlayers) {

//            if (p.isActive()) {
              playerPlays(p);
//            }
          }

          // dealer plays
          dealerPlays();

          /*
           * 
           * Checks if dealer and players isn't bust,
           * Checks the higher hand and give us the winner 
           * 
           * If dealer is bust => all players that isn't bust win
           */
          calculateWinners();
          
          //TODO HIT, STAY = disable		PLAY = enable
          controller.gameIsoff();
          // reset Thread and Players for next round! 
//          roundThread = null;
        }
      });
      roundThread.start();
      
    } else {
      // roundThread is still alive!
      System.out.println("Already running a round");
    }
    
  }

  /*
   * Player plays against Bank in one round. 
   * Sets player inactive if bust. 
   * 
   * Uses class UserChoiceAdapter to get user events from the user interface
   */
  private  void playerPlays( Player player ) {
    
    // TODO alternative to UserCHoiceAdapter???
    System.out.println("Player plays started...");
    
    uca.resetUserChoice(); // prepare for input
    
    while (uca.getUserChoice() != UserChoice.STAY) {
      
      // has to be run!?!? Why is that? 
      // TODO Why does whileLoop not run without some statement here?
      System.out.print("");
      
      
      if (uca.getUserChoice() == UserChoice.HIT) {
        dealOneCardToPlayer(player);
       
        updateGuiAfterChangeInDataModel();

        System.out.println("PLAYER HIT");
        player.printHandToConsole();
        uca.resetUserChoice();

        if (isPlayersHandOver21(player)) {
          player.printHandToConsole();
          System.out.println("PLAYER IS BUST!");
          updateGuiAfterChangeInDataModel();
          player.setPlayerActiveInRound(false);
          player.setBusted(true);
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
    
    while (calculateValueOfPlayersHand(dealer) < 17) {
      
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
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
        System.out.println("DEALER has " + calculateValueOfPlayersHand(dealer));
      }
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
    // main.ShowDealersHand(dealer.getPlayersHand());
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
          p.setRoundResult(RoundResult.WIN);
        } else {
          // To Gui YOU lost!
          System.out.println("Sorry, you lost.");
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
          p.setRoundResult(RoundResult.WIN);

        } else {
          // To Gui YOU lost!
          System.out.println("Sorry, you lost.");
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
   * Method to reset Bank to a default start state. 
   * Clears all players and dealer, gets a new Card Shoe.  
   */
  public void clearAllPlayersAndDealer() {
    registeredPlayers.clear();
    dealer = null;
    cardShoe = new CardShoe();
    
  }
  
}
