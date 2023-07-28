package org.ezcampus.search.hibernate.entityDAO;

import java.util.List;

import org.ezcampus.search.core.models.request.SchoolQuery;
import org.ezcampus.search.hibernate.entity.School;
import org.ezcampus.search.hibernate.entity.Term;
import org.ezcampus.search.hibernate.util.HibernateUtil;
import org.hibernate.Session;

public class SchoolDAO
{
	public static List<School> getAllSchools(Session session) 
	{
		return session.createQuery("FROM School s", School.class)
				.getResultList();
	}
	
	public static List<School> getAllSchools() 
	{
		try(Session session = HibernateUtil.getSessionFactory().openSession())
		{
			return getAllSchools(session);	
		}
	}
	
	public static School schoolFromID(Session session, int school_id) 
	{
		return session.createQuery("FROM School s WHERE s.schoolId = :sN", School.class)
				.setParameter("sN", school_id)
				.getSingleResultOrNull();
	}
	
	public static School schoolFromID(int school_id) 
	{
		try(Session session = HibernateUtil.getSessionFactory().openSession())
		{
			return schoolFromID(session, school_id);	
		}
	}
	
	

	
	public static List<Term> getTerms(SchoolQuery requestData)
	{
		try (Session session = HibernateUtil.getSessionFactory().openSession())
		{

			String queryString = "FROM School s WHERE s.schoolUniqueValue = :sN";

			School existingSchool = session.createQuery(queryString, School.class)
					.setParameter("sN", requestData.getSchoolName()).getSingleResult();

			// Get all terms offered from that School
			String queryString1 = "SELECT t FROM Term t WHERE t.school = :existingSchool";

			List<Term> terms = session.createQuery(queryString1, Term.class)
					.setParameter("existingSchool", existingSchool).getResultList();

			return terms;
		}
	}
}
