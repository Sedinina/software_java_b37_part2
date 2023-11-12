package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SessionHelper extends HelperBase {

  public SessionHelper(WebDriver wd) {
    super(wd);
  }

  public void login(String username, String password) {
    type("user", username);
    /*wd.findElement(By.id("LoginForm")).click();*/
    type("pass", password);
    click(By.xpath("//input[@value='Login']"));
  }


}
