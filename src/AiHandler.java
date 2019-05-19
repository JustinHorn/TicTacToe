
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 
 * @author Justin Horn
 *<br><br>
 *Helps Setting up the NN for tictactoe
 */
public class AiHandler {

	protected List<TTTField> listOfFields;
	protected List<Vector> inputVectors;
	protected List<Vector> outputVectors;
	protected List<Stats> statList;
	
	public AiHandler() {
		listOfFields = new ArrayList<TTTField>();
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
	
	/**
	 * Bubble sorts States List There surly is a easier way. But this was the one I
	 * first thought of
	 * 
	 * @param a List&ltStats&gt 
	 * @return sorted List&ltStats&gt  with greatest move on top
	 */
	public List<Stats> bSort(List<Stats> a) {
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
	 * Trains Networks to learn TicTacToe
	 * 
	 * @param layers    int[][] Array with layer order for each network (has to
	 *                  start with 27 and end with 9)
	 * @param epos      how often a the network shall be trained
	 *
	 * @param trainRate rate at wich a Net should be trained
	 * @return NeuralNetwork[] - trained networks
	 * @throws InterruptedException
	 */
	public NeuralNetwork[] trainTicTacToe(int[][] layers, int epos, double trainRate) {
		if(inputVectors == null ||outputVectors ==null||statList==null) {
			generateTrainingValues();
		}
		int ll = layers.length;

		NeuralNetwork[] nns = new NeuralNetwork[ll];

		for (int i = 0; i < ll; i++) {
			nns[i] = new NeuralNetwork(layers[i]);
		}

		for (int e = 0; e < epos; e++) { // epochen
			for (int i = 0; i < inputVectors.size(); i++) {
				for (int j = 0; j < ll; j++) {
					nns[j].train(inputVectors.get(i), outputVectors.get(i), trainRate);
				}
			}
		}
		System.out.println("Netze wurden trainiert");

		int[] score = new int[ll];

		for (int j = 0; j < ll; j++) {
			score[j] = 0;
		}
		for (int i = 0; i < listOfFields.size(); i++) {
			int move = statList.get(i).lastMove();
			for (int j = 0; j < ll; j++) {
				if (nns[j].calcLast(inputVectors.get(i)).largestIndex() == move) {
					score[j]++;
				}
			}
		}
		for (int j = 0; j < ll; j++) {
			System.out.println(
					"NN has a total score of: " + score[j] + " a ratio of:" + (((double) score[j]) / listOfFields.size()));
		}
		return nns;


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
	public void generateFields(int[] field, int deep, int depth) {
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
