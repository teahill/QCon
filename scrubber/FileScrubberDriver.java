package com.juntest.scrubber;

import java.util.List;

public class FileScrubberDriver {
	
	public static void main(String[] args){
		
		String targetPage = "https://qconsf.com/sf2015/schedule/tabular";
		String baseDir = "/Users/jyang/qcon2015";
		
		String[] regex = new String[] {	
			//"href=\"https://qconsf.com/system/files/presentation-slides/qconsf-2015-stream_processing_in_uber.pdf\">[Download",
			"href=\"https:\\/\\/qconsf\\.com\\/system\\/files\\/presentation-slides\\/[A-Za-z0-9-_]+\\.pdf\">\\[Download",
			//"href=\"https://qconsf.com/system/files/presentation-slides/technical_debt_1_2.pptx\">[Download",
			"href=\"https:\\/\\/qconsf\\.com\\/system\\/files\\/presentation-slides\\/[A-Za-z0-9-_]+\\.pptx\">\\[Download",
		};
		
		PageParser ps = new PageParser(targetPage, regex);
		
		FileDownloader fd = new FileDownloader(baseDir);
		
		try {
			List<String> files = ps.extractLinks();
			
			System.out.println("Totally " + files.size() + " files to be downloaded...");
			
			for (String furl : files){
				fd.downladFile(furl);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
