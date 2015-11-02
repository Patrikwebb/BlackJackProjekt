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
  
  
  private static UserChoiceAdapter uca = new UserChoiceAdapter();
  
  private static UserChoice userChoice = null;
  
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
    
    if( userChoice == null){
      userChoice = UserChoice.STAY;
    }
  }
  
  /**
   * Player chooses to get another card by pressing the Hit-button on his 
   * user-interface
   */
  public  void playerChoosesToHit(){
    
    if( userChoice == null){
      userChoice = UserChoice.HIT;
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
  public static UserChoice getUserChoice(){
    return userChoice;
  }
  
  
}
