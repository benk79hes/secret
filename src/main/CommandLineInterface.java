package main;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;

import static main.FileUtilities.*;

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
				generateMenu();
				break;
			case 2:
				getSecretMenu();
				break;
		}
	}


	public static void generateMenu()
	{

		int nbBites = 0;
		int[] biteOptions = {128,256,512,4096};

		//choix de bits

		System.out.println("Veuillez écrire le nombre de bits voulu");
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
		while (nbPersonnes < 2);


		//choix nombre de parts de secret voulue

		System.out.println("Choisissez le nombre de part de secret voulu:");


		int nbParts = 0;

		do {
			System.out.println("Nombre de parts :");
			nbParts = readCommandInt();
		}
		while (nbParts < nbPersonnes);




		Secret s = new Secret(nbBites / 8, nbPersonnes, nbParts);
		s.generateSecret();
		/* for (int i = 1; i <= nbParts; i++) {
			s.makeNewPoint(BigInteger.valueOf(i));
		} */


	}


	public static void getSecretMenu()
	{
		System.out.println("Indiquez le répertoire contenant les fichiers meta ainsi que les parts de secret");

		String path = "C:/temp/secret";
		// Metadata md = null;
/*
		do {
			System.out.print("Dossier: ");
			path = readCommandString();
		}
		while (! validSecretPath(path));*/

/*		try {
			md = getData(path);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} */

		Secret s = new Secret(path);
		s.find();

		//s.find();
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
