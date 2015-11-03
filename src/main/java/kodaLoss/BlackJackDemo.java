package kodaLoss;

import gui.Gui;

public class BlackJackDemo {

	public static void main(String[] args) {
	  
	  Bank bank = new Bank();
	  new Thread( new Runnable() {
      
      @Override
      public void run() {
        // TODO Auto-generated method stub
      bank.playOneRound();
      }  
      }).start();;
	  
	  
	  
    Gui.launch(Gui.class , args);
    
	}
	
	

}
