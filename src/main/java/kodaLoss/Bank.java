package kodaLoss;

import java.util.ArrayList;
import java.util.List;

import gui.Main;

public class Bank {

  // MEMBERS
  private Main main = null;

  private List<Player> registeredPlayers = new ArrayList<Player>();
  private Player dealer = new Player("Dealer");
  private CardShoe cardShoe = new CardShoe();

  // CONSTRUCTORS
  public Bank() {

  }

  public Bank(Main main) {
    this.main = main;
  }

  // CLASS METHODS

  /**
   * calculates value of players hand. Sets value of Aces in players hand to 1
   * if players should have gone bust otherwise.
   */
  public static int calculateValueOfPlayersHand(Player player) {
    int sum = 0;
    int numberOfAces = 0;

    for (Card card : player.getPlayersHand()) {
      sum += card.getValue();
      if (card.getRank() == Rank.ACE) {
        numberOfAces++;
      }
    }

    while (sum > 21 && numberOfAces > 0) {
      sum -= 10;
      numberOfAces--;
    }

    return sum;
  }

  public void setMain(Main main) {
    this.main = main;
  }

  /**
   * checks if players hand is a BlackJack!
   * 
   * @param player
   * @return true if Player has a BlackJack
   */
  public static boolean isPlayersHandABlackJack(Player player) {
    return (player.getPlayerHandsSize() == 2
        && calculateValueOfPlayersHand(player) == 21);
  }
  
  /**
   * checks if a players hands value is over 21, even considering
   * the aces rule (Ace value = 1)
   * @param player
   * @return true if players hand is over 21, else false
   */
  public static boolean isPlayersHandOver21 (Player player){
    return (calculateValueOfPlayersHand(player) > 21);
  }
  

  private void playOneRound() {

    // deal a card to all players and dealer
    dealOneCardToAll();

    dealOneCardToAll();
    // dealer last card to gui ska bli covered!

    // check if a player has a BlackJack from start!
    for (Player p : registeredPlayers) {
      if (isPlayersHandABlackJack(p)) {
        p.setPlayerActiveInRound(false);
      }
    }

    // each active player plays against bank

    for (Player p : registeredPlayers) {
      
      if (p.isActive()) {
        playerPlays();
      }
    }
    
    // dealer plays
    dealerPlays();
    
    // calculate winners
    /*
     *  if dealer is not bust => player who are not bust, and have a higher hand than dealer. 
     *  if dealer is bust => all players that are not bust win
     *  
     */
    

  }

  
  
  
  
  
  /**
   * Player plays against Bank in one round. Sets player inactive if bust
   * 
   */
  private void playerPlays() {
    //TODO 
  }

  /**
   * dealer plays. Takes cards until its hand is over 16
   */
  private void dealerPlays(){
    while( calculateValueOfPlayersHand( dealer ) < 17) {
      // has to be refactorized! Method for deal out a card?!?
      dealer.addCardToHand( this.cardShoe.getACardFromCardShoe() ); 
    }
  }
  
  /**
   * deals one card to all players and dealer
   */
  private void dealOneCardToAll() {
    for (Player p : registeredPlayers) {
      p.addCardToHand(cardShoe.getACardFromCardShoe());
    }
    dealer.addCardToHand(cardShoe.getACardFromCardShoe());
  }

  
  
  
  
  
  
  
  
  
  // TEST SHIT MÅSTE BORT SEN!

  public void sayHello() {
    System.out.println("Hello, this is Bank!");
  }

  public void testPrint() {
    System.out.println("Bank.testPrint() called in Bank!");
    main.setTestPic(new Card(Suite.HEARTS, Rank.ACE));
  }

}
