import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * 
 * @author Justin Horn<br><br>
 * Window and game..... <p>
 * Useful for testing purposes
 */
public class GraphicTTT extends JFrame {

	private SlideToGraphicClasses slide;

	public GraphicTTT(SlideToGraphicClasses slide) {
		this.slide = slide;
		setUpWindow();
		addPanelsToWindow();
		revalidate();
		repaint();
	}
	
	private void setUpWindow() {
		setTitle("TicTacToe");
		setWindowBounds();
		setEnabled(true);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setResizable(false);
	}
	
	private void setWindowBounds() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		setBounds(width / 4, height / 4, width / 2, height / 2); 
	}
	
	private void addPanelsToWindow() {
		JPanel aiPanel = createRadioButtonPanelForAiType();
		add(aiPanel);
		add(createRadioButtonPanelForGameMode(aiPanel));
		
		GraphicTTTField JtttFieldPanel = new GraphicTTTField(slide);
		add(JtttFieldPanel);
		JtttFieldPanel.fixBounds();
	}
	
	private JPanel createRadioButtonPanelForGameMode(JPanel panel_supposedTo_ShowOrHide) {
		JRadioButton[] radioButtons = new JRadioButton[3]; // radiobuttons gameTyp
		radioButtons[2] = new JRadioButton("Player Vs Player");
		radioButtons[1] = new JRadioButton("Player Vs Computer");
		radioButtons[0] = new JRadioButton("Computer Vs Player", true);

		addButtonsToAButtonGroup(radioButtons);
		
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 1));
		for (int i = 0; i < radioButtons.length; i++) {
			panel.add(radioButtons[i]);
			
			final int j = i;
			radioButtons[i].addActionListener(new ActionListener() {
				final int b =j;
				@Override
				public void actionPerformed(ActionEvent arg0) {
					slide.whichGameMode = b;
					if (slide.whichGameMode  == slide.PLAYER_VS_PLAYER) {
						panel_supposedTo_ShowOrHide.setEnabled(false);
						panel_supposedTo_ShowOrHide.setVisible(false);
					} else {
						panel_supposedTo_ShowOrHide.setEnabled(true);
						panel_supposedTo_ShowOrHide.setVisible(true);
					}
				}

			});
		}

		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Game Mode"));
		panel.setBounds(getWidth() / 20,getHeight() / 20, getWidth() / 5, getHeight() / 5);
		return panel;
	}
	
	private JPanel createRadioButtonPanelForAiType() {		
		JPanel buttonPanel = createRadioPanelAiType();
		
		JRadioButton[] radioButton = createAIradioButtonArray();
		addRadioButtonsToJPanel(buttonPanel,radioButton);

		return buttonPanel;
	}
	
	private JPanel createRadioPanelAiType() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ai Type"));
		panel.setBounds(getWidth() / 20, 6 * getHeight() / 20, getWidth() / 5,
				5 * getHeight() / 10);
		panel.setEnabled(true);
		return panel;
	}
	
	private void addRadioButtonsToJPanel(JPanel panel,JRadioButton[] rb) {
		panel.setLayout(new GridLayout(rb.length, 1)); // does two thing....
		for (int i = 0; i < rb.length; i++) {
			panel.add(rb[i]);
		}
	}
	
	private JRadioButton[] createAIradioButtonArray() {
		int s  = slide.neuralnetworks.size();
		JRadioButton[] ai = new JRadioButton[s+1]; 
		ai[0] = new JRadioButton("Algorithm", true);//selected button
		for(int i = 1; i <= s;i++) {
			ai[i] = new JRadioButton("NeuralNet "+i+" "+String.format(" %2.3f ",slide.getRelativScoreOfNN(i-1)));
		}
		for (int i = 0; i < ai.length; i++) {
			ai[i].addActionListener(createActionlistenerChangesWhichAi(i));
		}
		addButtonsToAButtonGroup(ai);

		return ai;
	}
	
	
	private ButtonGroup addButtonsToAButtonGroup(AbstractButton[] buttons) {
		ButtonGroup b = new ButtonGroup();
		for (int i = 0; i < buttons.length; i++) {
			b.add(buttons[i]);
		}
		return b;
	}

	private ActionListener createActionlistenerChangesWhichAi(final int buttonIndex) {
		return new ActionListener() {
			final int b = buttonIndex;

			public void actionPerformed(ActionEvent e) {
				slide.whichAi = b;
			}
		};
	}


}
