package kodaLoss;

public class BlackJackConstants {

  public final static String FILE_PICTURE_PROTOKOLL = "file://";
  public final static String FILE_USER_USERDIR = System.getProperty("user.dir");
  public final static String FILE_PICTURE_PATH = "/src/CardPictures/";
  public final static String FILE_PICTURE_FILEENDING = ".png";

  public static String getURLStringToFileInCardPictures(String filename) {
    String absolutePath;
    absolutePath = 
        FILE_PICTURE_PROTOKOLL 
        + FILE_USER_USERDIR
        + FILE_PICTURE_PATH
        + filename.trim().toUpperCase() 
        + FILE_PICTURE_FILEENDING;

    // replace windows slashes
    absolutePath = absolutePath.replace("\\" , "/");

    return absolutePath;
  }

}
