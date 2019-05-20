import java.util.ArrayList;
import java.util.List;

public class TestSurface_Execution_variableContainer {

	protected TTTField field;
	protected int whichAi;
	protected int whichGameMode;
	protected List<NN_Container> neuralnetworks;

	
	public static final int COMPUTER_VS_PLAYER = 0;
	public static final int PLAYER_VS_COMPUTER = 1;
	public static final int PLAYER_VS_PLAYER = 2;
	
	public TestSurface_Execution_variableContainer() {
		whichAi = 0;
		whichGameMode = 0;
		field = new TTTField();
		neuralnetworks = new ArrayList<NN_Container>();
	}
	
	public double getRelativScore_of_NN(int index) {
		return neuralnetworks.get(index).relativeScore;
	}

}
