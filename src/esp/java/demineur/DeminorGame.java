package esp.java.demineur;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import esp.java.Design.State;
import esp.java.Design.Design;
import esp.java.minuteur.BlockCompteur_Mines;
import esp.java.minuteur.Timer;
import java.awt.Font;

//l'interface principale du jeu

public class DeminorGame
    extends JFrame
    implements MouseListener, WindowListener, ActionListener {
  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;
  private JPanel panneauHaut = new JPanel();
  private JPanel panneaugame = new JPanel();
  private GridBagLayout layoutPanneaugame = new GridBagLayout();
  private BlockCompteur_Mines affMines = new BlockCompteur_Mines(); //l'afficheur du nombre de mines
  private JButton boutonNouveau = new JButton();
  private BlockCompteur_Mines affTemps = new BlockCompteur_Mines(); //l'afficheur du temps ecoulé
  private Border borderPanneaux;
  private JMenuBar menu = new JMenuBar();
  private JMenu partie = new JMenu("Partie");
  private JCheckBox pause = new JCheckBox("Pause");
  private JMenuItem menuNouveau = new JMenuItem("Nouveau");
  JCheckBoxMenuItem menuDebutant = new JCheckBoxMenuItem("Debutant");
  JCheckBoxMenuItem menuIntermediaire = new JCheckBoxMenuItem("Intermediaire");
  JCheckBoxMenuItem menuExpert = new JCheckBoxMenuItem("Expert");
  JCheckBoxMenuItem menuPerso = new JCheckBoxMenuItem("Personaliser");
  private BoxLayout layoutPanneauHaut = new BoxLayout(panneauHaut,
      BoxLayout.LINE_AXIS);
  private Component box2;
  private Component box3;
  private Component box1;
  private Component box4;

  private Icon cool, oups, boum, win;

  int nDrapeau = 0; //nombres de drapeaux
  private int nMines;
  private int LARGEUR;
  private int longueur;
  private int nCases; //nombre de cases non decouvertes
  State[][] game; //tableau des cases du game
  private String mines; //repartition des mines
  private int[][] casesSelectionnees = new int[8][2]; //reperages des cases selectionnees pour les deselectionnes lors du relachement de la sourie
  private Timer temps = new Timer(affTemps); //timer sur l'affichage
  private int TYPE;
  private final JButton Fermer = new JButton("X");

  public DeminorGame(int hauteur, int largeur, int mines, int type) {
	longueur = hauteur;
    LARGEUR = largeur;
    nCases = longueur * LARGEUR;
    nMines = mines;
    TYPE = type;
    game = new State[longueur][LARGEUR];

    //creation des cases
    for (int i = 0; i < longueur; i++) {
      for (int j = 0; j < LARGEUR; j++) {
        game[i][j] = new State();
      }
    }

    //selection du bon mode dans le JMenu
    if (type == 1) menuDebutant.setSelected(true);
    if (type == 2) menuIntermediaire.setSelected(true);
    if (type == 3) menuExpert.setSelected(true);
    if (type == 4) menuPerso.setSelected(true);

    //initialisation
    nouveau();

    try {
      //Design
      jbInit();
      this.setVisible(true);
      boutonNouveau.setFont(new Font("Tahoma", Font.BOLD, 11));
      boutonNouveau.setText("Reset");
      boutonNouveau.requestFocus();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  //initialisation du game
  public void nouveau() {
    temps.cancel(); //Timer à 0
    boutonNouveau.setIcon(cool); //Icon par defaut du bouton
    nDrapeau = 0; //nombre de drapeaux poses
    nCases = longueur * LARGEUR;
    affMines.setValeur(nMines);
    affTemps.setValeur(0);
    panneaugame.setBackground(SystemColor.activeCaption);
    panneaugame.setVisible(true); //affichage du panneau de game
    pause.setSelected(false);

    //Generation des mines
    mines = "";
    for (int i = 0; i < nMines; i++) mines = mines + "1";
    while (mines.length() < longueur * LARGEUR) {
      int i = (int) (Math.random() * (mines.length() + 1));
      mines = mines.substring(0, i) + "0" + mines.substring(i);
    }

    //paramètres des cases
    for (int i = 0; i < longueur; i++) {
      for (int j = 0; j < LARGEUR; j++) {
        game[i][j].reset();
        game[i][j].removeMouseListener(this); 
        game[i][j].addMouseListener(this); //ajout du listener sur les cases
        if (mines.charAt(i * LARGEUR + j) == '1') {
          game[i][j].setMine(true);
        }
      }
    }
    repaint();

    //comptage du nombre de mines autour
    for (int i = 0; i < longueur; i++) {
      for (int j = 0; j < LARGEUR; j++) {
        if (!game[i][j].isMine()) {
          int n = 0;
          //try car les cases n'existent pas forcement
          try {
            if (game[i - 1][j - 1].isMine()) n++;
          }
          catch (java.lang.ArrayIndexOutOfBoundsException e) {}
          try {
            if (game[i - 1][j].isMine()) n++;
          }
          catch (java.lang.ArrayIndexOutOfBoundsException e) {}
          try {
            if (game[i - 1][j + 1].isMine()) n++;
          }
          catch (java.lang.ArrayIndexOutOfBoundsException e) {}
          try {
            if (game[i][j - 1].isMine()) n++;
          }
          catch (java.lang.ArrayIndexOutOfBoundsException e) {}
          try {
            if (game[i][j + 1].isMine()) n++;
          }
          catch (java.lang.ArrayIndexOutOfBoundsException e) {}
          try {
            if (game[i + 1][j - 1].isMine()) n++;
          }
          catch (java.lang.ArrayIndexOutOfBoundsException e) {}
          try {
            if (game[i + 1][j].isMine()) n++;
          }
          catch (java.lang.ArrayIndexOutOfBoundsException e) {}
          try {
            if (game[i + 1][j + 1].isMine()) n++;
          }
          catch (java.lang.ArrayIndexOutOfBoundsException e) {}
          game[i][j].setChiffre(n); //on indique à la case le nombre de mines autour
        }
      }
    }
  }

  private void jbInit() throws Exception {
    borderPanneaux = BorderFactory.createEtchedBorder(Color.white,
        new Color(156, 156, 156));
    box2 = Box.createGlue();
    box3 = Box.createGlue();
    box1 = Box.createHorizontalStrut(8);
    box1.setSize(5, 50);
    box4 = Box.createHorizontalStrut(8);
    box4.setSize(5, 50);

    this.addWindowListener(this);

    int tailleX = LARGEUR * 16 + 20; //largeur de la fenetre
    int tailleY = longueur * 16 + 20;
    if (tailleX < 160) tailleX = 150; //largeur minimum

    this.setSize(tailleX + 6, tailleY + 50 + 23 + 25);
    this.setTitle("Demineur");
    this.setResizable(false);

    //MENU DEROULANT
    partie.setMnemonic('P');
    menuNouveau.addActionListener(this);
    menuNouveau.setMnemonic('N');
    menuDebutant.addActionListener(this);
    menuDebutant.setMnemonic('D');
    menuIntermediaire.addActionListener(this);
    menuIntermediaire.setMnemonic('I');
    menuExpert.addActionListener(this);
    menuExpert.setMnemonic('E');
    menuPerso.addActionListener(this);
    menuPerso.setMnemonic('P');
    partie.add(menuNouveau);
    partie.add(new JSeparator());
    partie.add(menuDebutant);
    partie.add(menuIntermediaire);
    partie.add(menuExpert);
    partie.add(menuPerso);
    menu.setBorderPainted(false);
    menu.add(partie);
    pause.setMnemonic('a');
    pause.setOpaque(false);
    pause.setFocusPainted(false);
    pause.addActionListener(this);
    menu.add(pause);
    this.setJMenuBar(menu);
    Fermer.setBackground(new Color(0, 0, 0));
    Fermer.setForeground(new Color(255, 0, 0));
    
    Fermer.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			onExit();
		}
	});
    menu.add(Fermer);

    affMines.setMaximumSize(new Dimension(49, 27));
    affTemps.setMaximumSize(new Dimension(49, 27));
    boutonNouveau.setMaximumSize(new Dimension(60, 60));
    boutonNouveau.setMinimumSize(new Dimension(25, 25));
    panneauHaut.setBackground(SystemColor.activeCaption);
    panneauHaut.setBorder(borderPanneaux);
    panneauHaut.setPreferredSize(new Dimension(450, 50));
    panneauHaut.setLayout(layoutPanneauHaut);
    panneaugame.setBorder(borderPanneaux);
    panneaugame.setPreferredSize(new Dimension(tailleX, tailleY));
    panneaugame.setLayout(layoutPanneaugame);
    affMines.setValeur(nMines);
    affTemps.setValeur(0);
    boutonNouveau.setPreferredSize(new Dimension(60, 60));
    boutonNouveau.setFocusPainted(false);
    boutonNouveau.setIcon(cool);
    boutonNouveau.setMargin(new Insets(0, 0, 0, 0));
    boutonNouveau.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        boutonNouveau_actionPerformed(e);
      }
    });
    panneauHaut.add(box1, null);
    panneauHaut.add(affMines, null);
    panneauHaut.add(box2, null);
    panneauHaut.add(boutonNouveau, null);
    panneauHaut.add(box3, null);
    panneauHaut.add(affTemps, null);
    panneauHaut.add(box4, null);
    this.getContentPane().add(panneauHaut, BorderLayout.NORTH);
    this.getContentPane().add(panneaugame, BorderLayout.CENTER);

    //gr contient le design de toutes les cases
    Design gr = new Design(this.getGraphicsConfiguration());

    //placement des cases dans la fenêtre
    for (int i = 0; i < longueur; i++) {
      for (int j = 0; j < LARGEUR; j++) {
        game[i][j].setGraphisme(gr); //on indique les graphismes à la cases
        panneaugame.add(game[i][j], new GridBagConstraints(j, i, 1, 1, 0.0, 0.0
            , GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0), 0, 0));
      }
    }
  }

  protected void onExit() {
		if (JOptionPane.YES_OPTION == 
				JOptionPane.showConfirmDialog
				(null, "Voulez-vous vraiment quitter ?",
						"Confirmation", 
				JOptionPane.YES_NO_OPTION)) {

			dispose();
		}
	}
  //**************************Ajout des Listeners et des Exceptions**************************//
  
  //Retourne le reperage de la case sous le clic de la souris si elle existe
  public int[] caseClic(int x, int y) {
    int OFFSETX = (int) game[0][0].getX() + 3; //3=largeur du cadre de la fenetre
    int OFFSETY = (int) game[0][0].getY() + 22; //22=hauteur du cadre de la fenetre
    int posx = -1, posy = -1;
    if (x - OFFSETX >= 0) posx = (x - OFFSETX) / 16;
    if (posx >= LARGEUR) posx = -1;
    if (y - OFFSETY >= 0 && posx != -1) posy = (y - OFFSETY) / 16;
    if (posy >= longueur) posy = -1;
    if (posy == -1) posx = -1;
    int[] retour = {
        posx, posy};
    return retour;
  }

  public void mouseClicked(MouseEvent e) {
  }

  public void mousePressed(MouseEvent e) {
    try {
      int x = (int) ( (JPanel) e.getSource()).getLocation().getX() + e.getX() +
          3; //retourne une exception si on est pas au dessus d'un panneau
      int y = (int) ( (JPanel) e.getSource()).getLocation().getY() + e.getY() +
          22;
      int[] coord = caseClic(x, y); //coordonnees de la case enfoncee enregistrees dans coord
      boutonNouveau.setIcon(oups); //bouton

      //si clic droit au dessus d'une case
      if (e.getButton() == e.BUTTON3 && coord[1] != -1 && coord[0] != -1) {
        int temp = game[coord[1]][coord[0]].getEtat();
        switch (temp) {
          case 0: //affichage d'un drapeau
            game[coord[1]][coord[0]].setEtat(2);
            nDrapeau++;
            affMines.setValeur(nMines - nDrapeau);
            break;
          case 2: //affichage d'un ?
            game[coord[1]][coord[0]].setEtat(3);
            nDrapeau--;
            affMines.setValeur(nMines - nDrapeau);
            break;
          case 3: //RAZ
            game[coord[1]][coord[0]].setEtat(0);
            break;
        }
        game[coord[1]][coord[0]].repaint();
      }

      //si clic gauche, on selectionne les cases autour

      y = coord[1];
      x = coord[0];
      if (e.getButton() == e.BUTTON1 && x != -1 && y != -1 &&
          game[y][x].getEtat() == 1 && game[y][x].getChiffre() != 0) {
        //on enregistre les coordonnees des cases selectionnees
        for (int i = 0; i < 7; i++) {
          for (int j = 0; j < 2; j++) {
            casesSelectionnees[i][j] = -1; //effacement de la memoire
          }
        }
        //essai sur les huit cases autour
        try {
          if (game[y - 1][x - 1].getEtat() == 0) {
            game[y - 1][x - 1].setSelected(true);
            casesSelectionnees[0][0] = y - 1;
            casesSelectionnees[0][1] = x - 1;
          }
        }
        catch (Exception exc) {}
        try {
          if (game[y - 1][x].getEtat() == 0) {
            game[y - 1][x].setSelected(true);
            casesSelectionnees[1][0] = y - 1;
            casesSelectionnees[1][1] = x;
          }
        }
        catch (Exception exc) {}
        try {
          if (game[y - 1][x + 1].getEtat() == 0) {
            game[y - 1][x + 1].setSelected(true);
            casesSelectionnees[2][0] = y - 1;
            casesSelectionnees[2][1] = x + 1;
          }
        }
        catch (Exception exc) {}
        try {
          if (game[y][x - 1].getEtat() == 0) {
            game[y][x - 1].setSelected(true);
            casesSelectionnees[3][0] = y;
            casesSelectionnees[3][1] = x - 1;
          }
        }
        catch (Exception exc) {}
        try {
          if (game[y][x + 1].getEtat() == 0) {
            game[y][x + 1].setSelected(true);
            casesSelectionnees[4][0] = y;
            casesSelectionnees[4][1] = x + 1;
          }
        }
        catch (Exception exc) {}
        try {
          if (game[y + 1][x - 1].getEtat() == 0) {
            game[y + 1][x - 1].setSelected(true);
            casesSelectionnees[5][0] = y + 1;
            casesSelectionnees[5][1] = x - 1;
          }
        }
        catch (Exception exc) {}
        try {
          if (game[y + 1][x].getEtat() == 0) {
            game[y + 1][x].setSelected(true);
            casesSelectionnees[6][0] = y + 1;
            casesSelectionnees[6][1] = x;
          }
        }
        catch (Exception exc) {}
        try {
          if (game[y + 1][x + 1].getEtat() == 0) {
            game[y + 1][x + 1].setSelected(true);
            casesSelectionnees[7][0] = y + 1;
            casesSelectionnees[7][1] = x + 1;
          }
        }
        catch (Exception exc) {}
      }
    }
    catch (java.lang.ClassCastException ex) {} //si on est pas au dessus d'un panneau
  }

  public void mouseReleased(MouseEvent e) {

    //Démarrer le compteur si on clique pour la 1ère fois sur une case au hasard
    if (nCases == longueur * LARGEUR && e.getButton() == e.BUTTON1) {
      temps.cancel();
      temps = new Timer(affTemps);
      temps.start();
    }

    try {
      int x = (int) ( (JPanel) e.getSource()).getLocation().getX() + e.getX() +
          3; //retourne une exception si on est pas au dessus d'un panneau
      int y = (int) ( (JPanel) e.getSource()).getLocation().getY() + e.getY() +
          22;
      int[] coord = caseClic(x, y); //coordonnees de la case enfoncee enregistrees dans coord
      boutonNouveau.setIcon(cool); //bouton
      if (coord[0] != -1 && coord[1] != -1) { //si on a clique sur une case
        y = coord[1];
        x = coord[0];
        if (e.getButton() == e.BUTTON1) { //si on a clique gauche
          decouvre(y, x);
          repaint();
        }
        game[y][x].setSelected(false); //on deselectionne la case
        try {
          game[casesSelectionnees[0][0]][casesSelectionnees[0][1]].setSelected(false);
        }
        catch (Exception exc) {}
        try {
          game[casesSelectionnees[1][0]][casesSelectionnees[1][1]].setSelected(false);
        }
        catch (Exception exc) {}
        try {
          game[casesSelectionnees[2][0]][casesSelectionnees[2][1]].setSelected(false);
        }
        catch (Exception exc) {}
        try {
          game[casesSelectionnees[3][0]][casesSelectionnees[3][1]].setSelected(false);
        }
        catch (Exception exc) {}
        try {
          game[casesSelectionnees[4][0]][casesSelectionnees[4][1]].setSelected(false);
        }
        catch (Exception exc) {}
        try {
          game[casesSelectionnees[5][0]][casesSelectionnees[5][1]].setSelected(false);
        }
        catch (Exception exc) {}
        try {
          game[casesSelectionnees[6][0]][casesSelectionnees[6][1]].setSelected(false);
        }
        catch (Exception exc) {}
        try {
          game[casesSelectionnees[7][0]][casesSelectionnees[7][1]].setSelected(false);
        }
        catch (Exception exc) {}
      }
    }
    catch (java.lang.ClassCastException ex) {} //Si le clic n'est pas au dessus d'un panneau
  }

  public void mouseEntered(MouseEvent e) {
  }

  public void mouseExited(MouseEvent e) {
  }

  //Methode qui retourne les coordonnees de la case cliquée
  void boutonNouveau_actionPerformed(ActionEvent e) {
    if (!pause.isSelected()) nouveau();
  }

  //methode pour decouvrir les cases
  public void decouvre(int y, int x) {
    //Si la case est normale ou avec un ?
    if ( (game[y][x].getEtat() == 0 || game[y][x].getEtat() == 3) &&
        !game[y][x].isMine()) {
      nCases--; //nombre de cases non decouvertes
      game[y][x].setEtat(1); //on indique que la case est decouverte
      if (game[y][x].getChiffre() == 0) { // Si le nombre de mines autour est nul, on decouvre les cases autour
        decouvrirPartiel1(x - 1, y - 1);
        decouvrirPartiel1(x - 1, y);
        decouvrirPartiel1(x - 1, y + 1);
        decouvrirPartiel1(x, y - 1);
        decouvrirPartiel1(x, y + 1);
        decouvrirPartiel1(x + 1, y - 1);
        decouvrirPartiel1(x + 1, y);
        decouvrirPartiel1(x + 1, y + 1);
      }
    }

    //Si on est au dessus d'un chiffre
    else if (game[y][x].getEtat() == 1 && game[y][x].getChiffre() != 0) {
      int n = 0; //on compte le nombre de drapeaux places
      if (decouvrirPartiel2(x - 1, y - 1)) n++;
      if (decouvrirPartiel2(x - 1, y)) n++;
      if (decouvrirPartiel2(x - 1, y + 1)) n++;
      if (decouvrirPartiel2(x, y - 1)) n++;
      if (decouvrirPartiel2(x, y + 1)) n++;
      if (decouvrirPartiel2(x + 1, y - 1)) n++;
      if (decouvrirPartiel2(x + 1, y)) n++;
      if (decouvrirPartiel2(x + 1, y + 1)) n++;

      if (n == game[y][x].getChiffre()) { //si il y en a autant que le nombre de mines autour, on decouvre les 8 cases autour par un appel recursif de decouvre(int, int)
        if (decouvrirPartiel3(x - 1, y - 1)) decouvre(y - 1, x - 1);
        if (decouvrirPartiel3(x - 1, y)) decouvre(y, x - 1);
        if (decouvrirPartiel3(x - 1, y + 1)) decouvre(y + 1, x - 1);
        if (decouvrirPartiel3(x, y - 1)) decouvre(y - 1, x);
        if (decouvrirPartiel3(x, y + 1)) decouvre(y + 1, x);
        if (decouvrirPartiel3(x + 1, y - 1)) decouvre(y - 1, x + 1);
        if (decouvrirPartiel3(x + 1, y)) decouvre(y, x + 1);
        if (decouvrirPartiel3(x + 1, y + 1)) decouvre(y + 1, x + 1);
      }
    }

    //Si on clique sur une mine
    else if ( (game[y][x].getEtat() == 0 || game[y][x].getEtat() == 3) &&
             game[y][x].isMine()) {
      temps.cancel(); //fin du timer
      game[y][x].setEtat(4); //boum
      boutonNouveau.setIcon(boum);
      for (int i = 0; i < longueur; i++) {
        for (int j = 0; j < LARGEUR; j++) {
          game[i][j].removeMouseListener(this); //on bloque les cases
          game[i][j].setBlocked(true);
          if (! (y == i && x == j) && mines.charAt(i * LARGEUR + j) == '1' &&
              game[i][j].getEtat() != 2)

            //si il ya une mine, (recherche par rapport à la chaîne mines
            game[i][j].setEtat(5); //on l' affiche
        }
      }
      //on affiche les erreurs
      for (int i = 0; i < longueur; i++) {
        for (int j = 0; j < LARGEUR; j++) {
          if (game[i][j].getEtat() == 2 && !game[i][j].isMine()) game[i][j].
              setEtat(6);
        }
      }
    }
    //Si on gagne, c'est à dire le nombre de cases restantes est egal au nombre de mines restantes
    if (nCases == nMines && !game[0][0].isBlocked()) {
      temps.cancel(); //fin du timer
      affMines.setValeur(0);
      boutonNouveau.setIcon(win);
      for (int i = 0; i < longueur; i++) {
        for (int j = 0; j < LARGEUR; j++) {
          game[i][j].removeMouseListener(this); //on bloque les cases
          game[i][j].setBlocked(true);
          if (game[i][j].isMine()) game[i][j].setEtat(2); //on affiche les mines
        }
      }
      EventMessage app = new EventMessage(this, "Demineur", true);
      app.setLocation( (int)this.getLocation().getX() + 20,
                      (int)this.getLocation().getY() + 20);
      app.setVisible(true);
    }
    
  }

  //si la case existe, on la decouvre et si necessaire, on appelle le decouvrement des cases autour
  public void decouvrirPartiel1(int x, int y) {
    if (x >= 0 && y >= 0 && x < LARGEUR && y < longueur) {
      if (game[y][x].getEtat() == 0 && game[y][x].getChiffre() != 0) {
        game[y][x].setEtat(1);
        nCases--;
      }
      if (game[y][x].getEtat() == 0 && game[y][x].getChiffre() == 0)
        decouvre(y, x); //Si le nombre de mines autour est nul, on decouvre les cases autour
    }
  }

  //verifie si la case existe et si elle porte un drapeau
  public boolean decouvrirPartiel2(int x, int y) {
    if (x >= 0 && y >= 0 && x < LARGEUR && y < longueur) {
      if (game[y][x].getEtat() == 2)
        return true;
    }
    return false;
  }

  //verifie si la case existe et si elle n'est pas decouverte ou si elle porte un '?'
  public boolean decouvrirPartiel3(int x, int y) {
    if (x >= 0 && y >= 0 && x < LARGEUR && y < longueur) {
      if (game[y][x].getEtat() == 0 || game[y][x].getEtat() == 3)
        return true;
    }
    return false;
  }

  public void windowOpened(WindowEvent e) {
  }

  public void windowClosing(WindowEvent e) {
    temps.stop(); //stop le timer avant de quitter
    System.exit(0);
  }

  public void windowClosed(WindowEvent e) {
  }

  public void windowIconified(WindowEvent e) {
    try {
      temps.suspend();
    } //pause du timer si il existe!!
    catch (Exception esc) {}
  }

  public void windowDeiconified(WindowEvent e) {
    try {
      temps.resume();
    } //reprise du timer (si il existe!!)
    catch (Exception esc) {}
  }

  public void windowActivated(WindowEvent e) {
  }

  public void windowDeactivated(WindowEvent e) {
  }

  //evenements lies au menu
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == menuNouveau) nouveau();
    else if (e.getSource() == menuDebutant && TYPE != 1) {
      this.dispose(); // on detruit la fenetre
      System.gc();
      if (TYPE == 1) menuDebutant.setSelected(true);
      DeminorGame demineur = new DeminorGame(9, 9, 10, 1); //et on en refait une
    }
    else if (e.getSource() == menuDebutant && !menuDebutant.isSelected())
      menuDebutant.setSelected(true);
    else if (e.getSource() == menuIntermediaire && TYPE != 2) {
      this.dispose(); // on detruit la fenetre
      System.gc();
      if (TYPE == 2) menuIntermediaire.setSelected(true);
      DeminorGame demineur = new DeminorGame(16, 16, 40, 2);
    }
    else if (e.getSource() == menuIntermediaire &&
             !menuIntermediaire.isSelected()) menuIntermediaire.setSelected(true);
    else if (e.getSource() == menuExpert && TYPE != 3) {
      this.dispose(); // on detruit la fenetre
      System.gc();
      if (TYPE == 3) menuExpert.setSelected(true);
      DeminorGame demineur = new DeminorGame(16, 30, 99, 3);
    }
    else if (e.getSource() == menuExpert && TYPE != 4) menuExpert.setSelected(true);
    else if (e.getSource() == menuPerso) {
      //un peu particulier : tout est gere par la fenetre de personalisation
      if (TYPE == 4) menuPerso.setSelected(true);
      else menuPerso.setSelected(false);
      Perso perso = new Perso(this, "Paramètres", true, longueur,
                                            LARGEUR, nMines);
      perso.setLocation( (int)this.getLocation().getX() + 20,
                        (int)this.getLocation().getY() + 20);
      perso.setVisible(true);
    }
    else if (e.getSource() == pause) {
      if (pause.isSelected()) {
        panneaugame.setVisible(false);
        temps.suspend();
      }
      else {
        panneaugame.setVisible(true);
        temps.resume();
      }
    }
    
  }
}
