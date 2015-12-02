package kodaLoss;

import static kodaLoss.Bank_HelpersAndTools.calculateValueOfPlayersHand;
import static kodaLoss.Bank_HelpersAndTools.isPlayersHandABlackJack;
import static kodaLoss.Bank_HelpersAndTools.isPlayersHandOver21;
import static kodaLoss.RoundResult.LOOSE;
import static kodaLoss.RoundResult.TIE;
import static kodaLoss.RoundResult.WIN;

import static kodaLoss.BlackJackConstantsAndTools.*;

import kodaLoss.UserChoiceAdapter.UserChoice;

public abstract class AbstractRound extends Thread {

  protected Bank bank = Bank.getInstance();

  protected UserChoiceAdapter uca = UserChoiceAdapter.getInstance();

  protected IController controller = bank.controller;

  public AbstractRound() {

  }

  @Override
  public void run() {

    // clear last hand from clean Table!
    clearHandsOffTheTable();

    // buttons off while dealing cards
    controller.allButtonsOff();

    // Activate player to set bet before pressing DEAL
    // activatePlayerToSetBet();

    // ask all players for bet before dealing!
    askPlayersForBetsForThisRound();

    BlackJackConstantsAndTools.sleepForXSeconds();

    // deal a card to all players and dealer
    bank.dealOneCardToAll();

    BlackJackConstantsAndTools.sleepForXSeconds();

    // deal second card and hide dealers second card
    controller.setHideDealersSecondCard(true);

    BlackJackConstantsAndTools.sleepForXSeconds();

    bank.dealOneCardToAll();

    // each player who does not have a Blackjack plays against bank
    allPlayersPlayAgainstTheDealer();

    BlackJackConstantsAndTools.sleepForXSeconds();

    // dealer plays
    dealerPlays();

    // get winners and inform active player
    calculateWinners();
    // roundOutcome();

    // handle the cash in pot and pay out players
    handlePlayersBetsAndPayWinners();

    // Check if player is game over
    // Sprint 3: => game terminates!
    checkForGameOver();

    cleanUpAfterRound();

    BlackJackConstantsAndTools.sleepForXSeconds(2000);

    // reset Thread and Players for next round!
    bank.resetBeforeNextRound();
  }

  // Can be overridden in implementation of Round
  protected void cleanUpAfterRound() {

  }

  /*
   * take all cards off the table (clear dealer and players hands and update
   * gui)
   */
  protected void clearHandsOffTheTable() {

    for (Player p : bank.registeredPlayers) {
      p.clearPlayersHand();
      controller.updatePlayer(p);
    }
    bank.activePlayerOnGui = bank.registeredPlayers.get(0); 
    bank.dealer.clearPlayersHand();
    bank.updateGuiAfterChangeInDataModel();
  }

  // all players without a BlackJack play against the bank
  private void allPlayersPlayAgainstTheDealer() {

    Player p;
    // has to be oldfashioned for-loop because of list might be changed in size
    // by playing split
    for (int i = 0; i < bank.registeredPlayers.size(); i++) {

      p = bank.registeredPlayers.get(i);
      bank.activePlayerOnGui = p;
      
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

  public void activatePlayerToSetBet() {

    for (Player p : bank.registeredPlayers) {

      controller.activatePlayersBetField();

      controller.setlabelWinnerText(
          p.getName() + BlackJackConstantsAndTools.ASK_FOR_BETS);
    }
  }

  public void setPlayersDefaultBet() {
    controller.getBetFromPlayersTextField();
  }

  /*
   * method which blocks starting the dealing of cards until all players set
   * their bets
   */
  // TODO public just for testing!
  public void askPlayersForBetsForThisRound() {

    for (Player p : bank.registeredPlayers) {

      // METHOD TO CHANGE ACTIVEPLAYERINGUI COMES HERE

      controller.activatePlayersBetField();

      controller.setlabelWinnerText(
          p.getName() + BlackJackConstantsAndTools.ASK_FOR_BETS);

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

      bet = (int) Math.abs(bet);

      if (bet > MAX_BET) {
        bet = MAX_BET;
        controller
            .setlabelWinnerText("Max bet is " + MAX_BET + " on this table!");
      }

      if (bet < BlackJackConstantsAndTools.MIN_BET) {
        bet = BlackJackConstantsAndTools.MIN_BET;
        controller.setlabelWinnerText("Min bet is "
            + BlackJackConstantsAndTools.MIN_BET + " on this table!");
      }
      p.setPlayersBet(bet);
      bank.updateGuiAfterChangeInDataModel();
    }
  }

  /*
   * Player plays against Bank in one round. Sets player inactive if bust.
   * 
   * Uses class UserChoiceAdapter to get user events from the user interface
   */
  public abstract void playerPlays(Player player);

//  //
//  public void playerDouble(Player p) {
//    // controller.activateDoubleButton(); // måste ha varit aktiverat innan dess
//    // för att kunna spela double
//    doublePlayersBet(p);
//    bank.dealOneCardToPlayer(p);
//    bank.updateGuiAfterChangeInDataModel();
//
//    System.out.println("PLAYER DOUBLE");
//    // tog bort sysout prints och reset-uca, då de kommer i player plays i alla
//    // fall
//  }
//
//  /*
//   * Doubles players bet for this round, if player has the cash! Otherwise just
//   * leaves the bet as it is. Should be called after checking if playing double
//   * is legal!
//   * 
//   * THROWS: IllegalArgumentException if doubled bet would exceed players cash!
//   */
//  public void doublePlayersBet(Player p) {
//    final int playersBet = p.getPlayersBet();
//
//    if (p.getPlayersCash() >= playersBet) {
//      p.addToPlayersCash(playersBet);
//      p.setPlayersBet(2 * playersBet);
//
//    } else {
//      controller.setlabelWinnerText(
//          BlackJackConstantsAndTools.NOT_ENOUGH_CASH_TO_DOUBLE);
//      // throw new IllegalArgumentException("doubled bet exceeds Players
//      // cash!");
//    }
//  }
//
// 
//
//  /**
//   * Returns true if a player has two cards in his hand and both have the same
//   * value. This rule means that even a hand with a 10 and a King e.g. could be
//   * split!
//   */
//  public boolean checkIfSplitCanBePlayed(Player p) {
//
//    if (p.getPlayersHand().size() != 2) {
//      return false;
//
//    } else if (p.getPlayersBet() > p.getPlayersCash()) {
//      return false;
//
//    } else {
//      final Card cardOne = p.getPlayersHand().get(0);
//      final Card cardTwo = p.getPlayersHand().get(1);
//
//      System.out.println(cardOne.getValue() == cardTwo.getValue());
//
//      return (cardOne.getValue() == cardTwo.getValue());
//    }
//  }
//
//  public void activateSplit(Player p) {
//
//    if (checkIfSplitCanBePlayed(p)) {
//      controller.activateSplitButton();
//    }
//  }

  

  /*
   * dealer plays. Takes cards until its hand is over 16
   */
  protected void dealerPlays() {

    // to show dealers second card, set to false
    controller.setHideDealersSecondCard(false);
    bank.updateGuiAfterChangeInDataModel();

    // take cards while hand less than 17
    while (calculateValueOfPlayersHand(bank.dealer) < 17) {

      if (isPlayersHandOver21(bank.dealer)) {

        System.out.println(DEALER_IS_BUST);
        controller.setlabelWinnerText(DEALER_IS_BUST);
      }

      BlackJackConstantsAndTools.sleepForXSeconds(2000);
      bank.dealOneCardToPlayer(bank.dealer);
      bank.updateGuiAfterChangeInDataModel();

      if (isPlayersHandOver21(bank.dealer)) {
        // temporary until we send to GUI
        System.out.println(DEALER_IS_BUST);
        bank.updateGuiAfterChangeInDataModel();

      } else {
        // temporary until we send to GUI
        System.out
            .println("Dealer has " + calculateValueOfPlayersHand(bank.dealer));
      }
    }
  }

  /// *
  // * deals one card to all players and dealer
  // */
  // protected void dealOneCardToAll() {
  //
  // for (Player p : bank.registeredPlayers) {
  // dealOneCardToPlayer(p);
  // }
  // dealOneCardToPlayer(bank.dealer);
  // bank.updateGuiAfterChangeInDataModel();
  // }
  //
  /// *
  // * bank deals one card from the card shoe to a player who takes the card and
  // * adds it to his hand. (Then the gui is updated for the player)
  // */
  // private void dealOneCardToPlayer(Player player) {
  // player.addCardToHand(bank.cardShoe.getACardFromCardShoe());
  // }

  /**
   * Calculate winners
   */
  public void calculateWinners() {
    /*
     * calculates and sets the RoundResult for every possible outcome of the
     * game between the bank and the player!
     */

    // Outcomes as ordered in Method:
    // PLAYER BJ - Dealer BJ => TIE
    // - Dealer less => WIN
    //
    // Player BUST - DEALER ? => LOOSE
    //
    // Player value - Dealer BUST=> WIN
    // - Dealer BJ => LOOSE
    // = Dealer value => TIE
    // < Dealer value => LOOSE
    // > Dealer value => WIN

    int playerpoints;
    String winnerText = null;

    for (Player player : bank.registeredPlayers) {
      playerpoints = calculateValueOfPlayersHand(player);

      // player has a BJ
      if (isPlayersHandABlackJack(player)) {

        if (isPlayersHandABlackJack(bank.dealer)) {
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

        int dealerpoints = calculateValueOfPlayersHand(bank.dealer);
        // DEALER is Bust
        if (isPlayersHandOver21(bank.dealer)) {
          winnerText = setAndShowResults(player, WIN, winnerText);
          // DEALER has a BJ
        } else if (isPlayersHandABlackJack(bank.dealer)) {
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
      BlackJackConstantsAndTools.sleepForXSeconds();
    }
  }

  // help function for calculateWinners()
  private String setAndShowResults(Player p, RoundResult result,
      String winnerText) {

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

//  /**
//   * NEW! Method to replace CalculateWinner
//   */
//  public void roundOutcome() {
//    String winnerText = null;
//
//    // 1. dealer is bust
//    for (Player p : bank.registeredPlayers) {
//
//      if (isPlayersHandOver21(bank.dealer)) {
//
//        if (isPlayersHandOver21(p)) {
//          System.out.println(RESULT_YOU_LOOSE);
//          winnerText = BlackJackConstantsAndTools.RESULT_YOU_LOOSE;
//          p.setRoundResult(RoundResult.LOOSE);
//
//        } else {
//          System.out.println("Congratulations! You won.");
//          winnerText = BlackJackConstantsAndTools.RESULT_YOU_WON;
//          p.setRoundResult(RoundResult.WIN);
//        }
//      }
//
//      else if (calculateValueOfPlayersHand(p) <= 21
//          && calculateValueOfPlayersHand(bank.dealer) <= 21) {
//
//        if (isPlayersHandABlackJack(p)
//            && !isPlayersHandABlackJack(bank.dealer)) {
//          System.out.println("Congratulations! You won.");
//          winnerText = BlackJackConstantsAndTools.RESULT_YOU_WON;
//          p.setRoundResult(RoundResult.WIN);
//        }
//
//        else if (calculateValueOfPlayersHand(p) > calculateValueOfPlayersHand(
//            bank.dealer)) {
//          System.out.println("Congratulations! You won.");
//          winnerText = BlackJackConstantsAndTools.RESULT_YOU_WON;
//          p.setRoundResult(RoundResult.WIN);
//
//        } else if (calculateValueOfPlayersHand(p) < calculateValueOfPlayersHand(
//            bank.dealer)) {
//          System.out.println("Sorry you LOST");
//          winnerText = BlackJackConstantsAndTools.RESULT_YOU_LOOSE;
//          p.setRoundResult(RoundResult.LOOSE);
//
//        } else if (calculateValueOfPlayersHand(
//            p) == calculateValueOfPlayersHand(bank.dealer)) {
//          System.out.println("It´s a TIE.");
//          winnerText = BlackJackConstantsAndTools.RESULT_A_TIE;
//          p.setRoundResult(RoundResult.TIE);
//        }
//      }
//    }
//    // Controller is set when a round plays! This is for our unit tests to
//    // work!
//    if (controller != null) {
//      controller.setlabelWinnerText(winnerText);
//      BlackJackConstantsAndTools.sleepForXSeconds();
//    }
//  }

  /*
   * uses players bets and result of played round to calculate money to be payed
   * back to players. Loosing players loose their bet, Tie players get their bet
   * back, winning players get the double amount of their bet, winning players
   * with a Blackjack even get an extra 50% of their bet!
   */

  public abstract void handlePlayersBetsAndPayWinners();

  /*
   * Sprint 3: if single user is bankrupt the game terminates!
   */
  private void checkForGameOver() {
    boolean allBroke = true;
    
    for (Player p : bank.registeredPlayers){
      if (p.getPlayersCash() >= BlackJackConstantsAndTools.MIN_BET) {
        allBroke = false;
        break;
      }
    }
    
    if (allBroke){
        controller.setlabelWinnerText(
          "GAME OVER! Please get money and restart the game!");
      BlackJackConstantsAndTools.sleepForXSeconds(3000);
      clearHandsOffTheTable();
      bank.resetBank();
      BlackJackConstantsAndTools.sleepForXSeconds(10000);
      controller.setlabelWinnerText("Programme terminates...");
      BlackJackConstantsAndTools.sleepForXSeconds(2000);
      System.exit(0);
      }
    }
  

}
