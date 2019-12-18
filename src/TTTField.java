import main.java.de.phip1611.math.matrices.Vector;

/**
 * 
 * @author Justin Horn <p>
 *	Datastructure class for the TicTacToe field
 */
public class TTTField {

	public int[] field;
	public final int X = 7;
	public final int O = 3;
	
	public TTTField() {
		field = new int[9];
		setField_toStart();
	}
	
	/** field length needs to be 9;
	 *  0 = not used, 3 = O, 7 = X, X starts
	 * @param field int[]
	 */
	public TTTField(int[] field) {
		if(field == null) {
			throw new IllegalArgumentException("int[] field is null");
		} else if(field.length != 9) {
			throw new IllegalArgumentException("int[] field hat nicht die Laenge 9");
		}
		this.field = field;
	}
	
	/**
	 * 
	 * @return What is used in this TicTacToe Game
	 */
	public static int[] getTypes() {
		return new int[]{0,3,7};
	}
	
	/**
	 * Sets all ints of given field to 0
	 *
	 */
	public void setField_toStart() {
		for (int i = 0; i < 9; i++) {
			field[i] = 0;
		}
	}
	
	/**
	 * returns who has won.
	 * @param field int[]
	 * @return 7 = X, 3 = O, 0 = draw, -1 = not over
	 */
	public int whoHasWon() {
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
	
	public boolean isFree(int x) {
		return field[x] == 0;
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
	
	public int getInt_atIndex_X(int x) {
		if(x < 0 || 8 < x) {
			System.out.println("Error: x:"+ x+" length:"+9);
			throw new IndexOutOfBoundsException();
		}
		return field[x];
	}
	
	/**
	 * @param a int
	 */
	public void set(int index) {
		field[index] = whosTurn();
	}
	
	/**
	 * Change parameter field
	 * @param a int
	 */
	public void set(int index,int value) {
		field[index] = value;
	}
	
	/**
	 * returns whos move is now to play
	 * @param field int[]
	 * @return 7 -> X turn 3-> O turn
	 */
	public int whosTurn() {
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
	
	/**
	 * Counts if the amount of sevens and threes is possible in a real game. <br>
	 * --> As many sevens as threes or one more seven than threes <br>
	 * Helper method for {@link #generateFields(int[], int, int)}
	 * 
	 * @param field
	 * @return boolean
	 */
	public boolean even() {
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
	 * Turns field to a {@link Vector}
	 * 
	 * @param field int[]
	 * @return {@link Vector}
	 */
	public Vector toInputVector() {
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
	 * @param field
	 * @return
	 */
	public boolean[] possibleMoves() {
		boolean[] a = new boolean[field.length];
		for (int i = 0; i < a.length; i++) {
			a[i] = field[i] == 0;
		}
		return a;
	}
	
	public char toChar(int x) {
		if(x != 0 && x!= 3 && x!=7) {
			throw new IllegalArgumentException("Parameter is neither 0,3 nor 7! x:"+x);
		}
		if (x == 0) {
			return ' ';
		} else if (x == 3) {
			return 'O';
		}
		return 'X';
	}


}
