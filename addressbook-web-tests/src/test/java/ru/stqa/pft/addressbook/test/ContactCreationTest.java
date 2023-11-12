package ru.stqa.pft.addressbook.test;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import org.testng.annotations.*;
import ru.stqa.pft.addressbook.model.ContactDate;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupDate;
import ru.stqa.pft.addressbook.model.Groups;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;


public class ContactCreationTest extends TestBase {

  String group = "test1";

  @BeforeMethod
  public void ensurePrecondition() {

    if (app.db().groups().size() == 0) {
      app.goTo().groupPage();
      app.group().create(new GroupDate().withName(group));
    }

       /* app.goTo().groupPage();
        if (!app.group().isThereAGroup(group)){
            app.goTo().groupPage();
            app.group().create(new GroupDate().withName(group));
        }*/
  }

  @DataProvider
  public Iterator<Object[]> validContactsXml() throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/contacts.xml"))) {
      String xml = "";
      String line = reader.readLine();
      while (line != null) {
        xml += line;
        line = reader.readLine();
      }
      XStream xstream = new XStream();
      xstream.processAnnotations(ContactDate.class);
      xstream.allowTypes(new Class[]{ContactDate.class});
      List<ContactDate> contacts = (List<ContactDate>) xstream.fromXML(xml);
      return contacts.stream().map((g) -> new Object[]{g}).collect(Collectors.toList()).iterator();
    }
  }

  @DataProvider
  public Iterator<Object[]> validContactsJson() throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/contacts.json"))) {

      String json = "";
      String line = reader.readLine();
      while (line != null) {
        json += line;
        line = reader.readLine();
      }
      Gson gson = new Gson();
      List<ContactDate> contacts = gson.fromJson(json, new TypeToken<List<ContactDate>>() {
      }.getType());
      return contacts.stream().map((g) -> new Object[]{g}).collect(Collectors.toList()).iterator();
    }
  }


  @Test(dataProvider = "validContactsXml")
  public void contactCreation(ContactDate contact) throws Exception {
    app.goTo().homePage();
    File photo = new File("src/test/resources/stru.png");
    Contacts before = app.db().contacts();
    //ContactDate contact = new ContactDate().withFirstName("Ivan").withMiddleName("Ivanovich"). withLastName("Ivanov").withHomePhone("987654").withMail("test@mail.ru").withPhoto(photo).withGroup(group);
    app.goTo().homePage();
    app.contact().create(contact);
    assertThat(app.contact().count(), equalTo(before.size() + 1));
    Contacts after = app.db().contacts();
    //assertThat(after.size(), equalTo(before.size() + 1));
    assertThat(after, equalTo(before.withAdded(contact.withId
            (after.stream().mapToInt((g) -> g.getId()).max().getAsInt()))));
    verifyContactListInUI();
  }

  @Test
  public void contactBadCreation() throws Exception {
    Groups groups = app.db().groups();
    Contacts before = app.db().contacts();
    ContactDate contact = new ContactDate().withFirstName("I'van").withMiddleName("Ivanovich").withLastName("Ivanov").withHomePhone("987654").withMail("test@mail.ru").inGroup(groups.iterator().next());
    app.goTo().homePage();
    app.contact().create(contact);
    assertThat(app.contact().count(), equalTo(before.size()));
    Contacts after = app.db().contacts();
    assertThat(after, equalTo(before));
    verifyContactListInUI();
  }
}