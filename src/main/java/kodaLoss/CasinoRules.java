package kodaLoss;

public enum CasinoRules {
 /** 
  * Split the hand of two equal ranks in two hands, 
  * playing both with the bet amount by same player
  */
  SPLIT,
  
  /** 
   * Double the bet and get one extra card in this round
   */
  
  DOUBLE,
  /**
   * Take insurance to secure a part of your 
   * bet if the dealer get Black Jack		</Br >
   * 
   * Insurance PAYS 2-1
   */
  INSURANCE;
	

}
