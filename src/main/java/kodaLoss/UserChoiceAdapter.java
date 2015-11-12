package kodaLoss;

public class UserChoiceAdapter {

  /**
   * one solution to handle user input. Not the best one!
   * @author timlanghans
   *
   */
  
  public enum UserChoice{
    STAY, HIT , START_ROUND, LAY_BET;
  }
  
  //Singleton Pattern
  private static UserChoiceAdapter uca = new UserChoiceAdapter();
  
  private  UserChoice userChoice = null;
  
  // private constructor, no objects!
  private UserChoiceAdapter(){
  }
  
  
  public static UserChoiceAdapter getInstance(){
    return uca;
  }
  
  
  
  /**
   * Player chooses to stay by pressing the Stay-button on his user-interface
   */
  public  void playerChoosesToStay(){
    System.out.println("Player choose STAY");
    if( userChoice == null){
      userChoice = UserChoice.STAY;
    }
  }
  
  /**
   * Player chooses to get another card by pressing the Hit-button on his 
   * user-interface
   */
  public  void playerChoosesToHit(){
    System.out.println("Player choose HIT");
    
    if( userChoice == null){
      userChoice = UserChoice.HIT;
    }
  }
  
  
  public void playerChoosesToStartNewRound(){
    System.out.println("Player Choose to Start new Round");
    
    if (userChoice == null){
      userChoice = UserChoice.START_ROUND;
    }
  }
  
/**
 * called when player has made a bet in his betting textfield  
 */
  public void playerChoosesToLayHisBet() {
System.out.println("Player Choose to Lay his Bet");
    
    if (userChoice == null){
      userChoice = UserChoice.LAY_BET;
    }
  }
  

  /**
   * resets the userChoice field for next user choice
   */
  public void resetUserChoice(){
    userChoice = null;
  }
  
  /**
   * get the choosen UserChoice enum at this time
   * @return UserChoice
   */
  public UserChoice getUserChoice(){
    return userChoice;
  }


  
}
