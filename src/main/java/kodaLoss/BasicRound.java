package kodaLoss;

import static kodaLoss.Bank_HelpersAndTools.isPlayersHandABlackJack;
import static kodaLoss.Bank_HelpersAndTools.isPlayersHandOver21;
import static kodaLoss.BlackJackConstantsAndTools.PLAYER_IS_BUST;

import kodaLoss.UserChoiceAdapter.UserChoice;

public class BasicRound extends AbstractRound{

  
  public BasicRound(){
    System.out.println("BasicRound");
    controller.disableAdvancedButton();
  }


  @Override
  public void playerPlays(Player player) {
    System.out.println("Player plays - Basic rules - started...");
    
    // activate players buttons
    controller.gameIson();

    uca.resetUserChoice(); // prepare UCA for input

    while (uca.getUserChoice() != UserChoice.STAY) {

      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      if (uca.getUserChoice() == UserChoice.HIT) {

        bank.dealOneCardToPlayer(player);
        bank.updateGuiAfterChangeInDataModel();

        System.out.println("PLAYER HIT");
        player.printHandToConsole();
        uca.resetUserChoice();

        if (isPlayersHandOver21(player)) {
          System.out.println(PLAYER_IS_BUST);
          controller.setlabelWinnerText(PLAYER_IS_BUST);
          bank.updateGuiAfterChangeInDataModel();
          break;
        }
      }
    }
    
    // print out all data of Player!
   System.out.println(player.toString());
    // finally reset last choice in UCA
    uca.resetUserChoice();
  }

  @Override
  public void handlePlayersBetsAndPayWinners() {
    /* BASIC RULES! 
     * TIE => return betting amount WIN => return 2 * bet WIN + BJ => return 2.5
     * bet Loose => players bet is not returned
     */
    int playersBet;
    int playersBalance = 0;
    
    for (Player player : bank.registeredPlayers) {

      playersBet = player.getPlayersBet();

      if (player.getRoundResult() == RoundResult.TIE) {
        playersBalance = playersBet;

      } else if (player.getRoundResult() == RoundResult.WIN) {
        playersBalance = (int) Math.floor(playersBet * 2.0d);

        // Win with Black Jack adds another 50% of bet!
        if (isPlayersHandABlackJack(player)) {
          playersBalance += (int) Math.floor(playersBet / 2.0d);
          System.out.println("BLACKJACK");
        }
      } 
      
      // last rounds Players bet as default in gui for next round
//              player.setPlayersBet(0);
      controller.setlabelWinnerText(String.format("%s $: + %d" , player.getName() , playersBalance ));
      player.addToPlayersCash(playersBalance);
      
      player.setRoundResult(null);
      bank.updateGuiAfterChangeInDataModel();
    }
  }

}
