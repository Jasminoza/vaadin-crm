package com.example.application.views.list;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.application.data.Contact;
import com.example.application.views.ContactForm;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class ListViewTest {

  @Autowired
  private ListView listView;

  @Test
  public void formShownWhenContactSelected() {
    Grid<Contact> contactsGrid = listView.contactsGrid;
    Contact firstContact = getFirstItem(contactsGrid);

    ContactForm contactForm = listView.contactForm;

    assertFalse(contactForm.isVisible());
    contactsGrid.asSingleSelect().setValue(firstContact);
    assertTrue(contactForm.isVisible());
    assertEquals(firstContact.getFirstName(), contactForm.firstName.getValue());
  }

  private Contact getFirstItem(Grid<Contact> grid) {
    return ((ListDataProvider<Contact>) grid.getDataProvider()).getItems().iterator().next();
  }

}