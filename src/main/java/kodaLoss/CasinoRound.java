package kodaLoss;

import static kodaLoss.Bank_HelpersAndTools.isPlayersHandABlackJack;
import static kodaLoss.Bank_HelpersAndTools.isPlayersHandOver21;
import static kodaLoss.BlackJackConstantsAndTools.PLAYER_IS_BUST;

import java.util.ArrayList;
import java.util.List;

import kodaLoss.UserChoiceAdapter.UserChoice;

public class CasinoRound extends AbstractRound {

  private List<Player> SplitPlayerToDelete = new ArrayList<>();
  
  
  
  
  
  public CasinoRound() {
    System.out.println("CasinoRound");
    controller.activateAdvancedButton();
  }

  @Override
  protected void playerPlays(Player player) {

    System.out.println("Player plays - Casino rules - started...");

    // check for casino rules before playing
    // just one casino rule per round, the other will be deactivated 
    // after playing one of them!
    
    if (!player.isSplitPlayer()){
    activateSplit(player);
    activateInsurance(player);
    activateDouble(player);
    } else {
      controller.disableAdvancedButton();
    }
    
    
    // activate players buttons
    controller.gameIson();

    uca.resetUserChoice(); // prepare UCA for input

    while (uca.getUserChoice() != UserChoice.STAY) {
      
      if (isPlayersHandOver21(player)) {
        System.out.println(PLAYER_IS_BUST);
        controller.setlabelWinnerText(PLAYER_IS_BUST);
        bank.updateGuiAfterChangeInDataModel();
        break;
      }

      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      if (uca.getUserChoice() == UserChoice.HIT) {

        bank.dealOneCardToPlayer(player);
        bank.updateGuiAfterChangeInDataModel();

        System.out.println("PLAYER HIT");
        player.printHandToConsole();
        uca.resetUserChoice();

      } else if (uca.getUserChoice() == UserChoice.SPLIT) {
        
        makeSplitPlayer(player);
        controller.disableAdvancedButton();
        uca.resetUserChoice();

      } else if (uca.getUserChoice() == UserChoice.DOUBLE) {
        playerDouble(player);
        break;

        
      } else if (uca.getUserChoice() == UserChoice.INSURANCE){
        playerInsurance(player);
        controller.disableAdvancedButton();
       
        
      }
    }

    // print out all data of Player!
    System.out.println(player.toString());

    
    // finally reset last choice in UCA
    uca.resetUserChoice();

  }
  
  
  private void activateDouble(Player player) {
    
    if (checkIfDoubleCanBePlayed(player)){
      controller.activateDoubleButton();
    }
    
  }

  
  // adds an insurance to player and adjusts money
  private void playerInsurance(Player p){
    p.setHasInsurance(true);
  }
  
  
  
  // makes a SPLIT_Player to player 
  private void makeSplitPlayer(Player player) {
    
    Player splitPlayer = new Player("SPLIT_"+ player.getName() , 0);
    splitPlayer.setPlayersBet(player.getPlayersBet());
    splitPlayer.setSplitPlayer(true);
    splitPlayer.addCardToHand(player.getPlayersHand().remove(1));
    SplitPlayerToDelete.add(splitPlayer);
    controller.updatePlayer(splitPlayer);
    bank.updateGuiAfterChangeInDataModel();
    bank.dealOneCardToPlayer(player);
    
    bank.dealOneCardToPlayer(splitPlayer);
    
    for (int i = 0 ; i < bank.registeredPlayers.size() ; i++){

      if (bank.registeredPlayers.get(i) == player){
        bank.registeredPlayers.add(i+1, splitPlayer);
      }
    }
    
    bank.updateGuiAfterChangeInDataModel();
  }
  
  @Override
  protected void clearUpAfterRound(){
    mergeSplitPlayers();
    deleteSplitPlayers();
    
  }
  


  private void mergeSplitPlayers() {
    bank.activePlayerOnGui = bank.registeredPlayers.get(0);
    
    for (int i = 0 ; i < bank.registeredPlayers.size() ; i++){
     
      Player p = bank.registeredPlayers.get(i);
      
      if (p.isSplitPlayer()){
        Player addPlayer = bank.registeredPlayers.get(i-1);
        addPlayer.addToPlayersCash(p.getPlayersCash());
      }
    }
  }

  private void deleteSplitPlayers() {
    for (Player p : SplitPlayerToDelete){
        if (bank.registeredPlayers.contains(p)){
          bank.registeredPlayers.remove(p);
      }
    }
  }

  /**
   * Checks if the player can buy an insurance. A player is allowed to insure
   * himself if the open card of the dealer is an ACE and the player has the
   * insurance price of half his bet for this round.
   * 
   * @param p
   *          Player
   * @return true if player can buy an insurance
   */
  private boolean checkIfDoubleCanBePlayed(Player player) {
    return player.getPlayersCash() >= player.getPlayersBet()
        && player.getPlayersHand().size() <= 2;
  }
  
  /**
   * Returns true if a player has two cards in his hand and both have the same
   * value. This rule means that even a hand with a 10 and a King e.g. could be
   * split!
   */
  public boolean checkIfSplitCanBePlayed(Player p) {
  
    if (p.getPlayersHand().size() != 2) {
      return false;
  
    } else if (p.getPlayersBet() > p.getPlayersCash()) {
      return false;
  
    } else {
      final Card cardOne = p.getPlayersHand().get(0);
      final Card cardTwo = p.getPlayersHand().get(1);
  
      System.out.println(cardOne.getValue() == cardTwo.getValue());
  
      return (cardOne.getValue() == cardTwo.getValue());
    }
  }

  public boolean checkIfInsuranceCanBePlayed(Player p){
    
    return Bank_HelpersAndTools.checkForAceCardOnYourHand(bank.dealer) && 
        p.getPlayersCash() * 2 >= p.getPlayersBet();
    
    
  }
  
  
  
  /**
   * 
   * Check if the dealer got an ACE on the first DEAL, And activates the button
   * so the player can choice to use Insurance
   * 
   */
  public void activateInsurance(Player p) {

    if (checkIfInsuranceCanBePlayed(p)) {

      controller.activateInsuranceButton();
    } else {

      if (p.getPlayersCash() < p.getPlayersBet() / 2) {
        controller.setlabelWinnerText(
            BlackJackConstantsAndTools.NOT_ENOUGH_CASH_TO_TAKE_INSURANCE);
      }
    }
  }
  
  
  

  @Override
  public void handlePlayersBetsAndPayWinners() {
    /*
     * WITH CASINO RULES! TIE => return betting amount WIN => return 2 * bet WIN
     * + BJ => return 2.5 bet Loose => players bet is not returned INSURANCE 3:2
     * if dealer has a BlackJAck, return bet if dealer wins
     */
    int playersBet;

    for (Player player : bank.registeredPlayers) {

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
      } else if (player.getRoundResult() == RoundResult.LOOSE) {
        // insured against loss => return bet (2 * 1/2 bet)
        // Dealer has a BlackJAck => return 3:2 of bet!
        if (player.isHasInsurance()) {

          if (isPlayersHandABlackJack(bank.dealer)) {
            player.addToPlayersCash((int) 1.5 * player.getPlayersBet());
          } else {
            player.addToPlayersCash(player.getPlayersBet());
          }
        }
      }
      // last rounds Players bet as default in gui for next round
      // player.setPlayersBet(0);
      player.setRoundResult(null);
      player.setHasInsurance(false);
      bank.updateGuiAfterChangeInDataModel();
    }
  }
  //
  public void playerDouble(Player p) {
    // controller.activateDoubleButton(); // måste ha varit aktiverat innan dess
    // för att kunna spela double
    doublePlayersBet(p);
    bank.dealOneCardToPlayer(p);
    bank.updateGuiAfterChangeInDataModel();

    System.out.println("PLAYER DOUBLE");
    // tog bort sysout prints och reset-uca, då de kommer i player plays i alla
    // fall
  }

  /*
   * Doubles players bet for this round, if player has the cash! Otherwise just
   * leaves the bet as it is. Should be called after checking if playing double
   * is legal!
   * 
   * THROWS: IllegalArgumentException if doubled bet would exceed players cash!
   */
  public void doublePlayersBet(Player p) {
    final int playersBet = p.getPlayersBet();

    if (p.getPlayersCash() >= playersBet) {
      p.addToPlayersCash(playersBet);
      p.setPlayersBet(2 * playersBet);

    } else {
      controller.setlabelWinnerText(
          BlackJackConstantsAndTools.NOT_ENOUGH_CASH_TO_DOUBLE);
      // throw new IllegalArgumentException("doubled bet exceeds Players
      // cash!");
    }
  }

 

  public void activateSplit(Player p) {

    if (true){//checkIfSplitCanBePlayed(p)) {
      controller.activateSplitButton();
    }
  }
}
