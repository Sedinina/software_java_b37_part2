package ru.stqa.pft.addressbook.test;

import org.hamcrest.CoreMatchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactDate;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupDate;
import ru.stqa.pft.addressbook.model.Groups;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.equalToObject;
import static org.hamcrest.MatcherAssert.assertThat;

public class AddContactsWithGroups extends TestBase {

  @BeforeMethod
  public void ensurePrecondition() {
    Groups groups = app.db().groups();
    Contacts contacts = app.db().contacts();

    if (groups.size() == 0) {
      app.goTo().groupPage();
      app.group().create(new GroupDate().withName("test 0"));
      app.goTo().homePage();
    }

    if (contacts.size() == 0) {
      ContactDate contact = new ContactDate().withFirstName("Test1").withLastName("AddToGroup");
      app.contact().create(contact);
      app.goTo().homePage();
    }

    if (app.contact().findContactWithoutGroup(contacts) == null) {
      ContactDate contact = new ContactDate().withFirstName("Test2").withLastName("FreeContact");
      app.contact().create(contact);
      app.goTo().homePage();
    }
  }

  @Test
  public void testAddContactToGroup() {
    Groups groups = app.db().groups();
    Contacts contactsDB = app.db().contacts();
    ContactDate contactWithoutGroup = app.contact().findContactWithoutGroup(contactsDB);
    int contactId = contactWithoutGroup.getId();
    GroupDate selectedGroup = groups.iterator().next();
    app.contact().addContactToGroup(contactWithoutGroup.getId(), selectedGroup.getId());

    Contacts contactAfter = app.db().getContactById(contactId);
    ContactDate contactWithGroup = contactAfter.iterator().next();
    assertThat(contactWithGroup, CoreMatchers.equalTo(contactWithoutGroup.inGroup(selectedGroup)));
  }
}