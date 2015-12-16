package kodaLoss;

import static kodaLoss.Bank_HelpersAndTools.isPlayersHandABlackJack;
import static kodaLoss.Bank_HelpersAndTools.isPlayersHandOver21;
import static kodaLoss.BlackJackConstantsAndTools.PLAYER_IS_BUST;
import kodaLoss.Player;
import java.util.ArrayList;
import java.util.List;

import kodaLoss.UserChoiceAdapter.UserChoice;

public class CasinoRound extends AbstractRound {

	// list to save Split-players for deletion at the end of round!
	private List<Player> SplitPlayerToDelete = new ArrayList<>();

	// Constructor
	public CasinoRound() {
		super();
		System.out.println("CasinoRound");
		controller.activateAdvancedButton();
	}

	@Override
	public void playerPlays(Player player) {

		System.out.println("Player plays - Casino rules - started...");

		// check for casino rules before playing!
		// just one casino rule per round, the other will be deactivated
		// after playing one of them!

		if (bank.indexOfPlayer(player) <= 4) {
			activateSplit(player);
			activateInsurance(player);
			activateDouble(player);

		} else {
			controller.disableAdvancedButton();
			controller.allButtonsOff();
			controller.setlabelWinnerText(BlackJackConstantsAndTools.SPLIT_TEXT_TO_SPLITPLAYER);
			//BlackJackConstantsAndTools.sleepForXSeconds();
		}
		// activate players buttons
		controller.gameIson();
		uca.resetUserChoice(); // prepare UCA for input

		while (uca.getUserChoice() != UserChoice.STAY) {
			controller.setlabelWinnerText("Play NOW: "+ player.getName());
			bank.updateGuiAfterChangeInDataModel();
			if (isPlayersHandOver21(player)) {
				System.out.println(PLAYER_IS_BUST);
				controller.setlabelWinnerText(player.getName() + ": " + PLAYER_IS_BUST);
				bank.updateGuiAfterChangeInDataModel();
				break;
			}
			// sleep to reduce processor load!
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
				// if(checkIfSplitCanBePlayed(player)){
				// makeSplitPlayer(player);
				// controller.setlabelWinnerText(
				// BlackJackConstantsAndTools.SPLIT_TEXT_TO_PLAYER);
				//
				// }else{
				// controller.setlabelWinnerText(
				// BlackJackConstantsAndTools.NOT_ENOUGH_CASH_TO_SPLIT);
				// }
				makeSplitPlayer(player);
				bank.updateGuiAfterChangeInDataModel();
				controller.setlabelWinnerText("Play NOW: "+ player.getName());
				bank.updateGuiAfterChangeInDataModel();
				//controller.disableAdvancedButton();
				uca.resetUserChoice();
			} else if (uca.getUserChoice() == UserChoice.DOUBLE) {
				playDouble(player);
				break; // break out of loop, round is over for player!

			} else if (uca.getUserChoice() == UserChoice.INSURANCE) {
				playInsurance(player);
				controller.setlabelWinnerText(BlackJackConstantsAndTools.INSURANCE_TEXT_TO_PLAYER);
				bank.updateGuiAfterChangeInDataModel();
				controller.disableAdvancedButton();
				uca.resetUserChoice();
			}
			bank.updateGuiAfterChangeInDataModel();
		}
		// print out all data of Player!
		System.out.println(player.toString());
		
		// finally reset last choice in UCA
		uca.resetUserChoice();
	}

	// activate double button if players hand allow playing Double
	private void activateDouble(Player player) {

		if (checkIfDoubleCanBePlayed(player)) {
			controller.activateDoubleButton();
		}
	}

	/**
	 * Check if the dealer got an ACE on the first DEAL, And activates the
	 * button so the player can choice to use Insurance
	 */
	public void activateInsurance(Player p) {

		if (checkIfInsuranceCanBePlayed(p)) {

			controller.activateInsuranceButton();
		} else {
			// SHOULD NEVER HAPPEN!
			if (p.getPlayersCash() < (int) Math.ceil(p.getPlayersBet() / 2.0d)) {
				controller.setlabelWinnerText(BlackJackConstantsAndTools.NOT_ENOUGH_CASH_TO_TAKE_INSURANCE);
			}
		}
	}

	// activate splitbutton if player may play Split
	public void activateSplit(Player p) {
		//TILLFÄLLIG override för att funka utan att checkIf.. blir activerad
		controller.activateSplitButton();

//		if (true) {
//			checkIfSplitCanBePlayed(p);
//			controller.activateSplitButton();
//		}
	}

	// returns true if a player may play Double
	private boolean checkIfDoubleCanBePlayed(Player player) {
		return player.getPlayersCash() >= bank.registeredPlayers.get(0).getPlayersBet()
				&& player.getPlayersHand().size() <= 2;
	}

	// returns true if player may play Split (2 cards of same value, even
	// different
	// Ranks!) AND if players bet is smaller than player index 0 cash
	public boolean checkIfSplitCanBePlayed(Player p) {

		if (p.getPlayersHand().size() != 2) {
			return false;

		} else if (p.getPlayersBet() > bank.registeredPlayers.get(0).getPlayersCash()) {
			return false;

		} else {
			final Card cardOne = p.getPlayersHand().get(0);
			final Card cardTwo = p.getPlayersHand().get(1);
			System.out.println(cardOne.getValue() == cardTwo.getValue());
			return (cardOne.getValue() == cardTwo.getValue());
		}
	}

	// returns true if player may buy an insurance
	public boolean checkIfInsuranceCanBePlayed(Player p) {

		return true;
		// return Bank_HelpersAndTools.checkForAceCardOnYourHand(bank.dealer)
		// && p.getPlayersCash() * 2 >=
		// bank.registeredPlayers.get(0).getPlayersBet();
	}

	// player plays Double
	public void playDouble(Player p) {

		doublePlayersBet(p);
		bank.dealOneCardToPlayer(p);
		bank.updateGuiAfterChangeInDataModel();
		System.out.println("PLAYER DOUBLE");
	}

	/*
	 * Doubles players bet for this round, if player has the cash! Otherwise
	 * just leaves the bet as it is. Should be called after checking if playing
	 * double is legal!
	 * 
	 * THROWS: IllegalArgumentException if doubled bet would exceed players
	 * cash!
	 */
	private void doublePlayersBet(Player p) {
		final int playersBet = p.getPlayersBet();

		if (p.getPlayersCash() >= playersBet) {
			p.addToPlayersCash(playersBet);
			p.setPlayersBet((int) Math.floor(2.0 * playersBet));

		} else {
			controller.setlabelWinnerText(BlackJackConstantsAndTools.NOT_ENOUGH_CASH_TO_DOUBLE);
			throw new IllegalArgumentException("doubled bet exceeds Players cash!");
		}
	}

	// adds an insurance to player and adjusts money
	private void playInsurance(Player p) {
		p.setHasInsurance(true);
		int insurance = (int) Math.ceil(p.getPlayersBet() / 2.0d);
		p.setPlayersCash(p.getPlayersCash() - insurance);

	}

	// makes a SPLIT_Player to player
	public void makeSplitPlayer(Player player) {

		Player splitPlayer = new Player("SPLIT_" + player.getName(), 0);
		splitPlayer.setSplitPlayer(true);
		// controller.updateSplitPlayer(splitPlayer);

		// take a new bet for splitplayer!
		final int bet = player.getPlayersBet();
		splitPlayer.setPlayersBet(bet);
		player.setPlayersBet(0);
		player.setPlayersBet(bet);

		splitPlayer.addCardToHand(player.getPlayersHand().remove(1));
		SplitPlayerToDelete.add(splitPlayer);
		controller.updatePlayer(splitPlayer);
		bank.updateGuiAfterChangeInDataModel();

		bank.dealOneCardToPlayer(player);
		BlackJackConstantsAndTools.sleepForXSeconds();
		bank.dealOneCardToPlayer(splitPlayer);
		controller.updatePlayer(splitPlayer);
		
		//Addas denna någon annanstans?
		bank.registeredPlayers.add(splitPlayer);
//		for (int i = 0; i < bank.registeredPlayers.size(); i++) {
//
//			if (bank.registeredPlayers.get(i) == player) {
//				bank.registeredPlayers.add(i + 1, splitPlayer);
//				System.out.println();
//				break;
//			}
//		}

		bank.updateGuiAfterChangeInDataModel();
	}

	// add Splitplayers money to players at the end of round
	private void mergeSplitPlayers() {
		bank.activePlayerOnGui = bank.registeredPlayers.get(0);

		for (int i = 1; i < bank.registeredPlayers.size(); i++) {
			Player p = bank.registeredPlayers.get(i);

			if (p.isSplitPlayer()) {
				// Player addToPlayer = bank.registeredPlayers.get(i - 1);
				// addToPlayer.addToPlayersCash(p.getPlayersCash());
				bank.registeredPlayers.get(0).addToPlayersCash(p.getPlayersCash());
			}
		}
		bank.updateGuiAfterChangeInDataModel();
	}

	/*
	 * delete splitplayers at the end of round! both from registeredPlayers and
	 * SplitPlayerToDelete
	 */
	public void deleteSplitPlayers() {
		// for (Player p : SplitPlayerToDelete) {
		//
		// if (bank.registeredPlayers.contains(p)) {
		// System.out.println("removed : " + p.getName());
		// bank.registeredPlayers.remove(p);
		// }
		// }
		bank.registeredPlayers.removeAll(SplitPlayerToDelete);
		SplitPlayerToDelete.removeAll(SplitPlayerToDelete);
		System.out.println("REG p " + bank.registeredPlayers.size());
		System.out.println("SPLIT p " + SplitPlayerToDelete.size());
		bank.updateGuiAfterChangeInDataModel();
	}

	@Override
	public void cleanUpAfterRound() {

		clearHandsOffTheTable();
		mergeSplitPlayers();
		deleteSplitPlayers();
		bank.updateGuiAfterChangeInDataModel();
	}

	@Override
	public void handlePlayersBetsAndPayWinners() {
		/*
		 * WITH CASINO RULES! TIE => return betting amount WIN => return 2 * bet
		 * WIN + BJ => return 2.5 bet Loose => players bet is not returned
		 * INSURANCE 3:2 if dealer has a BlackJAck, return bet if dealer wins
		 */
		int playersBet;
		int playersBalance = 0;

		for (Player player : bank.registeredPlayers) {

			playersBet = player.getPlayersBet();

			if (playersBet == 0) {
				System.out.println("NOT BET FOR : " + player.getName());
			}

			if (player.getRoundResult() == RoundResult.TIE) {
				controller.setlabelWinnerText(player.getName() + RoundResult.TIE);
				playersBalance = playersBet;

			} else if (player.getRoundResult() == RoundResult.WIN) {

				playersBalance = (int) Math.floor(playersBet * 2.0d);

				// Win with Black Jack adds another 50% of bet!
				if (isPlayersHandABlackJack(player)) {
					playersBalance += (int) Math.floor(playersBet / 2.0d);
					System.out.println("BLACKJACK");
				}
			} else if (player.getRoundResult() == RoundResult.LOOSE) {
				// insured against loss => return bet (2 * 1/2 bet)
				// Dealer has a BlackJAck => return 3:2 of bet!

				if (player.isHasInsurance()) {
					System.out.println("ENTERED HAS INSURANCE IN ROUND RESULT");
					if (isPlayersHandABlackJack(bank.dealer)) {
						playersBalance += (int) Math.floor(1.5 * player.getPlayersBet());
					} else {
						playersBalance = player.getPlayersBet();
					}

				} else {
					playersBalance = 0;
				}
			}
			// last rounds Players bet as default in gui for next round
			// player.setPlayersBet(0);
			controller.setlabelWinnerText(String.format("%s $: + %d", player.getName(), playersBalance));
			player.addToPlayersCash(playersBalance);
			player.setRoundResult(null);
			player.setHasInsurance(false);
			bank.updateGuiAfterChangeInDataModel();
			//BlackJackConstantsAndTools.sleepForXSeconds();
		}
	}
}
