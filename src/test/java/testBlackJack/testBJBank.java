package testBlackJack;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import kodaLoss.Bank;
import kodaLoss.Bank_HelpersAndTools;
import kodaLoss.Card;
import kodaLoss.Deck;
import kodaLoss.Player;
import kodaLoss.Rank;
import kodaLoss.Suite;
import kodaLoss.UserChoiceAdapter;
import kodaLoss.UserChoiceAdapter.UserChoice;
import junit.framework.TestCase;

public class testBJBank extends TestCase {

  @Test 
  public void testCalculateValueOfPlayersHand_4Aces(){
    Player p = new Player();
    p.addCardToHand(new Card(Suite.CLUBS , Rank.ACE));
    p.addCardToHand(new Card(Suite.CLUBS , Rank.ACE));
    p.addCardToHand(new Card(Suite.CLUBS , Rank.ACE));
    p.addCardToHand(new Card(Suite.CLUBS , Rank.ACE));
    int valueOfHand = Bank_HelpersAndTools.calculateValueOfPlayersHand(p);
    // value = 11 + 1 + 1 + 1 = 14
    Assert.assertTrue(valueOfHand == 14);
  }
  
  @Test
  public void testCalculateValueOfPlayersHand_ACEAnd10(){
    Player p = new Player();
    p.addCardToHand(new Card(Suite.HEARTS , Rank.TEN));
    p.addCardToHand(new Card(Suite.CLUBS , Rank.ACE));
    int valueOfHand = Bank_HelpersAndTools.calculateValueOfPlayersHand(p);
    Assert.assertTrue(valueOfHand == 21);
  }
  
  @Test
  public void testIsBlackJack(){
    Player p = new Player();
    p.addCardToHand(new Card(Suite.SPADES , Rank.TEN));
    p.addCardToHand(new Card(Suite.DIAMONDS , Rank.ACE));
    Assert.assertTrue(Bank_HelpersAndTools.isPlayersHandABlackJack(p));
    
    p.clearPlayersHand();
    p.addCardToHand(new Card(Suite.HEARTS , Rank.KING));
    Assert.assertFalse(Bank_HelpersAndTools.isPlayersHandABlackJack(p));
    
    p.addCardToHand(new Card(Suite.CLUBS , Rank.FOUR));
    p.addCardToHand(new Card(Suite.SPADES, Rank.SEVEN));
    Assert.assertFalse(Bank_HelpersAndTools.isPlayersHandABlackJack(p));
  }
  
  @Test
  public void testDeckHas52Cards() {
    
    Deck deck = new Deck();
    Assert.assertTrue(deck.getDeck().size() == 52);
  }

  @Test
  public void testDeckContainsAllCards() {

    Deck deck = new Deck();
    List<Card> cards = deck.getDeck();
    Card testCard;
    boolean cardFound;

    for (Suite suite : Suite.values()) {
      for (Rank rank : Rank.values()) {
        testCard = new Card(suite, rank);
        cardFound = false;

        for (Card deckCard : cards) {
          if (testCard.equals(deckCard)) {
            cardFound = true;
            break;
          }
        }

        Assert.assertTrue(cardFound);
      }
    }
  }

  @Test
  public void testDeckHasNoDoubleCards() {

    Deck deck = new Deck();
    List<Card> cards = deck.getDeck();

    Assert.assertFalse(hasDoubleCards(cards));
  }

  @Test
  public void testDeckHasDoubleCards() {
    Deck deck = new Deck();
    List<Card> cards = deck.getDeck();
    Card extraCard = new Card(Suite.SPADES, Rank.KING);
    cards.add(extraCard);

    Assert.assertTrue(hasDoubleCards(cards));
  }

  // help function that checks if deck contains double cards,
  // returns true if at least on pair of cards is found!
  private boolean hasDoubleCards(final List<Card> cards) {
    Card rmvCard;
    boolean doubleFound = false;

    mainloop: for (int i = 0; i < cards.size(); i++) {
      doubleFound = false;
      rmvCard = cards.remove(i);

      for (Card c : cards) {
        if (c.equals(rmvCard)) {
          doubleFound = true;
          break mainloop;
        }
      }
    }
    return doubleFound;
  }


  @Test
  public void testCardEquals(){
    Card card1 = new Card(Suite.CLUBS , Rank.ACE);
    Card card1b = card1;
    Card card2 = new Card(Suite.CLUBS , Rank.ACE);
    Card cardDiff = new Card(Suite.HEARTS , Rank.EIGHT);
    
    Assert.assertTrue(card1.equals(card2));
    Assert.assertTrue(card1.equals(card1b));
    Assert.assertFalse(card1 == cardDiff); 
  }

  @Test
  public void testCardHashcode(){
    
    Card card1 = new Card(Suite.CLUBS , Rank.ACE);
    Card card2 = new Card(Suite.CLUBS , Rank.ACE);
    Assert.assertTrue(card1.hashCode() == card2.hashCode());
  }

  

  
  @Test
  public void testUserChoiceAdapterGetters(){
    
    UserChoiceAdapter.getInstance().playerChoosesToHit();
    Assert.assertTrue (UserChoiceAdapter.getInstance().getUserChoice() == UserChoice.HIT);
    Assert.assertFalse(UserChoiceAdapter.getInstance().getUserChoice() != UserChoice.HIT);
    
  }
  
  
}
