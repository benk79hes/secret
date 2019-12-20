package main;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.security.SecureRandom;

public class Secret
{
	private BigInteger secret;
	private int byteLength;
	private int level;

	private BigInteger modularBase;
	private ArrayList<BigInteger> yList = new ArrayList<>() ;
	private ArrayList<BigInteger> xList = new ArrayList<>() ;
	private ArrayList<BigInteger> coeff = new ArrayList<>() ;


	public Secret()
	{
	}

	public void generateSecret(int byteLength, int level)
	{
		if (byteLength < 16 || byteLength > 512)
			throw new IllegalArgumentException("La clé doit être entre 16 et 512 bytes");

		this.byteLength = byteLength;
		this.level = level;

		Random rnd = new Random();

		modularBase = BigInteger.probablePrime(this.byteLength, rnd);

		int attempts = 0;
		do {
			secret = generateRandomNumber();
			attempts++;
		} while (modularBase.compareTo(secret) != 1 && !secret.testBit(0));

		generateMainFunction();

	}


	private BigInteger generateRandomNumber() throws IllegalArgumentException {
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[byteLength]; // 128 bits are converted to 16 bytes;
		random.nextBytes(bytes);
		return new BigInteger(bytes);
	}



	public void generateMainFunction()
	{
		coeff.add(secret);

		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[16];

		for (int i = 0; i < level; i++) {
			random.nextBytes(bytes);
			coeff.add(new BigInteger(bytes));
		}
	}


	public void makeNewPoint()
	{
		BigInteger newX, newY;
		byte[] newBytes = new byte[16];
		SecureRandom random = new SecureRandom();

		do {
			random.nextBytes(newBytes);
			newX = new BigInteger(newBytes);
		}
		while (xList.contains(newX));

		xList.add(newX);
		newY = computeY(newX);
		yList.add(newY);
	}



	/**
	 *	INPUT a, b element de Z avec a >= b
	 *	OUTPUT g = gcd(a, b)
	 */
	public static int gcd(int a, int b)
	{
		if (b > a)
			return gcd(b, a);

		int q, r, g;

		r = b;
		do {
			g = r;

			q = a / b;
			r = a - b * q;

			// prepare for next iteration
			a = b;
			b = r;

		} while (r > 0);

		return g;
	}

	/**
	 *	INPUT a, b element de Z avec a >= b
	 *	OUTPUT g = gcd(a, b)
	 */
	public static BigInteger multipleInverse(BigInteger a, BigInteger b)
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

	public static void printBiginteger(ArrayList<BigInteger> r)
	{

		for (BigInteger n : r) {
			System.out.print("\t" + n.toString());

		}

		System.out.println();
	}






	public BigInteger computeY(BigInteger x)
	{
		BigInteger y;

		y = BigInteger.valueOf(0);

		for (int i = 0; i < coeff.size(); i++) {
			BigInteger c = coeff.get(i);
			y = y.add(c.multiply(x.pow(i)));
		}

		return y;
	}

	public void showPoints()
	{
		for (int i = 0; i < xList.size(); i++) {
			System.out.println("x: " + xList.get(i));
			System.out.println("y: " + yList.get(i));
			System.out.println("---------------------------------------------");
		}
	}


}
