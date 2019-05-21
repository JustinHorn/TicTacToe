import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class JTTTFieldPanel extends JPanel {

	private PaintPanel[] jpanel = new PaintPanel[9]; 
	private JButton button_newGame;
	private JPanel jField;
	private ConnectorOfGraphicClasses connector;
	
	public JTTTFieldPanel( ConnectorOfGraphicClasses c) {
		super();
		connector = c;

		setLayout(null);
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "TicTacToe"));

		setUpjField();
		
		setUp_button_newGame();

		fixBounds();
	}
	
	private void setUpjField() {
		jField = new JPanel();
		jField.setLayout(new GridLayout(3, 3));
		
		for (int i = 0; i <jpanel.length; i++) {
			jpanel[i] = new PaintPanel();
			jField.add(jpanel[i]);
			jpanel[i].setState(0);
			jpanel[i].addMouseListener(newMouselistener_for_tttPanel(i));
		}
		add(jField);
	}
	
	/**
	 * Tells the panel when it was clicked or not.
	 * @param panelIndex
	 * @return MouseListener that takes care of the move
	 */
	private MouseListener newMouselistener_for_tttPanel(final int panelIndex ) {
		return new MouseListener() {
			final int index = panelIndex;
			@Override
			public void mouseClicked(MouseEvent arg0) {
				makeMove(index);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				makeMove(index);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				makeMove(index);
			}
			
			@Override public void mouseEntered(MouseEvent e) {}@Override public void mouseExited(MouseEvent e) {}
		};
	}
	
	private void makeMove(int index) {
		TTTField f = connector.field;
		if(f.whoHasWon() == -1) {
			if (f.isFree(index)) {
				paint(index, f.whosTurn());
				f.set(index);
				if (connector.whichGameMode != connector.PLAYER_VS_PLAYER && f.whoHasWon() ==-1) {
					int move = connector.execution.computerMove();
					paint(move,f.whosTurn());
					f.set(move);
				}
			}
		}else {
			JOptionPane.showMessageDialog(null, "Winner is: "+ f.toChar(f.whoHasWon()));
		}
	}
	
	public void paint(int fieldIndex, int fieldState) {
		jpanel[fieldIndex].setState(fieldState);
		jpanel[fieldIndex].repaint();
	}

	private void setUp_button_newGame() {
		button_newGame = new JButton("new Game");		
		button_newGame.addActionListener(createActionListener_newGame());
		add(button_newGame);
	}
	
	private ActionListener createActionListener_newGame() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
				connector.field.setField_toStart();
				if (connector.whichGameMode == connector.COMPUTER_VS_PLAYER) { // ai starts
					int move = connector.execution.computerMove();
					paint(move,connector.field.whosTurn());
					connector.field.set(move);			
				}
			}
		};
	}
	
	public void reset() {
		for (int i = 0; i < jpanel.length; i++) {
			jpanel[i].setState(0);
			jpanel[i].repaint();
		}
	}
	
	/**
	 * Sets the Bounds for this JPanel within the window.<p>
	 * And the Bounds for jField and button_newGame within this jPanel
	 */
	public void fixBounds() {
		JFrame window = (JFrame) SwingUtilities.getRoot(this);
		if(window != null) {
			setBounds(7 * window.getWidth() / 20, window.getHeight() / 20, 12 * window.getWidth() / 20,
				8 * window.getHeight() / 10);
			jField.setBounds(this.getWidth()/20,this.getHeight()/20,this.getWidth()/10*9,this.getHeight()/10*8);
			button_newGame.setBounds(this.getWidth()/10,this.getHeight()/20*18,this.getWidth()/10*8,this.getHeight()/20);
		}
	}
	
	public void set_by_Field(int[] field) {
		for (int i = 0; i < jpanel.length; i++) {
			jpanel[i].setState(field[i]);
			jpanel[i].repaint();
		}
	}
	
}
