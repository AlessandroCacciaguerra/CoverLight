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
	private static final String LOW = "Low";
	private static final String AVERAGE = "Average";
	private static final String HIGH = "High";
	private static String impactSel = AVERAGE;
	private static final long serialVersionUID = 1L;
	private CanvasPanel drawPanel;
	private FloorLogicImpl floorLogic;
	private CoverLightLogicImpl logic;


	public GUI() {
		// Inizializzazione
		logic = new CoverLightLogicImpl(WIDTH, HEIGHT, DIM_SQUARE);
		floorLogic = logic.getFloorLogic();

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
		drawPanel = new CanvasPanel();// panel che contiene il disegno
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
		
		//Tutorial btn
		final JPanel tutorialBtnPanel = new JPanel(new BorderLayout());
		final JButton tutorialBtn = new JButton("?");
		tutorialBtnPanel.add(tutorialBtn, BorderLayout.LINE_END);
		buttonsPanelContainer.add(tutorialBtnPanel, BorderLayout.PAGE_START);
		
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
				final JDialog captionDialog = new JDialog();
				captionDialog.setTitle("Didascalia");
				// Set Frame
				captionDialog.add(logic.getCaption().getCaptionpanel(), BorderLayout.CENTER);
				captionDialog.add(logic.getCaption().getColorPanel(), BorderLayout.PAGE_END);
				captionDialog.setSize(1050, 600);
				captionDialog.setVisible(true);				
			}
		});		
		
		tutorialBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				final JDialog tutorialDialog = new JDialog();
				tutorialDialog.setTitle("Tutorial for use of CoverLight");
				// Set Frame
				tutorialDialog.add(logic.getTutorialGUI().getTutorialPanel(), BorderLayout.CENTER);
				tutorialDialog.add(logic.getTutorialGUI().getButtonsTutorialPanel(), BorderLayout.PAGE_END);
				tutorialDialog.setSize(1050, 600);
				tutorialDialog.setResizable(false);
				tutorialDialog.setVisible(true);
			}			
		});
	
		createEmitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				powEmitTxt.setBorder(new JTextField().getBorder());// Borders reset
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
					xEmitTxt.setText("1");
					xEmit = 1;
				}
				if (xEmit % DIM_SQUARE == 0) {
					emitError.setText("Le coordinate non possono avere valore divisibile per " + DIM_SQUARE);
					xEmitTxt.setBorder(new LineBorder(Color.red, 1));// Set border to red
					return;
				}
				try {
					yEmit = Integer.parseInt(yEmitTxt.getText());
				} catch (NumberFormatException nfe) {
					yEmitTxt.setText("1");
					yEmit = 1;
				}
				if (yEmit % DIM_SQUARE == 0) {
					emitError.setText("Le coordinate non possono avere valore divisibile per " + DIM_SQUARE);
					yEmitTxt.setBorder(new LineBorder(Color.red, 1));
					return;
				}
				try {
					pow = Float.parseFloat(powEmitTxt.getText());
					if (pow > 200 || pow <= 0) { // ETSI EN regulations
						emitError.setText("La potenza non deve superare i 200 milliWatt");
						powEmitTxt.setBorder(new LineBorder(Color.red, 1));
						return;
					}
				} catch (NumberFormatException nfe) {
					emitError.setText("La potenza non deve superare i 200 milliWatt");
					powEmitTxt.setBorder(new LineBorder(Color.red, 1));
					return;
				}
				try {
					freq = Float.parseFloat(freqEmitterTxt.getText());
					if ((freq < 5150 || freq > 5350)) { // ETSI EN regulations
						emitError.setText(
								"La frequenza del Wi-Fi all'interno di un edificio deve essere compresa fra 5150 e 5350 MegaHertz");
						freqEmitterTxt.setBorder(new LineBorder(Color.red, 1));
						return;
					}
				} catch (NumberFormatException nfe) {
					emitError.setText(
							"La frequenza del Wi-Fi all'interno di un edificio deve essere compresa fra 5150 e 5350 MegaHertz");
					freqEmitterTxt.setBorder(new LineBorder(Color.red, 1));
					return;
				}
				try {
					angS = Integer.parseInt(angStartEmitTxt.getText());
					if (angS < 0 || angS >= 360) {
						emitError.setText("Gli angoli devono avere valore compreso fra 0 e 359");
						angStartEmitTxt.setBorder(new LineBorder(Color.red, 1));
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
						angEndEmitTxt.setBorder(new LineBorder(Color.red, 1));
						return;
					}
				} catch (NumberFormatException nfe) {
					angEndEmitTxt.setText("360");
					angE = 0;
				}
				Point emit = new Point(xEmit, yEmit);
				if(CoverLightLogicImpl.validatePosition(emit)) {
					Emitters emitter = new Emitters(emit, pow, freq, angS, angE);
					Boolean flag = floorLogic.getCurrentFloor().setNewEmitter(emitter);
					if(flag == null) {
						emitterListEnabledTxtArea.setText(emitterListEnabledTxtArea.getText() + "(" + emit.x + " " + emit.y + ") " + pow + " mW  "
								+ freq + " MHz  [" + angS + "°-" + angE + "°]\n");
						boolean changed = false;
						Dimension area = floorLogic.getCurrentFloor().getCanvas().getArea();
						
						int this_width = (xEmit + DIM_SQUARE/2 + 2);
						if (this_width > area.width) {
							floorLogic.getCurrentFloor().getCanvas().getArea().width = this_width;
							changed = true;
						}
		
						int this_height = (yEmit + DIM_SQUARE/2 + 2);
						if (this_height > area.height) {
							floorLogic.getCurrentFloor().getCanvas().getArea().height = this_height;
							changed = true;
						}
						if (changed) {
							floorLogic.getCurrentFloor().getCanvas().setPreferredSize(area);
							floorLogic.getCurrentFloor().getCanvas().revalidate();
						}
						drawPanel.getLogic().copyCanvas(floorLogic.getCurrentFloor().getCanvas().getLogic());
						drawPanel.setArea(floorLogic.getCurrentFloor().getCanvas().getArea());	
						drawPanel.setPreferredSize(drawPanel.getArea());
						drawPanel.revalidate();
						drawPanel.repaint();
					} else {
						emitError.setText("Gli emittenti devono essere distanziati di almeno " + DIM_SQUARE / 2
								+ " centimetri");
						xEmitTxt.setBorder(new LineBorder(Color.red, 1));
						yEmitTxt.setBorder(new LineBorder(Color.red, 1));
						if(flag) {
							emitterListEnabledTxtArea.setBorder(new LineBorder(Color.red, 1));
						} else {
							emitterListDisabledTxtArea.setBorder(new LineBorder(Color.red, 1));
						}
					}
				} else {
					emitError.setText("La posizione deve essere una coppia di numeri da 0 a " + WIDTH + " e da 0 a " + HEIGHT + ", non divisibili per " + DIM_SQUARE);
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
				String text = floorLogic.getCurrentFloor().removeEmitter(emit);
				if(text.length()>0) {
					emitterListEnabledTxtArea.setText(emitterListEnabledTxtArea.getText().replace(text, ""));
					emitterListDisabledTxtArea.setText(emitterListDisabledTxtArea.getText().replace(text, ""));
					drawPanel.getLogic().copyCanvas(floorLogic.getCurrentFloor().getCanvas().getLogic());
					drawPanel.setArea(floorLogic.getCurrentFloor().getCanvas().getArea());	
					drawPanel.setPreferredSize(drawPanel.getArea());
					drawPanel.revalidate();
					drawPanel.repaint();
					return;
				}
				emitError.setText("Impossibile cancellare un valore assente");
				xEmitTxt.setBorder(new LineBorder(Color.red, 1));
				yEmitTxt.setBorder(new LineBorder(Color.red, 1));
			}
		});

		enableDisableEmitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				powEmitTxt.setBorder(new JTextField().getBorder());// Borders reset
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
				if (CoverLightLogicImpl.validatePosition(emit)) {
					for (Emitters entry : floorLogic.getCurrentFloor().getEmitters().keySet()) {
						if (entry.getPosition().equals(emit)) {
							if (floorLogic.getCurrentFloor().enableDisableEmitter(entry)) {
								emitterListDisabledTxtArea.setText(emitterListDisabledTxtArea.getText()
										.replace("(" + emit.x + " " + emit.y + ") " + entry.getmW() + " mW  "
												+ entry.getMHz() + " MHz  [" + entry.getAngles().x + "°-"
												+ entry.getAngles().y + "°]\n", ""));
								emitterListEnabledTxtArea.setText(emitterListEnabledTxtArea.getText() + "(" + emit.x + " " + emit.y + ") "
										+ entry.getmW() + " mW  " + entry.getMHz() + " MHz  [" + entry.getAngles().x
										+ "°-" + entry.getAngles().y + "°]\n");
							} else {
								emitterListEnabledTxtArea.setText(emitterListEnabledTxtArea.getText()
										.replace("(" + emit.x + " " + emit.y + ") " + entry.getmW() + " mW  "
												+ entry.getMHz() + " MHz  [" + entry.getAngles().x + "°-"
												+ entry.getAngles().y + "°]\n", ""));
								emitterListDisabledTxtArea.setText(emitterListDisabledTxtArea.getText() + "(" + emit.x + " " + emit.y + ") "
										+ entry.getmW() + " mW  " + entry.getMHz() + " MHz  [" + entry.getAngles().x
										+ "°-" + entry.getAngles().y + "°]\n");
							}
							drawPanel.getLogic().copyCanvas(floorLogic.getCurrentFloor().getCanvas().getLogic());
							drawPanel.setArea(floorLogic.getCurrentFloor().getCanvas().getArea());	
							drawPanel.setPreferredSize(drawPanel.getArea());
							drawPanel.revalidate();
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
				wallStartxTxt.setBorder(new JTextField().getBorder());
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
				if(CoverLightLogicImpl.validateWallPosition(wall)) {
					Boolean flag = floorLogic.getCurrentFloor().setNewWall(wall, impactSel);
					if(flag != null) {
						wallError.setText("I muri non possono compenetrarsi");
						if(flag) {
							wallListEnabledTxtArea.setBorder(new LineBorder(Color.red, 1));
						} else {
							wallListDisabledTxtArea.setBorder(new LineBorder(Color.red, 1));
						}
					} else {
						floorLogic.getCurrentFloor().setNewWall(wall, impactSel);
						drawPanel.getLogic().copyCanvas(floorLogic.getCurrentFloor().getCanvas().getLogic());
						drawPanel.setArea(floorLogic.getCurrentFloor().getCanvas().getArea());	
						drawPanel.setPreferredSize(drawPanel.getArea());
						drawPanel.revalidate();
						drawPanel.repaint();
						wallListEnabledTxtArea.setText(wallListEnabledTxtArea.getText() + "(" + xWallS + " " + yWallS + ")-(" + xWallE + " " + yWallE + ") " + impactSel + "    ");
						return;
					}
				} else {
					wallError.setText("Gli estremi dei muri devono essere due punti con coordinate multiple di " + DIM_SQUARE + ", di cui una e solo una in comune");
				}
				wallStartxTxt.setBorder(new LineBorder(Color.red, 1));
				wallEndxTxt.setBorder(new LineBorder(Color.red, 1));
				wallStartyTxt.setBorder(new LineBorder(Color.red, 1));
				wallEndyTxt.setBorder(new LineBorder(Color.red, 1));
			}
		});

		deleteWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int xWallS, yWallS, xWallE, yWallE;
				wallStartxTxt.setBorder(new JTextField().getBorder());
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
				String text = floorLogic.getCurrentFloor().removeWall(wall);
				if(text.length()>0) {
					wallListEnabledTxtArea.setText(wallListEnabledTxtArea.getText().replace(text, ""));
					wallListDisabledTxtArea.setText(wallListDisabledTxtArea.getText().replace(text, ""));
					drawPanel.getLogic().copyCanvas(floorLogic.getCurrentFloor().getCanvas().getLogic());
					drawPanel.setArea(floorLogic.getCurrentFloor().getCanvas().getArea());	
					drawPanel.setPreferredSize(drawPanel.getArea());
					drawPanel.revalidate();
					drawPanel.repaint();
					return;
				}
				wallError.setText("Impossibile cancellare un valore assente");
				wallStartxTxt.setBorder(new LineBorder(Color.red, 1));
				wallEndxTxt.setBorder(new LineBorder(Color.red, 1));
				wallStartyTxt.setBorder(new LineBorder(Color.red, 1));
				wallEndyTxt.setBorder(new LineBorder(Color.red, 1));
			}
		});

		enableWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wallStartxTxt.setBorder(new JTextField().getBorder());
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
				String text = floorLogic.getCurrentFloor().enableDisableWall(wall);
				if (text.length() > 0) {
					if (wallListDisabledTxtArea.getText().contains(text)) {
						wallListDisabledTxtArea.setText(wallListDisabledTxtArea.getText().replace(text, ""));
						wallListEnabledTxtArea.setText(wallListEnabledTxtArea.getText() + text);
					} else {
						wallListEnabledTxtArea.setText(wallListEnabledTxtArea.getText().replace(text, ""));
						wallListDisabledTxtArea.setText(wallListDisabledTxtArea.getText() + text);
					}
					drawPanel.getLogic().copyCanvas(floorLogic.getCurrentFloor().getCanvas().getLogic());
					drawPanel.setArea(floorLogic.getCurrentFloor().getCanvas().getArea());	
					drawPanel.setPreferredSize(drawPanel.getArea());
					drawPanel.revalidate();
					drawPanel.repaint();
				} else {
					wallError.setText("Valore assente");
				}
			}
		});

		createUtilizer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				xUtilTxt.setBorder(new JTextField().getBorder());
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
				if(CoverLightLogicImpl.validatePosition(util)) {
					Boolean flag = floorLogic.getCurrentFloor().setNewUtilizer(util);
					if(flag == null) {
						utilizerListEnabledTxtArea.setText(utilizerListEnabledTxtArea.getText() + "(" + util.x + " " + util.y + ")    ");
						boolean changed = false;
						Dimension area = floorLogic.getCurrentFloor().getCanvas().getArea();
						
						int this_width = (xUtil - xUtil % DIM_SQUARE + DIM_SQUARE + 2);
						if (this_width > area.width) {
							floorLogic.getCurrentFloor().getCanvas().getArea().width = this_width;
							changed = true;
						}
		
						int this_height = (yUtil - yUtil % DIM_SQUARE + DIM_SQUARE + 2);
						if (this_height > area.height) {
							floorLogic.getCurrentFloor().getCanvas().getArea().height = this_height;
							changed = true;
						}
						if (changed) {
							floorLogic.getCurrentFloor().getCanvas().setPreferredSize(area);
							floorLogic.getCurrentFloor().getCanvas().revalidate();
						}
						drawPanel.getLogic().copyCanvas(floorLogic.getCurrentFloor().getCanvas().getLogic());
						drawPanel.setArea(floorLogic.getCurrentFloor().getCanvas().getArea());	
						drawPanel.setPreferredSize(drawPanel.getArea());
						drawPanel.revalidate();
						drawPanel.repaint();
					} else {
						utilError.setText("Valore già presente");
						xUtilTxt.setBorder(new LineBorder(Color.red, 1));
						yUtilTxt.setBorder(new LineBorder(Color.red, 1));
						if(flag) {
							utilizerListEnabledTxtArea.setBorder(new LineBorder(Color.red, 1));
						} else {
							utilizerListDisabledTxtArea.setBorder(new LineBorder(Color.red, 1));
						}
					}
				} else {
					utilError.setText("La posizione deve essere una coppia di numeri da 0 a " + WIDTH + " e da 0 a " + HEIGHT);
				}
			}
		});

		deleteUtilizer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				xUtilTxt.setBorder(new JTextField().getBorder());
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
				String text = floorLogic.getCurrentFloor().removeUtilizer(util);
				if(text.length()>0) {
					utilizerListEnabledTxtArea.setText(utilizerListEnabledTxtArea.getText().replace(text, ""));
					utilizerListDisabledTxtArea.setText(utilizerListDisabledTxtArea.getText().replace(text, ""));
					drawPanel.getLogic().copyCanvas(floorLogic.getCurrentFloor().getCanvas().getLogic());
					drawPanel.setArea(floorLogic.getCurrentFloor().getCanvas().getArea());	
					drawPanel.setPreferredSize(drawPanel.getArea());
					drawPanel.revalidate();
					drawPanel.repaint();
					return;
				}
				utilError.setText("Impossibile cancellare un valore assente");
				xUtilTxt.setBorder(new LineBorder(Color.red, 1));
				yUtilTxt.setBorder(new LineBorder(Color.red, 1));
			}
		});		

		enableUtilizer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				xUtilTxt.setBorder(new JTextField().getBorder());
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
				if (CoverLightLogicImpl.validatePosition(util)) {
					for (Utilizers entry : floorLogic.getCurrentFloor().getUtilizers().keySet()) {
						if (entry.getPosition().equals(util)) {
							if (floorLogic.getCurrentFloor().enableDisableUtilizer(entry)) {
								utilizerListEnabledTxtArea.setText(utilizerListEnabledTxtArea.getText() + "(" + util.x + " " + util.y + ")    ");
								utilizerListDisabledTxtArea.setText(
										utilizerListDisabledTxtArea.getText().replace("(" + util.x + " " + util.y + ")    ", ""));
							} else {
								utilizerListDisabledTxtArea.setText(utilizerListDisabledTxtArea.getText() + "(" + util.x + " " + util.y + ")    ");
								utilizerListEnabledTxtArea.setText(
										utilizerListEnabledTxtArea.getText().replace("(" + util.x + " " + util.y + ")    ", ""));
							}
							drawPanel.getLogic().copyCanvas(floorLogic.getCurrentFloor().getCanvas().getLogic());
							drawPanel.setArea(floorLogic.getCurrentFloor().getCanvas().getArea());	
							drawPanel.setPreferredSize(drawPanel.getArea());
							drawPanel.revalidate();
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
							    colour = (int) Math.min(Math.floor(end_result/15),logic.getCaption().getTones().length-1);
							    resultPanel.setNewRect(i,j,logic.getCaption().getTones()[colour],end_result);
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
					resultPanel.getLogic().copyCanvas(drawPanel.getLogic());
					resultPanel.getGraphics().clearRect(0, 0, WIDTH, HEIGHT);
					resultPanel.paintComponent(resultPanel.getGraphics());
				}
			}
		});
		
		previousFloorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				duplicateMapBtn.setEnabled(true);
				duplicateMapTxt.setEnabled(true);
				
				canvasContainerPanel.removeAll();

				floorLogic.moveToLowerFloor();
				
				drawPanel.getLogic().copyCanvas(floorLogic.getCurrentFloor().getCanvas().getLogic());
				drawPanel.setArea(floorLogic.getCurrentFloor().getCanvas().getArea());	
				drawPanel.setPreferredSize(drawPanel.getArea());
				canvasContainerPanel.add(drawPanel);
				drawPanel.revalidate();
				drawPanel.repaint();

				emitterListEnabledTxtArea.setText("");
				emitterListDisabledTxtArea.setText("");
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
				emitterListDisabledTxtArea.setBorder(new JTextField().getBorder());
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
				
				canvasContainerPanel.removeAll();
				floorLogic.moveToHigherFloor();	
				drawPanel.getLogic().copyCanvas(floorLogic.getCurrentFloor().getCanvas().getLogic());
				drawPanel.setArea(floorLogic.getCurrentFloor().getCanvas().getArea());	
				drawPanel.setPreferredSize(drawPanel.getArea());
				canvasContainerPanel.add(drawPanel);
				drawPanel.revalidate();
				drawPanel.repaint();

				emitterListEnabledTxtArea.setText("");
				emitterListDisabledTxtArea.setText("");
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
