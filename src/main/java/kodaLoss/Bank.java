package kodaLoss;

import java.util.ArrayList;
import java.util.List;

import static kodaLoss.UserChoiceAdapter.*;

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
  protected static Controller controller = null;

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
  public static void registerController(Controller control) {

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
    if (controller == null){
      System.out.println("CONTROLLER has to be set!");
      throw new IllegalStateException("Controller has to be set! ");
    }
    
    // DO not start a new game, when a game is running! 
    if (roundThread != null) {
      System.out.println("Already running a round");

    } else if (roundThread == null || !roundThread.isAlive()) {
      // start a new Round in its own Thread for not freezing the GUI!
      roundThread = new Thread( () -> {

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
          
          // handle the cash in pot and pay out players
          handlePlayersBetsAndPayWinners();

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
  
  /* take all cards off the table 
  * (clear dealer and players hands and update gui)*/
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
          p.getName() + ", " + BlackJackConstantsAndTools.ASK_FOR_BETS);

      final int bet;

      while (true) {

        if (uca.getUserChoice() == UserChoice.LAY_BET) {
          bet = controller.getBetFromPlayersTextField();
          // switch off asking for a bet!
          controller.setlabelWinnerText("");
          break;
        }
        BlackJackConstantsAndTools.sleepForXSeconds(10);
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
          System.out.println("PLAYER IS BUST!");
          controller.setlabelWinnerText("PLAYER IS BUST!");
          updateGuiAfterChangeInDataModel();
          break;
        }
      }
    }
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

          System.out.println("DEALER IS BUST!");
          controller.setlabelWinnerText("DEALER IS BUST!");
        }

        BlackJackConstantsAndTools.sleepForXSeconds(2000);
        dealOneCardToPlayer(dealer);
        updateGuiAfterChangeInDataModel();

        if (isPlayersHandOver21(dealer)) {
          // temporary until we send to GUI
          System.out.println("DEALER IS BUST!");
          updateGuiAfterChangeInDataModel();

        } else {
          // temporary until we send to GUI
          System.out
              .println("DEALER has " + calculateValueOfPlayersHand(dealer));
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
     * possible Combinations by basic rules : 
     * DEALER BUST -> PLayer not bust => WIN = CASE 1
     * DEALER BUST -> Player bust => Loose = CASE 2 
     * Dealer BJ -> Player BJ => TIE = CASE 3 
     * DEALER BJ -> Player <= 21 => Loose = CASE 4 
     * Dealer <= 21 -> Player Bust => Loose = CASE 5
     * Dealer <= 21 -> Player < dealer => Loose = CASE 6 
     * DEALER <= 21 -> Player > dealer => WIN = CASE 7 
     * DEALER <= 21 -> Player = dealer => TIE = CASE 8
     * Dealer <=21 -> Player = BJ => WIN = case 9
     */

    int playerpoints;
    String winnerText = null;

    // 1. dealer is bust
    if (isPlayersHandOver21(dealer)) {

      for (Player p : registeredPlayers) {
        playerpoints = calculateValueOfPlayersHand(p);

        // player not Bust - CASE 1
        if (playerpoints <= 21) {
          System.out.println("Congratulations! You won.");
          winnerText = BlackJackConstantsAndTools.RESULT_YOU_WON;
          p.setRoundResult(RoundResult.WIN);

          // player also bust - CASE 2
        } else {
          System.out.println("Sorry, you lost.");
          winnerText = BlackJackConstantsAndTools.RESULT_YOU_LOOSE;
          p.setRoundResult(RoundResult.LOOSE);
        }
      }

      // 2. dealer is not bust
    } else {

      for (Player p : registeredPlayers) {
        playerpoints = calculateValueOfPlayersHand(p);

        // player not bust and higher hand than dealer - CASE 7
        if (playerpoints <= 21
            && playerpoints > calculateValueOfPlayersHand(dealer)) {
          System.out.println("Congratulations! You won.");
          winnerText = BlackJackConstantsAndTools.RESULT_YOU_WON;
          p.setRoundResult(RoundResult.WIN);
        }
        // players hand has same hand as dealers - CASE 8
        else if (playerpoints <= 21
            && playerpoints == calculateValueOfPlayersHand(dealer)) {
          winnerText = BlackJackConstantsAndTools.RESULT_A_TIE;
          p.setRoundResult(RoundResult.TIE);

          // players hand is lower than Banks - CASE 6
        } else {
          System.out.println("Sorry, you lost.");
          winnerText = BlackJackConstantsAndTools.RESULT_YOU_LOOSE;
          p.setRoundResult(RoundResult.LOOSE);
        }

        // TODO CASE 3 , 4 , 5 , 9 are missing
      }
    }
      controller.setlabelWinnerText(winnerText);
  }

  /*
   * uses players bets and result of played round to calculate money to be payed
   * back to players. Loosing players loose their bet, Tie players get their bet
   * back, winning players get the double amount of their bet, winning players
   * with a Blackjack even get an extra 50% of their bet!
   */
  public void handlePlayersBetsAndPayWinners() {

    /*
     * TIE => return betting amount
     * WIN => return 2 * bet 
     * WIN + BJ => return 2.5
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
    
    if (registeredPlayers.size() <= BlackJackConstantsAndTools.PLAYERS_MAX_COUNT ){
      this.registeredPlayers.add(player);
    }
  }

  /**
   * Use for AlertBox
   * 
   * @param name
   * @param playerCash
   */
  public void addPlayerToBank(String name, Integer playerCash) {
    this.registeredPlayers.add(new Player(name, playerCash));
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
  }
}
