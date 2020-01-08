package main;

import java.math.BigInteger;
import java.util.ArrayList;

class MathUtilities
{
	/**
	 * 	Multiplicatif inverse
	 *
	 *	INPUT a, b element de Z avec a >= b
	 *	OUTPUT inverse modulaire
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


	/**
	 * Fonction de calcul pour retrouver le secret et générer de nouvelles parts
	 *
	 * utiliser x = 0 pour retrouver le secret et nouveaux x pour de nouvelles parts
	 *
	 *
	 * @param x X de la fonction pour lequel retrouver le y
	 * @param shares parts du secret
	 * @param base base modulaire (nombre premier)
	 * @return y correspondant au x
	 */
	public static BigInteger computeYLagrange(BigInteger x, ArrayList<Share> shares, BigInteger base)
	{
		BigInteger result = BigInteger.ZERO;
		for(int i = 0; i < shares.size(); i++) {
			BigInteger rv = BigInteger.ONE;

			for (int j = 0; j < shares.size() ; j++) {
				if (j == i)
					continue;

				BigInteger xi = shares.get(i).getX();
				BigInteger xj = shares.get(j).getX();

				//BigInteger inv = modularSubstract(xi, xj, base).modInverse(base);
				BigInteger inv = multipleInverse(modularSubstract(xi, xj, base), base);

				rv = rv.multiply(modularSubstract(x, xj, base)).multiply(inv).mod(base);
			}

			result = result.add(rv.multiply(shares.get(i).getY())).mod(base);
		}

		return result;
	}


	/**
	 * Soustraction modulaire
	 *
	 * @param n1
	 * @param n2
	 * @param base
	 * @return résultat de la soustraction modulaire
	 */
	public static BigInteger modularSubstract(BigInteger n1, BigInteger n2, BigInteger base)
	{
		BigInteger result = n1.subtract(n2);
		if (result.compareTo(BigInteger.ZERO) < 0)
			result = result.add(base);

		return result;
	}
}
