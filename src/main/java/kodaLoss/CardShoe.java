package kodaLoss;

import java.util.ArrayList;
import java.util.Collections;

/**
 * En ArrayList med cards från 4 decks som är Shuffeled!
 * getCardShoe!
 * @author johan
 *
 */

public class CardShoe {
private static int numberOfCards = 208;
	public static ArrayList<Card> getCardShoe() {
		ArrayList<Card> cardShoe = new ArrayList<>();
		
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
}