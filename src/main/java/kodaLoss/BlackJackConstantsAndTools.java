package kodaLoss;

public class BlackJackConstantsAndTools {

  public final static String FILE_PICTURE_PROTOKOLL = "file://";
  public final static String FILE_USER_USERDIR = System.getProperty("user.dir");
  public final static String FILE_PICTURE_PATH = "/src/CardPictures/";
  public final static String FILE_PICTURE_FILEENDING = ".png";

  
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

    // replace windows slashes and remove white spaces
    if (System.getProperty("os.name").toLowerCase().contains("window")){
     absolutePath = absolutePath.replace("/" , "\\"); 
    } else {
      absolutePath = absolutePath.replace("\\" , "/");
    }
    // no whitespaces allowed!
    absolutePath = absolutePath.replace(" " , "_");
    return absolutePath;
  }

}
