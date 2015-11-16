package testBlackJack;

import static kodaLoss.RoundResult.LOOSE;
import static kodaLoss.RoundResult.TIE;
import static kodaLoss.RoundResult.WIN;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kodaLoss.Bank;
import kodaLoss.Card;
import kodaLoss.Player;
import kodaLoss.Rank;
import kodaLoss.RoundResult;
import kodaLoss.Suite;

public class testBJBankWinnerAndCashSprint3 {

  Player dealerBJ;
  Player dealerBUST;
  Player dealer21;
  Player dealer17;
  Player playerBJ;
  Player playerBUST;
  Player player21;
  Player player17;
  
  Bank bank; 
  

  @Before
  public void buildUp() {
    
    playerBJ = new Player();
    playerBJ.addCardToHand(new Card(Suite.HEARTS, Rank.ACE));
    playerBJ.addCardToHand(new Card(Suite.DIAMONDS, Rank.KING));

    playerBUST = new Player();
    playerBUST.addCardToHand(new Card(Suite.HEARTS, Rank.QUEEN));
    playerBUST.addCardToHand(new Card(Suite.DIAMONDS, Rank.KING));
    playerBUST.addCardToHand(new Card(Suite.DIAMONDS, Rank.TEN));

    player21 = new Player();
    player21.addCardToHand(new Card(Suite.HEARTS, Rank.QUEEN));
    player21.addCardToHand(new Card(Suite.DIAMONDS, Rank.SEVEN));
    player21.addCardToHand(new Card(Suite.DIAMONDS, Rank.FOUR));

    player17 = new Player();
    player17.addCardToHand(new Card(Suite.SPADES, Rank.JACK));
    player17.addCardToHand(new Card(Suite.SPADES, Rank.SEVEN));
    
    bank = bank.getInstance();
  }

  
  @Test
  public void testCalculateWinners_DealerAndPlayerBUST() {
    
    bank.addPlayerToBank(playerBUST);
    
    bank.dealer.addCardToHand(new Card(Suite.HEARTS, Rank.QUEEN));
    bank.dealer.addCardToHand(new Card(Suite.DIAMONDS, Rank.KING));
    bank.dealer.addCardToHand(new Card(Suite.DIAMONDS, Rank.TEN));
    
    bank.roundOutcome();
    
    RoundResult result = bank.registeredPlayers.get(0).getRoundResult();
   
    Assert.assertTrue(result == RoundResult.LOOSE);
  }

    
  
  @Test
  public void testCalculateWinners_DealerBUSTPlayerNot() {
    bank.registeredPlayers.add(player17);
    
    bank.dealer.addCardToHand(new Card(Suite.HEARTS, Rank.QUEEN));
    bank.dealer.addCardToHand(new Card(Suite.DIAMONDS, Rank.KING));
    bank.dealer.addCardToHand(new Card(Suite.DIAMONDS, Rank.TEN));
    
    bank.roundOutcome();
    
    Assert.assertTrue(player17.getRoundResult() == WIN);
  }
  
  @Test
  public void testCalculateWinners_DealerlowerThanPlayer() {
    bank.registeredPlayers.add(player21);
    
    bank.dealer.addCardToHand(new Card(Suite.SPADES, Rank.JACK));
    bank.dealer.addCardToHand(new Card(Suite.SPADES, Rank.SEVEN));
    
    bank.roundOutcome();
    
    Assert.assertTrue(player21.getRoundResult() == WIN);
  }

  
  @Test
  public void testCalculateWinners_DealerhigherThanPlayer() {
    bank.registeredPlayers.add(player17);
    
    bank.dealer.addCardToHand(new Card(Suite.HEARTS, Rank.QUEEN));
    bank.dealer.addCardToHand(new Card(Suite.DIAMONDS, Rank.SEVEN));
    bank.dealer.addCardToHand(new Card(Suite.DIAMONDS, Rank.FOUR));
    
    bank.roundOutcome();
    
    Assert.assertTrue(player17.getRoundResult() == LOOSE);
  }

  @Test
  public void testCalculateWinners_DealerSameValueAsPlayer() {
    bank.registeredPlayers.add(player21);
    
    bank.dealer.addCardToHand(new Card(Suite.HEARTS, Rank.QUEEN));
    bank.dealer.addCardToHand(new Card(Suite.DIAMONDS, Rank.SEVEN));
    bank.dealer.addCardToHand(new Card(Suite.DIAMONDS, Rank.FOUR));
    
    bank.roundOutcome();
    
    Assert.assertTrue(player21.getRoundResult() == TIE);
  }

  @Test
  public void testCalculateWinners_DealerBJPlayerNotBJ() {
    bank.registeredPlayers.add(player17);
    
    bank.dealer.addCardToHand(new Card(Suite.HEARTS, Rank.ACE));
    bank.dealer.addCardToHand(new Card(Suite.DIAMONDS, Rank.KING));
    
    bank.roundOutcome();
    
    Assert.assertTrue(player17.getRoundResult() == LOOSE);
  }

  @Test
  public void testCalculateWinners_DealerNotBJPlayerBJ() {
    bank.registeredPlayers.add(playerBJ);
    
    bank.dealer.addCardToHand(new Card(Suite.HEARTS, Rank.QUEEN));
    bank.dealer.addCardToHand(new Card(Suite.DIAMONDS, Rank.SEVEN));
    bank.dealer.addCardToHand(new Card(Suite.DIAMONDS, Rank.FOUR));
    
    bank.roundOutcome();
    
    Assert.assertTrue(playerBJ.getRoundResult() == WIN);
  }

  @Test
  public void testCalculateWinners_DealerAndPlayerBJ() {
    bank.registeredPlayers.add(playerBJ);
    
    bank.dealer.addCardToHand(new Card(Suite.HEARTS, Rank.ACE));
    bank.dealer.addCardToHand(new Card(Suite.DIAMONDS, Rank.KING));
    
    bank.roundOutcome();
    
    Assert.assertTrue(playerBJ.getRoundResult() == TIE);
  }

  
  @After
  public void cleanUp(){
    bank.resetBank();
    bank = null;
  }
  
}
