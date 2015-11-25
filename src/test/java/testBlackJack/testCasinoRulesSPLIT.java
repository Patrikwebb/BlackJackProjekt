package testBlackJack;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import junit.framework.*;
import kodaLoss.Bank;
import kodaLoss.Card;
import kodaLoss.Player;
import kodaLoss.Rank;
import kodaLoss.Suite;

public class testCasinoRulesSPLIT extends TestCase {

  /**
   * tests method regarding split function when playing with 
   * casino rules (split, insurance, double)
   * 
   * rules to test: split legal when: 
   * 1. TWO equally valued Cards in hand after dealt
   * 2. Enough money to double the bets for the round in the game
   * 
   * => testcases for split:
   * Case 1: card-rule ok & enough money => split true
   * Case 2: card-rule ok & not enough money => split false
   * Case 3: card-rule not & enough money => split false
   * Case 4: card-rule not & not enough money => split false
   * to falsify card rule: 1 card (should be impossible) , 3 cards, 
   * different value
   * to falsify money rule : less cash than bet 
   */
  
  
  Player p = null;
  Card card1 = null;
  Card card2 = null;
  Card card2_clone = null;
  Card card3 = null;
  Bank bank = null;

  @Override // for junit3 
  public void setUp() {
    
    p = new Player("TestPlayer", 100);
    card1 = new Card(Suite.CLUBS, Rank.FOUR);
    card2 = new Card(Suite.DIAMONDS, Rank.FOUR);
    card3 = new Card(Suite.DIAMONDS , Rank.ACE);
    card2_clone = new Card(Suite.DIAMONDS, Rank.FOUR);
   
    bank = Bank.getInstance();
  }

  //CASE 1
  @Test
  public void testCheckIfSplitCanBePlayed_TwoEqualValueCardsAndEnoughCash() {
    p.addCardToHand(card1);
    p.addCardToHand(card2);
    p.setPlayersBet(50);

    Assert.assertTrue( bank.checkIfSplitCanBePlayed(p) );
  }

  // CASE 2
  @Test
  public void testCheckIfSplitCanBePlayed_CardOkayButNotEnoughCash() {
    p.addCardToHand(card1);
    p.addCardToHand(card2);
    p.setPlayersBet(75);
    Assert.assertFalse(bank.checkIfSplitCanBePlayed(p));
  }
  
  //CASE 3
  @Test
  public void testCheckIfSplitCanBePlayed_ThreeSameValueCardsButOKCash() {

    p.addCardToHand(card1);
    p.addCardToHand(card2);
    p.addCardToHand(card2_clone);
    p.setPlayersBet(50);
    Assert.assertFalse(bank.checkIfSplitCanBePlayed(p));
  }
  
  //CASE 3
  @Test
  public void testCheckIfSplitCanBePlayed_TwoCardsWithDiffValueButOKCash() {

    p.addCardToHand(card1);
    p.addCardToHand(card3);
    p.setPlayersBet(50);
    Assert.assertFalse(bank.checkIfSplitCanBePlayed(p));
  }
  
  @Override //for junit3
  public void tearDown() {
    bank = null;
    p = null;
    card1 = null;
    card2 = null;

  }

}
