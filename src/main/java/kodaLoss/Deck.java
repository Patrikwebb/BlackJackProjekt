package kodaLoss;

import java.util.ArrayList;

public class Deck { // Deck blir en arrayList med 52 kort.

	public ArrayList<Card> getDeck() {
		ArrayList<Card> deck = new ArrayList<>();

		for (Suite s : Suite.values()) {
			for (Rank r : Rank.values()) {
				deck.add(new Card(s,r));
			}
		}
		return deck;
	}
}
