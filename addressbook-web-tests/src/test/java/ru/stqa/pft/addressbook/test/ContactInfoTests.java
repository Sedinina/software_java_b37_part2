package ru.stqa.pft.addressbook.test;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactDate;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class ContactInfoTests extends TestBase {


  @Test
  public void testContactInfo() {
    app.goTo().homePage();
    ContactDate contact = app.contact().all().iterator().next();
    ContactDate contactInfoFromEditForm = app.contact().infoFromEditForm(contact);

    assertThat(contact.getAllPhone(), equalTo(mergePhone(contactInfoFromEditForm)));
    assertThat(contact.getAllMail(), equalTo(mergeMail(contactInfoFromEditForm)));
    assertThat(contact.getAddress(), equalTo(contactInfoFromEditForm.getAddress()));
  }

  private String mergeMail(ContactDate contact) {
    return Arrays.asList(contact.getMail(), contact.getMailTwo(), contact.getMailThree())
            .stream().filter((s) -> !s.equals(""))
            .collect(Collectors.joining("\n"));
  }


  private String mergePhone(ContactDate contact) {
    return Arrays.asList(contact.getHomePhone(), contact.getMobilePhone(), contact.getWorkPhone())
            .stream().filter((s) -> !s.equals(""))
            .map(ContactInfoTests::cleaned)
            .collect(Collectors.joining("\n"));

  }

  public static String cleaned(String phone) {
    return phone.replaceAll("\\s", "").replaceAll("[-()]", "");
  }
}