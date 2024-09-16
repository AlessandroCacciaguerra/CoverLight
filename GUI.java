package MyClasses;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.Map;

public class GUI extends JFrame {
	private static final int DIM_SQUARE = 50;	// un valore divisibile per 2 e per cui width e height sono divisibili
	private static final int WIDTH = 1000;		// 10 metri
	private static final int HEIGHT = 800;		// 8 metri
	private static final int MIN_INT = -90;		// un segnale sotto -90 decibel milliwatt non è percepibile dalla comune apparecchiatura Wi-Fi
	private static final Color COL0 = Color.RED; 			 // Da -75 a -90 dbm valore più basso
	private static final Color COL1 = Color.ORANGE;			 // Da -60 a -75 dbm
	private static final Color COL2 = Color.YELLOW.darker(); // Da -45 a -60 dbm
	private static final Color COL3 = Color.GREEN;			 // Da -30 a -45 dbm
	private static final Color COL4 = Color.CYAN;			 // > -30 dbm valore più alto
	private static Color[] tones = { COL0, COL1, COL2, COL3, COL4 };
	private static int index = -1;
	private static final String LOW = "Low";
	private static final String AVERAGE = "Average";
	private static final String HIGH = "High";
	private static String impactSel = AVERAGE;
	private static final long serialVersionUID = 1L;
	private CanvasPanel drawPanel;
	private FloorLogicImpl floorLogic;
	private int helpDialogCounter;
	private static final int MIN_SLIDE = 0;
	private static final int MAX_SLIDE = 3;

	private boolean validatePosition(Point val) {
		return val.x >= 0 && val.x <= WIDTH && val.y >= 0 && val.y <= HEIGHT;
	}

	private int getWallWidth(String impact) {
		switch (impact) {
			case LOW:
				return 5;
			case AVERAGE:
				return 7;
			case HIGH:
				return 9;
			default:
				return 7;
		}
	}
	
	private boolean validateWallPosition(Line2D val) {
		return (val.getX1() >= 0 && val.getX1() <= WIDTH && val.getY1() >= 0 && val.getY1() <= HEIGHT
			&& val.getX2() >= 0 && val.getX2() <= WIDTH && val.getY2() >= 0 && val.getY2() <= HEIGHT) && // entro i margini
			(val.getX1() % DIM_SQUARE + val.getX2() % DIM_SQUARE + val.getY1() % DIM_SQUARE	+ val.getY2() % DIM_SQUARE == 0) // divisibili per DIM_SQUARE
			&& (val.getX1() == val.getX2() ^ val.getY1() == val.getY2()); // non nulli e paralleli ad un asse
	}

	public GUI() {
		// Inizializzazione
		floorLogic = new FloorLogicImpl();

		// Canvas
		final JPanel canvasPanel = new JPanel(new BorderLayout());
		final JPanel canvasPanelBorder = new JPanel(new BorderLayout());
		canvasPanelBorder.add(canvasPanel);
		canvasPanelBorder.setBorder(new JTextField().getBorder());
		final JPanel canvasPanelSeparator = new JPanel(new BorderLayout());
		canvasPanelSeparator.add(canvasPanelBorder);

		// Didascalia e titolo
		final JLabel canvasTitleLbl = new JLabel("Canvas");
		canvasTitleLbl.setFont(new Font("Serif", Font.BOLD, 25));
		final JButton captionBtn = new JButton("Didascalia");
		final JPanel canvasTitlePanel = new JPanel(new BorderLayout());
		canvasTitlePanel.add(canvasTitleLbl, BorderLayout.LINE_START);
		canvasTitlePanel.add(captionBtn, BorderLayout.LINE_END);
		canvasTitlePanel.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 20));

		// Area di disegno del canvas
		final JPanel canvasContainerPanel = new JPanel(new BorderLayout());// panel che contiene lo scroll
		drawPanel = floorLogic.getCurrentFloor().getCanvas();// panel che contiene il disegno
		final JScrollPane scrollCanvas = new JScrollPane(canvasContainerPanel);
		scrollCanvas.setPreferredSize(new Dimension(500, 400));
		canvasContainerPanel.add(drawPanel);

		// Pulsanti piano
		final JPanel canvasFloorPanel = new JPanel(new BorderLayout());

		// Pusanti e label per i piani
		final JPanel canvasFloorBtnPanel = new JPanel(new FlowLayout());
		final JButton previousFloorBtn = new JButton("Crea piano -1");
		final JLabel currentFloorLbl = new JLabel("Piano: "+ floorLogic.getCurrentFloorNumber());
		final JButton nextFloorBtn = new JButton("Crea piano 1");
		final JButton duplicateMapBtn = new JButton("Copia su piano");
		duplicateMapBtn.setEnabled(false); // non ha senso copiare mentre c'è un solo piano
		final JTextField duplicateMapTxt = new JTextField();
		duplicateMapTxt.setColumns(3);
		duplicateMapTxt.setEnabled(false); // non ha senso copiare mentre c'è un solo piano
		
		final JPanel canvasFloorCreateBtnPanel = new JPanel(new FlowLayout());
		canvasFloorCreateBtnPanel.add(previousFloorBtn);
		canvasFloorCreateBtnPanel.add(currentFloorLbl);
		canvasFloorCreateBtnPanel.add(nextFloorBtn);
		canvasFloorCreateBtnPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
		
		final JPanel canvasFloorduplicateBtnPanel = new JPanel(new FlowLayout());
		canvasFloorduplicateBtnPanel.add(duplicateMapBtn);
		canvasFloorduplicateBtnPanel.add(duplicateMapTxt);
		
		canvasFloorBtnPanel.add(canvasFloorCreateBtnPanel);
		canvasFloorBtnPanel.add(canvasFloorduplicateBtnPanel);
		
		// checkbox
		final JPanel canvasCheckboxPanel = new JPanel(new BorderLayout());
		JCheckBox currentFloorCB = new JCheckBox("Valori", false);
		currentFloorCB.setToolTipText("Se spuntato mostra il valore in dbm dell'intensità di ogni area");
		ToolTipManager.sharedInstance().setInitialDelay(10);
		canvasCheckboxPanel.add(currentFloorCB);

		canvasFloorPanel.add(canvasFloorBtnPanel, BorderLayout.CENTER);
		canvasFloorPanel.add(canvasCheckboxPanel, BorderLayout.LINE_END);

		// Aggiunta a canvas panel
		canvasPanel.add(canvasTitlePanel, BorderLayout.PAGE_START);
		canvasPanel.add(scrollCanvas, BorderLayout.CENTER);
		canvasPanel.add(canvasFloorPanel, BorderLayout.PAGE_END);

		// fine Canvas

		// <Componenti
		final JPanel buttonsPanelContainer = new JPanel(new BorderLayout());
		final JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		buttonsPanelContainer.add(buttonsPanel, BorderLayout.CENTER);
		
		//Help btn
		final JPanel helpBtnPanel = new JPanel(new BorderLayout());
		final JButton helpBtn = new JButton("?");
		helpBtnPanel.add(helpBtn, BorderLayout.LINE_END);
		buttonsPanelContainer.add(helpBtnPanel, BorderLayout.PAGE_START);
		
		// Emittente
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
		JLabel emitError = new JLabel("");

		layoutEmitter.setAutoCreateGaps(true);
		layoutEmitter.setAutoCreateContainerGaps(true);

		layoutEmitter.setHorizontalGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(layoutEmitter.createSequentialGroup()
						.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(createEmitter, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(deleteEmitter, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(enableDisableEmitter, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
						.addGroup(layoutEmitter.createSequentialGroup()
								.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(layoutEmitter.createSequentialGroup().addComponent(xEmitLbl)
												.addComponent(xEmitTxt))
										.addGroup(layoutEmitter.createSequentialGroup().addComponent(yEmitLbl)
												.addComponent(yEmitTxt))
										.addGroup(layoutEmitter.createSequentialGroup().addComponent(emitAngStartLbl)
												.addComponent(angStartEmitTxt)))
								.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(layoutEmitter.createSequentialGroup().addComponent(powEmitLbl)
												.addComponent(powEmitTxt))
										.addGroup(layoutEmitter.createSequentialGroup().addComponent(freqEmitLbl)
												.addComponent(freqEmitterTxt))
										.addGroup(layoutEmitter.createSequentialGroup().addComponent(emitAngEndLbl)
												.addComponent(angEndEmitTxt)))))
				.addComponent(emitError));

		layoutEmitter.setVerticalGroup(layoutEmitter.createSequentialGroup().addGroup(layoutEmitter
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layoutEmitter.createSequentialGroup().addComponent(createEmitter).addComponent(deleteEmitter)
						.addComponent(enableDisableEmitter))
				.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layoutEmitter.createSequentialGroup()
								.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addGroup(layoutEmitter.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(xEmitLbl).addComponent(xEmitTxt))
										.addGroup(layoutEmitter
												.createParallelGroup(GroupLayout.Alignment.BASELINE)
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
												.addComponent(emitAngEndLbl).addComponent(angEndEmitTxt))))))
				.addComponent(emitError));

		// Utilizzatore
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
		JLabel utilError = new JLabel("");

		layoutUtilizer.setAutoCreateGaps(true);
		layoutUtilizer.setAutoCreateContainerGaps(true);

		layoutUtilizer.setHorizontalGroup(layoutUtilizer.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(layoutUtilizer.createSequentialGroup()
						.addGroup(layoutUtilizer.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(createUtilizer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(deleteUtilizer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(enableUtilizer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
						.addGroup(layoutUtilizer.createSequentialGroup()
								.addGroup(layoutUtilizer.createSequentialGroup().addComponent(xUtilLbl)
										.addComponent(xUtilCurVal).addComponent(xUtilTxt))
								.addGroup(layoutUtilizer.createSequentialGroup().addComponent(yUtilLbl)
										.addComponent(yUtilCurVal).addComponent(yUtilTxt))))
				.addComponent(utilError));

		layoutUtilizer.setVerticalGroup(layoutUtilizer.createSequentialGroup().addGroup(layoutUtilizer
				.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(layoutUtilizer.createSequentialGroup().addComponent(createUtilizer)
						.addComponent(deleteUtilizer).addComponent(enableUtilizer))
				.addGroup(layoutUtilizer.createParallelGroup(GroupLayout.Alignment.BASELINE).addGroup(layoutUtilizer
						.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGroup(layoutUtilizer.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(xUtilLbl).addComponent(xUtilCurVal).addComponent(xUtilTxt))
						.addGroup(layoutUtilizer.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addGroup(layoutUtilizer.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(yUtilLbl).addComponent(yUtilCurVal).addComponent(yUtilTxt))))))
				.addComponent(utilError));

		// Muro
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
		JRadioButton rAverage = new JRadioButton("Medio", true);
		JRadioButton rHigh = new JRadioButton("Alto", false);
		ButtonGroup impactGroup = new ButtonGroup();
		impactGroup.add(rLow);
		impactGroup.add(rAverage);
		impactGroup.add(rHigh);
		JLabel wallError = new JLabel("");

		layoutWall.setAutoCreateGaps(true);
		layoutWall.setAutoCreateContainerGaps(true);

		layoutWall.setHorizontalGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.CENTER).addGroup(layoutWall
				.createSequentialGroup()
				.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.LEADING, false)
						.addComponent(createWall, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(deleteWall, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(enableWall, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
								.addComponent(rAverage).addComponent(rHigh))))
				.addComponent(wallError));

		layoutWall.setVerticalGroup(layoutWall.createSequentialGroup().addGroup(layoutWall
				.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(layoutWall.createSequentialGroup().addComponent(createWall).addComponent(deleteWall)
						.addComponent(enableWall))
				.addGroup(layoutWall.createSequentialGroup()
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
						.addGroup(layoutWall.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(impactLbl)
								.addComponent(rLow).addComponent(rAverage).addComponent(rHigh))))
				.addComponent(wallError));

		buttonsPanel.add(panelEmitter);// Aggiunta al panel
		buttonsPanel.add(panelUtilizer);
		buttonsPanel.add(panelWall);

		// Panel results
		CanvasPanel resultPanel = new CanvasPanel();
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

		// Panel lists
		final JPanel listsPanel = new JPanel();
		listsPanel.setLayout(new BoxLayout(listsPanel, BoxLayout.Y_AXIS));

		// Emitters
		final JPanel listEmitterPanel = new JPanel(); // Emitters list
		final GroupLayout listEmitterLayout = new GroupLayout(listEmitterPanel);
		listEmitterPanel.setLayout(listEmitterLayout);

		JLabel emitterListEnabledLbl = new JLabel("Emettitori abilitati");
		final JPanel emitterListEnabledPanel = new JPanel(new BorderLayout());
		final JTextArea emitterListEnabledTxtArea = new JTextArea();
		emitterListEnabledTxtArea.setLineWrap(true);
		final JScrollPane emitterListEnabledScroll = new JScrollPane(emitterListEnabledTxtArea);
		emitterListEnabledScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		emitterListEnabledPanel.add(emitterListEnabledScroll);

		JLabel emitterListDisabledLbl = new JLabel("Emettitori disabilitati");
		final JPanel emitterListDisabledPanel = new JPanel(new BorderLayout());
		final JTextArea emitterListDisabledTxtArea = new JTextArea();
		emitterListDisabledTxtArea.setLineWrap(true);
		final JScrollPane emitterListDisabledScroll = new JScrollPane(emitterListDisabledTxtArea);
		emitterListDisabledScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		emitterListDisabledPanel.add(emitterListDisabledScroll);

		listEmitterLayout.setHorizontalGroup(listEmitterLayout.createSequentialGroup()
				.addGroup(listEmitterLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(emitterListEnabledLbl).addComponent(emitterListEnabledPanel))
				.addGroup(listEmitterLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(emitterListDisabledLbl).addComponent(emitterListDisabledPanel)));

		listEmitterLayout.setVerticalGroup(listEmitterLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(listEmitterLayout.createSequentialGroup().addComponent(emitterListEnabledLbl)
						.addComponent(emitterListEnabledPanel))
				.addGroup(listEmitterLayout.createSequentialGroup().addComponent(emitterListDisabledLbl)
						.addComponent(emitterListDisabledPanel)));

		// Utilizers
		final JPanel listUtilizerPanel = new JPanel(); // Emitters list
		final GroupLayout listUtilizerLayout = new GroupLayout(listUtilizerPanel);
		listUtilizerPanel.setLayout(listUtilizerLayout);

		JLabel utilizerListEnabledLbl = new JLabel("Utilizzatori abilitati");
		final JPanel utilizerListEnabledPanel = new JPanel(new BorderLayout());
		final JTextArea utilizerListEnabledTxtArea = new JTextArea();
		utilizerListEnabledTxtArea.setLineWrap(true);
		final JScrollPane utilizerListEnabledScroll = new JScrollPane(utilizerListEnabledTxtArea);
		utilizerListEnabledScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		utilizerListEnabledPanel.add(utilizerListEnabledScroll);

		JLabel utilizerListDisabledLbl = new JLabel("Utilizzatori disabilitati");
		final JPanel utilizerListDisabledPanel = new JPanel(new BorderLayout());
		final JTextArea utilizerListDisabledTxtArea = new JTextArea();
		utilizerListDisabledTxtArea.setLineWrap(true);
		final JScrollPane utilizerListDisabledScroll = new JScrollPane(utilizerListDisabledTxtArea);
		utilizerListDisabledScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		utilizerListDisabledPanel.add(utilizerListDisabledScroll);

		listUtilizerLayout.setHorizontalGroup(listUtilizerLayout.createSequentialGroup()
				.addGroup(listUtilizerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(utilizerListEnabledLbl).addComponent(utilizerListEnabledPanel))
				.addGroup(listUtilizerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(utilizerListDisabledLbl).addComponent(utilizerListDisabledPanel)));

		listUtilizerLayout.setVerticalGroup(listUtilizerLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(listUtilizerLayout.createSequentialGroup().addComponent(utilizerListEnabledLbl)
						.addComponent(utilizerListEnabledPanel))
				.addGroup(listUtilizerLayout.createSequentialGroup().addComponent(utilizerListDisabledLbl)
						.addComponent(utilizerListDisabledPanel)));

		// Walls
		final JPanel listWallPanel = new JPanel(); // Emitters list final
		GroupLayout listWallLayout = new GroupLayout(listWallPanel);
		listWallPanel.setLayout(listWallLayout);

		JLabel wallListEnabledLbl = new JLabel("Muri abilitati");
		final JPanel wallListEnabledPanel = new JPanel(new BorderLayout());
		final JTextArea wallListEnabledTxtArea = new JTextArea();
		wallListEnabledTxtArea.setLineWrap(true);
		final JScrollPane wallListEnabledScroll = new JScrollPane(wallListEnabledTxtArea);
		wallListEnabledScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		wallListEnabledPanel.add(wallListEnabledScroll);

		JLabel wallListDisabledLbl = new JLabel("Muri disabilitati");
		final JPanel wallListDisabledPanel = new JPanel(new BorderLayout());
		final JTextArea wallListDisabledTxtArea = new JTextArea();
		wallListDisabledTxtArea.setLineWrap(true);
		final JScrollPane wallListDisabledScroll = new JScrollPane(wallListDisabledTxtArea);
		wallListDisabledScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		wallListDisabledPanel.add(wallListDisabledScroll);

		listWallLayout.setHorizontalGroup(listWallLayout.createSequentialGroup()
				.addGroup(listWallLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(wallListEnabledLbl).addComponent(wallListEnabledPanel))
				.addGroup(listWallLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(wallListDisabledLbl).addComponent(wallListDisabledPanel)));

		listWallLayout.setVerticalGroup(listWallLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(listWallLayout.createSequentialGroup().addComponent(wallListEnabledLbl)
						.addComponent(wallListEnabledPanel))
				.addGroup(listWallLayout.createSequentialGroup().addComponent(wallListDisabledLbl)
						.addComponent(wallListDisabledPanel)));

		listsPanel.add(listEmitterPanel);
		listsPanel.add(listUtilizerPanel);
		listsPanel.add(listWallPanel);
		// Fine componenti

		
		// Setting del frame
		resultPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 10));
		buttonsPanelContainer.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
		listsPanel.setBorder(BorderFactory.createEmptyBorder(35, 0, 0, 0));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setSize(1500, 1030);
		final JPanel coverLightPanel = new JPanel(new GridLayout(2, 2));
		coverLightPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
		this.getContentPane().add(coverLightPanel);
		coverLightPanel.add(canvasPanelSeparator);
		coverLightPanel.add(buttonsPanelContainer);
		coverLightPanel.add(resultPanel);
		coverLightPanel.add(listsPanel);

		
		// Btn listeners		
		captionBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Didascalia
				final JDialog captionDialog = new JDialog();
				captionDialog.setTitle("Didascalia");
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

				// Colori
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

				// Set Frame
				captionPanel.add(scroll);
				captionDialog.add(captionPanel, BorderLayout.CENTER);
				captionDialog.add(colorPanel, BorderLayout.PAGE_END);
				captionDialog.setSize(1050, 600);
				captionDialog.setVisible(true);

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
			}
		});		
		
		helpBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Didascalia
				helpDialogCounter = 0; //Contatore per le slide
				final JDialog helpDialog = new JDialog();
				helpDialog.setTitle("Help");
				final JPanel helpPanel = new JPanel(new BorderLayout());					
				final JPanel helpPanelSeparator = new JPanel(new BorderLayout());
				final JPanel helpPanelBorder = new JPanel(new BorderLayout());
				final JPanel helpPanelSpacing = new JPanel(new BorderLayout());
				helpPanelSeparator.add(helpPanel);
				helpPanelBorder.add(helpPanelSeparator);
				helpPanelSpacing.add(helpPanelBorder);				
				helpPanelBorder.setBorder(new LineBorder(Color.BLACK, 2));
				helpPanelSpacing.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				
				// next and previous btn
				final JPanel btnHelpPanel = new JPanel(new BorderLayout());
				final JPanel btnHelpPanelContainer = new JPanel(new BorderLayout());
				final JPanel btnHelpPanelSpacing = new JPanel(new BorderLayout());
				final JLabel slideTitle = new JLabel("Introduzione");
				JPanel slideTitleGB = new JPanel(new GridBagLayout());
				slideTitleGB.add(slideTitle,new GridBagConstraints());
				btnHelpPanelContainer.add(btnHelpPanel);
				btnHelpPanelSpacing.add(btnHelpPanelContainer);
				final JButton nextBtn = new JButton("Next");
				final JButton previousBtn = new JButton("Previous");
				previousBtn.setEnabled(false);
				btnHelpPanel.add(nextBtn, BorderLayout.LINE_END);
				btnHelpPanel.add(slideTitleGB, BorderLayout.CENTER);
				btnHelpPanel.add(previousBtn, BorderLayout.LINE_START);
				
				btnHelpPanel.setBorder(BorderFactory.createEmptyBorder(15, 70, 15, 70));
				btnHelpPanelContainer.setBorder(new LineBorder(Color.BLACK, 1));
				btnHelpPanelSpacing.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
							
				HelpDialogImpl helpDialogClass = new HelpDialogImpl();					
				helpPanel.add(helpDialogClass.getFirstSlide());//First Slide		
				
				nextBtn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {	
						helpDialogCounter = helpDialogCounter<=MAX_SLIDE? helpDialogCounter+1 : helpDialogCounter;
						if(previousBtn.isEnabled() == false && (helpDialogCounter > MIN_SLIDE)) {
							previousBtn.setEnabled(true);
						}
						if(helpDialogCounter == MAX_SLIDE) {
							nextBtn.setEnabled(false);
						}
						helpPanel.removeAll();
						switch(helpDialogCounter) {
							case 0:
								helpPanel.add(helpDialogClass.getFirstSlide());	
								slideTitle.setText("Introduzione");
								break;
							case 1:
								helpPanel.add(helpDialogClass.getSecondSlide());
								slideTitle.setText("Area di disegno");
								break;
							case 2:
								helpPanel.add(helpDialogClass.getThirdSlide());
								slideTitle.setText("Area input");
								break;
							case 3:
								helpPanel.add(helpDialogClass.getFourthSlide());
								slideTitle.setText("Area di output");
								break;
						}
						helpPanel.revalidate();
						helpPanel.updateUI();
					}
				});
				
				previousBtn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {	
						helpDialogCounter = helpDialogCounter>=MIN_SLIDE? helpDialogCounter-1 : helpDialogCounter;
						if(nextBtn.isEnabled() == false && (helpDialogCounter < MAX_SLIDE)) {
							nextBtn.setEnabled(true);
						}
						if(helpDialogCounter == MIN_SLIDE) {
							previousBtn.setEnabled(false);
						}
						helpPanel.removeAll();
						switch(helpDialogCounter) {
							case 0:
								helpPanel.add(helpDialogClass.getFirstSlide());
								slideTitle.setText("Introduzione");
								break;
							case 1:
								helpPanel.add(helpDialogClass.getSecondSlide());
								slideTitle.setText("Area di disegno");
								break;
							case 2:
								helpPanel.add(helpDialogClass.getThirdSlide());
								slideTitle.setText("Area input");
								break;
							case 3:
								helpPanel.add(helpDialogClass.getFourthSlide());
								slideTitle.setText("Area di output");
								break;
						}
						helpPanel.revalidate();
						helpPanel.updateUI();
					}
				});			
				
				// Set Frame
				helpDialog.add(helpPanelSpacing, BorderLayout.CENTER);
				helpDialog.add(btnHelpPanelSpacing, BorderLayout.PAGE_END);
				helpDialog.setSize(1050, 600);
				helpDialog.setVisible(true);
			}			
		});
	
		createEmitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				powEmitTxt.setBorder(new JTextField().getBorder());// Reset dei border
				freqEmitterTxt.setBorder(new JTextField().getBorder());
				angStartEmitTxt.setBorder(new JTextField().getBorder());
				angEndEmitTxt.setBorder(new JTextField().getBorder());
				xEmitTxt.setBorder(new JTextField().getBorder());
				yEmitTxt.setBorder(new JTextField().getBorder());
				emitterListEnabledTxtArea.setBorder(new JTextField().getBorder());
				emitterListDisabledTxtArea.setBorder(new JTextField().getBorder());
				emitError.setText("");
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
					if (pow > 200 || pow <= 0) { // normative ETSI EN
						emitError.setText("La potenza non deve superare i 200 milliWatt");
						powEmitTxt.setBorder(new LineBorder(Color.red, 1));// Set border to red
						return;
					}
				} catch (NumberFormatException nfe) {
					emitError.setText("La potenza non deve superare i 200 milliWatt");
					powEmitTxt.setBorder(new LineBorder(Color.red, 1));
					return;
				}
				try {
					freq = Float.parseFloat(freqEmitterTxt.getText());
					if ((freq < 5150 || freq > 5350)) { // normative ETSI EN
						emitError.setText(
								"La frequenza del Wi-Fi all'interno di un edificio deve essere compresa fra 5150 e 5350 MegaHertz");
						freqEmitterTxt.setBorder(new LineBorder(Color.red, 1));// Set border to red
						return;
					}
				} catch (NumberFormatException nfe) {
					emitError.setText(
							"La frequenza del Wi-Fi all'interno di un edificio deve essere compresa fra 5150 e 5350 MegaHertz");
					freqEmitterTxt.setBorder(new LineBorder(Color.red, 1));// Set border to red
					return;
				}
				try {
					angS = Integer.parseInt(angStartEmitTxt.getText());
					if (angS < 0 || angS >= 360) {
						emitError.setText("Gli angoli devono avere valore compreso fra 0 e 359");
						angStartEmitTxt.setBorder(new LineBorder(Color.red, 1));// Set border to red
						return;
					}
				} catch (NumberFormatException nfe) {
					angStartEmitTxt.setText("0");
					angS = 0;
				}
				try {
					angE = Integer.parseInt(angEndEmitTxt.getText());
					if (angE <= 0 || angE > 360) {
						emitError.setText("L'angolo di fine deve avere valore compreso fra 1 e 360");
						angEndEmitTxt.setBorder(new LineBorder(Color.red, 1));// Set border to red
						return;
					}
				} catch (NumberFormatException nfe) {
					angEndEmitTxt.setText("360");
					angE = 0;
				}
				Point emit = new Point(xEmit, yEmit);
				if(validatePosition(emit)) {
					for(Emitters entry : floorLogic.getCurrentFloor().getEmitters().keySet()) { //controllo compenetrazione
						if (emit.distance(entry.getPosition()) < DIM_SQUARE / 2) {
							emitError.setText("Gli emittenti devono essere distanziati di almeno " + DIM_SQUARE / 2
									+ " centimetri");
							xEmitTxt.setBorder(new LineBorder(Color.red, 1));
							yEmitTxt.setBorder(new LineBorder(Color.red, 1));
							if(floorLogic.getCurrentFloor().getEmitters().get(entry)) {
								emitterListEnabledTxtArea.setBorder(new LineBorder(Color.red, 1));
							} else {
								emitterListDisabledTxtArea.setBorder(new LineBorder(Color.red, 1));
							}
							return;
						}
					}
					Emitters emitter = new Emitters(emit, pow, freq, angS, angE);
					Rectangle rect = new Rectangle(xEmit-DIM_SQUARE/2,yEmit-DIM_SQUARE/2,DIM_SQUARE,DIM_SQUARE);
					floorLogic.getCurrentFloor().setNewEmitter(emitter);
					floorLogic.getCurrentFloor().setNewGraphicalEmitter(rect, emitter.getAngles());
					drawPanel = floorLogic.getCurrentFloor().getCanvas();
					drawPanel.repaint();
					emitterListEnabledTxtArea.setText(emitterListEnabledTxtArea.getText() + "(" + emit.x + " " + emit.y + ") " + pow + " mW  "
							+ freq + " MHz  [" + angS + "°-" + angE + "°]\n");
				}
			}
		});

		deleteEmitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int xEmit, yEmit;
				xEmitTxt.setBorder(new JTextField().getBorder());
				yEmitTxt.setBorder(new JTextField().getBorder());
				emitterListEnabledTxtArea.setBorder(new JTextField().getBorder());
				emitterListDisabledTxtArea.setBorder(new JTextField().getBorder());
				emitError.setText("");
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
				Point emit = new Point(xEmit, yEmit);
				if (validatePosition(emit)) {
					for (Emitters entry : floorLogic.getCurrentFloor().getEmitters().keySet()) {
						if (entry.getPosition().equals(emit)) {
							if (floorLogic.getCurrentFloor().getEmitters().get(entry)) {
								emitterListEnabledTxtArea.setText(emitterListEnabledTxtArea.getText()
										.replace("(" + emit.x + " " + emit.y + ") " + entry.getmW() + " mW  "
												+ entry.getMHz() + " MHz  [" + entry.getAngles().x + "°-"
												+ entry.getAngles().y + "°]\n", ""));
							} else {
								emitterListDisabledTxtArea.setText(emitterListDisabledTxtArea.getText()
										.replace("(" + emit.x + " " + emit.y + ") " + entry.getmW() + " mW  "
												+ entry.getMHz() + " MHz  [" + entry.getAngles().x + "°-"
												+ entry.getAngles().y + "°]\n", ""));
							}
							Rectangle rect = new Rectangle(xEmit-DIM_SQUARE/2,yEmit-DIM_SQUARE/2,DIM_SQUARE,DIM_SQUARE);
							floorLogic.getCurrentFloor().removeGraphicalEmitter(rect);
							floorLogic.getCurrentFloor().removeEmitter(entry);
							drawPanel = floorLogic.getCurrentFloor().getCanvas();
							drawPanel.repaint();
							return;
						}
					}
				}
				emitError.setText("Impossibile cancellare un valore assente");
				xEmitTxt.setBorder(new LineBorder(Color.red, 1));
				yEmitTxt.setBorder(new LineBorder(Color.red, 1));
			}
		});

		enableDisableEmitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				powEmitTxt.setBorder(new JTextField().getBorder());// Reset dei border
				freqEmitterTxt.setBorder(new JTextField().getBorder());
				angStartEmitTxt.setBorder(new JTextField().getBorder());
				angEndEmitTxt.setBorder(new JTextField().getBorder());
				xEmitTxt.setBorder(new JTextField().getBorder());
				yEmitTxt.setBorder(new JTextField().getBorder());
				emitterListEnabledTxtArea.setBorder(new JTextField().getBorder());
				emitterListDisabledTxtArea.setBorder(new JTextField().getBorder());
				emitError.setText("");
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
				Point emit = new Point(xEmit, yEmit);
				if (validatePosition(emit)) {
					for (Emitters entry : floorLogic.getCurrentFloor().getEmitters().keySet()) {
						if (entry.getPosition().equals(emit)) {
							Rectangle rect = new Rectangle(xEmit-DIM_SQUARE/2,yEmit-DIM_SQUARE/2,DIM_SQUARE,DIM_SQUARE);
							if (floorLogic.getCurrentFloor().enableDisableEmitter(entry)) {
								floorLogic.getCurrentFloor().setNewGraphicalEmitter(rect, entry.getAngles());
								emitterListDisabledTxtArea.setText(emitterListDisabledTxtArea.getText()
										.replace("(" + emit.x + " " + emit.y + ") " + entry.getmW() + " mW  "
												+ entry.getMHz() + " MHz  [" + entry.getAngles().x + "°-"
												+ entry.getAngles().y + "°]\n", ""));
								emitterListEnabledTxtArea.setText(emitterListEnabledTxtArea.getText() + "(" + emit.x + " " + emit.y + ") "
										+ entry.getmW() + " mW  " + entry.getMHz() + " MHz  [" + entry.getAngles().x
										+ "°-" + entry.getAngles().y + "°]\n");
							} else {
								floorLogic.getCurrentFloor().removeGraphicalEmitter(rect);
								emitterListEnabledTxtArea.setText(emitterListEnabledTxtArea.getText()
										.replace("(" + emit.x + " " + emit.y + ") " + entry.getmW() + " mW  "
												+ entry.getMHz() + " MHz  [" + entry.getAngles().x + "°-"
												+ entry.getAngles().y + "°]\n", ""));
								emitterListDisabledTxtArea.setText(emitterListDisabledTxtArea.getText() + "(" + emit.x + " " + emit.y + ") "
										+ entry.getmW() + " mW  " + entry.getMHz() + " MHz  [" + entry.getAngles().x
										+ "°-" + entry.getAngles().y + "°]\n");
							}
							drawPanel = floorLogic.getCurrentFloor().getCanvas();
							drawPanel.repaint();
							return;
						}
					}
				}
			}
		});

		rLow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				impactSel = LOW;
			}
		});

		rAverage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				impactSel = AVERAGE;
			}
		});

		rHigh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				impactSel = HIGH;
			}
		});

		createWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wallStartxTxt.setBorder(new JTextField().getBorder());// Reset dei border
				wallEndxTxt.setBorder(new JTextField().getBorder());
				wallStartyTxt.setBorder(new JTextField().getBorder());
				wallEndyTxt.setBorder(new JTextField().getBorder());
				wallListEnabledTxtArea.setBorder(new JTextField().getBorder());
				wallListDisabledTxtArea.setBorder(new JTextField().getBorder());
				wallError.setText("");
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
				if (validateWallPosition(wall)) {
					for (Walls entry : floorLogic.getCurrentFloor().getWalls().keySet()) { // controllo compenetrazione
						if (wall.intersectsLine(entry.getPosition())	//si intersecano
								&& (!(wall.getX1() == wall.getX2() ^ entry.getPosition().getX1() == entry.getPosition().getX2()))	//sono collineari
								&& (Math.max(Math.max(wall.getP1().distance(entry.getPosition().getP1()),wall.getP1().distance(entry.getPosition().getP2())),Math.max(entry.getPosition().getP1().distance(wall.getP2()),entry.getPosition().getP2().distance(wall.getP2())))
										< (wall.getP1().distance(wall.getP2())+entry.getPosition().getP1().distance(entry.getPosition().getP2())))) {	//contengono ciascuno più dell'estremo dell'altro
							wallError.setText("I muri non possono compenetrarsi");
							wallStartxTxt.setBorder(new LineBorder(Color.red, 1));// Set border to red
							wallEndxTxt.setBorder(new LineBorder(Color.red, 1));
							wallStartyTxt.setBorder(new LineBorder(Color.red, 1));
							wallEndyTxt.setBorder(new LineBorder(Color.red, 1));
							if(floorLogic.getCurrentFloor().getWalls().get(entry)) {
								wallListEnabledTxtArea.setBorder(new LineBorder(Color.red, 1));
							} else {
								wallListDisabledTxtArea.setBorder(new LineBorder(Color.red, 1));
							}
							return;
						}
					}
					Walls walls = new Walls(wall, impactSel);
					floorLogic.getCurrentFloor().setNewWall(walls);
					int muratura = getWallWidth(impactSel);
					Rectangle rect = new Rectangle(Math.min(xWallS, xWallE),Math.min(yWallS, yWallE),
							Math.max(Math.abs(xWallE-xWallS), muratura),Math.max(Math.abs(yWallE-yWallS), muratura));
					floorLogic.getCurrentFloor().setNewGraphicalWall(rect);
					drawPanel = floorLogic.getCurrentFloor().getCanvas();
					drawPanel.repaint();
					wallListEnabledTxtArea.setText(wallListEnabledTxtArea.getText() + "(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE + ") " + impactSel + "    ");
				}
			}
		});

		deleteWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int xWallS, yWallS, xWallE, yWallE;
				wallStartxTxt.setBorder(new JTextField().getBorder());// Reset dei border
				wallEndxTxt.setBorder(new JTextField().getBorder());
				wallStartyTxt.setBorder(new JTextField().getBorder());
				wallEndyTxt.setBorder(new JTextField().getBorder());
				wallListEnabledTxtArea.setBorder(new JTextField().getBorder());
				wallListDisabledTxtArea.setBorder(new JTextField().getBorder());
				wallError.setText("");

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
				for (Walls entry : floorLogic.getCurrentFloor().getWalls().keySet()) {
					if (entry.getPosition().getP1().equals(wall.getP1())
							&& entry.getPosition().getP2().equals(wall.getP2())) {
						if (floorLogic.getCurrentFloor().getWalls().get(entry)) {
							wallListEnabledTxtArea.setText(wallListEnabledTxtArea.getText().replace("(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE + ") " + entry.getImpact() + "    ", ""));
						} else {
							wallListDisabledTxtArea.setText(wallListDisabledTxtArea.getText().replace("(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE + ") " + entry.getImpact() + "    ", ""));
						}
						int muratura = getWallWidth(impactSel);
						Rectangle rect = new Rectangle(Math.min(xWallS, xWallE),Math.min(yWallS, yWallE),
								Math.max(Math.abs(xWallE-xWallS), muratura),Math.max(Math.abs(yWallE-yWallS), muratura));
						floorLogic.getCurrentFloor().removeGraphicalWall(rect);
						floorLogic.getCurrentFloor().removeWall(entry);
						drawPanel = floorLogic.getCurrentFloor().getCanvas();
						drawPanel.repaint();
						return;
					}
				}
				wallError.setText("Impossibile cancellare un valore assente");
				wallStartxTxt.setBorder(new LineBorder(Color.red, 1));// Set border to red
				wallEndxTxt.setBorder(new LineBorder(Color.red, 1));
				wallStartyTxt.setBorder(new LineBorder(Color.red, 1));
				wallEndyTxt.setBorder(new LineBorder(Color.red, 1));
			}
		});

		enableWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wallStartxTxt.setBorder(new JTextField().getBorder());// Reset dei border
				wallEndxTxt.setBorder(new JTextField().getBorder());
				wallStartyTxt.setBorder(new JTextField().getBorder());
				wallEndyTxt.setBorder(new JTextField().getBorder());
				wallListEnabledTxtArea.setBorder(new JTextField().getBorder());
				wallListDisabledTxtArea.setBorder(new JTextField().getBorder());
				wallError.setText("");
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
				if (validateWallPosition(wall)) {
					for (Walls entry : floorLogic.getCurrentFloor().getWalls().keySet()) {
						if (entry.getPosition().getP1().equals(wall.getP1())
								&& entry.getPosition().getP2().equals(wall.getP2())) {
							int muratura = getWallWidth(impactSel);
							Rectangle rect = new Rectangle(Math.min(xWallS, xWallE),Math.min(yWallS, yWallE),
									Math.max(Math.abs(xWallE-xWallS), muratura),Math.max(Math.abs(yWallE-yWallS), muratura));
							if (floorLogic.getCurrentFloor().enableDisableWall(entry)) {
								floorLogic.getCurrentFloor().setNewGraphicalWall(rect);
								wallListDisabledTxtArea.setText(wallListDisabledTxtArea.getText().replace("(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE + ") " + entry.getImpact() + "    ", ""));
								wallListEnabledTxtArea.setText(wallListEnabledTxtArea.getText() + "(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE + ") " + entry.getImpact() + "    ");
							} else {
								floorLogic.getCurrentFloor().removeGraphicalWall(rect);
								wallListEnabledTxtArea.setText(wallListEnabledTxtArea.getText().replace("(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE + ") " + entry.getImpact() + "    ", ""));
								wallListDisabledTxtArea.setText(wallListDisabledTxtArea.getText() + "(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE + ") " + entry.getImpact() + "    ");
							}
							drawPanel = floorLogic.getCurrentFloor().getCanvas();
							drawPanel.repaint();
							return;
						}
					}
				}
			}
		});

		createUtilizer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				xUtilTxt.setBorder(new JTextField().getBorder());// Reset dei border
				yUtilTxt.setBorder(new JTextField().getBorder());
				utilizerListEnabledTxtArea.setBorder(new JTextField().getBorder());
				utilizerListDisabledTxtArea.setBorder(new JTextField().getBorder());
				utilError.setText("");
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
				Point util = new Point(xUtil, yUtil);
				if (validatePosition(util)) {
					for (Utilizers entry : floorLogic.getCurrentFloor().getUtilizers().keySet()) {
						if (entry.getPosition().equals(util)) {
							utilError.setText("Valore già presente");
							xUtilTxt.setBorder(new LineBorder(Color.red, 1));
							yUtilTxt.setBorder(new LineBorder(Color.red, 1));
							if(floorLogic.getCurrentFloor().getUtilizers().get(entry)) {
								utilizerListEnabledTxtArea.setBorder(new LineBorder(Color.red, 1));
							} else {
								utilizerListDisabledTxtArea.setBorder(new LineBorder(Color.red, 1));
							}
							return;
						}
					}
					Utilizers utilizzatore = new Utilizers(util);
					floorLogic.getCurrentFloor().setNewUtilizer(utilizzatore);
					utilizerListEnabledTxtArea.setText(utilizerListEnabledTxtArea.getText() + "(" + util.x + " " + util.y + ")    ");
					boolean changed = false;
					Dimension area = floorLogic.getCurrentFloor().getCanvas().getArea();
					int x = Math.max(xUtil - DIM_SQUARE / 2,0);
					int y = Math.max(yUtil - DIM_SQUARE / 2,0);
	
					Rectangle rect = new Rectangle(x, y, DIM_SQUARE, DIM_SQUARE);
					floorLogic.getCurrentFloor().setNewGraphicalUtilizer(rect);
					
					int this_width = (x + DIM_SQUARE + 2);
					if (this_width > area.width) {
						floorLogic.getCurrentFloor().getCanvas().getArea().width = this_width;
						changed = true;
					}
	
					int this_height = (y + DIM_SQUARE + 2);
					if (this_height > area.height) {
						floorLogic.getCurrentFloor().getCanvas().getArea().height = this_height;
						changed = true;
					}
					if (changed) {
						floorLogic.getCurrentFloor().getCanvas().setPreferredSize(area);
						floorLogic.getCurrentFloor().getCanvas().revalidate();
					}
					drawPanel = floorLogic.getCurrentFloor().getCanvas();
					drawPanel.repaint();
				}
			}
		});

		deleteUtilizer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				xUtilTxt.setBorder(new JTextField().getBorder());// Reset dei border
				yUtilTxt.setBorder(new JTextField().getBorder());
				utilizerListEnabledTxtArea.setBorder(new JTextField().getBorder());
				utilizerListDisabledTxtArea.setBorder(new JTextField().getBorder());
				utilError.setText("");
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
				Point util = new Point(xUtil, yUtil);
				if (validatePosition(util)) {
					for (Utilizers entry : floorLogic.getCurrentFloor().getUtilizers().keySet()) {
						if (entry.getPosition().equals(util)) {
							if (floorLogic.getCurrentFloor().getUtilizers().get(entry)) {
								utilizerListEnabledTxtArea.setText(
										utilizerListEnabledTxtArea.getText().replace("(" + util.x + " " + util.y + ")    ", ""));
							} else {
								utilizerListDisabledTxtArea.setText(
										utilizerListDisabledTxtArea.getText().replace("(" + util.x + " " + util.y + ")    ", ""));
							}
							floorLogic.getCurrentFloor().removeUtilizer(entry);
							int x = xUtil-xUtil%DIM_SQUARE;
							int y = yUtil-yUtil%DIM_SQUARE;
			
							Rectangle rec = new Rectangle(x, y, DIM_SQUARE, DIM_SQUARE);
							floorLogic.getCurrentFloor().removeGraphicalUtilizer(rec);
							drawPanel = floorLogic.getCurrentFloor().getCanvas();
							drawPanel.repaint();
							return;
						}
					}
					utilError.setText("Impossibile cancellare un valore assente");
					xUtilTxt.setBorder(new JTextField().getBorder());// Reset dei border
					yUtilTxt.setBorder(new JTextField().getBorder());
				}
			}
		});		

		enableUtilizer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				xUtilTxt.setBorder(new JTextField().getBorder());// Reset dei border
				yUtilTxt.setBorder(new JTextField().getBorder());
				utilizerListEnabledTxtArea.setBorder(new JTextField().getBorder());
				utilizerListDisabledTxtArea.setBorder(new JTextField().getBorder());
				utilError.setText("");
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
				Point util = new Point(xUtil, yUtil);
				if (validatePosition(util)) {
					for (Utilizers entry : floorLogic.getCurrentFloor().getUtilizers().keySet()) {
						if (entry.getPosition().equals(util)) {
							int x = Math.max(xUtil - DIM_SQUARE / 2,0);
							int y = Math.max(yUtil - DIM_SQUARE / 2,0);
							Rectangle rect = new Rectangle(x, y, DIM_SQUARE, DIM_SQUARE);
							if (floorLogic.getCurrentFloor().enableDisableUtilizer(entry)) {
								floorLogic.getCurrentFloor().setNewGraphicalUtilizer(rect);
								utilizerListEnabledTxtArea.setText(utilizerListEnabledTxtArea.getText() + "(" + util.x + " " + util.y + ")    ");
								utilizerListDisabledTxtArea.setText(
										utilizerListDisabledTxtArea.getText().replace("(" + util.x + " " + util.y + ")    ", ""));
							} else {
								floorLogic.getCurrentFloor().removeGraphicalUtilizer(rect);
								utilizerListDisabledTxtArea.setText(utilizerListDisabledTxtArea.getText() + "(" + util.x + " " + util.y + ")    ");
								utilizerListEnabledTxtArea.setText(
										utilizerListEnabledTxtArea.getText().replace("(" + util.x + " " + util.y + ")    ", ""));
							}
							drawPanel = floorLogic.getCurrentFloor().getCanvas();
							drawPanel.repaint();
							return;
						}
					}
				}
			}
		});

		generateResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer[][] values = new Integer[HEIGHT/DIM_SQUARE][WIDTH/DIM_SQUARE];
				double int_tot, int_em, att_air, attenuation, angle, radStart, radEnd;
				int end_result, colour;
				int pCheck = 0;
				boolean eCheck = false;
				// coloro la mappa come prima cosa cosicché muri ed emittenti sovrascrivano i colori e non viceversa
				for(pCheck=-floorLogic.getLowerFloorsSize(); pCheck<=floorLogic.getUpperFloorsSize(); pCheck++) {
					if(floorLogic.getFloor(pCheck).getEmitters().containsValue(true)) {
						eCheck = true;
						for (int i = DIM_SQUARE / 2; i < HEIGHT; i += DIM_SQUARE) {
							for (int j = DIM_SQUARE / 2; j < WIDTH; j += DIM_SQUARE) {
								int_tot = 0;
								for (Map.Entry<Emitters, Boolean> entryE : floorLogic.getFloor(pCheck).getEmitters().entrySet()) {
									if (!entryE.getValue()) {
										continue;
									}
									int_em = entryE.getKey().getmW();
									if ((entryE.getKey().getAngles().y - entryE.getKey().getAngles().x) < 360) {
										angle = (double) Math.atan2(i - entryE.getKey().getPosition().y,
												j - entryE.getKey().getPosition().x);
										if (angle < 0) {
											angle += 2 * Math.PI;
										}
										radStart = Math.toRadians(entryE.getKey().getAngles().x);
										radEnd = Math.toRadians(entryE.getKey().getAngles().y);
										if (radEnd < radStart) {
											radEnd += 2 * Math.PI;
										}
										if (!(radStart <= angle && radEnd >= angle)) {
											continue;
										}
										int_em = int_em * (2 * Math.PI) / (radEnd - radStart); // le antenne direzionali hanno guadagno sul fronte
									} // 1 elemento=1 cm; divido per 100 per convertire in metri
	
									attenuation = -27.55 + 20 * (Math.log10(Math.sqrt(Math
											.pow(((double) i - (double) entryE.getKey().getPosition().y) / 100, 2)
											+ Math.pow(((double) j - (double) entryE.getKey().getPosition().x) / 100, 2)))
											+ Math.log10((double) entryE.getKey().getMHz()));
									for (Map.Entry<Walls, Boolean> entryM : floorLogic.getFloor(pCheck).getWalls().entrySet()) {
										if (!entryM.getValue()) {
											continue;
										}
	
										if (Line2D.linesIntersect(entryM.getKey().getPosition().getX1(), entryM.getKey().getPosition().getY1(),
												entryM.getKey().getPosition().getX2(), entryM.getKey().getPosition().getY2(),
												entryE.getKey().getPosition().x, entryE.getKey().getPosition().y, j, i)) {
											switch (entryM.getKey().getImpact()) {
												case LOW:
													int_em /= 2;
													break;
												case AVERAGE:
													int_em /= 10;
													break;
												case HIGH:
													int_em /= 100;
													break;
												default:
													continue;
											}
										}
									}
									int_em = 10 * Math.log10(int_em) - attenuation - MIN_INT;
									if (int_em > 0) {
										int_tot += int_em;
									}
								}
								if(int_tot < 0) {
									int_tot = 0;
								}
								floorLogic.getFloor(pCheck).setIntensity(i/DIM_SQUARE-1/2,j/DIM_SQUARE-1/2,int_tot);
							}
						}
					}
				}
				if(eCheck) {
					for (int i = 0; i < HEIGHT/DIM_SQUARE; i++) {
						for (int j = 0; j < WIDTH/DIM_SQUARE; j++) {
							int_tot = floorLogic.getCurrentFloor().getIntensity(i,j);
							int_em = 0;
							for(int p=-floorLogic.getLowerFloorsSize(); p<floorLogic.getCurrentFloorNumber(); p++) {
								att_air = Math.max(floorLogic.getFloor(p).getIntensity(i,j)-20*(Math.abs(Math.abs(floorLogic.getCurrentFloorNumber()+floorLogic.getLowerFloorsSize())-Math.abs(p+floorLogic.getLowerFloorsSize()))*3+2.343),MIN_INT);
								int_em = (int_em + att_air-MIN_INT)/100;
								System.out.println(floorLogic.getFloor(p).getIntensity(i,j));
							}
							int_tot += int_em;
							int_em = 0;
							for(int p=floorLogic.getUpperFloorsSize(); p>floorLogic.getCurrentFloorNumber(); p--) {
								att_air = Math.max(floorLogic.getFloor(p).getIntensity(i,j)-20*(Math.abs(Math.abs(floorLogic.getCurrentFloorNumber()+floorLogic.getLowerFloorsSize())-Math.abs(p+floorLogic.getLowerFloorsSize()))*3+2.343),MIN_INT);
								int_em = (int_em + att_air-MIN_INT)/100;
								System.out.println(floorLogic.getFloor(p).getIntensity(i,j));
							}
							int_tot += int_em;
							if(int_tot > 0) {
								values[i][j] = (int) int_tot;
								end_result = (int) Math.floor(int_tot);
							    colour = (int) Math.min(Math.floor(end_result/15),tones.length-1);
							    resultPanel.setNewRect(i,j,tones[colour],end_result);
							} else {
								values[i][j] = 0;
							}
						}
					}
					if(currentFloorCB.isSelected()) {
						resultPanel.setValori(values);
					} else {
						resultPanel.setValori(null);
					}
					resultPanel.copyCanvas(drawPanel);
					resultPanel.getGraphics().clearRect(0, 0, WIDTH, HEIGHT);
					resultPanel.paintComponent(resultPanel.getGraphics());
				}
			}
		});
		
		previousFloorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				duplicateMapBtn.setEnabled(true);
				duplicateMapTxt.setEnabled(true);
				
				floorLogic.getCurrentFloor().getCanvas().redraw(drawPanel.getGraphics());
				drawPanel.setPreferredSize(drawPanel.getArea());
				drawPanel.revalidate();
				drawPanel.repaint();

				floorLogic.moveToLowerFloor();
				
				drawPanel = floorLogic.getCurrentFloor().getCanvas();
				canvasContainerPanel.add(drawPanel);
				drawPanel.setPreferredSize(drawPanel.getArea());
				drawPanel.revalidate();
				drawPanel.repaint();
				int muratura,x,y;
				floorLogic.getCurrentFloor().getCanvas().redraw(drawPanel.getGraphics());
				floorLogic.getCurrentFloor().getCanvas().redraw(resultPanel.getGraphics());

				emitterListEnabledTxtArea.setText("");
				emitterListEnabledTxtArea.setText("");
				wallListEnabledTxtArea.setText("");
				wallListDisabledTxtArea.setText("");
				utilizerListEnabledTxtArea.setText("");
				utilizerListDisabledTxtArea.setText("");
				powEmitTxt.setBorder(new JTextField().getBorder());
				freqEmitterTxt.setBorder(new JTextField().getBorder());
				angStartEmitTxt.setBorder(new JTextField().getBorder());
				angEndEmitTxt.setBorder(new JTextField().getBorder());
				xEmitTxt.setBorder(new JTextField().getBorder());
				yEmitTxt.setBorder(new JTextField().getBorder());
				wallStartxTxt.setBorder(new JTextField().getBorder());
				wallEndxTxt.setBorder(new JTextField().getBorder());
				wallStartyTxt.setBorder(new JTextField().getBorder());
				wallEndyTxt.setBorder(new JTextField().getBorder());
				xUtilTxt.setBorder(new JTextField().getBorder());
				yUtilTxt.setBorder(new JTextField().getBorder());
				emitterListEnabledTxtArea.setBorder(new JTextField().getBorder());
				emitterListEnabledTxtArea.setBorder(new JTextField().getBorder());
				wallListEnabledTxtArea.setBorder(new JTextField().getBorder());
				wallListDisabledTxtArea.setBorder(new JTextField().getBorder());
				utilizerListEnabledTxtArea.setBorder(new JTextField().getBorder());
				utilizerListDisabledTxtArea.setBorder(new JTextField().getBorder());

				for (Map.Entry<Emitters, Boolean> entry : floorLogic.getCurrentFloor().getEmitters().entrySet()) {
					if (entry.getValue()) {
						emitterListEnabledTxtArea.setText(emitterListEnabledTxtArea.getText() + "("
								+ entry.getKey().getPosition().x + " " + entry.getKey().getPosition().y + ") "
								+ entry.getKey().getmW() + " mW  " + entry.getKey().getMHz() + " MHz  ["
								+ entry.getKey().getAngles().x + "°-" + entry.getKey().getAngles().y + "°]    ");
						x = Math.max(entry.getKey().getPosition().x - DIM_SQUARE / 2,0);
						y = Math.max(entry.getKey().getPosition().y - DIM_SQUARE / 2,0);
						drawPanel.setNewEmitter(new Rectangle(x,y,DIM_SQUARE,DIM_SQUARE), new Point(entry.getKey().getAngles().x,entry.getKey().getAngles().y));
						resultPanel.setNewEmitter(new Rectangle(x,y,DIM_SQUARE,DIM_SQUARE), new Point(entry.getKey().getAngles().x,entry.getKey().getAngles().y));
					} else {
						emitterListDisabledTxtArea.setText(emitterListDisabledTxtArea.getText() + "("
								+ entry.getKey().getPosition().x + " " + entry.getKey().getPosition().y + ") "
								+ entry.getKey().getmW() + " mW  " + entry.getKey().getMHz() + " MHz  ["
								+ entry.getKey().getAngles().x + "°-" + entry.getKey().getAngles().y + "°]    ");
					}
				}
				for (Map.Entry<Walls, Boolean> entry : floorLogic.getCurrentFloor().getWalls().entrySet()) {
					if (entry.getValue()) {
						wallListEnabledTxtArea.setText(wallListEnabledTxtArea.getText() + "("
								+ entry.getKey().getPosition().getX1() + " " + entry.getKey().getPosition().getY1()
								+ ")-(" + entry.getKey().getPosition().getX2() + " "
								+ entry.getKey().getPosition().getY2() + ") " + entry.getKey().getImpact() + "    ");
						muratura = getWallWidth(entry.getKey().getImpact());
						drawPanel.setNewWall(new Rectangle((int) Math.min(entry.getKey().getPosition().getX1(),entry.getKey().getPosition().getX2()), (int) Math.min(entry.getKey().getPosition().getY1(),entry.getKey().getPosition().getY2()),
								(int) Math.max(muratura, Math.abs(entry.getKey().getPosition().getX1()-entry.getKey().getPosition().getX2())), (int) Math.max(muratura, Math.abs(entry.getKey().getPosition().getY1()-entry.getKey().getPosition().getY2()))));
						resultPanel.setNewWall(new Rectangle((int) Math.min(entry.getKey().getPosition().getX1(),entry.getKey().getPosition().getX2()), (int) Math.min(entry.getKey().getPosition().getY1(),entry.getKey().getPosition().getY2()),
								(int) Math.max(muratura, Math.abs(entry.getKey().getPosition().getX1()-entry.getKey().getPosition().getX2())), (int) Math.max(muratura, Math.abs(entry.getKey().getPosition().getY1()-entry.getKey().getPosition().getY2()))));
					} else {
						wallListDisabledTxtArea.setText(wallListDisabledTxtArea.getText() + "("
								+ entry.getKey().getPosition().getX1() + " " + entry.getKey().getPosition().getY1()
								+ ")-(" + entry.getKey().getPosition().getX2() + " "
								+ entry.getKey().getPosition().getY2() + ") " + entry.getKey().getImpact() + "    ");
					}
				}
				for (Map.Entry<Utilizers, Boolean> entry : floorLogic.getCurrentFloor().getUtilizers().entrySet()) {
					if (entry.getValue()) {
						utilizerListEnabledTxtArea.setText(utilizerListEnabledTxtArea.getText() + "("
								+ entry.getKey().getPosition().x + " " + entry.getKey().getPosition().y + ")    ");
						x = entry.getKey().getPosition().x - entry.getKey().getPosition().x%DIM_SQUARE;
						y = entry.getKey().getPosition().y - entry.getKey().getPosition().y%DIM_SQUARE;
						drawPanel.setNewUtilizer(new Rectangle(x,y,DIM_SQUARE,DIM_SQUARE));
						resultPanel.setNewUtilizer(new Rectangle(x,y,DIM_SQUARE,DIM_SQUARE));
					} else {
						utilizerListDisabledTxtArea.setText(utilizerListDisabledTxtArea.getText() + "("
								+ entry.getKey().getPosition().x + " " + entry.getKey().getPosition().y + ")    ");
					}
				}
				
				int story = floorLogic.getCurrentFloorNumber();
				currentFloorLbl.setText("Piano: " + story);
				nextFloorBtn.setText("Vai a piano " + (story + 1));				
				if (story == floorLogic.getLowerFloorsSize()) {
					previousFloorBtn.setText("Crea piano " + (story - 1));
				} else {
					previousFloorBtn.setText("Vai a piano " + (story - 1));
				}
			}
		});

		nextFloorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				duplicateMapBtn.setEnabled(true);
				duplicateMapTxt.setEnabled(true);

				floorLogic.getCurrentFloor().getCanvas().redraw(drawPanel.getGraphics());
				drawPanel.setPreferredSize(drawPanel.getArea());
				drawPanel.revalidate();
				drawPanel.repaint();
				
				floorLogic.moveToHigherFloor();
				
				drawPanel = floorLogic.getCurrentFloor().getCanvas();	
				canvasContainerPanel.add(drawPanel);
				drawPanel.setPreferredSize(drawPanel.getArea());
				drawPanel.revalidate();
				drawPanel.repaint();
				int muratura,x,y;
				floorLogic.getCurrentFloor().getCanvas().redraw(drawPanel.getGraphics());
				floorLogic.getCurrentFloor().getCanvas().redraw(resultPanel.getGraphics());

				emitterListEnabledTxtArea.setText("");
				emitterListEnabledTxtArea.setText("");
				wallListEnabledTxtArea.setText("");
				wallListDisabledTxtArea.setText("");
				utilizerListEnabledTxtArea.setText("");
				utilizerListDisabledTxtArea.setText("");
				powEmitTxt.setBorder(new JTextField().getBorder());
				freqEmitterTxt.setBorder(new JTextField().getBorder());
				angStartEmitTxt.setBorder(new JTextField().getBorder());
				angEndEmitTxt.setBorder(new JTextField().getBorder());
				xEmitTxt.setBorder(new JTextField().getBorder());
				yEmitTxt.setBorder(new JTextField().getBorder());
				wallStartxTxt.setBorder(new JTextField().getBorder());
				wallEndxTxt.setBorder(new JTextField().getBorder());
				wallStartyTxt.setBorder(new JTextField().getBorder());
				wallEndyTxt.setBorder(new JTextField().getBorder());
				xUtilTxt.setBorder(new JTextField().getBorder());
				yUtilTxt.setBorder(new JTextField().getBorder());
				emitterListEnabledTxtArea.setBorder(new JTextField().getBorder());
				emitterListEnabledTxtArea.setBorder(new JTextField().getBorder());
				wallListEnabledTxtArea.setBorder(new JTextField().getBorder());
				wallListDisabledTxtArea.setBorder(new JTextField().getBorder());
				utilizerListEnabledTxtArea.setBorder(new JTextField().getBorder());
				utilizerListDisabledTxtArea.setBorder(new JTextField().getBorder());

				for (Map.Entry<Emitters, Boolean> entry : floorLogic.getCurrentFloor().getEmitters().entrySet()) {
					if (entry.getValue()) {
						emitterListEnabledTxtArea.setText(emitterListEnabledTxtArea.getText() + "("
								+ entry.getKey().getPosition().x + " " + entry.getKey().getPosition().y + ") "
								+ entry.getKey().getmW() + " mW  " + entry.getKey().getMHz() + " MHz  ["
								+ entry.getKey().getAngles().x + "°-" + entry.getKey().getAngles().y + "°]    ");
						x = Math.max(entry.getKey().getPosition().x - DIM_SQUARE / 2,0);
						y = Math.max(entry.getKey().getPosition().y - DIM_SQUARE / 2,0);
						drawPanel.setNewEmitter(new Rectangle(x,y,DIM_SQUARE,DIM_SQUARE), new Point(entry.getKey().getAngles().x,entry.getKey().getAngles().y));
						resultPanel.setNewEmitter(new Rectangle(x,y,DIM_SQUARE,DIM_SQUARE), new Point(entry.getKey().getAngles().x,entry.getKey().getAngles().y));
					} else {
						emitterListDisabledTxtArea.setText(emitterListDisabledTxtArea.getText() + "("
								+ entry.getKey().getPosition().x + " " + entry.getKey().getPosition().y + ") "
								+ entry.getKey().getmW() + " mW  " + entry.getKey().getMHz() + " MHz  ["
								+ entry.getKey().getAngles().x + "°-" + entry.getKey().getAngles().y + "°]    ");
					}
				}
				for (Map.Entry<Walls, Boolean> entry : floorLogic.getCurrentFloor().getWalls().entrySet()) {
					if (entry.getValue()) {
						wallListEnabledTxtArea.setText(wallListEnabledTxtArea.getText() + "("
								+ entry.getKey().getPosition().getX1() + " " + entry.getKey().getPosition().getY1()
								+ ")-(" + entry.getKey().getPosition().getX2() + " "
								+ entry.getKey().getPosition().getY2() + ") " + entry.getKey().getImpact() + "    ");
						muratura = getWallWidth(entry.getKey().getImpact());
						drawPanel.setNewWall(new Rectangle((int) Math.min(entry.getKey().getPosition().getX1(),entry.getKey().getPosition().getX2()), (int) Math.min(entry.getKey().getPosition().getY1(),entry.getKey().getPosition().getY2()),
								(int) Math.max(muratura, Math.abs(entry.getKey().getPosition().getX1()-entry.getKey().getPosition().getX2())), (int) Math.max(muratura, Math.abs(entry.getKey().getPosition().getY1()-entry.getKey().getPosition().getY2()))));
						resultPanel.setNewWall(new Rectangle((int) Math.min(entry.getKey().getPosition().getX1(),entry.getKey().getPosition().getX2()), (int) Math.min(entry.getKey().getPosition().getY1(),entry.getKey().getPosition().getY2()),
								(int) Math.max(muratura, Math.abs(entry.getKey().getPosition().getX1()-entry.getKey().getPosition().getX2())), (int) Math.max(muratura, Math.abs(entry.getKey().getPosition().getY1()-entry.getKey().getPosition().getY2()))));
					} else {
						wallListDisabledTxtArea.setText(wallListDisabledTxtArea.getText() + "("
								+ entry.getKey().getPosition().getX1() + " " + entry.getKey().getPosition().getY1()
								+ ")-(" + entry.getKey().getPosition().getX2() + " "
								+ entry.getKey().getPosition().getY2() + ") " + entry.getKey().getImpact() + "    ");
					}
				}
				for (Map.Entry<Utilizers, Boolean> entry : floorLogic.getCurrentFloor().getUtilizers().entrySet()) {
					if (entry.getValue()) {
						utilizerListEnabledTxtArea.setText(utilizerListEnabledTxtArea.getText() + "("
								+ entry.getKey().getPosition().x + " " + entry.getKey().getPosition().y + ")    ");
						x = entry.getKey().getPosition().x - entry.getKey().getPosition().x%DIM_SQUARE;
						y = entry.getKey().getPosition().y - entry.getKey().getPosition().y%DIM_SQUARE;
						drawPanel.setNewUtilizer(new Rectangle(x,y,DIM_SQUARE,DIM_SQUARE));
						resultPanel.setNewUtilizer(new Rectangle(x,y,DIM_SQUARE,DIM_SQUARE));
					} else {
						utilizerListDisabledTxtArea.setText(utilizerListDisabledTxtArea.getText() + "("
								+ entry.getKey().getPosition().x + " " + entry.getKey().getPosition().y + ")    ");
					}
				}
				int story = floorLogic.getCurrentFloorNumber();
				currentFloorLbl.setText("Piano: " + story);
				previousFloorBtn.setText("Vai a piano " + (story - 1));			
				if (story == floorLogic.getUpperFloorsSize()) {
					nextFloorBtn.setText("Crea piano " + (story + 1));
				} else {
					nextFloorBtn.setText("Vai a piano " + (story + 1));
				}
			}
		});
		duplicateMapBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int nuovoPiano;
				try {
					nuovoPiano = Integer.parseInt(duplicateMapTxt.getText());
				} catch (NumberFormatException nfe) {
					System.out.println("Il piano deve essere un numero da " + -floorLogic.getLowerFloorsSize() + " a " + floorLogic.getUpperFloorsSize() + ", diverso dal piano corrente");
					return;
				}
				if(nuovoPiano>=-floorLogic.getLowerFloorsSize() && nuovoPiano<=floorLogic.getUpperFloorsSize() && nuovoPiano != floorLogic.getCurrentFloorNumber()) {
					int story = floorLogic.getCurrentFloorNumber();
					floorLogic.copyFloor(nuovoPiano);
					drawPanel = floorLogic.getCurrentFloor().getCanvas();
					drawPanel.repaint();
					resultPanel.repaint();
					if (story == -floorLogic.getLowerFloorsSize()) {
						previousFloorBtn.setText("Crea piano " + (story - 1));
					} else {
						previousFloorBtn.setText("Vai a piano " + (story - 1));
					}
					currentFloorLbl.setText("Piano: " + story);
					if (story == floorLogic.getUpperFloorsSize()) {
						nextFloorBtn.setText("Crea piano " + (story + 1));
					} else {
						nextFloorBtn.setText("Vai a piano " + (story + 1));
					}
				} else {
					System.out.println("Il piano deve essere un numero da " + -floorLogic.getLowerFloorsSize() + " a " + floorLogic.getUpperFloorsSize() + ", diverso dal piano corrente");
				}
			}
		});
	}
	
	public static void main(String[] args) {
		new GUI();
	}
}
