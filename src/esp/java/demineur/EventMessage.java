package esp.java.demineur;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.*;


public class EventMessage extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private JPanel panel = new JPanel();
  private GridBagLayout gridBagLayout = new GridBagLayout();
  private JLabel titre = new JLabel();
  private JButton continuer = new JButton();
  private JLabel vide2 = new JLabel();
  private Border border1;
  private JLabel vide1 = new JLabel();

  public EventMessage(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public EventMessage() {
    this(null, "", false);
  }
  private void jbInit() throws Exception {
    border1 = BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,SystemColor.info,SystemColor.info,new Color(109, 109, 109),new Color(156, 156, 156)),BorderFactory.createEmptyBorder(0,10,0,10));
    panel.setLayout(gridBagLayout);
    titre.setHorizontalAlignment(SwingConstants.CENTER);
    titre.setFont(new java.awt.Font("Serif", 1, 20));
    titre.setForeground(SystemColor.info);
    titre.setBorder(border1);
    titre.setPreferredSize(new Dimension(131, 50));
    titre.setText("YOU WIN");
    panel.setBackground(SystemColor.activeCaption);
    continuer.setFont(new java.awt.Font("Dialog", 0, 10));
    continuer.setFocusPainted(false);
    continuer.setMnemonic('C');
    continuer.setText("Continuer");
    continuer.addActionListener(this);
    vide2.setPreferredSize(new Dimension(200, 17));
    vide1.setPreferredSize(new Dimension(200, 17));
    getContentPane().add(panel);
    panel.add(titre,     new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
    panel.add(continuer,       new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
    panel.add(vide2,   new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    panel.add(vide1,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
  }
  public void actionPerformed(ActionEvent e) {
    this.setVisible(false);
  }
}