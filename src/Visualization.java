import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * 
 * @author Justin Horn<br><br>
 * Window and game.....
 */
public class Visualization {

	private JFrame window;
	private int width;
	private int height;
	private int aiStatus; //which ai shall make that move
	private int gTStatus;//what shall be played?
	private JPanel rPaT;
	private TTTHandler tttHandler;
	private PaintPanel[] jppaints;
	private NeuralNetwork[] nn;
	private TTT_NN_Trainer ntrain;

	/**
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		new Visualization();
	}

	public Visualization() {
		setUpNeuralNetworks();
		tttHandler = new TTTHandler();
		tttHandler.setUpField();
		aiStatus = 0;
		gTStatus = 0;
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		width = gd.getDisplayMode().getWidth();
		height = gd.getDisplayMode().getHeight();
		window = new JFrame("TicTacToe");
		window.setBounds(width / 4, height / 4, width / 2, height / 2); // first 2 == spawn point next 2 bounds
		window.setEnabled(true);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(null);

		JRadioButton[] jRBgT = new JRadioButton[3]; // radiobuttons gameTyp
		jRBgT[2] = new JRadioButton("Player Vs Player");
		jRBgT[1] = new JRadioButton("Player Vs Computer");
		jRBgT[0] = new JRadioButton("Computer Vs Player", true);

		ButtonGroup gameTyp = new ButtonGroup();

		JPanel rPgT = new JPanel();
		rPgT.setLayout(new GridLayout(3, 1));
		for (int i = 0; i < jRBgT.length; i++) {
			gameTyp.add(jRBgT[i]);
			rPgT.add(jRBgT[i]);
			final int j = i;
			jRBgT[i].addActionListener(new ActionListener() {
				final int b = j;

				@Override
				public void actionPerformed(ActionEvent arg0) {
					gTStatus = b;
					if (b == 2) {
						rPaT.setEnabled(false);
						rPaT.setVisible(false);
					} else {
						rPaT.setEnabled(true);
						rPaT.setVisible(true);
					}
				}

			});
		}

		rPgT.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Game Typ"));
		rPgT.setBounds(window.getWidth() / 20, window.getHeight() / 20, window.getWidth() / 5, window.getHeight() / 5);
		window.add(rPgT);

		JRadioButton[] ai = new JRadioButton[4]; // radioButtons aiType
		ai[0] = new JRadioButton("Algorithm", true);
		ai[1] = new JRadioButton("NeuralNet 4 99.8%", false);
		ai[2] = new JRadioButton("NeuralNet 3 96.4%", false);
		ai[3] = new JRadioButton("NeuralNet 1 47.9%", false);

		ButtonGroup aiType = new ButtonGroup();
		rPaT = new JPanel();
		rPaT.setLayout(new GridLayout(4, 1));
		for (int i = 0; i < ai.length; i++) {
			aiType.add(ai[i]);
			rPaT.add(ai[i]);
			final int j = i;
			ai[i].addActionListener(new ActionListener() {
				final int b = j;

				public void actionPerformed(ActionEvent e) {
					aiStatus = b;
				}
			});
		}

		rPaT.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ai Type"));
		rPaT.setBounds(window.getWidth() / 20, 6 * window.getHeight() / 20, window.getWidth() / 5,
				5 * window.getHeight() / 10);
		rPaT.setEnabled(true);
		window.add(rPaT);

		JPanel jPPaint = new JPanel(); // TicTacToe field
		jPPaint.setLayout(new GridLayout(3, 3));
		jppaints = new PaintPanel[9];

		for (int i = 0; i < 9; i++) {
			jppaints[i] = new PaintPanel();
			jppaints[i].setState(tttHandler.get(i));
			jPPaint.add(jppaints[i]);
			final int j = i;
			jppaints[i].addMouseListener(new MouseListener() {

				final int index = j;

				@Override
				public void mouseClicked(MouseEvent arg0) {
					if(tttHandler.whoHasWon() == -1) {
						if (tttHandler.get(index) == 0) {
							int s = tttHandler.whosTurn();
							jppaints[index].setState(s);
							tttHandler.set(index);
							if (gTStatus < 2) {
								computerMove();
							}
						}
					}else {
						JOptionPane.showMessageDialog(null, "Winner is: "+ intToChar(tttHandler.whoHasWon()));
					}
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mousePressed(MouseEvent e) {
					if(tttHandler.whoHasWon() == -1) {
						if (tttHandler.get(index) == 0) {
							int s = tttHandler.whosTurn();
							jppaints[index].setState(s);
							tttHandler.set(index);
							if (gTStatus < 2) {
								computerMove();
							}
						}
					}else {
						JOptionPane.showMessageDialog(null, "Winner is: "+ intToChar(tttHandler.whoHasWon()));
					}
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					if(tttHandler.whoHasWon() == -1) {
						if (tttHandler.get(index) == 0) {
							int s = tttHandler.whosTurn();
							jppaints[index].setState(s);
							tttHandler.set(index);
							if (gTStatus < 2) {
								computerMove();
							}
						}
					}else {
						JOptionPane.showMessageDialog(null, "Winner is: "+ intToChar(tttHandler.whoHasWon()));
					}
				}
			});
		}

		jPPaint.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "TicTacToe"));
		jPPaint.setBounds(7 * window.getWidth() / 20, window.getHeight() / 20, 12 * window.getWidth() / 20,
				65 * window.getHeight() / 100);

		window.add(jPPaint);

		JButton newGame = new JButton("new Game"); // new Game Button
		newGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < jppaints.length; i++) {
					jppaints[i].setState(0);
				}
				tttHandler.setUpField();
				if (gTStatus == 0) { // ai starts
					computerMove();
				}
			}

		});
		newGame.setBounds(8 * window.getWidth() / 20, 72 * window.getHeight() / 100, 10 * window.getWidth() / 20,
				window.getHeight() / 20);
		window.add(newGame);

		window.setResizable(false);
		
		window.revalidate();
		window.repaint();
	}

	/**
	 * Lets the pc calculate its move. <br>
	 * 
	 */
	public void computerMove() {
		if (tttHandler.whoHasWon() == -1) {
			int move = 0;
			if (aiStatus == 0) {
				move = tttHandler.ai(tttHandler.get()).lastMove();
			} else  {
				move = nn[aiStatus-1].calcLast(ntrain.fieldToInputVector(tttHandler.get())).largestIndex(fToInclude(tttHandler.get()));
			} 
			jppaints[move].setState(tttHandler.whosTurn());
			tttHandler.set(move);
		}
	}

	/**
	 * 
	 * @param field
	 * @return
	 */
	public boolean[] fToInclude(int[] field) {
		boolean[] a = new boolean[field.length];
		for (int i = 0; i < a.length; i++) {
			a[i] = field[i] == 0;
		}
		return a;
	}
	
	public void setUpNeuralNetworks() {
		ntrain = new TTT_NN_Trainer();
		nn = new NeuralNetwork[3];
		try {
			nn[0] = new NeuralNetwork("3_[27, 1000, 9].txt");
			nn[1] = new NeuralNetwork("3_[27, 100, 9].txt");
			nn[2] = new NeuralNetwork("2_[27, 9].txt");
		} catch (Exception e) {
			e.printStackTrace();
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
	public char intToChar(int x) {
		if (x == 0) {
			return ' ';
		} else if (x == 3) {
			return 'O';
		}
		return 'X';
	}
	
	

}
