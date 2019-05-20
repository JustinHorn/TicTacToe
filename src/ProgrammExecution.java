
public class ProgrammExecution {


	private Class_Connector c;
	private AiHandler aihandler;
	private JTTTFieldPanel panelToBePainted;
	private TTTField tttField;
	
	public ProgrammExecution(Class_Connector c) {	
		this.c = c;
		tttField = c.field;
		aihandler = new AiHandler(c);
		assignNeuralNetworks();
		panelToBePainted = c.fieldSurface;
	}


	
	/**
	 * Computer makes its move
	 * @throws Exception 
	 * 
	 */
	public int computerMove() {
		if (tttField.whoHasWon() == -1) {
			int move =	calcPcMove();
			panelToBePainted.paint(move, tttField.whosTurn());
			tttField.set(move);
			return move;
		}
		return -1;
	}
	
	
	/**
	 * Computer calculates its move
	 * @return int move
	 */
	private int calcPcMove() {
		int move = 0;
		if (c.whichAi == 0) {
			move = aihandler.ai(tttField).lastMove();
		} else  {
			move = aihandler.getMove_By_NeuralNetwork(c.whichAi-1, tttField);
		} 
		return move;
	}
	
	private void assignNeuralNetworks() {
		try {
			setUpNeuralNetworks_by_Files();
		} catch (Exception e) {
			e.printStackTrace();
			setUpNeuralNetworks_by_calculation();
		}
	}
	
	private void setUpNeuralNetworks_by_Files() {
		aihandler.addNeuralNet_by_File("3_[27, 1000, 9].txt");
		aihandler.addNeuralNet_by_File("3_[27, 100, 9].txt");
		aihandler.addNeuralNet_by_File("2_[27, 9].txt");
	}
	
	private void setUpNeuralNetworks_by_calculation() {
		int[] l1 = {27,9};
		int[] l2 = {27,100,9};
		int[] l3 = {27,1000,9};
		
		aihandler.addNeuralNet_by_Training(l1, 1000, 0.3);
		aihandler.addNeuralNet_by_Training(l2, 1000, 0.3);
		aihandler.addNeuralNet_by_Training(l3, 1000, 0.3);
	}
	

}
