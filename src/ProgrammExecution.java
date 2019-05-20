
public class ProgrammExecution {


	private TestSurface_Execution_variableContainer c;
	private AiHandler aihandler;
	private JTTTFieldPanel panel;
	
	public ProgrammExecution(TestSurface_Execution_variableContainer c,JTTTFieldPanel p) {	
		this.c = c;
		aihandler = new AiHandler(c);
		assignNeuralNetworks();
		panel = p;
	}


	
	/**
	 * Computer makes its move
	 * @throws Exception 
	 * 
	 */
	public int computerMove() {
		if (c.field.whoHasWon() == -1) {
			int move =	calcPcMove();
			panel.paint(move, c.field.whosTurn());
			c.field.set(move);
			return move;
		}
		return -1;
	}
	
	public AiHandler getAi() {
		return aihandler;
	}
	
	/**
	 * Computer calculates its move
	 * @return int move
	 */
	private int calcPcMove() {
		int move = 0;
		System.out.println(c.whichAi);
		if (c.whichAi == 0) {
			move = aihandler.ai(c.field).lastMove();
		} else  {
			move = aihandler.getMove_By_NeuralNetwork(c.whichAi-1, c.field);
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
