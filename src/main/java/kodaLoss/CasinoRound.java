package kodaLoss;

import static kodaLoss.Bank_HelpersAndTools.isPlayersHandABlackJack;
import static kodaLoss.Bank_HelpersAndTools.isPlayersHandOver21;
import static kodaLoss.BlackJackConstantsAndTools.PLAYER_IS_BUST;

import kodaLoss.UserChoiceAdapter.UserChoice;

public class CasinoRound extends AbstractRound{

  public CasinoRound(){
    System.out.println("CasinoRound");
    controller.activateAdvancedButton();
  }

  @Override
  protected void playerPlays(Player player) {

    System.out.println("Player plays - Casino rules - started...");

    // check for casino rules
    checkIfInsuranceCanBePlayed(player);
    checkIfSplitCanBePlayed(player);
   // checkIfDoubleCanBePlayer(player);
    
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
      else if (uca.getUserChoice() == UserChoice.DOUBLE) {
        playerDouble(player);
        break;
        
      } else if (uca.getUserChoice() == UserChoice.INSURANCE){
        // TODO 
        
        
      } else if (uca.getUserChoice() == UserChoice.SPLIT){
        //TODO
      }
    }
    
    // print out all data of Player!
   System.out.println(player.toString());
    
    // finally reset last choice in UCA
    uca.resetUserChoice();
  }

  @Override
  public void handlePlayersBetsAndPayWinners() {
      /* WITH CASINO RULES! 
       * TIE => return betting amount WIN => return 2 * bet WIN + BJ => return 2.5
       * bet Loose => players bet is not returned
       * INSURANCE 3:2 if dealer has a BlackJAck, return bet if dealer wins
       */
      int playersBet;

      for (Player player : bank.registeredPlayers) {

        playersBet = player.getPlayersBet();

        if (player.getRoundResult() == RoundResult.TIE) {
          player.addToPlayersCash(playersBet);

        } else if (player.getRoundResult() == RoundResult.WIN) {
          player.addToPlayersCash(playersBet * 2);

          // Win with Black Jack adds another 50% of bet!
          if (isPlayersHandABlackJack(player)) {
            player.addToPlayersCash((int) Math.round(playersBet / 2.0d));
            System.out.println("BLACKJACK");
          }
        } else if (player.getRoundResult() == RoundResult.LOOSE){
          // insured against loss => return bet (2 * 1/2 bet) 
          // Dealer has a BlackJAck => return 3:2 of bet!
          if (player.isHasInsurance()){
            
            if (isPlayersHandABlackJack(bank.dealer)){
              player.addToPlayersCash((int) 1.5 * player.getPlayersBet()); 
            } else {
              player.addToPlayersCash(player.getPlayersBet());
            }
          }
        }
        // last rounds Players bet as default in gui for next round
        //        player.setPlayersBet(0); 
        player.setRoundResult(null);
        bank.updateGuiAfterChangeInDataModel();
      }
    }
    
  
    
  

}
