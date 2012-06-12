package edu.utah.is6460.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ClassAlert implements EntryPoint {
	
	
	public void onModuleLoad() {
		RootPanel.get("wrapper").add(new AddClassForm());
	}
}
