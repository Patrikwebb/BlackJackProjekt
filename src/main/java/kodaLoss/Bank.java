package kodaLoss;

import java.util.ArrayList;
import java.util.List;
import static kodaLoss.UserChoiceAdapter.*;

import static kodaLoss.Bank_HelpersAndTools.*;

public class Bank {

  // MEMBERS

  private static UserChoiceAdapter uca = UserChoiceAdapter.getInstance();

  // Singelton object of Bank
  private static Bank bank = new Bank();

  private static Controller controller;

  public List<Player> registeredPlayers = new ArrayList<Player>();
  private Player dealer;
  private Player activePlayerOnGui;

  private CardShoe cardShoe = new CardShoe();
  // private boolean dealerIsBust = true;

  private Thread roundThread = null;

  // CONSTRUCTORS
  private Bank() {

    System.out.println("Bank started in a testMode!");

    registeredPlayers.add(new Player("TESTPLAYER"));
    System.out.println("Registered players: " + registeredPlayers.size());
    activePlayerOnGui = registeredPlayers.get(0);
    dealer = new Player("Dealer");

    System.out.println("No of Players" + registeredPlayers.size());

  }

  public static Bank getInstance() {
    return bank;
  }

  // METHODS TO REGISTER CONTROLLER

  public static void registerController(Controller cont) {
    controller = cont;
    controller.test();

  }

  // METHODS FOR GUI

  /*
   * called after changes in player or dealer model. Updates the gui by directly
   * calling methods in Controller.
   */

  private void updateGuiAfterChangeInDataModel() {
    if (controller != null) {
      controller.updatePlayer(activePlayerOnGui);
      controller.updateDealer(dealer);
    } else
      System.out.println("controller not set in bank!");
  }

  /*
   * methods that controls the sequence of actions to play one round, control of
   * the gameplay!
   */
  public void playOneRound() {

    if (roundThread == null) {

      roundThread = new Thread(new Runnable() {

        @Override
        public void run() {
          // TODO Auto-generated method stub

          System.out.println("Rundan startat");

          // deal a card to all players and dealer
          dealOneCardToAll();
          System.out.println(activePlayerOnGui.getPlayerHandsSize());

          updateGuiAfterChangeInDataModel();

          dealOneCardToAll();
          System.out.println(activePlayerOnGui.getPlayerHandsSize());
          updateGuiAfterChangeInDataModel();
          // TODO dealers other card to gui ska bli covered!

          // check if a player has a BlackJack from start!
          for (Player p : registeredPlayers) {

            if (isPlayersHandABlackJack(p)) {
              p.setPlayerActiveInRound(false);
            }
          }

          // each active player plays against bank

          System.out.println(activePlayerOnGui.isActive());
          for (Player p : registeredPlayers) {

            if (p.isActive()) {
              playerPlays(p);
              System.out.println("RETURNS FROM PLAYER PLAYS");
            }

          }

          // dealer plays
          dealerPlays();

          /*
           * if dealer is not bust => player who are not bust, and have a higher
           * hand than dealer. if dealer is bust => all players that are not
           * bust win
           * 
           */

          calculateWinners();

        }
      });
      roundThread.start();

    } else {
      System.out.println("Already running a round");
    }

  }

  /*
   * Player plays against Bank in one round. Sets player inactive if bust. Uses
   * class UserChoiceAdapter to get user events from the user interface
   */
  protected void playerPlays( Player player ) {
System.out.println("Player plays startad!");
    
    UserChoiceAdapter.getInstance().resetUserChoice(); // prepare for input

    while (getUserChoice() != UserChoice.STAY) {

       player.printHandToConsole();

      if (getUserChoice() == UserChoice.HIT) {
        dealOneCardToPlayer(player);
       
        updateGuiAfterChangeInDataModel();

        System.out.println("PLAYER HIT");
         player.printHandToConsole();
        UserChoiceAdapter.getInstance().resetUserChoice();

        if (isPlayersHandOver21(player)) {
          updateGuiAfterChangeInDataModel();
          player.setPlayerActiveInRound(false);
          player.setBusted(true);
          System.out.println("PLAYER IS BUST NOW!");
           player.printHandToConsole();
          break;
        }
      }
    }
    // finally
    UserChoiceAdapter.getInstance().resetUserChoice();
  }

  /*
   * dealer plays. Takes cards until its hand is over 16
   */
  protected void dealerPlays() {
    while (calculateValueOfPlayersHand(dealer) < 17) {

      dealOneCardToPlayer(dealer);

      // update gui now
      updateGuiAfterChangeInDataModel();

      setPlayerToBust(dealer, false);

      // dealer.printHandToConsole();

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
   * bank deals one card from the card shoe to a player who takes the card and
   * adds it to his hand. (Then the gui is updated for the player)
   */
  private void dealOneCardToPlayer(Player player) {
    player.addCardToHand(cardShoe.getACardFromCardShoe());
    // TODO call gui to update players hand!
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
   * Calculate winners TODO update GUI who won
   */
  //
  public void calculateWinners() {
    int playerpoints;

    if (isPlayerBust(dealer)) {

      for (Player p : registeredPlayers) {
        playerpoints = calculateValueOfPlayersHand(p);

        if (playerpoints <= 21) {
          // TO GUI Player WINNS
          System.out.println("Congratulations! You won.");
        } else {
          // To Gui YOU lost!
          System.out.println("Sorry, you lost.");
        }
      }
    } else {

      for (Player p : registeredPlayers) {
        playerpoints = calculateValueOfPlayersHand(p);

        if (playerpoints <= 21
            && playerpoints > calculateValueOfPlayersHand(dealer)) {
          // TO GUI Player WINNS
          System.out.println("Congratulations! You won.");

        } else {
          // To Gui YOU lost!
          System.out.println("Sorry, you lost.");
        }
      }
    }
  }

  // public static void main(String[] args) {
  //
  // System.out.println("now the Thread");
  // Thread Clicker = new Thread() {
  //
  // public void run() {
  // while (true) {
  // try {
  // Thread.sleep(1000);
  // UserChoiceAdapter.getInstance().playerChoosesToHit();
  // } catch (InterruptedException e) {
  // System.out.println("Thread misslyckade");
  // }
  // }
  // }
  // };
  // Clicker.start();
  //
  // Bank bank = new Bank();
  // Player p = new Player("TEST");
  // Player p2 = new Player("PATRIK");
  // bank.registeredPlayers.add(p);
  // bank.registeredPlayers.add(p2);
  //
  // System.out.println("Now to the bank");
  // bank.playOneRound();
  // }

}
