package MyClasses;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class HelpDialogImpl {

	private JScrollPane firstSlide;
	private JPanel secondSlide;
	private JPanel thirdSlide;
	private JPanel fourthSlide;
	
	public HelpDialogImpl() {
		setFirstSlide();
		setSecondSlide();
		setThirdSlide();
		setFourthSlide();
	}

	private void setFirstSlide() {
		final JTextArea firstSlideTxtArea = new JTextArea();
		firstSlideTxtArea.setLineWrap(true);
		firstSlideTxtArea.setText("L’applicazione genera di mappe di copertura di rete Wi-Fi all’interno di un edificio, simulando la qualità minima dei segnali a seconda delle infrastrutture presenti.\r\n"
				+ "In alto a destra vi è un gruppo di tre sezioni in cui inserire i dati relativi alla simulazione; le componenti vanno realizzate una per volta premendo 'Crea' dopo aver compilato le relative caselle.\r\n"
				+ "'Cancella' rimuove l'elemento referenziato dalle caselle 'X' e 'Y', mentre 'Abilita/Disabilita' lo sospende dalla simulazione corrente ma permette di richiamarlo ripremendo il pulsante (o rimuoverlo definitivamente con 'Cancella').\r\n"
				+ "'Muro' - la planimetria dei locali su cui effettuare la misura;\r\n"
				+ "'Emittente' - il posizionamento di apparati Wi-Fi master/access points (generatori di segnali radio);\r\n"
				+ "'Utilizzatore' - la locazione di apparati slave (ricevitori che utilizzano i segnali).\r\n"
				+ "La simulazione così creata è mostrata in forma di immagine in alto a sinistra e riassunta in forma testuale in basso a destra.\r\n"
				+ "In basso a sinistra si può generare il risultato della simulazione: sottoimposto al contenuto della mappa sovrastante, mostra l'intensità del segnale Wi-Fi - raffigurata con una scala di colori.\r\n"
				+ "Tale scala è elencata in 'Didascalia' ed ivi modificabile; le si può aggiungere una descrizione testuale più accurata spuntando la casella 'Valori'.\r\n"
				+ "A differenza della simulazione, il risultato non è aggiornato a caldo: si creerà una nuova proiezione solo premendo il pulsante 'Genera risultato'.\r\n"
				+ "Infine, vi sono due pulsanti per spostarsi nel piano inferiore/superiore dell'edificio (o crearlo, se non già presente); ogni piano presenterà la propria mappa, modificando immagini e testo in accordanza;\r\n"
				+ "emittenti presenti in punti diversi dal piano corrente sono considerati nel calcolo del segnale, quindi è possibile usare 'Genera risultato' anche senza aver inserito emettitori nel piano corrente.\r\n"
				+ "Dopo aver creato almeno un piano, si sbloccherà il pulsante 'Copia su piano': inserendo nella casella adiacente il numero di un piano, vi si ricopierà la mappa del piano corrente (muri, emittenti ed utilizzatori).\r\n"
				+ "Consultare la Didascalia per dettagli.");
		final JScrollPane scroll = new JScrollPane(firstSlideTxtArea);
		firstSlideTxtArea.setEditable(false);
		this.firstSlide = scroll;
	}
	
	public JScrollPane getFirstSlide() {		
		return this.firstSlide;
	}

	private void setSecondSlide() {
		final JPanel secondSlidePanel = new JPanel(new BorderLayout());
		secondSlidePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// Graphical part
		final JPanel canvasPanel = new JPanel(new BorderLayout());
		final JPanel canvasPanelBorder = new JPanel(new BorderLayout());
		canvasPanelBorder.add(canvasPanel);
		canvasPanelBorder.setBorder(new JTextField().getBorder());
		final JPanel canvasPanelSeparator = new JPanel(new BorderLayout());
		canvasPanelSeparator.add(canvasPanelBorder);
		canvasPanelSeparator.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		final JLabel canvasTitleLbl = new JLabel("Canvas");
		canvasTitleLbl.setFont(new Font("Serif", Font.BOLD, 25));
		final JButton captionBtn = new JButton("Didascalia");
		captionBtn.setBorder(new LineBorder(Color.BLUE, 2));// blue
		final JPanel canvasTitlePanel = new JPanel(new BorderLayout());
		canvasTitlePanel.add(canvasTitleLbl, BorderLayout.LINE_START);
		canvasTitlePanel.add(captionBtn, BorderLayout.LINE_END);
		canvasTitlePanel.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 20));

		final JTextArea drawArea = new JTextArea();
		drawArea.setBorder(new LineBorder(Color.RED, 2));// red
		final JPanel canvasFloorPanel = new JPanel(new BorderLayout());
		final JPanel canvasFloorBtnPanel = new JPanel(new FlowLayout());
		final JButton previousFloorBtn = new JButton("Crea piano -1");
		final JLabel currentFloorLbl = new JLabel("Piano: 0");
		final JButton nextFloorBtn = new JButton("Crea piano 1");
		final JButton duplicateMapBtn = new JButton("Copia su piano");
		final JTextField duplicateMapTxt = new JTextField();
		duplicateMapTxt.setColumns(3);

		final JPanel canvasFloorCreateBtnPanel = new JPanel(new BorderLayout());// empty right border
		final JPanel canvasFloorCreateBtnPanelBorder = new JPanel(new FlowLayout());// green border
		canvasFloorCreateBtnPanelBorder.add(previousFloorBtn);
		canvasFloorCreateBtnPanelBorder.add(currentFloorLbl);
		canvasFloorCreateBtnPanelBorder.add(nextFloorBtn);
		canvasFloorCreateBtnPanelBorder.setBorder(new LineBorder(Color.GREEN, 2));// green
		canvasFloorCreateBtnPanel.add(canvasFloorCreateBtnPanelBorder);
		canvasFloorCreateBtnPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

		final JPanel canvasFloorduplicateBtnPanel = new JPanel(new FlowLayout());
		canvasFloorduplicateBtnPanel.add(duplicateMapBtn);
		canvasFloorduplicateBtnPanel.add(duplicateMapTxt);
		canvasFloorduplicateBtnPanel.setBorder(new LineBorder(Color.MAGENTA, 2));// magenta
		canvasFloorBtnPanel.add(canvasFloorCreateBtnPanel);
		canvasFloorBtnPanel.add(canvasFloorduplicateBtnPanel);

		final JPanel canvasCheckboxPanel = new JPanel(new BorderLayout());
		final JPanel canvasCheckboxPanelBorder = new JPanel(new BorderLayout());
		canvasCheckboxPanelBorder.add(canvasCheckboxPanel);
		canvasCheckboxPanelBorder.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 5));

		JCheckBox currentFloorCB = new JCheckBox("Valori", false);
		canvasCheckboxPanel.setBorder(new LineBorder(Color.ORANGE, 2));// orange
		canvasCheckboxPanel.add(currentFloorCB);
		canvasFloorPanel.add(canvasFloorBtnPanel, BorderLayout.CENTER);
		canvasFloorPanel.add(canvasCheckboxPanelBorder, BorderLayout.LINE_END);
		canvasPanel.add(canvasTitlePanel, BorderLayout.PAGE_START);
		canvasPanel.add(drawArea, BorderLayout.CENTER);
		canvasPanel.add(canvasFloorPanel, BorderLayout.PAGE_END);

		// Text part
		final JTextPane textPane = new JTextPane();
		textPane.setBorder(new JTextField().getBorder());
		appendToPane(textPane, " \nDidascalia", Color.BLUE);
		appendToPane(textPane, " Il pulsante per la didascalia; si potrà consultare una guida dettagliata al programma e modificare la scala di colori usata per rappresentare i dati. \n", Color.BLACK);
		appendToPane(textPane, " \nArea di disegno", Color.RED);
		appendToPane(textPane, " Il canvas dove vengono disegnate le forme che rappresentano gli inserimenti dell'utente. \n", Color.BLACK);
		appendToPane(textPane, " \nPiano", Color.GREEN);
		appendToPane(textPane, " I pulsanti che permettono di creare, o navigare se già creati in precedenza, diversi livelli di piani "
				+ "in cui il progetto lavora. La label al centro indica il piano corrente (di default 0, il piano terra). \n", Color.BLACK);
		appendToPane(textPane, " \nCopia", Color.MAGENTA);
		appendToPane(textPane, " Il pulsante permette di copiare il contenuto del canvas in un altro piano inserito nella label. \n", Color.BLACK);
		appendToPane(textPane, " \nCheckbox", Color.ORANGE);
		appendToPane(textPane, " Se la checkbox è spuntata, al momento della generazione del risultato verranno mostrati i valori in dBm (decibel milliwatt) dell'intensità di ogni area. \n", Color.BLACK);

		textPane.setEditable(false);

		// Add to the panel
		secondSlidePanel.add(textPane, BorderLayout.CENTER);
		secondSlidePanel.add(canvasPanelSeparator, BorderLayout.LINE_START);
		this.secondSlide = secondSlidePanel;
	}
	
	public JPanel getSecondSlide() {	
		return this.secondSlide;
	}

	private void setThirdSlide() {
		final JPanel thirdSlidePanel = new JPanel(new BorderLayout());
		thirdSlidePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// Graphical part
		final JPanel buttonsPanelContainer = new JPanel(new BorderLayout());
		final JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		buttonsPanelContainer.add(buttonsPanel, BorderLayout.CENTER);
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 10));
		
		// Elemento
		final JPanel panelElement = new JPanel();
		final GroupLayout layoutElem = new GroupLayout(panelElement);
		panelElement.setLayout(layoutElem);
		TitledBorder border = BorderFactory.createTitledBorder("Elemento");
		panelElement.setBorder(border);
		border.setTitleColor(Color.RED);

		final JButton createElem = new JButton("Crea");
		createElem.setBorder(new LineBorder(Color.MAGENTA, 2));// magenta
		JButton deleteElem = new JButton("Cancella");
		deleteElem.setBorder(new LineBorder(Color.ORANGE, 2));// orange
		JButton enableElem = new JButton("Abilita/Disabilita");
		enableElem.setBorder(new LineBorder(Color.PINK, 2));// pink
		JLabel xElemLbl = new JLabel("X:");
		JTextField xElemTxt = new JTextField();
		xElemTxt.setBorder(new LineBorder(Color.BLUE, 2));// BLUE
		xElemTxt.setColumns(4);
		JLabel yElemLbl = new JLabel("Y:");
		JTextField yElemTxt = new JTextField();
		yElemTxt.setBorder(new LineBorder(Color.BLUE, 2));// BLUE
		yElemTxt.setColumns(4);

		layoutElem.setAutoCreateGaps(true);
		layoutElem.setAutoCreateContainerGaps(true);

		layoutElem.setHorizontalGroup(layoutElem.createSequentialGroup()
						.addGroup(layoutElem.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(createElem, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(deleteElem, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(enableElem, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
						.addGroup(layoutElem.createSequentialGroup()
								.addGroup(layoutElem.createSequentialGroup().addComponent(xElemLbl)
										.addComponent(xElemTxt))
								.addGroup(layoutElem.createSequentialGroup().addComponent(yElemLbl)
										.addComponent(yElemTxt))));

		layoutElem.setVerticalGroup(layoutElem
				.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(layoutElem.createSequentialGroup().addComponent(createElem)
						.addComponent(deleteElem).addComponent(enableElem))
				.addGroup(layoutElem.createParallelGroup(GroupLayout.Alignment.BASELINE).addGroup(layoutElem
						.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGroup(layoutElem.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(xElemLbl).addComponent(xElemTxt))
						.addGroup(layoutElem.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addGroup(layoutElem.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(yElemLbl).addComponent(yElemTxt))))));

		buttonsPanel.add(panelElement);// Aggiunta al panel

		// Text part
		final JTextPane textPane = new JTextPane();
		textPane.setBorder(new JTextField().getBorder());
		
		appendToPane(textPane, " \nElemento", Color.RED);
		appendToPane(textPane, " I tipi di dato inseribili nel canvas, di seguito riportati: \n", Color.BLACK);
		
		appendToPane(textPane, " \n EMITTENTI", Color.BLACK);
		appendToPane(textPane, " Emettono i segnali che ricoprono l'edificio - con una certa potenza, frequenza ed angolazione."
				+ "Nonostante le limitazioni legislative, le potenze consentite sono sufficienti ad attraversare pavimenti, soffitti e pareti multiple. \n", Color.BLACK);

		appendToPane(textPane, " \n UTILIZZATORI", Color.BLACK);
		appendToPane(textPane, " Evidenziano aree in cui siano presenti apparati ricevitori; non influenzano il calcolo dell'intensità - data la loro importanza come elemento visivo, sono disegnati in primo piano \n", Color.BLACK);

		appendToPane(textPane, " \n MURI", Color.BLACK);
		appendToPane(textPane, " Assorbono una porzione del segnale, indebolendolo; poiché esistono diversi tipi di muratura, il programma si serve di approssimazioni empiriche da consultare nella didascalia. \n", Color.BLACK);

		appendToPane(textPane, " \n\nDi seguito le spiegazioni per pulsanti e textbox di input. \n", Color.BLACK);

		appendToPane(textPane, " \nCrea", Color.MAGENTA);
		appendToPane(textPane, " Il pulsante disegna l'elemento sul canvas e lo memorizza nell'adeguata lista. \n",
				Color.BLACK);

		appendToPane(textPane, " \nCancella", Color.ORANGE);
		appendToPane(textPane,
				" Il pulsante richiede le coordinate dell'elemento (necessarie e sufficienti) per la cancellazione sia dal canvas che dalla lista. \n",
				Color.BLACK);

		appendToPane(textPane, " \nAbilita/Disabilita", Color.PINK);
		appendToPane(textPane,
				" Il pulsante permette di rimuovere un elemento dal Canvas mantenendolo in memoria; dovrà essere comunque tenuto in considerazione quando si inseriscono nuovi elementi\n"
				+ "(lo scopo della funzionalità è permettere di collaudare alternative; non si permette di proseguire in una maniera che invalidi un'alternativa non ancora esclusa).\n", Color.BLACK);
		
		appendToPane(textPane, "\nInput", Color.BLUE);
		appendToPane(textPane, " Tutti gli elementi richiedono l'inserimento delle relative coordinate nel piano per svolgere le varie operazioni"
				+ "sopra elencate; muri ed emittenti posseggono ulteriori attributi da specificare. \n", Color.BLACK);

		textPane.setEditable(false);

		// Add to the panel
		thirdSlidePanel.add(textPane, BorderLayout.CENTER);
		thirdSlidePanel.add(buttonsPanel, BorderLayout.LINE_START);
		this.thirdSlide = thirdSlidePanel;
	}
	
	public JPanel getThirdSlide() {
		return this.thirdSlide;
	}

	private void setFourthSlide() {
		final JPanel fourthSlidePanel = new JPanel(new BorderLayout());
		fourthSlidePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// Graphical part
		final JPanel graphicalPanel = new JPanel(new GridLayout(2, 1));
		graphicalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 20));
		
		final JPanel resultPanel = new JPanel();
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
		final JPanel resultPanelBorder = new JPanel(new BorderLayout());
		resultPanelBorder.add(resultPanel);
		resultPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		final JButton generateResult = new JButton("Genera risultato");
		generateResult.setAlignmentX(Component.CENTER_ALIGNMENT);
		resultPanelBorder.setBorder(new LineBorder(Color.RED, 2));// red

		final JPanel resultContainerPanel = new JPanel(new BorderLayout());
		final JScrollPane scrollResult = new JScrollPane(resultContainerPanel);
		scrollResult.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		final JTextArea showResult = new JTextArea();
		resultContainerPanel.add(showResult);
		resultPanel.add(generateResult);
		resultPanel.add(scrollResult);

		// Emitters
		final JPanel listEmitterPanel = new JPanel(); // Emitters list
		listEmitterPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		final GroupLayout listEmitterLayout = new GroupLayout(listEmitterPanel);
		listEmitterPanel.setLayout(listEmitterLayout);
		final JPanel listEmitterPanelBorder = new JPanel(new BorderLayout());
		listEmitterPanelBorder.add(listEmitterPanel);
		listEmitterPanelBorder.setBorder(new LineBorder(Color.ORANGE, 2));// orange
		final JPanel listEmitterPanelSpacing = new JPanel(new BorderLayout());
		listEmitterPanelSpacing.add(listEmitterPanelBorder);
		listEmitterPanelSpacing.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));

		JLabel emitterListEnabledLbl = new JLabel("Elementi abilitati");
		emitterListEnabledLbl.setForeground(Color.GREEN);
		final JPanel emitterListEnabledPanel = new JPanel(new BorderLayout());
		final JTextArea emitterListEnabledTxtArea = new JTextArea();
		emitterListEnabledTxtArea.setLineWrap(true);
		final JScrollPane emitterListEnabledScroll = new JScrollPane(emitterListEnabledTxtArea);
		emitterListEnabledScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		emitterListEnabledPanel.add(emitterListEnabledScroll);

		JLabel emitterListDisabledLbl = new JLabel("Elementi disabilitati");
		emitterListDisabledLbl.setForeground(Color.BLUE);
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

		graphicalPanel.add(resultPanelBorder);
		graphicalPanel.add(listEmitterPanelSpacing);

		// Text part
		final JTextPane textPane = new JTextPane();
		textPane.setBorder(new JTextField().getBorder());
		appendToPane(textPane, " \nGenera Risultato", Color.RED);
		appendToPane(textPane, " Stampa il canvas come una overview nell'area sottostante, sovrapponendolo ad una rappresentazione grafica del risultato. \n\n", Color.BLACK);
		
		appendToPane(textPane, " \nLe liste", Color.ORANGE);
		appendToPane(textPane, " di elementi(Emettitori, Utilizzatori e Muri) memorizzati nel programma. \n", Color.BLACK);
		
		appendToPane(textPane, " \nElementi abilitati", Color.GREEN);
		appendToPane(textPane, " Lista dei rispettivi elementi abilitati, presenti sul canvas e considerati nell'elaborazione del risultato. \n", Color.BLACK);
		
		appendToPane(textPane, " \nElementi disabilitati", Color.BLUE);
		appendToPane(textPane, " Lista dei rispettivi elementi disabilitati, presenti solo in memoria. \n", Color.BLACK);
		textPane.setEditable(false);

		// Add to the panel
		fourthSlidePanel.add(textPane, BorderLayout.CENTER);
		fourthSlidePanel.add(graphicalPanel, BorderLayout.LINE_START);
		this.fourthSlide = fourthSlidePanel;
	}
	
	public JPanel getFourthSlide() {	
		return this.fourthSlide;
	}

	private void appendToPane(JTextPane tp, String msg, Color c) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet attrSet = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
		attrSet = sc.addAttribute(attrSet, StyleConstants.FontFamily, "Lucida Console");
		attrSet = sc.addAttribute(attrSet, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

		int length = tp.getDocument().getLength();
		tp.setCaretPosition(length);
		tp.setCharacterAttributes(attrSet, false);
		tp.replaceSelection(msg);
	}

}
