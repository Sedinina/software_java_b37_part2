package ru.stqa.pft.addressbook.test;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactDate;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupDate;
import ru.stqa.pft.addressbook.model.Groups;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.equalToObject;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactsModificationTest extends TestBase {

  @BeforeMethod
  public void ensurePrecondition() {
    String group = "test1";
    Groups groups = app.db().groups();
    app.goTo().homePage();
    ContactDate contactDate = new ContactDate().withFirstName("Ivan").withMiddleName("Ivanovich").
            withLastName("Ivanov").withHomePhone("987654").withMail("test@mail.ru").inGroup(groups.iterator().next());

    if (app.db().contacts().size() == 0) {
      if (app.db().groups().size() == 0) {
        app.goTo().groupPage();
        app.group().create(new GroupDate().withName(group).withFooter("test4").withHeader("test5"));
      }
      app.contact().create(contactDate);
    }

        /*if (!app.contact().isThereAContant()) {
            app.goTo().groupPage();
            if (!app.group().isThereAGroup()) {
                app.group().create(new GroupDate().withName(group).withFooter("test4").withHeader("test5"));
            }
            app.contact().create(contactDate);
        }*/
  }


  @Test
  public void contactsModification() {
    Contacts before = app.db().contacts();
    ContactDate modifyContact = before.iterator().next();
    ContactDate contact = new ContactDate().withId(modifyContact.getId()).withFirstName("Ivan").withMiddleName("Ivanovich").
            withLastName("Ivanov").withHomePhone("987654").withMail("test@mail.ru").
            withMobilePhone("987654").withWorkPhone("123").withAddress("test");
    app.goTo().homePage();
    app.contact().modify(contact);
    assertThat(app.contact().count(), equalTo(before.size()));
    Contacts after = app.db().contacts();
    assertThat(after, equalToObject(before.without(modifyContact).withAdded(contact)));
    verifyContactListInUI();
  }
}