package main;

public class MainTest
{
	public static void main(String[] args)
	{
		/*for (int i = 0; i < 20; i ++){
			int r = MathUtilities.getRandomBetween(1, 2);
			System.out.println(r);
		} */

		int[] coeff = {7,7,4,7};

		for (int i = 0; i < 13; i++) {
			System.out.println(i + ", " + MathUtilities.computeY(i, coeff, 13));
			//break;
		}
	}
}
