package testBlackJack;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;
import kodaLoss.Bank;
import kodaLoss.Card;
import kodaLoss.CasinoRound;
import kodaLoss.Player;
import kodaLoss.Rank;
import kodaLoss.Suite;

public class testBJCasinoRound extends TestCase {

  Bank bank;
  Player dealer;
  Player player;
  Card card1 = new Card(Suite.CLUBS,Rank.TEN);
  Card card2 = new Card(Suite.DIAMONDS, Rank.ACE);
  CasinoRound round;
  
  
  @Override
  public void setUp(){
    bank = Bank.getInstance();
    bank.registerController(new PassivController());
    bank.setRule("Casino Rule");
    dealer = new Player("DEALER" , 0);
    player = new Player("TestPlayer" , 100);
    bank.addPlayerToBank(player);
    player.addCardToHand(card1);
    player.addCardToHand(card2);
    dealer.addCardToHand(card1);
    dealer.addCardToHand(card1);
    round = new CasinoRound();
    bank.roundThread = round;
  }
  
  
  @Test
  public void testSplitHandsFirstCardIsHandsSecond(){
    round.makeSplitPlayer(player);
    Assert.assertEquals(card2, bank.registeredPlayers.get(1).getPlayersHand().get(0));
  }
  
  @Test
  public void testFirstCardIsSameAfterSplit(){
    round.makeSplitPlayer(player);
    Assert.assertEquals(card1, player.getPlayersHand().get(0));
  }
  
  @Test
  public void testSecondCardInSplitHandIsNewCards(){
    round.makeSplitPlayer(player);
    final Player splitPlayer = bank.registeredPlayers.get(1);
    final Card splitPlayersSec = splitPlayer.getPlayersHand().get(1);
    Assert.assertTrue(splitPlayersSec != card1 && splitPlayersSec != card2);
  }
  
  
  @Test
  public void testSplitPlayersBetIsSameAsPlayers_CashOkay(){
    
    final int bet = 50;
    player.setPlayersBet(bet);
    System.out.println(player.getPlayersBet());
    round.makeSplitPlayer(player);
    Player splitplayer = bank.registeredPlayers.get(1);
    System.out.println(splitplayer.getPlayersBet());
    Assert.assertTrue(splitplayer.getPlayersBet() == bet);
    
  }
  
  
  @Test
  public void testMakeSplitPlayer_BothSplitHandsWinWith21(){
    // two hands with 50 dollar each => win 200!
    player.setPlayersBet(50);
    round.makeSplitPlayer(player);
    player.addCardToHand(card2);
    bank.registeredPlayers.get(1).addCardToHand(card1);
    
    bank.roundThread.calculateWinners();
    bank.roundThread.handlePlayersBetsAndPayWinners();
    round.cleanUpAfterRound();
    System.out.println(player.getPlayersCash());
    Assert.assertTrue(player.getPlayersCash() == 200);
    
  }
}
