package edu.utah.is6460.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("classAlert")
public interface ClassAlertService extends RemoteService {
	String addClass(String term, String subject, String catalogNum) throws IllegalArgumentException;
}
