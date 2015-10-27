package testBlackJack;


import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import junit.framework.TestCase;
import junit.framework.*;
import kodaLoss.Bank;
import kodaLoss.Card;
import kodaLoss.CardShoe;
import kodaLoss.Deck;
import kodaLoss.Player;
import kodaLoss.Rank;
import kodaLoss.Suite;

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
  
  @Test
  public void testIsBusted(){
    Player p = new Player();
    if (p.isBusted()){
      fail("Player initialized as isBusted!");
    }
    p.setBusted(true);
    Assert.assertTrue(p.isBusted());
  }

  @Test
  public void testPlayerGetName(){
    Player p1 = new Player();
    Player p2 = new Player("TESTNAME");
    Assert.assertTrue(p1.getName().equals("Anonymous"));
    Assert.assertTrue(p2.getName().equals("TESTNAME"));
  }
  
  @Test
  public void testCardShoe(){
	  CardShoe cS = new CardShoe();
	  Assert.assertTrue(cS.getNewCards().size() == 208);
  }
  
  @Test
  public void testCardShoeHas4CompleteDecks(){
    
    List<Card> cardShoe = new CardShoe().getNewCards();
    // using hashmap as an unsorted map!
    Map<Card,Integer> countMap = new HashMap<Card,Integer>();
    
    for ( Card card : cardShoe ){
      
      if ( countMap.containsKey( card ) ){
        int count = countMap.get(card);
        count += 1;
        countMap.remove( card );
        countMap.put( card , count);
        
      } else{
        countMap.put( card , 1 );
      }
    }
    // cardShoe contains 4 decks of 52 cards!
    Assert.assertTrue(countMap.size() == 52);
    
    Collection<Integer> valueList = countMap.values();
    for (Integer i : valueList){
      Assert.assertTrue( i == 4 );
    }
  }
  
  @Test
  public void testDeck(){
	  Assert.assertTrue(Deck.getDeck().size()==52);
  }
  
}
