/**
 * 
 * @author Jusitn Horn
 *<br><br>
 *this is crap
 */
public class Tests {
	
	static int[] f = 	{7,3,7,
						0,7,3,
						3,7,3};
	
	public static void main(String[] args) {
		
		TTTHandler alg = new TTTHandler(f);
		Stats a =alg.ai(f);
		f = alg.set(f,a.moves.get(a.moves.size()-1));
		alg.displayField(f);
		System.out.println(a);
	}

}
