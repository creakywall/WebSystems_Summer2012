package edu.utah.is6460.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AddClassForm extends Composite implements HasText {
	private final ClassAlertServiceAsync classAlertService = GWT.create(ClassAlertService.class);
	
	private static AddClassFormUiBinder uiBinder = GWT.create(AddClassFormUiBinder.class);
	interface AddClassFormUiBinder extends UiBinder<Widget, AddClassForm> {}

	@UiField Button submitClass;
	@UiField TextBox subjectBox, termBox, catalogBox, emailBox, sectionBox;
	
	public AddClassForm() {
		initWidget(uiBinder.createAndBindUi(this));
		submitClass.setText("Add Class");
	}

	@UiHandler("submitClass")
	void onClick(ClickEvent e) {
		String subject = subjectBox.getValue();
		String term = termBox.getValue();
		String catalog = catalogBox.getValue();
		String email = emailBox.getValue();
		String section = sectionBox.getValue();
		classAlertService.addClass(term, subject, catalog, email, section, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				
			}

			@Override
			public void onSuccess(String result) {
				//System.out.println(result);
				Window.alert(result);
			}
			
		});
	}

	public void setText(String text) {
		submitClass.setText(text);
	}

	public String getText() {
		return submitClass.getText();
	}

}
