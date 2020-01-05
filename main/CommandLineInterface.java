package main;

import java.util.Arrays;
import java.util.Scanner;

public class CommandLineInterface
{
	public static void showMainMenu()
	{
		int option;
		int[] options = {1, 2};
		System.out.println("Bienvenue sur l'application de protection de secrets");
		System.out.println("------------------------------------------------------");
		System.out.println("Choisir une option et valider par <enter>");
		System.out.println("1 - Générer un secret");
		System.out.println("2 - Gérer un secret existant");

		do {
			System.out.print("Option: ");
			option = readCommandInt();
		}
		while (Arrays.binarySearch(options, option) < 0);

		switch (option) {
			case 1:
				int nbBites = 0;
				int[] biteOptions = {128,256,512,4096};

				//choix de bites

				System.out.println("Veuillez écrire le nombre de bites voulu");
				System.out.println("128 bites");
				System.out.println("256 bites");
				System.out.println("512 bites");
				System.out.println("4096 bites");
				do {
					System.out.print("Option: ");
					nbBites = readCommandInt();
				}
				while (Arrays.binarySearch(biteOptions, nbBites) < 0);

				System.out.println(nbBites);

				//choix de nombre de personnes


				System.out.println("Combiens de personnes sont nécessaires pour retrouver le secret?");

				int nbPersonnes = 0;

				do {
					System.out.print("Nombre de personnes: ");
					nbPersonnes = readCommandInt();
				}
				while (nbPersonnes < 0);

				System.out.println(nbPersonnes);


				//choix nombre de parts de secret voulue

				System.out.println("Choisissez le nombre de part de secret voulu:");


				int nbParts = 0;

				do {
					System.out.println("Nombre de parts :");
					nbParts = readCommandInt();
				}
				while (nbParts > nbPersonnes);
				System.out.println(nbParts);

				break;
		}

	}

	public static String readCommandString()
	{
		Scanner scanner = new Scanner(System.in);
		String command = scanner.nextLine();
		return command;
	}

	public static int readCommandInt()
	{
		Scanner scanner = new Scanner(System.in);
		int command = scanner.nextInt();
		return command;
	}
}
