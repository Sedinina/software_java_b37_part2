package ru.stqa.pft.addressbook.test;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactDate;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupDate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.AssertJUnit.assertEquals;

public class ContactDeletionTest extends TestBase {

  @BeforeMethod
  public void ensurePrecondition() {
    if (app.db().contacts().size() == 0) {
      app.contact().homePage();
      app.contact().create(new ContactDate().withFirstName("Ivan").withLastName("Ivanov").withHomePhone("987654").withMail("test@mail.ru"));
    }

        /*if (!app.contact().isThereAContant()) {
            app.goTo().GroupPage();
            if (!app.group().isThereAGroup()) {
                app.group().create(new GroupDate().withName("test11").withHeader("test4").withFooter("test5"));
            }
            app.contact().create(new ContactDate().withFirstName("Ivan").withMiddleName("Ivanovich"). withLastName("Ivanov").withHomePhone("987654").withMail("test@mail.ru").withGroup("test11"));
        }*/
  }

  @Test
  public void contactDeletion() {

    Contacts before = app.db().contacts();
    ContactDate deletedContact = before.iterator().next();
    app.goTo().homePage();
    app.contact().delete(deletedContact);
    assertThat(app.contact().count(), equalTo(before.size() - 1));
    Contacts after = app.db().contacts();
    assertThat(after, equalTo(before.without(deletedContact)));
    verifyContactListInUI();

  }
}