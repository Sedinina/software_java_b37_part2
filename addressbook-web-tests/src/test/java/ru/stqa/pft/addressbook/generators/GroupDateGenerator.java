package ru.stqa.pft.addressbook.generators;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import ru.stqa.pft.addressbook.model.GroupDate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class GroupDateGenerator {

  @Parameter(names = "-c", description = "Group count")
  public int count;
  @Parameter(names = "-f", description = "Target file")
  public String file;

  @Parameter(names = "-d", description = "Data format")
  public String format;

  public static void main(String[] args) throws IOException {
    GroupDateGenerator generate = new GroupDateGenerator();
    JCommander jCommander = new JCommander(generate);
    try {
      jCommander.parse(args);
    } catch (ParameterException ex) {
      jCommander.usage();
      return;
    }
    generate.run();
  }

  private void run() throws IOException {
    List<GroupDate> groups = generateGroups(count);
    if (format.equals("csv")) {
      saveAsCsv(groups, new File(file));
    } else if (format.equals("xml")) {
      saveAsXml(groups, new File(file));
    } else if (format.equals("json")) {
      saveAsJson(groups, new File(file));
    } else {
      System.out.println("Unrecognized format " + format);
    }

  }

  private void saveAsJson(List<GroupDate> groups, File file) throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    String json = gson.toJson(groups);
    try (Writer writer = new FileWriter(file)) {
      writer.write(json);
    }
  }

  private void saveAsXml(List<GroupDate> groups, File file) throws IOException {
    XStream xstream = new XStream();
    xstream.processAnnotations(GroupDate.class);
    String xml = xstream.toXML(groups);
    try (Writer writer = new FileWriter(file)) {
      writer.write(xml);
    }
  }


  private void saveAsCsv(List<GroupDate> groups, File file) throws IOException {
    try (Writer writer = new FileWriter(file)) {
      for (GroupDate group : groups) {
        writer.write(String.format("%s;%s;%s\n", group.getName(), group.getFooter(), group.getHeader()));
      }
    }
  }


  private List<GroupDate> generateGroups(int count) {
    List<GroupDate> groups = new ArrayList<GroupDate>();
    for (int i = 0; i < count; i++) {
      groups.add(new GroupDate().withName(String.format("test %s", i))
              .withHeader(String.format("header %s", i)).withFooter(String.format("footer %s", i)));
    }
    return groups;
  }

}

