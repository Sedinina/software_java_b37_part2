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

public class DeletionContactsWithGroups extends TestBase {


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
      ContactDate contact = new ContactDate()
              .withFirstName("Test").withLastName("Test1").inGroup(groups.iterator().next());
      app.contact().create(contact);
      app.goTo().homePage();
    }

    if (app.contact().findContactWithGroup(contacts) == null) {
      app.contact().selectAllGroup();
      Contacts contactsUI = app.contact().all();
      ContactDate selectedContactUI = contactsUI.iterator().next();
      GroupDate selectedGroup = groups.iterator().next();
      app.contact().addContactToGroup(selectedContactUI.getId(), selectedGroup.getId());
      app.goTo().homePage();
    }
  }

  @Test
  public void deletionContactsWithGroups() {
    Contacts contacts = app.db().contacts();
    ContactDate contactWithGroup = app.contact().findContactWithGroup(contacts);
    int contactId = contactWithGroup.getId();
    GroupDate group = contactWithGroup.getGroups().iterator().next();
    int groupId = group.getId();
    Groups deletedGroup = app.db().getGroupById(groupId);
    GroupDate deletedGroupData = deletedGroup.iterator().next();
    app.contact().filterByGroup(groupId);
    app.contact().removeContactFromGroup(contactWithGroup.getId(), group.getId());

    Contacts contactAfter = app.db().getContactById(contactId);
    ContactDate contactWithoutGroup = contactAfter.iterator().next();
    assertThat(contactWithGroup, CoreMatchers.equalTo(contactWithoutGroup.inGroup(deletedGroupData)));
  }
}