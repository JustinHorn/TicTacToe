import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class JTTTFieldPanel extends JPanel {

	public PaintPanel[] jpanel; 
	public JButton button_newGame;
	public JPanel field;
	
	public JTTTFieldPanel(JFrame window) {
		super();
		setLayout(null);
		
		jpanel = new PaintPanel[9];

		field = new JPanel();
		field.setLayout(new GridLayout(3, 3));
		button_newGame = new JButton("new Game");		
		
		for (int i = 0; i <jpanel.length; i++) {
			jpanel[i] = new PaintPanel();
			field.add(jpanel[i]);
			jpanel[i].setState(0);
		}
		//field.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "TicTacToe"));
		add(field);
		add(button_newGame);
		setBounds(7 * window.getWidth() / 20, window.getHeight() / 20, 12 * window.getWidth() / 20,
				8 * window.getHeight() / 10);
		setBounds_of_FieldAndGame();
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "TicTacToe"));
	}
	

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		setBounds_of_FieldAndGame();
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

	public JTTTFieldPanel(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	public JTTTFieldPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public JTTTFieldPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}
}
