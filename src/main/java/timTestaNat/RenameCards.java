package timTestaNat;

import java.io.File;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import kodaLoss.Rank;
import kodaLoss.Suite;

public class RenameCards {

  
  
  
  
  public static void main(String[] args) {
    // TODO Auto-generated method stub

    
   
    File folder = new File("/Users/timlanghans/Desktop/pics/images");
    
    File[] files = folder.listFiles();
    
    for (File f : files){
      if (f.getName().matches("[1-9].png")){
        f.renameTo( new File(f.getParentFile().getAbsolutePath() + "/0" + f.getName()));
      } 
      
      if (!f.getName().matches("[0-9]+.png")){
        f.delete();
      }
    }
    
    files = folder.listFiles();
    
    
    Arrays.sort(files);
    System.out.println(Arrays.deepToString(files));
    
   int answer =  JOptionPane.showConfirmDialog(null, "Rename them in this order?");
    if (! (answer == JOptionPane.YES_OPTION)){
      System.exit(1);
    }
    
    final int count = files.length; 
    Rank[] ranks = Rank.values(); // 13
    Suite[] suites = Suite.values(); // 4
    
    int fil = 0;
    
    String newFileName = folder.getAbsolutePath() + "/";
    String fileEnding = ".png";
   
      for (int r = ranks.length - 1 ; r >= 0 ; r--){
        for (int s = suites.length -1  ; s >= 0 ; s--){
          System.out.println(fil + " renamed to " + suites[s] + "_" + ranks[r]);
          files[ fil ].renameTo( new File ( newFileName + suites[s] + "_" +ranks[r] + fileEnding ));
          fil++;
        }
      }
      
      
      
    System.out.println("files renamed in " + folder.getAbsolutePath());
    
    
    
    
  }

}
