
public class ExecuterOfGraphic {

	private SlideToGraphicClasses s;
	private AiHandler aihandler;

	public ExecuterOfGraphic() {
		s = new SlideToGraphicClasses();
		s.execution = this;

		s.field =  new TTTField();
		aihandler = new AiHandler(s);
		assignNeuralNetworks();

		new GraphicTTT(s);
	}

	/**
	 * R
	 * @throws Exception 
	 * 
	 */
	public int computerMove() {
		if (s.field.whoHasWon() == -1) {
			int move =	calcPcMove();
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
		if (s.whichAi == 0) {
			move = aihandler.calcMove(s.field).lastMove();
		} else  {
			move = aihandler.getMoveByNeuralNetwork(s.whichAi-1, s.field);
		} 
		return move;
	}
	
	private void assignNeuralNetworks() {
		try {
			setUpNeuralNetworksByFiles();
		} catch (Exception e) {
			e.printStackTrace();
			setUpNeuralNetworksByCalculation();
		}
	}
	
	private void setUpNeuralNetworksByFiles() {
		aihandler.addNeuralNetByFile("3_[27, 1000, 9].txt");
		aihandler.addNeuralNetByFile("3_[27, 100, 9].txt");
		aihandler.addNeuralNetByFile("2_[27, 9].txt");
	}
	
	private void setUpNeuralNetworksByCalculation() {
		int[] l1 = {27,9};
		int[] l2 = {27,100,9};
		int[] l3 = {27,1000,9};
		
		aihandler.addNeuralNetByTraining(l1, 1000, 0.3);
		aihandler.addNeuralNetByTraining(l2, 1000, 0.3);
		aihandler.addNeuralNetByTraining(l3, 1000, 0.3);
	}
	

}
