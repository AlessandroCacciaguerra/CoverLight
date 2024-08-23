package MyClasses;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GUI extends JFrame {
	static final int DIM_SQUARE = 50;	//un valore divisibile per 2 e per cui width e height sono divisibili
	static final int WIDTH = 1000;		//10 metri
	static final int HEIGHT = 800;		//8 metri
	static final int MIN_INT = -90;		//un segnale sotto -90 decibel milliwatt non è percepibile dalla comune apparecchiatura Wi-Fi
	static final String COL0 = "RED";		//Da -75 a -90 dbm			valore più basso
	static final String COL1 = "ORANGE";		//Da -60 a -75 dbm
	static final String COL2 = "YELLOW";		//Da -45 a -60 dbm
	static final String COL3 = "GREEN";		//Da -30 a -45 dbm
	static final String COL4 = "CYAN";		//> -30 dbm					valore più alto
	static final Color[] TONI = {Color.getColor(COL0),Color.getColor(COL1),Color.getColor(COL2),Color.getColor(COL3),Color.getColor(COL4)};//TODO:assicurarsi che il set di colori sia accessibile ai daltonici
	static final String BASSO = "Basso";
	static final String MEDIO = "Medio";
	static final String ALTO = "Alto";
	static String impactSel = "Medio";
	static int piano = 0;
	static final int PIANO_OFFSET = 3;
	static double[][][] intensity = new double[HEIGHT/DIM_SQUARE][WIDTH/DIM_SQUARE][PIANO_OFFSET*4];
	static int i, j, risultato;
	
	private static final long serialVersionUID = 1L;

	static java.util.List<HashMap<Emitters, Boolean>> apparati = new ArrayList<HashMap<Emitters, Boolean>>();		//mappa di emittenti
	static java.util.List<HashMap<Walls, Boolean>> planimetria = new ArrayList<HashMap<Walls, Boolean>>();			//mappa di muri
	static java.util.List<HashMap<Utilizers,Boolean>> consumatori = new ArrayList<HashMap<Utilizers, Boolean>>();	//mappa di utilizzatori
	
/*	private void changeFloor(Graphics draw, Graphics result) {
		redraw(draw);
		redraw(result);
		emitEnabled.setText("");
		emitEnabled.setText("");
		wallEnabledL.setText("");
		wallEnabledM.setText("");
		wallEnabledH.setText("");
		wallDisabledL.setText("");
		wallDisabledM.setText("");
		wallDisabledH.setText("");
		utilEnabled.setText("");
		utilDisabled.setText("");
		for(Map.Entry<Emitters,Boolean> entry : apparati[piano+PIANO_OFFSET].entrySet()) {
			if(entry.getValue()) {
				emitEnabled.setText(emitEnabled.getText() + "(" + entry.getKey().getPosition().x + " " + entry.getKey().getPosition().y + ") " + entry.getKey().getmW() + " mW  " + entry.getKey().getMHz() + " MHz  [" + entry.getKey().getAngles().x + "°-" + entry.getKey().getAngles().y + "°]    ");
				
			} else {
				emitDisabled.setText(emitDisabled.getText() + "(" + entry.getKey().getPosition().x + " " + entry.getKey().getPosition().y + ") " + entry.getKey().getmW() + " mW  " + entry.getKey().getMHz() + " MHz  [" + entry.getKey().getAngles().x + "°-" + entry.getKey().getAngles().y + "°]    ");
			}
		}
		for(Map.Entry<Walls, Boolean> entry : planimetria[piano+PIANO_OFFSET].entrySet()) {
			if(entry.getValue()) {
				switch(entry.getKey().getImpact()) {
					case BASSO: wallEnabledL.setText(wallEnabledL.getText() + "(" + entry.getKey().getPosition().getX1() + " " + entry.getKey().getPosition().getY1() + ")-(" + entry.getKey().getPosition().getX2() + " " + entry.getKey().getPosition().getY2()  + ")    "); break; 
					case MEDIO: wallEnabledM.setText(wallEnabledM.getText() + "(" + entry.getKey().getPosition().getX1() + " " + entry.getKey().getPosition().getY1() + ")-(" + entry.getKey().getPosition().getX2() + " " + entry.getKey().getPosition().getY2()  + ")    "); break;
					case ALTO: wallEnabledH.setText(wallEnabledH.getText() + "(" + entry.getKey().getPosition().getX1() + " " + entry.getKey().getPosition().getY1() + ")-(" + entry.getKey().getPosition().getX2() + " " + entry.getKey().getPosition().getY2()  + ")    "); break; 
					default: continue;
				}
			} else {
				switch(entry.getKey().getImpact()) {
					case BASSO:	wallDisabledL.setText(wallDisabledL.getText() + "(" + entry.getKey().getPosition().getX1() + " " + entry.getKey().getPosition().getY1() + ")-(" + entry.getKey().getPosition().getX2() + " " + entry.getKey().getPosition().getY2()  + ")    "); break; 
					case MEDIO:	wallDisabledM.setText(wallDisabledM.getText() + "(" + entry.getKey().getPosition().getX1() + " " + entry.getKey().getPosition().getY1() + ")-(" + entry.getKey().getPosition().getX2() + " " + entry.getKey().getPosition().getY2()  + ")    "); break;
					case ALTO: wallDisabledH.setText(wallDisabledH.getText() + "(" + entry.getKey().getPosition().getX1() + " " + entry.getKey().getPosition().getY1() + ")-(" + entry.getKey().getPosition().getX2() + " " + entry.getKey().getPosition().getY2()  + ")    "); break; 
					default: continue;
				}
			}
		}
		for(Map.Entry<Utilizers,Boolean> entry : consumatori[piano+PIANO_OFFSET].entrySet()) {
			if(entry.getValue()) {
				utilEnabled.setText(utilEnabled.getText() + "(" + entry.getKey().getPosition().x + " " + entry.getKey().getPosition().y + ")    ");
			} else {
				utilDisabled.setText(utilDisabled.getText() + "(" + entry.getKey().getPosition().x + " " + entry.getKey().getPosition().y + ")    ");
			}
		}
	}TODO*/
	
	private void copyFloor(int nuovoPiano, Graphics draw, Graphics result) {
		apparati.set(nuovoPiano+PIANO_OFFSET, apparati.get(piano+PIANO_OFFSET));
		planimetria.set(nuovoPiano+PIANO_OFFSET, planimetria.get(piano+PIANO_OFFSET));
		consumatori.set(nuovoPiano+PIANO_OFFSET, consumatori.get(piano+PIANO_OFFSET));
		piano = nuovoPiano;
	}
	
	private void result(Graphics graphics) {
		for(Map.Entry<Emitters,Boolean> entry : apparati.get(piano+PIANO_OFFSET).entrySet()) {
			if(entry.getValue()) {
				drawEmitter(graphics,entry.getKey());
			}
		}
		for(Map.Entry<Walls, Boolean> entry : planimetria.get(piano+PIANO_OFFSET).entrySet()) {
			if(entry.getValue()) {
				drawWall(graphics,entry.getKey());
			}
		}
		for(Map.Entry<Utilizers,Boolean> entry : consumatori.get(piano+PIANO_OFFSET).entrySet()) {
			if(entry.getValue()) {
				drawUtilizer((Graphics2D) graphics,entry.getKey().getPosition());
			}
		}
	}
	
	private void redraw(Graphics graphics) {
		graphics.clearRect(0, 0, WIDTH, HEIGHT);
		result(graphics);
	}
	
	private boolean validatePosition(Point val) {
		return val.x>=0 && val.x<=WIDTH && val.y>=0 && val.y<=HEIGHT;
	}
	
	private boolean validateWallPosition(Line2D val) {
		return (val.getX1()>=0 && val.getX1()<=WIDTH && val.getY1()>=0 && val.getY1()<=HEIGHT && val.getX2()>=0 && val.getX2()<=WIDTH && val.getY2()>=0 && val.getY2()<=HEIGHT) &&	//entro i margini
				(val.getX1() % DIM_SQUARE + val.getX2() % DIM_SQUARE + val.getY1() % DIM_SQUARE + val.getY2() % DIM_SQUARE == 0) &&		//divisibili per DIM_SQUARE
				(val.getX1() == val.getX2() ^ val.getY1() == val.getY2());	//non nulli e paralleli ad un asse
	}
	
	private void drawUtilizer(Graphics2D graphics, Point draw) {
		int x = draw.x - draw.x % DIM_SQUARE;
		int y = draw.y - draw.y % DIM_SQUARE;
		final float dim_dash = DIM_SQUARE/5;
		final BasicStroke black_dash = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {dim_dash,dim_dash}, 0);
		final BasicStroke white_dash = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {dim_dash,dim_dash}, dim_dash);
		graphics.setColor(Color.BLACK);
		graphics.setStroke(black_dash);
		graphics.drawRect(x, y, DIM_SQUARE, DIM_SQUARE);
		graphics.setColor(Color.WHITE);
		graphics.setStroke(white_dash);
		graphics.drawRect(x, y, DIM_SQUARE, DIM_SQUARE);
	} /*l'utente è un quadrato dai bordi bianchi e neri a zig-zag; ha il solo scopo di evidenziare il colore al proprio interno.*/

	private void drawEmitter(Graphics graphics, Emitters emittente) {
		graphics.setColor(Color.BLACK);
		graphics.fillArc(emittente.getPosition().x,emittente.getPosition().y,DIM_SQUARE,DIM_SQUARE,emittente.getAngles().x,Math.abs(emittente.getAngles().y-emittente.getAngles().x));
	}

	private void drawWall(Graphics graphics, Walls muro) {
		int muratura;
		switch(muro.getImpact()) {
			case BASSO:
				graphics.setColor(Color.GRAY);
				muratura = 5;
				break;
			case MEDIO:
				graphics.setColor(Color.DARK_GRAY);
				muratura = 7;
				break;
			case ALTO:
				graphics.setColor(Color.BLACK);
				muratura = 9;
				break;
			default: return;
		}
		((Graphics2D) graphics).setStroke(new BasicStroke(muratura));
        graphics.drawLine((int) muro.getPosition().getX1(),(int) muro.getPosition().getY1(),(int) muro.getPosition().getX2(),(int) muro.getPosition().getY2());
	}

	private void paintComponent(Graphics graphics) {
		graphics.setColor(TONI[risultato]);
		graphics.fillRect(j - DIM_SQUARE/2+1,i - DIM_SQUARE/2+1, DIM_SQUARE-1, DIM_SQUARE-1);
	}

	
	public GUI() {
		JCheckBox valueCheck = new JCheckBox("Valori esatti (dbm)", false);//TODO
		for(int p=0; p<=PIANO_OFFSET*4; p++) {
			apparati.add(new HashMap<Emitters,Boolean>());
			planimetria.add(new HashMap<Walls,Boolean>());
			consumatori.add(new HashMap<Utilizers,Boolean>());
		}
		JButton lowerFloor = new JButton("Vai al piano " + (piano-1));
		JLabel currentFloor = new JLabel("Piano " + piano);
		JButton upperFloor = new JButton("Vai al piano " + (piano+1));
		JButton duplicateMap = new JButton("Copia la mappa corrente nel piano ");
		JTextArea duplicateMapTxt = new JTextArea();

	//<Area di disegno
		
		//Didascalia
		final JPanel captionPanel = new JPanel(new BorderLayout());
		captionPanel.setBorder(new TitledBorder("Didascalia"));
		final JTextArea captionTxtArea = new JTextArea();
		captionTxtArea.setLineWrap(true);
		captionTxtArea.setText("Effetto del materiale dell'ostacolo su un segnale radio\r\n"
				+ "A seconda del materiale, gli ostacoli possono riflettere le onde radio, assorbirle, privandole di una parte della potenza, o non avere alcun effetto sul segnale radio. Tali materiali sono chiamati radiotrasparenti. Più alto è il coefficiente di assorbimento del segnale e più spesso è l'ostacolo, più forte è l'impatto sulla trasmissione radio.\r\n"
				+ "Coefficiente di assorbimento del segnale\r\n"
				+ "Basso\r\n"
				+ "Perdita di potenza del 50%\r\n"
				+ "- Mattone rosso secco di 90 mm di spessore\r\n"
				+ "- Pannello di gesso di 100 mm di spessore\r\n"
				+ "- Legno secco di 80 mm di spessore\r\n"
				+ "- Vetro di 15 mm di spessore\r\n"
				+ "\r\n"
				+ "Medio\r\n"
				+ "La potenza si riduce di 10 volte\r\n"
				+ "- Mattone di 250 mm di spessore\r\n"
				+ "- Blocchi di calcestruzzo di 200 mm di spessore\r\n"
				+ "- Calcestruzzo di 100 mm di spessore\r\n"
				+ "- Muratura di 200 mm di spessore\r\n"
				+ "\r\n"
				+ "Alto\r\n"
				+ "La potenza si riduce di 100 volte\r\n"
				+ "- Calcestruzzo di 300 mm di spessore\r\n"
				+ "- Calcestruzzo armato di 200 mm di spessore\r\n"
				+ "- Travi in alluminio e acciaio\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "Si presume un edificio composto da " + PIANO_OFFSET + " piani sotterranei, il piano terra e " + PIANO_OFFSET*3 + " piani superiori\r\n"
				+ "Su ogni piano, si provvede uno spazio di " + WIDTH/100 + " metri per " + HEIGHT/100 + " in cui riprodurre la pianta dell'edificio; valori illegali saranno considerati 0.\r\n"
				+ "Porte, finestre interne ed esterne od altre aperture fra stanze sono sempre considerate chiuse ai fini della rilevazione del segnale: si ipotizza il segnale minimo nel caso peggiore.\r\n"
				+ "La precisione massima nel piazzamento di un muro è di " + DIM_SQUARE + " centimetri, sugli assi x e y; non si accettano muri diagonali.\r\n"
				+ "Piani strutturati inframmezzati da piani vuoti od incompleti sono tollerati: si presume che le relative planimetrie siano ininfluenti ai fini della simulazione dell'utente.\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "L'intensità del segnale è così rappresentata (dbm: decibel milliwatt):\r\n"
				+ "Da -75 a -90 dbm: " + COL0 + "\r\n"
				+ "Da -60 a -75 dbm: " + COL1 + "\r\n"
				+ "Da -45 a -60 dbm: " + COL2 + "\r\n"
				+ "Da -30 a -45 dbm: " + COL3 + "\r\n"
				+ "> -30 dbm: " + COL4 + "\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "Per selezionare un campo da abilitare/disabilitare/cancellare è sufficiente indicarne la posizione e premere il relativo pulsante; non serve impostare correttamente il resto dei campi.\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "Secondo le normative ETSI EN, la potenza di emittenti wireless in un edificio non può superare i 200 milliWatt e la frequenza deve essere compresa nella banda 5150-5350 MegaHertz\r\n"
				+ "Gli emittenti devono essere distanziati di almeno " + DIM_SQUARE/2 + " centimetri ed i muri si possono intersecare ma non compenetrare.\r\n"
				+ "Gli angoli di inizio e di fine sono 0 e 360 per antenne omnidirezionali; per antenne direzionali, l'ampiezza è calcolata in senso antiorario con 0° = 360° = ore 3.\r\n");
		final JScrollPane scroll = new JScrollPane(captionTxtArea);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		captionPanel.add(scroll);
		
		
		//Canvas
		final JPanel canvasPanel = new JPanel(new BorderLayout());
		canvasPanel.setBorder(new TitledBorder("Canvas"));
		
		final JPanel canvasContainerPanel = new JPanel(new BorderLayout());// panel che contiene lo scroll
		canvasPanel.add(canvasContainerPanel);

		final JPanel drawPanel = new JPanel();// panel che contiene il disegno
		final JScrollPane scrollCanvas = new JScrollPane(canvasContainerPanel);
		scrollCanvas.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		canvasPanel.add(scrollCanvas);
		canvasContainerPanel.add(drawPanel);
		
    //fine Area Di Disegno>
		
		
    //<Componenti 
		
		final JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

		//Emittente 
		final JPanel panelEmitter = new JPanel();
		final GroupLayout layoutEmitter = new GroupLayout(panelEmitter);
		panelEmitter.setLayout(layoutEmitter);
		panelEmitter.setBorder(new TitledBorder("Emittente"));

		final JButton createEmitter = new JButton("Crea");
		JButton deleteEmitter = new JButton("Cancella");
		JButton enableDisableEmitter = new JButton("Abilita/Disabilita");
		JLabel xEmitLbl = new JLabel("X:");
		JTextField xEmitTxt = new JTextField();
		JLabel yEmitLbl = new JLabel("Y:");
		JTextField yEmitTxt = new JTextField();
		JLabel powEmitLbl = new JLabel("Potenza:");
		JTextField powEmitTxt = new JTextField();
		JLabel freqEmitLbl = new JLabel("Frequenza:");
		JTextField freqEmitterTxt = new JTextField();
		JLabel emitAngStartLbl = new JLabel("AngStart:");
		JTextField angStartEmitTxt = new JTextField("0");
		JLabel emitAngEndLbl = new JLabel("AngEnd:");
		JTextField angEndEmitTxt = new JTextField("360");
		JLabel emitEnabled = new JLabel("");
		JLabel emitDisabled = new JLabel("");
		
		layoutEmitter.setAutoCreateGaps(true);
		layoutEmitter.setAutoCreateContainerGaps(true);
		/*
		layoutEmitter.setHorizontalGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(createEmitter)
				.addGroup(layoutEmitter.createSequentialGroup().addComponent(deleteEmitter)
						.addComponent(enableDisableEmitter))
				.addGroup(layoutEmitter.createSequentialGroup().addGroup(layoutEmitter
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layoutEmitter.createSequentialGroup().addComponent(xEmitLbl).addComponent(xEmitTxt))
						.addGroup(layoutEmitter.createSequentialGroup().addComponent(yEmitLbl).addComponent(yEmitTxt))
						.addGroup(layoutEmitter.createSequentialGroup().addComponent(emitAngStartLbl)
								.addComponent(angStartEmitTxt)))
						.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(layoutEmitter.createSequentialGroup().addComponent(powEmitLbl)
										.addComponent(powEmitTxt))
								.addGroup(layoutEmitter.createSequentialGroup().addComponent(freqEmitLbl)
										.addComponent(freqEmitterTxt))
								.addGroup(layoutEmitter.createSequentialGroup().addComponent(emitAngEndLbl)
										.addComponent(angEndEmitTxt))))
				.addGroup(layoutEmitter.createSequentialGroup()
						.addGroup(layoutEmitter.createSequentialGroup().addComponent(emitEnabled).addComponent(emitDisabled))));

		layoutEmitter.setVerticalGroup(layoutEmitter.createSequentialGroup().addComponent(createEmitter)
				.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(deleteEmitter)
						.addComponent(enableDisableEmitter))
				.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layoutEmitter
						.createSequentialGroup()
						.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(xEmitLbl).addComponent(xEmitTxt))
								.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(powEmitLbl).addComponent(powEmitTxt)))
						.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(yEmitLbl).addComponent(yEmitTxt))
								.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(freqEmitLbl).addComponent(freqEmitterTxt)))
						.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(emitAngStartLbl).addComponent(angStartEmitTxt))
								.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(emitAngEndLbl).addComponent(angEndEmitTxt))))
				.addGroup(layoutEmitter.createSequentialGroup()
						.addGroup(layoutEmitter.createSequentialGroup().addComponent(emitEnabled).addComponent(emitDisabled)))));*/
		layoutEmitter.setHorizontalGroup(layoutEmitter.createSequentialGroup()
				.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.LEADING, false)
						.addComponent(createEmitter, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(deleteEmitter, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(enableDisableEmitter, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(layoutEmitter.createSequentialGroup().addGroup(layoutEmitter
						.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layoutEmitter.createSequentialGroup()
								.addComponent(xEmitLbl)
								.addComponent(xEmitTxt))
						.addGroup(layoutEmitter.createSequentialGroup()
								.addComponent(yEmitLbl)
								.addComponent(yEmitTxt))
						.addGroup(layoutEmitter.createSequentialGroup()
								.addComponent(emitAngStartLbl)
								.addComponent(angStartEmitTxt)))
						.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(layoutEmitter.createSequentialGroup()
										.addComponent(powEmitLbl)
										.addComponent(powEmitTxt))
								.addGroup(layoutEmitter.createSequentialGroup()
										.addComponent(freqEmitLbl)
										.addComponent(freqEmitterTxt))
								.addGroup(layoutEmitter.createSequentialGroup()
										.addComponent(emitAngEndLbl)
										.addComponent(angEndEmitTxt)))
						.addGroup(layoutEmitter.createSequentialGroup())
							.addComponent(lowerFloor).addComponent(currentFloor).addComponent(upperFloor).addComponent(duplicateMap).addComponent(duplicateMapTxt)));

		layoutEmitter.setVerticalGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(layoutEmitter.createSequentialGroup()
						.addComponent(createEmitter)
						.addComponent(deleteEmitter)
						.addComponent(enableDisableEmitter))
				.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layoutEmitter
						.createSequentialGroup()
						.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(xEmitLbl).addComponent(xEmitTxt))
								.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(powEmitLbl).addComponent(powEmitTxt)))
						.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(yEmitLbl).addComponent(yEmitTxt))
								.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(freqEmitLbl).addComponent(freqEmitterTxt)))
						.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(emitAngStartLbl).addComponent(angStartEmitTxt))
								.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(emitAngEndLbl).addComponent(angEndEmitTxt)))
						.addGroup(layoutEmitter.createSequentialGroup())
							.addComponent(lowerFloor).addComponent(currentFloor).addComponent(upperFloor).addComponent(duplicateMap).addComponent(duplicateMapTxt))));


		//Utilizzatore
		final JPanel panelUtilizer = new JPanel();
		final GroupLayout layoutUtilizer = new GroupLayout(panelUtilizer);
		panelUtilizer.setLayout(layoutUtilizer);
		panelUtilizer.setBorder(new TitledBorder("Utilizzatore"));
		
		final JButton createUtilizer = new JButton("Crea");
		JButton deleteUtilizer = new JButton("Cancella");
		JButton enableUtilizer = new JButton("Abilita/Disabilita");
		JLabel xUtilLbl = new JLabel("X:");
		JLabel xUtilCurVal = new JLabel();
		JTextField xUtilTxt = new JTextField();
		JLabel yUtilLbl = new JLabel("Y:");
		JLabel yUtilCurVal = new JLabel();
		JTextField yUtilTxt = new JTextField();

		
		JLabel utilEnabled = new JLabel();
		JLabel utilDisabled = new JLabel();
		/*final JButton createUtilizer = new JButton("Crea");
		JButton deleteUtilizer = new JButton("Cancella");
		JButton enableUtilizer = new JButton("Abilita/Disabilita");
		JLabel xUtilLbl = new JLabel("X:");
		JTextField xUtilTxt = new JTextField();
		JLabel yUtilLbl = new JLabel("Y:");
		JTextField yUtilTxt = new JTextField();
		JLabel utilEnabled = new JLabel();
		JLabel utilDisabled = new JLabel();
		
		layoutUtilizer.setAutoCreateGaps(true);
		layoutUtilizer.setAutoCreateContainerGaps(true);

		layoutUtilizer.setHorizontalGroup(layoutUtilizer.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(createUtilizer)
				.addGroup(layoutUtilizer.createSequentialGroup().addComponent(deleteUtilizer)
						.addComponent(enableUtilizer))
				.addGroup(layoutUtilizer.createSequentialGroup()
						.addGroup(layoutUtilizer.createSequentialGroup().addComponent(xUtilLbl).addComponent(xUtilTxt))
						.addGroup(layoutUtilizer.createSequentialGroup().addComponent(yUtilLbl).addComponent(yUtilTxt))
						.addGroup(layoutUtilizer.createSequentialGroup().addComponent(utilEnabled).addComponent(utilDisabled))));

		layoutUtilizer
				.setVerticalGroup(layoutUtilizer.createSequentialGroup().addComponent(createUtilizer)
						.addGroup(layoutUtilizer.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(deleteUtilizer).addComponent(enableUtilizer))
						.addGroup(layoutUtilizer.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addGroup(layoutUtilizer.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addGroup(layoutUtilizer.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(xUtilLbl).addComponent(xUtilTxt))
										.addGroup(layoutUtilizer.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addGroup(layoutUtilizer
														.createParallelGroup(GroupLayout.Alignment.BASELINE)
														.addComponent(yUtilLbl).addComponent(yUtilTxt)))
										.addGroup(layoutUtilizer.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addGroup(layoutUtilizer
														.createParallelGroup(GroupLayout.Alignment.BASELINE)
														.addComponent(utilEnabled).addComponent(utilDisabled))))));*/
		layoutUtilizer.setAutoCreateGaps(true);
		layoutUtilizer.setAutoCreateContainerGaps(true);

		layoutUtilizer.setHorizontalGroup(layoutUtilizer.createSequentialGroup()
				.addGroup(layoutUtilizer.createParallelGroup(GroupLayout.Alignment.LEADING, false)
						.addComponent(createUtilizer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(deleteUtilizer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(enableUtilizer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(layoutUtilizer.createSequentialGroup()
						.addGroup(layoutUtilizer.createSequentialGroup().addComponent(xUtilLbl)
								.addComponent(xUtilCurVal).addComponent(xUtilTxt))
						.addGroup(layoutUtilizer.createSequentialGroup().addComponent(yUtilLbl)
								.addComponent(yUtilCurVal).addComponent(yUtilTxt))));

		layoutUtilizer.setVerticalGroup(layoutUtilizer.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(layoutUtilizer.createSequentialGroup().addComponent(createUtilizer)
						.addComponent(deleteUtilizer).addComponent(enableUtilizer))
				.addGroup(layoutUtilizer.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGroup(layoutUtilizer.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addGroup(layoutUtilizer.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(xUtilLbl).addComponent(xUtilCurVal).addComponent(xUtilTxt))
								.addGroup(layoutUtilizer.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addGroup(layoutUtilizer.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(yUtilLbl).addComponent(yUtilCurVal)
												.addComponent(yUtilTxt))))));

		//Muro
		final JPanel panelWall = new JPanel();
		final GroupLayout layoutWall = new GroupLayout(panelWall);
		panelWall.setLayout(layoutWall);
		panelWall.setBorder(new TitledBorder("Muro"));

		final JButton createWall = new JButton("Crea");
		JButton deleteWall = new JButton("Cancella");
		JButton enableWall = new JButton("Abilita/Disabilita");
		JLabel wallStartxLbl = new JLabel("Xstart:");
		JTextField wallStartxTxt = new JTextField();
		JLabel wallEndxLbl = new JLabel("Xend:");
		JTextField wallEndxTxt = new JTextField();
		JLabel wallStartyLbl = new JLabel("Ystart:");
		JTextField wallStartyTxt = new JTextField();
		JLabel wallEndyLbl = new JLabel("Yend:");
		JTextField wallEndyTxt = new JTextField();
		JLabel impactLbl = new JLabel("Impatto:");
		JRadioButton rLow = new JRadioButton("Basso", false);
		JRadioButton rMedium = new JRadioButton("Medio", true);
		JRadioButton rHigh = new JRadioButton("Alto", false);
		ButtonGroup impactGroup = new ButtonGroup();
		impactGroup.add(rLow);
		impactGroup.add(rMedium);
		impactGroup.add(rHigh);
		JLabel wallEnabledL = new JLabel("");
		JLabel wallEnabledM = new JLabel("");
		JLabel wallEnabledH = new JLabel("");
		JLabel wallDisabledL = new JLabel("");
		JLabel wallDisabledM = new JLabel("");
		JLabel wallDisabledH = new JLabel("");
		
		layoutWall.setAutoCreateGaps(true);
		layoutWall.setAutoCreateContainerGaps(true);

		/*layoutWall.setHorizontalGroup(
				layoutWall.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(createWall)
						.addGroup(layoutWall.createSequentialGroup().addComponent(deleteWall).addComponent(enableWall))
						.addGroup(layoutWall.createSequentialGroup()
								.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(layoutWall.createSequentialGroup().addComponent(wallStartxLbl)
												.addComponent(wallStartxTxt))
										.addGroup(layoutWall.createSequentialGroup().addComponent(wallStartyLbl)
												.addComponent(wallStartyTxt)))
								.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(layoutWall.createSequentialGroup().addComponent(wallEndxLbl)
												.addComponent(wallEndxTxt))
										.addGroup(layoutWall.createSequentialGroup().addComponent(wallEndyLbl)
												.addComponent(wallEndyTxt))))
						.addGroup(layoutWall.createSequentialGroup().addComponent(impactLbl).addComponent(rLow)
								.addComponent(rMedium).addComponent(rHigh))
						.addGroup(layoutWall.createSequentialGroup()
								.addGroup(layoutWall.createSequentialGroup().addComponent(wallEnabledL).addComponent(wallEnabledM).addComponent(wallEnabledH)))
						.addGroup(layoutWall.createSequentialGroup()
										.addGroup(layoutWall.createSequentialGroup().addComponent(wallDisabledL).addComponent(wallDisabledM).addComponent(wallDisabledH))));

		layoutWall.setVerticalGroup(layoutWall.createSequentialGroup().addComponent(createWall)
				.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(deleteWall)
						.addComponent(enableWall))
				.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layoutWall.createSequentialGroup()
								.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(wallStartxLbl).addComponent(wallStartxTxt))
										.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(wallEndxLbl).addComponent(wallEndxTxt)))
								.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(wallStartyLbl).addComponent(wallStartyTxt))
										.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(wallEndyLbl).addComponent(wallEndyTxt)))))
				.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(impactLbl)
						.addComponent(rLow).addComponent(rMedium).addComponent(rHigh))
				.addGroup(layoutWall.createSequentialGroup()
						.addGroup(layoutWall.createSequentialGroup().addComponent(wallEnabledL).addComponent(wallEnabledM).addComponent(wallEnabledH)))
				.addGroup(layoutWall.createSequentialGroup()
								.addGroup(layoutWall.createSequentialGroup().addComponent(wallDisabledL).addComponent(wallDisabledM).addComponent(wallDisabledH))));
*/
		layoutWall.setHorizontalGroup(layoutWall.createSequentialGroup()
				.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(createWall)
						.addGroup(layoutWall.createSequentialGroup().addComponent(deleteWall).addComponent(enableWall)))
				.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addGroup(layoutWall.createSequentialGroup()
								.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(layoutWall.createSequentialGroup().addComponent(wallStartxLbl)
												.addComponent(wallStartxTxt))
										.addGroup(layoutWall.createSequentialGroup().addComponent(wallStartyLbl)
												.addComponent(wallStartyTxt)))
								.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.CENTER)
										.addGroup(layoutWall.createSequentialGroup().addComponent(wallEndxLbl)
												.addComponent(wallEndxTxt))
										.addGroup(layoutWall.createSequentialGroup().addComponent(wallEndyLbl)
												.addComponent(wallEndyTxt))))
						.addGroup(layoutWall.createSequentialGroup().addComponent(impactLbl).addComponent(rLow)
								.addComponent(rMedium).addComponent(rHigh))));

		layoutWall.setVerticalGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(layoutWall.createSequentialGroup().addComponent(createWall)
						.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(deleteWall)
								.addComponent(enableWall)))
				.addGroup(layoutWall.createSequentialGroup().addGroup(layoutWall
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layoutWall.createSequentialGroup()
								.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(wallStartxLbl).addComponent(wallStartxTxt))
										.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(wallEndxLbl).addComponent(wallEndxTxt)))
								.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(wallStartyLbl).addComponent(wallStartyTxt))
										.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(wallEndyLbl).addComponent(wallEndyTxt)))))
						.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(impactLbl)
								.addComponent(rLow).addComponent(rMedium).addComponent(rHigh))));

		//Aggiunta al panel
		buttonsPanel.add(panelEmitter);
		buttonsPanel.add(panelUtilizer);
		buttonsPanel.add(panelWall);
		
		
		// Panel results
		final JPanel resultPanel = new JPanel();
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

		final JButton generateResult = new JButton("Genera risultato");
		generateResult.setAlignmentX(Component.CENTER_ALIGNMENT);

		final JPanel resultContainerPanel = new JPanel(new BorderLayout());
		final JScrollPane scrollResult = new JScrollPane(resultContainerPanel);
		scrollResult.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		final JPanel showResult = new JPanel();// panel per i result
		resultContainerPanel.add(showResult);
		resultPanel.add(generateResult);
		resultPanel.add(scrollResult);
    //Fine componenti>		
		
		
		//Btn listeners
		createEmitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int xEmit, yEmit, angS, angE;
				float pow, freq;
				try {
					xEmit = Integer.parseInt(xEmitTxt.getText());
				} catch (NumberFormatException nfe) {
					xEmitTxt.setText("0");
					xEmit = 0;
				}
				try {
					yEmit = Integer.parseInt(yEmitTxt.getText());
				} catch (NumberFormatException nfe) {
					yEmitTxt.setText("0");
					yEmit = 0;
				}
				try {
					pow = Float.parseFloat(powEmitTxt.getText());
					if(pow>200 || pow<=0) {					//normative ETSI EN
						System.out.println("La potenza non deve superare i 200 milliWatt");
						return;
					}
				} catch (NumberFormatException nfe) {
					System.out.println("La potenza non deve superare i 200 milliWatt");
					return;
				}
				try {
					freq = Float.parseFloat(freqEmitterTxt.getText());
					if((freq < 5150 || freq > 5350)) {	//normative ETSI EN
						System.out.println("La frequenza del Wi-Fi all'interno di un edificio deve essere compresa fra 5150 e 5350 MegaHertz");
						return;
					}
				} catch (NumberFormatException nfe) {
					System.out.println("La frequenza del Wi-Fi all'interno di un edificio deve essere compresa fra 5150 e 5350 MegaHertz");
					return;
				}
				try {
					angS = Integer.parseInt(angStartEmitTxt.getText());
					if(angS < 0 || angS >= 360) {
						System.out.println("L'angolo d'inizio deve avere valore compreso fra 0 e 359");
						return;
					}
				} catch (NumberFormatException nfe) {
					angStartEmitTxt.setText("0");
					angS = 0;
				}
				try {
					angE = Integer.parseInt(angEndEmitTxt.getText());
					if(angE <= 0 || angE > 360) {
						System.out.println("L'angolo di fine deve avere valore compreso fra 1 e 360");
						return;
					}
				} catch (NumberFormatException nfe) {
					angEndEmitTxt.setText("360");
					angE = 360;
				}
				if(angS == angE) {
					angS = 0;
					angE = 360;
					angStartEmitTxt.setText("0");
					angEndEmitTxt.setText("360");
				}
				Point emit = new Point(xEmit,yEmit);
				if(validatePosition(emit)) {
					for(Emitters entry : apparati.get(piano+PIANO_OFFSET).keySet()) { //controllo compenetrazione
						if(emit.distance(entry.getPosition())<DIM_SQUARE/2) {
							System.out.println("Gli emittenti devono essere distanziati di almeno " + DIM_SQUARE/2 + " centimetri");
							return;
						}
					}
					Emitters emitter = new Emitters(emit, pow, freq, angS, angE);
					apparati.get(piano+PIANO_OFFSET).put(emitter, true);
					drawEmitter(drawPanel.getGraphics(),emitter);
					emitEnabled.setText(emitEnabled.getText() + "(" + emit.x + " " + emit.y + ") " + pow + " mW  " + freq + " MHz  [" + angS + "°-" + angE + "°]    ");
				} else {
					System.out.println("Errore: la posizione deve essere una coppia di numeri positivi minori rispettivamente di " + WIDTH + " e " + HEIGHT);
				}
			}
		});	

		deleteEmitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int xEmit, yEmit;
				try {
					xEmit = Integer.parseInt(xEmitTxt.getText());
				} catch (NumberFormatException nfe) {
					xEmitTxt.setText("0");
					xEmit = 0;
				}
				try {
					yEmit = Integer.parseInt(yEmitTxt.getText());
				} catch (NumberFormatException nfe) {
					yEmitTxt.setText("0");
					yEmit = 0;
				}
				Point emit = new Point(xEmit,yEmit);
				if(validatePosition(emit)) {
					for(Emitters entry : apparati.get(piano+PIANO_OFFSET).keySet()) {
						if(entry.getPosition().equals(emit)) {
							if(apparati.get(piano+PIANO_OFFSET).get(entry)) {
								emitEnabled.setText(emitEnabled.getText().replace("(" + emit.x + " " + emit.y + ") " + entry.getmW() + " mW  " + entry.getMHz() + " MHz  [" + entry.getAngles().x + "°-" + entry.getAngles().y + "°]    ",""));
							} else {
								emitDisabled.setText(emitDisabled.getText().replace("(" + emit.x + " " + emit.y + ") " + entry.getmW() + " mW  " + entry.getMHz() + " MHz  [" + entry.getAngles().x + "°-" + entry.getAngles().y + "°]    ",""));
							}
							apparati.get(piano+PIANO_OFFSET).remove(entry);
							redraw((Graphics2D) drawPanel.getGraphics());
							return;
						}
					}
				} else {
					System.out.println("Errore: la posizione deve essere una coppia di numeri positivi minori rispettivamente di " + WIDTH + " e " + HEIGHT);
				}
				System.out.println("Impossibile cancellare un valore assente");
			}
		});	

		enableDisableEmitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int xEmit, yEmit;
				try {
					xEmit = Integer.parseInt(xEmitTxt.getText());
				} catch (NumberFormatException nfe) {
					xEmitTxt.setText("0");
					xEmit = 0;
				}
				try {
					yEmit = Integer.parseInt(yEmitTxt.getText());
				} catch (NumberFormatException nfe) {
					yEmitTxt.setText("0");
					yEmit = 0;
				}
				Point emit = new Point(xEmit,yEmit);
				if(validatePosition(emit)) {
					for(Emitters entry : apparati.get(piano+PIANO_OFFSET).keySet()) {
						if(entry.getPosition().equals(emit)) {
							boolean flag = !apparati.get(piano+PIANO_OFFSET).get(entry);
							apparati.get(piano+PIANO_OFFSET).replace(entry,flag);
							if(flag) {
								emitDisabled.setText(emitDisabled.getText().replace("(" + emit.x + " " + emit.y + ") " + entry.getmW() + " mW  " + entry.getMHz() + " MHz  [" + entry.getAngles().x + "°-" + entry.getAngles().y + "°]    ",""));
								emitEnabled.setText(emitEnabled.getText() + "(" + emit.x + " " + emit.y + ") " + entry.getmW() + " mW  " + entry.getMHz() + " MHz  [" + entry.getAngles().x + "°-" + entry.getAngles().y + "°]    ");
							} else {
								emitEnabled.setText(emitEnabled.getText().replace("(" + emit.x + " " + emit.y + ") " + entry.getmW() + " mW  " + entry.getMHz() + " MHz  [" + entry.getAngles().x + "°-" + entry.getAngles().y + "°]    ",""));
								emitDisabled.setText(emitDisabled.getText() + "(" + emit.x + " " + emit.y + ") " + entry.getmW() + " mW  " + entry.getMHz() + " MHz  [" + entry.getAngles().x + "°-" + entry.getAngles().y + "°]    ");
							}
							redraw((Graphics2D) drawPanel.getGraphics());
							return;
						}
					}
					System.out.println("Il valore inserito è inesistente; premere Crea per aggiungerlo");
				} else {
					System.out.println("Errore: la posizione deve essere una coppia di numeri positivi minori rispettivamente di " + WIDTH + " e " + HEIGHT);
				}
			}
		});	

		rLow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				impactSel = BASSO;
			}
		});	
	
		rMedium.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				impactSel = MEDIO;
			}
		});	
	
		rHigh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				impactSel = ALTO;
			}
		});	

		createWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int xWallS, yWallS, xWallE, yWallE;
				try {
					xWallS = Integer.parseInt(wallStartxTxt.getText());
				} catch (NumberFormatException nfe) {
					wallStartxTxt.setText("0");
					xWallS = 0;
				}
				try {
					yWallS = Integer.parseInt(wallStartyTxt.getText());
				} catch (NumberFormatException nfe) {
					wallStartyTxt.setText("0");
					yWallS = 0;
				}
				try {
					xWallE = Integer.parseInt(wallEndxTxt.getText());
				} catch (NumberFormatException nfe) {
					wallEndxTxt.setText("0");
					xWallE = 0;
				}
				try {
					yWallE = Integer.parseInt(wallEndyTxt.getText());
				} catch (NumberFormatException nfe) {
					wallEndyTxt.setText("0");
					yWallE = 0;
				}
				Line2D wall = new Line2D.Float(xWallS, yWallS, xWallE, yWallE);
				if(validateWallPosition(wall)) {
					for(Walls entry : planimetria.get(piano+PIANO_OFFSET).keySet()) { //controllo compenetrazione
						if(wall.intersectsLine(entry.getPosition()) && (!(wall.getX1() == wall.getX2() ^ entry.getPosition().getX1() == entry.getPosition().getX2())) &&
						(wall.relativeCCW(entry.getPosition().getP1()) + wall.relativeCCW(entry.getPosition().getP2()) + entry.getPosition().relativeCCW(wall.getP1()) + entry.getPosition().relativeCCW(wall.getP2()) != -2)) {
							System.out.println("I muri non possono compenetrarsi");
							return;
						}
					}
					Walls walls = new Walls(wall, impactSel);
					planimetria.get(piano+PIANO_OFFSET).put(walls, true);
					drawWall((Graphics2D) drawPanel.getGraphics(),walls);
					switch(impactSel) {
						case BASSO: wallEnabledL.setText(wallEnabledL.getText() + "(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE  + ")    "); break;
						case MEDIO: wallEnabledM.setText(wallEnabledM.getText() + "(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE  + ")    "); break;
						case ALTO: wallEnabledH.setText(wallEnabledH.getText() + "(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE  + ")    "); break;
						default: return;
					}
				} else {
					System.out.println("Errore: la posizione deve essere una coppia di numeri positivi minori rispettivamente di " + WIDTH + " e " + HEIGHT + ", multipli di " + DIM_SQUARE + " (centimetri), che definiscano una linea non nulla parallela ad un asse");
				}
			}
		});	

		deleteWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int xWallS, yWallS, xWallE, yWallE;
				try {
					xWallS = Integer.parseInt(wallStartxTxt.getText());
				} catch (NumberFormatException nfe) {
					wallStartxTxt.setText("0");
					xWallS = 0;
				}
				try {
					yWallS = Integer.parseInt(wallStartyTxt.getText());
				} catch (NumberFormatException nfe) {
					wallStartyTxt.setText("0");
					yWallS = 0;
				}
				try {
					xWallE = Integer.parseInt(wallEndxTxt.getText());
				} catch (NumberFormatException nfe) {
					wallEndxTxt.setText("0");
					xWallE = 0;
				}
				try {
					yWallE = Integer.parseInt(wallEndyTxt.getText());
				} catch (NumberFormatException nfe) {
					wallEndyTxt.setText("0");
					yWallE = 0;
				}
				Line2D wall = new Line2D.Float(xWallS, yWallS, xWallE, yWallE);
				if(validateWallPosition(wall)) {
					for(Walls entry : planimetria.get(piano+PIANO_OFFSET).keySet()) {
						if(entry.getPosition().getP1().equals(wall.getP1()) && entry.getPosition().getP2().equals(wall.getP2())) {
							if(planimetria.get(piano+PIANO_OFFSET).get(entry)) {
								switch(entry.getImpact()) {
									case BASSO: wallEnabledL.setText(wallEnabledL.getText().replace("(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE  + ")    ","")); break;
									case MEDIO: wallEnabledM.setText(wallEnabledM.getText().replace("(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE  + ")    ","")); break;
									case ALTO: wallEnabledH.setText(wallEnabledH.getText().replace("(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE  + ")    ","")); break;
									default: return;
								}
							} else {
								switch(entry.getImpact()) {
									case BASSO: wallDisabledL.setText(wallDisabledL.getText().replace("(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE  + ")    ","")); break;
									case MEDIO: wallDisabledM.setText(wallDisabledM.getText().replace("(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE  + ")    ","")); break;
									case ALTO: wallDisabledH.setText(wallDisabledH.getText().replace("(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE  + ")    ","")); break;
									default: return;
								}
							}
							planimetria.get(piano+PIANO_OFFSET).remove(entry);
							redraw((Graphics2D) drawPanel.getGraphics());
							return;
						}
					}
				} else {
					System.out.println("Impossibile cancellare un valore assente");
				}
			}
		});	

		enableWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int xWallS, yWallS, xWallE, yWallE;
				try {
					xWallS = Integer.parseInt(wallStartxTxt.getText());
				} catch (NumberFormatException nfe) {
					wallStartxTxt.setText("0");
					xWallS = 0;
				}
				try {
					yWallS = Integer.parseInt(wallStartyTxt.getText());
				} catch (NumberFormatException nfe) {
					wallStartyTxt.setText("0");
					yWallS = 0;
				}
				try {
					xWallE = Integer.parseInt(wallEndxTxt.getText());
				} catch (NumberFormatException nfe) {
					wallEndxTxt.setText("0");
					xWallE = 0;
				}
				try {
					yWallE = Integer.parseInt(wallEndyTxt.getText());
				} catch (NumberFormatException nfe) {
					wallEndyTxt.setText("0");
					yWallE = 0;
				}
				Line2D wall = new Line2D.Float(xWallS, yWallS, xWallE, yWallE);
				if(validateWallPosition(wall)) {
					for(Walls entry : planimetria.get(piano+PIANO_OFFSET).keySet()) {
						if(entry.getPosition().getP1().equals(wall.getP1()) && entry.getPosition().getP2().equals(wall.getP2())) {
							boolean flag = !(planimetria.get(piano+PIANO_OFFSET).get(entry));
							planimetria.get(piano+PIANO_OFFSET).replace(entry, flag);
							if(flag) {
								drawWall(drawPanel.getGraphics(),entry);
								switch(entry.getImpact()) {
									case BASSO:
										wallDisabledL.setText(wallDisabledL.getText().replace("(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE  + ")    ",""));
										wallEnabledL.setText(wallEnabledL.getText() + "(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE  + ")    ");
										break; 
									case MEDIO:
										wallDisabledM.setText(wallDisabledM.getText().replace("(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE  + ")    ",""));
										wallEnabledM.setText(wallEnabledM.getText() + "(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE  + ")    ");
										break;
									case ALTO:
										wallDisabledH.setText(wallDisabledH.getText().replace("(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE  + ")    ",""));
										wallEnabledH.setText(wallEnabledH.getText() + "(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE  + ")    ");
										break; 
									default: return;
								}
							} else {
								redraw(drawPanel.getGraphics());
								switch(entry.getImpact()) {
									case BASSO:
										wallEnabledL.setText(wallEnabledL.getText().replace("(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE  + ")    ",""));
										wallDisabledL.setText(wallDisabledL.getText() + "(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE  + ")    ");
										break; 
									case MEDIO:
										wallEnabledM.setText(wallEnabledM.getText().replace("(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE  + ")    ",""));
										wallDisabledM.setText(wallDisabledM.getText() + "(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE  + ")    ");
										break;
									case ALTO:
										wallEnabledH.setText(wallEnabledH.getText().replace("(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE  + ")    ",""));
										wallDisabledH.setText(wallDisabledH.getText() + "(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE  + ")    ");
										break; 
									default: return;
								}
							}
							return;
						}
					}
					System.out.println("Il valore inserito è inesistente; premere Crea per aggiungerlo");
				} else {
					System.out.println("Errore: la posizione deve essere una coppia di numeri positivi minori rispettivamente di " + WIDTH + " e " + HEIGHT + ", multipli di " + DIM_SQUARE + " (centimetri), che definiscano una linea non nulla parallela ad un asse");
				}
			}
		});	

		createUtilizer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int xUtil, yUtil;
				try {
					xUtil = Integer.parseInt(xUtilTxt.getText());
				} catch (NumberFormatException nfe) {
					xUtilTxt.setText("0");
					xUtil = 0;
				}
				try {
					yUtil = Integer.parseInt(yUtilTxt.getText());
				} catch (NumberFormatException nfe) {
					yUtilTxt.setText("0");
					yUtil = 0;
				}
				Point util = new Point(xUtil,yUtil);
				if(validatePosition(util)) {
					for(Utilizers entry : consumatori.get(piano+PIANO_OFFSET).keySet()) {
						if(entry.getPosition().equals(util)) {
							System.out.println("Valore già presente");
							return;
						}
					}
					Utilizers utilizzatore = new Utilizers(util);
					consumatori.get(piano+PIANO_OFFSET).put(utilizzatore, true);
					drawUtilizer((Graphics2D) drawPanel.getGraphics(),util);
					utilEnabled.setText(utilEnabled.getText() + "(" + util.x + " " + util.y + ")    ");
				} else {
					System.out.println("Errore: la posizione deve essere una coppia di numeri positivi minori rispettivamente di " + WIDTH + " e " + HEIGHT);
				}
			}
		});	

		deleteUtilizer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int xUtil, yUtil;
				try {
					xUtil = Integer.parseInt(xUtilTxt.getText());
				} catch (NumberFormatException nfe) {
					xUtilTxt.setText("0");
					xUtil = 0;
				}
				try {
					yUtil = Integer.parseInt(yUtilTxt.getText());
				} catch (NumberFormatException nfe) {
					yUtilTxt.setText("0");
					yUtil = 0;
				}
				Point util = new Point(xUtil,yUtil);
				if(validatePosition(util)) {
					for(Utilizers entry : consumatori.get(piano+PIANO_OFFSET).keySet()) {
						if(entry.getPosition().equals(util)) {
							if(consumatori.get(piano+PIANO_OFFSET).get(entry)) {
								utilEnabled.setText(utilEnabled.getText().replace("(" + util.x + " " + util.y + ")    ",""));
							} else {
								utilDisabled.setText(utilDisabled.getText().replace("(" + util.x + " " + util.y + ")    ",""));
							}
							consumatori.get(piano+PIANO_OFFSET).remove(entry);
							redraw((Graphics2D) drawPanel.getGraphics());
							return;
						}
					}
					System.out.println("Impossibile cancellare un valore assente");
				} else {
					System.out.println("Errore: la posizione deve essere una coppia di numeri positivi minori rispettivamente di " + WIDTH + " e " + HEIGHT);
				}
			}
		});
		
		enableUtilizer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int xUtil, yUtil;
				try {
					xUtil = Integer.parseInt(xUtilTxt.getText());
				} catch (NumberFormatException nfe) {
					xUtilTxt.setText("0");
					xUtil = 0;
				}
				try {
					yUtil = Integer.parseInt(yUtilTxt.getText());
				} catch (NumberFormatException nfe) {
					yUtilTxt.setText("0");
					yUtil = 0;
				}
				Point util = new Point(xUtil,yUtil);
				if(validatePosition(util)) {
					for(Utilizers entry : consumatori.get(piano+PIANO_OFFSET).keySet()) {
						if(entry.getPosition().equals(util)) {
							boolean flag = !(consumatori.get(piano+PIANO_OFFSET).get(entry));
							consumatori.get(piano+PIANO_OFFSET).replace(entry, flag);
							if(flag) {
								drawUtilizer((Graphics2D) drawPanel.getGraphics(),util);
								utilEnabled.setText(utilEnabled.getText() + "(" + util.x + " " + util.y + ")    ");
								utilDisabled.setText(utilDisabled.getText().replace("(" + util.x + " " + util.y + ")    ",""));
							} else {
								redraw((Graphics2D) drawPanel.getGraphics());
								utilDisabled.setText(utilDisabled.getText() + "(" + util.x + " " + util.y + ")    ");
								utilEnabled.setText(utilEnabled.getText().replace("(" + util.x + " " + util.y + ")    ",""));
							}
							return;
						}
					}
					System.out.println("Il valore inserito è inesistente; premere Crea per aggiungerlo");
					/*Utilizers utilizzatore = new Utilizers(util);
					consumatori.put(utilizzatore, true);
					drawUtilizer((Graphics2D) drawPanel.getGraphics(),util);
					utilEnabled.setText(utilEnabled.getText() + "(" + util.x + " " + util.y + ")    ");*/
				} else {
					System.out.println("Errore: la posizione deve essere una coppia di numeri positivi minori rispettivamente di " + WIDTH + " e " + HEIGHT);
				}
			}
		});	
		
		generateResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double int_tot, int_em, attenuazione, angolo, radStart, radEnd;
				resultPanel.getGraphics().clearRect(0, 0, WIDTH, HEIGHT);
				//coloro la mappa come prima cosa cosicché muri ed emittenti sovrascrivano i colori e non viceversa
				if(apparati.get(piano+PIANO_OFFSET).containsValue(true)) {
					for (i = DIM_SQUARE/2; i < HEIGHT; i += DIM_SQUARE) {
						for (j = DIM_SQUARE/2; j < WIDTH; j += DIM_SQUARE) {
							int_tot = 0;
							for(Map.Entry<Emitters,Boolean> entryE : apparati.get(piano+PIANO_OFFSET).entrySet()) {
								if(!entryE.getValue()) { continue; }
								int_em = entryE.getKey().getmW();
								if((entryE.getKey().getAngles().y - entryE.getKey().getAngles().x)<360) {
									angolo = (double) Math.atan2(i - entryE.getKey().getPosition().y, j - entryE.getKey().getPosition().x);
									if(angolo<0) {
										angolo += 2*Math.PI;
									}
									radStart = Math.toRadians(entryE.getKey().getAngles().x);
									radEnd = Math.toRadians(entryE.getKey().getAngles().y);
									if(radEnd<radStart) {
										radEnd += 2*Math.PI;
									}
									if(radStart > angolo || radEnd < angolo) {
										continue;
									}
									int_em = int_em * (2*Math.PI) / (radEnd - radStart);		//le antenne direzionali hanno guadagno sul fronte
								}																//1 elemento=1 cm; divido per 100 per convertire in metri
								
								attenuazione = -27.55 + 20*(Math.log10(Math.sqrt(Math.pow(((double) i- (double) entryE.getKey().getPosition().y)/100,2)+Math.pow(((double) j- (double) entryE.getKey().getPosition().x)/100,2))) + Math.log10((double) entryE.getKey().getMHz()));
								for(Map.Entry<Walls,Boolean> entryM : planimetria.get(piano+PIANO_OFFSET).entrySet()) {
									if(!entryM.getValue()) { continue; }
									
									if(Line2D.linesIntersect(entryM.getKey().getPosition().getX1(),entryM.getKey().getPosition().getY1(),entryM.getKey().getPosition().getX2(),entryM.getKey().getPosition().getY2(),entryE.getKey().getPosition().x,-entryE.getKey().getPosition().y,j,i)) {
										switch(entryM.getKey().getImpact()) {
											case BASSO: int_em /= 2; break;
											case MEDIO: int_em /= 10; break;
											case ALTO: int_em /= 100; break;
											default: continue;
										}
									}
								}
								int_em = 10*Math.log10(int_em) - attenuazione - MIN_INT;
								if(int_em > 0) {
									int_tot += int_em;
								}
							}
							if(int_tot >= 0) {
								intensity[i/DIM_SQUARE][j/DIM_SQUARE][piano+PIANO_OFFSET] = int_tot;
								int_tot = 0;
								for(int p=0; p<PIANO_OFFSET*4; p++) {
									int_tot += intensity[i][j][p]/(Math.pow(100,Math.abs(piano + PIANO_OFFSET - p)));
								}
							    risultato = (int) Math.floor(int_tot/15);
							    if(risultato>4) {risultato=4;}
							    paintComponent(resultPanel.getGraphics());
							    if(valueCheck.isSelected()) {
							    	resultPanel.getGraphics().drawString(String.valueOf((int) int_tot+MIN_INT), j, i);
							    }
							}
						}
					}
				}
				result(resultPanel.getGraphics());	//sarebbe meglio un modo per copiare la mappa con background trasparente
			}
		});
		
		lowerFloor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(piano == PIANO_OFFSET*3) {
					upperFloor.setEnabled(true);
				}
				piano--;
				if(piano + PIANO_OFFSET == 0) {
					lowerFloor.setEnabled(false);
				}
				Graphics draw = drawPanel.getGraphics();
				Graphics result = resultPanel.getGraphics();
				redraw(draw);
				redraw(result);
				emitEnabled.setText("");
				emitEnabled.setText("");
				wallEnabledL.setText("");
				wallEnabledM.setText("");
				wallEnabledH.setText("");
				wallDisabledL.setText("");
				wallDisabledM.setText("");
				wallDisabledH.setText("");
				utilEnabled.setText("");
				utilDisabled.setText("");
				for(Map.Entry<Emitters,Boolean> entry : apparati.get(piano+PIANO_OFFSET).entrySet()) {
					if(entry.getValue()) {
						emitEnabled.setText(emitEnabled.getText() + "(" + entry.getKey().getPosition().x + " " + entry.getKey().getPosition().y + ") " + entry.getKey().getmW() + " mW  " + entry.getKey().getMHz() + " MHz  [" + entry.getKey().getAngles().x + "°-" + entry.getKey().getAngles().y + "°]    ");
						
					} else {
						emitDisabled.setText(emitDisabled.getText() + "(" + entry.getKey().getPosition().x + " " + entry.getKey().getPosition().y + ") " + entry.getKey().getmW() + " mW  " + entry.getKey().getMHz() + " MHz  [" + entry.getKey().getAngles().x + "°-" + entry.getKey().getAngles().y + "°]    ");
					}
				}
				for(Map.Entry<Walls, Boolean> entry : planimetria.get(piano+PIANO_OFFSET).entrySet()) {
					if(entry.getValue()) {
						switch(entry.getKey().getImpact()) {
							case BASSO: wallEnabledL.setText(wallEnabledL.getText() + "(" + entry.getKey().getPosition().getX1() + " " + entry.getKey().getPosition().getY1() + ")-(" + entry.getKey().getPosition().getX2() + " " + entry.getKey().getPosition().getY2()  + ")    "); break; 
							case MEDIO: wallEnabledM.setText(wallEnabledM.getText() + "(" + entry.getKey().getPosition().getX1() + " " + entry.getKey().getPosition().getY1() + ")-(" + entry.getKey().getPosition().getX2() + " " + entry.getKey().getPosition().getY2()  + ")    "); break;
							case ALTO: wallEnabledH.setText(wallEnabledH.getText() + "(" + entry.getKey().getPosition().getX1() + " " + entry.getKey().getPosition().getY1() + ")-(" + entry.getKey().getPosition().getX2() + " " + entry.getKey().getPosition().getY2()  + ")    "); break; 
							default: continue;
						}
					} else {
						switch(entry.getKey().getImpact()) {
							case BASSO:	wallDisabledL.setText(wallDisabledL.getText() + "(" + entry.getKey().getPosition().getX1() + " " + entry.getKey().getPosition().getY1() + ")-(" + entry.getKey().getPosition().getX2() + " " + entry.getKey().getPosition().getY2()  + ")    "); break; 
							case MEDIO:	wallDisabledM.setText(wallDisabledM.getText() + "(" + entry.getKey().getPosition().getX1() + " " + entry.getKey().getPosition().getY1() + ")-(" + entry.getKey().getPosition().getX2() + " " + entry.getKey().getPosition().getY2()  + ")    "); break;
							case ALTO: wallDisabledH.setText(wallDisabledH.getText() + "(" + entry.getKey().getPosition().getX1() + " " + entry.getKey().getPosition().getY1() + ")-(" + entry.getKey().getPosition().getX2() + " " + entry.getKey().getPosition().getY2()  + ")    "); break; 
							default: continue;
						}
					}
				}
				for(Map.Entry<Utilizers,Boolean> entry : consumatori.get(piano+PIANO_OFFSET).entrySet()) {
					if(entry.getValue()) {
						utilEnabled.setText(utilEnabled.getText() + "(" + entry.getKey().getPosition().x + " " + entry.getKey().getPosition().y + ")    ");
					} else {
						utilDisabled.setText(utilDisabled.getText() + "(" + entry.getKey().getPosition().x + " " + entry.getKey().getPosition().y + ")    ");
					}
				}
				lowerFloor.setText("Vai a piano " + (piano-1));
				currentFloor.setText("Piano: " + piano);
				upperFloor.setText("Vai a piano " + (piano+1));
			}
		});	

		upperFloor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(piano + PIANO_OFFSET == 0) {
					lowerFloor.setEnabled(true);
				}
				piano++;
				if(piano == PIANO_OFFSET*3) {
					upperFloor.setEnabled(false);
				}
				Graphics draw = drawPanel.getGraphics();
				Graphics result = resultPanel.getGraphics();
				redraw(draw);
				redraw(result);
				emitEnabled.setText("");
				emitEnabled.setText("");
				wallEnabledL.setText("");
				wallEnabledM.setText("");
				wallEnabledH.setText("");
				wallDisabledL.setText("");
				wallDisabledM.setText("");
				wallDisabledH.setText("");
				utilEnabled.setText("");
				utilDisabled.setText("");
				for(Map.Entry<Emitters,Boolean> entry : apparati.get(piano+PIANO_OFFSET).entrySet()) {
					if(entry.getValue()) {
						emitEnabled.setText(emitEnabled.getText() + "(" + entry.getKey().getPosition().x + " " + entry.getKey().getPosition().y + ") " + entry.getKey().getmW() + " mW  " + entry.getKey().getMHz() + " MHz  [" + entry.getKey().getAngles().x + "°-" + entry.getKey().getAngles().y + "°]    ");
						
					} else {
						emitDisabled.setText(emitDisabled.getText() + "(" + entry.getKey().getPosition().x + " " + entry.getKey().getPosition().y + ") " + entry.getKey().getmW() + " mW  " + entry.getKey().getMHz() + " MHz  [" + entry.getKey().getAngles().x + "°-" + entry.getKey().getAngles().y + "°]    ");
					}
				}
				for(Map.Entry<Walls, Boolean> entry : planimetria.get(piano+PIANO_OFFSET).entrySet()) {
					if(entry.getValue()) {
						switch(entry.getKey().getImpact()) {
							case BASSO: wallEnabledL.setText(wallEnabledL.getText() + "(" + entry.getKey().getPosition().getX1() + " " + entry.getKey().getPosition().getY1() + ")-(" + entry.getKey().getPosition().getX2() + " " + entry.getKey().getPosition().getY2()  + ")    "); break; 
							case MEDIO: wallEnabledM.setText(wallEnabledM.getText() + "(" + entry.getKey().getPosition().getX1() + " " + entry.getKey().getPosition().getY1() + ")-(" + entry.getKey().getPosition().getX2() + " " + entry.getKey().getPosition().getY2()  + ")    "); break;
							case ALTO: wallEnabledH.setText(wallEnabledH.getText() + "(" + entry.getKey().getPosition().getX1() + " " + entry.getKey().getPosition().getY1() + ")-(" + entry.getKey().getPosition().getX2() + " " + entry.getKey().getPosition().getY2()  + ")    "); break; 
							default: continue;
						}
					} else {
						switch(entry.getKey().getImpact()) {
							case BASSO:	wallDisabledL.setText(wallDisabledL.getText() + "(" + entry.getKey().getPosition().getX1() + " " + entry.getKey().getPosition().getY1() + ")-(" + entry.getKey().getPosition().getX2() + " " + entry.getKey().getPosition().getY2()  + ")    "); break; 
							case MEDIO:	wallDisabledM.setText(wallDisabledM.getText() + "(" + entry.getKey().getPosition().getX1() + " " + entry.getKey().getPosition().getY1() + ")-(" + entry.getKey().getPosition().getX2() + " " + entry.getKey().getPosition().getY2()  + ")    "); break;
							case ALTO: wallDisabledH.setText(wallDisabledH.getText() + "(" + entry.getKey().getPosition().getX1() + " " + entry.getKey().getPosition().getY1() + ")-(" + entry.getKey().getPosition().getX2() + " " + entry.getKey().getPosition().getY2()  + ")    "); break; 
							default: continue;
						}
					}
				}
				for(Map.Entry<Utilizers,Boolean> entry : consumatori.get(piano+PIANO_OFFSET).entrySet()) {
					if(entry.getValue()) {
						utilEnabled.setText(utilEnabled.getText() + "(" + entry.getKey().getPosition().x + " " + entry.getKey().getPosition().y + ")    ");
					} else {
						utilDisabled.setText(utilDisabled.getText() + "(" + entry.getKey().getPosition().x + " " + entry.getKey().getPosition().y + ")    ");
					}
				}
				lowerFloor.setText("Vai a piano " + (piano-1));
				currentFloor.setText("Piano: " + piano);
				upperFloor.setText("Vai a piano " + (piano+1));
			}
		});	

		duplicateMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int nuovoPiano;
				try {
					nuovoPiano = Integer.parseInt(duplicateMapTxt.getText());
				} catch (NumberFormatException nfe) {
					System.out.println("Il piano deve essere un numero da " + -PIANO_OFFSET + " a " + PIANO_OFFSET*3 + ", diverso dal piano corrente");
					return;
				}
				if(nuovoPiano>=-PIANO_OFFSET && nuovoPiano<=PIANO_OFFSET*3 && nuovoPiano != piano) {
					copyFloor(nuovoPiano, drawPanel.getGraphics(), resultPanel.getGraphics());
				} else {
					System.out.println("Il piano deve essere un numero da " + -PIANO_OFFSET + " a " + PIANO_OFFSET*3 + ", diverso dal piano corrente");
				}
			}
		});
			
    //Setting del frame
		resultPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setSize(1200, 1030);
		final JPanel coverLightPanel = new JPanel(new GridLayout(2, 2));
		this.getContentPane().add(coverLightPanel);
		coverLightPanel.add(captionPanel);
		coverLightPanel.add(buttonsPanel);
		coverLightPanel.add(canvasPanel);
		coverLightPanel.add(resultPanel);
	}

	public static void main(String[] args) {
	    new GUI();
	}
}
