package testBlackJack;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import kodaLoss.Bank;
import kodaLoss.Card;
import kodaLoss.Player;
import kodaLoss.Rank;
import kodaLoss.RoundResult;
import kodaLoss.Suite;

public class testBJBankBettingSystem extends TestCase {

  Player player;
  Bank bank;
  public static final int PLAYER_BET = 100;
  public static final int PLAYER_STARTCASH = 100;

  @Test
  public void testWinnerWithoutBJGetsRightWinAmount() {
    player = new Player("TESTPLAYER", PLAYER_STARTCASH);
    bank = Bank.getInstance();
    bank.addPlayerToBank(player);

    player.setPlayersBet(PLAYER_BET);

    player.setRoundResult(RoundResult.WIN);
    System.out.println(player.getRoundResult());
    int cashBefore = player.getPlayersCash();

    bank.handlePlayersBetsAndPayWinners();
    System.out.println("Should be 200: " + player.getPlayersCash());
    
    Assert.assertTrue(player.getPlayersCash() == cashBefore + (2 * PLAYER_BET));
  }

  @Test
  public void testWinnerWithABLACKJACKGetsRightWinAmount() {

    player = new Player("TESTPLAYER", PLAYER_STARTCASH);
    bank = Bank.getInstance();
    bank.addPlayerToBank(player);
    List<Card> hand = player.getPlayersHand();
    hand.clear();
    hand.add(new Card(Suite.CLUBS, Rank.ACE));
    hand.add(new Card(Suite.DIAMONDS, Rank.KING));

    player.setRoundResult(RoundResult.WIN);
    System.out.println(player.getRoundResult() + " with a BLACK JACK!");
    final int cashBefore = player.getPlayersCash();

    player.setPlayersBet(PLAYER_BET);

    bank.handlePlayersBetsAndPayWinners();
    System.out.println("Should be 250: " + player.getPlayersCash());
    
    Assert.assertTrue(player.getPlayersCash() == (int) (2.5 * PLAYER_BET));

  }

  @Test
  public void testPlayerWithTIEGetsRightWinAmount() {

    player = new Player("TESTPLAYER", PLAYER_STARTCASH);
    bank = Bank.getInstance();
    bank.addPlayerToBank(player);
    player.setRoundResult(RoundResult.TIE);
    System.out.println(player.getRoundResult());

    int cashBefore = player.getPlayersCash();
    System.out.println("TIE CASH BEFORE: " + cashBefore);

    player.setPlayersBet(PLAYER_BET);

    bank.handlePlayersBetsAndPayWinners();
    System.out.println("Should be 100: " + player.getPlayersCash());
    
    Assert.assertTrue(player.getPlayersCash() == cashBefore);

  }

  @Test
  public void testPlayerWithLOOSELoosesRoundBet() {

    player = new Player("TESTPLAYER", PLAYER_STARTCASH);
    bank = Bank.getInstance();
    bank.addPlayerToBank(player);

    player.setRoundResult(RoundResult.LOOSE);
    System.out.println(player.getRoundResult());
    int cashBefore = player.getPlayersCash();

    player.setPlayersBet(PLAYER_BET);

    bank.handlePlayersBetsAndPayWinners();
    System.out.println("Should be 0: " + player.getPlayersCash());
    
    Assert.assertTrue(player.getPlayersCash() == 0);
  }

}
