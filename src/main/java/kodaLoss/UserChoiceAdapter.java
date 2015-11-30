package kodaLoss;

public class UserChoiceAdapter {

  /**
   * one solution to handle user input. Not the best one!
   * @author timlanghans
   *
   */
  
  public enum UserChoice{
	  
    /** Player do not want another card or action - player stays */
    STAY,
    
    /** Player wants to get a new card, player hits */
    HIT ,
    
    /** Player wants to start the next round */
    START_ROUND, 
    
    /** Player wants to set his bet for the round */
    LAY_BET, 
    
    /** Player wants to play Double, the casino rule */
    DOUBLE, 
    
    /** Player wants to buy an insurance from bank */
    INSURANCE, 
    
    /** Player wants to split his deck , the casino rule */
    SPLIT;
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
   * Player choose to take insurance by pressing the Insurance button in Gui 
   */
  public void playerChoosesToTakeInsurance(){
	  System.out.println("Player takes Insurance");
	  
	  if( userChoice == null){
		  userChoice = UserChoice.INSURANCE;
	  }
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
   * Change  UserChoice to DOUBLE
   */
    public void playerChoosesToDouble() {
  System.out.println("Player Choose to Double");
      
      if (userChoice == null){
        userChoice = UserChoice.DOUBLE;
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


  public void playerChoosesToSplit() {
 System.out.println("Player choose SPLIT");
    
    if( userChoice == null){
      userChoice = UserChoice.SPLIT;
    }
  }
  


  
}
