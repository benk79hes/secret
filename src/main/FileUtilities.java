package main;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtilities
{
	public static void saveToFile(String fileName, BigInteger value) throws Exception
	{
		byte[] array = value.toByteArray();
		OutputStream fos = new FileOutputStream(fileName);
		ObjectOutputStream outputStream = new ObjectOutputStream(fos);
		outputStream.write(array);
		outputStream.close();
		fos.close();
	}


	public static BigInteger readFromFile(String fileName) throws Exception
	{
		byte[] array = Files.readAllBytes(Paths.get(fileName));
		return new BigInteger(1, array);
	}


	public static boolean validSecretPath(String path)
	{
		File f = new File(path + "/metadata.shs");
		return f.exists();
	}


	/**
	 * This method is used to read data from file and return deserialized object.
	 *
	 * @param path
	 * @return Metadata
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Metadata getMetadata (String path) throws IOException, ClassNotFoundException
	{
		FileInputStream fileInputStream = new FileInputStream(path + "/metadata.shs");
		BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
		ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);

		Metadata object = (Metadata)objectInputStream.readObject();

		objectInputStream.close();

		return object;
	}


	/**
	 * This method is used to write serialized data to file
	 *
	 * @param path
	 * @param object
	 * @throws IOException When the file can not be written
	 */
	public static void saveMetadata (String path, Metadata object) throws IOException
	{
		FileOutputStream fileOutputStream = new FileOutputStream(path + "/metadata.shs");
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
		objectOutputStream.writeObject(object);
		objectOutputStream.close();
	}

}
