package timTestaNat;

import static timTestaNat.BlackJackConstants.FILE_PICTURE_FILEENDING;
import static timTestaNat.BlackJackConstants.FILE_PICTURE_PATH;

import java.awt.Dimension;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import kodaLoss.Rank;
import kodaLoss.Suite;


/**
 * Loads Pictures from the specified picture-directory in a jagged Image-array.
 * Indeces correspond to the ordinals of Suite and Ranks of the cards. OBS!
 * Method assumes that image-files are named after the Cards specified by Rank
 * and Suite enum: e.g. "/picturedir/DIAMONDS KING.png"
 * 
 * @author timlanghans
 *
 */
public class CardPictureData {

  private Image[][] imageData;

  public CardPictureData() {
    imageData = new Image[4][13];
    for (Suite s : Suite.values()) {
      for (Rank r : Rank.values()) {
        File imgFile;
        try {
          imgFile = new File(FILE_PICTURE_PATH + s.toString() + " "
              + r.toString() + FILE_PICTURE_FILEENDING);

          imageData[s.ordinal()][r.ordinal()] = ImageIO.read(imgFile);
        } catch (Exception e) {
          // TODO exception handling;
        }
      }
    }
  }

  /**
   * get the picture corresponding to a Card
   * 
   * @param s
   *          Suite of card
   * @param r
   *          Rank of card
   * @return Image
   */
  public Image getPictureByNameOfCard(Suite s, Rank r) {
    return imageData[s.ordinal()][r.ordinal()];
  }

  
  
  
  
  // just for showing a card, testing! 
  private static void showPreviewOfPicture( Image img ){
    PicResizePanel panel = new PicResizePanel(img);
    JFrame frame = new JFrame("PREVIEW");
    frame.add(panel);
    frame.setMinimumSize(new Dimension(150, 200));
    frame.pack();
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setVisible(true);
  }
  
  
  public void serveCardTest(){
    try {
      ServerSocket ssocket = new ServerSocket(9999);
      System.out.println("Server socket on port 9999 ");
      Socket socket = ssocket.accept();
      System.out.println("Socket bound!");
      BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      String message;
      while(true){
        message = reader.readLine();
        
        if (message !=null)
          actionEventConverter (message);
      }
      
      
      
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  
  
  private void actionEventConverter(String message) {
    String[] tokens = message.split("[_]+");
    System.out.println(Arrays.deepToString(tokens));
    
    
    switch(tokens[0]){
      case ("SHOWCARD"):
        showPreviewOfPicture(getPictureByNameOfCard(
            Suite.valueOf(tokens[1]),
            Rank.valueOf(tokens[2])));
        break;
      case ("PLAYER"):
        System.out.println("Player " + tokens[1] + " has score " + tokens[2]);
        break;
      default :
        System.out.println("CANNOT READ ACTIONMESSAGE from CLIENT!!!");
        break;
    }
  }

  public static void main(String[] args) {
    System.out.println(Suite.valueOf("DIAMONDS"));
   CardPictureData cpd = new CardPictureData();
   cpd.serveCardTest();
   
   

  }

}
