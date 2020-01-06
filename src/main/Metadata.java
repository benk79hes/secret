package main;

import java.io.Serializable;
import java.math.BigInteger;

public class Metadata implements Serializable
{
	/**
	 * Serial number for serialization
	 */
	private static final Long serialVersionUID = 1L;


	public BigInteger base;

	public int byteLength;

	public int level;

	public int shares = 0;
}
