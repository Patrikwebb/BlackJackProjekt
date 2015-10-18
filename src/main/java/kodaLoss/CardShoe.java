package kodaLoss;

import java.util.ArrayList;
import java.util.Collections;

/**
 * En ArrayList med cards från 4 decks som är Shuffeled!
 * getCardShoe!
 * @author johan
 *
 */

public class CardShoe extends Deck {

	public ArrayList<Card> getCardShoe() {
		ArrayList<Card> cardShoe = new ArrayList<>();
		int numberOfCards = 208;
		int count = 1;
		while (count <= numberOfCards) {
			for (Card c : getDeck()) {
				cardShoe.add(c);
				count++;
			}
		}
		Collections.shuffle(cardShoe);
		return cardShoe;
	}
}