package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Logger {

	static String filePath="Logger.log";

	public static void init (String path, String fileName) {

		if (path == null & fileName == null)
			filePath = "Logger.log";
		else if (path == null)
			filePath = fileName;
		else if (fileName == null)
			filePath = path + "Logger.log";
		else
			filePath = path + fileName;

	}

	public static void log(String log_) {

		try {
			
			//System.out.println(filePath);
	
			log_="Logger "+System.currentTimeMillis()+": "+log_+"\n";
			File f = new File(filePath);

			if (!f.exists())
				f.createNewFile();
			// For Append the file.
			OutputStream out = new FileOutputStream(f, true);
			
			byte[] buf = new byte[1024];
			buf = log_.getBytes();
			out.write(buf);
			out.close();
		} catch (FileNotFoundException ex) {
			System.out
					.println(ex.getMessage() + " in the specified directory.");
			System.exit(0);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void log(byte[] log_) {

		try {
			
			//System.out.println(filePath);
			
			File f = new File(filePath);

			if (!f.exists())
				f.createNewFile();
			// For Append the file.
			OutputStream out = new FileOutputStream(f, true);

			out.write(log_);
			out.close();
		} catch (FileNotFoundException ex) {
			System.out
					.println(ex.getMessage() + " in the specified directory.");
			System.exit(0);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}


}
