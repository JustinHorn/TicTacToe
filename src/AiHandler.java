
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sun.javafx.binding.StringFormatter;


/**
 * 
 * @author Justin Horn
 *<br><br>
 *Helps Setting up the NN for tictactoe
 */
public class AiHandler {

	private List<TTTField> listOfFields;
	private List<Vector> inputVectors;
	private List<Vector> outputVectors;
	private List<Stats> statList;
	private TestSurface_Execution_variableContainer c;
	
	public AiHandler(TestSurface_Execution_variableContainer c) {
		listOfFields = new ArrayList<TTTField>();
		generateTrainingValues();
		this.c = c;

	}
	/**
	 * Algorithm that calculates flawless/perfect next moves.<br>
	 * Tells also if in case of perfect play it is a loose, victory or a draw for any player.
	 * @param field int[9] </pre>
	 * @return {@link Stats}
	 */
	public Stats ai(TTTField field) {
		if (field.whoHasWon() == -1) {
			List<Stats> outcome = new ArrayList<Stats>();
			for (int i = 0; i < 9; i++) {
				if (field.getInt_atIndex_X(i) == 0) {
					field.set(i);
					Stats a = ai(field);
					a.invert(); // what is victory to one man is defeat to another
					a.moves.add(i);
					outcome.add(a);
					field.set(i,0);
				}
			}
			bSort(outcome); //Sortiert besten zug nach oben
			Stats stat = outcome.get(0);
			double w = stat.wins;
			double lo = stat.looses;
			for (Stats s : outcome) {
				stat.wins += s.wins;
				stat.looses += s.looses;
			}
			stat.wins -= w;
			stat.looses -= lo;
			return stat;
		}
		// wenn Feld Unentschieden, dann 1 ansonsten sei es verloren also 0
		return new Stats((field.whoHasWon() == 0 ? 1 : 0));
	}
	
	public void addNeuralNet_by_File(String filePath) throws IllegalArgumentException {
		NN_Container container = new NN_Container(new NeuralNetwork(filePath));
		c.neuralnetworks.add(container);
		calcScore_of_Container(container);
	}
	
	private void calcScore_of_Container(NN_Container container) { // output argument?!
		NeuralNetwork n = container.nn;
		int score = 0;
		for (int i = 0; i < listOfFields.size(); i++) {
			int move = statList.get(i).lastMove();
				if (n.calcLast(inputVectors.get(i)).largestIndex() == move) {
					score++;
				}
		}
		container.score = score;
		container.relativeScore = ((double)score)/listOfFields.size();
		System.out.println("NN has a total score of: " + container.score + " a ratio of:" + container.relativeScore);

	}
	
	public void addNeuralNet_by_Training(int[] layers, int epochs, double learningRate) {
		NeuralNetwork nn = new NeuralNetwork(layers);


		for (int e = 0; e <epochs; e++) { // epochen
			for (int i = 0; i < inputVectors.size(); i++) {
					nn.train(inputVectors.get(i), outputVectors.get(i), learningRate);
			}
		}
		System.out.println("Netz wurde trainiert");
		NN_Container container = new NN_Container(nn);
		c.neuralnetworks.add(container);
		
		calcScore_of_Container(container);		
	}
	
	
	public int getMove_By_NeuralNetwork(int index, TTTField field) {
		NeuralNetwork n = c.neuralnetworks.get(index).nn;
		Vector output = n.calcLast(field.toInputVector());
		int move = output.largestIndex(field.possibleMoves());
		return move;
	}
	
	/**
	 * Bubble sorts States List There surly is a easier way. But this was the one I
	 * first thought of
	 * 
	 * @param a List&ltStats&gt 
	 * @return sorted List&ltStats&gt  with greatest move on top
	 */
	private List<Stats> bSort(List<Stats> a) {
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
			statList.add(ai(listOfFields.get(j)));
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
		field[deep] = 0;
		if (deep + 1 == depth) {
			listOfFields.add(new TTTField(field.clone()));
			// printArray(field);
		} else if (deep + 1 < depth) {
			generateFields(field, deep + 1, depth);
		}
		field[deep] = 3;
		if (deep + 1 == depth) {
			listOfFields.add(new TTTField(field.clone()));
			// printArray(field);
		} else if (deep + 1 < depth) {
			generateFields(field, deep + 1, depth);
		}
		field[deep] = 7;
		if (deep + 1 == depth) {
			listOfFields.add(new TTTField(field.clone()));
			// printArray(field);
		} else if (deep + 1 < depth) {
			generateFields(field, deep + 1, depth);
		}
	}



	/**
	 * 
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
