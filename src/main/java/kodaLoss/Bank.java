package kodaLoss;

import java.util.ArrayList;
import java.util.List;
import static kodaLoss.UserChoiceAdapter.*;

import gui.Main;

public class Bank {

	// MEMBERS
	private Main main = null; // reference to gui

	public List<Player> registeredPlayers = new ArrayList<Player>();
	private Player dealer = new Player("Dealer");
	private CardShoe cardShoe = new CardShoe();
	private boolean dealerIsBust = true;

	// CONSTRUCTORS
	public Bank() {
	}

	public Bank(Main main) {
		this.main = main;
		
		// TEST OCH DEMO 
		
		registeredPlayers.add(new Player("Peter"));
		
		
		
		
	}

	// METHODS FOR GUI

	/**
	 * sets the reference to the gui that this Bank object holds
	 * 
	 * @param main
	 */
	public void setReferenceToGui(Main main) {
		this.main = main;
	}

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

	/*
	 * methods that controls the sequence of actions to play one round, control
	 * of the gameplay!
	 */
	public void playOneRound() {

		// deal a card to all players and dealer
		dealOneCardToAll();
		

		dealOneCardToAll();
		// TODO dealers other card to gui ska bli covered!

		// check if a player has a BlackJack from start!
		for (Player p : registeredPlayers) {
			if (isPlayersHandABlackJack(p)) {
				p.setPlayerActiveInRound(false);
			}
		}

		// each active player plays against bank

		for (Player p : registeredPlayers) {

			if (p.isActive()) {
				playerPlays(p);
			}
		}

		// dealer plays
		dealerPlays();

		/*
		 * if dealer is not bust => player who are not bust, and have a higher
		 * hand than dealer. if dealer is bust => all players that are not bust
		 * win
		 * 
		 */

		calculateWinners();
	}

	/*
	 * Player plays against Bank in one round. Sets player inactive if bust.
	 * Uses class UserChoiceAdapter to get user events from the user interface
	 */
	protected void playerPlays(Player player) {

		UserChoiceAdapter.resetUserChoice(); // prepare for input

		while (getUserChoice() != UserChoice.STAY) {

			player.printHandToConsole();

			if (getUserChoice() == UserChoice.HIT) {
				dealOneCardToPlayer(player);
				// main.setTestPic(player.getPlayersHand().get(player.getPlayerHandsSize()
				// - 1 ));
				System.out.println("PLAYER HIT");
				player.printHandToConsole();
				resetUserChoice();

				if (isPlayersHandOver21(player)) {
					player.setPlayerActiveInRound(false);
					player.setBusted(true);
					System.out.println("PLAYER IS BUST NOW!");
					player.printHandToConsole();
					break;
				}
			}
		}
		// finally
		resetUserChoice();
	}

	/**
	 * Setter och Getter to dealerIsBust =>
	 * 
	 * @return the dealerIsBust
	 */
	public boolean isDealerIsBust() {
		return dealerIsBust;
	}

	/**
	 * @param dealerIsBust
	 *            the dealerIsBust to set
	 */
	public void setDealerIsBust(boolean dealerIsBust) {
		this.dealerIsBust = dealerIsBust;
	}

	/*
	 * dealer plays. Takes cards until its hand is over 16
	 */
	protected void dealerPlays() {
		while (calculateValueOfPlayersHand(dealer) < 17) {
			// has to be refactorized!? Method "deal out a card"?!?
			dealer.addCardToHand(this.cardShoe.getACardFromCardShoe());
			setDealerIsBust(false);

			dealer.printHandToConsole();
			if (isPlayersHandOver21(dealer)) {
				//temporary until we send to GUI
				System.out.println("DEALER IS BUST!");
				setDealerIsBust(true);
			}else{
				//temporary until we send to GUI
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
		//main.ShowDealersHand(dealer.getPlayersHand());
	}

	/*
	 * Calculate winners TODO update GUI who won
	 */
	//
	public void calculateWinners() {
		int playerpoints;
		if (dealerIsBust) {
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
				if (playerpoints <= 21 && playerpoints > calculateValueOfPlayersHand(dealer)) {
					// TO GUI Player WINNS
					System.out.println("Congratulations! You won.");
				} else {
					// To Gui YOU lost!
					System.out.println("Sorry, you lost.");
				}
			}
		}
	}

	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////
	// TEST SHIT MÅSTE BORT SEN!
	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////
	public void sayHello() {
		System.out.println("Hello, this is Bank!");
	}

	public void testPrint() {
		System.out.println("Bank.testPrint() called in Bank!");
		main.setTestPic(new Card(Suite.SPADES, Rank.SEVEN));
	}

	// Testspelar en runda! med en player som hela tiden bara
	// vill ha ett nytt kort!

	public static void main(String[] args) {

		System.out.println("now the Thread");
		Thread Clicker = new Thread() {

			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
						UserChoiceAdapter.playerChoosesToHit();
					} catch (InterruptedException e) {
						System.out.println("Thread misslyckade");
					}
				}
			}
		};
		Clicker.start();

		Bank bank = new Bank();
		Player p = new Player("TEST");
		Player p2 = new Player("PATRIK");
		bank.registeredPlayers.add(p);
		bank.registeredPlayers.add(p2);

		System.out.println("Now to the bank");
		bank.playOneRound();
	}

}
