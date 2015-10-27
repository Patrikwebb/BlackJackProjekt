package kodaLoss;

public class Round extends Bank {

	public Round() {
		playOneRound();
		// TODO GUI option play again?
	}

	public void playOneRound() {

		// deal a card to all players and dealer
		dealOneCardToAll();

		dealOneCardToAll();
		// TODO dealers other card to gui ska bli covered!

		// check if a player has a BlackJack from start!
		for (Player p : registeredPlayers) {
			if (isPlayersHandABlackJack(p)) {
				p.setPlayerActiveInRound(false);
				System.out.println("BlackJack");
			}
		}

		// each active player plays against bank

		for (Player p : registeredPlayers) {

			if (p.isActive()) {
				playerPlays(p);
				System.out.println(p.getPlayersHand());
			}
		}

		// dealer plays
		dealerPlays();

		// calculate winners
		calculateWinners();

	}

}
