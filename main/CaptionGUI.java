import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

public class CaptionGUI {
	private static final Color COL0 = Color.RED; 			 // Da -75 a -90 dbm valore più basso
	private static final Color COL1 = Color.ORANGE;			 // Da -60 a -75 dbm
	private static final Color COL2 = Color.YELLOW.darker(); // Da -45 a -60 dbm
	private static final Color COL3 = Color.GREEN;			 // Da -30 a -45 dbm
	private static final Color COL4 = Color.CYAN;			 // > -30 dbm valore più alto
	private Color[] tones = { COL0, COL1, COL2, COL3, COL4 };
	private int WIDTH, HEIGHT, DIM_SQUARE;
	private JPanel captionPanel;
	private JPanel colorPanel;
	private static int index = -1;
	
	public CaptionGUI(int width, int height, int dim_square) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.DIM_SQUARE = dim_square;
		setCaptionPanel();
		setColorPanel();
	}
	
	public Color[] getTones() {
		return this.tones;
	}
	
	public void setCaptionPanel() {
		final JPanel captionPanel = new JPanel(new BorderLayout());
		final JTextArea captionTxtArea = new JTextArea();
		captionTxtArea.setLineWrap(true);
		captionTxtArea.setText("Effetto del materiale dell'ostacolo su un segnale radio\r\n"
				+ "A seconda del materiale, gli ostacoli possono riflettere le onde radio, assorbirle, privandole di una parte della potenza, o non avere alcun effetto sul segnale radio. Tali materiali sono chiamati radiotrasparenti. Più alto è il coefficiente di assorbimento del segnale e più spesso è l'ostacolo, più forte è l'impatto sulla trasmissione radio.\r\n"
				+ "Coefficiente di assorbimento del segnale\r\n" + "Basso\r\n"
				+ "Perdita di potenza del 50%\r\n" + "- Mattone rosso secco di 90 mm di spessore\r\n"
				+ "- Pannello di gesso di 100 mm di spessore\r\n" + "- Legno secco di 80 mm di spessore\r\n"
				+ "- Vetro di 15 mm di spessore\r\n" + "\r\n" + "Medio\r\n"
				+ "La potenza si riduce di 10 volte\r\n" + "- Mattone di 250 mm di spessore\r\n"
				+ "- Blocchi di calcestruzzo di 200 mm di spessore\r\n"
				+ "- Calcestruzzo di 100 mm di spessore\r\n" + "- Muratura di 200 mm di spessore\r\n" + "\r\n"
				+ "Alto\r\n" + "La potenza si riduce di 100 volte\r\n"
				+ "- Calcestruzzo di 300 mm di spessore\r\n" + "- Calcestruzzo armato di 200 mm di spessore\r\n"
				+ "- Travi in alluminio e acciaio\r\n" + "\r\n" + "\r\n"
				+ "Di default, l'edificio è composto dal solo piano Terra, ma si possono aggiungere piani superiori o sotterranei\r\n"
				+ "Su ogni piano, si provvede uno spazio di " + WIDTH / 100 + " metri per " + HEIGHT / 100
				+ " in cui riprodurre la pianta dell'edificio; valori illegali saranno considerati 0.\r\n"
				+ "Porte, finestre interne ed esterne od altre aperture fra stanze sono sempre considerate chiuse ai fini della rilevazione del segnale: si ipotizza il segnale minimo nel caso peggiore.\r\n"
				+ "La precisione massima nel piazzamento di un muro è di " + DIM_SQUARE
				+ " centimetri, sugli assi x e y; non si accettano muri diagonali.\r\n"
				+ "Piani strutturati inframmezzati da piani vuoti od incompleti sono tollerati: si presume che le relative planimetrie siano ininfluenti ai fini della simulazione dell'utente.\r\n"
				+ "\r\n" + "\r\n"
				+ "Per selezionare un campo da abilitare/disabilitare/cancellare è sufficiente indicarne la posizione e premere il relativo pulsante; non serve impostare correttamente il resto dei campi.\r\n"
				+ "\r\n" + "\r\n"
				+ "Secondo le normative ETSI EN, la potenza di emittenti wireless in un edificio non può superare i 200 milliWatt e la frequenza deve essere compresa nella banda 5150-5350 MegaHertz.\r\n"
				+ "Gli emittenti devono essere distanziati di almeno " + DIM_SQUARE / 2
				+ " centimetri ed i muri si possono intersecare ma non compenetrare.\r\n"
				+ "Gli angoli di inizio e di fine sono 0 e 360 per antenne omnidirezionali; per antenne direzionali, l'ampiezza è calcolata in senso orario con 0° = 360° = ore 3.\r\n"
				+ "\r\n"
				+ "Si presume che tutti gli emittenti, direzionali ed omnidirezionali, siano ottimizzati per trasmettere segnali nel piano in cui sono collocati: i segnali propagati verticalmente sono considerati diffusione.\r\n"
				+ "Il guadagno rispetto ad un'emittente isotropica si ritiene già incluso nella potenza; se così non fosse, triplicare quanto si intendeva inserire\r\n");
		final JScrollPane scroll = new JScrollPane(captionTxtArea);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		captionTxtArea.setEditable(false);
		captionTxtArea.setCaretPosition(0);// Scroll parte dalla cima
		captionPanel.add(scroll);
		this.captionPanel = captionPanel;
	}
	
	public JPanel getCaptionpanel() {
		return captionPanel;
	}
	
	public void setColorPanel() {
		final JPanel colorPanel = new JPanel();// Panel containing colors btn/lbl
		GroupLayout colorLayout = new GroupLayout(colorPanel);
		colorPanel.setLayout(colorLayout);

		final JLabel captionLbl = new JLabel(
				"L'intensità del segnale è così rappresentata (dbm: decibel milliwatt)");
		final JLabel captionExplanationLbl = new JLabel(
				"Selezionare un range di dbm per visionare il colore rappresentante; se si desidera utilizzare una tonalità diversa, alterare i valori (numeri da 0 a 255)e premere Applica.");
		final JMenuBar colorMenuBar = new JMenuBar();
		final JMenu colorMenu = new JMenu("Range");
		final JMenuItem menu0 = new JMenuItem("Da -75 a -90 dbm");
		final JMenuItem menu1 = new JMenuItem("Da -60 a -75 dbm");
		final JMenuItem menu2 = new JMenuItem("Da -45 a -60 dbm");
		final JMenuItem menu3 = new JMenuItem("Da -30 a -45 dbm");
		final JMenuItem menu4 = new JMenuItem("> -30 dbm");
		colorMenuBar.add(colorMenu); // Setting MenuBar
		colorMenu.add(menu0);
		colorMenu.add(menu1);
		colorMenu.add(menu2);
		colorMenu.add(menu3);
		colorMenu.add(menu4);
		final JLabel captionLblR = new JLabel("R");
		final JTextField captionTxtR = new JTextField();
		final JLabel captionLblG = new JLabel("G");
		final JTextField captionTxtG = new JTextField();
		final JLabel captionLblB = new JLabel("B");
		final JTextField captionTxtB = new JTextField();
		final JButton changeColorBtn = new JButton("Applica");
		final JLabel captionError = new JLabel();
		captionError.setBackground(Color.BLACK);

		colorLayout.setAutoCreateGaps(true);
		colorLayout.setAutoCreateContainerGaps(true);

		colorLayout.setHorizontalGroup(colorLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(captionLbl).addComponent(captionExplanationLbl).addComponent(colorMenuBar)
				.addGroup(colorLayout.createSequentialGroup()
						.addGroup(colorLayout.createSequentialGroup().addComponent(captionLblR)
								.addComponent(captionTxtR))
						.addGroup(colorLayout.createSequentialGroup().addComponent(captionLblG)
								.addComponent(captionTxtG))
						.addGroup(colorLayout.createSequentialGroup().addComponent(captionLblB)
								.addComponent(captionTxtB)))
				.addGroup(colorLayout.createSequentialGroup().addComponent(changeColorBtn)
						.addComponent(captionError)));

		colorLayout.setVerticalGroup(colorLayout.createSequentialGroup().addComponent(captionLbl)
				.addComponent(captionExplanationLbl).addComponent(colorMenuBar)
				.addGroup(colorLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addGroup(colorLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(captionLblR).addComponent(captionTxtR))
						.addGroup(colorLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(captionLblG).addComponent(captionTxtG))
						.addGroup(colorLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(captionLblB).addComponent(captionTxtB)))
				.addGroup(colorLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(changeColorBtn).addComponent(captionError)));
		
		// Btn action Listeners
		menu0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				index = 0;
				captionTxtR.setText("" + tones[index].getRed());
				captionTxtG.setText("" + tones[index].getGreen());
				captionTxtB.setText("" + tones[index].getBlue());
			}
		});

		menu1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				index = 1;
				captionTxtR.setText("" + tones[index].getRed());
				captionTxtG.setText("" + tones[index].getGreen());
				captionTxtB.setText("" + tones[index].getBlue());
			}
		});

		menu2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				index = 2;
				captionTxtR.setText("" + tones[index].getRed());
				captionTxtG.setText("" + tones[index].getGreen());
				captionTxtB.setText("" + tones[index].getBlue());
			}
		});

		menu3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				index = 3;
				captionTxtR.setText("" + tones[index].getRed());
				captionTxtG.setText("" + tones[index].getGreen());
				captionTxtB.setText("" + tones[index].getBlue());
			}
		});

		menu4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				index = 4;
				captionTxtR.setText("" + tones[index].getRed());
				captionTxtG.setText("" + tones[index].getGreen());
				captionTxtB.setText("" + tones[index].getBlue());
			}
		});

		changeColorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (index < 0) {
					captionError.setText("Selezionare un range");
					colorMenuBar.setBorder(new LineBorder(Color.red, 1));
					return;
				}
				captionTxtR.setBorder(new JTextField().getBorder());
				captionTxtG.setBorder(new JTextField().getBorder());
				captionTxtB.setBorder(new JTextField().getBorder());
				colorMenuBar.setBorder(new JTextField().getBorder());
				captionError.setText("");
				int r = 0, g = 0, b = 0;
				try {
					r = Integer.parseInt(captionTxtR.getText());
				} catch (NumberFormatException nfe) {
					captionTxtR.setBorder(new LineBorder(Color.red, 1));
				}
				try {
					g = Integer.parseInt(captionTxtG.getText());
				} catch (NumberFormatException nfe) {
					captionTxtG.setBorder(new LineBorder(Color.red, 1));
				}
				try {
					b = Integer.parseInt(captionTxtB.getText());
				} catch (NumberFormatException nfe) {
					captionTxtB.setBorder(new LineBorder(Color.red, 1));
				}
				if (r >= 0 && g >= 0 && b >= 0 && r <= 255 && g <= 255 && b <= 255) {
					Color newColor = new Color(r, g, b);
					if (newColor.equals(Color.white) || newColor.equals(Color.gray)
							|| newColor.equals(Color.darkGray) || newColor.equals(Color.black)) {
						captionError.setText(
								"Non si accettano i colori bianco, grigio, grigio scuro e nero perché già usati altrove nell'applicazione");
					} else {
						for (int count = 0; count < 5; count++) {
							if (tones[count].equals(newColor)) {
								captionTxtR.setBorder(new LineBorder(Color.red, 1));
								captionTxtG.setBorder(new LineBorder(Color.red, 1));
								captionTxtB.setBorder(new LineBorder(Color.red, 1));
								captionError.setText("Colore già presente");
								return;
							}
						}
						tones[index] = newColor;
						captionError.setText("Colore modificato con successo");
					}
				} else {
					captionError.setText("I tre colori devono essere compresi fra 0 e 255");
				}
			}
		});
		
		this.colorPanel = colorPanel;
	}
	
	public JPanel getColorPanel() {
		return this.colorPanel;
	}

}
