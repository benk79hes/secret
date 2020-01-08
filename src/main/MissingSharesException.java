package main;

public class MissingSharesException extends Exception
{
	public MissingSharesException()
	{
		super("Not enough shares");
	}
}
