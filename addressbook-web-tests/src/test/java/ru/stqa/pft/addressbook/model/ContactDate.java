package ru.stqa.pft.addressbook.model;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import java.util.HashSet;
import java.util.Objects;
import javax.persistence.*;

import java.io.File;
import java.util.Set;

@XStreamAlias("contacts")
@Entity
@Table(name = "addressbook")
public class ContactDate {
  @XStreamOmitField
  @Id
  @Column(name = "id")
  private int id = Integer.MAX_VALUE;
  @Expose
  @Column(name = "firstname")
  private String firstName;
  @Expose
  @Column(name = "middlename")
  private String middleName;
  @Expose
  @Column(name = "lastname")
  private String lastName;
  @Expose
  @Column(name = "home")
  @Type(type = "text")
  private String homePhone;
  @Expose
  @Column(name = "mobile")
  @Type(type = "text")
  private String mobilePhone;
  @Expose
  @Column(name = "work")
  @Type(type = "text")
  private String workPhone;

  @Expose
  @Column(name = "address")
  @Type(type = "text")
  private String address;
  @Transient //пропускает поле
  private String allPhone;
  @Transient
  private String allMail;
  @Expose
  @Column(name = "email")
  @Type(type = "text")
  private String mail;
  @Transient
  private String mailTwo;
  @Transient
  private String mailThree;
  //private File photo;
  @Column(name = "photo")
  @Type(type = "text")
  private String photo;


  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "address_in_groups", joinColumns = @JoinColumn(name = "id"),
          inverseJoinColumns = @JoinColumn(name = "group_id"))
  private Set<GroupDate> groups = new HashSet<GroupDate>();

  public Groups getGroups() {
    return new Groups(groups);
  }

  public int getId() {
    return id;
  }

  public ContactDate withId(int id) {
    this.id = id;
    return this;
  }

  public ContactDate withFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public ContactDate withMiddleName(String middleName) {
    this.middleName = middleName;
    return this;
  }

  public ContactDate withLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public ContactDate withHomePhone(String phone) {
    this.homePhone = phone;
    return this;
  }

  public ContactDate withMail(String mail) {
    this.mail = mail;
    return this;
  }

  public ContactDate withMobilePhone(String mobile) {
    this.mobilePhone = mobile;
    return this;
  }

  public ContactDate withWorkPhone(String work) {
    this.workPhone = work;
    return this;
  }

  public ContactDate withAddress(String address) {
    this.address = address;
    return this;
  }

  public ContactDate withAllPhone(String allPhone) {
    this.allPhone = allPhone;
    return this;
  }

  public String getAllPhone() {
    return allPhone;
  }


  public ContactDate withAllMail(String allMail) {
    this.allMail = allMail;
    return this;
  }


  public ContactDate withMailThree(String mailThree) {
    this.mailThree = mailThree;
    return this;
  }

  public ContactDate withMailTwo(String mailTwo) {
    this.mailTwo = mailTwo;
    return this;
  }

  public ContactDate withPhoto(File photo) {
    this.photo = photo.getPath();
    return this;
  }


  public File getPhoto() {
    if (photo == null) {
      return null;
    } else {
      return new File(photo);
    }
  }


  public String getFirstName() {
    return firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getHomePhone() {
    return homePhone;
  }

  public String getMail() {
    return mail;
  }

  public String getMailTwo() {
    return mailTwo;
  }

  public String getMailThree() {
    return mailThree;
  }

  public String getAllMail() {
    return allMail;
  }

  public String getMobilePhone() {
    return mobilePhone;
  }

  public String getWorkPhone() {
    return workPhone;
  }

  public String getAddress() {
    return address;
  }


  @Override
  public String toString() {
    return "ContactDate{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", middleName='" + middleName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", homePhone='" + homePhone + '\'' +
            ", mobilePhone='" + mobilePhone + '\'' +
            ", workPhone='" + workPhone + '\'' +
            ", address='" + address + '\'' +
            ", mail='" + mail + '\'' +
            ", groups='" + groups + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ContactDate that = (ContactDate) o;
    return id == that.id && Objects.equals(firstName, that.firstName) && Objects.equals(middleName, that.middleName) &&
            Objects.equals(lastName, that.lastName) && Objects.equals(homePhone, that.homePhone) &&
            Objects.equals(mobilePhone, that.mobilePhone) && Objects.equals(workPhone, that.workPhone)
            && Objects.equals(address, that.address) && Objects.equals(mail, that.mail) && Objects.equals(mail, that.mail)
            && Objects.equals(groups, that.groups);
  }


  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, middleName, lastName, homePhone, mobilePhone, workPhone, address, mail);
  }

  public ContactDate inGroup(GroupDate group) {
    groups.add(group);
    return this;
  }
}
