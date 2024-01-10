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

@PageTitle("Contacts | Vaadin CRM")
@Route(value = "")
public class ListView extends VerticalLayout {
  private final Grid<Contact> contactsGrid = new Grid<>(Contact.class);
  private final TextField filterText = new TextField();

  private final CrmService crmService;

  private ContactForm contactForm;

  public ListView(CrmService crmService) {
    this.crmService = crmService;

    addClassName("list-view");
    setSizeFull();
    configureGrid();
    configureContactForm();

    add(getToolbar(), getContent());
    updateList();
    closeEditor();
  }

  private void configureGrid() {
    contactsGrid.addClassNames("contacts-grid");
    contactsGrid.setSizeFull();
    contactsGrid.setColumns("firstName", "lastName", "email");
    contactsGrid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status");
    contactsGrid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");
    contactsGrid.getColumns().forEach(col -> col.setAutoWidth(true));

    contactsGrid
      .asSingleSelect()
      .addValueChangeListener(event -> editContact(event.getValue()));
  }

  private void editContact(Contact contact) {
    if (contact == null) {
      closeEditor();
    }

    contactForm.setContact(contact);
    contactForm.setVisible(true);
    addClassName("editing");
  }

  private void closeEditor() {
    contactForm.setContact(null);
    contactForm.setVisible(false);
    removeClassName("editing");
  }

  private void configureContactForm() {
    contactForm = new ContactForm(crmService.findAllCompanies(), crmService.findAllStatuses());
    contactForm.setWidth("25em");

    contactForm.addSaveListener(this::saveContact);
    contactForm.addDeleteListener(this::deleteContact);
    contactForm.addCloseListener(e -> closeEditor());
  }

  private void saveContact(ContactForm.SaveEvent saveEvent) {
    crmService.saveContact(saveEvent.getContact());
    updateList();
    closeEditor();
  }

  private void deleteContact(ContactForm.DeleteEvent deleteEvent) {
    crmService.deleteContact(deleteEvent.getContact());
    updateList();
    closeEditor();
  }

  private Component getContent() {
    HorizontalLayout content = new HorizontalLayout(contactsGrid, contactForm);
    content.setFlexGrow(2, contactsGrid);
    content.setFlexGrow(1, contactForm);
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
    addContactButton.addClickListener(click -> addContact());

    var toolbar = new HorizontalLayout(filterText, addContactButton);
    toolbar.setClassName("toolbar");
    return toolbar;
  }

  private void addContact() {
    contactsGrid.asSingleSelect().clear();
    editContact(new Contact());
  }

  private void updateList() {
    contactsGrid.setItems(crmService.findAllContacts(filterText.getValue()));
  }
}
