package testBlackJack;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import kodaLoss.Bank;
import kodaLoss.IController;
import kodaLoss.Player;
import kodaLoss.UserChoiceAdapter;

public class testBlackJackGetBetsFromPlayer extends TestCase {
  
  private Bank bank;
  private ImplIController controller ;
  
  @Before
  public void buildup(){
    bank = Bank.getInstance();
    Player player = new Player("TESTPLAYER", 1000);
    bank.registeredPlayers.add(player);
    controller = new ImplIController();
    bank.registerController(controller);
  }
  
  
  @Test
  public void testPlayerSendsANegativeAmount(){
    
    controller.setBetFromTestEngine( -50 );
//    UserChoiceAdapter.getInstance().playerChoosesToLayHisBet();
    bank.askPlayersForBetsForThisRound();
    
    Assert.assertTrue(bank.registeredPlayers.get(0).getPlayersBet() == 50 );
  }
  
  
  @After
  public void tearDown(){
    bank.resetBank();
    bank = null;
  }
}
