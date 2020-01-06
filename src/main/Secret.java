package main;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.security.SecureRandom;

import static main.FileUtilities.saveMetadata;
import static main.FileUtilities.saveToFile;

public class Secret
{
	private BigInteger secret;
	//private int byteLength;
	//private int level;
	private Metadata metadata;

	//private BigInteger modularBase;
	private ArrayList<BigInteger> yList = new ArrayList<>() ;
	private ArrayList<BigInteger> xList = new ArrayList<>() ;
	private ArrayList<BigInteger> coeff = new ArrayList<>() ;


	public Secret(int byteLength, int level, int shares)
	{
		if (byteLength < 16 || byteLength > 512)
			throw new IllegalArgumentException("La clé doit être entre 16 et 512 bytes");


		Random rnd = new Random();

		metadata = new Metadata();
		metadata.base = BigInteger.probablePrime(byteLength * 8, rnd);
		metadata.level = level;
		metadata.byteLength = byteLength;
		metadata.shares = shares;

		try {
			saveMetadata("C:/temp/secret/", metadata);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Secret(Metadata metadata)
	{
		this.metadata = metadata;
	}

	public void generateSecret()
	{
		// int attempts = 0;
		do {
			secret = generateRandomNumber();
			// attempts++;
		}
		while (!validSecret(secret));

		save("secret.shs", secret);
		generateMainFunction();
		generateShares();
	}


	private boolean validSecret(BigInteger s)
	{
		if (metadata.base.compareTo(secret) < 1)
			return false;

		if (secret.bitLength() < metadata.byteLength * 8)
			return false;

		return true;
	}


	private BigInteger generateRandomNumber() {
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[metadata.byteLength]; // 128 bits are converted to 16 bytes;
		random.nextBytes(bytes);
		return new BigInteger(1, bytes);
	}


	/**
	 * Génère les coefficients de la fonction polynomiale
	 *
	 */
	private void generateMainFunction()
	{
		coeff.add(secret);

		for (int i = 0; i < metadata.level; i++) {
			BigInteger c = generateRandomNumber();
			if (metadata.base.compareTo(c) < 1) {
				c = c.mod(metadata.base);
			}

			coeff.add(c);
		}
	}


	private void generateShares()
	{
		System.out.println("Créations de parts: " + metadata.shares);
		//return;
		for (int i = 1; i <= metadata.shares; i++) {
			makeNewPoint(BigInteger.valueOf(i));
		}
	}


	/**
	 * Génère une nouvelle part en fonction
	 *
	 *
	 */
	private void makeNewPoint(BigInteger keyId)
	{
		BigInteger newX, newY;
		newX = keyId;

		xList.add(newX);
		newY = computeY(newX);
		BigInteger share = newY.mod(metadata.base);

		save("share" + keyId + ".shs", share);
		System.out.println("Secret share " + keyId + "    " + share.toString(2));
		yList.add(share);
	}



	public BigInteger computeY(BigInteger x)
	{
		BigInteger y;

		y = BigInteger.valueOf(0);

		/**
		 * @todo Replace with Horner
		 */
		for (int i = 0; i < coeff.size(); i++) {
			BigInteger c = coeff.get(i);
			y = y.add(c.multiply(x.pow(i)));
		}

		return y;
	}


	public void save(String fn, BigInteger value)
	{
		try {
			saveToFile("C:/temp/secret/" + fn, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



}
