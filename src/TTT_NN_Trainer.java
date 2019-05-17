import java.io.BufferedWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author Justin Horn
 *<br><br>
 *Helps Setting up the NN for tictactoe
 */
public class TTT_NN_Trainer {

	protected List<int[]> listOfFields;
	private TTTHandler tttHandler;
	protected List<Vector> inputVectors;
	protected List<Vector> outputVectors;
	protected List<Stats> statList;

	public TTT_NN_Trainer() {
		tttHandler = new TTTHandler(new int[9]);
		tttHandler.setUpField();
		listOfFields = new ArrayList<int[]>();
		
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
			if (!(tttHandler.whoHasWon(listOfFields.get(i)) == -1) || !even(listOfFields.get(i))) {
				listOfFields.remove(i);
			} else {
				i++;
			}
		}
		
		inputVectors = new ArrayList<Vector>();
		outputVectors = new ArrayList<Vector>();
		statList = new ArrayList<Stats>();

		for (int j = 0; j < listOfFields.size(); j++) {
			inputVectors.add(fieldToInputVector(listOfFields.get(j)));
			statList.add(tttHandler.ai(listOfFields.get(j)));
			outputVectors.add(fieldToOutputVector(statList.get(j).lastMove()));
		}
		System.out.println("Werte wurden prepariert");
	}

	/**
	 * Counts if the amount of sevens and threes is possible in a real game. <br>
	 * --> As many sevens as threes or one more seven than threes <br>
	 * Helper method for {@link #generateFields(int[], int, int)}
	 * 
	 * @param field
	 * @return boolean
	 */
	public boolean even(int[] field) {
		int countX = 0;
		int countO = 0;
		for (int i = 0; i < field.length; i++) {
			if (field[i] == 3) {
				countO++;
			} else if (field[i] == 7) {
				countX++;
			}
		}
		return (countX == countO) || (countX - 1 == countO);
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
			listOfFields.add(field.clone());
			// printArray(field);
		} else if (deep + 1 < depth) {
			generateFields(field, deep + 1, depth);
		}
		field[deep] = 3;
		if (deep + 1 == depth) {
			listOfFields.add(field.clone());
			// printArray(field);
		} else if (deep + 1 < depth) {
			generateFields(field, deep + 1, depth);
		}
		field[deep] = 7;
		if (deep + 1 == depth) {
			listOfFields.add(field.clone());
			// printArray(field);
		} else if (deep + 1 < depth) {
			generateFields(field, deep + 1, depth);
		}
	}

	/**
	 * Turns field to a {@link Vector}
	 * 
	 * @param field int[]
	 * @return {@link Vector}
	 */
	public static Vector fieldToInputVector(int[] field) {
		double[] vector = new double[27];
		for (int i = 0; i < 27; i += 3) {
			vector[i] = 0.01;
			vector[i + 1] = 0.01;
			vector[i + 2] = 0.01;

			if (field[i / 3] == 0) {
				vector[i] = 0.99;
			} else if (field[i / 3] == 3) {
				vector[i + 1] = 0.99;
			} else if (field[i / 3] == 7) {
				vector[i + 2] = 0.99;
			}
		}
		Vector v = new Vector(27);
		v.arrayInit(vector);
		return v;

	}

	/**
	 * 
	 * @param field  int[]
	 * @param {@link Stats s}
	 * @return {@link Vector}
	 */
	public static Vector fieldToOutputVector(int move) {
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
