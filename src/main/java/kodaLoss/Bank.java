package kodaLoss;

import java.util.ArrayList;
import java.util.List;

import gui.NameAlertBox;

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

  // Default value for dealer & playersHandScore
  String dealerscore = "0";
  String playerscore = "0";
  int round = 0;

  // CONSTRUCTORS
  public Bank() {
    System.out.println("Bank started...");
  }

  public void addPlayersToTheTable() {

    int rp = registeredPlayers.size() - 1;
    activePlayerOnGui = registeredPlayers.get(rp);
    updateGuiAfterChangeInDataModel();

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

    // Kolla om en runda redan är startad men kan vara Alive
    if (roundThread != null) {

      // if (roundThread.isAlive()){

      // roundThread is still alive!
      System.out.println("Already running a round");

    } else if (roundThread == null || !roundThread.isAlive()) {
      // start a new Round in its own Thread for not freezing the GUI!
      roundThread = new Thread(new Runnable() {

        @Override
        public void run() {

          System.out.println("Round started...");

          System.out.println(
              "Round -- > Number of Players: " + registeredPlayers.size());

          // Updaterar runda DIRTY TEST
          String roundString = Integer.toString(++round);
          controller.updateRound(roundString);

          
          // clear last hand from clean Table!
          for (Player p : registeredPlayers) {
            p.clearPlayersHand();
          }
          dealer.clearPlayersHand();

          // buttons off while dealing cards
          controller.allButtonsOff();

          
          // ask all players for bet before dealing!
          askPlayersForBetsForThisRound();
          
          BlackJackConstantsAndTools.sleepForXSeconds();
          
          // deal a card to all players and dealer
          dealOneCardToAll();
          updateGuiAfterChangeInDataModel();

          BlackJackConstantsAndTools.sleepForXSeconds();

          // deal second card and hide dealers second card
          controller.setHideDealersSecondCard(true);
          dealOneCardToAll();
          updateGuiAfterChangeInDataModel();

          // Get the dealer and players hands score in a toString metod
          // TODO Can this be added to updateGuiAfterChange...() method!?
          controller.setdealersHandScore("");
          controller.setplayersHandScore(playersHandScore(playerscore));

          // System out Dealer score
          System.out.println("Dealer score "
              + Bank_HelpersAndTools.calculateValueOfPlayersHand(dealer));
          // System out Player score
          System.out.println("Player score " + Bank_HelpersAndTools
              .calculateValueOfPlayersHand(activePlayerOnGui));

          // check if a player has a BlackJack from start!
          for (Player p : registeredPlayers) {

            if (isPlayersHandABlackJack(p)) {
              p.setPlayerActiveInRound(false);
            } else {
              p.setPlayerActiveInRound(true);
            }
          }

          // each player who does not have a Blackjack plays against bank
          for (Player p : registeredPlayers) {

            if (!isPlayersHandABlackJack(p)) {

              controller.gameIson();
              playerPlays(p);
              // TODO Change GUI to next Player!
            } else {
              // TODO inform player about BlackJack!!!
            }
          }

          // dealer plays after one second break / deactivate all buttons while
          // dealer plays
          controller.allButtonsOff();
          BlackJackConstantsAndTools.sleepForXSeconds();
          dealerPlays();
          
          // get winners and inform active player 
          calculateWinners();
          
          // handle the cash in pot and pay out players
          handlePlayersBetsAndPayWinners();
          
          controller.allButtonsOff();

          BlackJackConstantsAndTools.sleepForXSeconds(2000);

          // TODO RESET BEFORE NEXT ROUND!
          // reset Thread and Players for next round!

          resetBeforeNextRound();
        }
      });
      roundThread.start();
    } else {
      System.out.println("WRONT STATE in ROUND THREAD !");
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
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      if (uca.getUserChoice() == UserChoice.HIT) {

        dealOneCardToPlayer(player);
        // TODO Can setPlayerHandScore be moved in
        // updateGuiAfterChangeInDataModel()???
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
    // finally reset last choice in UCA
    uca.resetUserChoice();
  }

  /*
   * dealer plays. Takes cards until its hand is over 16
   */
  protected void dealerPlays() {

    try {
      // to show dealers second card, set to false
      controller.setHideDealersSecondCard(false);
      updateGuiAfterChangeInDataModel();

      // take cards while hand less than 17
      while (calculateValueOfPlayersHand(dealer) < 17) {

        if (isPlayersHandOver21(dealer)) {

          System.out.println("DEALER IS BUST!");
          controller.setlabelWinnerText("DEALER IS BUST!");
        }

        Thread.sleep(2000);
        controller.setdealersHandScore(dealersHandScore(dealerscore));
        dealOneCardToPlayer(dealer);
        // update gui now
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

  /**
   * Calculate winners
   */

  public void calculateWinners() {
    /*
     * possible Combinations by basic rules : DEALER BUST -> PLayer not bust =>
     * WIN = CASE 1 -> Player bust => Loose = CASE 2 Dealer BJ -> Player BJ =>
     * TIE = CASE 3 -> Player <= 21 => Loose = CASE 4 Dealer <= 21 -> Player
     * Bust => Loose = CASE 5 -> Player < dealer => Loose = CASE 6 -> Player >
     * dealer => WIN = CASE 7 -> Player = dealer => TIE = CASE 8
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
          // TODO not tie but LOOSE
          System.out.println("Tie");
          winnerText = BlackJackConstantsAndTools.RESULT_A_TIE;
          p.setRoundResult(RoundResult.TIE);

          // players hand is lower than Banks - CASE 6
        } else {
          System.out.println("Sorry, you lost.");
          winnerText = BlackJackConstantsAndTools.RESULT_YOU_LOOSE;
          p.setRoundResult(RoundResult.LOOSE);
        }

        // TODO CASE 3 , 4 , 5 are missing
      }
    }
    if (Bank.controller != null) {
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
    this.registeredPlayers.add(player);
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
    dealer = new Player("Dealer", 0);
    cardShoe = new CardShoe();

  }

  /**
   * Reads the Bank_HelpersAndTools.calculateValueOfPlayersHand metod </br >
   * and converts it from an int to String
   * 
   * @return dealerscore
   */
  public String dealersHandScore(String dealerscore) {

    int input = Bank_HelpersAndTools.calculateValueOfPlayersHand(dealer);
    dealerscore = Integer.toString(input);
    return dealerscore;

  }

  /**
   * Reads the Bank_HelpersAndTools.calculateValueOfPlayersHand metod </br >
   * and converts it from an int to String
   * 
   * @return playerscore
   */
  public String playersHandScore(String playerscore) {

    int input = Bank_HelpersAndTools
        .calculateValueOfPlayersHand(activePlayerOnGui);
    playerscore = Integer.toString(input);
    return playerscore;
  }
}
