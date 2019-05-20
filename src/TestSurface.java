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
public class TestSurface extends JFrame {

	private Class_Connector connector;

	public TestSurface(Class_Connector connector) {
		this.connector = connector;
		setUpWindow();
		addPanelsToWindow();
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
		JPanel aiPanel = create_radioButtonPanel_for_aiType();
		add(aiPanel);
		add(create_radioButtonPanel_for_gameMode(aiPanel));
		revalidate();
		repaint();
	}
	
	private JPanel create_radioButtonPanel_for_gameMode(JPanel panel_supposedTo_ShowOrHide) {
		JRadioButton[] radioButtons = new JRadioButton[3]; // radiobuttons gameTyp
		radioButtons[2] = new JRadioButton("Player Vs Player");
		radioButtons[1] = new JRadioButton("Player Vs Computer");
		radioButtons[0] = new JRadioButton("Computer Vs Player", true);

		addButtons_toA_ButtonGroup(radioButtons);
		
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 1));
		for (int i = 0; i < radioButtons.length; i++) {
			panel.add(radioButtons[i]);
			
			final int j = i;
			radioButtons[i].addActionListener(new ActionListener() {
				final int b =j;
				@Override
				public void actionPerformed(ActionEvent arg0) {
					connector.whichGameMode = b;
					if (connector.whichGameMode  == connector.PLAYER_VS_PLAYER) {
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
	
	private JPanel create_radioButtonPanel_for_aiType() {		
		JPanel buttonPanel = create_radioPanel_aiType();
		
		JRadioButton[] radioButton = create_AIradioButtonArray();
		addRadioButtons_to_JPanel(buttonPanel,radioButton);

		return buttonPanel;
	}
	
	private JPanel create_radioPanel_aiType() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ai Type"));
		panel.setBounds(getWidth() / 20, 6 * getHeight() / 20, getWidth() / 5,
				5 * getHeight() / 10);
		panel.setEnabled(true);
		return panel;
	}
	
	private void addRadioButtons_to_JPanel(JPanel panel,JRadioButton[] rb) {
		panel.setLayout(new GridLayout(rb.length, 1)); // does two thing....
		for (int i = 0; i < rb.length; i++) {
			panel.add(rb[i]);
		}
	}
	
	private JRadioButton[] create_AIradioButtonArray() {
		JRadioButton[] ai = new JRadioButton[4]; // radioButtons aiType
		ai[0] = new JRadioButton("Algorithm", true);//selected button
		ai[1] = new JRadioButton("NeuralNet 1 "+String.format(" %2.3f ",connector.getRelativScore_of_NN(0)));
		ai[2] = new JRadioButton("NeuralNet 2 "+String.format(" %2.3f ",connector.getRelativScore_of_NN(1)));
		ai[3] = new JRadioButton("NeuralNet 3 "+String.format(" %2.3f ",connector.getRelativScore_of_NN(2)));
		for (int i = 0; i < ai.length; i++) {
			ai[i].addActionListener(create_Actionlistener_changes_whichAi(i));
		}
		addButtons_toA_ButtonGroup(ai);

		return ai;
	}
	
	
	private ButtonGroup addButtons_toA_ButtonGroup(AbstractButton[] buttons) {
		ButtonGroup b = new ButtonGroup();
		for (int i = 0; i < buttons.length; i++) {
			b.add(buttons[i]);
		}
		return b;
	}

	private ActionListener create_Actionlistener_changes_whichAi(final int buttonIndex) {
		return new ActionListener() {
			final int b = buttonIndex;

			public void actionPerformed(ActionEvent e) {
				connector.whichAi = b;
			}
		};
	}


}
