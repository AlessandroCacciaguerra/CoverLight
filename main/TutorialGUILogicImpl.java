import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class TutorialGUILogicImpl {
	private int tutorialSlideCounter;
	private static final int MIN_SLIDE = 0;
	private static final int MAX_SLIDE = 3;
	private TutorialGUI tutorialGui;
	private JPanel tutorialGraphicPanel;
	private JPanel buttonsTutorialGraphicPanel;
	private JPanel tutorialContainerPanel;
	
	public TutorialGUILogicImpl() {
		tutorialGui = new TutorialGUI();
		tutorialSlideCounter = 0;
		setTutorialPanel();
		setButtonsTutorialPanel();
	}
	
	public void nextSlide() {
		tutorialSlideCounter = tutorialSlideCounter < MAX_SLIDE ? tutorialSlideCounter + 1 : tutorialSlideCounter;
	}
	
	public void previousSlide() {
		tutorialSlideCounter = tutorialSlideCounter > MIN_SLIDE? tutorialSlideCounter - 1 : tutorialSlideCounter;
	}

	public void buttonsLogic(JButton previousBtn, JButton nextBtn) {
		if (previousBtn.isEnabled() == false && (tutorialSlideCounter > MIN_SLIDE)) {
			previousBtn.setEnabled(true);
		}
		if (tutorialSlideCounter == MAX_SLIDE) {
			nextBtn.setEnabled(false);
		}
		if(nextBtn.isEnabled() == false && (tutorialSlideCounter < MAX_SLIDE)) {
			nextBtn.setEnabled(true);
		}
		if(tutorialSlideCounter == MIN_SLIDE) {
			previousBtn.setEnabled(false);
		}
	}
	
	public int getSlideNumber() {
		return this.tutorialSlideCounter;
	}
	
	public JPanel getCurrectSlide() {
		switch (tutorialSlideCounter) {
		case 0:
			return tutorialGui.getFirstSlide();
		case 1:
			return tutorialGui.getSecondSlide();
		case 2:
			return tutorialGui.getThirdSlide();
		case 3:
			return tutorialGui.getFourthSlide();
		}
		return tutorialGui.getFirstSlide();
	}
	
	private void setTutorialPanel() {
		tutorialContainerPanel = new JPanel(new BorderLayout());					
		final JPanel tutorialPanelSeparator = new JPanel(new BorderLayout());
		final JPanel tutorialPanelBorder = new JPanel(new BorderLayout());
		final JPanel tutorialPanelSpacing = new JPanel(new BorderLayout());
		tutorialPanelSeparator.add(tutorialContainerPanel);
		tutorialPanelBorder.add(tutorialPanelSeparator);
		tutorialPanelSpacing.add(tutorialPanelBorder);				
		tutorialPanelBorder.setBorder(new LineBorder(Color.BLACK, 2));
		tutorialPanelSpacing.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		tutorialContainerPanel.add(getCurrectSlide());
		this.tutorialGraphicPanel = tutorialPanelSpacing;
	}
	
	public JPanel getTutorialPanel() {
		return this.tutorialGraphicPanel;
	}
	
	private void setButtonsTutorialPanel() {
		final JPanel buttonsTutorialPanel = new JPanel(new BorderLayout());
		final JPanel buttonsTutorialPanelContainer = new JPanel(new BorderLayout());
		final JPanel buttonsTutorialPanelSpacing = new JPanel(new BorderLayout());
		final JLabel slideTitle = new JLabel("Introduzione");
		JPanel slideTitleGB = new JPanel(new GridBagLayout());
		slideTitleGB.add(slideTitle,new GridBagConstraints());
		buttonsTutorialPanelContainer.add(buttonsTutorialPanel);
		buttonsTutorialPanelSpacing.add(buttonsTutorialPanelContainer);
		final JButton nextBtn = new JButton("Next");
		final JButton previousBtn = new JButton("Previous");
		previousBtn.setEnabled(false);
		buttonsTutorialPanel.add(nextBtn, BorderLayout.LINE_END);
		buttonsTutorialPanel.add(slideTitleGB, BorderLayout.CENTER);
		buttonsTutorialPanel.add(previousBtn, BorderLayout.LINE_START);
		
		buttonsTutorialPanel.setBorder(BorderFactory.createEmptyBorder(15, 70, 15, 70));
		buttonsTutorialPanelContainer.setBorder(new LineBorder(Color.BLACK, 1));
		buttonsTutorialPanelSpacing.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		nextBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				nextSlide();
				buttonsLogic(previousBtn, nextBtn);						
				tutorialContainerPanel.removeAll();
				tutorialContainerPanel.add(getCurrectSlide());
				slideTitle.setText(getTitleSlide());
				tutorialContainerPanel.revalidate();
				tutorialContainerPanel.updateUI();
			}
		});
		
		previousBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				previousSlide();
				buttonsLogic(previousBtn, nextBtn);		
				tutorialContainerPanel.removeAll();
				tutorialContainerPanel.add(getCurrectSlide());
				slideTitle.setText(getTitleSlide());
				tutorialContainerPanel.revalidate();
				tutorialContainerPanel.updateUI();
			}
		});			
		this.buttonsTutorialGraphicPanel = buttonsTutorialPanelSpacing;
	}
	
	public JPanel getButtonsTutorialPanel() {
		return this.buttonsTutorialGraphicPanel;
	}
	
	public String getTitleSlide() {
		switch (tutorialSlideCounter) {
		case 0:
			return ("Introduzione");
		case 1:
			return ("Area di disegno");
		case 2:
			return ("Area input");
		case 3:
			return ("Area di output");
		}
		return "Errore slide non esistente";
	}
}
