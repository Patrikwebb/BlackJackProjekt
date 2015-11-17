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

  /*
   * Possible results with possible outcomes BUST, 17 , 21 , BJ => 16 possible
   * combinations
   */
  // Dealer bust - Player bust => LOOSE *
  // Dealer bust - Player not bust (17* , 21 , BJ) => WIN 
  // Dealer 17 - Player BUst => LOOSE *
  // Dealer 17 - Player 17 => TIE
  // Dealer 17 - Player 21* or BJ => WIN
  // Dealer 21 - Player bust => Loose
  // Dealer 21 - Player 17* => Loose
  // Dealer 21 - Player 21 => TIE *
  // Dealer 21 - Player BJ => WIN *
  // Dealer BJ - Player 17 or 21 * => LOOSE
  // Dealer BJ - PLAYER BJ => TIE*
  
  // *-marked cases are implemented in the following tests, the other outcomes
  // are redundant

  @Test
  public void testCalculateWinners_DealerBustPlayerBust() {

    bank.addPlayerToBank(playerBUST);

    bank.dealer.addCardToHand(new Card(Suite.HEARTS, Rank.QUEEN));
    bank.dealer.addCardToHand(new Card(Suite.DIAMONDS, Rank.KING));
    bank.dealer.addCardToHand(new Card(Suite.DIAMONDS, Rank.TEN));

    bank.calculateWinners();

    RoundResult result = bank.registeredPlayers.get(0).getRoundResult();

    Assert.assertTrue(result == RoundResult.LOOSE);
  }

  @Test
  public void testCalculateWinners_DealerBustPlayer17() {
    bank.registeredPlayers.add(player17);

    bank.dealer.addCardToHand(new Card(Suite.HEARTS, Rank.QUEEN));
    bank.dealer.addCardToHand(new Card(Suite.DIAMONDS, Rank.KING));
    bank.dealer.addCardToHand(new Card(Suite.DIAMONDS, Rank.TEN));

    bank.calculateWinners();

    Assert.assertTrue(player17.getRoundResult() == WIN);
  }

  @Test
  public void testCalculateWinners_Dealer17PlayerBust() {
    bank.registeredPlayers.add(playerBUST);

    bank.dealer.addCardToHand(new Card(Suite.SPADES, Rank.JACK));
    bank.dealer.addCardToHand(new Card(Suite.SPADES, Rank.SEVEN));

    bank.calculateWinners();
    System.out.println("Dealer17PlayerBust => " + playerBUST.getRoundResult());
    Assert.assertTrue(playerBUST.getRoundResult() == LOOSE);
  }
  
  @Test
  public void testCalculateWinners_Dealer17Player21() {
    bank.registeredPlayers.add(player21);

    bank.dealer.addCardToHand(new Card(Suite.SPADES, Rank.JACK));
    bank.dealer.addCardToHand(new Card(Suite.SPADES, Rank.SEVEN));

    bank.calculateWinners();

    Assert.assertTrue(player21.getRoundResult() == WIN);
  }

  @Test
  public void testCalculateWinners_Dealer21Player17() {
    bank.registeredPlayers.add(player17);

    bank.dealer.addCardToHand(new Card(Suite.HEARTS, Rank.QUEEN));
    bank.dealer.addCardToHand(new Card(Suite.DIAMONDS, Rank.SEVEN));
    bank.dealer.addCardToHand(new Card(Suite.DIAMONDS, Rank.FOUR));

    bank.calculateWinners();

    Assert.assertTrue(player17.getRoundResult() == LOOSE);
  }

  @Test
  public void testCalculateWinners_DealerANDPlayer21() {
    bank.registeredPlayers.add(player21);

    bank.dealer.addCardToHand(new Card(Suite.HEARTS, Rank.QUEEN));
    bank.dealer.addCardToHand(new Card(Suite.DIAMONDS, Rank.SEVEN));
    bank.dealer.addCardToHand(new Card(Suite.DIAMONDS, Rank.FOUR));

    bank.calculateWinners();

    Assert.assertTrue(player21.getRoundResult() == TIE);
  }

  @Test
  public void testCalculateWinners_DealerBJPlayer21() {
    bank.registeredPlayers.add(player21);

    bank.dealer.addCardToHand(new Card(Suite.HEARTS, Rank.ACE));
    bank.dealer.addCardToHand(new Card(Suite.DIAMONDS, Rank.KING));

    bank.calculateWinners();
    System.out.println("DealerBJPlayer21 => " + player21.getRoundResult());
    Assert.assertTrue(player21.getRoundResult() == LOOSE);
  }

  @Test
  public void testCalculateWinners_Dealer21PlayerBJ() {
    bank.registeredPlayers.add(playerBJ);

    bank.dealer.addCardToHand(new Card(Suite.HEARTS, Rank.QUEEN));
    bank.dealer.addCardToHand(new Card(Suite.DIAMONDS, Rank.SEVEN));
    bank.dealer.addCardToHand(new Card(Suite.DIAMONDS, Rank.FOUR));

    bank.calculateWinners();

    Assert.assertTrue(playerBJ.getRoundResult() == WIN);
  }

  @Test
  public void testCalculateWinners_DealerANDPlayerBJ() {
    bank.registeredPlayers.add(playerBJ);

    bank.dealer.addCardToHand(new Card(Suite.HEARTS, Rank.ACE));
    bank.dealer.addCardToHand(new Card(Suite.DIAMONDS, Rank.KING));

    bank.calculateWinners();

    Assert.assertTrue(playerBJ.getRoundResult() == TIE);
  }

  @After
  public void cleanUp() {
    bank.resetBank();
    bank = null;
  }

}
