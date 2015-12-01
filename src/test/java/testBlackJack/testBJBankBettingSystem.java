package testBlackJack;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;
import kodaLoss.AbstractRound;
import kodaLoss.Bank;
import kodaLoss.Card;
import kodaLoss.CasinoRound;
import kodaLoss.Player;
import kodaLoss.Rank;
import kodaLoss.RoundResult;
import kodaLoss.Suite;

public class testBJBankBettingSystem extends TestCase {

  Player player;
  Bank bank;
  CasinoRound round  = new CasinoRound();
  public static final int PLAYER_BET = 100;
  public static final int PLAYER_STARTCASH = 100;

  
  public void setUp(){
    bank = bank.getInstance();
    bank.registerController(new PassivController());
    bank.roundThread = round;
    player = new Player("TESTPLAYER", PLAYER_STARTCASH);
    bank.addPlayerToBank(player);
  }
  
  
  @Test
  public void testWinnerWithoutBJGetsRightWinAmount() {

    player.setPlayersBet(PLAYER_BET);
    player.setRoundResult(RoundResult.WIN);
    System.out.println(player.getRoundResult());
    int cashBefore = player.getPlayersCash();

    bank.roundThread.handlePlayersBetsAndPayWinners();
    System.out.println("Should be 200: " + player.getPlayersCash());
    
    Assert.assertTrue(player.getPlayersCash() == cashBefore + (2 * PLAYER_BET));
  }

  @Test
  public void testWinnerWithABLACKJACKGetsRightWinAmount() {

    
    List<Card> hand = player.getPlayersHand();
    hand.clear();
    hand.add(new Card(Suite.CLUBS, Rank.ACE));
    hand.add(new Card(Suite.DIAMONDS, Rank.KING));

    player.setRoundResult(RoundResult.WIN);
    System.out.println(player.getRoundResult() + " with a BLACK JACK!");
    final int cashBefore = player.getPlayersCash();

    player.setPlayersBet(PLAYER_BET);

    bank.roundThread.handlePlayersBetsAndPayWinners();
    System.out.println("Should be 250: " + player.getPlayersCash());
    
    Assert.assertTrue(player.getPlayersCash() == (int) (2.5 * PLAYER_BET));

  }

  @Test
  public void testPlayerWithTIEGetsRightWinAmount() {

   
    player.setRoundResult(RoundResult.TIE);
    System.out.println(player.getRoundResult());

    int cashBefore = player.getPlayersCash();
    System.out.println("TIE CASH BEFORE: " + cashBefore);

    player.setPlayersBet(PLAYER_BET);

    bank.roundThread.handlePlayersBetsAndPayWinners();
    System.out.println("Should be 100: " + player.getPlayersCash());
    
    Assert.assertTrue(player.getPlayersCash() == cashBefore);

  }

  @Test
  public void testPlayerWithLOOSELoosesRoundBet() {

    player.setRoundResult(RoundResult.LOOSE);
    System.out.println(player.getRoundResult());
    int cashBefore = player.getPlayersCash();

    player.setPlayersBet(PLAYER_BET);

    bank.roundThread.handlePlayersBetsAndPayWinners();
    System.out.println("Should be 0: " + player.getPlayersCash());
    
    Assert.assertTrue(player.getPlayersCash() == 0);
  }

  @Override
  public void tearDown(){
    bank.resetBank();
    bank = null;
    player = null;
    round = null;
  }
  
  
  
}
