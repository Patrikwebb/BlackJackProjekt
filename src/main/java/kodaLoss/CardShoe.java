package kodaLoss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * En ArrayList med cards från 4 decks som är Shuffeled!
 * getCardShoe!
 * @author johan
 *
 */

public class CardShoe {
  
private List<Card> cards;
  

public CardShoe(){
  cards = getCardShoe();
}

	public static ArrayList<Card> getCardShoe() {
		
	  ArrayList<Card> cardShoe = new ArrayList<>();
		final int numberOfCards = 208;
		
		int count = 1;
		while (count <= numberOfCards) {
			for (Card c : Deck.getDeck()) {
				cardShoe.add(c);
				count++;
			}
		}
		Collections.shuffle(cardShoe);
		return cardShoe;
	}
	
	/**
	 * Returns the first card from CardShoe in Bank. Gets new Cards
	 * automatically if cardshoe is empty. 
	 * @return Card - first card from Cardshoe
	 */
	public Card getACardFromCardShoe(){
	  
	  if ( !(cards.size() > 0)){
	    this.cards = getCardShoe();
	  }
	  
	  return cards.remove(0); 
	}
	
	
	
	
	
	
	
	
	
}