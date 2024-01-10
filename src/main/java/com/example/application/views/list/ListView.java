package com.example.application.views.list;

import com.example.application.data.Contact;
import com.example.application.services.CrmService;
import com.example.application.views.ContactForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Collections;

@PageTitle("Contacts | Vaadin CRM")
@Route(value = "")
public class ListView extends VerticalLayout {
  private final Grid<Contact> contactsGrid = new Grid<>(Contact.class);
  private final TextField filterText = new TextField();
  private ContactForm form;
  private CrmService service;

  public ListView(CrmService service) {
    this.service = service;

    addClassName("list-view");
    setSizeFull();
    configureGrid();
    configureForm();

    add(getToolbar(), getContent());
    updateList();
  }

  private void configureGrid() {
    contactsGrid.addClassNames("contacts-grid");
    contactsGrid.setSizeFull();
    contactsGrid.setColumns("firstName", "lastName", "email");
    contactsGrid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status");
    contactsGrid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");
    contactsGrid.getColumns().forEach(col -> col.setAutoWidth(true));
  }

  private void configureForm() {
    form = new ContactForm(service.findAllCompanies(), service.findAllStatuses());
    form.setWidth("25em");
  }

  private Component getContent() {
    HorizontalLayout content = new HorizontalLayout(contactsGrid, form);
    content.setFlexGrow(2, contactsGrid);
    content.setFlexGrow(1, form);
    content.addClassName("content");
    content.setSizeFull();
    return content;
  }

  private HorizontalLayout getToolbar() {
    filterText.setPlaceholder("Filter by name...");
    filterText.setClearButtonVisible(true);
    filterText.setValueChangeMode(ValueChangeMode.LAZY);
    filterText.addValueChangeListener(e -> updateList());

    Button addContactButton = new Button("Add contact");

    var toolbar = new HorizontalLayout(filterText, addContactButton);
    toolbar.setClassName("toolbar");
    return toolbar;
  }

  private void updateList() {
    contactsGrid.setItems(service.findAllContacts(filterText.getValue()));
  }
}
