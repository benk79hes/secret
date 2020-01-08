package main;

import java.math.BigInteger;
import java.util.ArrayList;

class MathUtilities
{
	/**
	 *	INPUT a, b element de Z avec a >= b
	 *	OUTPUT g = gcd(a, b)
	 */
	static BigInteger multipleInverse(BigInteger a, BigInteger b)
	{
		ArrayList<BigInteger> r = new ArrayList<>();
		ArrayList<BigInteger> q = new ArrayList<>();
		ArrayList<BigInteger> x = new ArrayList<>();
		ArrayList<BigInteger> y = new ArrayList<>();

		r.add(a);
		r.add(b);

		x.add(BigInteger.valueOf(1));
		x.add(BigInteger.ZERO);

		q.add(BigInteger.ZERO);
		y.add(BigInteger.ZERO);
		y.add(BigInteger.valueOf(1));

		int i = 0;

		do {
			i++;

			q.add(i, r.get(i-1).divide(r.get(i)));

			r.add(i+1, r.get(i-1).subtract(r.get(i).multiply(q.get(i))));

			x.add(i+1, x.get(i-1).subtract(x.get(i).multiply(q.get(i))));
			y.add(i+1, y.get(i-1).subtract(y.get(i).multiply(q.get(i))));

		}
		while (r.get(i+1).compareTo(BigInteger.ZERO) > 0) ;

		BigInteger multInverse = y.get(i);

		while (multInverse.compareTo(BigInteger.ZERO) < 0) {
			multInverse = multInverse.add(a);
		}

		return multInverse;
	}
}
