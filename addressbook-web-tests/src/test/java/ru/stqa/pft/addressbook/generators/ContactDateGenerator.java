package ru.stqa.pft.addressbook.generators;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import ru.stqa.pft.addressbook.model.ContactDate;
import ru.stqa.pft.addressbook.model.GroupDate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ContactDateGenerator {

  @Parameter(names = "-c", description = "Contact count")
  public int count;
  @Parameter(names = "-f", description = "Target file")
  public String file;
  @Parameter(names = "-d", description = "Data format")
  public String format;


  public static void main(String[] args) throws IOException {
    ContactDateGenerator generate = new ContactDateGenerator();
    JCommander jCommander = new JCommander(generate);
    try {
      jCommander.parse(args);
    } catch (ParameterException ex) {
      jCommander.usage();
      return;
    }
    generate.run();
    ;
  }

  private void run() throws IOException {
    List<ContactDate> contacts = generateContact(count);
    if (format.equals("csv")) {
      saveAsCsv(contacts, new File(file));
    } else if (format.equals("xml")) {
      saveAsXml(contacts, new File(file));
    } else if (format.equals("json")) {
      saveAsJson(contacts, new File(file));
    } else {
      System.out.println("Unrecognized format " + format);
    }
  }

  private void saveAsJson(List<ContactDate> contacts, File file) throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    String json = gson.toJson(contacts);
    try (Writer writer = new FileWriter(file)) {
      writer.write(json);
    }

  }

  private void saveAsXml(List<ContactDate> contacts, File file) throws IOException {
    XStream xstream = new XStream();
    xstream.processAnnotations(ContactDate.class);
    String xml = xstream.toXML(contacts);
    try (Writer writer = new FileWriter(file)) {
      writer.write(xml);
    }

  }


  private void saveAsCsv(List<ContactDate> contacts, File file) throws IOException {

    Writer writer = new FileWriter(file);
    for (ContactDate contact : contacts) {
      writer.write(String.format("%s;%s;%s;%s;%s;%s\n", contact.getFirstName(), contact.getMiddleName(), contact.getLastName(),
              contact.getAddress(), contact.getMobilePhone(), contact.getMail()));
    }
    writer.close();
  }


  private List<ContactDate> generateContact(int count) {
    List<ContactDate> contacts = new ArrayList<ContactDate>();
    for (int i = 0; i < count; i++) {

      contacts.add(new ContactDate().withFirstName(String.format("testFirstName %s", i))
              .withMiddleName(String.format("testMiddleName %s", i))
              .withLastName(String.format("testLastName %s", i))
              .withAddress(String.format("testAddress %s", i))
              .withMobilePhone(String.format("testMobilePhone %s", i))
              .withMail(String.format("testEmail1 %s", i))
              .withHomePhone(String.format("testHomePhone %S", i))
              .withWorkPhone(String.format("testWorkPhone %S", i))
              .withMailTwo(String.format("testEmail2 %s", i))
              .withMailThree(String.format("testEmail3 %s", i)));

    }
    return contacts;
  }
}
