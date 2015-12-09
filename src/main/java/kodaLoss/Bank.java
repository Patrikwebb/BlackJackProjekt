package kodaLoss;

import static kodaLoss.BlackJackConstantsAndTools.BANK_LIMIT;
import static kodaLoss.BlackJackConstantsAndTools.MIN_BET;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Bank {

  // MEMBERS Patrik, Tim, Johannes , Johan

  /*
   * Reference to UserChoiceAdapter just because its easier to write 'uca' than
   * 'UserChoiceAdapter.getInstance().xxx' all the time
   */
  private static UserChoiceAdapter uca = UserChoiceAdapter.getInstance();

  // Singleton object of Bank
  private static Bank bank = new Bank();

  // Banks reference to the Controller object
  protected static IController controller = null;

  public List<Player> registeredPlayers = new ArrayList<Player>();

  // 4 Decks of shuffled cards
  CardShoe cardShoe = new CardShoe();

  // a round plays in its own thread for GUI-responsivity
  public AbstractRound roundThread = null;
  /*
   * reference to the Player objects of dealer and the active player to be shown
   * on the GUI. Needed for updating the GUI.
   */
  public Player dealer = new Player("Dealer", 0);
  Player activePlayerOnGui;

  private static final String DEFAULT_RULE = "kodaLoss.BasicRound";
  
  private static String ruleForNextRound = DEFAULT_RULE;
  
  private static Map<String,String> ruleMapping;


  // CONSTRUCTORS
  // private because of Singleton pattern
  private Bank() {
    System.out.println("Bank started...");
    
    mapRules();
  }
  
  
// sends the selectable rules to ComboBox
  private static void setUpGui() {
    controller.setPlayableRules(ruleMapping.keySet());
  }


// Mapping of the rules that can be played. Add new Rules (Round-types) here!
  private void mapRules() {
    ruleMapping = new TreeMap<>();
    ruleMapping.put("Basic", "kodaLoss.BasicRound");
    ruleMapping.put("Casino", "kodaLoss.CasinoRound");
  }


  /**
   * Returns a reference to the one and only Bank object
   * 
   * @return reference to Bank-object singleton
   */
  public static Bank getInstance() {
    return bank;
  }

  // METHODS TO REGISTER CONTROLLER
  /**
   * Adds a reference to the controller class to the Bank object. Needed for
   * Bank to be able to request changes in GUI.
   * 
   * @param control
   *          - a controller object
   */
  public static void registerController(IController control) {

    controller = control;
    setUpGui();
  }

  // METHODS FOR GUI

  /*
   * called after changes in player or dealer model. Updates the GUI by directly
   * calling methods in Controller. Bank decides this way when GUI should be
   * changed
   */
  void updateGuiAfterChangeInDataModel() {

    if (controller != null) {
    	for (Player p : bank.registeredPlayers) {
      controller.updatePlayer(p);
    	}
      controller.updateDealer(dealer);
    } else
      System.out.println("ERROR: Controller not set in bank!");
  }
  
  /**
   * This method controls the sequence of actions to play one round, control of
   * the gameplay! Has to run in its own thread because method blocks while
   * waiting for user input!
   */
  public void playOneRound() {

    // A ROUND CANNOT BE PLAYED WITHOUT A REGISTERED CONTROLLER!
    if (controller == null) {
      System.out.println("CONTROLLER has to be set!");
      throw new IllegalStateException("Controller has to be set! ");
    }

    // DO not start a new game, when a game is running!
    if (roundThread != null) {
      System.out.println("Already running a round");

    } else if (roundThread == null || !roundThread.isAlive()) {
      // start a new Round in its own Thread for not freezing the GUI!
      // Mapping<String,String> Rule name for gui <--> class to create below 
      try {
        roundThread = (AbstractRound)Class.forName( ruleForNextRound ).newInstance();
      } catch (InstantiationException | IllegalAccessException
          | ClassNotFoundException e) {
        
        e.printStackTrace();
        throw new RuntimeException("CANNOT START RULE-MODEL");
      }
      
//      roundThread = new CasinoRound(this);
//      roundThread = new BasicRound(this);
      
      roundThread.start();
    }
  }
  
 /*
   * reset Bank before next round can start, inform GUI.
   */
  final void resetBeforeNextRound() {
    controller.setlabelWinnerText("");
    roundThread = null;
    controller.gameIsoff(); // for player to choose DEAL! DONT FORGET!
  }
  
  public void activatePlayerToSetBet(){
	
	for (Player p : registeredPlayers){
		
	    controller.activatePlayersBetField(true);
	
	    controller.setlabelWinnerText(
	        p.getName()  + BlackJackConstantsAndTools.ASK_FOR_BETS);
	}
  }
  public void setPlayersDefaultBet(){
	  controller.getBetFromPlayersTextField();
  }

//  /**
//   * 
//   * Check if the dealer got an ACE on the first DEAL,
//   * And activates the button so the player can choice to use Insurance
//   * 
//   */
//  public void activateInsurance(){
//	  
//	  if (Bank_HelpersAndTools.checkForAceCardOnYourHand(dealer) == true){
//		  
//		  System.out.println("dealer.getPlayerHandsSize " + dealer.getPlayerHandsSize());
//		  
//		  // Denna if sats behövs egentligen inte, 
//		  // men hjälper till för att inte få möjliga buggar
//		  if (dealer.getPlayerHandsSize() == 1){
//			  controller.activateInsuranceButton();
//		  } else {
//			  System.out.println("\nDealer got more the one card on hand\n");
//		  }
//	  // For testing 
//	  } else {
//		  System.out.println("\nDealer aint got any ACE on first hand\n");
//	  }
//  }
//
//  /*
//   * dealer plays. Takes cards until its hand is over 16
//   */

  /*
   * deals one card to all players and dealer
   */
  protected void dealOneCardToAll() {

    for (Player p : registeredPlayers) {
      dealOneCardToPlayer(p);
    }
    dealOneCardToPlayer(dealer);
    updateGuiAfterChangeInDataModel();
  }

  /*
   * bank deals one card from the card shoe to a player who takes the card and
   * adds it to his hand. (Then the gui is updated for the player)
   */
  void dealOneCardToPlayer(Player player) {
    player.addCardToHand(cardShoe.getACardFromCardShoe());
    updateGuiAfterChangeInDataModel();
  }



  // NON STATIC HELPER FUNCTIONS
  public void addPlayerToBank(Player player) {

    if (registeredPlayers
        .size() <= BlackJackConstantsAndTools.PLAYERS_MAX_COUNT) {
      this.registeredPlayers.add(player);
    }
  }

  /**
   * Adds a player to the bank from the inlog-window
   * Checks that player cannot bring to much or too little money to
   * the table according to the Banks limits.
   * @param name
   * @param playerCash
   */
  public void addPlayerToBank(String name, Integer playerCash) {
    
    int cash = (int)Math.abs(playerCash.intValue());
    
    if (cash >= BANK_LIMIT){
      cash = BANK_LIMIT;
    } else if(cash < MIN_BET){
      cash = MIN_BET;
    }
    
    this.registeredPlayers.add(new Player(name, cash));
  }

  public void addPlayersToTheTable() {
    int rp = registeredPlayers.size() - 1;
    activePlayerOnGui = registeredPlayers.get(rp);
    updateGuiAfterChangeInDataModel();
  }

  /*
   * Method to reset Bank to a default start state. Clears all players and
   * dealer, gets a new Card Shoe.
   */
  public void resetBank() {
    registeredPlayers.clear();
    dealer = new Player("Dealer", 0);
    cardShoe = new CardShoe();
    roundThread = null;
  }

  /**
   * Sets the requested rule to be played in the next round. 
   * 
   * @param value - selected rule from the rule-combobox on GUI as String
   */
  public void setRule(String value) {
    
    if (value == null){
      return;
    }
    String ruleClass = ruleMapping.getOrDefault(value , DEFAULT_RULE);
    ruleForNextRound = ruleClass;
  }
}
