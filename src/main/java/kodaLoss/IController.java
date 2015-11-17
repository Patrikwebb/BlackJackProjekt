/**
 * 
 */
package kodaLoss;

/**
 * 
 * Needed to be able to create a mock-controller for testing our methods
 * or to play in console instead of a GUI
 * 
 * @author timlanghans
 *
 */
public interface IController {

  public void setplayersHandScore(String score);
  
  public void setdealersHandScore(String score);
  
  public void setlabelWinnerText(String winnerText);
  
  public void setHideDealersSecondCard(boolean hide);
  
  public void updatePlayer(Player activePlayerOnGui);
  
  public void updateDealer(Player dealer);
  
  public void updateRound(String round) ;
  
  public void gameIson() ;
  
  public void gameIsoff();
  
  public void allButtonsOff();
  
  public void activatePlayersBetField();
  
  public int getBetFromPlayersTextField() ;
  
  
  
  
}
