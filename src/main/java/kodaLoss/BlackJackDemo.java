package kodaLoss;

import java.util.ArrayList;

// import gui.Main;

public class BlackJackDemo {

	public static void main(String[] args) {
		
		// Main guiMain = new Main();
		// Använda Gui.Main klassen som Demo direkt?
		CardShoe cs = new CardShoe();
		ArrayList<Card> theCs= cs.getCardShoe();
		System.out.println(theCs);
		boolean test = theCs.size() == 208;
		System.out.println(theCs.size());
		System.out.println(test);
	}
	
	

}
