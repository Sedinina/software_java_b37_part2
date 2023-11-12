package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.stqa.pft.addressbook.model.GroupDate;
import ru.stqa.pft.addressbook.model.Groups;

import java.util.List;


public class GroupHelper extends HelperBase {

  public GroupHelper(WebDriver wd) {
    super(wd);
  }

  public void groupsPage() {
    click(By.linkText("groups"));
  }

  public void returnToGroupPage() {
    click(By.linkText("group page"));
  }

  public void submitGroupCreation() {

    click(By.name("submit"));
  }

  public void fillGroupForm(GroupDate groupDate) {
    type("group_name", groupDate.getName());
    type("group_header", groupDate.getHeader());
    type("group_footer", groupDate.getFooter());
  }

  public void initGroupCreation() {
    click(By.name("new"));
  }

  public void deleteSelectedGroups() {
    click(By.name("delete"));
  }


  public void initGroupModification() {
    click(By.name("edit"));
  }

  public void submitGroupModification() {
    click(By.name("update"));
  }

  public void create(GroupDate group) {
    initGroupCreation();
    fillGroupForm(group);
    submitGroupCreation();
    groupCashe = null;
    groupsPage();


  }

  public void modify(GroupDate group) {
    selectGroupById(group.getId());
    initGroupModification();
    fillGroupForm(group);
    submitGroupModification();
    groupCashe = null;
    groupsPage();
  }

  public void delete(GroupDate group) {
    selectGroupById(group.getId());
    deleteSelectedGroups();
    groupCashe = null;
    groupsPage();
  }

  private void selectGroupById(int id) {
    wd.findElement(By.cssSelector("input[value='" + id + "']")).click();
  }


  public boolean isThereAGroup() {
    return isElementPresent(By.name("selected[]"));
  }

  public boolean isThereAGroup(String group) {
    return isElementPresent(By.xpath("//select[@name='new_group']//option[text()='" + group + "']"));
  }

  public int count() {
    return wd.findElements(By.name("selected[]")).size();
  }

  private Groups groupCashe = null;


  public Groups all() {
    if (groupCashe != null) {
      return new Groups(groupCashe);
    }
    groupCashe = new Groups();
    List<WebElement> elements = wd.findElements(By.cssSelector("span.group"));
    for (WebElement element : elements) {
      int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("value"));
      String name = element.getText();
      groupCashe.add(new GroupDate().withId(id).withName(name));
    }
    return new Groups(groupCashe);
  }


}
