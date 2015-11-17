package testBlackJack;

import kodaLoss.IController;
import kodaLoss.Player;

public class ImplIController implements IController {

  @Override
  public void setplayersHandScore(String score) {
    // TODO Auto-generated method stub
   
  }

  @Override
  public void setdealersHandScore(String score) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void setlabelWinnerText(String winnerText) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void setHideDealersSecondCard(boolean hide) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void updatePlayer(Player activePlayerOnGui) {
    // TODO Auto-generated method stub
    System.out.println("Player: " + activePlayerOnGui.toString());
  }

  @Override
  public void updateDealer(Player dealer) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void updateRound(String round) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void gameIson() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void gameIsoff() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void allButtonsOff() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void activatePlayersBetField() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public int getBetFromPlayersTextField() {
    // TODO Auto-generated method stub
    return 0;
  }

}
