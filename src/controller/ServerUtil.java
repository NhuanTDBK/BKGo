package controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ServerUtil {
	public static String encryptMessage(String input,String algorithm) throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance(algorithm);
		md.update(input.getBytes());
		byte[] output = md.digest();
		return bytesToHex(output);
	}
	public static String encryptFile(FileInputStream input) throws NoSuchAlgorithmException, IOException
	{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte [] dataToRead = new byte [1024];
		int nRead = 0;
		while((nRead=input.read(dataToRead))!=-1)
		{
			md.update(dataToRead,0,nRead);
		}
		byte[] output = md.digest();
		return bytesToHex(output);
	}
	public static String bytesToHex(byte[] b) {

	      StringBuffer buf = new StringBuffer();
	      for (int j=0; j<b.length; j++) {
	       buf.append(Integer.toHexString(0xFF&b[j]));
	      }
	      return buf.toString();
	 }
}
