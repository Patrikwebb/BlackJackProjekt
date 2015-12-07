package testBlackJack;

import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;
import kodaLoss.Controller;
import kodaLoss.Language;

public class TestControllerLoadRules  extends TestCase{

  
  
  @Test
  public void testGetStringURL2Ressource(){
    String testPath = Controller.getStringURL2Ressource(Language.GERMAN);
    System.out.println(testPath);
    Assert.assertTrue(testPath.equals("file:/Users/timlanghans/git/"
        + "BlackJackProjekt/target/classes/ruleText/GERMAN_RULES.html"));
    
  }
}
