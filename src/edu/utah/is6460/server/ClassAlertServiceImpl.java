package edu.utah.is6460.server;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.utah.is6460.client.ClassAlertService;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ClassAlertServiceImpl extends RemoteServiceServlet implements
		ClassAlertService {

	@Override
	public String addClass(String term, String subject, String catalogNum,
			String email, String section) throws IllegalArgumentException {
		Long seats = askYahoo(term, subject, catalogNum, section);
		String result = "";
		if (seats.equals(0L)) {
			storeAlert(term, subject, catalogNum, email, section);
			// RETURN message saying that we will email them
			result = "request saved. you will be emailed with a seat is available";
		} else if (seats.equals(-1L)) {
			// RETURN message saying that there was an error, check input
			result = "There was a problem finding the class. Please check your input.";
		} else if (seats >= 1) {
			// RETURN message saying that there are currently seats available
			result = "There is currently an opening for that class... go sign up... quick!";
		}
		return result;
	}

	public static void emailMessage(String email, String term, String subj,String catalogNum, String section ) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		//String msgBody = "This is a test email from appengine";
		String mBody =(
		"<body>" +
		"<p>There is an available seat in " +subj+ " " + catalogNum +"-"+section + "!<br /><br />" +
		"Details:</p>" +
		"<ul>" +
		"<li>Term: " + term + "</li>"+
		"<li>Subject: " +subj+ "</li>" +
		"<li>Catalog Number: " + catalogNum + "</li>" +
		"</ul>" +
		"<p>Schedule: <a href =http://www.acs.utah.edu/uofu/stu/scheduling/crse-info?term="+term+"&subj="+subj+
			"&catno="+catalogNum+">http://www.acs.utah.edu/uofu/stu/scheduling/crse-info?term="+term+"" +
					"&subj="+subj+"&catno="+catalogNum+"</a>" +
		"</p>" +
		"</body>");

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("narfdre@gmail.com", "Class Alert"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					email, "Mr. User"));
			msg.setSubject("There is a seat available in your class");
			//msg.setText(msgBody);
			msg.setContent(mBody, "text/html");
			Transport.send(msg);

		} catch (AddressException e) {
			e.printStackTrace();
			// ...
		} catch (MessagingException e) {
			e.printStackTrace();
			// ...
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			// ...
		}
	}

	public static Long askYahoo(String term, String subject, String catalogNum,
			String section) {
		Long seats = -1L;
		try {
			String urlQuery = "http://query.yahooapis.com/v1/public/yql"
					+ "?q=select%20*%20from%20html%20where%20"
					+ "url%3D%22http%3A%2F%2Fwww.acs.utah.edu%2Fuofu%2Fstu%2Fscheduling%2Fcrse-info%3F"
					+ "term%3D"
					+ URLEncoder.encode(term, "UTF-8")
					+ "%26"
					+ "subj%3D"
					+ URLEncoder.encode(subject, "UTF-8")
					+ "%26"
					+ "catno%3D"
					+ URLEncoder.encode(catalogNum, "UTF-8")
					+ "%22%20and%0A%20%20%20%20%20%20xpath%3D'%2F%2Ftable%2Ftr%5Btd%2F%2Ftext()%5Bcontains(.%2C%20%22"
					+ URLEncoder.encode(section, "UTF-8")
					+ "%22)%5D%5D%2Ftd%5B8%5D'&format=xml";

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(urlQuery);
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getElementsByTagName("font");
			Node node = nodeList.item(0);
			seats = Long.valueOf(node.getTextContent());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return seats;

	}

	private void storeAlert(String term, String subject, String catalogNum,
			String email, String section) {
		Key classalertKey = KeyFactory.createKey("AlertGroup", email);
		Date date = new Date();
		Entity alert = new Entity("Alert", classalertKey);
		alert.setProperty("term", term);
		alert.setProperty("subj", subject);
		alert.setProperty("catalog", catalogNum);
		alert.setProperty("email", email);
		alert.setProperty("section", section);
		alert.setProperty("dateAdded", date);
		alert.setProperty("flag", "0");
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		datastore.put(alert);
	}

	private String getAlert(String email) {
		String catalog = "";

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query q = new Query("Alert");

		// q.addFilter("email", Query.FilterOperator.EQUAL, email);

		PreparedQuery pq = datastore.prepare(q);

		for (Entity result : pq.asList(FetchOptions.Builder.withDefaults())) {
			// System.out.println(result);
			catalog = (String) result.getProperty("catalog");
			;
		}
		return catalog;

		/*
		 * Entity e = new Entity("Alert"); DatastoreService datastore =
		 * DatastoreServiceFactory .getDatastoreService(); try { e =
		 * datastore.get(KeyFactory.createKey("AlertGroup", email)); } catch
		 * (Exception ex) { } return e.toString();
		 */
	}
}
