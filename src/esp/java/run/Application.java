package esp.java.run;

import javax.swing.UIManager;

import esp.java.demineur.DeminorGame;



public class Application {

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    new DeminorGame(16,30,50,3);
  } 
}
