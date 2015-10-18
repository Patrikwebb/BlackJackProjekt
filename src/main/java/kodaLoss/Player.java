package kodaLoss;

import java.util.ArrayList;
import java.util.List;

import gui.Main;

public class Player {

	private String name;
	private int score = 0;
	// private int cash = 100; // implemented in later sprints! Tim

	private List<Card> hand = new ArrayList<Card>();

	private boolean isBusted = false;
	private boolean isActive = false; // betyder att det ar spelarens tur

	// CONSTRUCTORS
	/**
	 * creates an new, anonymous player with default settings
	 */
	public Player() {
		name = "Anonymous";
	}

	public Player(String name) {
		this.name = name;
	};

	// INSTANCE METHODS
	private Main main;
	
	public Player(Main main) {
		this.main=main;
	}

	/**
	 * Player takes another card which is added to his hand
	 * 
	 * @param card
	 */
	public void addCardToHand(Card card) {
		this.hand.add(card);
	}

	/**
	 * clears the hand, removes all cards, after each round. !!! Assumes that
	 * played cards just are erased and not added to another container!!!
	 */
	public void clearPlayersHand() {
		this.hand.clear();
	}

	/**
	 * getter for the count of cards in players hand
	 * 
	 * @return number of cards in hand
	 */
	public int getPlayerHandsSize() {
		return hand.size();
	}

	/**
	 * getter for players hand
	 * 
	 * @return Players hand as a java.util.List<Card>
	 */
	public List<Card> getPlayersHand() {
		return hand;
	}

	/**
	 * prints out the hand of this player to console
	 */
	public void printHandToConsole() {
		System.out.print("Player " + this.getName() + ": ");
		for (Card c : hand) {
			System.out.print(c + ", ");
		}
		System.out.println();
	}

	// GETTERS AND SETTERS
	/**
	 * returns the name of this player as a String
	 * 
	 * @return name of player
	 */
	public String getName() {
		return this.name;
	}
	
	public String text(){
    	String text = "Dude";
    	return text;
    }

	/**
	 * returns the name of this players scorepoints
	 * 
	 * @return players score
	 */
	public int getPlayersScore() {
		return this.score;
	}

	/**
	 * sets the Players score
	 * 
	 * @param newScore
	 */
	public void setPlayersScore(int newScore) {
		this.score = newScore;
	}

	/**
	 * getter to see if this player is busted, hand > 21 points!
	 * 
	 * @return true if player is busted
	 */
	public boolean isBusted() {
		return isBusted;
	}

	/**
	 * sets the player to busted, hand > 21 points, the bank decides!
	 * 
	 * @param isBusted
	 */
	public void setBusted(boolean isBusted) {
		this.isBusted = isBusted;
	}

}
