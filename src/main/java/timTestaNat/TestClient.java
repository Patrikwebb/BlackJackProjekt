package timTestaNat;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class TestClient {

  
  Socket socket;
  
  public TestClient(){
    try {
      socket = new Socket("localhost" , 9999);
      
        
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        
        Thread.sleep(1000);
        writer.println("SHOWCARD_SPADES_KING");
        System.out.println("Skickade SHOWCARD");
        writer.flush();
        Thread.sleep(1000);
        writer.println("PLAYER_Tim_123");
        System.out.println("Skickade Players score");
        writer.flush();
        
        writer.close();
        socket.close();
      
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
   
    
  }
  
  public static void main(String[] args) {
    TestClient client = new TestClient();
  }
  
}
