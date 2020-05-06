import main.java.de.phip1611.math.matrices.NeuralNetwork;

/**
 * 
 * @author Justin Horn
 *
 */
public class NNContainer  {
	
	int score;
	double relativeScore;
	NeuralNetwork nn;

	public NNContainer(String path) {
		nn = new NeuralNetwork(path);
	}

	public NNContainer(int[] layers) {	
		nn = new NeuralNetwork(layers);
	}
	
	public NNContainer(NeuralNetwork n) {	
		nn = n;
	}
	
	
}
