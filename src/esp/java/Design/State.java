package esp.java.Design;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;


public class State
    extends JPanel
    implements MouseListener {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int etat = 0; // 0 = vide, 1 = 1 bombe autour, 2 = 2 bombes autour, 3 = 3 bombes autour, 4 = 4 bombes autour 
	private boolean mine = false; // true si la case contient une bombe
	private boolean selected = false; // true si la case est sélectionnée
	private boolean blocked = false; // true si la case est bloquée
	private int chiffre = 0; // nombre de bombes autour de la case

  private Design gr = null; // objet graphique
  
  public State() {
    try {
      //construction de la case
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.setBackground(Design.dessus);
    this.setMaximumSize(new Dimension(16, 16)); // taille de la case
    this.setMinimumSize(new Dimension(16, 16));
    this.addMouseListener(this);
    this.setPreferredSize(new Dimension(16, 16));
  }

  //**************************Getters & Setters**************************//

  public boolean isMine() {
    return mine;
  }

  public int getEtat() {
    return etat;
  }

  public void setEtat(int etat) {
    this.etat = etat;
  }

  public void setMine(boolean mine) {
    this.mine = mine;
  }

  public int getChiffre() {
    return chiffre;
  }

  public void setChiffre(int chiffre) {
    this.chiffre = chiffre;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
    this.paintComponent(this.getGraphics());
  }
  
  public void setBlocked(boolean blocked) {
	    this.blocked = blocked;
	  }

	  public boolean isBlocked() {
	    return blocked;
	  }

	  public void setGraphisme(Design gr) {
	    this.gr = gr;
	  }

  //**************************Event Listener**************************//
  
  public void mouseClicked(MouseEvent e) {
  }
  
  public void mouseReleased(MouseEvent e) {
    //Déselectionne la case si on relâche le clique
    selected = false;
    repaint();
  }

  public void mousePressed(MouseEvent e) {
	    //Selectionne la case si on clique dessus
	    if (e.getModifiersEx() == 16 && etat != 1 && etat != 2 && !blocked) {
	      selected = true;
	      repaint();
	    }
	  }
  
  public void mouseEntered(MouseEvent e) {
    //Affiche la case si on passe la souris dessus
    if (e.getModifiersEx() == 16 && etat != 1 && etat != 2 && !blocked) {
      selected = true;
      repaint();
    }
  }

  public void mouseExited(MouseEvent e) {
    //Déselectionne la case si on quitte la souris
    selected = false;
    repaint();
  }
  
  @SuppressWarnings("static-access")
public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (gr != null) {
      if (!selected) { // si la case n'est pas sélectionnée
        if (etat == 0) { // si la case est vide
          g.setColor(Color.lightGray); //bordure haut et gauche blanche
          g.drawLine(0, 0, 0, 15);
          g.drawLine(0, 0, 15, 0);
        }
        else if (etat == 1) g.drawImage(gr.chiffre[chiffre], 0, 0, null); //chiffre ou blanc
        else if (etat == 2) g.drawImage(gr.drapeau, 0, 0, null); //drapeau
        else if (etat == 6) g.drawImage(gr.erreur, 0, 0, null); //erreur de drapeau
        else if (etat == 3) g.drawImage(gr.question, 0, 0, null); //?
        else if (etat == 4) g.drawImage(gr.boum, 0, 0, null); //mine sur fond rouge
        else if (etat == 5) g.drawImage(gr.mine, 0, 0, null); //mine
      }
      else { // si la case est sélectionnée
        if (etat == 3) g.drawImage(gr.questionSel, 0, 0, null); //?
        else if (etat != 1) { // du reste du programme
          g.setColor(Color.gray); //bordure haut et gauche grise
          g.drawLine(0, 0, 0, 15);
          g.drawLine(0, 0, 15, 0);
        }
      }
    }
    g.setColor(Color.darkGray); //bordure bas et droite
    g.drawLine(0, 15, 15, 15);
    g.drawLine(15, 0, 15, 15);
    g.dispose();
  }

  public void reset() { // remet la case à 0
    this.etat = 0;
    this.selected = false;
    setMine(false);
    setBlocked(false);
    //repaint();
  }

}
