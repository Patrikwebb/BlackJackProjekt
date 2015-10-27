package kodaLoss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * En ArrayList med cards från 4 decks som är Shuffeled! getCardShoe!
 * 
 * @author johan
 *
 */
public class CardShoe {

	private List<Card> cards;

	public CardShoe() {
		cards = getNewCards();
	}

	public ArrayList<Card> getNewCards() {

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
	 * automatically if cardshoe is empty. No info is given to the players that
	 * a new card shoe is used!!!
	 * 
	 * @return Card - first card from Cardshoe
	 */
	public Card getACardFromCardShoe() {

		if (!(cards.size() > 0)) {
			this.cards = getNewCards();
		}
		return cards.remove(0);
	}
}