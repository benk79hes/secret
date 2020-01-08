package main;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.security.SecureRandom;

import static main.FileUtilities.*;
import static main.MathUtilities.multipleInverse;

public class Secret
{
	private BigInteger secret;
	//private int byteLength;
	//private int level;
	private Metadata metadata;

	private String path = "C:/temp/secret";

	//private BigInteger modularBase;
	//private ArrayList<BigInteger> yList = new ArrayList<>() ;
	//private ArrayList<BigInteger> xList = new ArrayList<>() ;
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

		saveData("meta.smd", metadata);
	}

	public Secret(String path)
	{
		System.out.println("Charger les métadonnées");
		this.metadata = (Metadata) getData("meta.smd");
		System.out.println("Base: " + metadata.base.toString());
	}

	public void generateSecret()
	{
		// int attempts = 0;
		do {
			secret = generateRandomNumber();
			// attempts++;
		}
		while (!validSecret(secret));

		System.out.println(secret.toString());
		//
		saveData("secret.shs", secret);

		generateMainFunction();
		generateShares();
	}


	public void find()
	{
		System.out.println("Chercher les parts dans le dossier" + metadata.shares);
		ArrayList<Share> shares = getFolderShares();

		computeYLagrange (BigInteger.ZERO, shares);
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


	private void computeYLagrange(BigInteger x, ArrayList<Share> shares)
	{
		// ArrayList<BigInteger> cf;
		BigInteger result = BigInteger.ZERO;
		for(int i = 0; i < shares.size(); i++) {
			BigInteger fix = null;
			BigInteger rv = BigInteger.ONE;

			for (int j = 0; j < shares.size() ; j++) {
				//Share shareI =
				BigInteger t;
				if (j == i)
					continue;

				BigInteger xi = shares.get(i).getX();
				BigInteger xj = shares.get(j).getX();
				// BigInteger inv = multipleInverse(xi.subtract(xj), metadata.base);
				//t = xi.subtract(xj);
				//if (t.compareTo(BigInteger.ZERO) < 0)
				//	t.add(metadata.base);

				BigInteger inv = modularSubstract(xi, xj, metadata.base).modInverse(metadata.base);


				//t =
				//rv = rv.multiply(x.subtract(xj)).multiply(inv).mod(metadata.base);
				rv = rv.multiply(modularSubstract(x, xj, metadata.base)).multiply(inv).mod(metadata.base);
			}

			result = result.add(rv.multiply(shares.get(i).getY())).mod(metadata.base);
		}

		System.out.println(result.toString());
	}

	private BigInteger modularSubstract(BigInteger n1, BigInteger n2, BigInteger base)
	{
		BigInteger result = n1.subtract(n2);
		if (result.compareTo(BigInteger.ZERO) < 0)
			result.add(base);

		return result;
	}


	/**
	 * f (x) = y0`0(x) + y1`1(x) + y2`2(x) + y3`3(x)
	 *
	 * @return
	 */
	 /*public byte[] calculateLagrange(ShamirKey[] sk){

		BigInteger d;
		BigInteger D;
		BigInteger c;
		BigInteger S = BigInteger.ZERO;
		for(int i = 0; i < sk.length; i++){
			BigInteger d = BigInteger.ONE;
			D = BigInteger.ONE;
			for(int j = 0; j < sk.length; j++){
				if(j==i)
					continue;

				d = d.multiply(sk[j].getX());

				D = D.multiply(sk[j].getX().subtract(sk[i].getX()));
			}

			c = d.multiply(D.modInverse(metadata.base)).mod(metadata.base);
			S = S.add(c.multiply(sk[i].getF())).mod(metadata.base);
		}
		return S.toByteArray();
	}*/



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
