package main;

import java.math.BigInteger;
import java.util.ArrayList;

public class Examen
{

	public static void main(String[] args) {
		// write your code here
		BigInteger base;

		System.out.println("Votre entreprise utilise le *Shamir's secret sharing* étudié dans ce cours.");
		System.out.println("Toute l'arithmétique est effectuée modulo le nombre premier p = ? :");

		int baseInt = CommandLineInterface.readCommandInt();
		base = BigInteger.valueOf(baseInt);

		System.out.println("Toute l'arithmétique est effectuée modulo le nombre premier p = ? :");
		System.out.println("Le seuil pour reconstruire le polynôme d'interpolation f(x) est fixé à combien de parts ? :");


		int levelInt = CommandLineInterface.readCommandInt();
		System.out.println("Indiquez " + levelInt + " parts de secret:");

		int[][] shares = new int[levelInt][2];

		for (int i = 0; i < levelInt; i++) {
			int[] part = new int[2];
			System.out.println("-------------------------");
			System.out.println("Part " + (i+1));
			System.out.println("Valeur de x: ");
			part[0] = CommandLineInterface.readCommandInt();
			System.out.println("Valeur de y: ");
			part[1] = CommandLineInterface.readCommandInt();
			shares[i] = part;
		}

		System.out.println("--------------------------------------------------");
		System.out.println("-  SOLUTION                                      -");
		System.out.println("--------------------------------------------------");
		System.out.println("");


		System.out.println("- Quel est le degré du polynôme d'interpolation f(x) permettant de calculer le secret S ?");
		System.out.println("");

		int degrePolynome = levelInt -1;
		System.out.println("\tLe degré du polynôme est " + degrePolynome + " (seuil pour reconstruire - 1)." );
		System.out.println("");



		System.out.println("");
		System.out.println("- Quel est le secret ?");



		System.out.println("");
		System.out.println("  - Quel système d'équations devez-vous résoudre (plusieurs réponses possibles) ?");
		System.out.println("");

		for (int s = 0; s < levelInt; s++) {
			int x = shares[s][0];
			int y = shares[s][1];

			String funct = "\t";
			int letter = (int)'a';

			for (int p = 0; p < levelInt; p++) {

				if (p == 0) {
					funct += (char)letter;

				} else {
					funct += " + " +(char)letter + "*" + x ;

					if (p > 1)
						funct += "^" + p ;
				}
				letter ++;
			}

			funct += " = " + y;
			System.out.println(funct);
		}

		System.out.println();



		System.out.println();
		System.out.println("  - Utilisez l'interpolation de Lagrange pour calculer le secret. Donnez les équations des fonctions l_j(x) et utilisez la bibliothèque Java écrite pour votre projet afin de trouver S.");
		System.out.println();

		for(int i = 0; i < shares.length; i++) {
			int ptCount = 0;
			String funct = "\tl_" + i + "(x) = ";
			for (int j = 0; j < shares.length ; j++) {
				if (j == i)
					continue;

				int xi = shares[i][0];
				int xj = shares[j][0];

				if (ptCount > 0)
					funct += " * ";
				funct += "(x - " + xj + ") / (" + xi+ " - " + xj + ")";
				ptCount++;

			}
			System.out.println(funct);
		}

		String secret = findSecret(baseInt, shares);
		System.out.println();
		System.out.println("\tLe secret est: " + secret);
		System.out.println();

		System.out.println("Points du graphique:");

		findAllY(baseInt, shares);


		System.out.println("Nouveau seuil pour reconstruire g(x): ");
		int seuil = CommandLineInterface.readCommandInt();

		int[] coeff = new int[seuil];
		coeff[0] = Integer.parseInt(secret);
		for (int i = 1; i < seuil; i++) {
			coeff[i] = MathUtilities.getRandomBetween(1, baseInt -1);
		}

		String funct = "\tg(x) = ";
		//int letter = (int)'a';

		for (int p = 0; p < seuil; p++) {

			if (p == 0) {
				funct += coeff[p];

			} else {
				funct += " + " +coeff[p] + "*x" ;

				if (p > 1)
					funct += "^" + p ;
			}
			//letter ++;
		}

		//funct += " = " + y;
		System.out.println(funct);

		System.out.println();
		System.out.println("Points du graphique: ");

		for (int i = 0; i < baseInt; i ++) {
			System.out.println(i + ", " + MathUtilities.computeY(i, coeff, baseInt));
		}


	}

	public static String findSecret(int base, int[][]parts) {
		ArrayList<Share> shares = new ArrayList<>();

		for (int i = 0; i < parts.length; i++) {
			Share s = new Share();
			s.setX(BigInteger.valueOf(parts[i][0]));
			s.setY(BigInteger.valueOf(parts[i][1]));
			shares.add(s);
		}

		BigInteger secret = MathUtilities.computeYLagrange(BigInteger.ZERO, shares, BigInteger.valueOf(base));
		return secret.toString();
	}

	public static void findAllY(int base, int[][]parts) {
		ArrayList<Share> shares = new ArrayList<>();

		for (int i = 0; i < parts.length; i++) {
			Share s = new Share();
			s.setX(BigInteger.valueOf(parts[i][0]));
			s.setY(BigInteger.valueOf(parts[i][1]));
			shares.add(s);
		}

		int[][] points = new int[base][2];
		for (int i = 0; i < base; i++) {
			BigInteger bigY =  MathUtilities.computeYLagrange(BigInteger.valueOf(i), shares, BigInteger.valueOf(base));
			System.out.println(i + ", " + bigY);
		}

		//BigInteger secret = MathUtilities.computeYLagrange(BigInteger.ZERO, shares, BigInteger.valueOf(base));
		//return secret.toString();
	}
}
