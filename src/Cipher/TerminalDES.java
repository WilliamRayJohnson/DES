package Cipher;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Scanner;

public class TerminalDES {


	private static final Charset UTF8 = Charset.forName("UTF-8");

	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);

		in.close();

		int[] plaintext = { 0x63, 0x6f, 0x75, 0x6e, 0x63, 0x69, 0x6c, 0x6c };
		DES cipher = new DES(16);
		long total = 0;
		long keyTot = 0;
		for (int i = 0; i < 1000000; i++)
		{
			long time = System.nanoTime();
			cipher.setKey("00000001111111000000011111110000000111111100000001111111");
			keyTot = keyTot + (System.nanoTime() - time);
			time = System.nanoTime();
			cipher.decrypt(plaintext);
			total = total + (System.nanoTime() - time);
		}

		System.out.println("key: " + (keyTot / 1000000.0));
		System.out.println("encryption: " + (total / 1000000.0));

	}

	/**
	 * Reads data from a file using a particular character set Code found at http://illegalargumentexception.blogspot.com/2009/05/java-rough-guide-to-character-encoding.html
	 *
	 * @param file
	 *            The file to read from
	 * @param charset
	 *            The character set to use
	 * @return An array of bytes representing the character's value
	 * @throws IOException
	 */
	public static byte[] readFromFile(File file, Charset charset) throws IOException
	{
		InputStream in = new FileInputStream(file);
		Closeable stream = in;
		try
		{
			Reader reader = new InputStreamReader(in, charset);
			stream = reader;
			StringBuilder inputBuilder = new StringBuilder();
			char[] buffer = new char[1024];
			while (true)
			{
				int readCount = reader.read(buffer);
				if (readCount < 0) break;
				inputBuilder.append(buffer, 0, readCount);
			}
			return inputBuilder.toString().getBytes();
		}
		finally
		{
			stream.close();
		}
	}

	/**
	 * Write data to a file given a particular character set. Code found at http://illegalargumentexception.blogspot.com/2009/05/java-rough-guide-to-character-encoding.html
	 *
	 * @param file
	 *            The file to write to
	 * @param charset
	 *            The character set to use
	 * @param data
	 *            The data to write
	 * @throws IOException
	 */
	public static void writeToFile(File file, Charset charset, String data) throws IOException
	{
		OutputStream out = new FileOutputStream(file);
		Closeable stream = out;
		try
		{
			Writer writer = new OutputStreamWriter(out, charset);
			stream = writer;
			writer.write(data);
		}
		finally
		{
			stream.close();
		}
	}

}