package edu.ntust.cs.idsl.nomissing.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;

public class FileUtil {
	
	public static String getFileNameFromURL(String url) {
		return url.substring(url.lastIndexOf("/") + 1);
	}
	
	public static void saveFile(Context context, String dirName, String fileName, byte[] binary) throws IOException {
		File dirFile = (dirName == null) ? context.getFilesDir() : context.getDir(dirName, Context.MODE_PRIVATE);
		File file = new File(dirFile, fileName);
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write(binary);
		fileOutputStream.flush();
		fileOutputStream.close();		
	}
	
	
	public static String getFilesDir(Context context) {
		return context.getFilesDir().toString();
	}
	
	public static File getDir(Context context, String dirName) {
		return context.getDir(dirName, Context.MODE_PRIVATE);
	}
	
//	public static String saveFile(Context context, String dirName, byte[] file) {
//		File dir = context.getDir(dirName, Context.MODE_PRIVATE);
//		
//	    FileOutputStream fileOutputStream = null;
//	    try {
//	    	fileOutputStream = new FileOutputStream(file);
//	    	fileOutputStream.flush();
//	    } catch (Exception e) {
//	        ;
//	    } finally {
//	        try {
//	            osw.close();
//	        } catch (Exception e) {
//	            ;
//	        }
//	    }		
		
		
		
//		File fileWithinMyDir = new File(dir, "myfile"); //Getting a file within the dir.
//	
//		
//		FileOutputStream out = new FileOutputStream(fileWithinMyDir); //Use the stream as usual to write into the file.		
//	}
	
}
