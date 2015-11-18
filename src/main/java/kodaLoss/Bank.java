package kodaLoss;

import java.util.ArrayList;
import java.util.List;

import static kodaLoss.UserChoiceAdapter.*;

import static kodaLoss.BlackJackConstantsAndTools.*;

import static kodaLoss.RoundResult.*;

import static kodaLoss.Bank_HelpersAndTools.*;

public class Bank {

  // MEMBERS Patrik, Tim, Johannes , Johan

  /*
   * Reference to UserChoiceAdapter just because its easier to write 'uca' than
   * 'UserChoiceAdapter.getInstance().xxx' all the time
   */
  private static UserChoiceAdapter uca = UserChoiceAdapter.getInstance();

  // Singleton object of Bank
  private static Bank bank = new Bank();

  // Banks reference to the Controller object
  protected static IController controller = null;

  public List<Player> registeredPlayers = new ArrayList<Player>();

  // 4 Decks of shuffled cards
  private CardShoe cardShoe = new CardShoe();

  // a round plays in its own thread for GUI-responsivity
  private Thread roundThread = null;

  /*
   * reference to the Player objects of dealer and the active player to be shown
   * on the GUI. Needed for updating the GUI.
   */
  public Player dealer = new Player("Dealer", 0);
  private Player activePlayerOnGui;

  private int round = 0;

  // CONSTRUCTORS
  // private because of Singleton pattern
  private Bank() {
    System.out.println("Bank started...");
  }

  /**
   * Returns a reference to the one and only Bank object
   * 
   * @return reference to Bank-object singleton
   */
  public static Bank getInstance() {
    return bank;
  }

  // METHODS TO REGISTER CONTROLLER
  /**
   * Adds a reference to the controller class to the Bank object. Needed for
   * Bank to be able to request changes in GUI.
   * 
   * @param control
   *          - a controller object
   */
  public static void registerController(IController control) {

    controller = control;
  }

  // METHODS FOR GUI

  /*
   * called after changes in player or dealer model. Updates the GUI by directly
   * calling methods in Controller. Bank decides this way when GUI should be
   * changed
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

    // A ROUND CANNOT BE PLAYED WITHOUT A REGISTERED CONTROLLER!
    if (controller == null) {
      System.out.println("CONTROLLER has to be set!");
      throw new IllegalStateException("Controller has to be set! ");
    }

    // DO not start a new game, when a game is running!
    if (roundThread != null) {
      System.out.println("Already running a round");

    } else if (roundThread == null || !roundThread.isAlive()) {
      // start a new Round in its own Thread for not freezing the GUI!
      roundThread = new Thread(() -> {

        // Updaterar runda DIRTY TEST
        String roundString = Integer.toString(++round);
        controller.updateRound(roundString);

        // clear last hand from clean Table!
        clearHandsOffTheTable();

        // buttons off while dealing cards
        controller.allButtonsOff();

        // ask all players for bet before dealing!
        askPlayersForBetsForThisRound();

        BlackJackConstantsAndTools.sleepForXSeconds();

        // deal a card to all players and dealer
        dealOneCardToAll();

        BlackJackConstantsAndTools.sleepForXSeconds();

        // deal second card and hide dealers second card
        controller.setHideDealersSecondCard(true);
        dealOneCardToAll();

        // each player who does not have a Blackjack plays against bank
        allPlayersPlayAgainstTheDealer();

        BlackJackConstantsAndTools.sleepForXSeconds();

        // dealer plays
        dealerPlays();

        // get winners and inform active player
         calculateWinners();
//        roundOutcome();
         
        // handle the cash in pot and pay out players
        handlePlayersBetsAndPayWinners();
        
        // Check if player is game over
        // Sprint 3: => game terminates!
        checkForGameOver();

        BlackJackConstantsAndTools.sleepForXSeconds(2000);

        // reset Thread and Players for next round!
        resetBeforeNextRound();
      });
      roundThread.start();

    } else {
      System.out.println("WRONG STATE in ROUND THREAD !");
      throw new RuntimeException("Round Thread in an impossible state!");
    }
  }

  
  
  /*
   * Sprint 3: if single user is bankrupt the game terminates!
   */
  private void checkForGameOver() {
    
    if (activePlayerOnGui.getPlayersCash() < BlackJackConstantsAndTools.MIN_BET){
      controller.setlabelWinnerText("GAME OVER! Please get money and restart the game!");
      clearHandsOffTheTable();
      resetBank();
      BlackJackConstantsAndTools.sleepForXSeconds(10000);
      controller.setlabelWinnerText("Programme terminates...");
      BlackJackConstantsAndTools.sleepForXSeconds(2000);
      System.exit(0);
    }
    
  }

  /*
   * take all cards off the table (clear dealer and players hands and update
   * gui)
   */
  private void clearHandsOffTheTable() {

    for (Player p : registeredPlayers) {
      p.clearPlayersHand();
    }
    dealer.clearPlayersHand();
    updateGuiAfterChangeInDataModel();
  }

  // all players without a BlackJack play against the bank
  private void allPlayersPlayAgainstTheDealer() {

    for (Player p : registeredPlayers) {

      if (!isPlayersHandABlackJack(p)) {
        controller.gameIson();
        playerPlays(p);
        // TODO Change GUI to next Player!
      } else {
        // TODO inform player about BlackJack!!!
      }
      controller.allButtonsOff();
    }
  }

  /*
   * reset Bank before next round can start, inform GUI.
   */
  private void resetBeforeNextRound() {
    controller.setlabelWinnerText("");
    roundThread = null;
    controller.gameIsoff(); // for player to choose DEAL! DONT FORGET!
  }

  /*
   * method which blocks starting the dealing of cards until all players set
   * their bets
   */
  // TODO public just for testing!
  public void askPlayersForBetsForThisRound() {

    for (Player p : registeredPlayers) {

      // METHOD TO CHANGE ACTIVEPLAYERINGUI COMES HERE

      controller.activatePlayersBetField();

      controller.setlabelWinnerText(
          p.getName()  + BlackJackConstantsAndTools.ASK_FOR_BETS);

      int bet;

      while (true) {

        if (uca.getUserChoice() == UserChoice.LAY_BET) {
          bet = controller.getBetFromPlayersTextField();
          // switch off asking for a bet!
          controller.setlabelWinnerText("");
          break;
        }
        BlackJackConstantsAndTools.sleepForXSeconds(10);
      }
      
      bet = (int)Math.abs(bet);
      
      if(bet > MAX_BET){
    	  bet = MAX_BET;
    	  controller.setlabelWinnerText("Max bet is " + MAX_BET + " on this table!");
      }
      
      if(bet<BlackJackConstantsAndTools.MIN_BET){
    	  bet=BlackJackConstantsAndTools.MIN_BET;
    	  controller.setlabelWinnerText("Min bet is "+BlackJackConstantsAndTools.MIN_BET+" on this table!");
      }
      p.setPlayersBet(bet);
      updateGuiAfterChangeInDataModel();
    }
  }

  /*
   * Player plays against Bank in one round. Sets player inactive if bust.
   * 
   * Uses class UserChoiceAdapter to get user events from the user interface
   */
  protected void playerPlays(Player player) {

    System.out.println("Player plays started...");

    // activate players buttons
    controller.gameIson();

    uca.resetUserChoice(); // prepare UCA for input

    while (uca.getUserChoice() != UserChoice.STAY) {

      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      if (uca.getUserChoice() == UserChoice.HIT) {

        dealOneCardToPlayer(player);
        updateGuiAfterChangeInDataModel();

        System.out.println("PLAYER HIT");
        player.printHandToConsole();
        uca.resetUserChoice();

        if (isPlayersHandOver21(player)) {
          System.out.println(PLAYER_IS_BUST);
          controller.setlabelWinnerText(PLAYER_IS_BUST);
          updateGuiAfterChangeInDataModel();
          break;
        }
      }
    }
   System.out.println(player.toString());
    
    // finally reset last choice in UCA
    uca.resetUserChoice();
  }

  /*
   * dealer plays. Takes cards until its hand is over 16
   */
  protected void dealerPlays() {

    // to show dealers second card, set to false
    controller.setHideDealersSecondCard(false);
    updateGuiAfterChangeInDataModel();

    // take cards while hand less than 17
    while (calculateValueOfPlayersHand(dealer) < 17) {

      if (isPlayersHandOver21(dealer)) {

        System.out.println(DEALER_IS_BUST);
        controller.setlabelWinnerText(DEALER_IS_BUST);
      }

      BlackJackConstantsAndTools.sleepForXSeconds(2000);
      dealOneCardToPlayer(dealer);
      updateGuiAfterChangeInDataModel();

      if (isPlayersHandOver21(dealer)) {
        // temporary until we send to GUI
        System.out.println(DEALER_IS_BUST);
        updateGuiAfterChangeInDataModel();

      } else {
        // temporary until we send to GUI
        System.out.println("Dealer has " + calculateValueOfPlayersHand(dealer));
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
    updateGuiAfterChangeInDataModel();
  }

  /*
   * bank deals one card from the card shoe to a player who takes the card and
   * adds it to his hand. (Then the gui is updated for the player)
   */
  private void dealOneCardToPlayer(Player player) {
    player.addCardToHand(cardShoe.getACardFromCardShoe());
  }

  /**
   * Calculate winners
   */
  public void calculateWinners() {
    /*
     * calculates and sets the RoundResult for every possible outcome of the
     * game between the bank and the player!
     */

    // Outcomes as ordered in Method: 
    // PLAYER BJ    - Dealer BJ => TIE
    //              - Dealer less => WIN
    //
    // Player BUST  - DEALER ? => LOOSE
    // 
    // Player value - Dealer BUST=> WIN
    //              - Dealer BJ => LOOSE
    //              = Dealer value => TIE
    //              < Dealer value => LOOSE
    //              > Dealer value => WIN
    

    int playerpoints;
    String winnerText = null;

    for (Player player : registeredPlayers) {
      playerpoints = calculateValueOfPlayersHand(player);

      // player has a BJ
      if (isPlayersHandABlackJack(player)) {

        if (isPlayersHandABlackJack(dealer)) {
          winnerText = setAndShowResults(player, TIE, winnerText);
        
        } else {
          winnerText = setAndShowResults(player, WIN, winnerText);
        }
      // player is bust
      } else if (isPlayersHandOver21(player)) {
        winnerText = setAndShowResults(player, LOOSE, winnerText);
      }

      // player not a BJ nor BUST => has a value
      else {

        int dealerpoints = calculateValueOfPlayersHand(dealer);
        // DEALER is Bust
        if (isPlayersHandOver21(dealer)) {
          winnerText = setAndShowResults(player, WIN, winnerText);
          // DEALER has a BJ
        } else if (isPlayersHandABlackJack(dealer)) {
          winnerText = setAndShowResults(player, LOOSE, winnerText);
          // EQUAL
        } else if (playerpoints == dealerpoints) {
          winnerText = setAndShowResults(player, TIE, winnerText);
          // LESS
        } else if (playerpoints < dealerpoints) {
          winnerText = setAndShowResults(player, LOOSE, winnerText);
          // HIGHER HAND
        } else if (playerpoints > dealerpoints) {
          winnerText = setAndShowResults(player, WIN, winnerText);
        }
      }
    }
    // Controller is set when a round plays! This is for our unit tests to work!
    if (controller != null) {
      controller.setlabelWinnerText(winnerText);
    }
  }
  
  // help function for calculateWinners()
  private String setAndShowResults( Player p, RoundResult result, 
      String winnerText ) {

    switch (result) {
      case LOOSE:
        System.out.println("Sorry, you lost.");
        winnerText = BlackJackConstantsAndTools.RESULT_YOU_LOOSE;
        p.setRoundResult(RoundResult.LOOSE);
        break;
      case WIN:
        System.out.println("Congratulations! You won.");
        winnerText = BlackJackConstantsAndTools.RESULT_YOU_WON;
        p.setRoundResult(RoundResult.WIN);
        break;
      case TIE:
        System.out.println("It´s a tie!");
        winnerText = BlackJackConstantsAndTools.RESULT_A_TIE;
        p.setRoundResult(RoundResult.TIE);
        break;
      default:
        System.out.println("Error in calculation of round result!");
    }
    return winnerText;
  }

  /**
   * NEW! Method to replace CalculateWinner
   */
  public void roundOutcome() {
    String winnerText = null;

    // 1. dealer is bust
    for (Player p : registeredPlayers) {

      if (isPlayersHandOver21(dealer)) {

        if (isPlayersHandOver21(p)) {
          System.out.println(RESULT_YOU_LOOSE);
          winnerText = BlackJackConstantsAndTools.RESULT_YOU_LOOSE;
          p.setRoundResult(RoundResult.LOOSE);

        } else {
          System.out.println("Congratulations! You won.");
          winnerText = BlackJackConstantsAndTools.RESULT_YOU_WON;
          p.setRoundResult(RoundResult.WIN);
        }
      }

      else if (calculateValueOfPlayersHand(p) <= 21
          && calculateValueOfPlayersHand(dealer) <= 21) {

        if (isPlayersHandABlackJack(p) && !isPlayersHandABlackJack(dealer)) {
          System.out.println("Congratulations! You won.");
          winnerText = BlackJackConstantsAndTools.RESULT_YOU_WON;
          p.setRoundResult(RoundResult.WIN);
        }

        else if (calculateValueOfPlayersHand(p) > calculateValueOfPlayersHand(
            dealer)) {
          System.out.println("Congratulations! You won.");
          winnerText = BlackJackConstantsAndTools.RESULT_YOU_WON;
          p.setRoundResult(RoundResult.WIN);

        } else if (calculateValueOfPlayersHand(p) < calculateValueOfPlayersHand(
            dealer)) {
          System.out.println("Sorry you LOST");
          winnerText = BlackJackConstantsAndTools.RESULT_YOU_LOOSE;
          p.setRoundResult(RoundResult.LOOSE);

        } else if (calculateValueOfPlayersHand(
            p) == calculateValueOfPlayersHand(dealer)) {
          System.out.println("It´s a TIE.");
          winnerText = BlackJackConstantsAndTools.RESULT_A_TIE;
          p.setRoundResult(RoundResult.TIE);
        }
      }
    }
    // Controller is set when a round plays! This is for our unit tests to
    // work!
    if (controller != null) {
      controller.setlabelWinnerText(winnerText);
    }
  }

  /*
   * uses players bets and result of played round to calculate money to be payed
   * back to players. Loosing players loose their bet, Tie players get their bet
   * back, winning players get the double amount of their bet, winning players
   * with a Blackjack even get an extra 50% of their bet!
   */
  public void handlePlayersBetsAndPayWinners() {

    /*
     * TIE => return betting amount WIN => return 2 * bet WIN + BJ => return 2.5
     * bet Loose => players bet is not returned
     */
    int playersBet;

    for (Player player : registeredPlayers) {

      playersBet = player.getPlayersBet();

      if (player.getRoundResult() == RoundResult.TIE) {
        player.addToPlayersCash(playersBet);

      } else if (player.getRoundResult() == RoundResult.WIN) {
        player.addToPlayersCash(playersBet * 2);

        // Win with Black Jack adds another 50% of bet!
        if (isPlayersHandABlackJack(player)) {
          player.addToPlayersCash((int) Math.round(playersBet / 2.0d));
          System.out.println("BLACKJACK");
        }
      }
      // just reset players bet for round
      // TODO would a whole method to reset players for next round?
      player.setPlayersBet(0);
      player.setRoundResult(null);
      updateGuiAfterChangeInDataModel();
    }
  }

  // NON STATIC HELPER FUNCTIONS
  public void addPlayerToBank(Player player) {

    if (registeredPlayers
        .size() <= BlackJackConstantsAndTools.PLAYERS_MAX_COUNT) {
      this.registeredPlayers.add(player);
    }
  }

  /**
   * Adds a player to the bank from the inlog-window
   * Checks that player cannot bring to much or too little money to
   * the table according to the Banks limits.
   * @param name
   * @param playerCash
   */
  public void addPlayerToBank(String name, Integer playerCash) {
    
    int cash = (int)Math.abs(playerCash.intValue());
    
    if (cash >= BANK_LIMIT){
      cash = BANK_LIMIT;
    } else if(cash < MIN_BET){
      cash = MIN_BET;
    }
    
    this.registeredPlayers.add(new Player(name, cash));
  }

  public void addPlayersToTheTable() {
    int rp = registeredPlayers.size() - 1;
    activePlayerOnGui = registeredPlayers.get(rp);
    updateGuiAfterChangeInDataModel();
  }

  /*
   * Method to reset Bank to a default start state. Clears all players and
   * dealer, gets a new Card Shoe.
   */
  public void resetBank() {
    registeredPlayers.clear();
    dealer = new Player("Dealer", 0);
    cardShoe = new CardShoe();
    roundThread = null;
  }
}
