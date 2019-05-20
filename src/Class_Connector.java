import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class Class_Connector {

	protected TTTField field;
	protected int whichAi;
	protected int whichGameMode;
	protected List<NN_Container> neuralnetworks;
	protected ProgrammExecution execution;
	protected JTTTFieldPanel fieldSurface;
	protected JFrame testSurface;
	
	public static final int COMPUTER_VS_PLAYER = 0;
	public static final int PLAYER_VS_COMPUTER = 1;
	public static final int PLAYER_VS_PLAYER = 2;
	

	public Class_Connector() {
		whichAi = 0;
		whichGameMode = 0;
		field = new TTTField();
		neuralnetworks = new ArrayList<NN_Container>();
		execution = new ProgrammExecution(this);

		testSurface = new TestSurface(this);
		fieldSurface = new JTTTFieldPanel(this);
		testSurface.revalidate();
		testSurface.repaint();
	}

	public double getRelativScore_of_NN(int index) {
		return neuralnetworks.get(index).relativeScore;
	}
}
