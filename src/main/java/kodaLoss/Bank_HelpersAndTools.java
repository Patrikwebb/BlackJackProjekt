package kodaLoss;

public class Bank_HelpersAndTools {

  /**
   * calculates value of players hand. Sets value of Aces in players hand to 1
   * if players should have gone bust otherwise until players hand is under 21
   * again.
   * 
   * @return value of players hand as an integer
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
  
  /**
   * Check if any player got an ACE on there hand.
   * @param player
   * @return true / false
   */
  public static boolean checkForAceCardOnYourHand(Player player){
	  
	  for (Card card : player.getPlayersHand()) {
	      if (card.getRank() == Rank.ACE) 
	        return true;
	  }
	  return false;
  }

  /**
   * checks if players hand is a BlackJack!
   * 
   * @param player
   * @return true if Player has a BlackJack
   */
  public static boolean isPlayersHandABlackJack(Player player) {
    return (player.getPlayerHandsSize() == 2 && calculateValueOfPlayersHand(player) == 21);
  }

  /**
   * checks if a players hands value is over 21, even considering the aces
   * rule (Ace value = 1)
   * 
   * @param player
   * @return true if players hand is over 21, else false
   */
  public static boolean isPlayersHandOver21(Player player) {
    return (calculateValueOfPlayersHand(player) > 21);
  }
}
