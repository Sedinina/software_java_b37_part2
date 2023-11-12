package ru.stqa.pft.addressbook.appmanager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.stqa.pft.addressbook.model.ContactDate;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupDate;
import ru.stqa.pft.addressbook.model.Groups;

import java.util.List;

public class DbHelper {

  private final SessionFactory sessionFactory;

  public DbHelper() {
    // A SessionFactory is set up once for an application!
    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure() // configures settings from hibernate.cfg.xml
            .build();
    sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
  }

  public Groups groups() {
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    List<GroupDate> resultGroup = session.createQuery("from GroupDate").list();
    for (GroupDate group : resultGroup) {
      System.out.println(group);
    }
    session.getTransaction().commit();
    session.close();
    return new Groups(resultGroup);
  }

  public Contacts contacts() {
    Session session = sessionFactory.openSession();
    session.beginTransaction();

    List<ContactDate> resultContact = session.createQuery("from ContactDate where deprecated = '0000-00-00'").list();
    for (ContactDate contact : resultContact) {
      System.out.println(contact);
    }
    session.getTransaction().commit();
    session.close();
    return new Contacts(resultContact);
  }

  public Contacts getContactById(int contactId) {
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    List<ContactDate> result = session.createQuery("from ContactDate where id = " + contactId).list();
    session.getTransaction().commit();
    session.close();
    return new Contacts(result);
  }

  public Groups getGroupById(int groupId) {
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    List<GroupDate> result = session.createQuery("from GroupDate where id = " + groupId).list();
    session.getTransaction().commit();
    session.close();
    return new Groups(result);
  }


}
