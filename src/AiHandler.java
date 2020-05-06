
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.java.de.phip1611.math.matrices.NeuralNetwork;
import main.java.de.phip1611.math.matrices.Vector;
/**
 * 
 * @author Justin Horn <br>
 *         <br>
 *         Helps Setting up the NN for tictactoe
 */
public class AiHandler {

	private List<TTTField> listOfFields;
	private List<Vector> inputVectors;
	private List<Vector> outputVectors;
	private List<Stats> statList;
	protected List<NNContainer> neuralnetworks;
	private int[] types = { 0, 3, 7 };
	

	public AiHandler(SlideToGraphicClasses slide) {
		init();
		neuralnetworks = slide.neuralnetworks;
	}

	public AiHandler() {
		init();
		neuralnetworks = new ArrayList<NNContainer>();
	}

	public AiHandler(ArrayList<NNContainer> nnC) {
		init();
		neuralnetworks = nnC;
	}

	private void init() {
		listOfFields = new ArrayList<TTTField>();
		generateTrainingValues();
	}
	
	public void writeTrainingValuesToFile() {
		int l = inputVectors.size();
		try {
			File f = new File("trainData.txt");
			if(!f.exists()) {
				f.createNewFile();
			}
			BufferedWriter w = new BufferedWriter(new FileWriter(f));
			for(int i = 0; i < l;i++) {
				w.write(removeNewLinesFromString(inputVectors.get(i).toString()+"|||"));
				w.write(removeNewLinesFromString(outputVectors.get(i).toString()));
				w.newLine();
			}
			w.write(l+"");
			w.flush();
			w.close();
		}catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private String removeNewLinesFromString(String str) {
		return str.replaceAll(System.lineSeparator()+"", "");
	}

	/**
	 * Algorithm that calculates flawless/perfect next moves.
	 * <p>
	 * Tells also if in case of perfect play it is a loose, victory or a draw for
	 * any player.
	 * 
	 * @param field int[9]
	 *              </pre>
	 * @return {@link Stats}
	 */
	public Stats calcMove(TTTField field) {
		if (field.whoHasWon() == -1) {
			List<Stats> outcome = calcMovesBruteForce(field);
			outcome = sortMoves(outcome); 
			
			Stats stat = outcome.get(0);
			calcOpportunitys(stat,outcome);
			return stat;
		}
		// wenn Feld Unentschieden, dann 1 ansonsten sei es verloren also 0
		return new Stats((field.whoHasWon() == 0 ? 1 : 0));
	}
	
	private List<Stats> calcMovesBruteForce(TTTField field)  {
		List<Stats> outcome = new ArrayList<Stats>();
		for (int i = 0; i < 9; i++) {
			if (field.get(i) == 0) {
				field.set(i);
				Stats a = calcMove(field);
				a.invert(); // what is victory to one man is defeat to another
				a.moves.add(i);
				outcome.add(a);
				field.set(i, 0);
			}
		}
		return outcome;
	}
	
	/**
	 * Bubble sorts States List There surly is a easier way. But this was the one I
	 * first thought of
	 * 
	 * @param a List&ltStats&gt
	 * @return sorted List&ltStats&gt with greatest move on top
	 */
	private List<Stats> sortMoves(List<Stats> a) {
		int b = a.size();
		for (int i = 0; i < b; i++) {
			for (int j = 0; j < b - i - 1; j++) {
				if (a.get(j).total < a.get(j + 1).total
						|| (a.get(j).total == a.get(j + 1).total && a.get(j).score() < a.get(j + 1).score())) {
					Collections.swap(a, j, j + 1);
				}
			}
		}
		return a;
	}
	
	private void calcOpportunitys(Stats stat,List<Stats> outcome) {
		double w = stat.wins;
		double lo = stat.looses;
		for (Stats s : outcome) {
			stat.wins += s.wins;
			stat.looses += s.looses;
		}
		stat.wins -= w;
		stat.looses -= lo;
	}
	

	public void addNeuralNetByFile(String filePath) throws IllegalArgumentException {
		NNContainer container = new NNContainer(new NeuralNetwork(filePath));
		neuralnetworks.add(container);
		calcScoreOfContainer(container);
	}

	private void calcScoreOfContainer(NNContainer container) { // output argument?!
		NeuralNetwork n = container.nn;
		int score = 0;
		for (int i = 0; i < listOfFields.size(); i++) {
			int move = statList.get(i).lastMove();
			if (n.calcLast(inputVectors.get(i)).largestIndex() == move) {
				score++;
			}
		}
		container.score = score;
		container.relativeScore = ((double) score) / listOfFields.size();
		System.out.println("NN has a total score of: " + container.score + " a ratio of:" + container.relativeScore);

	}

	public void addNeuralNetByTraining(int[] layers, int epochs, double learningRate) {
		NeuralNetwork nn = new NeuralNetwork(layers);

		for (int e = 0; e < epochs; e++) { // epochen
			for (int i = 0; i < inputVectors.size(); i++) {
				nn.train(inputVectors.get(i), outputVectors.get(i), learningRate);
			}
		}
		System.out.println("Netz wurde trainiert");
		NNContainer container = new NNContainer(nn);
		neuralnetworks.add(container);

		calcScoreOfContainer(container);
	}

	public int getMoveByNeuralNetwork(int index, TTTField field) {
		NeuralNetwork n = neuralnetworks.get(index).nn;
		Vector output = n.calcLast(field.toInputVector());
		int move = output.largestIndex(field.possibleMoves());
		return move;
	}

	

	/**
	 * Generates all the values you need to train a TicTacToe NN.<br>
	 * InputVectors & target OutputVectors
	 */
	protected void generateTrainingValues() {
		generateFields(new int[9], 0, 9);
		int i = 0;
		while (i < listOfFields.size()) {
			if (!(listOfFields.get(i).whoHasWon() == -1) || !(listOfFields.get(i).even())) {
				listOfFields.remove(i);
			} else {
				i++;
			}
		}

		inputVectors = new ArrayList<Vector>();
		outputVectors = new ArrayList<Vector>();
		statList = new ArrayList<Stats>();

		for (int j = 0; j < listOfFields.size(); j++) {
			inputVectors.add(listOfFields.get(j).toInputVector());
			statList.add(calcMove(listOfFields.get(j)));
			outputVectors.add(moveToOutputVector(statList.get(j).lastMove()));
		}
		System.out.println("Werte wurden prepariert");
	}

	/**
	 * Recursive function that generates all possible TicTacToe fields and adds them
	 * to global ArrayList&ltint[]&gt {@link #fieldList}
	 * 
	 * @param field
	 * @param deep
	 * @param depth <br>
	 * @see #even(int[])
	 * @see TTTHandler#whoHasWon(int[])
	 *
	 */
	private void generateFields(int[] field, int deep, int depth) {
		for (int t : types) {
			field[deep] = t;
			if (deep + 1 == depth) {
				listOfFields.add(new TTTField(field.clone()));
				// printArray(field);
			} else if (deep + 1 < depth) {
				generateFields(field, deep + 1, depth);
			}
		}
	}

	/**
	 * @param field  int[]
	 * @param {@link Stats s}
	 * @return {@link Vector}
	 */
	public static Vector moveToOutputVector(int move) {
		double[] output = new double[9];
		for (int i = 0; i < 9; i++) {
			output[i] = 0.01;
		}
		output[move] = 0.99;
		Vector v = new Vector(9);
		v.arrayInit(output);
		return v;
	}
}
