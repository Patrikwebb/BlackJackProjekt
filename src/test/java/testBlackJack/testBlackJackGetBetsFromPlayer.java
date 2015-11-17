package testBlackJack;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import kodaLoss.Bank;
import kodaLoss.IController;
import kodaLoss.Player;

public class testBlackJackGetBetsFromPlayer extends TestCase {
  
  Bank bank;
  
  @Before
  public void buildup(){
    bank = Bank.getInstance();
    Player player = new Player("TESTPLAYER", 1000);
    bank.registeredPlayers.add(player);
    IController controller = new ImplIController();
    bank.registerController(controller);
  }
  
  
  @Test
  public void testPlayerSendsANegativeAmount(){
    
    
    
    
  }
  
  
  @After
  public void tearDown(){
    bank.resetBank();
    bank = null;
  }
}
