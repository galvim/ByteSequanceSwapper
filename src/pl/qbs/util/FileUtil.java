package pl.qbs.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;



public class FileUtil {
	public static String getFileExtension(File file) {
		int lastDotIndex = file.getName().lastIndexOf(".");
		if ((lastDotIndex != -1) && (file.getName().length() > lastDotIndex)) {
			return file.getName().substring(lastDotIndex + 1);
		} else {
			return "";
		}
	}

//	public static void getAllFiles(String extension, File directory, List<File> foundFiles) {
//		for (File file : directory.listFiles()){
//			if(file.isDirectory()){
//				getAllFiles(extension, file, foundFiles);
//			} else {
//				if (FileUtil.getFileExtension(file).equals(extension)) {
//					foundFiles.add(file);
//				}
//			}
//		}
//	}

	public static List<File> getAllFiles(String extension, File directory) {
		List<File> foundFiles = new ArrayList<>();
		for (File file : directory.listFiles()){
			if(file.isDirectory()){
				foundFiles.addAll(getAllFiles(extension, file));
			} else {
				if (FileUtil.getFileExtension(file).equals(extension)) {
					foundFiles.add(file);
				}
			}
		}

		return foundFiles;
	}

	public static List<File> getFilesWithSpecifiedExtension(String extension,File directory) {
		File list[] = new File(directory.toString()).listFiles();
		return Arrays.stream(list)
			.filter(f -> f.isFile() && FileUtil.getFileExtension(f).equals(extension))
			.collect(Collectors.toList());
	}


	public static List<Integer> getIndexesOfOcurrencies(byte[] fileBuffer, byte[] key) {
		List<Integer> indexes = new ArrayList<>();
		for (int i = 0; i <= (fileBuffer.length - key.length);) {
			int j = 0;
			while ((j < key.length) && (fileBuffer[i + j] == key[j])) {
				j++;
			}

			if (j == key.length) {
				indexes.add(i);
				i += key.length;
			} else {
				i++;
			}

		}
		return indexes;
	}

	public static byte[] replaceBytes(byte[] fileBuffer, byte[] key, byte[] newKey, List<Integer> indexes){
		byte[] newFile = new byte[fileBuffer.length + (indexes.size() * (newKey.length - key.length))];

		//System.out.println(String.format("key: %s, newKey: %s, indexes: %s, oldFile size: %d, newFileSize: %d", Util.bytesToHex(key), Util.bytesToHex(newKey),
				//Arrays.toString(indexes.toArray()), fileBuffer.length, newFile.length));

		for (int oldFileIndex = 0, indexesIndex = 0, newFileIndex = 0; oldFileIndex < fileBuffer.length; ) {
			if( (indexes.size() > indexesIndex) && (oldFileIndex == indexes.get(indexesIndex))){
				//System.out.println(String.format("Zamiana klucza index: %d", indexes.get(indexesIndex)));
				System.arraycopy(newKey, 0, newFile, newFileIndex, newKey.length);
				indexesIndex++;
				oldFileIndex+=key.length;
				newFileIndex+=newKey.length;
				//System.out.println(String.format("Zamiana klucza indexesIndex: %d, oldFileIndex: %d, newFileIndex: %d\n",
				//	indexesIndex, oldFileIndex, newFileIndex));
			} else {
				int length = 0;
				if (indexes.size() > indexesIndex){
					length = indexes.get(indexesIndex)-oldFileIndex;
				} else {
					length = fileBuffer.length - oldFileIndex;
				}
				//System.out.println(String.format("Kopiowanie pliku oldFileIndex: %d, newFileIndex: %d, length: %d", oldFileIndex, newFileIndex, length));
				System.arraycopy(fileBuffer, oldFileIndex, newFile, newFileIndex, length);
				oldFileIndex += length;
				newFileIndex += length;
			}
			//System.out.println("newFile: " + Util.bytesToHex(newFile));
		}
		return newFile;
	}

	public static void writeFile(byte[] file, String path) throws IOException {
			try (DataOutputStream output = new DataOutputStream(new FileOutputStream(path))) {
				output.write(file);
			}
	}

	public static byte[] readFile(File path) throws IOException{
		try (FileInputStream input = new FileInputStream(path)){
			byte[] fileBuffer = new byte[input.available()];
			input.read(fileBuffer);
			return fileBuffer;
		}

	}
}
