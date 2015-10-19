package testBlackJack;


import kodaLoss.Card;
import kodaLoss.CardShoe;
import kodaLoss.Deck;
import kodaLoss.Player;
import kodaLoss.Rank;
import kodaLoss.Suite;

import org.testng.Assert;
import org.testng.annotations.Test;

import junit.framework.TestCase;

public class testBJPlayerAndCard extends TestCase {

  
  @Test
  public void testCardValue(){
    Card card = new Card( Suite.CLUBS , Rank.JACK);
    Assert.assertTrue(card.getValue() == 10);
  }
  
  @Test
  public void testAddCardToHand(){
    
    Player player = new Player();
    Card card1 = new Card( Suite.CLUBS , Rank.JACK);
    
    for (int i = 0 ; i < 10 ; i++){
      player.addCardToHand(card1);
    }
    
    Assert.assertTrue(player.getPlayerHandsSize() == 10);
  }
  
  @Test
  public void testClearPlayersHand(){
    Player player = new Player();
    Card card1 = new Card( Suite.CLUBS , Rank.JACK);
    
    for (int i = 0 ; i < 10 ; i++){
      player.addCardToHand(card1);
    }
    if (player.getPlayerHandsSize() < 1 ){
      fail("Adding cards to hand failed!");
    }
    player.clearPlayersHand();
    Assert.assertTrue( player.getPlayerHandsSize() == 0 );
  }
  
  public void testIsBusted(){
    Player p = new Player();
    if (p.isBusted()){
      fail("Player initialized as isBusted!");
    }
    p.setBusted(true);
    Assert.assertTrue(p.isBusted());
  }

  public void testPlayerGetName(){
    Player p1 = new Player();
    Player p2 = new Player("TESTNAME");
    Assert.assertTrue(p1.getName().equals("Anonymous"));
    Assert.assertTrue(p2.getName().equals("TESTNAME"));
  }
  
  public void testCardShoe(){
	  CardShoe cS = new CardShoe();
	  Assert.assertTrue(cS.getCardShoe().size() == 208);
  }
  
  public void testDeck(){
	  
	  Assert.assertTrue(Deck.getDeck().size()==52);
  }
  
  
  
  
  
  
}
