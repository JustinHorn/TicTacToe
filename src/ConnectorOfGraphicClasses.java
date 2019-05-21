import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

/**
 * 
 * @author Justin Horn
 *	Saves all values and is passed around as private variable
 */
public class ConnectorOfGraphicClasses {

	protected TTTField field;
	protected int whichAi;
	protected int whichGameMode;
	protected List<NN_Container> neuralnetworks;
	protected ProgrammExecution execution;
	protected JFrame testSurface;
	
	public static final int COMPUTER_VS_PLAYER = 0;
	public static final int PLAYER_VS_COMPUTER = 1;
	public static final int PLAYER_VS_PLAYER = 2;
	

	public ConnectorOfGraphicClasses() {
		whichAi = 0;
		whichGameMode = 0;
		neuralnetworks = new ArrayList<NN_Container>();
	}
	
	public double getRelativScore_of_NN(int index) {
		return neuralnetworks.get(index).relativeScore;
	}
}
