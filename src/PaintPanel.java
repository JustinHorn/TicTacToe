import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * 
 * @author Justin Horn
 * Paints the shit
 *
 */
public class PaintPanel extends JPanel {

	private int state;

	public PaintPanel() {
		super();
		state = 0;
		this.setBackground(Color.white);
		setBorder(BorderFactory.createLineBorder(Color.black));
	}

	/**
	 * Sets the state of the TicTacToe field:<br>
	 * X<br>
	 * O<br>
	 * - 
	 * @param x
	 * @return
	 */
	public int setState(int x) {
		state = x;
		repaint();
		return x;
	}

	public int getState() {
		return state;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
	    g2.setStroke(new BasicStroke(3));
		int w = this.getWidth();
		int h = this.getHeight();
		if (state == 3) {
			g2.setColor(Color.red);
			g2.drawOval(w/20, h/20, 9*w/10, 9*h/10);
		} else if (state == 7) {
			g2.setColor(Color.blue);
			g2.drawLine(w/20, h/20, 9*w/10, 9*h/10);
			g2.drawLine(9*w/10, h/20, w/20, 9*h/10);
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
