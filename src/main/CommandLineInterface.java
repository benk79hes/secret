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
		int[] options = {1, 2, 3, 4};


		System.out.println("Bienvenue sur l'application de protection de secrets");
		System.out.println("------------------------------------------------------");
		System.out.println("Choisir une option et valider par <enter>");
		System.out.println("1 - Générer un secret");
		System.out.println("2 - Retrouver un secret existant");
		System.out.println("3 - Ajouter une part pour un secret existant");
		System.out.println("4 - Regénérer les parts pour un secret existant (départ employé)");

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
			case 3:
				addShareMenu();
				break;
			case 4:
				regenerateSharesMenu();
				break;
		}
	}


	public static void generateMenu()
	{

		int nbBites = 0;
		int[] biteOptions = {128,256,512,4096};

		//choix de bits

		System.out.println("Veuillez choisir le nombre de bits");
		System.out.println("128  - 128 bits");
		System.out.println("256  - 256 bits");
		System.out.println("512  - 512 bits");
		System.out.println("4096 - 4096 bits");
		do {
			System.out.print("Nombre de bits: ");
			nbBites = readCommandInt();
		}
		while (Arrays.binarySearch(biteOptions, nbBites) < 0);

		// System.out.println(nbBites);

		//choix de nombre de personnes


		System.out.println("Combiens de personnes sont nécessaires pour retrouver le secret?");

		int nbPersonnes = 0;

		do {
			System.out.print("Nombre de personnes: ");
			nbPersonnes = readCommandInt();
		}
		while (nbPersonnes < 2);


		//choix nombre de parts de secret voulue

		System.out.println("Choisissez le nombre de part de secret voulu");


		int nbParts = 0;

		do {
			System.out.print("Nombre de parts: ");
			nbParts = readCommandInt();
		}
		while (nbParts < nbPersonnes);


		/**
		 * Choix du répertoire de travail
		 */
		String path = "";
		System.out.println("Choisissez le répertoire où créer les fichiers (il doit être vide)");

		do {
			System.out.print("Répertoire: ");
			path = readCommandString();
		}
		while (!validateEmptyFolder(path));


		Secret s = new Secret(path, nbBites / 8, nbPersonnes, nbParts);



		s.generateSecret();


		System.out.println("");

		System.out.println("Les fichiers du secret ont été créés dans le repertoire " + path);
		System.out.println("Tout fichier nommé 'secret.*' contient le secret et doit être détruit après utilisation.");
		System.out.println("Le fichier 'meta.smd' contient les métadonnées nécessaires à retrouver le secret.");
		System.out.println("Les fichier 'share*.ssh' contiennent les parts à distribuer");
		System.out.println("");
		System.out.println("VEILLEZ A UTILISER CORRECTEMENT CES FICHIERS POUR LA SECURITE DE VOTRE SECRET!");
		System.out.println("");
		System.out.println("Pour retrouver le secret vous devrez créer un répertoire avec les parts minimum requises ainsi que les métadonnées");
	}


	public static void getSecretMenu()
	{
		System.out.println("Indiquez le répertoire contenant les fichiers meta ainsi que les parts de secret");

		String path = "";


		do {
			System.out.print("Dossier: ");
			path = readCommandString();
		}
		while (! validSecretPath(path));


		Secret s = new Secret(path);


		try {
			s.findSecret();
			s.saveSecret();
		} catch (MissingSharesException e) {
			System.out.println("Le dossier ne contient pas assez de part de secret!");
			System.out.println("Bye");
			return;
		}

		System.out.println("Le secret a été enregistré dans le dossier!");
	}


	public static void regenerateSharesMenu()
	{
		System.out.println("Indiquez le répertoire contenant les fichiers meta ainsi que les parts de secret");

		String path = "";


		do {
			System.out.print("Dossier: ");
			path = readCommandString();
		}
		while (! validSecretPath(path));


		Secret s = new Secret(path);


		try {
			s.findSecret();

		} catch (MissingSharesException e) {
			System.out.println("Le dossier ne contient pas assez de part de secret!");
			System.out.println("Bye");
			return;
		}

		s.generateMainFunction();
		s.generateShares();

		System.out.println("Les nouvelles parts on été créées");
	}


	public static void addShareMenu()
	{
		System.out.println("Indiquez le répertoire contenant les fichiers meta ainsi que les parts de secret");

		String path = "";


		do {
			System.out.print("Dossier: ");
			path = readCommandString();
		}
		while (! validSecretPath(path));


		Secret s = new Secret(path);


		try {
			s.createNewShare();

		} catch (MissingSharesException e) {
			System.out.println("Le dossier ne contient pas assez de part de secret!");
			System.out.println("Bye");
			return;
		}

		System.out.println("La nouvelle part a été enregistré dans le dossier et les metadonnées mises à jour!");
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

	private static boolean validateEmptyFolder(String path)
	{
		if (!folderExists(path)) {
			System.out.println("Le dossier '" + path + "' n'existe pas.");
			return false;
		}

		if (!isEmptyFolder(path)) {
			System.out.println("Le dossier '" + path + "' n'est pas vide.");
			return false;
		}

		return true;
	}
}
