package main;

import java.io.Serializable;
import java.math.BigInteger;

public class Share implements Serializable
{
	/**
	 * Serial number for serialization
	 */
	private static final Long serialVersionUID = 1L;


	/**
	 * Share id and x ccordinate
	 */
	private BigInteger x;


	/**
	 * Share value and y ccordinate
	 */
	private BigInteger y;





	public BigInteger getX ()
	{
		return x;
	}


	public void setX (BigInteger x)
	{
		this.x = x;
	}


	public BigInteger getY ()
	{
		return y;
	}


	public void setY (BigInteger y)
	{
		this.y = y;
	}
}
