import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * <h1> A TicTacToe field managment system with an Algorithm for the perfect move</h1>
 * @author Justin Horn
 * <br><br>
 * 	
 *  0 = not used, 3 = O, 7 = X, X starts
 */
public class TTTHandler {

	private int[] field;
	private final int X = 7;
	private final int O = 3;

		/** field length needs to be 9;
		 *  0 = not used, 3 = O, 7 = X, X starts
		 * @param field int[]
		 */
	public TTTHandler(int[] field) {
		if(field == null) {
			throw new IllegalArgumentException("int[] field is null");
		}
		this.field = field;
	}
	
	public TTTHandler() {
		this.field = new int[9];
		setUpField();
	}
	/**
	 * 
	 * @return What is used in this TicTacToe Game
	 */
	public static int[] getTypes() {
		return new int[]{0,3,7};
	}

	/**
	 * Algorithm that calculates flawless/perfect next moves.<br>
	 * Tells also if in case of perfect play it is a loose, victory or a draw for any player.
	 * @param field int[9] </pre>
	 * @return {@link Stats}
	 */
	public Stats ai(int[] field) {
		if (whoHasWon(field) == -1) {
			List<Stats> outcome = new ArrayList<Stats>();
			for (int i = 0; i < 9; i++) {
				if (field[i] == 0) {
					set(field,i);
					Stats a = ai(field);
					a.invert(); // what is victory to one man is defeat to another
					a.moves.add(i);
					outcome.add(a);
					field[i] = 0;
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
		return new Stats((whoHasWon(field) == 0 ? 1 : 0)); 
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
	 * Asks for next move (0-8) and returns 
	 * @param in Scanner
	 * @return move int
	 */
	public int askForMove(Scanner in) {
		if (in == null) {
			in = new Scanner(System.in);
		}
		System.out.println("Geben sie die Koordinate 0-8 ein:");
		return in.nextInt();
	}
	
	/**
	 * Change parameter field
	 * @param field int[] 
	 * @param a int
	 * @return field int[] 
	 */
	public int[] set(int[] field, int a) {
		field[a] = whosTurn(field);
		return field;
	}

	/**
	 * changes global field of object this
	 * @param a int
	 * @return this.field int[]
	 */
	public int[] set(int a) {
		field[a] = whosTurn(field);
		return field;
	}

	/**
	 * returns whos move is now to play
	 * @param field int[]
	 * @return 7 -> X turn 3-> O turn
	 */
	public int whosTurn(int[] field) {
		int countX = 0;
		int countO = 0;
		for (int i = 0; i < field.length; i++) {
			if (field[i] == 3) {
				countO++;
			} else if (field[i] == 7) {
				countX++;
			}
		}
		return countX <= countO ? 7 : 3;
	}
	public int whosTurn() {
		return whosTurn(field);
	}

	/**
	 * Prints field readable into the console
	 * @param field int[]
	 */
	public void displayField(int[] field) {
		System.out.println("------");
		for (int i = 0; i < 3; i++) {
			System.out.print("|");
			for (int j = 0; j < 3; j++) {
				System.out.print(field[j + i * 3] + "|");
			}
			System.out.println();
			System.out.println("------");
		}
	}

	/**
	 * adds all values together
	 * @param field int[]
	 * @return all values int
	 */
	public int value(int[] field) {
		int a = 0;
		int l = field.length;
		for (int i = 0; i < l; i++) {
			a += field[i];
		}
		return a;
	}

	/**
	 * 
	 * @return field int[] respectively the TicTacToe field
	 */
	public int[] get() {
		return field;
	}
	
	/**
	 * returns who has won.
	 * @param field int[]
	 * @return 7 = X, 3 = O, 0 = draw, -1 = not over
	 */
	public int whoHasWon(int[] field) {
		for (int i = 0; i < 3; i++) {
			if (field[i] + field[i + 3] + field[i + 6] == X*3 || field[i] + field[i + 3] + field[i + 6] == O*3) {
				return (field[i] + field[i + 3] + field[i + 6]) / 3; //
			}
		}
		for (int i = 0; i < 9; i += 3) {
			if (field[i] + field[i + 1] + field[i + 2] == X*3 || field[i] + field[i + 1] + field[i + 2] == O*3) {
				return (field[i] + field[i + 1] + field[i + 2]) / 3; //
			}
		}

		if (field[0] + field[4] + field[8] == X*3 || field[0] + field[4] + field[8] == O*3) {
			return (field[0] + field[4] + field[8]) / 3;
		}
		if (field[2] + field[4] + field[6] == X*3 || field[2] + field[4] + field[6] == O*3) {
			return (field[2] + field[4] + field[6]) / 3;
		}
		if (value(field) == X*5+O*4) {
			return 0;
		} else {
			return -1;
		}
	}
	/**
	 * returns who has won. In this Programm.
	 * @return 7 = X, 3 = O, 0 = draw, -1 = not over
	 */
	public int whoHasWon() {
		return whoHasWon(field);
	}

	/**
	 * Sets all ints of given field to 0
	 * @param field int[]
	 * @return field int[]
	 */
	public int[] setUpField(int[] field) {
		int a = field.length;
		for (int i = 0; i < a; i++) {
			field[i] = 0;
		}
		return field;
	}

	/**
	 * Returns a field[9] with all ints = 0;
	 * @return field int[]
	 */
	public int[] setUpField() {
		return setUpField(field);
	}
	
	public int get(int x) {
		if(x < 0 || 8 < x) {
			System.out.println("Error: x:"+ x+" length:"+9);
			throw new IndexOutOfBoundsException();
		}
		return field[x];
	}

}
