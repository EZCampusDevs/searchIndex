package org.ezcampus.search.hibernate;

import java.util.List;

import org.ezcampus.search.hibernate.entity.Student;
import org.ezcampus.search.hibernate.util.SessionUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class App
{
	// https://www.javaguides.net/2018/11/hibernate-hello-world-tutorial.html
	
	public static void main(String[] args)
	{
		Student student = new Student("Ramesh", "Fadatare", "rameshfadatare@javaguides.com");
		Student student1 = new Student("John", "Cena", "john@javaguides.com");
		
		Transaction transaction = null;
		try (Session session = SessionUtil.getSessionFactory().openSession())
		{
			// start a transaction
			transaction = session.beginTransaction();
			// save the student objects
			session.persist(student);
			session.persist(student1);
			// commit transaction
			transaction.commit();
		}
		catch (Exception e)
		{
			if (transaction != null)
			{
				transaction.rollback();
			}
			e.printStackTrace();
		}

		try (Session session = SessionUtil.getSessionFactory().openSession())
		{
			List<Student> students = session.createQuery("from Student", Student.class).list();
			students.forEach(s -> System.out.println(s.getFirstName()));
		}
		catch (Exception e)
		{
			if (transaction != null)
			{
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}
}
