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
 * Window and game..... <br>
 * Useful for testing purposes
 */
public class TestSurface {

	private JFrame window;
	 /**0 = Algorithm, 1 = nn[0]| 2 = nn[1]| 3 = nn[2]*/
	private int whichAi;
	/**0 = CvS| 1 = SvC| 2 = SvS*/
	private int whichGameMode;
	private NeuralNetwork[] nn;
	private TTT_NN_Trainer ntrain;
	private JTTTFieldPanel fieldSurface;
	private TTTField field;
	
	private final int PLAYER_VS_PLAYER = 2;
	/**
	 * Does the shit
	 * @param args
	 */
	public static void main(String[] args) {
		new TestSurface();
	}

	public TestSurface() {
		assingGlobals();
		setUpWindow();
	}
	
	/**
	 * Assigning global variables
	 */
	private void assingGlobals() {
		assignNeuralNetworks();
		ntrain = new TTT_NN_Trainer();
		whichAi = 0;
		whichGameMode = 0;
		field = new TTTField();
	}
	
	private void setUpWindow() {
		window = new JFrame("TicTacToe");
		setWindowBounds();
		window.setEnabled(true);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(null);
		addPanelsToWindow();
	}
	private void setWindowBounds() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		window.setBounds(width / 4, height / 4, width / 2, height / 2); 
	}
	
	private void addPanelsToWindow() {

		windowAdd_radioButtonPanels();
		window.add(create_TicTacToePanel());

		window.setResizable(false);
		
		window.revalidate();
		window.repaint();
	}
	
	private void windowAdd_radioButtonPanels() {
		JPanel aiPanel = create_radioButtonPanel_for_aiType();
		window.add(aiPanel);
		window.add(create_radioButtonPanel_for_gameMode(aiPanel));
	}
	
	private JPanel create_radioButtonPanel_for_gameMode(JPanel aiRadioPanel) {
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
					whichGameMode = b;
					if (whichGameMode == PLAYER_VS_PLAYER) {
						aiRadioPanel.setEnabled(false);
						aiRadioPanel.setVisible(false);
					} else {
						aiRadioPanel.setEnabled(true);
						aiRadioPanel.setVisible(true);
					}
				}

			});
		}

		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Game Typ"));
		panel.setBounds(window.getWidth() / 20, window.getHeight() / 20, window.getWidth() / 5, window.getHeight() / 5);
		return panel;
	}
	
	private JPanel create_radioButtonPanel_for_aiType() {
		JPanel buttonPanel = new JPanel();
		JRadioButton[] radioButton = create_AIradioButtonArray();
		
		addButtons_toA_ButtonGroup(radioButton);
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 1));
		for (int i = 0; i < radioButton.length; i++) {
			buttonPanel.add(radioButton[i]);
			radioButton[i].addActionListener(actionListener_that_changes_whichAi(i));
		}

		
		buttonPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ai Type"));
		buttonPanel.setBounds(window.getWidth() / 20, 6 * window.getHeight() / 20, window.getWidth() / 5,
				5 * window.getHeight() / 10);
		buttonPanel.setEnabled(true);
		return buttonPanel;
	}
	
	private JRadioButton[] create_AIradioButtonArray() {
		JRadioButton[] ai = new JRadioButton[4]; // radioButtons aiType
		ai[0] = new JRadioButton("Algorithm", true);
		ai[1] = new JRadioButton("NeuralNet 4 99.8%", false);
		ai[2] = new JRadioButton("NeuralNet 3 96.4%", false);
		ai[3] = new JRadioButton("NeuralNet 1 47.9%", false);
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
	
		fieldSurface = new JTTTFieldPanel();

		for (int i = 0; i < 9; i++) {
			fieldSurface.setMouseListener(i,newMouselistener_for_tttPanel(i));
		}
		
		fieldSurface.addListenerToButton(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fieldSurface.reset();
				field.setField_toStart();
				if (whichGameMode == 0) { // ai starts
					computerMove();
				}
			}

		});
		
		fieldSurface.setBounds(7 * window.getWidth() / 20, window.getHeight() / 20, 12 * window.getWidth() / 20,
				8 * window.getHeight() / 10);
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
				if(field.whoHasWon() == -1) {
					if (field.isFree(index)) {
						fieldSurface.paint(index, field.whosTurn());
						field.set(index);
						if (whichGameMode < 2) {
							computerMove();
						}
					}
				}else {
					JOptionPane.showMessageDialog(null, "Winner is: "+ field.toChar(field.whoHasWon()));
				}
			}
			@Override public void mouseEntered(MouseEvent e) {}@Override public void mouseExited(MouseEvent e) {}
		};
	}
	
	private ActionListener actionListener_that_changes_whichAi(final int buttonIndex) {
		return new ActionListener() {
			final int b = buttonIndex;

			public void actionPerformed(ActionEvent e) {
				whichGameMode = b;
			}
		};
	}

	/**
	 * Computer makes its move
	 * 
	 */
	private void computerMove() {
		if (field.whoHasWon() == -1) {
			int move =	calcPcMove();
			fieldSurface.paint(move,field.whosTurn());
			field.set(move);
		}
	}
	
	/**
	 * Computer calculates its move
	 * @return int move
	 */
	private int calcPcMove() {
		int move = 0;
		if (whichAi == 0) {
			move = ntrain.ai(field).lastMove();
		} else  {
			move = nn[whichAi-1].calcLast(field.toInputVector()).largestIndex(field.getMoves_left_to_make());
		} 
		return move;
	}
	
	private void assignNeuralNetworks() {
		nn = new NeuralNetwork[3];
		try {
			setUpNeuralNetworks_by_Files();
		} catch (Exception e) {
			e.printStackTrace();
			setUpNeuralNetworks_by_calculation();
		}
	}
	
	private void setUpNeuralNetworks_by_Files() {
		nn[0] = new NeuralNetwork("3_[27, 1000, 9].txt");
		nn[1] = new NeuralNetwork("3_[27, 100, 9].txt");
		nn[2] = new NeuralNetwork("2_[27, 9].txt");
	}
	
	private void setUpNeuralNetworks_by_calculation() {
		int[][] layers  = new int[3][];
		int[] l1 = {27,9};
		int[] l2 = {27,100,9};
		int[] l3 = {27,1000,9};

		layers[0] = l1;
		layers[1] = l2;
		layers[2] = l3;
		nn = ntrain.trainTicTacToe(layers, 1000, 0.3);
	}
}
