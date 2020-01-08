package main;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

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
		File f = new File(path + "/meta.smd");
		return f.exists();
	}


	/**
	 * This method is used to read data from file and return deserialized object.
	 *
	 * @param fn
	 * @return Metadata
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object getObject (String fn) throws IOException, ClassNotFoundException
	{
		FileInputStream fileInputStream = new FileInputStream(fn);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
		ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);

		Object object = objectInputStream.readObject();

		objectInputStream.close();

		return object;
	}


	/**
	 * This method is used to write serialized data to file
	 *
	 * @param fn
	 * @param object
	 * @throws IOException When the file can not be written
	 */
	public static void saveObject (String fn, Serializable object) throws IOException
	{
		FileOutputStream fileOutputStream = new FileOutputStream(fn);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
		objectOutputStream.writeObject(object);
		objectOutputStream.close();
	}


	public static ArrayList<Share> getFolderShares(String path)
	{
		String[] pathnames;
		ArrayList<Share> shares = new ArrayList<>();
		File f = new File(path);
		pathnames = f.list();

		for (String pathname : pathnames) {
			// Print the names of files and directories
			if (! pathname.toLowerCase().endsWith(".ssh"))
				continue;

			Share share = null;
			try {
				share = (Share) getObject(path + "/" + pathname);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			shares.add(share);
			System.out.println(pathname);
		}

		return shares;
	}

	public static boolean folderExists(String path)
	{
		File file = new File(path);

		boolean exists = file.exists();

		if (! exists)
			return false;

		return file.isDirectory();
	}

	public static boolean isEmptyFolder(String path)
	{
		String[] pathnames;
		File f = new File(path);
		pathnames = f.list();

		return pathnames.length == 0;
	}
}
