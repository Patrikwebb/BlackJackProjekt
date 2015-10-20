package kodaLoss;


public class Card {

	private final Suite suite;
	private final Rank rank;
	private final int value;
	
	public Card(Suite s, Rank r) {
		suite = s;
		rank = r;
		this.value = rank.getValue();
	}

	/**
	 * getter for the Suite of this card
	 * 
	 * @return Suite of card
	 */
	public Suite getSuite() {
		return suite;
	}

	/**
	 * getter for the Rank of this card.
	 * 
	 * @return Rank of card
	 */
	public Rank getRank() {
		return rank;
	}

	/**
	 * returns the value points of this card. ACE = 11 !
	 * 
	 * @return int value
	 */
	public int getValue() {
		return value;
	}

	@Override
	/** 
	 * returns true if card to compare with has same Suite and same Rank as
	 *  this card. 
	 */
	public boolean equals(Object o){
	  
	  if (o == this){
	    return true;
	  }
	  
	  if ( !(o instanceof Card) ){
	    return false;
	  }
	  
	  Card card = (Card)o;
	  
	  return card.getRank() == this.getRank() &&
	      card.getSuite() == this.getSuite();
	}


	@Override 
	public int hashCode(){
	  
	  int result = 17;
	  result = 32 * result + this.rank.hashCode();
	  result = 32 * result + this.suite.hashCode();
	  return result;
	}
	
	
	@Override
	public final String toString() {
		return suite + "_" + rank;
	}

}
