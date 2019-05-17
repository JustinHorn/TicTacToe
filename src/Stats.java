import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Justin Horn <br><br>
 *	Keeps track of information and moves to play for {@link Algorithmus#ai(int[])}
 */
public class Stats {
	
	 List<Integer> moves;
	 int total;
	 double wins,looses;
	
	
	/**
	 * 0 = loose
	 *  1 = draw
	 *  win  = 2; but cant happen
	 * @param loseOrDraw
	 */
	public Stats(int loseOrDraw) {
		moves = new ArrayList<Integer>(9);
		total = loseOrDraw;
		wins = 0;
		looses = loseOrDraw == 0? 1:0;
	}
	/**
	 * 
	 * @return last move in moves List&ltInteger&gt <br>
	 * 	respectivly the move of the algorithm
	 */
	public int lastMove() {
		return moves.get(moves.size()-1);
	}
	
	
	public String toString() {
		String a = "";
		for(Integer b: moves) {
			a+=b +"|";
		}
		return "moves: "+ a + " total: "+ total+" wins: "+ wins+" looses: "+looses +" score: "+ score();
	}
	
	/**
	 * compact to String Method
	 * @return Information: last move,total,wins,looses
	 */
	public String toSample() {
		return lastMove()+","+total+","+wins+","+looses;
	}
	
	public double score() {
		if(0 < wins || 0 < looses) {
			return (wins-looses)/(wins+looses);
		} else {
			return 0;
		}
	}
	
	/**
	 * One mans victory is another mans defeat!<br>
	 * Therefor it is useful to invert the information in the object
	 */
	public void invert() {
		wins +=looses;
		looses = wins - looses;
		wins -=looses;
		
		total = total == 1? 1:total == 0?2:0;
	}
	
}
