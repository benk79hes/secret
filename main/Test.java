package main;

import java.math.BigInteger;

public class Test
{
	public static void main(String[] args)
	{
		/* int g = Secret.gcd(561, 44);
		System.out.println(g);

		g = Secret.gcd(59, 12);
		System.out.println(g); */

		BigInteger n = Secret.multipleInverse(BigInteger.valueOf(65537), BigInteger.valueOf(2019));
		System.out.println(n.toString());

	}
}
