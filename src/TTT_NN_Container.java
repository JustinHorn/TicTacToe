
/**
 * 
 * @author Justin Horn
 *
 */
public class TTT_NN_Container  {
	
	int score;
	double relativeScore;
	NeuralNetwork nn;

	public TTT_NN_Container(String path) {
		nn = new NeuralNetwork(path);
	}

	public TTT_NN_Container(int[] layers) {	
		nn = new NeuralNetwork(layers);
	}
	
	public TTT_NN_Container(NeuralNetwork n) {	
		nn = n;
	}
	
	
}
