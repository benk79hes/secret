package main;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.security.SecureRandom;

import static main.FileUtilities.*;
import static main.MathUtilities.*;

public class Secret
{
	private BigInteger secret;

	private Metadata metadata;

	private String path = "C:/temp/secret";

	private ArrayList<BigInteger> coeff = new ArrayList<>() ;

	public Secret()
	{

	}

	public Secret(String path, int byteLength, int level, int shares)
	{
		if (byteLength < 16 || byteLength > 512)
			throw new IllegalArgumentException("La clé doit être entre 16 et 512 bytes");

		this.path = path;
		Random rnd = new Random();

		metadata = new Metadata();
		metadata.base = BigInteger.probablePrime(byteLength * 8, rnd);
		metadata.level = level;
		metadata.byteLength = byteLength;
		metadata.shares = shares;

		saveData("meta.smd", metadata);
	}

	public Secret(String path)
	{
		this.path = path;
		System.out.println("Charger les métadonnées");
		this.metadata = (Metadata) getData("meta.smd");
	}

	public void generateSecret()
	{
		// int attempts = 0;
		do {
			secret = generateRandomNumber();
			// attempts++;
		}
		while (!validSecret(secret));

		//
		saveSecret();

		generateMainFunction();
		generateShares();
	}


	public void findSecret() throws MissingSharesException
	{
		ArrayList<Share> shares = getFolderShares(path);
		if (shares.size() < metadata.level)
			throw new MissingSharesException();

		secret = computeYLagrange(BigInteger.ZERO, shares, metadata.base);
	}

	public void saveSecret()
	{
		saveData("secret.bin.shs", secret);
	}


	public void createNewShare() throws MissingSharesException
	{
		ArrayList<Share> shares = getFolderShares(path);
		if (shares.size() < metadata.level)
			throw new MissingSharesException();

		metadata.shares++;

		Share share = new Share();
		share.setX(BigInteger.valueOf(metadata.shares));
		share.setY(computeYLagrange (share.getX(), shares, metadata.base));
		saveData("new.share." + metadata.shares + ".ssh", share);
		saveData("meta.smd", metadata);
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
		byte[] bytes = new byte[metadata.byteLength]; // 128 bits are converted to 16 bytes;
		random.nextBytes(bytes);
		return new BigInteger(1, bytes);
	}


	/**
	 * Génère les coefficients de la fonction polynomiale
	 */
	public void generateMainFunction()
	{
		coeff.add(secret);

		for (int i = 1; i < metadata.level; i++) {
			BigInteger c = generateRandomNumber();
			if (metadata.base.compareTo(c) < 1) {
				c = c.mod(metadata.base);
			}

			coeff.add(c);
		}
	}


	public void generateShares()
	{
		for (int i = 1; i <= metadata.shares; i++) {
			makeNewShare(BigInteger.valueOf(i));
		}
	}


	/**
	 * Génère une nouvelle part du secret
	 */
	private void makeNewShare(BigInteger keyId)
	{
		BigInteger newX, newY;
		Share share = new Share();

		//
		share.setX(keyId);
		share.setY(computeY(share.getX()));

		//
		saveData("share" + keyId + ".ssh", share);
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

		return y.mod(metadata.base);
	}



	public void saveData(String fn, Serializable object)
	{
		try {
			saveObject(path + "/" + fn, object);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public Object getData(String fn)
	{
		Object object = null;
		try {
			object = getObject(path + "/" + fn);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return object;
	}

}
