package kodaLoss;

public class UserChoiceAdapter {

  /**
   * 
   * @author timlanghans
   *
   */
  
  
  
  public enum UserChoice{
    STAY, HIT;
  }
  
  private static UserChoice userChoice = null;
  
  // private constructor, no objects!
  private UserChoiceAdapter(){
  }
  
  /**
   * Player chooses to stay by pressing the Stay-button on his user-interface
   */
  public static void playerChoosesToStay(){
    
    if( userChoice == null){
      userChoice = UserChoice.STAY;
    }
  }
  
  /**
   * Player chooses to get another card by pressing the Hit-button on his 
   * user-interface
   */
  public static void playerChoosesToHit(){
    
    if( userChoice == null){
      userChoice = UserChoice.HIT;
    }
  }
  

  /**
   * resets the userChoice field for next user choice
   */
  public static void resetUserChoice(){
    userChoice = null;
  }
  
  /**
   * get the choosen UserChoice enum at this time
   * @return UserChoice
   */
  public static UserChoice getUserChoice(){
    return userChoice;
  }
  
  
}
