import main.java.de.phip1611.math.matrices.NeuralNetwork;

/**
 * 
 * @author Justin Horn
 *
 */
public class NN_Container  {
	
	int score;
	double relativeScore;
	NeuralNetwork nn;

	public NN_Container(String path) {
		nn = new NeuralNetwork(path);
	}

	public NN_Container(int[] layers) {	
		nn = new NeuralNetwork(layers);
	}
	
	public NN_Container(NeuralNetwork n) {	
		nn = n;
	}
	
	
}
