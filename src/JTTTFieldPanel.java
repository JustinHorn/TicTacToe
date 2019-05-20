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

public class JTTTFieldPanel extends JPanel {

	public PaintPanel[] jpanel; 
	public JButton button_newGame;
	public JPanel field;
	private Class_Connector connector;
	private TTTField tttField;
	private JFrame window;
	
	public JTTTFieldPanel( Class_Connector c) {
		super();
		setLayout(null);
		connector = c;
		window = connector.testSurface;
		tttField = connector.field;
		
		jpanel = new PaintPanel[9];

		field = new JPanel();
		field.setLayout(new GridLayout(3, 3));
		
		for (int i = 0; i <jpanel.length; i++) {
			jpanel[i] = new PaintPanel();
			field.add(jpanel[i]);
			jpanel[i].setState(0);
			jpanel[i].addMouseListener(newMouselistener_for_tttPanel(i));
		}
		//field.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "TicTacToe"));
		add(field);
		
		button_newGame = new JButton("new Game");		
		button_newGame.addActionListener(createActionListener_newGame());
	
		add(button_newGame);
		setBounds(7 * window.getWidth() / 20, window.getHeight() / 20, 12 * window.getWidth() / 20,
				8 * window.getHeight() / 10);
		setBounds_of_FieldAndGame();
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "TicTacToe"));

		connector.testSurface.add(this);

	}
	

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		//setBounds_of_FieldAndGame();
	}
	
	public void setBounds_of_FieldAndGame() {
		
		field.setBounds(this.getWidth()/20,this.getHeight()/20,this.getWidth()/10*9,this.getHeight()/10*8);
		button_newGame.setBounds(this.getWidth()/10,this.getHeight()/20*18,this.getWidth()/10*8,this.getHeight()/20);
	}
	
	public void set_by_Field(int[] field) {
		for (int i = 0; i < jpanel.length; i++) {
			jpanel[i].setState(field[i]);
			jpanel[i].repaint();
		}
	}
	
	public void reset() {
		for (int i = 0; i < jpanel.length; i++) {
			jpanel[i].setState(0);
			jpanel[i].repaint();
		}
	}
		public void paint(int fieldIndex, int fieldState) {
		jpanel[fieldIndex].setState(fieldState);
		jpanel[fieldIndex].repaint();
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
					if(tttField.whoHasWon() == -1) {
						if (tttField.isFree(index)) {
							paint(index, tttField.whosTurn());
							tttField.set(index);
							if (connector.whichGameMode != connector.PLAYER_VS_PLAYER) {
								connector.execution.computerMove();
							}
						}
					}else {
						JOptionPane.showMessageDialog(null, "Winner is: "+ tttField.toChar(tttField.whoHasWon()));
					}
				}
				@Override public void mouseEntered(MouseEvent e) {}@Override public void mouseExited(MouseEvent e) {}
			};
		}
		
		public ActionListener createActionListener_newGame() {
			return new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					reset();
					tttField.setField_toStart();
					if (connector.whichGameMode == connector.COMPUTER_VS_PLAYER) { // ai starts
						connector.execution.computerMove();
					}
				}

			};
		}

}
