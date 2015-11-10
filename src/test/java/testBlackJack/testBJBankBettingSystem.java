package testBlackJack;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import kodaLoss.Bank;
import kodaLoss.Player;
import kodaLoss.RoundResult;

public class testBJBankBettingSystem extends TestCase {

   Player player;
   Bank bank = Bank.getInstance();
  public  static final int PLAYER_BET = 100;
  
  
  @Before
  public void buildUp() {
    
     player = new Player("TESTPLAYER" , PLAYER_BET);
     player.setPlayersBet( PLAYER_BET);
     System.out.println(player.getPlayersBet());
     bank.addPlayerToBank(player);
  }
  
  
  @Test
  public void testWinnerWithoutBJGetsRightWinAmount(){
    
    player.setRoundResult(RoundResult.WIN);
    System.out.println(player.getRoundResult());
    int cashBefore = player.getPlayersCash();
    
    bank.handlePlayersBetsAndPayWinners();
    Assert.assertTrue( player.getPlayersCash() == cashBefore + (2 * PLAYER_BET) );
  }
  
  
  
  
  
}
