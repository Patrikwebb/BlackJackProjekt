package testBlackJack;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import kodaLoss.Bank;
import kodaLoss.Player;
import kodaLoss.RoundResult;

public class testBJBankBettingSystem extends TestCase {

  private Player player;
  private Bank bank = Bank.getInstance();
  private static final int PLAYER_BET = 100;
  
  
  @Before
  public void buildUp() {
    
     player = new Player();
     player.setPlayersBet( PLAYER_BET);
     
     bank.addPlayerToBank(player);
    
  
  }
  
  
  @Test
  public void testWinnerWithoutBJGetsRightWinAmount(){
    
    player.setRoundResult(RoundResult.WIN);
    
    int cashBefore = player.getPlayersCash();
    
    bank.handlePlayersBetsAndPayWinners();
    Assert.assertTrue( player.getPlayersCash() == cashBefore + (2 * PLAYER_BET) );
  }
  
  
  
  
  
}
