package edu.utah.is6460.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class RefreshAlerts extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query("Alert");

		PreparedQuery pq = datastore.prepare(q);

		for (Entity result : pq.asList(FetchOptions.Builder.withDefaults())) {
			//String addClass(String term, String subject, String catalogNum, String email, String section) throws IllegalArgumentException;
			
			String term = result.getProperty("term").toString();
			String subject = result.getProperty("subj").toString();
			String catalogNum = result.getProperty("catalog").toString(); 
			String section = result.getProperty("section").toString();
			
			//toDo: Call Andre's method to check the URL
			
		}
	}

}
