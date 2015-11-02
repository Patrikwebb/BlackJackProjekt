package kodaLoss;

import java.util.ArrayList;
import java.util.List;
import static kodaLoss.UserChoiceAdapter.*;


import static kodaLoss.Bank_HelpersAndTools.*;

public class Bank {

	// MEMBERS
	
  
  private UserChoiceAdapter uca = UserChoiceAdapter.getInstance();
  
  private Controller controller;
  
  

	public List<Player> registeredPlayers = new ArrayList<Player>();
	private Player dealer = new Player("Dealer");
	private Player activePlayerOnGui;
	
	private CardShoe cardShoe = new CardShoe();
//	private boolean dealerIsBust = true;

	// CONSTRUCTORS
	public Bank() {
	  System.out.println("Bank started!");
	  
	}


	// METHODS TO REGISTER CONTROLLER
	
	public void registerController(Controller cont){
	  this.controller = cont;
	}
	
	
	// METHODS FOR GUI

	/* called after changes in player or dealer model. Updates the gui by
	 * directly calling methods in Controller. 
	 */
	
	private void updateGuiAfterChangeInDataModel(){
	  
	  controller.updatePlayer( activePlayerOnGui);
	  controller.updateDealer( dealer );
	}
	
	
	
	/*
	 * methods that controls the sequence of actions to play one round, control
	 * of the gameplay!
	 */
	public void playOneRound() {

		// deal a card to all players and dealer
		dealOneCardToAll();

		updateGuiAfterChangeInDataModel();
	
		dealOneCardToAll();
		updateGuiAfterChangeInDataModel();
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

		UserChoiceAdapter.getInstance().resetUserChoice(); // prepare for input

		while (getUserChoice() != UserChoice.STAY) {

			player.printHandToConsole();

			if (getUserChoice() == UserChoice.HIT) {
				dealOneCardToPlayer(player);
				// main.setTestPic(player.getPlayersHand().get(player.getPlayerHandsSize()
				// - 1 ));
				System.out.println("PLAYER HIT");
				player.printHandToConsole();
				UserChoiceAdapter.getInstance().resetUserChoice();

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

//			dealer.printHandToConsole();
			
			if (isPlayersHandOver21(dealer)) {
				//temporary until we send to GUI
				System.out.println("DEALER IS BUST!");
				setPlayerToBust(dealer, true);
				updateGuiAfterChangeInDataModel();

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



	public static void main(String[] args) {

		System.out.println("now the Thread");
		Thread Clicker = new Thread() {

			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
						UserChoiceAdapter.getInstance().playerChoosesToHit();
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
