package testBlackJack;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kodaLoss.Bank;
import kodaLoss.Card;
import kodaLoss.Player;
import kodaLoss.Rank;
import kodaLoss.Suite;
import static  kodaLoss.RoundResult.*;

public class testBJBankWinnerAndCashSprint3 {

  Player dealerBJ;
  Player dealerBUST;
  Player dealer21;
  Player dealer17;
  Player playerBJ;
  Player playerBUST;
  Player player21;
  Player player17;

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

    dealerBJ = playerBJ;
    dealerBUST = playerBUST;
    dealer21 = player21;
    dealer17 = player17;

  }

  @Test
  public void testCalculateWinners_DealerAndBankBUST() {
    Bank.getInstance().registeredPlayers.add(playerBUST);
    Bank.getInstance().dealer = dealerBUST;
    Bank.getInstance().calculateWinners();
    Assert.assertTrue(playerBUST.getRoundResult() == TIE);
  }

  @Test
  public void testCalculateWinners_DealerBUSTPlayerNot() {
    Bank.getInstance().registeredPlayers.add(player17);
    Bank.getInstance().dealer = dealerBUST;
    Bank.getInstance().calculateWinners();
    Assert.assertTrue(player17.getRoundResult() == WIN);
  }

  public void testCalculateWinners_DealerlowerThanPlayer() {
    Bank.getInstance().registeredPlayers.add(player21);
    Bank.getInstance().dealer = dealer17;
    Bank.getInstance().calculateWinners();
    Assert.assertTrue(player21.getRoundResult() == WIN);

  }

  public void testCalculateWinners_DealerhigherThanPlayer() {
    Bank.getInstance().registeredPlayers.add(player17);
    Bank.getInstance().dealer = dealer21;
    Bank.getInstance().calculateWinners();
    Assert.assertTrue(player17.getRoundResult() == LOOSE);
  }

  public void testCalculateWinners_DealerSameValueAsPlayer() {
    Bank.getInstance().registeredPlayers.add(player21);
    Bank.getInstance().dealer = dealer21;
    Bank.getInstance().calculateWinners();
    Assert.assertTrue(player21.getRoundResult() == LOOSE);
  }

  public void testCalculateWinners_DealerBJPlayerNotBJ() {
    Bank.getInstance().registeredPlayers.add(player17);
    Bank.getInstance().dealer = dealerBJ;
    Bank.getInstance().calculateWinners();
    Assert.assertTrue(player17.getRoundResult() == LOOSE);
  }

  public void testCalculateWinners_DealernotPlayerBJ() {
    Bank.getInstance().registeredPlayers.add(playerBJ);
    Bank.getInstance().dealer = dealer21;
    Bank.getInstance().calculateWinners();
    Assert.assertTrue(playerBJ.getRoundResult() == WIN);
  }

  public void testCalculateWinners_DealerAndPlayerBJ() {
    Bank.getInstance().registeredPlayers.add(playerBJ);
    Bank.getInstance().dealer = dealerBJ;
    Bank.getInstance().calculateWinners();
    Assert.assertTrue(playerBJ.getRoundResult() == TIE);
  }

  
  @After
  public void cleanUp(){
    Bank.getInstance().clearAllPlayersAndDealer();
  }
  
  
  
}
