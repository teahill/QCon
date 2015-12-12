package com.juntest.scrubber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class PageParser {
	
	private URL url;
	private Pattern[] patterns;
	
	public PageParser(String pageUrl, String[] regex){
		
		patterns = new Pattern[regex.length];
		
		try {
			url = new URL(pageUrl);
		} 
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.err.println("MalformedURL: + " + pageUrl);
			throw new RuntimeException(e);
		}
		
		for (int i=0; i<regex.length; i++){
			
			try{
				patterns[i] = Pattern.compile(regex[i]);
			}
			catch (PatternSyntaxException e){
				System.err.println("Pattern Syntax error: + " + regex[i]);
				throw new RuntimeException(e);
			}
		}
	}

	public List<String> extractLinks() {
		
		List<String> matchList = new ArrayList<String>();
		
		try
		{
			URLConnection conn = url.openConnection();
			
			// 	Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null)
			{
				line = line.trim();
				String[] tokens = line.split("\\s+");
				
				for (int i=0; i<tokens.length; i++){
					//System.out.println(tokens[i]);
					String matchedLink = getMatchedLink(tokens[i]);
					
					if (matchedLink!=null){
						//System.out.println("Matched link: " + matchedLink);
						matchList.add(matchedLink);
					}
				}
			}
			rd.close();
		}
		catch (IOException e) {
			System.err.println("Error reading from HTTP connection");
			throw new RuntimeException(e);
		}
		
		return matchList;
	}
	
	private String getMatchedLink(String token) {
		
		for (int i=0; i<patterns.length; i++){
			Matcher m = patterns[i].matcher(token);
			
			if (m.matches()){
				
				int start = token.indexOf("\"");
				int end = token.lastIndexOf("\"");
				
				if (start > 0 && end > start){
					return token.substring(start+1, end);
				}
			}
		}
		
		return null;
	}
}
