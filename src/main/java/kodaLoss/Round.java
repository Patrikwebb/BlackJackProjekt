package kodaLoss;


import kodaLoss.Bank;
import kodaLoss.Bank_HelpersAndTools;

public class Round {

	// a round plays in its own thread for GUI-responsivity
	private Thread roundThread = null;

	public Round() {
		playOneRound();
		// TODO GUI option play again?
	}

	public void playOneRound() {

		if (roundThread == null || !roundThread.isAlive()) {

			roundThread = new Thread(new Runnable() {

				@Override
				public void run() {

					System.out.println("Round started...");

					// TODO HIT, STAY = enable PLAY = disable
					
					Bank.controller.gameIson();

					// deal a card to all players and dealer
					Bank.getInstance().dealOneCardToAll();

					Bank.getInstance().dealOneCardToAll();
					// TODO dealers other card to gui ska bli covered!

					// check if a player has a BlackJack from start!
					hasBlackJack();

					// each active player plays against bank
					play();

					// dealer plays
					Bank.getInstance().dealerPlays();

					// calculate winners
					Bank.getInstance().calculateWinners();

					Bank.getInstance();
					// TODO HIT, STAY = disable PLAY = enable
					Bank.controller.gameIsoff();
					// reset Thread and Players for next round!
					// roundThread = null;

				}
			});
			roundThread.start();

		} else {
			// roundThread is still alive!
			System.out.println("Already running a round");
		}
	}

	
	/**
	 * All players play their rounds before dealer
	 */
	private void play() {
		for (Player p : Bank.getInstance().registeredPlayers) {

			if (p.isActive()) {
				Bank.getInstance().playerPlays(p);
				System.out.println(p.getPlayersHand());
			}
		}
	}
	/**
	 * Check for Black Jack before players play
	 */
	private void hasBlackJack() { // set method after p has two cards
		for (Player p : Bank.getInstance().registeredPlayers) {
			if (Bank_HelpersAndTools.isPlayersHandABlackJack(p)) {
				p.setPlayerActiveInRound(false);
				System.out.println("BlackJack");
			}
		}
	}
}
