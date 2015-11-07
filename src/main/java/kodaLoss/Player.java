package kodaLoss;

import java.util.ArrayList;
import java.util.List;


public class Player {
  
  
  private String name; // alla spelare har ett namn
	
	private int playersCash = 100; // alla spelare startar med 100 dollar
	
	private int betForThisRound = 0 ; // vad spelare satsar denna runda
	
	private List<Card> hand = new ArrayList<Card>(); //Players hand

	// THIS IS NEW!
	private RoundResult roundResult;

	// TODO Do we need this???
	private boolean isBusted = false;
	

  // TODO do we need this?
	private boolean isActiveInRound = true; 

	
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
	 * 
	 *TODO Do we save played cards? Or do we just erase them? 
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
	public void setName(String name){
		this.name = name;
	}


	/**
	 * Sets the player to active (true) or inactive in this round of 
	 * the game!
	 * @param active: boolean (true = active)
	 */
	
	public void setPlayerActiveInRound( boolean active ){
	  this.isActiveInRound = active;
	}
	
	
	/**
	 * User makes a bet via the UI. This method checks if users player-object
	 * has enough cash to make that bet. Reduces players cash by the desired amount
	 * or with all of players cash if the desired amount exceeds players cash. 
	 * Moves players cash to the betting pot for this round. 
	 * @param requestedBet - amount user wants to bet
	 */
	public void setPlayersBet(int requestedBet){
	  
	  if (requestedBet > playersCash){
	    // just set bet to max bet! "ALL IN"!
	    betForThisRound = playersCash;
	    playersCash = 0;
	    
	  } else {
	   betForThisRound =requestedBet;
	   playersCash -= requestedBet;
	  }
	}
	

	/**
	 * returns the amount of cash this player has
	 * 
	 * @return players score
	 */
	public int getPlayersCash() {
		return this.playersCash;
	}

	/**
	 * returns the amount of money the player has bet in 
	 * this round
	 * @return amount of players bet in dollar
	 */
	public int getPlayersBet(){
	  return this.betForThisRound;
	}
	
	/**
	 * adds money to the players cash ( if he won any)
	 * @param amount
	 */
	public void addToPlayersCash(int amount) {
		this.playersCash += amount;
	}
	
	/**
	 * method to be called by bank for calculating money amount won  
	 * @return result of last played Round as enum type RoundResult
	 */
	public RoundResult getRoundResult() {
	  return roundResult;
	}
	
	/**
	 * method to be called by bank when calculating results of a round
	 * @param roundResult - set players result for the last round
	 */
	public void setRoundResult(RoundResult roundResult) {
	  this.roundResult = roundResult;
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
		
	
	/**
	 * returns true if the player is set to active in this round. else false;
	 * @return
	 */
	public boolean isActive(){
	  return this.isActiveInRound;
	}

}
