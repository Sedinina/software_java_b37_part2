package ru.stqa.pft.addressbook.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactDate;
import ru.stqa.pft.addressbook.model.GroupDate;

import java.util.List;

public class HbConnectionTest {

  private SessionFactory sessionFactory;

  @BeforeClass
  protected void setUp() throws Exception {
    // A SessionFactory is set up once for an application!
    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure() // configures settings from hibernate.cfg.xml
            .build();
    try {
      sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    } catch (Exception e) {
      e.printStackTrace();
      // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
      // so destroy it manually.
      StandardServiceRegistryBuilder.destroy(registry);
    }
  }


  @Test
  public void testDbConnection() {
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    List<GroupDate> resultGroup = session.createQuery("from GroupDate").list();
    for (GroupDate group : resultGroup) {
      System.out.println(group);
    }

    List<ContactDate> resultContact = session.createQuery("from ContactDate where deprecated = '0000-00-00'").list();
    for (ContactDate contact : resultContact) {
      System.out.println(contact);
      System.out.println(contact.getGroups());
    }
    session.getTransaction().commit();
    session.close();
  }
}