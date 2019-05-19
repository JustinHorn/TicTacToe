import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class JTTTFieldPanel extends JPanel {

	private PaintPanel[] panel; 
	private JButton newGame;
	private JPanel field;
	
	public JTTTFieldPanel() {
		super();
		setLayout(null);
		
		panel = new PaintPanel[9];

		field = new JPanel();
		field.setLayout(new GridLayout(3, 3));
		newGame = new JButton("new Game");		
		
		for (int i = 0; i < panel.length; i++) {
			panel[i] = new PaintPanel();
			field.add(panel[i]);
			panel[i].setState(0);
		}
		//field.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "TicTacToe"));
		add(field);
		add(newGame);
		setBounds_of_FieldAndGame();
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "TicTacToe"));
	}
	
	public void addListenerToButton(ActionListener action) {
		newGame.addActionListener(action);
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		setBounds_of_FieldAndGame();
	}
	
	public void setBounds_of_FieldAndGame() {
		field.setBounds(this.getWidth()/20,this.getHeight()/20,this.getWidth()/10*9,this.getHeight()/10*8);
		newGame.setBounds(this.getWidth()/10,this.getHeight()/20*18,this.getWidth()/10*8,this.getHeight()/20);
	}
	
	public void set_by_Field(int[] field) {
		for (int i = 0; i < panel.length; i++) {
			panel[i].setState(field[i]);
			panel[i].repaint();
		}
	}
	
	public void reset() {
		for (int i = 0; i < panel.length; i++) {
			panel[i].setState(0);
			panel[i].repaint();
		}
	}
	
	
	
	public void paint(int fieldIndex, int fieldState) {
		panel[fieldIndex].setState(fieldState);
		panel[fieldIndex].repaint();
	}
	
	public void setMouseListener(int i, MouseListener m) {
			panel[i].addMouseListener(m);
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
