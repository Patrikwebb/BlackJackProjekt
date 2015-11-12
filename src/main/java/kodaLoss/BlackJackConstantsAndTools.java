package kodaLoss;

public class BlackJackConstantsAndTools {

  // FILE PATH TO CARD PICTURES
  public final static String FILE_PICTURE_PROTOKOLL = "file:///" ;
  public final static String FILE_USER_USERDIR = System.getProperty("user.dir");
  public final static String FILE_PICTURE_PATH = "\\src\\CardPictures\\";
  public final static String FILE_PICTURE_FILEENDING = ".png";

  // Animation
  public final static long ONE_SECOND_SLEEP = 1000;
  
  // TEXTs
  public final static  String RESULT_YOU_WON =  "Congratulations! You won.";
  public final static String RESULT_YOU_LOOSE = "Sorry, you lost.";
  public final static String RESULT_A_TIE = "It´s a tie";
  public static final String ASK_FOR_BETS = "please enter your bet!";
  
  
  public static void sleepForXSeconds( ){

    try{
      Thread.sleep(ONE_SECOND_SLEEP);
    } catch (InterruptedException e){
      e.printStackTrace();
      System.out.println("BJConstantsAndTools.sleepFor1Second: problems with animation");
    }
  }
  
  public static void sleepForXSeconds( long x ){

    try{
      Thread.sleep( x );
    } catch (InterruptedException e){
      e.printStackTrace();
      System.out.println("BJConstantsAndTools.sleepFor1Second: problems with animation");
    }
  }
  
  
  
  
  /**
   * help method to loading pictures in JavaFX via a URL to the 
   * card picture in the local project file system 
   * @param filename - Name of card to load (e.g. "HEARTS_ACE") 
   * @return URL to requested card picture as a String
   */
  
  public static String getURLStringToFileInCardPictures(String filename) {
    String absolutePath;
    absolutePath = 
        FILE_PICTURE_PROTOKOLL 
        + FILE_USER_USERDIR
        + FILE_PICTURE_PATH
        + filename.trim().toUpperCase() 
        + FILE_PICTURE_FILEENDING;
    
    // if system not windows switch '\' to '/'
    if ( ! System.getProperty("os.name").contains("wind")){
      absolutePath = absolutePath.replace("\\" , "/" );
    }
//    System.out.println(absolutePath);
    return absolutePath;
  }

}