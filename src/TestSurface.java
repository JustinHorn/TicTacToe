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
public class TestSurface {

	private JFrame window;
	 /**0 = Algorithm, 1 = nn[0]| 2 = nn[1]| 3 = nn[2]*/
	/**0 = CvS| 1 = SvC| 2 = SvS*/
	private JTTTFieldPanel fieldSurface;
	private ProgrammExecution execution;
	private TestSurface_Execution_variableContainer container;
	

	
	public static void main(String[] args) {
		new TestSurface();
	}

	public TestSurface() {
		assingGlobals();
		setUpWindow();
	}

	private void assingGlobals() {
		container = new TestSurface_Execution_variableContainer();
		window = new JFrame("TicTacToe");
		setWindowBounds();

		fieldSurface = new JTTTFieldPanel(window);

		execution = new ProgrammExecution(container,fieldSurface);
		
	}
	
	private void setUpWindow() {
		setWindowBounds();
		window.setEnabled(true);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(null);
		window.setResizable(false);
		addPanelsToWindow();
		window.revalidate();
		window.repaint();
	}
	private void setWindowBounds() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		window.setBounds(width / 4, height / 4, width / 2, height / 2); 
	}
	
	private void addPanelsToWindow() {
		JPanel aiPanel = create_radioButtonPanel_for_aiType();
		window.add(aiPanel);
		window.add(create_radioButtonPanel_for_gameMode(aiPanel));
		window.add(create_TicTacToePanel());
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
					container.whichGameMode = b;
					if (container.whichGameMode  == container.PLAYER_VS_PLAYER) {
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
		panel.setBounds(window.getWidth() / 20, window.getHeight() / 20, window.getWidth() / 5, window.getHeight() / 5);
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
		panel.setBounds(window.getWidth() / 20, 6 * window.getHeight() / 20, window.getWidth() / 5,
				5 * window.getHeight() / 10);
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
		ai[1] = new JRadioButton("NeuralNet 1 "+String.format(" %2.3f ",container.getRelativScore_of_NN(0)));
		ai[2] = new JRadioButton("NeuralNet 2 "+String.format(" %2.3f ",container.getRelativScore_of_NN(1)));
		ai[3] = new JRadioButton("NeuralNet 3 "+String.format(" %2.3f ",container.getRelativScore_of_NN(2)));
		for (int i = 0; i < ai.length; i++) {
			ai[i].addActionListener(actionListener_that_changes_whichAi(i));

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
	
	private JPanel create_TicTacToePanel() {
		for (int i = 0; i < 9; i++) {
			fieldSurface.jpanel[i].addMouseListener(newMouselistener_for_tttPanel(i));
		}
		
		fieldSurface.button_newGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fieldSurface.reset();
				container.field.setField_toStart();
				if (container.whichGameMode == container.COMPUTER_VS_PLAYER) { // ai starts
					execution.computerMove();
				}
			}

		});
		return fieldSurface;
	}
	
	/**
	 * Tells the panel when it was clicked or not.
	 * @param panelIndex
	 * @return MouseListener that takes care of the move
	 */
	private MouseListener newMouselistener_for_tttPanel(final int panelIndex) {
		return new MouseListener() {
			final int index = panelIndex;

			@Override
			public void mouseClicked(MouseEvent arg0) {
				mouseEvent();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				mouseEvent();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				mouseEvent();
			}
			private void mouseEvent() {
				if(container.field.whoHasWon() == -1) {
					if (container.field.isFree(index)) {
						fieldSurface.paint(index, container.field.whosTurn());
						container.field.set(index);
						if (container.whichGameMode != container.PLAYER_VS_PLAYER) {
							execution.computerMove();
						}
					}
				}else {
					JOptionPane.showMessageDialog(null, "Winner is: "+ container.field.toChar(container.field.whoHasWon()));
				}
			}
			@Override public void mouseEntered(MouseEvent e) {}@Override public void mouseExited(MouseEvent e) {}
		};
	}
	
	private ActionListener actionListener_that_changes_whichAi(final int buttonIndex) {
		return new ActionListener() {
			final int b = buttonIndex;

			public void actionPerformed(ActionEvent e) {
				container.whichAi = b;
			}
		};
	}


}
