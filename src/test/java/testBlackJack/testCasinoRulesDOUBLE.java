package testBlackJack;

import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;
import kodaLoss.Bank;
import kodaLoss.Player;

public class testCasinoRulesDOUBLE extends TestCase{

  /**
   * 
   * test method which doubles players bet when playing double, etc
   * bank.doublePlayersBet(Player p)
   * 
   * 
   * 
   * 
   * 
   */
  
  
  
  private Bank bank;
  private Player player;
 
  
@Override
  public void setUp(){
    bank = Bank.getInstance();
    player = new Player("TESTPLAYER" , 100);
    
    
  }
  
  
  @Test
  public void testDoublePBet_PlayerHasEnoughCash(){
    final int firstBet = 50;
    player.setPlayersBet( firstBet );
    bank.doublePlayersBet(player);
    Assert.assertTrue(player.getPlayersBet() == 2 * firstBet);
  }
  
  @Test
  public void testDoublePBet_PlayerHasNotEnoughCash(){
    final int firstBet = 75;
    player.setPlayersBet(firstBet);
    
    try{
      bank.doublePlayersBet(player);
      fail("Should not be able to double! Test failed!");
    
    } catch (Exception e){
     
      if (e.getClass() == IllegalArgumentException.class){
        Assert.assertTrue("Correct Exception thrown!" , true);
      
      } else {
        fail("Wrong Exception type thrown: " + e.getClass());
      }
    }
  }
  
  
  
  
  @Override
  public void tearDown(){
    bank = null;
    player = null;
  }
  
  
  
}
