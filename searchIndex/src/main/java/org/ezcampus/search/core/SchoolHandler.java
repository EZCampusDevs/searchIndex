package org.ezcampus.search.core;

import java.util.List;
import org.ezcampus.search.core.models.request.TermsQuery;
import org.ezcampus.search.hibernate.entity.School;
import org.ezcampus.search.hibernate.entity.Term;
import org.ezcampus.search.hibernate.util.HibernateUtil;
import org.hibernate.Session;


// This File will handle School related requests; Getting terms & CSS configurations, 

public abstract class SchoolHandler {

    public static List<Term> getTerms(TermsQuery requestData) {
            try (Session session = HibernateUtil.getSessionFactory().openSession() )
            {
                        
            String queryString = "FROM School s WHERE s.schoolUniqueValue = :sN";

            School existingSchool = session.createQuery(queryString, School.class)
                    .setParameter("sN", requestData.getSchoolName())
                    .getSingleResult();

            //Get all terms offered from that School
            String queryString1 = "SELECT t FROM Term t WHERE t.school = :existingSchool";

            List<Term> terms = session.createQuery(queryString1, Term.class)
                    .setParameter("existingSchool", existingSchool)
                    .getResultList();


            return terms;
            }
    }
}