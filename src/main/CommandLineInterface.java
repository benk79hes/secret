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
				Secret s = new Secret();
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
