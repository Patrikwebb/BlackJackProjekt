package kodaLoss;

import java.util.ArrayList;
import java.util.List;

import gui.Main;

public class Bank {
	
	private Main main = null;
	
	
	public Bank(){
		
	}
	
	public Bank(Main main){
		this.main=main;
	}

	private List<Player> registeredPlayers = new ArrayList<Player>();

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
	
	public void setMain(Main main)
	{
		this.main = main;
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
	
	public void round(){
		registeredPlayers.get(0);
		
	}

	
	public void sayHello(){
		System.out.println("Hello, this is Bank!");
	}

	public void testPrint() {
		System.out.println("Main.testPrint() called in Main!");
		
	}
	
	
}
