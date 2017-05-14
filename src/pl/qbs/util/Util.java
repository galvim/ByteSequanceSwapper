package pl.qbs.util;

import javax.xml.bind.DatatypeConverter;

public class Util {

	public static byte[] stringToHex(String input){
		String cleanInput = input.replaceAll("[^0-9a-fA-F]", "");
		return DatatypeConverter.parseHexBinary(cleanInput);
	}

	public static String bytesToHex(byte[] arrayBytes){
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < arrayBytes.length; i++) {
			builder.append(String.format("%02x ", arrayBytes[i]));

		}
		return builder.toString();
	}
}
