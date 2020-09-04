package arbre;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Options extends JDialog {

	private static final long serialVersionUID = 4137085874346647753L;

	private JComboBox<String> typeCombo, typeFeuillesCombo;
	private JColorChooser couleurChooser;
	private JSlider profondeurSlider, nbBranchesMaxSlider, epaisseurSlider, radiusBranchesSlider, radiusFeuillesSlider,
			niveauAutomneSlider, animationSlider;
	private JTextField nbArbresField;
	private ChangeListener changeListener;
	private PropertyChangeListener propertyChangeListener;
	private ActionListener actionListener;
	private JPanel niveauAutomnePanel, couleurPanel;

	static int estimationBranche = 0, estimationFeuilles = 0;

	private boolean ok = false;

	public Options(JFrame parent, String title, boolean modal, JoliArbreV2 joliArbreV2) {
		super(parent, title, modal);
		this.setSize(800, 400);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		changeListener = new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
			}
		};
		propertyChangeListener = new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
			}
		};
		actionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				niveauAutomnePanel.setVisible(typeCombo.getSelectedIndex() == 2);
				if (typeCombo.getSelectedIndex() == 4) {
					couleurPanel.setVisible(true);
					changeSize(800, 600);
				} else {
					couleurPanel.setVisible(false);
					changeSize(800, 400);
				}

			}
		};
		this.initComponent();
		this.setVisible(true);
		if (!ok)
			System.exit(1);
	}

	protected void changeSize(int x, int y) {
		this.setSize(x, y);
	}

	private void initComponent() {
		// profondeur
		JPanel profondeurPanel = new JPanel();
		profondeurPanel.setBackground(Color.white);
		profondeurPanel.setPreferredSize(new Dimension(230, 80));
		profondeurSlider = new JSlider(JSlider.HORIZONTAL, 0, 15, 8);
		profondeurSlider.addChangeListener(changeListener);
		profondeurSlider.setMajorTickSpacing(5);
		profondeurSlider.setMinorTickSpacing(1);
		profondeurSlider.setPaintTicks(true);
		profondeurSlider.setPaintLabels(true);
		profondeurPanel.setBorder(BorderFactory.createTitledBorder("Profondeur de l'arbre"));
		profondeurPanel.add(profondeurSlider);

		// nbArbres
		JPanel nbArbresPanel = new JPanel();
		nbArbresPanel.setBackground(Color.white);
		nbArbresPanel.setPreferredSize(new Dimension(170, 50));
		nbArbresField = new JTextField();
		nbArbresField.addPropertyChangeListener("nbArbres", propertyChangeListener);
		nbArbresField.setColumns(8);
		nbArbresField.setText("2");
		nbArbresPanel.setBorder(BorderFactory.createTitledBorder("Taille de la forêt"));
		nbArbresPanel.add(nbArbresField);

		// nbBranchesMax
		JPanel nbBranchesMaxPanel = new JPanel();
		nbBranchesMaxPanel.setBackground(Color.white);
		nbBranchesMaxPanel.setPreferredSize(new Dimension(230, 80));
		nbBranchesMaxSlider = new JSlider(JSlider.HORIZONTAL, 2, 6, 4);
		nbBranchesMaxSlider.addChangeListener(changeListener);
		nbBranchesMaxSlider.setMajorTickSpacing(2);
		nbBranchesMaxSlider.setMinorTickSpacing(1);
		nbBranchesMaxSlider.setPaintTicks(true);
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(2), new JLabel("faible"));
		labelTable.put(new Integer(6), new JLabel("Élevé"));
		nbBranchesMaxSlider.setLabelTable(labelTable);
		nbBranchesMaxSlider.setPaintLabels(true);
		nbBranchesMaxPanel.setBorder(BorderFactory.createTitledBorder("Nombre de branches"));
		nbBranchesMaxPanel.add(nbBranchesMaxSlider);

		// type
		JPanel typePanel = new JPanel();
		typePanel.setBackground(Color.white);
		typePanel.setPreferredSize(new Dimension(170, 70));
		String[] types = { "Normal", "Cerisier", "Automne", "Hiver", "Couleur personnalisée" };
		typeCombo = new JComboBox<String>(types);
		typeCombo.setSelectedIndex(0);
		typeCombo.addActionListener(actionListener);
		typePanel.setBorder(BorderFactory.createTitledBorder("Type d'arbre"));
		typePanel.add(typeCombo);

		// typeFeuille
		JPanel typeFeuillesPanel = new JPanel();
		typeFeuillesPanel.setBackground(Color.white);
		typeFeuillesPanel.setPreferredSize(new Dimension(170, 70));
		String[] typesFeuilles = { "Particules", "Ronds", "Triangles" };
		typeFeuillesCombo = new JComboBox<String>(typesFeuilles);
		typeFeuillesCombo.setSelectedIndex(0);
		typeFeuillesPanel.setBorder(BorderFactory.createTitledBorder("Forme des feuilles"));
		typeFeuillesPanel.add(typeFeuillesCombo);

		// couleurPerso
		couleurPanel = new JPanel();
		couleurPanel.setBackground(Color.white);
		couleurPanel.setPreferredSize(new Dimension(650, 250));
		couleurChooser = new JColorChooser(new Color(34, 177, 76));
		couleurChooser.setPreviewPanel(new JPanel());
		couleurPanel.setBorder(BorderFactory.createTitledBorder("Couleur personnalisée"));
		AbstractColorChooserPanel[] panels = couleurChooser.getChooserPanels();
		for (AbstractColorChooserPanel accp : panels) {
			if (!accp.getDisplayName().equals("TSV")) {
				couleurChooser.removeChooserPanel(accp);
			}
		}
		couleurPanel.add(couleurChooser);

		// niveauAutomne
		niveauAutomnePanel = new JPanel();
		niveauAutomnePanel.setBackground(Color.white);
		niveauAutomnePanel.setPreferredSize(new Dimension(230, 80));
		niveauAutomneSlider = new JSlider(JSlider.HORIZONTAL, 1, 9, 2);
		niveauAutomneSlider.addChangeListener(changeListener);
		niveauAutomneSlider.setMajorTickSpacing(3);
		niveauAutomneSlider.setMinorTickSpacing(1);
		niveauAutomneSlider.setPaintTicks(true);
		labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(1), new JLabel("faible"));
		labelTable.put(new Integer(9), new JLabel("Élevée"));
		niveauAutomneSlider.setLabelTable(labelTable);
		niveauAutomneSlider.setPaintLabels(true);
		niveauAutomnePanel.setBorder(BorderFactory.createTitledBorder("Perte des feuilles"));
		niveauAutomnePanel.add(niveauAutomneSlider);

		// animation
		JPanel animationPanel = new JPanel();
		animationPanel.setBackground(Color.white);
		animationPanel.setPreferredSize(new Dimension(230, 80));
		animationSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
		animationSlider.addChangeListener(changeListener);
		animationSlider.setMajorTickSpacing(10);
		animationSlider.setMinorTickSpacing(5);
		animationSlider.setPaintTicks(true);
		labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(0), new JLabel("Lente"));
		labelTable.put(new Integer(100), new JLabel("Instantanée"));
		animationSlider.setLabelTable(labelTable);
		animationSlider.setPaintLabels(true);
		animationPanel.setBorder(BorderFactory.createTitledBorder("Vitesse d'animation"));
		animationPanel.add(animationSlider);

		// epaisseur
		JPanel epaisseurPanel = new JPanel();
		epaisseurPanel.setBackground(Color.white);
		epaisseurPanel.setPreferredSize(new Dimension(230, 80));
		epaisseurSlider = new JSlider(JSlider.HORIZONTAL, 1, 5, 1);
		epaisseurSlider.addChangeListener(changeListener);
		epaisseurSlider.setMajorTickSpacing(3);
		epaisseurSlider.setMinorTickSpacing(1);
		epaisseurSlider.setPaintTicks(true);
		labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(1), new JLabel("Normale"));
		labelTable.put(new Integer(5), new JLabel("Élevée"));
		epaisseurSlider.setLabelTable(labelTable);
		epaisseurSlider.setPaintLabels(true);
		epaisseurPanel.setBorder(BorderFactory.createTitledBorder("Densité de l'arbre"));
		epaisseurPanel.add(epaisseurSlider);

		// radiusBranches
		JPanel radiusBranchesPanel = new JPanel();
		radiusBranchesPanel.setBackground(Color.white);
		radiusBranchesPanel.setPreferredSize(new Dimension(230, 80));
		radiusBranchesSlider = new JSlider(JSlider.HORIZONTAL, 5, 50, 20);
		radiusBranchesSlider.addChangeListener(changeListener);
		radiusBranchesSlider.setMajorTickSpacing(10);
		radiusBranchesSlider.setMinorTickSpacing(5);
		radiusBranchesSlider.setPaintTicks(true);
		labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(5), new JLabel("Petite"));
		labelTable.put(new Integer(50), new JLabel("Énorme"));
		radiusBranchesSlider.setLabelTable(labelTable);
		radiusBranchesSlider.setPaintLabels(true);
		radiusBranchesPanel.setBorder(BorderFactory.createTitledBorder("Taille des branches"));
		radiusBranchesPanel.add(radiusBranchesSlider);

		// radiusFeuilles
		JPanel radiusFeuillesPanel = new JPanel();
		radiusFeuillesPanel.setBackground(Color.white);
		radiusFeuillesPanel.setPreferredSize(new Dimension(230, 80));
		radiusFeuillesSlider = new JSlider(JSlider.HORIZONTAL, 10, 30, 15);
		radiusFeuillesSlider.addChangeListener(changeListener);
		radiusFeuillesSlider.setMajorTickSpacing(10);
		radiusFeuillesSlider.setMinorTickSpacing(2);
		radiusFeuillesSlider.setPaintTicks(true);
		labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(10), new JLabel("Petite"));
		labelTable.put(new Integer(30), new JLabel("Énorme"));
		radiusFeuillesSlider.setLabelTable(labelTable);
		radiusFeuillesSlider.setPaintLabels(true);
		radiusFeuillesPanel.setBorder(BorderFactory.createTitledBorder("Taille des feuilles"));
		radiusFeuillesPanel.add(radiusFeuillesSlider);

		// ok
		JPanel control = new JPanel();
		JButton okBouton = new JButton("OK");
		JPanel separateur = new JPanel();
		separateur.setPreferredSize(new Dimension(20, 1));
		JButton resetBouton = new JButton("Réinitialiser");

		okBouton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JoliArbreV2.profondeurInit = profondeurSlider.getValue();
				String nbArbres = nbArbresField.getText();
				JoliArbreV2.nbArbres = nbArbres.matches("^\\d{1,}$") || nbArbres.equals("0")
						? Integer.parseInt(nbArbres)
						: 1;
				JoliArbreV2.nbBranchesMax = nbBranchesMaxSlider.getValue();
				JoliArbreV2.type = typeCombo.getSelectedIndex() + 1;
				JoliArbreV2.tailleFeuille = radiusFeuillesSlider.getValue() / 1000.0;
				JoliArbreV2.typeFeuille = JoliArbreV2.nbArbres <= 10 ? typeFeuillesCombo.getSelectedIndex() + 1 : 2;
				JoliArbreV2.niveauAutomne = niveauAutomneSlider.getValue() / 10.0;
				JoliArbreV2.superposition = epaisseurSlider.getValue();
				JoliArbreV2.radiusInit = radiusBranchesSlider.getValue() / 100.0;
				JoliArbreV2.couleurFeuilles = couleurChooser.getColor();
				Turtle.animation = animationSlider.getValue() / 100.0;
				setVisible(false);
				ok = true;
			}
		});

		resetBouton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				profondeurSlider.setValue(8);
				nbArbresField.setText("2");
				nbBranchesMaxSlider.setValue(4);
				typeCombo.setSelectedIndex(0);
				typeFeuillesCombo.setSelectedIndex(0);
				couleurChooser.setColor(new Color(34, 177, 76));
				niveauAutomneSlider.setValue(2);
				animationSlider.setValue(100);
				epaisseurSlider.setValue(1);
				radiusBranchesSlider.setValue(20);
				radiusFeuillesSlider.setValue(15);

			}
		});
		control.add(resetBouton);
		control.add(separateur);
		control.add(okBouton);

		JPanel content = new JPanel(), l1 = new JPanel(), l2 = new JPanel(), l3 = new JPanel(), l4 = new JPanel();
		l1.setBackground(Color.white);
		l2.setBackground(Color.white);
		l3.setBackground(Color.white);
		l4.setBackground(Color.white);
		content.setBackground(Color.white);
		l1.add(profondeurPanel);
		l1.add(nbBranchesMaxPanel);
		l1.add(nbArbresPanel);
		l2.add(typeFeuillesPanel);
		l2.add(typePanel);
		l2.add(niveauAutomnePanel);
		l2.add(animationPanel);
		niveauAutomnePanel.setVisible(false);
		l3.add(epaisseurPanel);
		l3.add(radiusBranchesPanel);
		l3.add(radiusFeuillesPanel);
		l4.add(couleurPanel);
		couleurPanel.setVisible(false);

		content.add(l1);
		content.add(l3);
		content.add(l2);
		content.add(l4);

		this.getContentPane().add(content, BorderLayout.CENTER);
		this.getContentPane().add(control, BorderLayout.SOUTH);
	}
}
