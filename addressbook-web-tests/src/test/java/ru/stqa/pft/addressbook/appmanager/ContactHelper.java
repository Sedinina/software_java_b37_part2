package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactDate;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupDate;

import java.util.List;
import java.util.Set;

public class ContactHelper extends HelperBase {

  public ContactHelper(WebDriver wd) {
    super(wd);
  }

  public void fill(ContactDate contactDate, boolean creation) {

    type("firstname", contactDate.getFirstName());
    type("middlename", contactDate.getMiddleName());
    type("lastname", contactDate.getLastName());
    type("address", contactDate.getAddress());
    type("home", contactDate.getHomePhone());
    type("mobile", contactDate.getMobilePhone());
    type("work", contactDate.getWorkPhone());
    type("email", contactDate.getMail());

    if (creation) {
      if (contactDate.getGroups().size() > 0) {
        Assert.assertTrue(contactDate.getGroups().size() == 1);
        new Select(wd.findElement(By.name("new_group"))).selectByVisibleText(contactDate.getGroups().iterator().next().getName());
      }
    } else {
      Assert.assertFalse(isElementPresent(By.name("new_group")));
    }

  }

  public void homePage() {
    click(By.linkText("home"));
  }

  public void create(ContactDate contact) {
    initNewContact();
    fill(contact, true);
    pushEnter();
    contactCashe = null;
    homePage();
  }

  public void modify(ContactDate contact) {
    editContactById(contact.getId());
    fill(contact, false);
    submitContactModification();
    contactCashe = null;
    homePage();
  }

  public void delete(ContactDate contact) {
    selectContactById(contact.getId());
    deleteSelectedContacts();
    contactCashe = null;
    homePage();
  }


  public void addContactToGroup(int contactId, int groupId) {
    selectContactById(contactId);
    selectGroupById(groupId);
    clickByAddTo();
  }

  public void selectGroupById(int groupId) {
    click(By.xpath("(//select[@name='to_group']/option[@value='" + groupId + "'])"));
  }

  public void clickByAddTo() {
    click(By.xpath("(//input[@name='add'])"));
  }


  public void filterByGroup(int groupId) {
    click(By.xpath("(//select[@name='group']/option[@value='" + groupId + "'])"));
  }

  public void removeContactFromGroup(int contactId, int groupId) {
    filterByGroup(groupId);
    selectContactById(contactId);
    removeFromGroup();
  }


  public void removeFromGroup() {
    click(By.xpath("(//input[@name='remove'])"));
  }

  public void initNewContact() {
    click(By.linkText("add new"));
  }

  public void pushEnter() {
    click(By.xpath("//div[@id='content']/form/input[21]"));
  }


  public void selectContactById(int id) {
    wd.findElement(By.cssSelector("input[value='" + id + "']")).click();
  }


  public void deleteSelectedContacts() {
    click(By.xpath("//input[@value='Delete']"));
    wd.switchTo().alert().accept();
  }

  public ContactDate infoFromEditForm(ContactDate contact) {
    editContactById(contact.getId());
    String firstname = wd.findElement(By.name("firstname")).getAttribute("value");
    String lastname = wd.findElement(By.name("lastname")).getAttribute("value");
    String home = wd.findElement(By.name("home")).getAttribute("value");
    String mobile = wd.findElement(By.name("mobile")).getAttribute("value");
    String work = wd.findElement(By.name("work")).getAttribute("value");
    String mail = wd.findElement(By.name("email")).getAttribute("value");
    String mailTwo = wd.findElement(By.name("email2")).getAttribute("value");
    String mailThree = wd.findElement(By.name("email3")).getAttribute("value");
    String address = wd.findElement(By.name("address")).getAttribute("value");
    wd.navigate().back();
    return new ContactDate().withId(contact.getId()).withFirstName(firstname).withLastName(lastname).
            withHomePhone(home).withMobilePhone(mobile).withWorkPhone(work)
            .withMail(mail).withMailTwo(mailTwo).withMailThree(mailThree).withAddress(address);
  }


  public void editContactById(int id) {
    wd.findElement(By.cssSelector(String.format("a[href='edit.php?id=%s']", id))).click();

  }

  public void submitContactModification() {
    click(By.name("update"));
  }


  public int count() {
    return wd.findElements(By.name("selected[]")).size();
  }

  private Contacts contactCashe = null;

  public Contacts all() {
    if (contactCashe != null) {
      return new Contacts(contactCashe);
    }

    contactCashe = new Contacts();

    List<WebElement> rows = wd.findElements(By.name("entry"));
    for (WebElement row : rows) {

      List<WebElement> td = row.findElements(By.tagName("td"));
      String value = row.findElement(By.tagName("input")).getAttribute("value");
      int id = Integer.parseInt(value);
      String lastname = td.get(1).getText();
      String firstname = td.get(2).getText();
      String address = td.get(3).getText();
      String allMail = td.get(4).getText();
      String allPhone = td.get(5).getText();

      contactCashe.add(new ContactDate().withId(id).withFirstName(firstname).
              withMiddleName("name2").withLastName(lastname)
              .withAllPhone(allPhone).withAllMail(allMail).withAddress(address));
    }
    return contactCashe;
  }

  public ContactDate findContactWithGroup(Contacts contacts) {
    for (ContactDate contact : contacts) {
      Set<GroupDate> contInGroup = contact.getGroups();
      if (contInGroup.size() > 0) {
        return contact;
      }
    }
    return null;
  }

  public ContactDate findContactWithoutGroup(Contacts contacts) {
    for (ContactDate contact : contacts) {
      Set<GroupDate> contInGroup = contact.getGroups();
      if (contInGroup.size() == 0) {
        return contact;
      }
    }
    return null;
  }

  public void selectAllGroup() {
    click(By.xpath("(//select[@name='group']/option[text()='[all]'])"));
  }


}
