package kodaLoss;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Player {
  
  
  private String name = "Anonymous"; // alla spelare har ett namn
	
	private int playersCash = 100; // alla spelare startar med 100 dollar
	


	private int betForThisRound = 0 ; // vad spelare satsar denna runda
	
	private List<Card> hand = new ArrayList<Card>(); //Players hand

	// THIS IS NEW!
	private RoundResult roundResult;
	
	// CONSTRUCTORS
	/**
	 * creates an new, anonymous player with default settings
	 */
	public Player() {
	}
	
	/**
	 * Constructor to player, starts with name and playersCash
	 * @param name
	 * @param playersCash
	 */
	public Player(String name, int playersCash) {
		this.playersCash = playersCash;
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
	
	/**
	 * User makes a bet via the UI. This method checks if users player-object
	 * has enough cash to make that bet. Reduces players cash by the desired amount
	 * or with all of players cash if the desired amount exceeds players cash. 
	 * Moves players cash to the betting pot for this round. 
	 * @param requestedBet - amount user wants to bet
	 */
	public void setPlayersBet(int requestedBet){
	  
	  if (requestedBet >= playersCash){
	    // just set bet to max bet! "ALL IN"!
	    betForThisRound = playersCash;
	    playersCash = 0;
	    
	  } else {
	   betForThisRound =requestedBet;
	   playersCash -= requestedBet;
	  }
	}
	

	// INSTANCE METHODS
	
	/**
	 * set the players Cash amount
	 * @param playersCash
	 */
  public void setPlayersCash(int playersCash) {
    this.playersCash = playersCash;
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
	
	@Override
	public String toString(){
	  return String.format("Player %s,Cash: %d, Bet: %d%nPlayers hand: %s ", 
	      this.getName() ,
	      this.getPlayersCash() ,
	      this.getPlayersBet(), 
	      Arrays.deepToString(this.getPlayersHand().toArray()) );
	}
	
}
