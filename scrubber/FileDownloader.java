package com.juntest.scrubber;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FileDownloader {
	
	private File baseDir;
	
	public FileDownloader(String base){
	
		baseDir = new File(base);
		
		if (!baseDir.exists()){
			 baseDir.mkdir();
		}
	}

	public void downladFile(String srcUrl) throws IOException{
		
		if (srcUrl.isEmpty()){
			return;
		}
		
		System.out.print("Downloading: " + srcUrl + "\n");	
		
		URL fileUrl = new URL(srcUrl);
			
		String fileName = srcUrl.substring(srcUrl.lastIndexOf("/")+1, srcUrl.length());
		
		URLConnection uc = fileUrl.openConnection();
		uc.setReadTimeout(0);
		
		//String contentType = uc.getContentType();
		int contentLength = uc.getContentLength();
		
		InputStream raw = uc.getInputStream();
		InputStream in = new BufferedInputStream(raw);
		
		byte[] data = new byte[contentLength];
		int bytesRead = 0;
		int offset = 0;
		
		int count = 0; 
		while (offset < contentLength) {
			bytesRead = in.read(data, offset, data.length - offset);
			if (bytesRead == -1)
				break;
			offset += bytesRead;
			count++;
			if (count % 5000 == 0)
				System.out.print("\n");			
			else if (count % 50 == 0)
				System.out.print(".");

		}
				
		if (count>=50){
			System.out.print("\n");
		}
		
		in.close();

		if (offset != contentLength) {
			throw new IOException("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(baseDir.getPath() + "/" + fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		out.write(data);
		out.flush();
		out.close();
	}
	
}
