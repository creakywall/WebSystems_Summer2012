package edu.utah.is6460.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.utah.is6460.client.ClassAlertService;
import edu.utah.is6460.shared.FieldVerifier;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ClassAlertServiceImpl extends RemoteServiceServlet implements ClassAlertService {
	
	@Override
	public String addClass(String term, String subject, String catalogNum) throws IllegalArgumentException {
		try {
			String urlQuery = "http://query.yahooapis.com/v1/public/yql" +
					"?q=select%20*%20from%20html%20where%20" +
					"url%3D%22http%3A%2F%2Fwww.acs.utah.edu%2Fuofu%2Fstu%2Fscheduling%2Fcrse-info%3F" +
					"term%3D"+ URLEncoder.encode(term, "UTF-8") +"%26" +
					"subj%3D" + URLEncoder.encode(subject, "UTF-8") + "%26" +
					"catno%3D" + URLEncoder.encode(catalogNum, "UTF-8") + 
					"%22%20and%0A%20%20%20%20%20%20xpath%3D'%2F%2Ftable%2Ftr%5B2%5D%2Ftd%5B8%5D'&format=json&diagnostics=true&callback=cbfunc";
			System.out.println(urlQuery);
			System.out.println(readUrl(urlQuery));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String readUrl(String urlString) throws Exception {
	    BufferedReader reader = null;
	    try {
	        URL url = new URL(urlString);
	        reader = new BufferedReader(new InputStreamReader(url.openStream()));
	        StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read); 

	        return buffer.toString();
	    } finally {
	        if (reader != null)
	            reader.close();
	    }
	}
}
