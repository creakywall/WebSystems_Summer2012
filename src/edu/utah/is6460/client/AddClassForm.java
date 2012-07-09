package edu.utah.is6460.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.FieldSetElement;
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
	@UiField FieldSetElement results;
	
	public AddClassForm() {
		initWidget(uiBinder.createAndBindUi(this));
		submitClass.setText("Add Class");
		
	}

	@UiHandler("submitClass")
	void onClick(ClickEvent e) {
		String subject = subjectBox.getValue().isEmpty() ? emptyField(subjectBox) : clearField(subjectBox);
		String term = termBox.getValue().isEmpty()  ? emptyField(termBox) : clearField(termBox);
		String catalog = catalogBox.getValue().isEmpty() ? emptyField(catalogBox) : clearField(catalogBox);
		String email = emailBox.getValue().isEmpty() ? emptyField(emailBox) : clearField(emailBox);
		String section = sectionBox.getValue().isEmpty() ? emptyField(sectionBox): clearField(sectionBox);
		if(!subject.isEmpty() && !term.isEmpty() && !catalog.isEmpty() && !email.isEmpty() && !section.isEmpty()){
			results.setInnerText("");
			classAlertService.addClass(term, subject, catalog, email, section, new AsyncCallback<String>() {
	
				@Override
				public void onFailure(Throwable caught) {
					
				}
	
				@Override
				public void onSuccess(String result) {
					results.setInnerText(result);
					subjectBox.setValue("");
					termBox.setValue("");
					catalogBox.setValue("");
					emailBox.setValue("");
					sectionBox.setValue("");
				}
				
			});
		}
	}

	private String clearField(TextBox box) {
		box.getElement().removeClassName("error");
		return box.getValue();
	}

	private String emptyField(TextBox box) {
		box.getElement().addClassName("error");
		return "";
	}

	public void setText(String text) {
		submitClass.setText(text);
	}

	public String getText() {
		return submitClass.getText();
	}

}
