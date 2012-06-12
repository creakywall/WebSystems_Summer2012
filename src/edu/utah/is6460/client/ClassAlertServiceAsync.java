package edu.utah.is6460.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>ClassAlertService</code>.
 */
public interface ClassAlertServiceAsync {
	void addClass(String term, String subject, String catalogNum,
			AsyncCallback<String> callback);
}
